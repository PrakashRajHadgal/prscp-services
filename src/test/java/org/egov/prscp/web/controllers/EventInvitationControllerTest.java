package org.egov.prscp.web.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.egov.prscp.service.EventInvitationService;
import org.egov.prscp.web.models.RequestInfoWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@WebMvcTest(EventInvitationController.class)
@Import(TestConfiguration.class)
@AutoConfigureMockMvc
public class EventInvitationControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private EventInvitationService eventInvitationService;

	@MockBean
	private KafkaTemplate kafkaTemplate;

	@MockBean
	private JdbcTemplate jdbcTemplate;

	/*
	 * @Test public void testGetTemplate() throws IOException, Exception {
	 * Mockito.when(eventInvitationService.getTemplate(Matchers.any(
	 * RequestInfoWrapper.class))) .thenReturn(new
	 * ResponseEntity(RequestInfoWrapper.builder().build(), HttpStatus.OK));
	 * 
	 * mockMvc.perform(MockMvcRequestBuilders.post("/v1/invitation/_gettemplate")
	 * .content(getFileContents("junitResource/EventInvitation/getTemplate.json"))
	 * .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).
	 * andExpect(status().isOk());
	 * 
	 * }
	 */

	@Test
	public void testUplaodExternalUser() throws IOException, Exception {
		Mockito.when(eventInvitationService.uplaodExternalGuest(Matchers.any(RequestInfoWrapper.class),
				Matchers.anyString()))
				.thenReturn(new ResponseEntity(RequestInfoWrapper.builder().build(), HttpStatus.CREATED));

		mockMvc.perform(MockMvcRequestBuilders.post("/v1/invitation/guest/_upload").header("User-Agent", "")
				.content(getFileContents("junitResource/EventInvitation/uploadGuets.json"))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());

	}

	@Test
	public void testAddGuest() throws IOException, Exception {
		Mockito.when(eventInvitationService.addGuest(Matchers.any(RequestInfoWrapper.class), Matchers.anyString()))
				.thenReturn(new ResponseEntity(RequestInfoWrapper.builder().build(), HttpStatus.CREATED));

		mockMvc.perform(MockMvcRequestBuilders.post("/v1/invitation/guest/_add").header("User-Agent", "")
				.content(getFileContents("junitResource/EventInvitation/addGuest.json"))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());
	}

	@Test
	public void testDeleteGuest() throws IOException, Exception {
		Mockito.when(eventInvitationService.deleteGuest(Matchers.any(RequestInfoWrapper.class)))
				.thenReturn(new ResponseEntity(RequestInfoWrapper.builder().build(), HttpStatus.OK));

		mockMvc.perform(MockMvcRequestBuilders.post("/v1/invitation/guest/_delete")
				.content(getFileContents("junitResource/EventInvitation/deleteGuest.json"))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void testGetGuest() throws IOException, Exception {
		Mockito.when(eventInvitationService.getGuest(Matchers.any(RequestInfoWrapper.class)))
				.thenReturn(new ResponseEntity(RequestInfoWrapper.builder().build(), HttpStatus.OK));

		mockMvc.perform(MockMvcRequestBuilders.post("/v1/invitation/guest/_get")
				.content(getFileContents("junitResource/EventInvitation/getGuets.json"))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void testSendInvitations() throws IOException, Exception {
		Mockito.when(eventInvitationService.sendInvitations(Matchers.any(RequestInfoWrapper.class)))
				.thenReturn(new ResponseEntity(RequestInfoWrapper.builder().build(), HttpStatus.OK));

		mockMvc.perform(MockMvcRequestBuilders.post("/v1/invitation/_send")
				.content(getFileContents("junitResource/EventInvitation/sendInvitation.json"))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	private String getFileContents(String fileName) throws IOException {
		return IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream(fileName), "UTF-8");
	}

}
