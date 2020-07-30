package org.egov.prscp.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.egov.prscp.config.PrScpConfiguration;
import org.egov.prscp.producer.Producer;
import org.egov.prscp.repository.builder.PrQueryBuilder;
import org.egov.prscp.repository.rowmapper.NotificationTemplateRowMapper;
import org.egov.prscp.util.MDMSService;
import org.egov.prscp.web.models.AuditDetails;
import org.egov.prscp.web.models.ContactDetails;
import org.egov.prscp.web.models.GuestsResend;
import org.egov.prscp.web.models.InviteGuest;
import org.egov.prscp.web.models.NotificationReceiver;
import org.egov.prscp.web.models.NotificationTemplate;
import org.egov.prscp.web.models.PressMaster;
import org.egov.prscp.web.models.PressNote;
import org.egov.prscp.web.models.Template;
import org.egov.prscp.web.models.TenderNotice;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class NotificationTemplateRepository {

	private JdbcTemplate jdbcTemplate;

	private Producer producer;

	private PrScpConfiguration config;

	private NotificationTemplateRowMapper notificationTemplateRowMapper;

	private TenderNoticePublicationRepository tenderNoticePublicationRepository;

	private EventInvetationRepository eventInvetationRepository;

	private GeneratePressNotesRepository generatePressNotesRepository;

	public NotificationTemplateRepository(JdbcTemplate jdbcTemplate, MDMSService mDMSService, Producer producer,
			PrScpConfiguration config, NotificationTemplateRowMapper notificationTemplateRowMapper,
			EventInvetationRepository eventInvetationRepository,
			TenderNoticePublicationRepository tenderNoticePublicationRepository,
			GeneratePressNotesRepository generatePressNotesRepository) {
		this.tenderNoticePublicationRepository = tenderNoticePublicationRepository;
		this.jdbcTemplate = jdbcTemplate;
		this.eventInvetationRepository = eventInvetationRepository;
		this.generatePressNotesRepository = generatePressNotesRepository;
		this.notificationTemplateRowMapper = notificationTemplateRowMapper;
		this.producer = producer;
		this.config = config;
	}
	
	/**
	 * Get Template for the given criteria
	 * @param requestInfoWrapper to get single or all templates
	 * @return list of Template
	 */
	public NotificationTemplate getTemplate(Template template) {
		return jdbcTemplate
				.query(PrQueryBuilder.GET_TEMPLATE,
						new Object[] { template.getTenantId(), template.getModuleCode(),
								template.getTemplateMappedUuid(), template.getTemplateType() },
						notificationTemplateRowMapper);
	}

	/**
     * Re-send event invitation
     * @param Guest contact details
     * @return Guest contact details 
     */
	public GuestsResend resendEvent(GuestsResend guestsResend, AuditDetails auditDetails) {
		List<ContactDetails> contactDetails = new ArrayList<>();
		NotificationReceiver notificationReceiver = NotificationReceiver.builder().tenantId(guestsResend.getTenantId())
				.receiverUuid(guestsResend.getInvitationUuid()).moduleCode(guestsResend.getModuleCode())
				.receiverType(guestsResend.getModuleName()).build();

		if (guestsResend.isDefaultAll()) {
			InviteGuest inviteGuests = InviteGuest.builder().tenantId(guestsResend.getTenantId())
					.moduleCode(guestsResend.getModuleCode()).eventDetailUuid(guestsResend.getInvitationUuid()).build();
			List<InviteGuest> listGuest = eventInvetationRepository.getGuestReminder(inviteGuests);

			for (InviteGuest guest : listGuest) {
				ContactDetails details = ContactDetails.builder().notificationResendUuid(UUID.randomUUID().toString())
						.moduleCode(guestsResend.getModuleCode()).moduleName(guestsResend.getModuleName())
						.tenantId(guestsResend.getTenantId()).notificationMappedUuid(guestsResend.getInvitationUuid())
						.receiverName(guest.getGuestName()).receiverEmail(guest.getGuestEmail())
						.receiverMobile(guest.getGuestMobile()).receiverUuid(guest.getEventGuestUuid())
						.resentBy(auditDetails.getCreatedBy()).resentTime(auditDetails.getCreatedTime()).build();

				contactDetails.add(details);
			}
		} else {
			List<ContactDetails> contactDes = guestsResend.getContactDetails();
			if (contactDes != null && !contactDes.isEmpty()) {
				for (ContactDetails contact : contactDes) {
					contact = ContactDetails.builder().notificationResendUuid(UUID.randomUUID().toString())
							.moduleCode(guestsResend.getModuleCode()).moduleName(guestsResend.getModuleName())
							.tenantId(guestsResend.getTenantId())
							.notificationMappedUuid(guestsResend.getInvitationUuid())
							.receiverName(contact.getReceiverName()).receiverEmail(contact.getReceiverEmail())
							.receiverMobile(contact.getReceiverMobile()).receiverUuid(contact.getReceiverEmail())
							.resentBy(auditDetails.getCreatedBy()).resentTime(auditDetails.getCreatedTime()).build();
					contactDetails.add(contact);
				}
			}
		}
		notificationReceiver.setContactDetails(contactDetails);
		guestsResend.setContactDetails(contactDetails);
		reSendInvitation(notificationReceiver);
		return guestsResend;
	}
	/**
     * Re-send tender invitation
     * @param Guest contact details
     * @return Guest contact details 
     */
	public GuestsResend resendTender(GuestsResend guestsResend, AuditDetails auditDetails) {
		List<ContactDetails> contactDetails = new ArrayList<>();
		NotificationReceiver notificationReceiver = NotificationReceiver.builder().tenantId(guestsResend.getTenantId())
				.receiverUuid(guestsResend.getInvitationUuid()).moduleCode(guestsResend.getModuleCode())
				.receiverType(guestsResend.getModuleName()).build();

		if (guestsResend.isDefaultAll()) {

			TenderNotice tenderNotice = TenderNotice.builder().tenantId(guestsResend.getTenantId())
					.moduleCode(guestsResend.getModuleCode()).tenderNoticeUuid(guestsResend.getInvitationUuid())
					.build();
			List<PressMaster> listGuest = tenderNoticePublicationRepository.getTenderPress(tenderNotice);

			for (PressMaster guest : listGuest) {
				ContactDetails details = ContactDetails.builder().notificationResendUuid(UUID.randomUUID().toString())
						.moduleCode(guestsResend.getModuleCode()).moduleName(guestsResend.getModuleName())
						.tenantId(guestsResend.getTenantId()).notificationMappedUuid(guestsResend.getInvitationUuid())
						.receiverName(guest.getPersonnelName()).receiverEmail(guest.getEmail())
						.receiverMobile(guest.getMobile()).receiverUuid(guest.getPressMasterUuid())
						.resentBy(auditDetails.getCreatedBy()).resentTime(auditDetails.getCreatedTime()).build();

				contactDetails.add(details);
			}

		} else {
			List<ContactDetails> contactDes = guestsResend.getContactDetails();
			if (contactDes != null && !contactDes.isEmpty()) {
				for (ContactDetails contact : contactDes) {
					contact = ContactDetails.builder().notificationResendUuid(UUID.randomUUID().toString())
							.moduleCode(guestsResend.getModuleCode()).moduleName(guestsResend.getModuleName())
							.tenantId(guestsResend.getTenantId())
							.notificationMappedUuid(guestsResend.getInvitationUuid())
							.receiverName(contact.getReceiverName()).receiverEmail(contact.getReceiverEmail())
							.receiverMobile(contact.getReceiverMobile()).receiverUuid(contact.getReceiverEmail())
							.resentBy(auditDetails.getCreatedBy()).resentTime(auditDetails.getCreatedTime()).build();
					contactDetails.add(contact);
				}
			}
		}
		notificationReceiver.setContactDetails(contactDetails);
		guestsResend.setContactDetails(contactDetails);

		reSendInvitation(notificationReceiver);
		return guestsResend;
	}
	/**
     * Re-send press note  invitation
     * @param Guest contact details
     * @return Guest contact details 
     */
	public GuestsResend resendPressNote(GuestsResend guestsResend, AuditDetails auditDetails) {
		List<ContactDetails> contactDetails = new ArrayList<>();

		NotificationReceiver notificationReceiver = NotificationReceiver.builder().tenantId(guestsResend.getTenantId())
				.receiverUuid(guestsResend.getInvitationUuid()).moduleCode(guestsResend.getModuleCode())
				.receiverType(guestsResend.getModuleName()).build();

		if (guestsResend.isDefaultAll()) {

			PressNote pressNote = PressNote.builder().tenantId(guestsResend.getTenantId())
					.moduleCode(guestsResend.getModuleCode()).pressNoteUuid(guestsResend.getInvitationUuid()).build();
			List<PressMaster> listGuest = generatePressNotesRepository.getPressNoteePressList(pressNote);

			for (PressMaster guest : listGuest) {
				ContactDetails details = ContactDetails.builder().notificationResendUuid(UUID.randomUUID().toString())
						.moduleCode(guestsResend.getModuleCode()).moduleName(guestsResend.getModuleName())
						.tenantId(guestsResend.getTenantId()).notificationMappedUuid(guestsResend.getInvitationUuid())
						.receiverName(guest.getPersonnelName()).receiverEmail(guest.getEmail())
						.receiverMobile(guest.getMobile()).receiverUuid(guest.getPressMasterUuid())
						.resentBy(auditDetails.getCreatedBy()).resentTime(auditDetails.getCreatedTime()).build();

				contactDetails.add(details);
			}
		} else {
			List<ContactDetails> contactDes = guestsResend.getContactDetails();
			if (contactDes != null && !contactDes.isEmpty()) {
				for (ContactDetails contact : contactDes) {
					contact = ContactDetails.builder().notificationResendUuid(UUID.randomUUID().toString())
							.moduleCode(guestsResend.getModuleCode()).moduleName(guestsResend.getModuleName())
							.tenantId(guestsResend.getTenantId())
							.notificationMappedUuid(guestsResend.getInvitationUuid())
							.receiverName(contact.getReceiverName()).receiverEmail(contact.getReceiverEmail())
							.receiverMobile(contact.getReceiverMobile()).receiverUuid(contact.getReceiverEmail())
							.resentBy(auditDetails.getCreatedBy()).resentTime(auditDetails.getCreatedTime()).build();
					contactDetails.add(contact);
				}
			}
		}
		notificationReceiver.setContactDetails(contactDetails);
		guestsResend.setContactDetails(contactDetails);

		reSendInvitation(notificationReceiver);
		return guestsResend;
	}
	/**
     * Push Data to re-send invitation
     * @param NotificationReceiver details
     * @return Email and sms notification 
     */
	public void reSendInvitation(NotificationReceiver notificationReceiver) {
		producer.push(config.getInvitationResendTopic(), notificationReceiver);
	}
}
