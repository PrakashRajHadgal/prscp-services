package org.egov.prscp.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.prscp.config.PrScpConfiguration;
import org.egov.prscp.producer.Producer;
import org.egov.prscp.web.models.CommitteeDetail;
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
public class EventCommitteeMasterRepositoryTest {

	@InjectMocks
	private EventCommitteeMasterRepository repository;

	@Mock
	private JdbcTemplate jdbcTemplate;

	@Mock
	private Producer producer;

	@Mock
	private PrScpConfiguration config;

	@SuppressWarnings("unchecked")
	@Test
	public void testGetCommittee() {

		CommitteeDetail committeeDetail = CommitteeDetail.builder()
				.committeeUuid("ee9c4698-3e55-4bd2-a4a2-352b48c0bdb0").build();

		List<CommitteeDetail> list = new ArrayList<>();
		list.add(committeeDetail);

		Mockito.when(jdbcTemplate.query(Matchers.anyString(), Matchers.any(Object[].class),
				Matchers.any(ResultSetExtractor.class))).thenReturn(list);

		Assert.assertEquals(committeeDetail.getCommitteeName(),
				repository.getCommittee(new CommitteeDetail()).get(0).getCommitteeName());
	}

	@Test
	public void testGetCommitteeByName() {

		CommitteeDetail committeeDetail = CommitteeDetail.builder().committeeName("Test Purpose").build();

		List<CommitteeDetail> list = new ArrayList<>();

		list.add(committeeDetail);

		Mockito.when(jdbcTemplate.query(Matchers.anyString(), Matchers.any(Object[].class),
				Matchers.any(ResultSetExtractor.class))).thenReturn(list);

		Assert.assertEquals(committeeDetail.getCommitteeName(),
				repository.getCommitteeByName(new CommitteeDetail()).get(0).getCommitteeName());
	}

}
