package org.egov.prscp.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.prscp.config.PrScpConfiguration;
import org.egov.prscp.repository.TenderNoticePublicationRepository;
import org.egov.prscp.util.DeviceSource;
import org.egov.prscp.util.IDGenUtil;
import org.egov.prscp.util.IdGenRepository;
import org.egov.prscp.web.models.AuditDetails;
import org.egov.prscp.web.models.RequestInfoWrapper;
import org.egov.prscp.web.models.Template;
import org.egov.prscp.web.models.TenderNotice;
import org.egov.prscp.web.models.Idgen.IdGenerationResponse;
import org.egov.prscp.web.models.Idgen.IdResponse;
import org.egov.tracer.model.CustomException;
import org.json.simple.JSONArray;
import org.junit.Assert;
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

@RunWith(MockitoJUnitRunner.class)
public class TenderNoticePublicationServiceTest {

	@Mock
	private ObjectMapper objectMapper;
	@Mock
	private TenderNoticePublicationRepository repository;
	@InjectMocks
	private TenderNoticePublicationService tenderNoticePublicationService;
	@Mock
	private IDGenUtil idgen;
	@Mock
	private DeviceSource deviceSource;
	@Mock
	private IdGenRepository idgenrepository;
	@Mock
	private PrScpConfiguration config;

	@Test
	public void testCreateTender() throws Exception {

		JSONArray docs = new JSONArray();

		TenderNotice tender = TenderNotice.builder().tenderNoticeUuid("abe3a709-50fb-4c61-b3f5-e4d4eaaaf3fd")
				.moduleCode("PR").tenantId("ch.chandigarh").tenderDocument(docs).tenderSubject("Tender Subject")
				.build();

		AuditDetails auditDetails = AuditDetails.builder().createdBy("1").createdTime(1546515646L).lastModifiedBy("1")
				.lastModifiedTime(15645455L).build();

		RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().auditDetails(auditDetails).requestBody(tender)
				.build();
		Mockito.when(objectMapper.convertValue(infoWrapper.getRequestBody(), TenderNotice.class)).thenReturn(tender);

		List<IdResponse> idResponses = new ArrayList<>();

		IdResponse ids = IdResponse.builder().id("1").build();

		idResponses.add(ids);

		IdGenerationResponse id = IdGenerationResponse.builder().idResponses(idResponses).build();

		Mockito.when(idgenrepository.getId(Matchers.anyObject(), Matchers.anyString(), Matchers.anyString(),
				Matchers.anyString(), Matchers.anyInt())).thenReturn(id);

		ResponseInfo workflowResponse = ResponseInfo.builder().status("successful").build();

		Mockito.when(idgen.createWorkflowRequest(Matchers.anyObject())).thenReturn(workflowResponse);

		Assert.assertEquals(HttpStatus.CREATED,
				tenderNoticePublicationService.createTender(infoWrapper, "User-Agent-Values").getStatusCode());

	}

	@Test(expected = CustomException.class)
	public void testCreateTender_1() throws Exception {
		RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().auditDetails(null).requestBody(null).build();
		Assert.assertEquals(HttpStatus.CREATED,
				tenderNoticePublicationService.createTender(infoWrapper, "User-Agent-Values").getStatusCode());
	}

	@Test
	public void testGetTender() {

		TenderNotice tender = TenderNotice.builder().tenderNoticeUuid("abe3a709-50fb-4c61-b3f5-e4d4eaaaf3fd")
				.moduleCode("PR").tenantId("ch.chandigarh").build();

		RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().requestBody(tender).build();
		Mockito.when(objectMapper.convertValue(infoWrapper.getRequestBody(), TenderNotice.class)).thenReturn(tender);

		Mockito.when(repository.getTender(tender)).thenReturn(new ArrayList<TenderNotice>());

		Assert.assertEquals(HttpStatus.OK, tenderNoticePublicationService.getTender(infoWrapper).getStatusCode());
	}

	@Test
	public void testUpdateTender() throws Exception {

		JSONArray docs = new JSONArray();

		TenderNotice tender = TenderNotice.builder().tenderNoticeUuid("abe3a709-50fb-4c61-b3f5-e4d4eaaaf3fd")
				.moduleCode("PR").tenantId("ch.chandigarh").tenderDocument(docs).tenderSubject("Tender Subject")
				.build();

		AuditDetails auditDetails = AuditDetails.builder().createdBy("1").createdTime(1546515646L).lastModifiedBy("1")
				.lastModifiedTime(15645455L).build();

		RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().auditDetails(auditDetails).requestBody(tender)
				.build();

		Mockito.when(objectMapper.convertValue(infoWrapper.getRequestBody(), TenderNotice.class)).thenReturn(tender);

		Assert.assertEquals(HttpStatus.OK, tenderNoticePublicationService.updateTender(infoWrapper).getStatusCode());

	}

	@Test(expected = CustomException.class)
	public void testUpdateTender_1() throws Exception {
		RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().auditDetails(null).requestBody(null).build();
		Assert.assertEquals(HttpStatus.CREATED,
				tenderNoticePublicationService.updateTender(infoWrapper).getStatusCode());
	}

	@Test
	public void testPublishTender() throws Exception {

		JSONArray docs = new JSONArray();

		TenderNotice tender = TenderNotice.builder().tenderNoticeUuid("abe3a709-50fb-4c61-b3f5-e4d4eaaaf3fd")
				.moduleCode("PR").tenantId("ch.chandigarh").tenderDocument(docs).build();

		Template template = Template.builder().moduleCode("PR").tenantId("ch.chandigarh").documentAttachment(docs)
				.emailContent(docs).moduleName("").templateType("typre").build();

		AuditDetails auditDetails = AuditDetails.builder().createdBy("1").createdTime(1546515646L).lastModifiedBy("1")
				.lastModifiedTime(15645455L).build();

		ResponseInfo workflowResponse = ResponseInfo.builder().status("successful").build();

		Mockito.when(idgen.createWorkflowRequest(Matchers.anyObject())).thenReturn(workflowResponse);

		RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().auditDetails(auditDetails).requestBody(tender)
				.build();

		List<TenderNotice> existingPress = new ArrayList<>();
		existingPress.add(tender);
		Mockito.when(repository.getTenderDetails(Matchers.any(TenderNotice.class))).thenReturn(existingPress);
		Mockito.when(objectMapper.convertValue(infoWrapper.getRequestBody(), TenderNotice.class)).thenReturn(tender);
		Mockito.when(objectMapper.convertValue(infoWrapper.getRequestBody(), Template.class)).thenReturn(template);
		Assert.assertEquals(HttpStatus.OK, tenderNoticePublicationService.publish(infoWrapper).getStatusCode());

	}
	@Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void createTenderThrowsCustomException() {
        thrown.expect(CustomException.class);
             RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().auditDetails(null).requestBody(null).build();
             TenderNoticePublicationService obj = new TenderNoticePublicationService(repository, config, objectMapper, idgenrepository, deviceSource, idgen);
        obj.createTender(infoWrapper,"user-agent");
    }
    @Test
    public void updateTenderThrowsCustomException() {
        thrown.expect(CustomException.class);
             RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().auditDetails(null).requestBody(null).build();
             TenderNoticePublicationService obj = new TenderNoticePublicationService(repository, config, objectMapper, idgenrepository, deviceSource, idgen);
        obj.updateTender(infoWrapper);
    }
    @Test
    public void publishThrowsCustomException() {
        thrown.expect(CustomException.class);
             RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().auditDetails(null).requestBody(null).build();
             TenderNoticePublicationService obj = new TenderNoticePublicationService(repository, config, objectMapper, idgenrepository, deviceSource, idgen);
        obj.publish(infoWrapper);
    }

}
