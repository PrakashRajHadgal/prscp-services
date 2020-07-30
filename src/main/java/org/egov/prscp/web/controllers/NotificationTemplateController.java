package org.egov.prscp.web.controllers;

import javax.validation.Valid;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.prscp.service.NotificationTemplateService;
import org.egov.prscp.service.notification.EmailSmsEventReminderInvitationService;
import org.egov.prscp.web.models.RequestInfoWrapper;
import org.egov.prscp.web.models.ResponseInfoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/template")
public class NotificationTemplateController {

	private final NotificationTemplateService notificationTemplateService;
	private EmailSmsEventReminderInvitationService emailSmsEventReminderInvitationService;

	@Autowired
	public NotificationTemplateController(NotificationTemplateService notificationTemplateService,
			EmailSmsEventReminderInvitationService emailSmsEventReminderInvitationService) {
		this.notificationTemplateService = notificationTemplateService;
		this.emailSmsEventReminderInvitationService = emailSmsEventReminderInvitationService;
	}

	@PostMapping(value = "/_gettemplate")
	public ResponseEntity<ResponseInfoWrapper> getTemplate(@Valid @RequestBody RequestInfoWrapper requestInfoWrapper) {
		return notificationTemplateService.getTemplate(requestInfoWrapper);
	}

	@PostMapping(value = "/_resend")
	public ResponseEntity<ResponseInfoWrapper> resendInvitation(
			@Valid @RequestBody RequestInfoWrapper requestInfoWrapper) {
		return notificationTemplateService.resendInvitation(requestInfoWrapper);
	}
	
	@PostMapping(value = "/crone/job/eventReminder")
	public ResponseEntity<ResponseInfo> schedulerCall(@Valid @RequestBody RequestInfoWrapper requestInfoWrapper) {
         emailSmsEventReminderInvitationService.reminderInvitation();
     	return new ResponseEntity<>(ResponseInfo.builder().status("SUCCESS").build(), HttpStatus.CREATED);
                 
		
	}

}
