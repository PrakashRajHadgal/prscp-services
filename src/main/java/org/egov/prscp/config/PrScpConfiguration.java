package org.egov.prscp.config;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.egov.tracer.config.TracerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Import({ TracerConfiguration.class })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
public class PrScpConfiguration {

	@Value("${app.timezone}")
	private String timeZone;

	@PostConstruct
	public void initialize() {
		TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
	}

	@Bean
	@Autowired
	public MappingJackson2HttpMessageConverter jacksonConverter(ObjectMapper objectMapper) {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setObjectMapper(objectMapper);
		return converter;
	}

	@Value("${email.guest.greet}")
	private String emailGuestGreet;

	@Value("${persister.save.devicesource.topic}")
	private String requestDeviceSource;

	@Value("${egov.event.period}")
	private String periodEvents;

	@Value("${egov.pressmaster.period}")
	private String periodPressMaster;

	@Value("${egov.pressnote.period}")
	private String periodPressNote;

	@Value("${egov.tenderotice.period}")
	private String periodTenderNotice;

	@Value("${egov.core.notification.email.name}")
	private String notificationEmailTopic;

	@Value("${egov.core.notification.sms.name}")
	private String notificationSmsTopic;

	// Invitation Guest
	@Value("${persister.save.invitation.guest.topic}")
	private String invitationSaveGuestTopic;

	@Value("${persister.delete.invitation.guest.topic}")
	private String invitationDeleteGuestTopic;

	@Value("${persister.save.invitation.template.topic}")
	private String invitationSaveTemplateTopic;

	@Value("${persister.update.invitation.template.topic}")
	private String invitationUpdateTemplateTopic;

	@Value("${persister.update.invitation.template.notification.topic}")
	private String invitationUpdateEventNotificaitonTopic;

	@Value("${persister.update.event.template.topic}")
	private String invitationUpdateEventTemplateTopic;

	@Value("${persister.events.notification.send.topic}")
	private String invitationSendTopic;

	@Value("${persister.events.notification.resend.topic}")
	private String invitationResendTopic;

	@Value("${persister.events.notification.resend.history.topic}")
	private String persistResendHistoryTopic;

	@Value("${persister.events.notification.acknowledgement.topic}")
	private String invitationAcknowledgementTopic;

	@Value("${persister.pressnote.notification.acknowledgement.topic}")
	private String presNoteInvitationAcknowledgementTopic;

	@Value("${persister.tendernotice.notification.acknowledgement.topic}")
	private String tenderNoticeInvitationAcknowledgementTopic;

	// Press Master Topics
	@Value("${persister.save.pressmaster.topic}")
	private String pressMasterSaveTopic;

	@Value("${persister.update.pressmaster.topic}")
	private String pressMasterUpdateTopic;

	@Value("${persister.delete.pressmaster.topic}")
	private String pressMasterDeleteTopic;

	// User Config
	@Value("${egov.user.host}")
	private String userHost;

	@Value("${egov.user.context.path}")
	private String userContextPath;

	@Value("${egov.user.create.path}")
	private String userCreateEndpoint;

	@Value("${egov.user.search.path}")
	private String userSearchEndpoint;

	@Value("${egov.user.update.path}")
	private String userUpdateEndpoint;

	@Value("${egov.user.username.prefix}")
	private String usernamePrefix;

	// Idgen Config
	@Value("${egov.idgen.host}")
	private String idGenHost;

	@Value("${egov.idgen.path}")
	private String idGenPath;

	@Value("${egov.idgen.pr.applicationNum.name}")
	private String applicationNumberIdgenName;

	@Value("${egov.idgen.pr.applicationNum.format}")
	private String applicationNumberIdgenFormat;

	@Value("${egov.idgen.pr.app.tender.name}")
	private String appTenderNoticeNumberIdgenName;

	@Value("${egov.idgen.pr.app.tender.format}")
	private String appTenderNoticeNumberIdgenFormat;

	// Location Config
	@Value("${egov.location.host}")
	private String locationHost;

	@Value("${egov.location.context.path}")
	private String locationContextPath;

	@Value("${persister.save.eventdetail.topic}")
	private String saveEventTopic;

	@Value("${persister.update.eventdetail.topic}")
	private String updateEventDetailsTopic;

	@Value("${persister.update.eventstatus.topic}")
	private String updateEventStatusTopic;

	// Localization
	@Value("${egov.localization.host}")
	private String localizationHost;

	@Value("${egov.localization.context.path}")
	private String localizationContextPath;

	@Value("${egov.localization.search.endpoint}")
	private String localizationSearchEndpoint;

	@Value("${egov.localization.statelevel}")
	private Boolean isLocalizationStateLevel;

	// MDMS
	@Value("${egov.mdms.host}")
	private String mdmsHost;

	@Value("${egov.mdms.search.endpoint}")
	private String mdmsEndPoint;

	// Workflow
	@Value("${create.pr.workflow.name}")
	private String businessServiceValue;

	@Value("${workflow.context.path}")
	private String wfHost;

	@Value("${workflow.transition.path}")
	private String wfTransitionPath;

	@Value("${workflow.businessservice.search.path}")
	private String wfBusinessServiceSearchPath;

	@Value("${is.external.workflow.enabled}")
	private Boolean isExternalWorkFlowEnabled;

	// USER EVENTS
	@Value("${egov.ui.app.host}")
	private String uiAppHost;

	@Value("${egov.usr.events.create.topic}")
	private String saveUserEventsTopic;

	@Value("${egov.usr.events.pay.link}")
	private String payLink;

	@Value("${egov.usr.events.pay.code}")
	private String payCode;

	@Value("${egov.user.event.notification.enabled}")
	private Boolean isUserEventsNotificationEnabled;

	// Pr-Library
	@Value("${persister.save.library.topic}")
	private String librarySaveTopic;

	@Value("${persister.delete.library.topic}")
	private String libraryDeletTopic;

	@Value("${persister.save.pressnote.topic}")
	private String pressNoteSaveTopic;

	@Value("${persister.update.pressnote.topic}")
	private String pressNoteUpdateTopic;

	@Value("${persister.save.tender.topic}")
	private String createTender;

	@Value("${persister.update.tender.topic}")
	private String updateTender;

	@Value("${persister.update.published.tender.topic}")
	private String updatePublishedTender;

	@Value("${persister.save.committee.topic}")
	private String createCommitteeTopic;

	@Value("${persister.update.committee.topic}")
	private String updateCommitteeTopic;

	@Value("${persister.update.published.tender.map.press.topic}")
	private String updatePublishedTenderMapPress;

	// FileStore Service
	@Value("${egov.filestore.host}")
	private String filStoreHost;

	@Value("${egov.filestore.url}")
	private String filStoreUrlEndPoint;

	@Value("${egov.filestore.search}")
	private String filStoreSearchEndPoint;

	@Value("${persister.notification.upload.library.topic}")
	private String uploadLibraryNotification;

	@Value("${egov.notification.resend.text.footer}")
	private String notificationResendTextFooter;
}
