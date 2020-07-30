package org.egov.prscp.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.egov.prscp.repository.EventLibraryManagementRepository;
import org.egov.prscp.util.DeviceSource;
import org.egov.prscp.web.models.AuditDetails;
import org.egov.prscp.web.models.DocumentList;
import org.egov.prscp.web.models.Library;
import org.egov.prscp.web.models.RequestInfoWrapper;
import org.egov.tracer.model.CustomException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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
public class EventLibraryManagementServiceTest {

	@Mock
	private ObjectMapper objectMapper;

	@Mock
	private EventLibraryManagementRepository repository;

	@InjectMocks
	private EventLibraryManagementService eventLibraryManagement;

	@Mock
	private DeviceSource deviceSource;

	@Test
	public void testUploadLibrary() {

		Library library = Library.builder().moduleCode("PR").build();
		Mockito.when(repository.checkEventUuid(library)).thenReturn(1);
		AuditDetails auditDetails = AuditDetails.builder().createdBy("1").createdTime(1546515646L).lastModifiedBy("1")
				.lastModifiedTime(15645455L).build();
		RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().auditDetails(auditDetails).requestBody(library)
				.build();
		Mockito.when(objectMapper.convertValue(infoWrapper.getRequestBody(), Library.class)).thenReturn(library);

		Assert.assertEquals(HttpStatus.OK, eventLibraryManagement.uploadLibrary(infoWrapper, "").getStatusCode());

	}

	@Test(expected = CustomException.class)
	public void testUploadLibrary_1() {
		RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().auditDetails(null).requestBody(null).build();
		Assert.assertEquals(HttpStatus.OK, eventLibraryManagement.uploadLibrary(infoWrapper, "").getStatusCode());
	}

	@Test
	public void testDeleteLibrary() {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("documentId", "asc32f23f3");
		jsonArray.add(jsonObject);

		DocumentList docs = DocumentList.builder().documentId(jsonArray).build();
		List<DocumentList> documentList = new ArrayList<>();
		documentList.add(docs);

		Library library = Library.builder().moduleCode("Test").eventDetailUuid("aasdjiasdu8ahs89asdy8a9h")
				.documentList(documentList).build();
		AuditDetails auditDetails = AuditDetails.builder().createdBy("1").createdTime(1546515646L).lastModifiedBy("1")
				.lastModifiedTime(15645455L).build();
		RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().auditDetails(auditDetails).requestBody(library)
				.build();
		Mockito.when(objectMapper.convertValue(infoWrapper.getRequestBody(), Library.class)).thenReturn(library);

		Mockito.when(repository.checkEventUuid(Matchers.any(Library.class))).thenReturn(1);

		Assert.assertEquals(HttpStatus.OK, eventLibraryManagement.deleteLibrary(infoWrapper).getStatusCode());
	}

	@Test(expected = CustomException.class)
	public void testDeleteLibrary_1() {
		RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().auditDetails(null).requestBody(null).build();
		Mockito.when(repository.checkEventUuid(Matchers.any(Library.class))).thenReturn(null);
		Assert.assertEquals(HttpStatus.OK, eventLibraryManagement.deleteLibrary(infoWrapper).getStatusCode());
	}

	@Test
	public void testDeleteLibrary_2() {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("documentId", "asc32f23f3");
		jsonArray.add(jsonObject);

		DocumentList docs = DocumentList.builder().documentId(jsonArray).build();
		List<DocumentList> documentList = new ArrayList<>();
		documentList.add(docs);

		Library library = Library.builder().moduleCode("Test").eventDetailUuid("aasdjiasdu8ahs89asdy8a9h")
				.documentList(documentList).build();
		AuditDetails auditDetails = AuditDetails.builder().createdBy("1").createdTime(1546515646L).lastModifiedBy("1")
				.lastModifiedTime(15645455L).build();
		RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().auditDetails(auditDetails).requestBody(library)
				.build();
		Mockito.when(objectMapper.convertValue(infoWrapper.getRequestBody(), Library.class)).thenReturn(library);

		Mockito.when(repository.checkEventUuid(Matchers.any(Library.class))).thenReturn(0);

		Assert.assertEquals(HttpStatus.OK, eventLibraryManagement.deleteLibrary(infoWrapper).getStatusCode());
	}

	@Test
	public void testGetLibrary() {

		Library library = Library.builder().eventDetailUuid("aasdjiasdu8ahs89asdy8a9h").moduleCode("Test").build();
		RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().requestBody(library).build();
		Mockito.when(objectMapper.convertValue(infoWrapper.getRequestBody(), Library.class)).thenReturn(library);

		Mockito.when(repository.checkEventUuid(library)).thenReturn(1);

		Mockito.when(repository.getLibrary(library)).thenReturn(new ArrayList<Library>());

		Assert.assertEquals(HttpStatus.OK, eventLibraryManagement.getLibrary(infoWrapper).getStatusCode());
	}

	@Test(expected = CustomException.class)
	public void testGetLibrary_1() {
		RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().auditDetails(null).requestBody(null).build();
		Assert.assertEquals(HttpStatus.OK, eventLibraryManagement.getLibrary(infoWrapper).getStatusCode());
	}
	
	@Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void uploadLibraryThrowsCustomException() throws IOException {
        thrown.expect(CustomException.class);
             RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().auditDetails(null).requestBody(null).build();
             EventLibraryManagementService obj = new EventLibraryManagementService(objectMapper, repository, deviceSource);
        obj.uploadLibrary(infoWrapper,"user-agent");
    }
  
}
