package org.egov.prscp.repository;

import java.util.List;

import org.egov.prscp.repository.builder.PrQueryBuilder;
import org.egov.prscp.repository.rowmapper.CommitteeMasterRowMapper;
import org.egov.prscp.web.models.CommitteeDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class EventCommitteeMasterRepository {

	private JdbcTemplate jdbcTemplate;

	private CommitteeMasterRowMapper rowMapper;

	@Autowired
	public EventCommitteeMasterRepository(JdbcTemplate jdbcTemplate, CommitteeMasterRowMapper rowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.rowMapper = rowMapper;
	}
	/**
     * Searches committee for given committeeuuid in database
     * @param committee object
     * @return List of committee from search
     */
	public List<CommitteeDetail> getCommittee(CommitteeDetail committeeDetail) {
		return jdbcTemplate.query(PrQueryBuilder.GET_COMMITTEE_DEATILS,
				new Object[] { committeeDetail.getTenantId(), committeeDetail.getModuleCode(),
						committeeDetail.getCommitteeUuid(), committeeDetail.getCommitteeUuid(),
						committeeDetail.getCommitteeName(), committeeDetail.getCommitteeName() },
				rowMapper);
	}
	/**
     * Searches committee for given committeeName in database
     * @param committee object
     * @return List of committee from search
     */
	public List<CommitteeDetail> getCommitteeByName(CommitteeDetail committeeDetail) {
		return jdbcTemplate.query(PrQueryBuilder.GET_COMMITTEE_DEATILS,
				new Object[] { committeeDetail.getTenantId(), committeeDetail.getModuleCode(), "", "",
						committeeDetail.getCommitteeName(), committeeDetail.getCommitteeName() },
				rowMapper);
	}

}
