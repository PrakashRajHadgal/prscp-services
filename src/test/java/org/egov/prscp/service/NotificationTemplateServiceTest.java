package org.egov.prscp.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.egov.prscp.config.PrScpConfiguration;
import org.egov.prscp.producer.Producer;
import org.egov.prscp.repository.EventManegementRepository;
import org.egov.prscp.repository.NotificationTemplateRepository;
import org.egov.prscp.repository.TenderNoticePublicationRepository;
import org.egov.prscp.util.MDMSService;
import org.egov.prscp.web.models.AuditDetails;
import org.egov.prscp.web.models.EmailContent;
import org.egov.prscp.web.models.EventDetail;
import org.egov.prscp.web.models.GuestsResend;
import org.egov.prscp.web.models.NotificationTemplate;
import org.egov.prscp.web.models.RequestInfoWrapper;
import org.egov.prscp.web.models.Template;
import org.egov.prscp.web.models.TenderNotice;
import org.egov.tracer.model.CustomException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import junit.framework.Assert;

@RunWith(MockitoJUnitRunner.class)
public class NotificationTemplateServiceTest {

	@Mock
	private ObjectMapper objectMapper;

	@Mock
	private NotificationTemplateRepository repository;

	@Mock
	private EventManegementRepository eventManegementRepository;

	@Mock
	private TenderNoticePublicationRepository tenderNoticePublicationRepository;

	@Mock
	private MDMSService mDMSService;

	@Mock
	private Producer producer;

	@Mock
	private PrScpConfiguration config;

	@InjectMocks
	private NotificationTemplateService service;

	@Test
	public void testGetTemplate() throws JsonParseException, JsonMappingException, IOException {
		Template template = Template.builder().tenantId("ch").moduleCode("PR").templateMappedUuid("CREATE_EVENT")
				.moduleName("EVENT").templateMappedUuid("de9279af-7eb3-4005-85eb-d9b37625fef8").build();
		NotificationTemplate notificationTemplate = NotificationTemplate.builder().smsContent("smmss contents")
				.emailContent("[{\"emailSubject\":\"asjahfas\",\"emailBody\":\"asfasfasf\"}]").build();

		RequestInfoWrapper requestInfoWrapper = RequestInfoWrapper.builder().requestBody(template).build();
		Mockito.when(objectMapper.convertValue(requestInfoWrapper.getRequestBody(), Template.class))
				.thenReturn(template);
		Mockito.when(repository.getTemplate(Matchers.any(Template.class))).thenReturn(notificationTemplate);
		EmailContent content = EmailContent.builder().emailSubject("emailSubject").emailBody("emailBody").build();
		List<EmailContent> emailContent = new ArrayList<>();
		emailContent.add(content);

		EmailContent[] list = new EmailContent[] { content };
		Mockito.when(objectMapper.readValue(notificationTemplate.getEmailContent(), EmailContent[].class))
				.thenReturn(list);
		List<EventDetail> eventDetails = new ArrayList<>();
		EventDetail detail = EventDetail.builder().eventTitle("title").startDate("2020-12-12").startTime("03:05:00")
				.eventLocation("location").facebookUrl("facebook").instagramUrl("instagram").twitterUrl("twitter")
				.build();
		eventDetails.add(detail);
		Mockito.when(eventManegementRepository.getEvent(Matchers.any(EventDetail.class))).thenReturn(eventDetails);
		Assert.assertEquals(HttpStatus.OK, service.getTemplate(requestInfoWrapper).getStatusCode());
	}

	@Test
	public void testGetTemplate_2() throws JsonParseException, JsonMappingException, IOException {
		Template template = Template.builder().tenantId("ch").moduleCode("PR").templateMappedUuid("CREATE_EVENT")
				.moduleName("PRESSNOTE").templateMappedUuid("de9279af-7eb3-4005-85eb-d9b37625fef8").build();
		NotificationTemplate notificationTemplate = NotificationTemplate.builder().smsContent("smmss contents")
				.emailContent("[{\"emailSubject\":\"asjahfas\",\"emailBody\":\"asfasfasf\"}]").build();

		RequestInfoWrapper requestInfoWrapper = RequestInfoWrapper.builder().requestBody(template).build();
		Mockito.when(objectMapper.convertValue(requestInfoWrapper.getRequestBody(), Template.class))
				.thenReturn(template);
		Mockito.when(repository.getTemplate(Matchers.any(Template.class))).thenReturn(notificationTemplate);
		EmailContent content = EmailContent.builder().emailSubject("emailSubject").emailBody("emailBody").build();
		List<EmailContent> emailContent = new ArrayList<>();
		emailContent.add(content);

		EmailContent[] list = new EmailContent[] { content };
		Mockito.when(objectMapper.readValue(notificationTemplate.getEmailContent(), EmailContent[].class))
				.thenReturn(list);
		List<EventDetail> eventDetails = new ArrayList<>();
		EventDetail detail = EventDetail.builder().eventTitle("title").startDate("2020-12-12").startTime("03:05:00")
				.eventLocation("location").facebookUrl("facebook").instagramUrl("instagram").twitterUrl("twitter")
				.build();
		eventDetails.add(detail);
		Mockito.when(eventManegementRepository.getEvent(Matchers.any(EventDetail.class))).thenReturn(eventDetails);
		Assert.assertEquals(HttpStatus.OK, service.getTemplate(requestInfoWrapper).getStatusCode());
	}

	@Test
	public void testGetTemplate_3() throws JsonParseException, JsonMappingException, IOException {
		Template template = Template.builder().tenantId("ch").moduleCode("PR").templateMappedUuid("CREATE_EVENT")
				.moduleName("TENDER").templateMappedUuid("de9279af-7eb3-4005-85eb-d9b37625fef8").build();
		NotificationTemplate notificationTemplate = NotificationTemplate.builder().smsContent("smmss contents")
				.emailContent("[{\"emailSubject\":\"asjahfas\",\"emailBody\":\"asfasfasf\"}]").build();

		RequestInfoWrapper requestInfoWrapper = RequestInfoWrapper.builder().requestBody(template).build();
		Mockito.when(objectMapper.convertValue(requestInfoWrapper.getRequestBody(), Template.class))
				.thenReturn(template);
		Mockito.when(repository.getTemplate(Matchers.any(Template.class))).thenReturn(notificationTemplate);
		EmailContent content = EmailContent.builder().emailSubject("emailSubject").emailBody("emailBody").build();
		List<EmailContent> emailContent = new ArrayList<>();
		emailContent.add(content);

		EmailContent[] list = new EmailContent[] { content };
		Mockito.when(objectMapper.readValue(notificationTemplate.getEmailContent(), EmailContent[].class))
				.thenReturn(list);

		List<TenderNotice> pressTenderList = new ArrayList<>();
		TenderNotice detail = TenderNotice.builder().tenderSubject("subject title").noteContent("noteContent").build();
		pressTenderList.add(detail);
		Mockito.when(tenderNoticePublicationRepository.getTender(Matchers.any(TenderNotice.class)))
				.thenReturn(pressTenderList);
		Assert.assertEquals(HttpStatus.OK, service.getTemplate(requestInfoWrapper).getStatusCode());
	}

	@Test
	public void testResendInvitation_1() {
		GuestsResend guestsResend = GuestsResend.builder().moduleName("EVENT").moduleCode("PR").tenantId("ch").build();

		Mockito.when(repository.resendEvent(Matchers.any(GuestsResend.class), Matchers.any(AuditDetails.class)))
				.thenReturn(guestsResend);
		RequestInfoWrapper requestInfoWrapper = RequestInfoWrapper.builder().requestBody(guestsResend).build();
		Mockito.when(objectMapper.convertValue(requestInfoWrapper.getRequestBody(), GuestsResend.class))
				.thenReturn(guestsResend);
		Assert.assertEquals(HttpStatus.OK, service.resendInvitation(requestInfoWrapper).getStatusCode());
	}

	@Test
	public void testResendInvitation_2() {
		GuestsResend guestsResend = GuestsResend.builder().moduleName("PRESSNOTE").moduleCode("PR").tenantId("ch")
				.build();

		Mockito.when(repository.resendEvent(Matchers.any(GuestsResend.class), Matchers.any(AuditDetails.class)))
				.thenReturn(guestsResend);
		RequestInfoWrapper requestInfoWrapper = RequestInfoWrapper.builder().requestBody(guestsResend).build();
		Mockito.when(objectMapper.convertValue(requestInfoWrapper.getRequestBody(), GuestsResend.class))
				.thenReturn(guestsResend);
		Assert.assertEquals(HttpStatus.OK, service.resendInvitation(requestInfoWrapper).getStatusCode());
	}

	@Test
	public void testResendInvitation_3() {
		GuestsResend guestsResend = GuestsResend.builder().moduleName("TENDER").moduleCode("PR").tenantId("ch").build();
		Mockito.when(repository.resendEvent(Matchers.any(GuestsResend.class), Matchers.any(AuditDetails.class)))
				.thenReturn(guestsResend);
		RequestInfoWrapper requestInfoWrapper = RequestInfoWrapper.builder().requestBody(guestsResend).build();
		Mockito.when(objectMapper.convertValue(requestInfoWrapper.getRequestBody(), GuestsResend.class))
				.thenReturn(guestsResend);
		Assert.assertEquals(HttpStatus.OK, service.resendInvitation(requestInfoWrapper).getStatusCode());
	}

	@Test(expected = CustomException.class)
	public void testResendInvitation_4() {
		GuestsResend guestsResend = GuestsResend.builder().moduleName(null).moduleCode("PR").tenantId("ch").build();
		Mockito.when(repository.resendEvent(Matchers.any(GuestsResend.class), Matchers.any(AuditDetails.class)))
				.thenReturn(guestsResend);
		RequestInfoWrapper requestInfoWrapper = RequestInfoWrapper.builder().requestBody(guestsResend).build();
		Mockito.when(objectMapper.convertValue(requestInfoWrapper.getRequestBody(), GuestsResend.class))
				.thenReturn(guestsResend);
		Assert.assertEquals(HttpStatus.OK, service.resendInvitation(requestInfoWrapper).getStatusCode());
	}
	@Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void resendInvitationThrowsCustomException() {
        thrown.expect(CustomException.class);
             RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().auditDetails(null).requestBody(null).build();
             NotificationTemplateService obj = new NotificationTemplateService(objectMapper, mDMSService, repository, eventManegementRepository, tenderNoticePublicationRepository, null);
        obj.resendInvitation(infoWrapper);
    }
}
