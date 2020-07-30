package org.egov.prscp.web.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.egov.prscp.service.NotificationTemplateService;
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
@WebMvcTest(NotificationTemplateController.class)
@Import(TestConfiguration.class)
@AutoConfigureMockMvc
public class NotificationTemplateControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private NotificationTemplateService notificationTemplateService;

	@MockBean
	private KafkaTemplate kafkaTemplate;

	@MockBean
	private JdbcTemplate jdbcTemplate;

	@Test
	public void testGetTemplate() throws IOException, Exception {
		Mockito.when(notificationTemplateService.getTemplate(Matchers.any(RequestInfoWrapper.class)))
				.thenReturn(new ResponseEntity(RequestInfoWrapper.builder().build(), HttpStatus.OK));

		mockMvc.perform(MockMvcRequestBuilders.post("/v1/template/_gettemplate")
				.content(getFileContents("junitResource/Notification/getTemplate.json"))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

	}

	@Test
	public void testResendInvitation() throws IOException, Exception {
		Mockito.when(notificationTemplateService.resendInvitation(Matchers.any(RequestInfoWrapper.class)))
				.thenReturn(new ResponseEntity(RequestInfoWrapper.builder().build(), HttpStatus.OK));

		mockMvc.perform(MockMvcRequestBuilders.post("/v1/template/_resend")
				.content(getFileContents("junitResource/Notification/resend.json"))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	private String getFileContents(String fileName) throws IOException {
		return IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream(fileName), "UTF-8");
	}
}
