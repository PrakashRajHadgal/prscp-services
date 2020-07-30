package org.egov.prscp.service.notification;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.prscp.config.PrScpConfiguration;
import org.egov.prscp.producer.Producer;
import org.egov.prscp.repository.EventInvetationRepository;
import org.egov.prscp.repository.EventManegementRepository;
import org.egov.prscp.util.CommonConstants;
import org.egov.prscp.util.MDMSService;
import org.egov.prscp.web.models.EmailContent;
import org.egov.prscp.web.models.EmailRequest;
import org.egov.prscp.web.models.EventDetail;
import org.egov.prscp.web.models.InviteGuest;
import org.egov.prscp.web.models.NotificationTemplate;
import org.egov.prscp.web.models.SMSRequest;
import org.egov.prscp.web.models.Template;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class EmailSmsEventReminderInvitationService {

	@Value("${prscp.invitation.days.reminder}")
	private String noOfDaysReminder;

	private MDMSService mDMSService;
	private EventManegementRepository eventManegementRepository;
	private EventInvetationRepository eventInvetationRepository;
	private final ObjectMapper objectMapper;
	private Producer producer;
	private PrScpConfiguration config;

	@Autowired
	public EmailSmsEventReminderInvitationService(EventManegementRepository eventManegementRepository,
			EventInvetationRepository eventInvetationRepository, ObjectMapper objectMapper, Producer producer,
			PrScpConfiguration config, MDMSService mDMSService) {
		this.eventManegementRepository = eventManegementRepository;
		this.eventInvetationRepository = eventInvetationRepository;
		this.objectMapper = objectMapper;
		this.producer = producer;
		this.config = config;
		this.mDMSService = mDMSService;
	}
	/**
	 * Send notification for event reminder  
	 * @param list of events
 	 * @return email and sms notification
	 */
	public void reminderInvitation() {
		log.info("Started : Send Notification reminderInvitation()");

		try {
			List<EventDetail> listOfEvent = eventManegementRepository.getEventReminder("PUBLISHED");
			listOfEvent = listOfEvent.stream()
					.filter(object -> (ChronoUnit.DAYS.between(LocalDate.now(),
							LocalDate.parse(object.getStartDate().substring(0, 10)))) == Long
									.parseLong(noOfDaysReminder))
					.collect(Collectors.toList());

			if (!listOfEvent.isEmpty()) {

				for (EventDetail eventDetail : listOfEvent) {

					Template template = Template.builder().tenantId(eventDetail.getTenantId())
							.eventDetailUuid(eventDetail.getEventDetailUuid()).moduleCode(eventDetail.getModuleCode())
							.templateType("CREATE_EVENT_REMINDER").build();
					NotificationTemplate notificationTemplate = mDMSService.getTemplate(RequestInfo.builder().build(),
							template);
					if (notificationTemplate != null) {

						InviteGuest inviteGuests = InviteGuest.builder().moduleCode(eventDetail.getModuleCode())
								.eventDetailUuid(eventDetail.getEventDetailUuid()).tenantId(eventDetail.getTenantId()).sentFlag(true)
								.build();
						List<InviteGuest> guestsList = eventInvetationRepository.getGuestReminder(inviteGuests);

						if (!guestsList.isEmpty()) {
							notificationTemplate.setEventDetailUuid(eventDetail.getEventDetailUuid());
							List<EmailContent> emailContent = Arrays.asList(objectMapper
									.readValue(notificationTemplate.getEmailContent(), EmailContent[].class));
							String smsContent = notificationTemplate.getSmsContent();

							if (!emailContent.isEmpty() && !smsContent.isEmpty()) {

								String emailSubject = emailContent.get(0).getEmailSubject();
								String emailBody = emailContent.get(0).getEmailBody();
								String smsTemplate = notificationTemplate.getSmsContent();

								DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
								String datsse = eventDetail.getStartDate().substring(0, 10);
								LocalDate localDate = LocalDate.parse(datsse);
								String startDate = formatter.format(localDate);

								emailSubject = emailSubject.replace("[:eventTitle:]", eventDetail.getEventTitle());
								emailBody = emailBody.replace("[:eventTitle:]", eventDetail.getEventTitle());
								emailBody = emailBody.replace("[:eventDate:]",
										startDate + " " + eventDetail.getStartTime());
								emailBody = emailBody.replace("[:location:]", eventDetail.getEventLocation());
								emailBody = emailBody.replace("[:FacebookLink:]",
										"Facebook : " + eventDetail.getFacebookUrl());
								emailBody = emailBody.replace("[:TwitterLink:]",
										"Twitter : " + eventDetail.getTwitterUrl());
								emailBody = emailBody.replace("[:InstagramLink:]",
										"Instagram : " + eventDetail.getInstagramUrl());

								smsTemplate = smsTemplate.replace("[:eventTitle:]", eventDetail.getEventTitle());
								smsTemplate = smsTemplate.replace("[:eventDate:]",
										startDate + " " + eventDetail.getStartTime());
								smsTemplate = smsTemplate.replace("[:location:]", eventDetail.getEventLocation());
								smsTemplate = smsTemplate.replace("[:FacebookLink:]",
										"Facebook : " + eventDetail.getFacebookUrl());
								smsTemplate = smsTemplate.replace("[:TwitterLink:]",
										"Twitter : " + eventDetail.getTwitterUrl());
								smsTemplate = smsTemplate.replace("[:InstagramLink:]",
										"Instagram : " + eventDetail.getInstagramUrl());

								for (InviteGuest guest : guestsList) {

									StringBuilder builderEmail = new StringBuilder(
											config.getEmailGuestGreet().replace("[:contactName:]", guest.getGuestName())
													+ "<br\\>");
									builderEmail.append(emailBody);
									StringBuilder builderSms = new StringBuilder(config.getEmailGuestGreet()
											.replace("[:contactName:]", guest.getGuestName()));
									builderSms.append(smsTemplate);

									// Email
									EmailRequest emailRequest = EmailRequest.builder().email(guest.getGuestEmail())
											.subject(emailSubject).body(builderEmail.toString()).isHTML(true)
											// .attachmentUrls(listOfAttachments)
											.build();
									producer.push(config.getNotificationEmailTopic(), emailRequest);

									// SMS
									if (notificationTemplate.getSmsContent() != null) {
										SMSRequest smsRequest = SMSRequest.builder()
												.mobileNumber(guest.getGuestMobile()).message(builderSms.toString())
												.build();
										producer.push(config.getNotificationSmsTopic(), smsRequest);
									}
									log.info("Success : Send Notification reminderInvitation()");
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			log.info("Failed : Send Notification reminderInvitation()");
			log.error("Failed : Send Notification reminderInvitation(): " + e.getMessage());
			throw new CustomException(CommonConstants.NOTIFICATION_EVENT_SEND_REMINDER_EXCEPTION_CODE, e.getMessage());
		}
	}
}
