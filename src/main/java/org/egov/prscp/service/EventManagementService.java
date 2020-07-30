
package org.egov.prscp.service;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.prscp.config.PrScpConfiguration;
import org.egov.prscp.repository.EventManegementRepository;
import org.egov.prscp.util.CommonConstants;
import org.egov.prscp.util.DeviceSource;
import org.egov.prscp.util.ErrorConstants;
import org.egov.prscp.util.IdGenRepository;
import org.egov.prscp.web.models.EventDetail;
import org.egov.prscp.web.models.NotificationReceiver;
import org.egov.prscp.web.models.RequestInfoWrapper;
import org.egov.prscp.web.models.ResponseInfoWrapper;
import org.egov.prscp.web.models.Idgen.IdGenerationResponse;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class EventManagementService {

	private EventManegementRepository repository;
	private PrScpConfiguration config;
	private IdGenRepository idgenrepository;
	private ObjectMapper objectMapper;
	private DeviceSource deviceSource;

	@Autowired
	public EventManagementService(EventManegementRepository repository, ObjectMapper objectMapper,
			IdGenRepository idgenrepository, PrScpConfiguration config, DeviceSource deviceSource) {
		this.objectMapper = objectMapper;
		this.repository = repository;
		this.config = config;
		this.idgenrepository = idgenrepository;
		this.deviceSource = deviceSource;
	}
	/**
	 * Get event for the given criteria
	 * @param requestInfoWrapper to get single or all events
	 * @return list of Events
	 */
	public ResponseEntity<ResponseInfoWrapper> getEvent(RequestInfoWrapper requestInfo) {
		try {
			EventDetail eventDetail = objectMapper.convertValue(requestInfo.getRequestBody(), EventDetail.class);
			List<EventDetail> resultData = repository.getEvent(eventDetail);
			return new ResponseEntity<>(ResponseInfoWrapper.builder()
					.responseInfo(ResponseInfo.builder().status(CommonConstants.SUCCESS).build())
					.responseBody(resultData).build(), HttpStatus.OK);
		} catch (Exception e) {
			throw new CustomException(CommonConstants.EVENT_EXCEPTION_CODE,
					CommonConstants.EVENT_EXCEPTION_MESSAGE_GET);
		}
	}

	 /**
     * Create event for the given for the given request
     * @param requestInfoWrapper to create event
     * @return The object of created event
     */
	
	public ResponseEntity<ResponseInfoWrapper> createEvent(RequestInfoWrapper requestInfoWrapper,
			String requestHeader) {

		try {

			EventDetail eventDetail = objectMapper.convertValue(requestInfoWrapper.getRequestBody(), EventDetail.class);
			
			String sourceUuid = deviceSource.saveDeviceDetails(requestHeader, CommonConstants.DEVICE_EVENT,
					eventDetail.getTenantId(), eventDetail.getModuleCode(), requestInfoWrapper.getAuditDetails());
			eventDetail.setSourceUuid(sourceUuid);
			validateEvent(eventDetail);
			repository.checkEventExist(eventDetail);
			eventDetail.setEventDetailUuid(UUID.randomUUID().toString());
			eventDetail.setCreatedBy(requestInfoWrapper.getAuditDetails().getCreatedBy());
			eventDetail.setCreatedTime(requestInfoWrapper.getAuditDetails().getCreatedTime());
			eventDetail.setLastModifiedBy(requestInfoWrapper.getAuditDetails().getLastModifiedBy());
			eventDetail.setLastModifiedTime(requestInfoWrapper.getAuditDetails().getLastModifiedTime());
			eventDetail.setActive(true);
			//idgen service call to genrate event id
			IdGenerationResponse id = idgenrepository.getId(requestInfoWrapper.getRequestInfo(),
					eventDetail.getTenantId(), config.getApplicationNumberIdgenName(),
					config.getApplicationNumberIdgenFormat(), 1);
			if (id.getIdResponses() != null && id.getIdResponses().get(0) != null)
				eventDetail.setEventId(id.getIdResponses().get(0).getId());
			else
				throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR.toString(), CommonConstants.ID_GENERATION);

			eventDetail.setStartDate(eventDetail.getStartDate());
			eventDetail.setEndDate(eventDetail.getEndDate());
			eventDetail.setStartTime((LocalTime.parse(eventDetail.getStartTime())).toString());
			eventDetail.setEndTime((LocalTime.parse(eventDetail.getEndTime())).toString());
			eventDetail.setEventString(eventDetail.getEventImage().toJSONString());

			repository.createEvent(eventDetail); 
			return new ResponseEntity<>(ResponseInfoWrapper.builder()
					.responseInfo(ResponseInfo.builder().status(CommonConstants.SUCCESS).build())
					.responseBody(eventDetail).build(), HttpStatus.CREATED);

		} catch (Exception e) {
			throw new CustomException(CommonConstants.EVENT_EXCEPTION_CODE,
					e.getMessage());
		}
	}
	
	/**
	 * Update event for the given criteria
	 * @param requestInfoWrapper to update event
	 * @return object of event 
	 */

	public ResponseEntity<ResponseInfoWrapper> updateEvent(RequestInfoWrapper requestInfoWrapper) {

		try {

			EventDetail eventDetail = objectMapper.convertValue(requestInfoWrapper.getRequestBody(), EventDetail.class);
			chekcStatus(eventDetail);
			validateEvent(eventDetail);
			eventDetail.setActive(true);
			eventDetail.setLastModifiedBy(requestInfoWrapper.getAuditDetails().getLastModifiedBy());
			eventDetail.setLastModifiedTime(requestInfoWrapper.getAuditDetails().getLastModifiedTime());
			eventDetail.setStartDate(eventDetail.getStartDate());
			eventDetail.setEndDate(eventDetail.getEndDate());
			eventDetail.setStartTime((LocalTime.parse(eventDetail.getStartTime())).toString());
			eventDetail.setEndTime((LocalTime.parse(eventDetail.getEndTime())).toString());
			eventDetail.setEventString(eventDetail.getEventImage().toJSONString());
			eventDetail.setCreatedTime(requestInfoWrapper.getAuditDetails().getCreatedTime());
			
			repository.updateEvent(eventDetail);
			NotificationReceiver notificationReceiver = NotificationReceiver.builder()
					.receiverType(CommonConstants.RECIEVER_TYPE_EVENTUPDATE)
					.receiverUuid(eventDetail.getEventDetailUuid()).tenantId(eventDetail.getTenantId())
					.moduleCode(eventDetail.getModuleCode())
					.senderUuid(requestInfoWrapper.getAuditDetails().getCreatedBy()).build();

			repository.sendEventUpdateNotification(notificationReceiver);
			return new ResponseEntity<>(ResponseInfoWrapper.builder()
					.responseInfo(ResponseInfo.builder().status(CommonConstants.SUCCESS).build())
					.responseBody(eventDetail).build(), HttpStatus.OK);

		} catch (Exception e) {
			throw new CustomException(CommonConstants.EVENT_EXCEPTION_CODE,
					CommonConstants.EVENT_EXCEPTION_MESSAGE_UPDATE);
		}
	}

	
	/**
	 * Update event status for the given criteria
	 * @param requestInfoWrapper to update event status
	 * @return object of event status
	 */
	public ResponseEntity<ResponseInfoWrapper> updateEventStatus(RequestInfoWrapper requestInfoWrapper) {
		try {

			EventDetail eventDetail = objectMapper.convertValue(requestInfoWrapper.getRequestBody(), EventDetail.class);
			chekcStatus(eventDetail);

			eventDetail.setLastModifiedBy(requestInfoWrapper.getAuditDetails().getLastModifiedBy());
			eventDetail.setLastModifiedTime(requestInfoWrapper.getAuditDetails().getLastModifiedTime());
			eventDetail.setCreatedTime(requestInfoWrapper.getAuditDetails().getCreatedTime());
			
			List<EventDetail> listCommittee = Arrays.asList(eventDetail);
			RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().requestBody(listCommittee).build();
			repository.updateEventStatus(infoWrapper);

			NotificationReceiver notificationReceiver = NotificationReceiver.builder().receiverType("CANCELEVENT")
					.receiverUuid(eventDetail.getEventDetailUuid()).tenantId(eventDetail.getTenantId())
					.moduleCode(eventDetail.getModuleCode())
					.senderUuid(requestInfoWrapper.getAuditDetails().getCreatedBy()).build();

			repository.sendEventUpdateNotification(notificationReceiver);

			return new ResponseEntity<>(ResponseInfoWrapper.builder()
					.responseInfo(ResponseInfo.builder().status(CommonConstants.SUCCESS).build())
					.responseBody(eventDetail).build(), HttpStatus.OK);

		} catch (Exception e) {
			throw new CustomException(CommonConstants.EVENT_EXCEPTION_CODE, e.getMessage());
		}
	}
//method to validate request parameter for event
	private void validateEvent(EventDetail eventDetail) {
		Pattern validatePatternDate = Pattern.compile("[0-9]{1,2}(\\/)[0-9]{1,2}(\\/)[0-9]{4}");
		Pattern validatePatternTime = Pattern.compile("(?:[01]\\d|2[0123]):(?:[012345]\\d)");
		Map<String, String> errorMap = new HashMap<>();
		if (eventDetail != null) {

			if (eventDetail.getStartDate() == null
					|| !validatePatternDate.matcher(eventDetail.getStartDate()).matches()) {
				errorMap.put(ErrorConstants.MISSING_OR_INVALID_START_DATE_CODE,
						ErrorConstants.MISSING_OR_INVALID_START_DATE_MESSAGE);
			}

			if (eventDetail.getStartTime() == null
					|| !validatePatternTime.matcher(eventDetail.getStartTime()).matches()) {
				errorMap.put(ErrorConstants.MISSING_OR_INVALID_START_TIME_CODE,
						ErrorConstants.MISSING_OR_INVALID_START_TIME_MESSAGE);
			}

			if (eventDetail.getEndDate() == null || !validatePatternDate.matcher(eventDetail.getEndDate()).matches()) {
				errorMap.put(ErrorConstants.MISSING_OR_INVALID_END_DATE_CODE,
						ErrorConstants.MISSING_OR_INVALID_END_DATE_MESSAGE);
			}

			if (eventDetail.getEndTime() == null || !validatePatternTime.matcher(eventDetail.getEndTime()).matches()) {
				errorMap.put(ErrorConstants.MISSING_OR_INVALID_END_TIME_CODE,
						ErrorConstants.MISSING_OR_INVALID_END_TIME_MESSAGE);
			}

		} else {
			errorMap.put(ErrorConstants.MISSING_OR_INVALID_EVENT_OBJECT,
					ErrorConstants.MISSING_OR_INVALID_EVENT_OBJECT_MESSAGE);
		}
		if (!CollectionUtils.isEmpty(errorMap.keySet())) {
			throw new CustomException(errorMap);
		}
	}

	public void chekcStatus(EventDetail eventDetail) {
		EventDetail eventExisting = EventDetail.builder().tenantId(eventDetail.getTenantId())
				.moduleCode(eventDetail.getModuleCode()).eventDetailUuid(eventDetail.getEventDetailUuid())
				.defaultGrid(false).build();
		List<EventDetail> listExisting = repository.getEvent(eventExisting);
		if (!listExisting.isEmpty() && listExisting.size() == 1) {
			eventExisting = listExisting.get(0);
			if (eventExisting.getStatus().equals("EXPIRED")) {
				throw new CustomException(CommonConstants.EVENT_EXCEPTION_CODE,
						CommonConstants.EVENT_EXCEPTION_EXPIRED);
			}
		} else {
			throw new CustomException(CommonConstants.EVENT_EXCEPTION_CODE, CommonConstants.INVALID_EVENT);
		}
	}
}