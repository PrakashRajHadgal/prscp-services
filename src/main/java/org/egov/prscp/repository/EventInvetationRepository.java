package org.egov.prscp.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.egov.prscp.config.PrScpConfiguration;
import org.egov.prscp.producer.Producer;
import org.egov.prscp.repository.builder.PrQueryBuilder;
import org.egov.prscp.repository.rowmapper.InvitationGuestRowMapper;
import org.egov.prscp.repository.rowmapper.NotificationTemplateRowMapper;
import org.egov.prscp.web.models.EventDetail;
import org.egov.prscp.web.models.InviteGuest;
import org.egov.prscp.web.models.NotificationReceiver;
import org.egov.prscp.web.models.NotificationTemplate;
import org.egov.prscp.web.models.RequestInfoWrapper;
import org.egov.prscp.web.models.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class EventInvetationRepository {

	private JdbcTemplate jdbcTemplate;

	private InvitationGuestRowMapper invitationGuestRowMapper;

	private NotificationTemplateRowMapper notificationTemplateRowMapper;

	private Producer producer;

	private PrScpConfiguration config;

	@Autowired
	public EventInvetationRepository(JdbcTemplate jdbcTemplate, Producer producer, PrScpConfiguration config,
			NotificationTemplateRowMapper notificationTemplateRowMapper,
			InvitationGuestRowMapper invitationGuestRowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.producer = producer;
		this.config = config;
		this.notificationTemplateRowMapper = notificationTemplateRowMapper;
		this.invitationGuestRowMapper = invitationGuestRowMapper;
	}

	/**
	 * Save invite guests for event
	 * @param list of invite guests,push data in save invite guest topic 
	 * @return list of invte guests invited
	 */
	public List<InviteGuest> saveGuest(List<InviteGuest> inviteGuests, String tenantId, String eventDetailUuid,
			String userId, String moduleCode) {

		List<InviteGuest> existingList = jdbcTemplate.query(PrQueryBuilder.GET_INVITATION_GUEST,
				new Object[] { tenantId, moduleCode, eventDetailUuid, userId }, invitationGuestRowMapper);

		List<InviteGuest> existing = existingList.stream()
				.filter(exits -> inviteGuests.stream()
						.filter(nwList -> (exits.getGuestEmail().equalsIgnoreCase(nwList.getGuestEmail())
								&& exits.getGuestMobile().equalsIgnoreCase(nwList.getGuestMobile())
								&& exits.getGuestName().equalsIgnoreCase(nwList.getGuestName())
								&& exits.getEventGuestType().equalsIgnoreCase(nwList.getEventGuestType()))
								&& exits.getModuleCode().equalsIgnoreCase(nwList.getModuleCode()))
						.findFirst().isPresent())
				.collect(Collectors.toList());

		existingList.stream().forEach(
				exits -> inviteGuests.removeIf(nwList -> (exits.getGuestEmail().equalsIgnoreCase(nwList.getGuestEmail())
						&& exits.getGuestMobile().equalsIgnoreCase(nwList.getGuestMobile())
						&& exits.getGuestName().equalsIgnoreCase(nwList.getGuestName())
						&& exits.getEventGuestType().equalsIgnoreCase(nwList.getEventGuestType())
						&& exits.getModuleCode().equalsIgnoreCase(nwList.getModuleCode()))));

		RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().requestBody(inviteGuests).build();
		producer.push(config.getInvitationSaveGuestTopic(), infoWrapper);

		inviteGuests.addAll(existing);
		return inviteGuests;
	}
	
	/**
	 * Delete guest for event
	 * @param list of invited guest,push data in delete invite guest topic 
	 * @return Deleted Guest response
	 */
	public void deleteGuest(List<InviteGuest> inviteGuest) {
		RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().requestBody(inviteGuest).build();
		producer.push(config.getInvitationDeleteGuestTopic(), infoWrapper);
	}
	
	/**
	 * Get guest for event for given criteria
	 * @param invite guest criteria
	 * @return List of Guest response
	 */
	public List<InviteGuest> getGuest(InviteGuest inviteGuests) {
		return jdbcTemplate.query(PrQueryBuilder.GET_INVITATION_GUEST_LIST,
				new Object[] { inviteGuests.getTenantId(), inviteGuests.getModuleCode(),
						inviteGuests.getEventDetailUuid(), inviteGuests.getCreatedBy(), inviteGuests.getCreatedBy(),
						inviteGuests.isSentFlag(), inviteGuests.isSentFlag() },
				invitationGuestRowMapper);
	}
	/**
	 * Get Guest for given criteria 
	 * @param invite guest data 
	 * @return List of invite response 
	 */
	public List<InviteGuest> getGuestReminder(InviteGuest inviteGuests) {
		return jdbcTemplate.query(PrQueryBuilder.GET_INVITATION_GUEST_LIST_REMINDER, new Object[] {
				inviteGuests.getTenantId(), inviteGuests.getModuleCode(), inviteGuests.getEventDetailUuid() },
				invitationGuestRowMapper);
	}

	/**
	 * Get template for given criteria 
	 * @param Template data 
	 * @return List of template response 
	 */
	public NotificationTemplate getTemplate(Template template) {
		return jdbcTemplate.query(PrQueryBuilder.GET_TEMPLATE, new Object[] { template.getTenantId(),
				template.getModuleCode(), template.getEventDetailUuid(), template.getTemplateType() },
				notificationTemplateRowMapper);
	}
	
	
	/**
	 * Save template of event 
	 * @param Notification Template data,push data in event save template topic
	 * @return Created template response 
	 */
	public void saveTemplate(NotificationTemplate notificationTemplate) {
		RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().requestBody(notificationTemplate).build();
		producer.push(config.getInvitationSaveTemplateTopic(), infoWrapper);
	}
	/**
	 * Update template of event 
	 * @param Notification Template data,push data in event update template topic
	 * @return Updated template response 
	 */
	public void updateTemplate(NotificationTemplate notificationTemplate) {
		RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().requestBody(notificationTemplate).build();
		producer.push(config.getInvitationUpdateTemplateTopic(), infoWrapper);
	}

	/**
	 * Update template of event 
	 * @param Invite guest data,push data in event update template topic
	 * @return Updated template response 
	 */
	public void updateTemplateOfEvent(InviteGuest inviteGuest) {
		RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().requestBody(inviteGuest).build();
		producer.push(config.getInvitationUpdateEventTemplateTopic(), infoWrapper);
	}

	/**
	 * Send notification on event update
	 * @param eventDetail to send notification to guest,push data in event update topic
	 * @return Notification response 
	 */
	public void updateEventNotification(EventDetail eventDetail) {
		RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().requestBody(eventDetail).build();
		producer.push(config.getInvitationUpdateEventNotificaitonTopic(), infoWrapper);
	}
	
	/**
	 * Send invitation to guest for event
	 * @param NotificationReceiver to send invite to guest,push data in invite send topic
	 * @return Invitation response 
	 */
	public void sendInvitation(NotificationReceiver notificationReceiver) {
		producer.push(config.getInvitationSendTopic(), notificationReceiver);
	}
}
