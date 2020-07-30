package org.egov.prscp.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.prscp.config.PrScpConfiguration;
import org.egov.prscp.producer.Producer;
import org.egov.prscp.web.models.PressMaster;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

@RunWith(MockitoJUnitRunner.class)
public class EventPressMasterRepositoryTest {

	@InjectMocks
	private EventPressMasterRepository repository;

	@Mock
	private JdbcTemplate jdbcTemplate;

	@Mock
	private Producer producer;

	@Mock
	private PrScpConfiguration config;

	@Test
	public void testCreatePress() {
		PressMaster master = PressMaster.builder().personnelName("Test").build();
		repository.createPress(master);
	}

	@Test
	public void testUpdatePress() {
		PressMaster master = PressMaster.builder().personnelName("Test").pressMasterUuid("ncw8qhc327yddb72fd23b2")
				.build();
		repository.updatePress(master);
	}

	@Test
	public void testGetPress() {

		PressMaster master = PressMaster.builder().pressMasterUuid("ncw8qhc327yddb72fd23b2").build();
		List<PressMaster> list = new ArrayList<>();
		list.add(master);

		Mockito.when(jdbcTemplate.query(Matchers.anyString(), Matchers.any(Object[].class),
				Matchers.any(ResultSetExtractor.class))).thenReturn(list);

		Mockito.when(config.getPeriodTenderNotice()).thenReturn("2");

		Assert.assertEquals(master.getPublicationName(),
				repository.getPress(new PressMaster()).get(0).getPublicationName());
	}

	@Test
	public void testDeletePress() {
		PressMaster master = PressMaster.builder().pressMasterUuid("ncw8qhc327yddb72fd23b2").build();
		repository.deletePress(master);
	}

}
