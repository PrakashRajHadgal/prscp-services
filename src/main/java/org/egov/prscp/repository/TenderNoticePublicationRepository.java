package org.egov.prscp.repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.egov.prscp.config.PrScpConfiguration;
import org.egov.prscp.producer.Producer;
import org.egov.prscp.repository.builder.PrQueryBuilder;
import org.egov.prscp.repository.rowmapper.PressMasterRowMapper;
import org.egov.prscp.repository.rowmapper.TenderNoticeRowMapper;
import org.egov.prscp.util.ErrorConstants;
import org.egov.prscp.web.models.MapTenderPress;
import org.egov.prscp.web.models.NotificationReceiver;
import org.egov.prscp.web.models.NotificationTemplate;
import org.egov.prscp.web.models.PressMaster;
import org.egov.prscp.web.models.PressNote;
import org.egov.prscp.web.models.RequestInfoWrapper;
import org.egov.prscp.web.models.TenderNotice;
import org.egov.tracer.model.CustomException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class TenderNoticePublicationRepository {

	private JdbcTemplate jdbcTemplate;

	private TenderNoticeRowMapper rowMapper;

	private Producer producer;

	private PrScpConfiguration config;

	private PressMasterRowMapper pressMasterRowMapper;

	@Autowired
	public TenderNoticePublicationRepository(JdbcTemplate jdbcTemplate, PrQueryBuilder queryBuilder,
			TenderNoticeRowMapper rowMapper, Producer producer, PrScpConfiguration config,
			PressMasterRowMapper pressMasterRowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.rowMapper = rowMapper;
		this.producer = producer;
		this.config = config;
		this.pressMasterRowMapper = pressMasterRowMapper;
	}

	/**
     * Pushes the request on save topic
     *
     * @param JSONObject to create tender notice
     */
	public void createTender(TenderNotice tenderNotice) {
		RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().requestBody(tenderNotice).build();
		producer.push(config.getCreateTender(), infoWrapper);
	}
	

    /**
     * Searches tender for given tender date ,file number in database
     * @param tenderNotice object
     */
	public void checkTenderExist(TenderNotice tenderNotice) {
		Map<String, String> errorMap = new HashMap<>();
		int i= jdbcTemplate.queryForObject(PrQueryBuilder.GET_TENDER_NOTICE_EXIST_QUERY,
				new Object[] {tenderNotice.getTenderDate(),tenderNotice.getFileNumber(), tenderNotice.getTenantId(), tenderNotice.getModuleCode() },
				Integer.class);

		if(i>0)
		{
			errorMap.put(ErrorConstants.INVALID_TENDER_NOTICE,ErrorConstants.INVALID_TENDER_NOTICE_MESSAGE);
			throw new CustomException(errorMap);
		}
	}
	/**
     * Searches tender for given tender uuid
     * @param tenderNotice object
     */
	public void isValidTenderUuid(TenderNotice tenderNotice) {
		Integer isAvail = jdbcTemplate.queryForObject(PrQueryBuilder.GET_TENDER_AVAILABLE, new Object[] {
				tenderNotice.getModuleCode(), tenderNotice.getTenantId(), tenderNotice.getTenderNoticeUuid() },
				Integer.class);
		if (isAvail == null || isAvail == 0)
			throw new CustomException("TENDERNOTICE_EXCEPTION", "Invalid Tender Notice Id");
	}
	/**
     * Pushes the request on update topic
     *
     * @param JSONObject to update tender notice
     */
	public void updateTender(TenderNotice tenderNotice) {
		RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().requestBody(tenderNotice).build();
		producer.push(config.getUpdateTender(), infoWrapper);
	}

	
	/**
     * Searches tender for given tender uuid
     * @param tenderNotice object
     * @return TenderNotice List
     */
	public List<TenderNotice> getTender(TenderNotice tenderNotice) {

		LocalDate today = LocalDate.now();
		LocalDate periodDate = today.minusDays(Integer.parseInt(config.getPeriodTenderNotice()));
		String periodDays = (!tenderNotice.isDefaultGrid() ? "" : periodDate.toString());

		return jdbcTemplate.query(PrQueryBuilder.FIND_TENDER,
				new Object[] { tenderNotice.getTenantId(), tenderNotice.getModuleCode(),
						tenderNotice.getTenderNoticeStatus(), tenderNotice.getTenderNoticeStatus(),
						tenderNotice.getTenderNoticeUuid(), tenderNotice.getTenderNoticeUuid(),
						tenderNotice.getTenderNoticeId(), tenderNotice.getTenderNoticeId(),
						tenderNotice.getFileNumber(), tenderNotice.getFileNumber(), tenderNotice.getTenderSubject(),
						tenderNotice.getTenderSubject(), tenderNotice.getFromDate(), tenderNotice.getFromDate(),
						tenderNotice.getToDate(), tenderNotice.getToDate(), periodDays, periodDays },
				rowMapper);
	}
	/**
     * Searches tender for given tender uuid
     * @param tenderNotice object
     * @return TenderNotice List
     */
	public List<TenderNotice> getTenderDetails(TenderNotice tenderNotice) {
		LocalDate today = LocalDate.now();
		LocalDate periodDate = today.minusDays(Integer.parseInt(config.getPeriodTenderNotice()));
		String periodDays = (!tenderNotice.isDefaultGrid() ? "" : periodDate.toString());

		return jdbcTemplate.query(PrQueryBuilder.FIND_TENDER,
				new Object[] { tenderNotice.getTenantId(), tenderNotice.getModuleCode(), "", "",
						tenderNotice.getTenderNoticeUuid(), tenderNotice.getTenderNoticeUuid(),
						tenderNotice.getTenderNoticeId(), tenderNotice.getTenderNoticeId(), "", "", "", "", "", "", "",
						"", periodDays, periodDays },
				rowMapper);
	}


	/**
	 * Searches tender notice  in database
	 * @param tenderNotice object
	 * @return List of press master from search
	 */
	public List<PressMaster> getTenderPress(TenderNotice tenderNotice) {
		return jdbcTemplate.query(PrQueryBuilder.GET_TENDER_PRESS, new Object[] { tenderNotice.getTenantId(),
				tenderNotice.getModuleCode(), tenderNotice.getTenderNoticeUuid() }, pressMasterRowMapper);
	}
	/**
     * Pushes the request on update topic
     * updates workflow status to PUBLISHED
     * @param JSONObject to update tender notice
     */
	public void publish(TenderNotice tenderNotice, NotificationTemplate template,
			NotificationReceiver notificationReceiver) {

		JSONObject obj = new JSONObject();
		obj.put("TenderNotice", tenderNotice);
		obj.put("Template", template);

		List<MapTenderPress> mapPres = tenderNotice.getPublicationList().stream().map(mapper -> MapTenderPress.builder()
				.mapTenderPressUuid(UUID.randomUUID().toString()).tenderNoticeUuid(tenderNotice.getTenderNoticeUuid())
				.pressMasterUuid(mapper.getPressMasterUuid()).tenantId(tenderNotice.getTenantId())
				.createdBy(tenderNotice.getCreatedBy()).createdTime(tenderNotice.getCreatedTime())
				.lastModifiedBy(tenderNotice.getLastModifiedBy()).lastModifiedTime(tenderNotice.getLastModifiedTime())
				.moduleCode(tenderNotice.getModuleCode()).isActive(true).build()).collect(Collectors.toList());

		RequestInfoWrapper infoWrapperTenderPress = RequestInfoWrapper.builder().requestBody(mapPres).build();
		producer.push(config.getUpdatePublishedTenderMapPress(), infoWrapperTenderPress);

		RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().requestBody(obj).build();
		producer.push(config.getUpdatePublishedTender(), infoWrapper);
		producer.push(config.getInvitationSendTopic(), notificationReceiver);
	}
}
