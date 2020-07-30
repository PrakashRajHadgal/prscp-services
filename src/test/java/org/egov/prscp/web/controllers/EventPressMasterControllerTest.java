package org.egov.prscp.web.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.egov.prscp.service.EventPressMasterService;
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
@WebMvcTest(EventPressMasterController.class)
@Import(TestConfiguration.class)
@AutoConfigureMockMvc
public class EventPressMasterControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private EventPressMasterService eventPressMaster;

	@MockBean
	private KafkaTemplate kafkaTemplate;

	@MockBean
	private JdbcTemplate jdbcTemplate;

	@Test
	public void testCreatePress() throws Exception {

		Mockito.when(eventPressMaster.createPress(Matchers.any(RequestInfoWrapper.class)))
				.thenReturn(new ResponseEntity(RequestInfoWrapper.builder().build(), HttpStatus.CREATED));

		mockMvc.perform(MockMvcRequestBuilders.post("/v1/press/_create")
				.content(getFileContents("junitResource/PressMaster/create.json"))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());
	}

	@Test
	public void testUpdatePress() throws Exception {
		Mockito.when(eventPressMaster.updatePress(Matchers.any(RequestInfoWrapper.class)))
				.thenReturn(new ResponseEntity(RequestInfoWrapper.builder().build(), HttpStatus.OK));

		mockMvc.perform(MockMvcRequestBuilders.post("/v1/press/_update")
				.content(getFileContents("junitResource/PressMaster/update.json"))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void testGetPress() throws Exception {
		Mockito.when(eventPressMaster.getPress(Matchers.any(RequestInfoWrapper.class)))
				.thenReturn(new ResponseEntity(RequestInfoWrapper.builder().build(), HttpStatus.OK));

		mockMvc.perform(MockMvcRequestBuilders.post("/v1/press/_get")
				.content(getFileContents("junitResource/PressMaster/get.json")).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void testDeletePress() throws Exception {
		Mockito.when(eventPressMaster.deletePress(Matchers.any(RequestInfoWrapper.class)))
				.thenReturn(new ResponseEntity(RequestInfoWrapper.builder().build(), HttpStatus.OK));

		mockMvc.perform(MockMvcRequestBuilders.post("/v1/press/_delete")
				.content(getFileContents("junitResource/PressMaster/delete.json"))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	private String getFileContents(String fileName) throws IOException {
		return IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream(fileName), "UTF-8");
	}
}
