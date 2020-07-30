package org.egov.prscp.repository;

import java.time.LocalDate;
import java.util.List;

import org.egov.prscp.config.PrScpConfiguration;
import org.egov.prscp.producer.Producer;
import org.egov.prscp.repository.builder.PrQueryBuilder;
import org.egov.prscp.repository.rowmapper.PressMasterRowMapper;
import org.egov.prscp.web.models.PressMaster;
import org.egov.prscp.web.models.RequestInfoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class EventPressMasterRepository {

	private JdbcTemplate jdbcTemplate;

	private Producer producer;

	private PrScpConfiguration config;

	private PressMasterRowMapper pressMasterRowMapper;

	@Autowired
	public EventPressMasterRepository(JdbcTemplate jdbcTemplate, Producer producer, PrScpConfiguration config,
			PressMasterRowMapper pressMasterRowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.producer = producer;
		this.config = config;
		this.pressMasterRowMapper = pressMasterRowMapper;
	}
	/**
     * Pushes the request on save topic
     *
     * @param PressMaster to create press master 
     */
	public void createPress(PressMaster pressMaster) {
		RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().requestBody(pressMaster).build();
		producer.push(config.getPressMasterSaveTopic(), infoWrapper);
	}

	/**
     * Pushes the request on update topic
     *
     * @param PressMaster to update press master 
     */
	public void updatePress(PressMaster pressMaster) {
		RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().requestBody(pressMaster).build();
		producer.push(config.getPressMasterUpdateTopic(), infoWrapper);
	}

	/**
     * Searches press master in database 
     * @param PressMaster object
     */
	public List<PressMaster> getPress(PressMaster pressMaster) {
		LocalDate today = LocalDate.now();
		LocalDate periodDate = today.minusDays(Integer.parseInt(config.getPeriodTenderNotice()));
		String periodDays = (!pressMaster.isDefaultGrid() ? "" : periodDate.toString());

		return jdbcTemplate.query(PrQueryBuilder.GET_PRESS_MASTER,
				new Object[] { pressMaster.getTenantId(), pressMaster.getModuleCode(), pressMaster.getPressMasterUuid(),
						pressMaster.getPressMasterUuid(), pressMaster.getPersonnelName(),
						pressMaster.getPersonnelName(), pressMaster.getPublicationName(),
						pressMaster.getPublicationName(), pressMaster.getPressType(), pressMaster.getPressType(),
						periodDays, periodDays },
				pressMasterRowMapper);
	}
	/**
     * Pushes the request on delete topic
     *
     * @param PressMaster to delete press master 
     */
	public void deletePress(PressMaster pressMaster) {
		RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().requestBody(pressMaster).build();
		producer.push(config.getPressMasterDeleteTopic(), infoWrapper);
	}
}
