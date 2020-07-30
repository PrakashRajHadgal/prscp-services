package org.egov.prscp.service.notification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.prscp.config.PrScpConfiguration;
import org.egov.prscp.producer.Producer;
import org.egov.prscp.repository.EventManegementRepository;
import org.egov.prscp.util.CommonConstants;
import org.egov.prscp.util.MDMSService;
import org.egov.prscp.util.UserUtil;
import org.egov.prscp.web.models.EmailContent;
import org.egov.prscp.web.models.EmailRequest;
import org.egov.prscp.web.models.Employee;
import org.egov.prscp.web.models.EventDetail;
import org.egov.prscp.web.models.Library;
import org.egov.prscp.web.models.NotificationTemplate;
import org.egov.prscp.web.models.SMSRequest;
import org.egov.prscp.web.models.Template;
import org.egov.prscp.web.models.User;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LibraryNotificationService {

	private EventManegementRepository eventManegementRepository;

	private final ObjectMapper objectMapper;

	private Producer producer;

	private PrScpConfiguration config;

	private MDMSService mdmsService;

	private UserUtil userUtil;

	@Value("${egov.user.role.departementuser}")
	private String roleDepartUser;

	@Autowired
	public LibraryNotificationService(EventManegementRepository eventManegementRepository, ObjectMapper objectMapper,
			Producer producer, PrScpConfiguration config, MDMSService mdmsService, UserUtil userUtil) {
		this.eventManegementRepository = eventManegementRepository;
		this.objectMapper = objectMapper;
		this.producer = producer;
		this.config = config;
		this.mdmsService = mdmsService;
		this.userUtil = userUtil;
	}
	/**
	 * Send notification for event library upload  
	 * @param notificationreceiver to send notification 
	 * @return email and sms notification
	 */
	public void sendLibraryUploadNotificaiton(Library library) {
		log.info("Started : Send Notification sendLibraryUploadNotificaiton(): " + library.toString());
		try {

			Template template = Template.builder().tenantId(library.getTenantId())
					.eventDetailUuid(library.getEventDetailUuid()).moduleCode(library.getModuleCode())
					.templateType("NEW_MEDIA_UPLOAD").build();

			RequestInfo requestInfo = RequestInfo.builder().build();
			NotificationTemplate notificationTemplate = mdmsService.getTemplate(requestInfo, template);
			if (notificationTemplate != null) {
				notificationTemplate.setEventDetailUuid(library.getEventDetailUuid());

				List<EmailContent> emailContent = Arrays
						.asList(objectMapper.readValue(notificationTemplate.getEmailContent(), EmailContent[].class));

				JsonNode employeeInfo = userUtil.getUserByRole(RequestInfo.builder().build(), roleDepartUser,
						library.getTenantId());

				List<Employee> empList = new ArrayList<>();

				if (employeeInfo != null) {
					empList = Arrays.asList(objectMapper.readValue(employeeInfo.toString(), Employee[].class));
				}

				// Sending
				if (!emailContent.isEmpty()) {

					// Get Event Details:
					EventDetail criteria = EventDetail.builder().eventStatus("PUBLISHED")
							.tenantId(library.getTenantId()).eventDetailUuid(library.getEventDetailUuid())
							.moduleCode(library.getModuleCode()).build();

					List<EventDetail> eventDetails = eventManegementRepository.getEvent(criteria);

					EventDetail detail = null;
					if (!eventDetails.isEmpty())
						detail = eventDetails.get(0);
					if (detail != null) {
						String emailSubject = emailContent.get(0).getEmailSubject();
						String emailBody = emailContent.get(0).getEmailBody();
						String smsTemplate = notificationTemplate.getSmsContent();

						emailSubject = emailSubject.replace("[:eventTitle:]", detail.getEventTitle());
						emailBody = emailBody.replace("[:eventTitle:]", detail.getEventTitle());
						smsTemplate = smsTemplate.replace("[:eventTitle:]", detail.getEventTitle());

						for (Employee employee : empList) {

							User user = employee.getUser();

							String greets = config.getEmailGuestGreet() + "<br\\>";
							greets = greets.replace("[:contactName:]", user.getName());
							StringBuilder builderEmail = new StringBuilder(greets);
							builderEmail.append(emailBody);
							StringBuilder builderSms = new StringBuilder(
									config.getEmailGuestGreet().replace("[:contactName:]", user.getName()));
							builderSms.append(smsTemplate);

							// Email
							EmailRequest emailRequest = EmailRequest.builder().email(user.getEmailId())
									.subject(emailSubject).body(builderEmail.toString()).isHTML(true).build();
							producer.push(config.getNotificationEmailTopic(), emailRequest);

							// SMS
							if (notificationTemplate.getSmsContent() != null) {
								SMSRequest smsRequest = SMSRequest.builder().mobileNumber(user.getMobileNumber())
										.message(builderSms.toString()).build();
								producer.push(config.getNotificationSmsTopic(), smsRequest);
							}

						}
						log.info("Success : Sent Notification sendLibraryUploadNotificaiton(): " + library.toString());
					}
				}

			}

		} catch (Exception e) {
			log.info("Failed : Send Notification sendLibraryUploadNotificaiton(): " + library.toString());
			log.error("Failed : Send Notification sendLibraryUploadNotificaiton(): " + e.getMessage());
			throw new CustomException(CommonConstants.NOTIFICATION_EVENT_SEND_LIBRARY_EXCEPTION_CODE, e.getMessage());
		}

	}

}
