
package org.egov.prscp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.prscp.config.PrScpConfiguration;
import org.egov.prscp.repository.GeneratePressNotesRepository;
import org.egov.prscp.util.CommonConstants;
import org.egov.prscp.util.DeviceSource;
import org.egov.prscp.web.models.NotificationReceiver;
import org.egov.prscp.web.models.NotificationTemplate;
import org.egov.prscp.web.models.PressNote;
import org.egov.prscp.web.models.PressNoteMap;
import org.egov.prscp.web.models.PublicationList;
import org.egov.prscp.web.models.RequestInfoWrapper;
import org.egov.prscp.web.models.ResponseInfoWrapper;
import org.egov.tracer.model.CustomException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GeneratePressNotesService {
	private GeneratePressNotesRepository repository;

	private final ObjectMapper objectMapper;
	private DeviceSource deviceSource;

	@Autowired
	public GeneratePressNotesService(ObjectMapper objectMapper, GeneratePressNotesRepository repository,
			DeviceSource deviceSource, PrScpConfiguration config) {
		this.objectMapper = objectMapper;
		this.deviceSource = deviceSource;
		this.repository = repository;
	}

	/**
	 * Generates press note for the given for the given request
	 * 
	 * @param requestInfoWrapper
	 *            to generate press note
	 * @return The object of generated press note
	 */

	public ResponseEntity<ResponseInfoWrapper> createPressNote(RequestInfoWrapper requestInfoWrapper,
			String requestHeader) {
		try {
			PressNote pressNote = objectMapper.convertValue(requestInfoWrapper.getRequestBody(), PressNote.class);
			repository.checkPressNote(pressNote);
			String sourceUuid = deviceSource.saveDeviceDetails(requestHeader, CommonConstants.DEVICE_PRESSNOTE,
					pressNote.getTenantId(), pressNote.getModuleCode(), requestInfoWrapper.getAuditDetails());
			pressNote.setSourceUuid(sourceUuid);
			pressNote.setCreatedBy(requestInfoWrapper.getAuditDetails().getCreatedBy());
			pressNote.setCreatedTime(requestInfoWrapper.getAuditDetails().getCreatedTime());
			String templateid = UUID.randomUUID().toString();
			String pressid = UUID.randomUUID().toString();

			JSONArray docsListTemplet = pressNote.getDocumentAttachment();
			if (pressNote.getNoteDocument() != null && !pressNote.getNoteDocument().isEmpty()) {
				for (int i = 0; i < pressNote.getNoteDocument().size(); i++) {
					JSONObject jsonObject = (JSONObject) pressNote.getNoteDocument().get(i);
					docsListTemplet.add(jsonObject);
				}
			}

			// save notification_Template
			NotificationTemplate notify = NotificationTemplate.builder().build();
			notify.setNotificationTemplateUuid(templateid);
			notify.setTemplateMappedUuid(pressid);
			notify.setTenantId(pressNote.getTenantId());
			notify.setEmailContent(pressNote.getEmailContent().toJSONString());
			notify.setSmsContent(pressNote.getSmsContent());
			notify.setTemplateType(pressNote.getTemplateType());
			notify.setCreatedBy(pressNote.getCreatedBy());
			notify.setCreatedTime(pressNote.getCreatedTime());
			notify.setModuleCode(pressNote.getModuleCode());
			notify.setSetdoc(docsListTemplet.toJSONString());

			// save pressnote
			pressNote.setNotifiactionTemplateUuid(templateid);
			pressNote.setPressNoteUuid(pressid);
			pressNote.setActive(true);

			// save press_note_map
			ArrayList<PressNoteMap> maplist = new ArrayList<>();
			for (PublicationList mapobj : pressNote.getPublicationList()) {

				PressNoteMap map = new PressNoteMap();
				map.setMapPressNoteUuid(UUID.randomUUID().toString());
				map.setPressNoteUuid(pressid);
				map.setModuleCode(pressNote.getModuleCode());
				map.setPressMasterUuid(mapobj.getPressMasterUuid());

				map.setTenantId(pressNote.getTenantId());
				map.setIsActive(true);
				map.setCreatedBy(pressNote.getCreatedBy());
				map.setCreatedTime(pressNote.getCreatedTime());
				maplist.add(map);
			}

			NotificationReceiver notificationReceiver = NotificationReceiver.builder()
					.receiverType(CommonConstants.RECIEVER_TYPE_PRESSNOTE).receiverUuid(pressid)
					.tenantId(pressNote.getTenantId()).moduleCode(pressNote.getModuleCode())
					.senderUuid(requestInfoWrapper.getAuditDetails().getCreatedBy()).build();
			repository.sendInvitation(notificationReceiver);

			JSONObject data = new JSONObject();
			data.put("NotificationTemplate", notify);
			data.put("PressNote", pressNote);
			data.put("PressNoteMap", maplist);
			repository.uploadPressNote(data);
			return new ResponseEntity<>(ResponseInfoWrapper.builder()
					.responseInfo(ResponseInfo.builder().status(CommonConstants.SUCCESS).build())
					.responseBody(pressNote).build(), HttpStatus.CREATED);
		} catch (Exception e) {
			throw new CustomException(CommonConstants.PRESSNOTE_EXCEPTION_CODE, e.getMessage());
		}
	}

	/**
	 * Get press note for the given criteria
	 * 
	 * @param requestInfoWrapper
	 *            to get single or all press notes
	 * @return list of Press notes
	 */
	public ResponseEntity<ResponseInfoWrapper> getPressNote(RequestInfoWrapper requestInfoWrapper) {
		try {
			PressNote pressNote = objectMapper.convertValue(requestInfoWrapper.getRequestBody(), PressNote.class);

			List<PressNote> pressNote1 = repository.getPressNote(pressNote);
			return new ResponseEntity<>(ResponseInfoWrapper.builder()
					.responseInfo(ResponseInfo.builder().status(CommonConstants.SUCCESS).build())
					.responseBody(pressNote1).build(), HttpStatus.OK);

		} catch (Exception e) {
			throw new CustomException(CommonConstants.PRESSNOTE_EXCEPTION_CODE, e.getMessage());
		}
	}

	/**
	 * Update press note for the given criteria
	 * 
	 * @param requestInfoWrapper
	 *            to update press note
	 * @return object of Press note
	 */
	public ResponseEntity<ResponseInfoWrapper> updatePressNote(RequestInfoWrapper requestInfoWrapper) {
		try {
			PressNote pressNote = objectMapper.convertValue(requestInfoWrapper.getRequestBody(), PressNote.class);
			// check press note uuid exist or not
			Integer flag = repository.checkpressNote(pressNote);
			if (flag > 0) {
				// update pressnote
				pressNote.setActive(true);
				pressNote.setLastModifiedBy(requestInfoWrapper.getAuditDetails().getLastModifiedBy());
				pressNote.setLastModifiedTime(requestInfoWrapper.getAuditDetails().getLastModifiedTime());
				pressNote.setCreatedTime(requestInfoWrapper.getAuditDetails().getCreatedTime());

				// save notification_Template
				NotificationTemplate notify = NotificationTemplate.builder().build();

				notify.setTemplateMappedUuid(pressNote.getPressNoteUuid());
				notify.setEmailContent(pressNote.getEmailContent().toJSONString());
				notify.setSmsContent(pressNote.getSmsContent());
				notify.setLastModifiedBy(pressNote.getLastModifiedBy());
				notify.setLastModifiedTime(pressNote.getLastModifiedTime());
				notify.setModuleCode(pressNote.getModuleCode());
				notify.setTenantId(pressNote.getTenantId());
				notify.setSetdoc(pressNote.getDocumentAttachment().toJSONString());
				notify.setCreatedTime(requestInfoWrapper.getAuditDetails().getCreatedTime());

				// map to delete
				PressNoteMap map = new PressNoteMap();
				map.setPressNoteUuid(pressNote.getPressNoteUuid());
				map.setModuleCode(pressNote.getModuleCode());
				map.setTenantId(pressNote.getTenantId());
				map.setLastModifiedBy(pressNote.getLastModifiedBy());
				map.setLastModifiedTime(pressNote.getLastModifiedTime());
				map.setCreatedTime(requestInfoWrapper.getAuditDetails().getCreatedTime());
				map.setIsActive(false);

				// save map new records
				ArrayList<PressNoteMap> maplist = new ArrayList<>();
				for (PublicationList mapobj : pressNote.getPublicationList()) {

					PressNoteMap mapobject = new PressNoteMap();
					mapobject.setMapPressNoteUuid(UUID.randomUUID().toString());
					mapobject.setPressNoteUuid(pressNote.getPressNoteUuid());
					mapobject.setModuleCode(pressNote.getModuleCode());
					mapobject.setPressMasterUuid(mapobj.getPressMasterUuid());

					mapobject.setTenantId(pressNote.getTenantId());
					mapobject.setIsActive(true);
					mapobject.setCreatedBy(pressNote.getCreatedBy());
					mapobject.setCreatedTime(pressNote.getCreatedTime());
					maplist.add(mapobject);
				}
				JSONObject data = new JSONObject();
				data.put("NotificationTemplate", notify);
				data.put("PressNote", pressNote);
				data.put("PressNoteMap", map);
				data.put("PressNoteMapList", maplist);
				repository.updatePressNote(data);
				return new ResponseEntity<>(ResponseInfoWrapper.builder()
						.responseInfo(ResponseInfo.builder().status(CommonConstants.SUCCESS).build())
						.responseBody(pressNote).build(), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						ResponseInfoWrapper.builder().responseInfo(ResponseInfo.builder()
								.resMsgId(CommonConstants.INVALIDNOTEID).status(CommonConstants.FAIL).build()).build(),
						HttpStatus.OK);
			}
		} catch (Exception e) {
			throw new CustomException(CommonConstants.PRESSNOTE_EXCEPTION_CODE, e.getMessage());
		}
	}
}