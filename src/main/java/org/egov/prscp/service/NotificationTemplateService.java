
package org.egov.prscp.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.prscp.repository.EventManegementRepository;
import org.egov.prscp.repository.GeneratePressNotesRepository;
import org.egov.prscp.repository.NotificationTemplateRepository;
import org.egov.prscp.repository.TenderNoticePublicationRepository;
import org.egov.prscp.util.CommonConstants;
import org.egov.prscp.util.MDMSService;
import org.egov.prscp.web.models.EmailContent;
import org.egov.prscp.web.models.EventDetail;
import org.egov.prscp.web.models.GuestsResend;
import org.egov.prscp.web.models.NotificationTemplate;
import org.egov.prscp.web.models.RequestInfoWrapper;
import org.egov.prscp.web.models.ResponseInfoWrapper;
import org.egov.prscp.web.models.Template;
import org.egov.prscp.web.models.TenderNotice;
import org.egov.tracer.model.CustomException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class NotificationTemplateService {

	private final ObjectMapper objectMapper;

	private NotificationTemplateRepository notificationTemplateRepository;

	private TenderNoticePublicationRepository tenderNoticePublicationRepository;

	private MDMSService mDMSService;

	private EventManegementRepository eventManegementRepository;

	private GeneratePressNotesRepository generatePressNotesRepository;

	@Autowired
	public NotificationTemplateService(ObjectMapper objectMapper, MDMSService mDMSService,
			NotificationTemplateRepository notificationTemplateRepository,
			EventManegementRepository eventManegementRepository,
			TenderNoticePublicationRepository tenderNoticePublicationRepository,
			GeneratePressNotesRepository generatePressNotesRepository) {
		this.objectMapper = objectMapper;
		this.notificationTemplateRepository = notificationTemplateRepository;
		this.tenderNoticePublicationRepository = tenderNoticePublicationRepository;
		this.mDMSService = mDMSService;
		this.eventManegementRepository = eventManegementRepository;
		this.generatePressNotesRepository = generatePressNotesRepository;
	}
	
	/**
	 * Get Template for the given criteria
	 * @param requestInfoWrapper to get single or all templates
	 * @return list of Template
	 */
	public ResponseEntity<ResponseInfoWrapper> getTemplate(RequestInfoWrapper requestInfoWrapper) {
		try {

			Template template = objectMapper.convertValue(requestInfoWrapper.getRequestBody(), Template.class);
			NotificationTemplate notificationTemplate = notificationTemplateRepository.getTemplate(template);
			if (notificationTemplate == null) {
				notificationTemplate = mDMSService.getTemplate(requestInfoWrapper.getRequestInfo(), template);
			}

			if (template != null && notificationTemplate != null
					&& template.getModuleName().equals(CommonConstants.MODULE_NAME_EVENT)) {
				getEventTemplate(notificationTemplate, template);
			} else if (template != null && notificationTemplate != null
					&& template.getModuleName().equals(CommonConstants.MODULE_NAME_PRESSNOTE)) {
				getPressNoteTemplate(notificationTemplate, template);
			} else if (template != null && notificationTemplate != null
					&& template.getModuleName().equals(CommonConstants.MODULE_NAME_TENDER)) {
				getTenderTemplate(notificationTemplate, template);
			} else {
				throw new CustomException(CommonConstants.TEMPLATE_EXCEPTION_CODE, CommonConstants.MODULE_NAME_INVALID);
			}
			return new ResponseEntity(ResponseInfoWrapper.builder()
					.responseInfo(ResponseInfo.builder().status(CommonConstants.SUCCESS).build())
					.responseBody(notificationTemplate).build(), HttpStatus.OK);
		} catch (Exception e) {
			throw new CustomException(CommonConstants.TEMPLATE_EXCEPTION_CODE, e.getMessage());
		}
	}
	
	/**
	 * Get Event Template for the given criteria
	 * @param requestInfoWrapper to get Event template
	 * @return Notification template
	 */
	public NotificationTemplate getEventTemplate(NotificationTemplate notificationTemplate, Template template) {
		try {
			List<EmailContent> emailContent = Arrays
					.asList(objectMapper.readValue(notificationTemplate.getEmailContent(), EmailContent[].class));

			// Get Event Details:
			EventDetail event = EventDetail.builder().eventStatus(CommonConstants.EVENT_STATUS)
					.tenantId(template.getTenantId()).eventDetailUuid(template.getTemplateMappedUuid())
					.moduleCode(template.getModuleCode()).build();

			List<EventDetail> eventDetails = eventManegementRepository.getEvent(event);

			EventDetail detail = null;
			if (!eventDetails.isEmpty())
				detail = eventDetails.get(0);
			if (detail != null) {
				String emailSubject = emailContent.get(0).getEmailSubject();
				String emailBody = emailContent.get(0).getEmailBody();
				String smsTemplate = notificationTemplate.getSmsContent();

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
				String datsse = detail.getStartDate().substring(0, 10);
				LocalDate localDate = LocalDate.parse(datsse);
				String startDate = formatter.format(localDate);

				DateFormat f1 = new SimpleDateFormat("HH:mm"); // HH for hour of the day (0 - 23)
				Date d = f1.parse(detail.getStartTime());
				DateFormat f2 = new SimpleDateFormat("h:mm a");
				String startTime = f2.format(d).toUpperCase();

				emailSubject = emailSubject.replace("[:eventTitle:]", detail.getEventTitle());
				emailBody = emailBody.replace("[:eventTitle:]", detail.getEventTitle());
				emailBody = emailBody.replace("[:eventDate:]", startDate.toUpperCase() + " " + startTime);
				emailBody = emailBody.replace("[:location:]", detail.getEventLocation());
				emailBody = emailBody.replace("[:FacebookLink:]", "Facebook : " + detail.getFacebookUrl());
				emailBody = emailBody.replace("[:TwitterLink:]", "Twitter : " + detail.getTwitterUrl());
				emailBody = emailBody.replace("[:InstagramLink:]", "Instagram : " + detail.getInstagramUrl());
				emailBody = emailBody.replace("[:contactName:]", "GuestName");

				smsTemplate = smsTemplate.replace("[:eventTitle:]", detail.getEventTitle());
				smsTemplate = smsTemplate.replace("[:eventDate:]", startDate + " " + startTime);
				smsTemplate = smsTemplate.replace("[:location:]", detail.getEventLocation());
				smsTemplate = smsTemplate.replace("[:FacebookLink:]", "Facebook : " + detail.getFacebookUrl());
				smsTemplate = smsTemplate.replace("[:TwitterLink:]", "Twitter : " + detail.getTwitterUrl());
				smsTemplate = smsTemplate.replace("[:InstagramLink:]", "Instagram : " + detail.getInstagramUrl());

				JSONObject jsonObject = new JSONObject();
				jsonObject.put("emailSubject", emailSubject);
				jsonObject.put("emailBody", emailBody);

				JSONArray jsonArray = new JSONArray();
				jsonArray.add(jsonObject);

				notificationTemplate.setEmailContent(jsonArray.toJSONString());
				notificationTemplate.setSmsContent(smsTemplate);

				notificationTemplate.setTemplateMappedUuid(template.getTemplateMappedUuid());
				notificationTemplate.setModuleCode(template.getModuleCode());
				notificationTemplate.setTenantId(template.getTenantId());
			}
		} catch (Exception e) {
			throw new CustomException(CommonConstants.TEMPLATE_EXCEPTION_CODE, CommonConstants.TEMPLATE_LOAD);
		}
		return notificationTemplate;
	}
	/**
	 * Get Press Note Template for the given criteria
	 * @param requestInfoWrapper to get Press note template
	 * @return Notification template
	 */
	public NotificationTemplate getPressNoteTemplate(NotificationTemplate notificationTemplate, Template template) {
		try {
			notificationTemplate.setEventDetailUuid(template.getTemplateMappedUuid());
			notificationTemplate.setModuleCode(template.getModuleCode());
			notificationTemplate.setTenantId(template.getTenantId());
			notificationTemplate.setTemplateMappedUuid(template.getTemplateMappedUuid());
			notificationTemplate.setModuleCode(template.getModuleCode());
			notificationTemplate.setTenantId(template.getTenantId());
		} catch (Exception e) {
			throw new CustomException(CommonConstants.TEMPLATE_EXCEPTION_CODE, CommonConstants.TEMPLATE_LOAD);
		}
		return notificationTemplate;
	}
	/**
	 * Get Tender Template for the given criteria
	 * @param requestInfoWrapper to get Tender template
	 * @return Notification template
	 */
	public NotificationTemplate getTenderTemplate(NotificationTemplate notificationTemplate, Template template) {
		try {
			notificationTemplate.setTemplateMappedUuid(template.getTemplateMappedUuid());
			notificationTemplate.setModuleCode(template.getModuleCode());
			notificationTemplate.setTenantId(template.getTenantId());

			List<EmailContent> emailContent = Arrays
					.asList(objectMapper.readValue(notificationTemplate.getEmailContent(), EmailContent[].class));

			TenderNotice tender = TenderNotice.builder().tenderNoticeStatus(CommonConstants.EVENT_STATUS)
					.tenantId(template.getTenantId()).tenderNoticeUuid(template.getTemplateMappedUuid())
					.moduleCode(template.getModuleCode()).build();

			List<TenderNotice> pressTenderList = tenderNoticePublicationRepository.getTender(tender);
			if (!pressTenderList.isEmpty()) {
				TenderNotice tenderNoticeDetail = pressTenderList.get(0);
				if (!emailContent.isEmpty() && tenderNoticeDetail != null) {

					String emailSubject = emailContent.get(0).getEmailSubject();
					String emailBody = emailContent.get(0).getEmailBody();
					String smsTemplate = notificationTemplate.getSmsContent();

					emailSubject = emailSubject.replace("[:tenderNotice:]", tenderNoticeDetail.getNoteContent());
					emailBody = emailBody.replace("[:tenderNotice:]", tenderNoticeDetail.getNoteContent());
					smsTemplate = smsTemplate.replace("[:tenderNotice:]", tenderNoticeDetail.getNoteContent());

					JSONObject jsonObject = new JSONObject();
					jsonObject.put("emailSubject", emailSubject);
					jsonObject.put("emailBody", emailBody);
					JSONArray jsonArray = new JSONArray();
					jsonArray.add(jsonObject);

					notificationTemplate.setEmailContent(jsonArray.toJSONString());
					notificationTemplate.setSmsContent(smsTemplate);

					notificationTemplate.setTemplateMappedUuid(template.getTemplateMappedUuid());
					notificationTemplate.setModuleCode(template.getModuleCode());
					notificationTemplate.setTenantId(template.getTenantId());
				}
			}
		} catch (Exception e) {
			throw new CustomException(CommonConstants.TEMPLATE_EXCEPTION_CODE, CommonConstants.TEMPLATE_LOAD);
		}
		return notificationTemplate;
	}

	/**
     * Re-send event, press note , tender invitation
     * @param requestInfoWrapper for Guest contact details
     * @return Notification to Guest contact details 
     */
	public ResponseEntity<ResponseInfoWrapper> resendInvitation(RequestInfoWrapper requestInfoWrapper) {
		try {
			GuestsResend guestsResend = objectMapper.convertValue(requestInfoWrapper.getRequestBody(),
					GuestsResend.class);

			if (guestsResend != null && guestsResend.getModuleName().equals(CommonConstants.MODULE_NAME_EVENT)) {
				guestsResend = notificationTemplateRepository.resendEvent(guestsResend,
						requestInfoWrapper.getAuditDetails());
			} else if (guestsResend != null
					&& guestsResend.getModuleName().equals(CommonConstants.MODULE_NAME_PRESSNOTE)) {
				guestsResend = notificationTemplateRepository.resendPressNote(guestsResend,
						requestInfoWrapper.getAuditDetails());
			} else if (guestsResend != null
					&& guestsResend.getModuleName().equals(CommonConstants.MODULE_NAME_TENDER)) {
				guestsResend = notificationTemplateRepository.resendTender(guestsResend,
						requestInfoWrapper.getAuditDetails());
			} else {
				throw new CustomException(CommonConstants.TEMPLATE_EXCEPTION_CODE, CommonConstants.MODULE_NAME_INVALID);
			}
			return new ResponseEntity(ResponseInfoWrapper.builder()
					.responseInfo(ResponseInfo.builder().status(CommonConstants.SUCCESS).build())
					.responseBody(guestsResend).build(), HttpStatus.OK);
		} catch (Exception e) {
			throw new CustomException(CommonConstants.TEMPLATE_EXCEPTION_CODE, e.getMessage());
		}
	}
}