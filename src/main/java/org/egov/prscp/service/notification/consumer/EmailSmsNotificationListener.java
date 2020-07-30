package org.egov.prscp.service.notification.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.prscp.service.notification.EmailSmsEventInvitationService;
import org.egov.prscp.service.notification.EmailSmsEventReminderInvitationService;
import org.egov.prscp.service.notification.LibraryNotificationService;
import org.egov.prscp.service.notification.ResendEmailSmsEventInvitationService;
import org.egov.prscp.web.models.Library;
import org.egov.prscp.web.models.NotificationReceiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmailSmsNotificationListener {

	private EmailSmsEventInvitationService emailSmsService;
	private EmailSmsEventReminderInvitationService emailSmsEventReminderInvitationService;
	private LibraryNotificationService libraryNotificationService;
	private ObjectMapper objectMapper;
	private ResendEmailSmsEventInvitationService resendEmailSmsEventInvitationService;

	@Autowired
	public EmailSmsNotificationListener(EmailSmsEventInvitationService emailSmsService,
			EmailSmsEventReminderInvitationService emailSmsEventReminderInvitationService,
			LibraryNotificationService libraryNotificationService, ObjectMapper objectMapper,
			ResendEmailSmsEventInvitationService resendEmailSmsEventInvitationService) {
		this.emailSmsService = emailSmsService;
		this.emailSmsEventReminderInvitationService = emailSmsEventReminderInvitationService;
		this.libraryNotificationService = libraryNotificationService;
		this.objectMapper = objectMapper;
		this.resendEmailSmsEventInvitationService = resendEmailSmsEventInvitationService;
	}

	/**
	 * Consumer for event,press note, tender notification 
	 * @param notificationreceiver to send notification 
	 * @return email and sms notification
	 */
	@KafkaListener(topics = "${persister.events.notification.send.topic}")
	public void invitationsNew(ConsumerRecord<String, Object> data) throws InterruptedException {
		Thread.sleep(4000);
		log.info("Send Notofication Kafka Topic : " + data);
		NotificationReceiver notificationReceiver = objectMapper.convertValue(data.value(), NotificationReceiver.class);
		log.info("Send Notofication Kafka Topic : " + notificationReceiver.toString());
		emailSmsService.sendEmailAndSMS(notificationReceiver);
	}

	/**
	 * Consumer for event,press note, tender re-send notification 
	 * @param notificationreceiver to send notification 
	 * @return email and sms notification
	 */
	@KafkaListener(topics = "${persister.events.notification.resend.topic}")
	public void invitationsResend(ConsumerRecord<String, Object> data) throws InterruptedException {
		Thread.sleep(4000);
		log.info("ReSend Notofication Kafka Topic : " + data);
		NotificationReceiver notificationReceiver = objectMapper.convertValue(data.value(), NotificationReceiver.class);
		log.info("ReSend Notofication Kafka Topic : " + notificationReceiver.toString());
		resendEmailSmsEventInvitationService.sendEmailAndSMS(notificationReceiver);
	}

	/**
	 * Scheduler for event remainder notification 
	 * @param event
	 * @return email and sms notification
	 *//*
	//@Scheduled(cron = "0 0 6 * * ?")
	public void invitationsEventReminder() {
		emailSmsEventReminderInvitationService.reminderInvitation();
	}*/

	/**
	 * Consumer for upload library notification 
	 * @param library to send notification 
	 * @return email and sms notification
	 */
	@KafkaListener(topics = "${persister.notification.upload.library.topic}")
	public void sendLibraryUploadNotificaiton(ConsumerRecord<String, Object> data) throws InterruptedException {
		Thread.sleep(4000);
		log.info("Send Notofication Library Kafka Topic : " + data);
		Library library = objectMapper.convertValue(data.value(), Library.class);
		log.info("Send Notofication Library Kafka Topic : " + data);
		libraryNotificationService.sendLibraryUploadNotificaiton(library);
	}
}
