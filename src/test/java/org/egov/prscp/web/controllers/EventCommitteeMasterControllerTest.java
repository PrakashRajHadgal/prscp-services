package org.egov.prscp.web.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.egov.prscp.service.EventCommitteeMasterService;
import org.egov.prscp.web.models.RequestInfoWrapper;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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
public class EventCommitteeMasterControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private EventCommitteeMasterService service;

	@MockBean
	private KafkaTemplate kafkaTemplate;

	@MockBean
	private JdbcTemplate jdbcTemplate;

	@Test
	public void testCreateCommittee() throws Exception {

		Mockito.when(service.createCommittee(Matchers.any(RequestInfoWrapper.class)))
				.thenReturn(new ResponseEntity(RequestInfoWrapper.builder().build(), HttpStatus.CREATED));

		mockMvc.perform(MockMvcRequestBuilders.post("/v1/committee/_create")
				.content(getFileContents("junitResource/EventCommitteeMaster/create.json"))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());
	}

	@Test
	public void testUpdateCommittee() throws Exception {
		Mockito.when(service.updateCommittee(Matchers.any(RequestInfoWrapper.class)))
				.thenReturn(new ResponseEntity(RequestInfoWrapper.builder().build(), HttpStatus.OK));

		mockMvc.perform(MockMvcRequestBuilders.post("/v1/committee/_update")
				.content(getFileContents("junitResource/EventCommitteeMaster/update.json"))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void testGetCommittee() throws Exception {
		Mockito.when(service.getCommittee(Matchers.any(RequestInfoWrapper.class)))
				.thenReturn(new ResponseEntity(RequestInfoWrapper.builder().build(), HttpStatus.OK));

		mockMvc.perform(MockMvcRequestBuilders.post("/v1/committee/_get")
				.content(getFileContents("junitResource/EventCommitteeMaster/get.json")).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	
	private String getFileContents(String fileName) throws IOException {
		return IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream(fileName), "UTF-8");
	}
	@Rule
	public ExpectedException exceptionRule = ExpectedException.none();
	 
	@Test
	public void whenExceptionThrown_thenRuleIsApplied() {
	    exceptionRule.expect(NumberFormatException.class);
	    exceptionRule.expectMessage("For input string");
	    Integer.parseInt("1a");
	}
}
