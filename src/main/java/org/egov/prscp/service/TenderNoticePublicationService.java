
package org.egov.prscp.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.prscp.config.PrScpConfiguration;
import org.egov.prscp.repository.TenderNoticePublicationRepository;
import org.egov.prscp.util.CommonConstants;
import org.egov.prscp.util.DeviceSource;
import org.egov.prscp.util.IDGenUtil;
import org.egov.prscp.util.IdGenRepository;
import org.egov.prscp.web.models.NotificationReceiver;
import org.egov.prscp.web.models.NotificationTemplate;
import org.egov.prscp.web.models.ProcessInstance;
import org.egov.prscp.web.models.ProcessInstanceRequest;
import org.egov.prscp.web.models.RequestInfoWrapper;
import org.egov.prscp.web.models.ResponseInfoWrapper;
import org.egov.prscp.web.models.Template;
import org.egov.prscp.web.models.TenderNotice;
import org.egov.prscp.web.models.Idgen.IdGenerationResponse;
import org.egov.tracer.model.CustomException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TenderNoticePublicationService {
	private TenderNoticePublicationRepository repository;
	private IdGenRepository idgenrepository;
	private PrScpConfiguration config;
	private final ObjectMapper objectMapper;
	private DeviceSource deviceSource;
	private IDGenUtil idgen;

	@Autowired
	public TenderNoticePublicationService(TenderNoticePublicationRepository repository, PrScpConfiguration config,
			ObjectMapper objectMapper, IdGenRepository idgenrepository, DeviceSource deviceSource, IDGenUtil idgen) {
		this.repository = repository;
		this.config = config;
		this.objectMapper = objectMapper;
		this.idgenrepository = idgenrepository;
		this.deviceSource = deviceSource;
		this.idgen = idgen;
	}

	/**
	 * Creates Tender for the given criteria
	 * 
	 * @param requestInfoWrapper
	 *            to create tender
	 * @return Tender Response
	 */
	public ResponseEntity<ResponseInfoWrapper> createTender(RequestInfoWrapper requestInfoWrapper,
			String requestHeader) {
		try {
			TenderNotice tenderNotice = objectMapper.convertValue(requestInfoWrapper.getRequestBody(),
					TenderNotice.class);
			repository.checkTenderExist(tenderNotice);
			String sourceUuid = deviceSource.saveDeviceDetails(requestHeader, CommonConstants.DEVICE_TENDER,
					tenderNotice.getTenantId(), tenderNotice.getModuleCode(), requestInfoWrapper.getAuditDetails());

			tenderNotice.setSourceUuid(sourceUuid);
			tenderNotice.setActive(true);
			tenderNotice.setCreatedBy(requestInfoWrapper.getAuditDetails().getCreatedBy());
			tenderNotice.setCreatedTime(requestInfoWrapper.getAuditDetails().getCreatedTime());
			tenderNotice.setLastModifiedBy(requestInfoWrapper.getAuditDetails().getLastModifiedBy());
			tenderNotice.setLastModifiedTime(requestInfoWrapper.getAuditDetails().getLastModifiedTime());

			String tenderNoticeUuid = UUID.randomUUID().toString();
			tenderNotice.setTenderNoticeUuid(tenderNoticeUuid);
			tenderNotice.setTenderDocumentList(tenderNotice.getTenderDocument().toJSONString());
			// Idgen service call to generate tender id
			IdGenerationResponse id = idgenrepository.getId(requestInfoWrapper.getRequestInfo(),
					tenderNotice.getTenantId(), config.getAppTenderNoticeNumberIdgenName(),
					config.getAppTenderNoticeNumberIdgenFormat(), 1);
			if (id.getIdResponses() != null && id.getIdResponses().get(0) != null)
				tenderNotice.setTenderNoticeId(id.getIdResponses().get(0).getId());
			else
				throw new CustomException(CommonConstants.TENDERNOTICE_EXCEPTION_CODE, CommonConstants.ID_GENERATION);
			// workflow service call to integrate tender workflow
			workflowIntegration(requestInfoWrapper, tenderNotice);
			repository.createTender(tenderNotice);

			return new ResponseEntity<>(ResponseInfoWrapper.builder()
					.responseInfo(ResponseInfo.builder().status(CommonConstants.SUCCESS).build())
					.responseBody(tenderNotice).build(), HttpStatus.CREATED);

		} catch (Exception e) {

			throw new CustomException(CommonConstants.TENDERNOTICE_EXCEPTION_CODE, e.getMessage());
		}
	}

	/**
	 * Update Tender for the given criteria
	 * 
	 * @param requestInfoWrapper
	 *            to update tender
	 * @return Tender Response
	 */
	public ResponseEntity<ResponseInfoWrapper> updateTender(RequestInfoWrapper requestInfoWrapper) {

		try {
			TenderNotice tenderNotice = objectMapper.convertValue(requestInfoWrapper.getRequestBody(),
					TenderNotice.class);
			repository.isValidTenderUuid(tenderNotice);
			tenderNotice.setActive(true);
			tenderNotice.setLastModifiedBy(requestInfoWrapper.getAuditDetails().getLastModifiedBy());
			tenderNotice.setLastModifiedTime(requestInfoWrapper.getAuditDetails().getLastModifiedTime());
			tenderNotice.setTenderDocumentList(tenderNotice.getTenderDocument().toJSONString());
			tenderNotice.setCreatedTime(requestInfoWrapper.getAuditDetails().getCreatedTime());
			repository.updateTender(tenderNotice);
			return new ResponseEntity<>(ResponseInfoWrapper.builder()
					.responseInfo(ResponseInfo.builder().status(CommonConstants.SUCCESS).build())
					.responseBody(tenderNotice).build(), HttpStatus.OK);

		} catch (Exception e) {
			throw new CustomException(CommonConstants.TENDERNOTICE_EXCEPTION_CODE, e.getMessage());
		}
	}

	/**
	 * Get Tender for the given criteria
	 * 
	 * @param requestInfoWrapper
	 *            to get tender
	 * @return Tender Response
	 */
	public ResponseEntity<ResponseInfoWrapper> getTender(RequestInfoWrapper requestInfoWrapper) {
		try {
			TenderNotice tenderNotice = objectMapper.convertValue(requestInfoWrapper.getRequestBody(),
					TenderNotice.class);
			List<TenderNotice> existingPress = repository.getTender(tenderNotice);

			return new ResponseEntity<>(ResponseInfoWrapper.builder()
					.responseInfo(ResponseInfo.builder().status(CommonConstants.SUCCESS).build())
					.responseBody(existingPress).build(), HttpStatus.OK);

		} catch (Exception e) {
			throw new CustomException(CommonConstants.TENDERNOTICE_EXCEPTION_CODE, e.getMessage());
		}
	}

	/**
	 * Publish Tender for the given criteria
	 * 
	 * @param requestInfoWrapper
	 *            to publish tender
	 * @return Tender Response
	 */
	public ResponseEntity<ResponseInfoWrapper> publish(RequestInfoWrapper requestInfoWrapper) {
		try {
			TenderNotice tenderNotice = objectMapper.convertValue(requestInfoWrapper.getRequestBody(),
					TenderNotice.class);
			Template template = objectMapper.convertValue(requestInfoWrapper.getRequestBody(), Template.class);
			List<TenderNotice> existingPress = repository.getTenderDetails(tenderNotice);
			if (!existingPress.isEmpty() && existingPress.size() == 1) {
				TenderNotice tenderExisting = existingPress.get(0);
				repository.isValidTenderUuid(tenderNotice);
				// workflow service call to integrate tender workflow
				workflowIntegration(requestInfoWrapper, tenderNotice);

				String notificationTemplateUuid = UUID.randomUUID().toString();
				tenderNotice.setActive(true);
				tenderNotice.setCreatedBy(requestInfoWrapper.getAuditDetails().getCreatedBy());
				tenderNotice.setCreatedTime(requestInfoWrapper.getAuditDetails().getCreatedTime());
				tenderNotice.setLastModifiedBy(requestInfoWrapper.getAuditDetails().getLastModifiedBy());
				tenderNotice.setLastModifiedTime(requestInfoWrapper.getAuditDetails().getLastModifiedTime());

				tenderNotice.setNotificationTemplateUuid(notificationTemplateUuid);

				JSONArray docsListTemplet = template.getDocumentAttachment();
				if (tenderExisting.getTenderDocument() != null && !tenderExisting.getTenderDocument().isEmpty()) {
					for (int i = 0; i < tenderExisting.getTenderDocument().size(); i++) {
						JSONObject jsonObject = (JSONObject) tenderExisting.getTenderDocument().get(i);
						docsListTemplet.add(jsonObject);
					}
				}

				NotificationTemplate notificationTemplate = NotificationTemplate.builder()
						.tenantId(template.getTenantId()).templateType(template.getTemplateType())
						.moduleCode(template.getModuleCode()).notificationTemplateUuid(notificationTemplateUuid)
						.templateMappedUuid(tenderNotice.getTenderNoticeUuid()).setdoc(docsListTemplet.toJSONString())
						.smsContent(template.getSmsContent()).emailContent(template.getEmailContent().toJSONString())
						.createdBy(requestInfoWrapper.getAuditDetails().getCreatedBy())
						.createdTime(requestInfoWrapper.getAuditDetails().getCreatedTime())
						.lastModifiedBy(requestInfoWrapper.getAuditDetails().getLastModifiedBy())
						.lastModifiedTime(requestInfoWrapper.getAuditDetails().getLastModifiedTime()).build();

				NotificationReceiver notificationReceiver = NotificationReceiver.builder()
						.receiverType(CommonConstants.RECIEVER_TYPE_TENDERNOTICE).tenantId(template.getTenantId())
						.receiverUuid(tenderNotice.getTenderNoticeUuid()).moduleCode(tenderNotice.getModuleCode())
						.senderUuid(requestInfoWrapper.getAuditDetails().getCreatedBy()).build();

				repository.publish(tenderNotice, notificationTemplate, notificationReceiver);

				return new ResponseEntity<>(ResponseInfoWrapper.builder()
						.responseInfo(ResponseInfo.builder().status(CommonConstants.SUCCESS).build())
						.responseBody(tenderNotice).build(), HttpStatus.OK);
			} else {
				throw new CustomException(CommonConstants.TENDERNOTICE_EXCEPTION_CODE,
						CommonConstants.TENDERNOTICE_EXCEPTION_MESSAGE);
			}
		} catch (Exception e) {
			throw new CustomException(CommonConstants.TENDERNOTICE_EXCEPTION_CODE, e.getMessage());
		}
	}

	/**
	 * Method to integrate with workflow
	 *
	 * takes the tender request as parameter constructs the work-flow request
	 *
	 * and sets the resultant status from wf-response back to tender object
	 *
	 * @param tenderNotice
	 */
	private void workflowIntegration(RequestInfoWrapper requestInfoWrapper, TenderNotice tenderNotice) {
		try {
			ProcessInstanceRequest workflowRequest = new ProcessInstanceRequest();
			workflowRequest.setRequestInfo(requestInfoWrapper.getRequestInfo());
			ProcessInstance processInstances = new ProcessInstance();
			processInstances.setTenantId(tenderNotice.getTenantId());
			processInstances.setAction(tenderNotice.getTenderNoticeStatus());
			processInstances.setBusinessId(tenderNotice.getTenderNoticeId());
			processInstances.setModuleName("PRTENDER");
			processInstances.setBusinessService("PRTENDER");
			List<ProcessInstance> processList = Arrays.asList(processInstances);
			workflowRequest.setProcessInstances(processList);
			ResponseInfo workflowResponse = idgen.createWorkflowRequest(workflowRequest);
			if (workflowResponse == null || !workflowResponse.getStatus().equals(CommonConstants.SUCCESSFUL)) {
				throw new CustomException(CommonConstants.TENDERNOTICE_EXCEPTION_CODE,
						(workflowResponse != null ? workflowResponse.getMsgId() : CommonConstants.WORKFLOW_MESSAGE));
			}
		} catch (Exception e) {
			throw new CustomException(CommonConstants.TENDERNOTICE_EXCEPTION_CODE, e.getMessage());
		}
	}
}