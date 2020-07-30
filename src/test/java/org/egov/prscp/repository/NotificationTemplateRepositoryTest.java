package org.egov.prscp.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.prscp.config.PrScpConfiguration;
import org.egov.prscp.producer.Producer;
import org.egov.prscp.repository.rowmapper.NotificationTemplateRowMapper;
import org.egov.prscp.web.models.AuditDetails;
import org.egov.prscp.web.models.ContactDetails;
import org.egov.prscp.web.models.GuestsResend;
import org.egov.prscp.web.models.InviteGuest;
import org.egov.prscp.web.models.NotificationTemplate;
import org.egov.prscp.web.models.PressMaster;
import org.egov.prscp.web.models.PressNote;
import org.egov.prscp.web.models.Template;
import org.egov.prscp.web.models.TenderNotice;
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
public class NotificationTemplateRepositoryTest {

	@InjectMocks
	private NotificationTemplateRepository repository;
	@Mock
	private JdbcTemplate jdbcTemplate;
	@Mock
	private Producer producer;
	@Mock
	private PrScpConfiguration config;
	@Mock
	private NotificationTemplateRowMapper notificationTemplateRowMapper;
	@Mock
	private TenderNoticePublicationRepository tenderNoticePublicationRepository;
	@Mock
	private EventInvetationRepository eventInvetationRepository;
	@Mock
	private GeneratePressNotesRepository generatePressNotesRepository;

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
	public void testResendEvent() {
		GuestsResend guestsResend = GuestsResend.builder().defaultAll(true).invitationUuid("asdf43f34f4")
				.moduleCode("PR").tenantId("ch").build();
		AuditDetails auditDetails = AuditDetails.builder().createdBy("12").lastModifiedBy("12").createdTime(156515L)
				.lastModifiedTime(156515L).build();

		InviteGuest guest = InviteGuest.builder().eventDetailUuid("asdn8dh38d98d").tenantId("ch").moduleCode("PR")
				.guestEmail("abc@gmail.com").guestName("guest1").guestMobile("9898989898").userUuid("sdff3fewasdf")
				.eventGuestType("EX").build();
		List<InviteGuest> list = new ArrayList<InviteGuest>();
		list.add(guest);
		Mockito.when(eventInvetationRepository.getGuestReminder(Matchers.any(InviteGuest.class))).thenReturn(list);
		GuestsResend output = repository.resendEvent(guestsResend, auditDetails);
		Assert.assertEquals("9898989898", output.getContactDetails().get(0).getReceiverMobile());
	}

	@Test
	public void testResendEvent_2() {
		GuestsResend guestsResend = GuestsResend.builder().defaultAll(false).invitationUuid("asdf43f34f4")
				.moduleCode("PR").tenantId("ch").build();
		AuditDetails auditDetails = AuditDetails.builder().createdBy("12").lastModifiedBy("12").createdTime(156515L)
				.lastModifiedTime(156515L).build();

		ContactDetails contactDetails = ContactDetails.builder().receiverEmail("acb@gmail.com")
				.receiverMobile("9898989898").receiverName("asc3").receiverUuid("asc3f29ci39").build();
		List<ContactDetails> list = new ArrayList<>();
		list.add(contactDetails);
		guestsResend.setContactDetails(list);

		GuestsResend output = repository.resendEvent(guestsResend, auditDetails);
		Assert.assertEquals("9898989898", output.getContactDetails().get(0).getReceiverMobile());
	}

	@Test
	public void testResendTender() {
		GuestsResend guestsResend = GuestsResend.builder().defaultAll(true).invitationUuid("asdf43f34f4")
				.moduleCode("PR").tenantId("ch").build();
		AuditDetails auditDetails = AuditDetails.builder().createdBy("12").lastModifiedBy("12").createdTime(156515L)
				.lastModifiedTime(156515L).build();

		PressMaster press = PressMaster.builder().pressMasterUuid("asdn8dh38d98d").tenantId("ch").moduleCode("PR")
				.email("abc@gmail.com").personnelName("guest1").mobile("9898989898").build();
		List<PressMaster> listGuest = new ArrayList<>();
		listGuest.add(press);

		Mockito.when(tenderNoticePublicationRepository.getTenderPress(Matchers.any(TenderNotice.class)))
				.thenReturn(listGuest);
		GuestsResend output = repository.resendTender(guestsResend, auditDetails);
		Assert.assertEquals("9898989898", output.getContactDetails().get(0).getReceiverMobile());
	}

	@Test
	public void testResendTender_2() {
		GuestsResend guestsResend = GuestsResend.builder().defaultAll(false).invitationUuid("asdf43f34f4")
				.moduleCode("PR").tenantId("ch").build();
		AuditDetails auditDetails = AuditDetails.builder().createdBy("12").lastModifiedBy("12").createdTime(156515L)
				.lastModifiedTime(156515L).build();

		ContactDetails contactDetails = ContactDetails.builder().receiverEmail("acb@gmail.com")
				.receiverMobile("9898989898").receiverName("asc3").receiverUuid("asc3f29ci39").build();
		List<ContactDetails> list = new ArrayList<>();
		list.add(contactDetails);
		guestsResend.setContactDetails(list);

		GuestsResend output = repository.resendTender(guestsResend, auditDetails);
		Assert.assertEquals("9898989898", output.getContactDetails().get(0).getReceiverMobile());
	}

	@Test
	public void testResendPressNote() {
		GuestsResend guestsResend = GuestsResend.builder().defaultAll(true).invitationUuid("asdf43f34f4")
				.moduleCode("PR").tenantId("ch").build();
		AuditDetails auditDetails = AuditDetails.builder().createdBy("12").lastModifiedBy("12").createdTime(156515L)
				.lastModifiedTime(156515L).build();

		PressMaster press = PressMaster.builder().pressMasterUuid("asdn8dh38d98d").tenantId("ch").moduleCode("PR")
				.email("abc@gmail.com").personnelName("guest1").mobile("9898989898").build();
		List<PressMaster> listGuest = new ArrayList<>();
		listGuest.add(press);

		Mockito.when(generatePressNotesRepository.getPressNoteePressList(Matchers.any(PressNote.class)))
				.thenReturn(listGuest);
		GuestsResend output = repository.resendPressNote(guestsResend, auditDetails);
		Assert.assertEquals("9898989898", output.getContactDetails().get(0).getReceiverMobile());
	}

	@Test
	public void testResendPressNote_2() {
		GuestsResend guestsResend = GuestsResend.builder().defaultAll(false).invitationUuid("asdf43f34f4")
				.moduleCode("PR").tenantId("ch").build();
		AuditDetails auditDetails = AuditDetails.builder().createdBy("12").lastModifiedBy("12").createdTime(156515L)
				.lastModifiedTime(156515L).build();

		ContactDetails contactDetails = ContactDetails.builder().receiverEmail("acb@gmail.com")
				.receiverMobile("9898989898").receiverName("asc3").receiverUuid("asc3f29ci39").build();
		List<ContactDetails> list = new ArrayList<>();
		list.add(contactDetails);
		guestsResend.setContactDetails(list);

		GuestsResend output = repository.resendPressNote(guestsResend, auditDetails);
		Assert.assertEquals("9898989898", output.getContactDetails().get(0).getReceiverMobile());
	}

}
