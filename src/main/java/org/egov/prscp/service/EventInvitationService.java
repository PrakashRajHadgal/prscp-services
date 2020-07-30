
package org.egov.prscp.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.prscp.repository.EventInvetationRepository;
import org.egov.prscp.util.CommonConstants;
import org.egov.prscp.util.DeviceSource;
import org.egov.prscp.util.FileStoreUtils;
import org.egov.prscp.web.models.EventDetail;
import org.egov.prscp.web.models.Files;
import org.egov.prscp.web.models.Guests;
import org.egov.prscp.web.models.InviteGuest;
import org.egov.prscp.web.models.NotificationReceiver;
import org.egov.prscp.web.models.NotificationTemplate;
import org.egov.prscp.web.models.RequestInfoWrapper;
import org.egov.prscp.web.models.ResponseInfoWrapper;
import org.egov.prscp.web.models.Template;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EventInvitationService {

	private final ObjectMapper objectMapper;

	private EventInvetationRepository repository;

	private DeviceSource deviceSource;

	private FileStoreUtils fileStoreUtils;

	@Autowired
	public EventInvitationService(EventInvetationRepository repository, ObjectMapper objectMapper,
			FileStoreUtils fileStoreUtils, DeviceSource deviceSource) {
		this.objectMapper = objectMapper;
		this.repository = repository;
		this.fileStoreUtils = fileStoreUtils;
		this.deviceSource = deviceSource;
	}

	/**
	 * Upload external user for event
	 * 
	 * @param requestInfoWrapper
	 *            to upload external user
	 * @return Uploaded external user response
	 */
	public ResponseEntity<ResponseInfoWrapper> uplaodExternalGuest(RequestInfoWrapper requestInfoWrapper,
			String requestHeader) throws IOException {
		try {
			InviteGuest guests = objectMapper.convertValue(requestInfoWrapper.getRequestBody(), InviteGuest.class);
			String sourceUuid = deviceSource.saveDeviceDetails(requestHeader, CommonConstants.DEVICE_EXTRENALGUEST,
					guests.getTenantId(), guests.getModuleCode(), requestInfoWrapper.getAuditDetails());

			Files uploadfileId = Files.builder().fileStoreId(guests.getExternalFileStoreId()).build();
			List<Files> attachments = new ArrayList<>();
			attachments.add(uploadfileId);
			String fileUrls = null;
			List<Files> attachmentsUrls = fileStoreUtils.getFiles(guests.getTenantId(), attachments);
			for (Files files : attachmentsUrls) {
				fileUrls = files.getUrl();
			}
			if (fileUrls == null || fileUrls.isEmpty())
				throw new CustomException(CommonConstants.INVITATION_EXCEPTION_CODE,
						"Failed to get file Url from File service");

			int lastIndexOf = fileUrls.lastIndexOf('\\') + 1;
			int lastIndexOf1 = fileUrls.indexOf(".xls") - 1;
			String filename = fileUrls.substring(lastIndexOf, lastIndexOf1).replaceAll(" ", "%20");
			StringBuilder string = new StringBuilder(fileUrls);
			string.replace(lastIndexOf, lastIndexOf1, filename);
			UrlResource fileResource = new UrlResource(string.toString());
			/*
			 * CloseableHttpClient client = HttpClientBuilder.create().build(); HttpGet
			 * request = new HttpGet(fileUrls.replaceAll(" ", "%20"));
			 * request.addHeader("accept", "application/vnd.ms-excel"); HttpResponse
			 * response = client.execute(request); HttpEntity entity = response.getEntity();
			 * int responseCode = response.getStatusLine().getStatusCode(); InputStream
			 * inputStream = entity.getContent();
			 */

			List<InviteGuest> userList = new ArrayList<>();
			HSSFWorkbook workbook = new HSSFWorkbook(fileResource.getInputStream());
			HSSFSheet worksheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = worksheet.iterator();
			rowIterator.next(); // skip the header row

			while (rowIterator.hasNext()) {
				Row nextRow = rowIterator.next();
				Iterator<Cell> cellIterator = nextRow.cellIterator();
				InviteGuest user = InviteGuest.builder().build();

				while (cellIterator.hasNext()) {
					Cell nextCell = cellIterator.next();
					int columnIndex = nextCell.getColumnIndex();
					switch (columnIndex) {
					case 0:
						nextCell.setCellType(Cell.CELL_TYPE_STRING);
						String name = nextCell.getStringCellValue();
						user.setGuestName(name);
						break;
					case 1:
						nextCell.setCellType(Cell.CELL_TYPE_STRING);
						String email = nextCell.getStringCellValue();
						user.setGuestEmail(email);
						break;
					case 2:
						nextCell.setCellType(Cell.CELL_TYPE_STRING);
						String mobile = nextCell.getStringCellValue();
						user.setGuestMobile(mobile);
						break;
					default:
						break;
					}

				}

				List<InviteGuest> isExists = userList.stream()
						.filter(obj -> (obj.getGuestEmail().equals(user.getGuestEmail())
								&& obj.getGuestMobile().equals(user.getGuestMobile())))
						.collect(Collectors.toList());

				if (isExists.isEmpty()) {
					String uuid = UUID.randomUUID().toString();
					user.setEventGuestType(guests.getEventGuestType());
					user.setEventDetailUuid(guests.getEventDetailUuid());
					user.setEventGuestUuid(uuid);
					user.setSentFlag(false);
					user.setActive(true);
					user.setCreatedBy(requestInfoWrapper.getAuditDetails().getCreatedBy());
					user.setCreatedTime(requestInfoWrapper.getAuditDetails().getCreatedTime());
					user.setLastModifiedBy(requestInfoWrapper.getAuditDetails().getLastModifiedBy());
					user.setLastModifiedTime(requestInfoWrapper.getAuditDetails().getLastModifiedTime());
					user.setTenantId(guests.getTenantId());
					user.setModuleCode(guests.getModuleCode());
					user.setSourceUuid(sourceUuid);
					userList.add(user);
				}
			}

			if (!userList.isEmpty()) {
				List<InviteGuest> userListFinal = repository.saveGuest(userList, guests.getTenantId(),
						guests.getEventDetailUuid(), requestInfoWrapper.getAuditDetails().getCreatedBy(),
						guests.getModuleCode());
				return new ResponseEntity(ResponseInfoWrapper.builder()
						.responseInfo(ResponseInfo.builder().status(CommonConstants.SUCCESS).build())
						.responseBody(userListFinal).build(), HttpStatus.CREATED);
			} else {
				throw new CustomException(CommonConstants.INVITATION_EXCEPTION_CODE, CommonConstants.INVALID_FILE);
			}

		} catch (Exception exception) {
			throw new CustomException(CommonConstants.INVITATION_EXCEPTION_CODE, exception.getMessage());
		}
	}

	/**
	 * Send invitation to guest for event
	 * 
	 * @param requestInfoWrapper
	 *            to send invite to guest
	 * @return Invitation response
	 */
	public ResponseEntity<ResponseInfoWrapper> sendInvitations(RequestInfoWrapper requestInfoWrapper) {

		try {

			Template template = objectMapper.convertValue(requestInfoWrapper.getRequestBody(), Template.class);
			String notificationTemplateUuid = this.validateTemplate(template, requestInfoWrapper);

			InviteGuest inviteGuest = InviteGuest.builder().eventDetailUuid(template.getEventDetailUuid())
					.tenantId(template.getTenantId()).sentFlag(true)
					.createdBy(requestInfoWrapper.getAuditDetails().getCreatedBy())
					.createdTime(requestInfoWrapper.getAuditDetails().getCreatedTime())
					.lastModifiedBy(requestInfoWrapper.getAuditDetails().getLastModifiedBy())
					.lastModifiedTime(requestInfoWrapper.getAuditDetails().getLastModifiedTime())
					.notificationTemplateUuid(notificationTemplateUuid).build();

			repository.updateTemplateOfEvent(inviteGuest);
			EventDetail eventDetail = EventDetail.builder().notificationTemplateUuid(notificationTemplateUuid)
					.tenantId(template.getTenantId()).isActive(true).moduleCode(template.getModuleCode())
					.eventDetailUuid(template.getEventDetailUuid())
					.createdTime(requestInfoWrapper.getAuditDetails().getCreatedTime()).build();

			repository.updateEventNotification(eventDetail);

			NotificationReceiver notificationReceiver = NotificationReceiver.builder()
					.receiverType(CommonConstants.RECIEVER_TYPE_EVENT).receiverUuid(template.getEventDetailUuid())
					.tenantId(template.getTenantId()).moduleCode(template.getModuleCode())
					.senderUuid(requestInfoWrapper.getAuditDetails().getCreatedBy()).build();
			repository.sendInvitation(notificationReceiver);

			return new ResponseEntity(
					ResponseInfoWrapper.builder()
							.responseInfo(ResponseInfo.builder().status(CommonConstants.SUCCESS).build()).build(),
					HttpStatus.OK);

		} catch (Exception exception) {
			throw new CustomException(CommonConstants.INVITATION_EXCEPTION_CODE, exception.getMessage());
		}
	}

	/**
	 * Add update event template
	 * 
	 * @param requestInfoWrapper
	 *            to add update template
	 * @return Template uuid
	 */
	public String validateTemplate(Template template, RequestInfoWrapper requestInfoWrapper) {
		String uuid = null;
		NotificationTemplate notitemplate = repository.getTemplate(template);
		if (notitemplate == null) {
			uuid = UUID.randomUUID().toString();
			notitemplate = NotificationTemplate.builder().notificationTemplateUuid(uuid)
					.eventDetailUuid(template.getEventDetailUuid()).smsContent(template.getSmsContent())
					.emailContent(template.getEmailContent().toJSONString()).tenantId(template.getTenantId())
					.templateType(template.getTemplateType()).setdoc(template.getDocumentAttachment().toJSONString())
					.moduleCode(template.getModuleCode()).createdBy(requestInfoWrapper.getAuditDetails().getCreatedBy())
					.createdTime(requestInfoWrapper.getAuditDetails().getCreatedTime())
					.lastModifiedBy(requestInfoWrapper.getAuditDetails().getLastModifiedBy())
					.lastModifiedTime(requestInfoWrapper.getAuditDetails().getLastModifiedTime()).build();
			repository.saveTemplate(notitemplate);
		} else {
			uuid = notitemplate.getNotificationTemplateUuid();
			notitemplate.setSmsContent(template.getSmsContent());
			notitemplate.setSetdoc(template.getDocumentAttachment().toJSONString());
			notitemplate.setEmailContent(template.getEmailContent().toJSONString());
			notitemplate.setLastModifiedBy(requestInfoWrapper.getAuditDetails().getLastModifiedBy());
			notitemplate.setLastModifiedTime(requestInfoWrapper.getAuditDetails().getLastModifiedTime());
			notitemplate.setCreatedTime(requestInfoWrapper.getAuditDetails().getCreatedTime());
			repository.updateTemplate(notitemplate);
		}
		return uuid;
	}

	/**
	 * Add guests for event
	 * 
	 * @param requestInfoWrapper
	 *            to add guests
	 * @return Created guests response
	 */
	public ResponseEntity<ResponseInfoWrapper> addGuest(RequestInfoWrapper requestInfoWrapper, String requestHeader) {
		try {
			Guests guests = objectMapper.convertValue(requestInfoWrapper.getRequestBody(), Guests.class);
			String sourceUuid = deviceSource.saveDeviceDetails(requestHeader, CommonConstants.DEVICE_GUEST,
					guests.getTenantId(), guests.getModuleCode(), requestInfoWrapper.getAuditDetails());

			List<InviteGuest> inviteGuest = guests.getInviteGuest();
			inviteGuest.stream().forEach(element -> {
				String uuid = UUID.randomUUID().toString();
				element.setEventGuestUuid(uuid);
				element.setEventDetailUuid(guests.getEventDetailUuid());
				element.setTenantId(guests.getTenantId());
				element.setModuleCode(guests.getModuleCode());
				element.setActive(true);
				element.setSentFlag(false);
				element.setCreatedBy(requestInfoWrapper.getAuditDetails().getCreatedBy());
				element.setCreatedTime(requestInfoWrapper.getAuditDetails().getCreatedTime());
				element.setLastModifiedBy(requestInfoWrapper.getAuditDetails().getLastModifiedBy());
				element.setLastModifiedTime(requestInfoWrapper.getAuditDetails().getLastModifiedTime());
				element.setSourceUuid(sourceUuid);
			});

			List<InviteGuest> inviteGuestFinal = repository.saveGuest(inviteGuest, guests.getTenantId(),
					guests.getEventDetailUuid(), requestInfoWrapper.getAuditDetails().getCreatedBy(),
					guests.getModuleCode());
			return new ResponseEntity(ResponseInfoWrapper.builder()
					.responseInfo(ResponseInfo.builder().status(CommonConstants.SUCCESS).build())
					.responseBody(inviteGuestFinal).build(), HttpStatus.CREATED);
		} catch (Exception exception) {
			throw new CustomException(CommonConstants.INVITATION_EXCEPTION_CODE, exception.getMessage());
		}
	}

	/**
	 * Delete guest for event
	 * 
	 * @param requestInfoWrapper
	 *            to delete guest
	 * @return Deleted Guest response
	 */
	public ResponseEntity<ResponseInfoWrapper> deleteGuest(RequestInfoWrapper requestInfoWrapper) {
		try {
			Guests guests = objectMapper.convertValue(requestInfoWrapper.getRequestBody(), Guests.class);
			List<InviteGuest> inviteGuest = guests.getInviteGuest();
			inviteGuest.stream().forEach(element -> {
				element.setEventDetailUuid(guests.getEventDetailUuid());
				element.setTenantId(guests.getTenantId());
				element.setModuleCode(guests.getModuleCode());
				element.setActive(false);
				element.setCreatedBy(requestInfoWrapper.getAuditDetails().getCreatedBy());
				element.setCreatedTime(requestInfoWrapper.getAuditDetails().getCreatedTime());
				element.setLastModifiedBy(requestInfoWrapper.getAuditDetails().getCreatedBy());
				element.setLastModifiedTime(requestInfoWrapper.getAuditDetails().getCreatedTime());
			});

			repository.deleteGuest(inviteGuest);
			return new ResponseEntity(ResponseInfoWrapper.builder()
					.responseInfo(ResponseInfo.builder().status(CommonConstants.SUCCESS).build()).responseBody(guests)
					.build(), HttpStatus.OK);
		} catch (Exception exception) {
			throw new CustomException(CommonConstants.INVITATION_EXCEPTION_CODE, exception.getMessage());
		}
	}

	/**
	 * Get guest for event for given criteria
	 * 
	 * @param requestInfoWrapper
	 *            to get guest
	 * @return List of Guest response
	 */
	public ResponseEntity<ResponseInfoWrapper> getGuest(RequestInfoWrapper requestInfoWrapper) {
		try {
			InviteGuest inviteGuest = objectMapper.convertValue(requestInfoWrapper.getRequestBody(), InviteGuest.class);
			List<InviteGuest> inviteGuests = repository.getGuest(inviteGuest);
			return new ResponseEntity(ResponseInfoWrapper.builder()
					.responseInfo(ResponseInfo.builder().status(CommonConstants.SUCCESS).build())
					.responseBody(inviteGuests).build(), HttpStatus.OK);
		} catch (Exception exception) {
			throw new CustomException(CommonConstants.INVITATION_EXCEPTION_CODE, exception.getMessage());
		}
	}

}