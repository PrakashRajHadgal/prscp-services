package org.egov.prscp.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.egov.prscp.repository.EventInvetationRepository;
import org.egov.prscp.util.DeviceSource;
import org.egov.prscp.util.FileStoreUtils;
import org.egov.prscp.web.models.AuditDetails;
import org.egov.prscp.web.models.Files;
import org.egov.prscp.web.models.Guests;
import org.egov.prscp.web.models.InviteGuest;
import org.egov.prscp.web.models.NotificationTemplate;
import org.egov.prscp.web.models.RequestInfoWrapper;
import org.egov.prscp.web.models.Template;
import org.egov.tracer.model.CustomException;
import org.json.simple.JSONArray;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import junit.framework.Assert;

@RunWith(MockitoJUnitRunner.class)
public class EventInvitationServiceTest {

	@Mock
	private ObjectMapper objectMapper;

	@Mock
	private EventInvetationRepository repository;

	@Mock
	private DeviceSource deviceSource;

	@Mock
	private FileStoreUtils fileStoreUtils;

	@InjectMocks
	private EventInvitationService service;

	@Test
	public void testUplaodExternalGuest() throws IOException {
		Mockito.when(deviceSource.saveDeviceDetails(Matchers.anyString(), Matchers.anyString(), Matchers.anyString(),
				Matchers.anyString(), Matchers.any(AuditDetails.class))).thenReturn("ajsknd87qwbiuw");

		InviteGuest guests = InviteGuest.builder().tenantId("ch").moduleCode("PR").externalFileStoreId("asd232223c")
				.createdBy("dascawf232f").build();
		AuditDetails auditDetails = AuditDetails.builder().createdBy("1").createdTime(1546515646L).lastModifiedBy("1")
				.lastModifiedTime(15645455L).build();
		RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().auditDetails(auditDetails).requestBody(guests)
				.build();

		// Files files = Files.builder().url(
		// "https://chstage.blob.core.windows.net/fileshare/ch/PublicRelations/May/29/1590745732600sampledata.xls?sig=33DoKPb65SxWPCXbvNKxEby7htWYPc76OaueJGi%2BwSM%3D&st=2020-05-29T09%3A48%3A53Z&se=2020-05-30T09%3A48%3A53Z&sv=2016-05-31&sp=r&sr=b")
		// .build();

		Files files = Files.builder().url(
				"https://egov-rainmakers.s3.ap-south-1.amazonaws.com/ch/sampledata1.xls?response-content-disposition=attachment&X-Amz-Security-Token=IQoJb3JpZ2luX2VjEPD%2F%2F%2F%2F%2F%2F%2F%2F%2F%2FwEaCmFwLXNvdXRoLTEiSDBGAiEAhR3r547rpIqvgXmpkhJG1o%2Ff%2F7waOMwcy6TLZOFpWuUCIQCa7o7%2Bpb5SSh2SMn4BElGRtk5l1VexPGFx0p1K6a0%2F4Sr4AQhZEAAaDDg1MTg1NTgyMjEyMiIMmNPi%2FfpT%2FNQPhEHdKtUBYvUb0xWeSLWh98qjdzxecr4SLlKkocDKLj9u2oQmvzWI2gl8sB1xHuZ9PFWKzp38QwTYlBoqSyTv2Mxn1WZggYkOyiLEl4M2QNQsCTHqUXJzerEc2CCc5axpEbXvsTE%2BGpA9%2FC5ZKUjmLwPk%2BNdHbjdX5nXLUH0nN8J27FGFNDswiZQ6E851KdpFVeTVhCBzM%2BnLSKGuHoEMwW357SOa%2BCu9ffrZVZtZUXwqW8D9OEHIT%2BnUvrZUGiF%2BCRjhQLPrLkojMYPHfqmb4RqOmNMQhn4zoqISMLmG2PYFOvUCWh3MySbtK7cXMDRfdx%2B61M7gh29lXJMXUgAniO79slujCnlwTCyGX7gV%2FpyVxxtB3xpsi6pDrnOTAwXl6U9wWasssCl%2Bl8eHpT5jkUnYXbOsHcfL4CqXP5IHvu798CFJE96dbhGWQldpMS5w1jSLjE5EIHKxH9DyqUjYsd5CfIcpKdymhBJlj1NUuzs6bCbEGouBIpbyG98FQ98B9BjBVY4LlfsfEqUXIKqjlEIRI09G1o%2B3LV2HznR1iTAQjAw0yfVZ3SN5YBLQHCJrFqnBktc%2BRe7Vh5PZzV5rO4Mimo9mHMLq9r4ah2cxhC5Ex%2Bqj%2BFcnUXLHwBjIUsBAHG86mX6EERF4aNfv9dQ7qu%2BHEMIVySGUNWd81U469biAbBlyVx68tybPBrJHZxrBLWGngq60wZiwbX13%2FIz4DZWx7n5%2Bgu0XcE6OOsujtPaiH2I3tncEoE38587yDThFUjejVWxlwpeB5P8SmzYfDN221Kq8b5WJAA%3D%3D&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20200602T082119Z&X-Amz-SignedHeaders=host&X-Amz-Expires=300&X-Amz-Credential=ASIA4MVUQJUVF7TFK5G3%2F20200602%2Fap-south-1%2Fs3%2Faws4_request&X-Amz-Signature=d04bfd75915e875185abd253b786cc6e67c2931d57cfbcefa2105665e1b27918")
				.build();
		List<Files> attachmentsUrls = new ArrayList<>();
		attachmentsUrls.add(files);
		Mockito.when(fileStoreUtils.getFiles(Matchers.anyString(), Matchers.anyObject())).thenReturn(attachmentsUrls);
		Mockito.when(objectMapper.convertValue(infoWrapper.getRequestBody(), InviteGuest.class)).thenReturn(guests);
	//	Assert.assertEquals(HttpStatus.CREATED, service.uplaodExternalGuest(infoWrapper, "").getStatusCode());
	}

	@Test(expected = CustomException.class)
	public void testUplaodExternalGuest_2() throws IOException {
		Mockito.when(deviceSource.saveDeviceDetails(Matchers.anyString(), Matchers.anyString(), Matchers.anyString(),
				Matchers.anyString(), Matchers.any(AuditDetails.class))).thenReturn("ajsknd87qwbiuw");

		InviteGuest guests = InviteGuest.builder().tenantId("ch").moduleCode("PR").externalFileStoreId("asd232223c")
				.createdBy("dascawf232f").build();
		AuditDetails auditDetails = AuditDetails.builder().createdBy("1").createdTime(1546515646L).lastModifiedBy("1")
				.lastModifiedTime(15645455L).build();
		RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().auditDetails(auditDetails).requestBody(guests)
				.build();

		Files files = Files.builder().url(null).build();
		List<Files> attachmentsUrls = new ArrayList<>();
		attachmentsUrls.add(files);
		Mockito.when(fileStoreUtils.getFiles(Matchers.anyString(), Matchers.anyObject())).thenReturn(attachmentsUrls);
		Mockito.when(objectMapper.convertValue(infoWrapper.getRequestBody(), InviteGuest.class)).thenReturn(guests);
		Assert.assertEquals(HttpStatus.BAD_REQUEST, service.uplaodExternalGuest(infoWrapper, "").getStatusCode());
	}

	@Test
	public void testSendInvitations() {
		JSONArray emailContent = new JSONArray();
		JSONArray documentAttachment = new JSONArray();

		Template template = Template.builder().moduleCode("PR").tenantId("ch").emailContent(emailContent)
				.documentAttachment(documentAttachment).smsContent("sms").eventDetailUuid("asb87qwdb287")
				.templateMappedUuid("asb87qwdb287").templateType("CREATE_EVENT").build();

		NotificationTemplate notificationTemplate = NotificationTemplate.builder().moduleCode("PR").tenantId("ch")
				.notificationTemplateUuid("ansdh8723hrbdu23bd923").emailContent(emailContent.toJSONString())
				.setdoc(documentAttachment.toJSONString()).smsContent("sms").eventDetailUuid("asb87qwdb287")
				.templateMappedUuid("asb87qwdb287").templateType("CREATE_EVENT").build();

		Mockito.when(repository.getTemplate(Matchers.any(Template.class))).thenReturn(notificationTemplate);

		AuditDetails auditDetails = AuditDetails.builder().createdBy("1").createdTime(1546515646L).lastModifiedBy("1")
				.lastModifiedTime(15645455L).build();
		RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().auditDetails(auditDetails).requestBody(template)
				.build();
		Mockito.when(objectMapper.convertValue(infoWrapper.getRequestBody(), Template.class)).thenReturn(template);

		Assert.assertEquals(HttpStatus.OK, service.sendInvitations(infoWrapper).getStatusCode());
	}

	@Test
	public void testSendInvitations_1() {
		JSONArray emailContent = new JSONArray();
		JSONArray documentAttachment = new JSONArray();

		Template template = Template.builder().moduleCode("PR").tenantId("ch").emailContent(emailContent)
				.documentAttachment(documentAttachment).smsContent("sms").eventDetailUuid("asb87qwdb287")
				.templateMappedUuid("asb87qwdb287").templateType("CREATE_EVENT").build();

		Mockito.when(repository.getTemplate(Matchers.any(Template.class))).thenReturn(null);

		AuditDetails auditDetails = AuditDetails.builder().createdBy("1").createdTime(1546515646L).lastModifiedBy("1")
				.lastModifiedTime(15645455L).build();
		RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().auditDetails(auditDetails).requestBody(template)
				.build();
		Mockito.when(objectMapper.convertValue(infoWrapper.getRequestBody(), Template.class)).thenReturn(template);

		Assert.assertEquals(HttpStatus.OK, service.sendInvitations(infoWrapper).getStatusCode());
	}

	@Test(expected = CustomException.class)
	public void testSendInvitations_2() {
		JSONArray emailContent = new JSONArray();
		JSONArray documentAttachment = new JSONArray();

		Template template = Template.builder().moduleCode("PR").tenantId("ch").emailContent(emailContent)
				.documentAttachment(documentAttachment).smsContent("sms").eventDetailUuid("asb87qwdb287")
				.templateMappedUuid("asb87qwdb287").templateType("CREATE_EVENT").build();

		Mockito.when(repository.getTemplate(Matchers.any(Template.class))).thenReturn(null);

		RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().auditDetails(null).requestBody(template).build();
		Mockito.when(objectMapper.convertValue(infoWrapper.getRequestBody(), Template.class)).thenReturn(template);

		Assert.assertEquals(HttpStatus.BAD_REQUEST, service.sendInvitations(infoWrapper).getStatusCode());
	}

	@Test
	public void testAddGuest() {
		List<InviteGuest> inviteGuest = new ArrayList<>();
		InviteGuest guest = InviteGuest.builder().departmentName("depst").departmentUuid("asjdbasd8sdasd2")
				.eventGuestType("EXTRANEL").guestEmail("guestEmail@gmail.com").guestMobile("258941894")
				.userUuid("asd2edd").build();
		inviteGuest.add(guest);

		Guests guests = Guests.builder().eventDetailUuid("asdad89dhandds").tenantId("ch").moduleCode("PR")
				.inviteGuest(inviteGuest).build();

		AuditDetails auditDetails = AuditDetails.builder().createdBy("1").createdTime(1546515646L).lastModifiedBy("1")
				.lastModifiedTime(15645455L).build();
		RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().auditDetails(auditDetails).requestBody(guests)
				.build();

		Mockito.when(objectMapper.convertValue(infoWrapper.getRequestBody(), Guests.class)).thenReturn(guests);

		Mockito.when(deviceSource.saveDeviceDetails(Matchers.anyString(), Matchers.anyString(), Matchers.anyString(),
				Matchers.anyString(), Matchers.any(AuditDetails.class))).thenReturn("ajsknd87qwbiuw");

		Mockito.when(repository.saveGuest(Matchers.anyObject(), Matchers.anyString(), Matchers.anyString(),
				Matchers.anyString(), Matchers.anyString())).thenReturn(inviteGuest);

		Assert.assertEquals(HttpStatus.CREATED, service.addGuest(infoWrapper, "").getStatusCode());
	}

	@Test(expected = CustomException.class)
	public void testAddGuest_2() {
		RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().auditDetails(null).requestBody(null).build();
		Assert.assertEquals(HttpStatus.BAD_REQUEST, service.addGuest(infoWrapper, "").getStatusCode());
	}

	@Test
	public void testDeleteGuest() {
		List<InviteGuest> inviteGuest = new ArrayList<>();
		InviteGuest guest = InviteGuest.builder().departmentName("depst").departmentUuid("asjdbasd8sdasd2")
				.eventDetailUuid("asda2d22").eventGuestType("EXTRANEL").guestEmail("guestEmail@gmail.com")
				.guestMobile("258941894").userUuid("asd2edd").build();
		inviteGuest.add(guest);

		Guests guests = Guests.builder().eventDetailUuid("asdad89dhandds").tenantId("ch").moduleCode("PR")
				.inviteGuest(inviteGuest).build();

		AuditDetails auditDetails = AuditDetails.builder().createdBy("1").createdTime(1546515646L).lastModifiedBy("1")
				.lastModifiedTime(15645455L).build();
		RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().auditDetails(auditDetails).requestBody(guests)
				.build();

		Mockito.when(objectMapper.convertValue(infoWrapper.getRequestBody(), Guests.class)).thenReturn(guests);
		Assert.assertEquals(HttpStatus.OK, service.deleteGuest(infoWrapper).getStatusCode());
	}

	@Test(expected = CustomException.class)
	public void testDeleteGuest_2() {
		RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().auditDetails(null).requestBody(null).build();
		Assert.assertEquals(HttpStatus.BAD_REQUEST, service.deleteGuest(infoWrapper).getStatusCode());
	}

	@Test
	public void testGetGuest() {
		AuditDetails auditDetails = AuditDetails.builder().createdBy("1").createdTime(1546515646L).lastModifiedBy("1")
				.lastModifiedTime(15645455L).build();
		InviteGuest guests = new InviteGuest();
		List<InviteGuest> list = new ArrayList<>();
		list.add(guests);
		Mockito.when(repository.getGuest(Matchers.any())).thenReturn(list);
		RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().auditDetails(auditDetails).requestBody(guests)
				.build();
		Mockito.when(objectMapper.convertValue(infoWrapper.getRequestBody(), InviteGuest.class)).thenReturn(guests);
		Assert.assertEquals(HttpStatus.OK, service.getGuest(infoWrapper).getStatusCode());
	}
	@Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void uplaodExternalGuestThrowsCustomException() throws IOException {
        thrown.expect(CustomException.class);
             RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().auditDetails(null).requestBody(null).build();
             EventInvitationService obj = new EventInvitationService(repository, objectMapper, fileStoreUtils, deviceSource);
        obj.uplaodExternalGuest(infoWrapper,"user-agent");
    }
    @Test
    public void sendInvitationsThrowsCustomException() {
        thrown.expect(CustomException.class);
             RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().auditDetails(null).requestBody(null).build();
             EventInvitationService obj = new EventInvitationService(repository, objectMapper, fileStoreUtils, deviceSource);
        obj.sendInvitations(infoWrapper);
    }
    @Test
    public void addGuestThrowsCustomException() {
        thrown.expect(CustomException.class);
             RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().auditDetails(null).requestBody(null).build();
             EventInvitationService obj = new EventInvitationService(repository, objectMapper, fileStoreUtils, deviceSource);
        obj.addGuest(infoWrapper,"user-agent");
    }
   
}
