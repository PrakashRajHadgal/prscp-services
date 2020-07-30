package org.egov.prscp.repository;


import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.prscp.config.PrScpConfiguration;
import org.egov.prscp.producer.Producer;
import org.egov.prscp.repository.builder.PrQueryBuilder;
import org.egov.prscp.web.models.DocumentList;
import org.egov.prscp.web.models.EventDetail;
import org.egov.prscp.web.models.NotificationReceiver;
import org.egov.prscp.web.models.RequestInfoWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import junit.framework.Assert;

@RunWith(MockitoJUnitRunner.class)
public class EventManagementRepositoryTest {

	@InjectMocks
	private EventManegementRepository repository;

	@Mock
	private JdbcTemplate jdbcTemplate;

	@Mock
	private Producer producer;

	@Mock
	private PrScpConfiguration config;

	@Mock
	private PrQueryBuilder queryBuilder;

	@Test
	public void testcreateEvent() {
		ArrayList<DocumentList> ds = new ArrayList<>();

		EventDetail event = EventDetail.builder().moduleCode("pr").build();
		repository.createEvent(event);
	}

	@Test
	public void testupdateEvent() {
		EventDetail event = EventDetail.builder().moduleCode("Test").eventDetailUuid("ncw8qhc327yddb72fd23b2").build();
		repository.updateEvent(event);
	}

	@Test
	public void testGetEventDetail() {

		EventDetail eventDetail = EventDetail.builder().tenantId("ch").eventStatus("PUBLISHED").moduleCode("PR")
				.eventDetailUuid("aasdjiasdu8ahs89asdy8a9h").moduleCode("Test").build();
		RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().requestBody(eventDetail).build();
		List<EventDetail> list = new ArrayList<>();
		list.add(eventDetail);

		Mockito.when(jdbcTemplate.query(Matchers.anyString(), Matchers.any(Object[].class),
				Matchers.any(ResultSetExtractor.class))).thenReturn(list);

		repository.getEvent(eventDetail);
	}

	@Test
	public void testsendUploadNotification() {
		NotificationReceiver event = NotificationReceiver.builder().tenantId("ncw8qhc327yddb72fd23b2").build();
		repository.sendEventUpdateNotification(event);
	}

	@Test
	public void testupdateEventStatus() {
		EventDetail eventDetail = EventDetail.builder().tenantId("ch").eventStatus("PUBLISHED").moduleCode("PR")
				.eventDetailUuid("aasdjiasdu8ahs89asdy8a9h").moduleCode("Test").build();
		RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().requestBody(eventDetail).build();
		repository.updateEventStatus(infoWrapper);
	}

	@Test
	public void getEventReminder() {
		List<EventDetail> list = new ArrayList<>();
		EventDetail detail = new EventDetail();
		detail.setEventTitle("Test");
		list.add(detail);
		Mockito.when(jdbcTemplate.query(Matchers.anyString(), Matchers.any(Object[].class),
				Matchers.any(ResultSetExtractor.class))).thenReturn(list);

		Assert.assertEquals("Test", repository.getEventReminder("PUBLISHED").get(0).getEventTitle());

	}
	
	

}
