package org.egov.prscp.service;

import java.util.ArrayList;

import org.egov.prscp.repository.EventPressMasterRepository;
import org.egov.prscp.web.models.AuditDetails;
import org.egov.prscp.web.models.PressMaster;
import org.egov.prscp.web.models.RequestInfoWrapper;
import org.egov.tracer.model.CustomException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
public class EventPressMasterServiceTest {

	@Mock
	private ObjectMapper objectMapper;

	@Mock
	private EventPressMasterRepository repository;

	@InjectMocks
	private EventPressMasterService eventPressMasterService;

	@Test
	public void testCreatePress() {

		PressMaster pressMaster = PressMaster.builder().personnelName("Test").build();
		AuditDetails auditDetails = AuditDetails.builder().createdBy("1").createdTime(1546515646L).lastModifiedBy("1")
				.lastModifiedTime(15645455L).build();
		RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().auditDetails(auditDetails)
				.requestBody(pressMaster).build();
		Mockito.when(objectMapper.convertValue(infoWrapper.getRequestBody(), PressMaster.class))
				.thenReturn(pressMaster);

		Assert.assertEquals(HttpStatus.CREATED, eventPressMasterService.createPress(infoWrapper).getStatusCode());

	}

	@Test(expected = CustomException.class)
	public void testCreatePress_1() {
		RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().auditDetails(null).requestBody(null).build();
		eventPressMasterService.createPress(infoWrapper);
	}

	@Test
	public void testUpdatePress() {

		PressMaster pressMaster = PressMaster.builder().personnelName("Test")
				.pressMasterUuid("aasdjiasdu8ahs89asdy8a9h").build();
		AuditDetails auditDetails = AuditDetails.builder().createdBy("1").createdTime(1546515646L).lastModifiedBy("1")
				.lastModifiedTime(15645455L).build();
		RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().auditDetails(auditDetails)
				.requestBody(pressMaster).build();
		Mockito.when(objectMapper.convertValue(infoWrapper.getRequestBody(), PressMaster.class))
				.thenReturn(pressMaster);

		Assert.assertEquals(HttpStatus.OK, eventPressMasterService.updatePress(infoWrapper).getStatusCode());
	}

	@Test(expected = CustomException.class)
	public void testUpdatePress_1() {
		RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().auditDetails(null).requestBody(null).build();
		eventPressMasterService.updatePress(infoWrapper);
	}

	@Test
	public void testGetPress() {

		PressMaster pressMaster = PressMaster.builder().pressMasterUuid("aasdjiasdu8ahs89asdy8a9h")
				.personnelName("Test").build();
		RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().requestBody(pressMaster).build();
		Mockito.when(objectMapper.convertValue(infoWrapper.getRequestBody(), PressMaster.class))
				.thenReturn(pressMaster);

		Mockito.when(repository.getPress(pressMaster)).thenReturn(new ArrayList<PressMaster>());

		Assert.assertEquals(HttpStatus.OK, eventPressMasterService.getPress(infoWrapper).getStatusCode());
	}

	@Test(expected = CustomException.class)
	public void testGetPress_1() {
		eventPressMasterService.getPress(null);
	}

	@Test
	public void testDeletePress() {
		PressMaster pressMaster = PressMaster.builder().pressMasterUuid("aasdjiasdu8ahs89asdy8a9h")
				.personnelName("Test").build();
		AuditDetails auditDetails = AuditDetails.builder().createdBy("1").createdTime(1546515646L).lastModifiedBy("1")
				.lastModifiedTime(15645455L).build();
		RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().auditDetails(auditDetails)
				.requestBody(pressMaster).build();
		Mockito.when(objectMapper.convertValue(infoWrapper.getRequestBody(), PressMaster.class))
				.thenReturn(pressMaster);

		Assert.assertEquals(HttpStatus.OK, eventPressMasterService.deletePress(infoWrapper).getStatusCode());
	}

	@Test(expected = CustomException.class)
	public void testDeletePress_1() {
		RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().auditDetails(null).requestBody(null).build();
		eventPressMasterService.deletePress(infoWrapper);
	}
	@Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void createPressThrowsCustomException() {
        thrown.expect(CustomException.class);
             RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().auditDetails(null).requestBody(null).build();
             EventPressMasterService obj = new EventPressMasterService(repository, objectMapper);
        obj.createPress(infoWrapper);
    }
    @Test
    public void updatePressThrowsCustomException() {
        thrown.expect(CustomException.class);
             RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().auditDetails(null).requestBody(null).build();
             EventPressMasterService obj = new EventPressMasterService(repository, objectMapper);
        obj.updatePress(infoWrapper);
    }
}
