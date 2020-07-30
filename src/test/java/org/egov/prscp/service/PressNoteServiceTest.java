package org.egov.prscp.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.prscp.repository.GeneratePressNotesRepository;
import org.egov.prscp.util.DeviceSource;
import org.egov.prscp.web.models.AuditDetails;
import org.egov.prscp.web.models.PressNote;
import org.egov.prscp.web.models.PublicationList;
import org.egov.prscp.web.models.RequestInfoWrapper;
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
public class PressNoteServiceTest {

	@Mock
	private ObjectMapper objectMapper;

	@Mock
	private GeneratePressNotesRepository repository;
	@Mock
	private DeviceSource deviceSource;
	@InjectMocks
	private GeneratePressNotesService eventPressNoteManagement;

	@Test
	public void testUploadPressNote() {

		JSONArray emailContent = new JSONArray();
		emailContent.add("ggs");
		JSONArray documentAttachment = new JSONArray();
		documentAttachment.add("ggs");
		ArrayList<PublicationList> publicationList = new ArrayList<>();
		PublicationList pr = new PublicationList();
		pr.setPersonnelName("xyz");
		publicationList.add(pr);
		PressNote pressNote = PressNote.builder().emailContent(emailContent).documentAttachment(documentAttachment)
				.publicationList(publicationList).moduleCode("PR").build();

		AuditDetails auditDetails = AuditDetails.builder().createdBy("1").createdTime(1546515646L).lastModifiedBy("1")
				.lastModifiedTime(15645455L).build();
		RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().auditDetails(auditDetails).requestBody(pressNote)
				.build();
		Mockito.when(objectMapper.convertValue(infoWrapper.getRequestBody(), PressNote.class)).thenReturn(pressNote);

		Mockito.when(deviceSource.saveDeviceDetails(Matchers.anyString(), Matchers.anyString(), Matchers.anyString(),
				Matchers.anyString(), Matchers.any(AuditDetails.class))).thenReturn("asdasdwdsd");
		Assert.assertEquals(HttpStatus.CREATED,
				eventPressNoteManagement.createPressNote(infoWrapper, "").getStatusCode());
	}

	@Test(expected = CustomException.class)
	public void testUploadPressNote_1() {
		RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().auditDetails(null).requestBody(null).build();
		Assert.assertEquals(HttpStatus.CREATED,
				eventPressNoteManagement.createPressNote(infoWrapper, "").getStatusCode());
	}

	@Test
	public void testDeletePressNote() {
		PublicationList publicationList = new PublicationList();
		List<PublicationList> list = new ArrayList<>();
		list.add(publicationList);
		JSONArray jsonArray = new JSONArray();
		JSONArray documentAttachment = new JSONArray();
		PressNote pressNote = PressNote.builder().moduleCode("Test").pressNoteUuid("aasdjiasdu8ahs89asdy8a9h")
				.emailContent(jsonArray).documentAttachment(documentAttachment).publicationList(list).build();
		AuditDetails auditDetails = AuditDetails.builder().createdBy("1").createdTime(1546515646L).lastModifiedBy("1")
				.lastModifiedTime(15645455L).build();
		RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().auditDetails(auditDetails).requestBody(pressNote)
				.build();
		Mockito.when(objectMapper.convertValue(infoWrapper.getRequestBody(), PressNote.class)).thenReturn(pressNote);
		Mockito.when(repository.checkpressNote(Matchers.any(PressNote.class))).thenReturn(1);

		Assert.assertEquals(HttpStatus.OK, eventPressNoteManagement.updatePressNote(infoWrapper).getStatusCode());
	}

	@Test
	public void testGetPressNote() {

		PressNote pressNote = PressNote.builder().pressNoteUuid("aasdjiasdu8ahs89asdy8a9h").moduleCode("Test").build();
		RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().requestBody(pressNote).build();
		Mockito.when(objectMapper.convertValue(infoWrapper.getRequestBody(), PressNote.class)).thenReturn(pressNote);

		Mockito.when(repository.getPressNote(pressNote)).thenReturn(new ArrayList<PressNote>());

		Assert.assertEquals(HttpStatus.OK, eventPressNoteManagement.getPressNote(infoWrapper).getStatusCode());
	}
	@Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void createPressNoteThrowsCustomException() {
        thrown.expect(CustomException.class);
             RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().auditDetails(null).requestBody(null).build();
             GeneratePressNotesService obj = new GeneratePressNotesService(objectMapper, repository, deviceSource, null);
        obj.createPressNote(infoWrapper,"user-agent");
    }
  
}
