package org.egov.prscp.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.prscp.config.PrScpConfiguration;
import org.egov.prscp.producer.Producer;
import org.egov.prscp.repository.rowmapper.NotificationTemplateRowMapper;
import org.egov.prscp.web.models.InviteGuest;
import org.egov.prscp.web.models.NotificationTemplate;
import org.egov.prscp.web.models.Template;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

@RunWith(MockitoJUnitRunner.class)
public class EventInvetationRepositoryTest {

	@InjectMocks
	private EventInvetationRepository repository;

	@Mock
	private NotificationTemplateRowMapper notificationTemplateRowMapper;

	@Mock
	private JdbcTemplate jdbcTemplate;

	@Mock
	private Producer producer;

	@Mock
	private PrScpConfiguration config;

	@Test
	public void testSaveGuest() {
		InviteGuest guest = InviteGuest.builder().eventDetailUuid("asdn8dh38d98d").tenantId("ch").moduleCode("PR")
				.guestEmail("abc@gmail.com").guestName("guest1").guestMobile("9898989898").userUuid("sdff3fewasdf")
				.eventGuestType("EX").build();
		InviteGuest guest2 = InviteGuest.builder().eventDetailUuid("asdn8dh38d98d").tenantId("ch").moduleCode("PR")
				.guestEmail("xyz@gmail.com").guestName("guest2").guestMobile("9898984845").userUuid("sdff3fewasdf")
				.eventGuestType("EX").build();
		List<InviteGuest> existingList = new ArrayList<>();
		existingList.add(guest);

		List<InviteGuest> inputList = new ArrayList<>();
		inputList.add(guest);
		inputList.add(guest2);

		Mockito.when(jdbcTemplate.query(Matchers.anyString(), Matchers.any(Object[].class),
				Matchers.any(ResultSetExtractor.class))).thenReturn(existingList);

		List<InviteGuest> outputList = repository.saveGuest(inputList, "ch", "asdn8dh38d98d", "sdff3fewasdf", "PR");
		Assert.assertEquals("xyz@gmail.com", outputList.get(0).getGuestEmail());
	}

	@Test
	public void testDeleteGuest() {
		InviteGuest guest = InviteGuest.builder().eventDetailUuid("asdn8dh38d98d").tenantId("ch").moduleCode("PR")
				.guestEmail("abc@gmail.com").guestName("guest1").guestMobile("9898989898").userUuid("sdff3fewasdf")
				.eventGuestType("EX").build();
		List<InviteGuest> inviteGuest = new ArrayList<>();
		inviteGuest.add(guest);
		repository.deleteGuest(inviteGuest);
	}

	@Test
	public void testGetGuest() {
		InviteGuest guest = InviteGuest.builder().eventDetailUuid("asdn8dh38d98d").tenantId("ch").moduleCode("PR")
				.guestEmail("abc@gmail.com").guestName("guest1").guestMobile("9898989898").userUuid("sdff3fewasdf")
				.eventGuestType("EX").build();
		List<InviteGuest> inviteGuest = new ArrayList<>();
		inviteGuest.add(guest);
		Mockito.when(jdbcTemplate.query(Matchers.anyString(), Matchers.any(Object[].class),
				Matchers.any(ResultSetExtractor.class))).thenReturn(inviteGuest);

		InviteGuest inviteGuests = new InviteGuest();
		List<InviteGuest> outputList = repository.getGuest(inviteGuests);
		Assert.assertEquals("abc@gmail.com", outputList.get(0).getGuestEmail());
	}

	@Test
	public void testGetGuestReminder() {
		InviteGuest guest = InviteGuest.builder().eventDetailUuid("asdn8dh38d98d").tenantId("ch").moduleCode("PR")
				.guestEmail("abc@gmail.com").guestName("guest1").guestMobile("9898989898").userUuid("sdff3fewasdf")
				.eventGuestType("EX").build();
		List<InviteGuest> inviteGuest = new ArrayList<>();
		inviteGuest.add(guest);
		Mockito.when(jdbcTemplate.query(Matchers.anyString(), Matchers.any(Object[].class),
				Matchers.any(ResultSetExtractor.class))).thenReturn(inviteGuest);

		InviteGuest inviteGuests = new InviteGuest();
		List<InviteGuest> outputList = repository.getGuestReminder(inviteGuests);
		Assert.assertEquals("abc@gmail.com", outputList.get(0).getGuestEmail());
	}

	@Test
	public void testGetTemplate() {
		NotificationTemplate notificationTemplate = NotificationTemplate.builder().notificationTemplateUuid("wwef3r3f")
				.build();
		Template template = Template.builder().build();
		Mockito.when(jdbcTemplate.query(Matchers.anyString(), Matchers.any(Object[].class),
				Matchers.any(ResultSetExtractor.class))).thenReturn(notificationTemplate);
		NotificationTemplate notificationTemplate2 = repository.getTemplate(template);
		Assert.assertEquals("wwef3r3f", notificationTemplate2.getNotificationTemplateUuid());
	}

	@Test
	public void testSaveTemplate() {
		repository.saveTemplate(null);
	}

	@Test
	public void testUpdateTemplate() {
		repository.updateTemplate(null);
	}

	@Test
	public void testUpdateTemplateOfEvent() {
		repository.updateTemplateOfEvent(null);
	}

	@Test
	public void testUpdateEventNotification() {
		repository.updateEventNotification(null);
	}

	@Test
	public void testSendInvitation() {
		repository.sendInvitation(null);
	}

}
