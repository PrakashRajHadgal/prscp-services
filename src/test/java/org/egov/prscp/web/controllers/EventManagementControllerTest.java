package org.egov.prscp.web.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.egov.prscp.service.EventManagementService;
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
@WebMvcTest(EventManagemntController.class)
@Import(TestConfiguration.class)
@AutoConfigureMockMvc
public class EventManagementControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private EventManagementService service;

	@MockBean
	private KafkaTemplate kafkaTemplate;

	@MockBean
	private JdbcTemplate jdbcTemplate;

	@Test
	public void testcreateEvent() throws Exception {

		Mockito.when(service.createEvent(Matchers.any(RequestInfoWrapper.class), Matchers.anyString()))
				.thenReturn(new ResponseEntity(RequestInfoWrapper.builder().build(), HttpStatus.CREATED));

		mockMvc.perform(MockMvcRequestBuilders.post("/v1/event/_create").header("user-agent", "dbdb")
				.content(getFileContents("junitResource/Event/create.json")).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
	}

	@Test
	public void testgetEvent() throws Exception {
		Mockito.when(service.getEvent(Matchers.any(RequestInfoWrapper.class)))
				.thenReturn(new ResponseEntity(RequestInfoWrapper.builder().build(), HttpStatus.OK));

		mockMvc.perform(
				MockMvcRequestBuilders.post("/v1/event/_get").content(getFileContents("junitResource/Event/get.json"))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void testupdateEvent() throws Exception {
		Mockito.when(service.updateEvent(Matchers.any(RequestInfoWrapper.class)))
				.thenReturn(new ResponseEntity(RequestInfoWrapper.builder().build(), HttpStatus.OK));

		mockMvc.perform(MockMvcRequestBuilders.post("/v1/event/_update")
				.content(getFileContents("junitResource/Event/update.json")).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void testupdateStatus() throws Exception {
		Mockito.when(service.updateEvent(Matchers.any(RequestInfoWrapper.class)))
				.thenReturn(new ResponseEntity(RequestInfoWrapper.builder().build(), HttpStatus.OK));

		mockMvc.perform(MockMvcRequestBuilders.post("/v1/event/_updateStatus")
				.content(getFileContents("junitResource/Event/updateStatus.json"))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	private String getFileContents(String fileName) throws IOException {
		return IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream(fileName), "UTF-8");
	}
}
