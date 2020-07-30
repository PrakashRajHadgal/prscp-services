
package org.egov.prscp.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.prscp.config.PrScpConfiguration;
import org.egov.prscp.producer.Producer;
import org.egov.prscp.repository.rowmapper.TenderNoticeRowMapper;
import org.egov.prscp.web.models.NotificationReceiver;
import org.egov.prscp.web.models.NotificationTemplate;
import org.egov.prscp.web.models.PublicationList;
import org.egov.prscp.web.models.TenderNotice;
import org.json.simple.JSONArray;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
public class TenderNoticePublicationRepositoryTest {

	@Mock
	private JdbcTemplate jdbcTemplate;

	@Mock
	private Producer producer;

	@Mock
	private TenderNoticeRowMapper rowMapper;

	@Mock
	private ObjectMapper objectMapper;

	@InjectMocks
	private TenderNoticePublicationRepository repository;

	@Spy
	private PrScpConfiguration config;

	@Before
	public void setUp() {
		ReflectionTestUtils.setField(config, "periodTenderNotice", "7");
	}

	@Test
	public void isValidTenderUuid() throws Exception {

		PublicationList publicationList = new PublicationList();
		publicationList.setPublicationName("Test");
		publicationList.setPersonnelName("TestUser");
		publicationList.setEmail("test@test.com");
		publicationList.setMobile("265454545544");

		List<PublicationList> list = new ArrayList<>();
		list.add(publicationList);

		JSONArray jsonArray = new JSONArray();

		TenderNotice tender = TenderNotice.builder().tenderNoticeUuid("abe3a709-50fb-4c61-b3f5-e4d4eaaaf3fd")
				.moduleCode("PR").tenantId("ch.chandigarh").tenderDocument(jsonArray).publicationList(list).build();

		Integer i = 1;

		Mockito.when(jdbcTemplate.queryForObject(Matchers.anyString(),
				new Object[] { Matchers.anyString(), Matchers.anyString() }, Integer.class)).thenReturn(i);

		repository.isValidTenderUuid(tender);

	}

	@Test
	public void testGetTender() {

		TenderNotice tender = TenderNotice.builder().tenderNoticeUuid("abe3a709-50fb-4c61-b3f5-e4d4eaaaf3fd")
				.moduleCode("PR").defaultGrid(true).tenantId("ch.chandigarh").build();

		List<TenderNotice> list = new ArrayList<>();
		list.add(tender);

		Mockito.when(jdbcTemplate.query(Matchers.anyString(), Matchers.any(Object[].class),
				Matchers.any(ResultSetExtractor.class))).thenReturn(list);

		Assert.assertEquals(tender.getTenderNoticeStatus(),
				repository.getTender(new TenderNotice()).get(0).getTenderNoticeStatus());

	}

	@Test
	public void testUpdateTender() throws Exception {

		PublicationList publicationList = new PublicationList();
		publicationList.setPublicationName("Test");
		publicationList.setPersonnelName("TestUser");
		publicationList.setEmail("test@test.com");
		publicationList.setMobile("265454545544");

		List<PublicationList> list = new ArrayList<>();
		list.add(publicationList);

		JSONArray jsonArray = new JSONArray();
		TenderNotice tender = TenderNotice.builder().tenderNoticeUuid("abe3a709-50fb-4c61-b3f5-e4d4eaaaf3fd")
				.moduleCode("PR").tenantId("ch.chandigarh").tenderDocument(jsonArray).publicationList(list).build();

		repository.updateTender(tender);

	}

	@Test
	public void testCreateTender() throws Exception {

		PublicationList publicationList = new PublicationList();
		publicationList.setPublicationName("Test");
		publicationList.setPersonnelName("TestUser");
		publicationList.setEmail("test@test.com");
		publicationList.setMobile("265454545544");

		List<PublicationList> list = new ArrayList<>();
		list.add(publicationList);
		JSONArray jsonArray = new JSONArray();
		TenderNotice tender = TenderNotice.builder().tenderNoticeUuid("abe3a709-50fb-4c61-b3f5-e4d4eaaaf3fd")
				.moduleCode("PR").tenantId("ch.chandigarh").tenderDocument(jsonArray).publicationList(list).build();

		repository.createTender(tender);

	}

	@Test
	public void testPublishTender() throws Exception {
		PublicationList publicationList = new PublicationList();
		publicationList.setPublicationName("Test");
		publicationList.setPersonnelName("TestUser");
		publicationList.setEmail("test@test.com");
		publicationList.setMobile("265454545544");

		List<PublicationList> list = new ArrayList<>();
		list.add(publicationList);

		NotificationTemplate notificationTemplate = NotificationTemplate.builder().build();
		JSONArray jsonArray = new JSONArray();
		NotificationReceiver notificationReceiver = NotificationReceiver.builder().build();
		JSONArray pressList = new JSONArray();
		pressList.add("uuiidd");

		TenderNotice tender = TenderNotice.builder().tenderNoticeUuid("abe3a709-50fb-4c61-b3f5-e4d4eaaaf3fd")
				.moduleCode("PR").tenantId("ch.chandigarh").tenderDocument(jsonArray).publicationList(list).build();

		repository.publish(tender, notificationTemplate, notificationReceiver);

	}

}
