package org.egov.prscp.service.notification;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.egov.prscp.config.PrScpConfiguration;
import org.egov.prscp.producer.Producer;
import org.egov.prscp.repository.EventInvetationRepository;
import org.egov.prscp.repository.EventManegementRepository;
import org.egov.prscp.repository.GeneratePressNotesRepository;
import org.egov.prscp.repository.TenderNoticePublicationRepository;
import org.egov.prscp.util.CommonConstants;
import org.egov.prscp.util.FileStoreUtils;
import org.egov.prscp.util.MDMSService;
import org.egov.prscp.web.models.ContactDetails;
import org.egov.prscp.web.models.EmailAttachment;
import org.egov.prscp.web.models.EmailContent;
import org.egov.prscp.web.models.EmailRequest;
import org.egov.prscp.web.models.EventDetail;
import org.egov.prscp.web.models.Files;
import org.egov.prscp.web.models.NotificationReceiver;
import org.egov.prscp.web.models.NotificationTemplate;
import org.egov.prscp.web.models.PressNote;
import org.egov.prscp.web.models.RequestInfoWrapper;
import org.egov.prscp.web.models.SMSRequest;
import org.egov.prscp.web.models.Template;
import org.egov.prscp.web.models.TenderNotice;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ResendEmailSmsEventInvitationService {

	private EventInvetationRepository eventInvetationRepository;
	private GeneratePressNotesRepository generatePressNotesRepository;
	private TenderNoticePublicationRepository tenderNoticePublicationRepository;
	private EventManegementRepository eventManegementRepository;

	private final ObjectMapper objectMapper;

	private Producer producer;

	private PrScpConfiguration config;

	private FileStoreUtils fileStoreUtils;

	private MDMSService mdmsService;

	@Autowired
	public ResendEmailSmsEventInvitationService(EventInvetationRepository eventInvetationRepository,
			GeneratePressNotesRepository generatePressNotesRepository,
			TenderNoticePublicationRepository tenderNoticePublicationRepository,
			EventManegementRepository eventManegementRepository, ObjectMapper objectMapper, Producer producer,
			PrScpConfiguration config, FileStoreUtils fileStoreUtils, MDMSService mdmsService) {
		this.eventInvetationRepository = eventInvetationRepository;
		this.generatePressNotesRepository = generatePressNotesRepository;
		this.tenderNoticePublicationRepository = tenderNoticePublicationRepository;
		this.eventManegementRepository = eventManegementRepository;
		this.objectMapper = objectMapper;
		this.producer = producer;
		this.config = config;
		this.fileStoreUtils = fileStoreUtils;
		this.mdmsService = mdmsService;
	}

	/**
	 * Re-send notification for event, press note tender notice
	 * 
	 * @param notificationreceiver
	 *            to send notification
	 * @return email and sms notification
	 */
	public void sendEmailAndSMS(NotificationReceiver notificationReceiver) {

		if (notificationReceiver.getReceiverType() != null && notificationReceiver.getModuleCode() != null
				&& notificationReceiver.getTenantId() != null && notificationReceiver.getReceiverUuid() != null) {
			if (notificationReceiver.getReceiverType().equalsIgnoreCase("EVENT")) {
				sendEventInvitation(notificationReceiver);
			} else if (notificationReceiver.getReceiverType().equalsIgnoreCase("PRESSNOTE")) {
				sendPressNoteNotification(notificationReceiver);
			} else if (notificationReceiver.getReceiverType().equalsIgnoreCase("TENDER")) {
				sendTenderNoticeNotification(notificationReceiver);
			}
		}
	}

	/**
	 * Send notification for event invitation
	 * 
	 * @param notificationreceiver
	 *            to send notification
	 * @return email and sms notification
	 */
	private void sendEventInvitation(NotificationReceiver notificationReceiver) {
		log.info("Started : Send Notification sendEventInvitation(): " + notificationReceiver.toString());
		try {
			Template template = Template.builder().tenantId(notificationReceiver.getTenantId())
					.eventDetailUuid(notificationReceiver.getReceiverUuid())
					.moduleCode(notificationReceiver.getModuleCode()).templateType("CREATE_EVENT").build();
			NotificationTemplate notificationTemplate = eventInvetationRepository.getTemplate(template);

			if (notificationTemplate != null) {
				notificationTemplate.setEventDetailUuid(notificationReceiver.getReceiverUuid());
				List<EmailContent> emailContent = Arrays
						.asList(objectMapper.readValue(notificationTemplate.getEmailContent(), EmailContent[].class));
				List<EmailAttachment> listOfAttachments = new ArrayList<>();
				if (notificationTemplate.getSetdoc() != null && !notificationTemplate.getSetdoc().isEmpty()) {
					List<Files> attachments = Arrays
							.asList(objectMapper.readValue(notificationTemplate.getSetdoc(), Files[].class));
					List<Files> attachmentsUrls = fileStoreUtils.getFiles(notificationReceiver.getTenantId(),
							attachments);
					if (!attachmentsUrls.isEmpty()) {
						listOfAttachments = attachmentsEmail(attachmentsUrls);
					}
				}

				List<ContactDetails> contactDetails = notificationReceiver.getContactDetails();

				// Sending
				if (!emailContent.isEmpty() && !contactDetails.isEmpty()) {

					// Get Event Details:
					EventDetail criteria = EventDetail.builder().eventStatus("PUBLISHED")
							.tenantId(notificationReceiver.getTenantId())
							.eventDetailUuid(notificationReceiver.getReceiverUuid())
							.moduleCode(notificationReceiver.getModuleCode()).build();
					List<EventDetail> eventDetails = eventManegementRepository.getEvent(criteria);

					EventDetail detail = null;
					if (!eventDetails.isEmpty())
						detail = eventDetails.get(0);
					if (detail != null) {
						String emailSubject = (emailContent.isEmpty() ? "" : emailContent.get(0).getEmailSubject());
						String emailBody = (emailContent.isEmpty() ? "" : emailContent.get(0).getEmailBody());
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

						smsTemplate = smsTemplate.replace("[:eventTitle:]", detail.getEventTitle());
						smsTemplate = smsTemplate.replace("[:eventDate:]", startDate + " " + startTime);
						smsTemplate = smsTemplate.replace("[:location:]", detail.getEventLocation());
						smsTemplate = smsTemplate.replace("[:FacebookLink:]", "Facebook : " + detail.getFacebookUrl());
						smsTemplate = smsTemplate.replace("[:TwitterLink:]", "Twitter : " + detail.getTwitterUrl());
						smsTemplate = smsTemplate.replace("[:InstagramLink:]",
								"Instagram : " + detail.getInstagramUrl());

						StringBuilder footerText = new StringBuilder("<html><body>" + emailBody + "<br/><br/>"
								+ config.getNotificationResendTextFooter() + "<body/><html/>");

						emailBody = footerText.toString();

						smsTemplate = smsTemplate + "," + config.getNotificationResendTextFooter();
						for (ContactDetails guest : contactDetails) {
							StringBuilder builderEmail = new StringBuilder(
									config.getEmailGuestGreet().replace("[:contactName:]", guest.getReceiverName())
											+ "<br/><br/>");
							builderEmail.append(emailBody);
							StringBuilder builderSms = new StringBuilder(
									config.getEmailGuestGreet().replace("[:contactName:]", guest.getReceiverName()));
							builderSms.append(smsTemplate);

							// Email
							EmailRequest emailRequest = EmailRequest.builder().email(guest.getReceiverEmail())
									.subject(emailSubject).body(builderEmail.toString()).isHTML(true)
									.attachments(listOfAttachments).build();
							producer.push(config.getNotificationEmailTopic(), emailRequest);

							// SMS
							if (notificationTemplate.getSmsContent() != null) {
								SMSRequest smsRequest = SMSRequest.builder().mobileNumber(guest.getReceiverMobile())
										.message(builderSms.toString()).build();
								producer.push(config.getNotificationSmsTopic(), smsRequest);
							}
						}
						insertResendHistory(contactDetails);
						log.info("Success : Sent Notification sendEventInvitation(): "
								+ notificationReceiver.toString());
					}
				}
			} else {
				log.error("Failed : Sent Notification sendEventInvitation(): No Template found :: "
						+ notificationReceiver.toString());
			}
		} catch (Exception e) {
			log.info("Failed : Send Notification sendEventInvitation(): " + notificationReceiver.toString());
			log.error("Failed : Send Notification sendEventInvitation(): " + e.getMessage());
			throw new CustomException(CommonConstants.NOTIFICATION_EVENT_RESEND_EXCEPTION_CODE, e.getMessage());
		}
	}

	/**
	 * Re Send notification for press note
	 * 
	 * @param notificationreceiver
	 *            to send notification
	 * @return email and sms notification
	 */
	private void sendPressNoteNotification(NotificationReceiver notificationReceiver) {
		log.info("Started : Send Notification sendPressNoteNotification(): " + notificationReceiver.toString());
		try {
			Template template = Template.builder().tenantId(notificationReceiver.getTenantId())
					.eventDetailUuid(notificationReceiver.getReceiverUuid())
					.moduleCode(notificationReceiver.getModuleCode()).templateType("PRESS_RELEASE").build();
			NotificationTemplate notificationTemplate = eventInvetationRepository.getTemplate(template);

			if (notificationTemplate != null) {
				notificationTemplate.setEventDetailUuid(notificationReceiver.getReceiverUuid());
				List<EmailContent> emailContent = Arrays
						.asList(objectMapper.readValue(notificationTemplate.getEmailContent(), EmailContent[].class));
				List<EmailAttachment> listOfAttachments = new ArrayList<>();
				if (notificationTemplate.getSetdoc() != null && !notificationTemplate.getSetdoc().isEmpty()) {
					List<Files> attachments = Arrays
							.asList(objectMapper.readValue(notificationTemplate.getSetdoc(), Files[].class));

					List<Files> attachmentsUrls = fileStoreUtils.getFiles(notificationReceiver.getTenantId(),
							attachments);
					if (!attachmentsUrls.isEmpty()) {
						listOfAttachments = attachmentsEmail(attachmentsUrls);
					}
				}

				List<ContactDetails> contactDetails = notificationReceiver.getContactDetails();
				if (!contactDetails.isEmpty()) {
					PressNote pressNote = PressNote.builder().pressNoteUuid(notificationReceiver.getReceiverUuid())
							.tenantId(notificationReceiver.getTenantId())
							.moduleCode(notificationReceiver.getModuleCode()).build();

					List<PressNote> pressNoteList = generatePressNotesRepository.getPressNote(pressNote);
					if (!pressNoteList.isEmpty()) {
						PressNote pressNoteDetail = pressNoteList.get(0);
						// Sending
						if (!emailContent.isEmpty() && pressNoteDetail != null) {
							String emailSubject = (emailContent.isEmpty() ? "" : emailContent.get(0).getEmailSubject());
							String emailBody = (emailContent.isEmpty() ? "" : emailContent.get(0).getEmailBody());
							String smsTemplate = notificationTemplate.getSmsContent();

							StringBuilder footerText = new StringBuilder("<html><body>" + emailBody + "<br/><br/>"
									+ config.getNotificationResendTextFooter() + "<body/><html/>");

							emailBody = footerText.toString();

							smsTemplate = smsTemplate + "," + config.getNotificationResendTextFooter();

							for (ContactDetails press : contactDetails) {
								StringBuilder builderEmail = new StringBuilder(
										config.getEmailGuestGreet().replace("[:contactName:]", press.getReceiverName())
												+ "<br/><br/>");
								builderEmail.append(emailBody);
								StringBuilder builderSms = new StringBuilder(config.getEmailGuestGreet()
										.replace("[:contactName:]", press.getReceiverName()));
								builderSms.append(smsTemplate);

								// Email
								EmailRequest emailRequest = EmailRequest.builder().email(press.getReceiverEmail())
										.subject(emailSubject).body(builderEmail.toString()).isHTML(true)
										.attachments(listOfAttachments).build();
								producer.push(config.getNotificationEmailTopic(), emailRequest);

								// SMS
								if (notificationTemplate.getSmsContent() != null) {
									SMSRequest smsRequest = SMSRequest.builder().mobileNumber(press.getReceiverMobile())
											.message(builderSms.toString()).build();
									producer.push(config.getNotificationSmsTopic(), smsRequest);
								}
							}
							insertResendHistory(contactDetails);
							log.info("Success : Sent Notification sendPressNoteNotification(): "
									+ notificationReceiver.toString());
						}
					}
				}
			} else {
				log.info("Failed : Sent Notification sendPressNoteNotification(): No Template found :: "
						+ notificationReceiver.toString());
			}
		} catch (Exception e) {
			log.info("Failed : Send Notification sendPressNoteNotification(): " + notificationReceiver.toString());
			log.error("Failed : Send Notification sendPressNoteNotification(): " + e.getMessage());
			throw new CustomException(CommonConstants.NOTIFICATION_PRESSNOTE_RESEND_EXCEPTION_CODE, e.getMessage());
		}
	}

	/**
	 * Re Send notification for tender notice
	 * 
	 * @param notificationreceiver
	 *            to send notification
	 * @return email and sms notification
	 */

	private void sendTenderNoticeNotification(NotificationReceiver notificationReceiver) {
		log.info("Started : Send Notification sendTenderNoticeNotification(): " + notificationReceiver.toString());
		try {
			Template template = Template.builder().tenantId(notificationReceiver.getTenantId())
					.eventDetailUuid(notificationReceiver.getReceiverUuid())
					.moduleCode(notificationReceiver.getModuleCode()).templateType("TENDER_NOTICE").build();
			NotificationTemplate notificationTemplate = eventInvetationRepository.getTemplate(template);

			if (notificationTemplate != null) {
				notificationTemplate.setEventDetailUuid(notificationReceiver.getReceiverUuid());
				List<EmailContent> emailContent = Arrays
						.asList(objectMapper.readValue(notificationTemplate.getEmailContent(), EmailContent[].class));
				List<EmailAttachment> listOfAttachments = new ArrayList<>();
				if (notificationTemplate.getSetdoc() != null && !notificationTemplate.getSetdoc().isEmpty()) {
					List<Files> attachments = Arrays
							.asList(objectMapper.readValue(notificationTemplate.getSetdoc(), Files[].class));

					List<Files> attachmentsUrls = fileStoreUtils.getFiles(notificationReceiver.getTenantId(),
							attachments);
					if (!attachmentsUrls.isEmpty()) {
						listOfAttachments = attachmentsEmail(attachmentsUrls);
					}
				}
				List<ContactDetails> contactDetails = notificationReceiver.getContactDetails();
				if (!contactDetails.isEmpty()) {

					TenderNotice tenderNotice = TenderNotice.builder()
							.tenderNoticeUuid(notificationReceiver.getReceiverUuid()).tenderNoticeStatus("PUBLISHED")
							.tenantId(notificationReceiver.getTenantId())
							.moduleCode(notificationReceiver.getModuleCode()).build();

					List<TenderNotice> pressTenderList = tenderNoticePublicationRepository.getTender(tenderNotice);
					if (!pressTenderList.isEmpty()) {
						TenderNotice tenderNoticeDetail = pressTenderList.get(0);

						// Sending
						if (!emailContent.isEmpty() && tenderNoticeDetail != null) {
							String emailSubject = (emailContent.isEmpty() ? "" : emailContent.get(0).getEmailSubject());
							String emailBody = (emailContent.isEmpty() ? "" : emailContent.get(0).getEmailBody());
							String smsTemplate = notificationTemplate.getSmsContent();

							emailSubject = emailSubject.replace("[:tenderNotice:]",
									tenderNoticeDetail.getNoteContent());

							emailBody = emailBody.replace("[:tenderNotice:]", tenderNoticeDetail.getNoteContent());
							smsTemplate = smsTemplate.replace("[:tenderNotice:]", tenderNoticeDetail.getNoteContent());

							StringBuilder footerText = new StringBuilder("<html><body>" + emailBody + "<br/><br/>"
									+ config.getNotificationResendTextFooter() + "<body/><html/>");

							emailBody = footerText.toString();

							smsTemplate = smsTemplate + "," + config.getNotificationResendTextFooter();

							for (ContactDetails press : contactDetails) {
								StringBuilder builderEmail = new StringBuilder(
										config.getEmailGuestGreet().replace("[:contactName:]", press.getReceiverName())
												+ "<br/><br/>");
								builderEmail.append(emailBody);
								StringBuilder builderSms = new StringBuilder(config.getEmailGuestGreet()
										.replace("[:contactName:]", press.getReceiverName()));
								builderSms.append(smsTemplate);

								// Email
								EmailRequest emailRequest = EmailRequest.builder().email(press.getReceiverEmail())
										.subject(emailSubject).body(builderEmail.toString()).isHTML(true)
										.attachments(listOfAttachments).build();
								producer.push(config.getNotificationEmailTopic(), emailRequest);

								// SMS
								if (notificationTemplate.getSmsContent() != null) {
									SMSRequest smsRequest = SMSRequest.builder().mobileNumber(press.getReceiverMobile())
											.message(builderSms.toString()).build();
									producer.push(config.getNotificationSmsTopic(), smsRequest);
								}
							}
							insertResendHistory(contactDetails);
							log.info("Success : Sent Notification sendTenderNoticeNotification(): "
									+ notificationReceiver.toString());
						}
					}
				}
			} else {
				log.info("Failed : Sent Notification sendTenderNoticeNotification(): No Template found :: "
						+ notificationReceiver.toString());
			}
		} catch (Exception e) {
			log.info("Failed : Send Notification sendTenderNoticeNotification(): " + notificationReceiver.toString());
			log.error("Failed : Send Notification sendTenderNoticeNotification(): " + e.getMessage());
			throw new CustomException(CommonConstants.NOTIFICATION_TENDER_RESEND_EXCEPTION_CODE, e.getMessage());
		}
	}

	void insertResendHistory(List<ContactDetails> contactDetails) {
		RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().requestBody(contactDetails).build();
		producer.push(config.getPersistResendHistoryTopic(), infoWrapper);
	}

	/**
	 * Get email attachments
	 * 
	 * @param filestore
	 *            paths
	 * @return list of email attachments
	 */
	public List<EmailAttachment> attachmentsEmail(List<Files> attachmentsUrls) {
		log.info("Started : Send Notification attachmentsEmail(): " + attachmentsUrls.toString());
		List<EmailAttachment> attachedDocs = new ArrayList<>();
		try {
			for (Files files : attachmentsUrls) {
				String[] urls = files.getUrl().split(",");
				if (urls.length > 0) {
					urls = urls[0].split(";");
					URL url = new URL(urls[0]);
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("HEAD");
					connection.connect();
					// url.get
					String contentType = connection.getContentType();
					UrlResource resource = new UrlResource(urls[0]);
					EmailAttachment attachment = EmailAttachment.builder().mimeType(contentType).url(urls[0])
							.name(resource.getFilename()).build();
					attachedDocs.add(attachment);
				}
			}
			log.info("Success : Send Notification attachmentsEmail(): " + attachmentsUrls.toString());
			return attachedDocs;
		} catch (Exception e) {
			log.info("Failed : Send Notification attachmentsEmail(): " + attachmentsUrls.toString());
			log.error("Failed : Send Notification attachmentsEmail(): " + e.getMessage());
			throw new CustomException(CommonConstants.NOTIFICATION_EMAIL_ATTACHMENTS_EXCEPTION_CODE, e.getMessage());
		}
	}
}
