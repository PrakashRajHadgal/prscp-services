package org.egov.prscp.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.contract.request.RequestInfo;
import org.egov.mdms.model.MasterDetail;
import org.egov.mdms.model.MdmsCriteria;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.mdms.model.ModuleDetail;
import org.egov.prscp.config.PrScpConfiguration;
import org.egov.prscp.web.models.NotificationTemplate;
import org.egov.prscp.web.models.Template;
import org.egov.tracer.model.CustomException;
import org.egov.tracer.model.ServiceCallException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MDMSService {

	private PrScpConfiguration config;
	private RestTemplate restTemplate;

	@Autowired
	public MDMSService(PrScpConfiguration config, RestTemplate restTemplate) {
		this.config = config;
		this.restTemplate = restTemplate;
	}
	
	/**
	 * Get mdms criteria request 
	 * @param Requestinfo and tenant id
	 * @return Mdms criteria request 
	 */
	private MdmsCriteriaReq getMDMSRequest(RequestInfo requestInfo, String tenantId) {

		List<MasterDetail> fyMasterDetails = new ArrayList<>();
		fyMasterDetails.add(MasterDetail.builder().name(PrScpConstants.MDM_TEMPLATE_MODULE).build());

		ModuleDetail fyModuleDtls = ModuleDetail.builder().masterDetails(fyMasterDetails)
				.moduleName(PrScpConstants.MDM_TEMPLATE_MASTER).build();

		List<ModuleDetail> moduleDetails = new ArrayList<>();
		moduleDetails.add(fyModuleDtls);
		MdmsCriteria mdmsCriteria = MdmsCriteria.builder().moduleDetails(moduleDetails).tenantId(tenantId).build();

		return MdmsCriteriaReq.builder().requestInfo(requestInfo).mdmsCriteria(mdmsCriteria).build();
	}

	/**
	 * Get notification template from mdms 
	 * @param RequestInfo and template
	 * @return Notification template
	 */
	public NotificationTemplate getTemplate(RequestInfo requestInfo, Template template) {
		String tenantId=template.getTenantId();
		Object mdmsData = mDMSCall(requestInfo, tenantId.split("\\.")[0]);

		HashMap<String, Object> calculationType = new HashMap<>();
		try {

			LinkedHashMap tempData = JsonPath.read(mdmsData, PrScpConstants.MDMS_TEMPLATE_PATH);
			if (tempData.size() == 0)
				return NotificationTemplate.builder().build();
			List jsonOutput = JsonPath.read(mdmsData, PrScpConstants.MDMS_TEMPLATE_MASTER_PATH);

			for (Object entry : jsonOutput) {
				HashMap<String, Object> map = (HashMap<String, Object>) entry;
				//
				String code = ((String) map.get("code"));
				String moduleCode = ((String) map.get("moduleCode"));
				if (code.equalsIgnoreCase(template.getTemplateType())
						&& moduleCode.equalsIgnoreCase(template.getModuleCode())) {
					NotificationTemplate notificationTemplate = NotificationTemplate.builder().build();
					String smsContent = ((String) map.get("smsContent"));
					String emailBody = ((String) map.get("emailBody"));
					String emailSubjectContent = ((String) map.get("emailSubject"));
					JSONArray emailContent = new JSONArray();
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("emailSubject", emailSubjectContent);
					jsonObject.put("emailBody", emailBody);
					emailContent.add(jsonObject);

					notificationTemplate.setSmsContent(smsContent);
					notificationTemplate.setEmailContent(emailContent.toJSONString());
					notificationTemplate.setTenantId(template.getTenantId());
					notificationTemplate.setModuleCode(template.getModuleCode());

					return notificationTemplate;
				}
			}

		} catch (Exception e) {
			throw new CustomException("MDMS ERROR", "Failed to get Template");
		}

		return NotificationTemplate.builder().build();
	}
	/**
	 * Get result from mdms 
	 * @param RequestInfo and tenantId
	 * @return Mdms reponse data 
	 */
	public Object mDMSCall(RequestInfo requestInfo, String tenantId) {
		MdmsCriteriaReq mdmsCriteriaReq = getMDMSRequest(requestInfo, tenantId);
		StringBuilder url = getMdmsSearchUrl();
		return fetchResult(url, mdmsCriteriaReq);
	}

	/**
	 * Get result from mdms 
	 * @param Request and uri
	 * @return Mdms reponse data 
	 */
	public Object fetchResult(StringBuilder uri, Object request) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		Object response = null;
		log.info("URI: " + uri.toString());
		try {
			log.info("Request: " + mapper.writeValueAsString(request));
			response = restTemplate.postForObject(uri.toString(), request, Map.class);
		} catch (HttpClientErrorException e) {
			log.error("External Service threw an Exception: ", e);
			throw new ServiceCallException(e.getResponseBodyAsString());
		} catch (Exception e) {
			log.error("Exception while fetching from searcher: ", e);
		}
		return response;
	}

	private StringBuilder getMdmsSearchUrl() {
		return new StringBuilder().append(config.getMdmsHost()).append(config.getMdmsEndPoint());
	}

}
