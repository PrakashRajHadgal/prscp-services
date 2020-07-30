package org.egov.prscp.repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.prscp.config.PrScpConfiguration;
import org.egov.prscp.producer.Producer;
import org.egov.prscp.repository.builder.PrQueryBuilder;
import org.egov.prscp.repository.rowmapper.PressMasterRowMapper;
import org.egov.prscp.repository.rowmapper.PressNoteMapper;
import org.egov.prscp.util.ErrorConstants;
import org.egov.prscp.web.models.NotificationReceiver;
import org.egov.prscp.web.models.PressMaster;
import org.egov.prscp.web.models.PressNote;
import org.egov.prscp.web.models.RequestInfoWrapper;
import org.egov.tracer.model.CustomException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class GeneratePressNotesRepository {

	private JdbcTemplate jdbcTemplate;

	private PressNoteMapper pressnoterowMapper;

	private Producer producer;

	private PrScpConfiguration config;

	private PressMasterRowMapper pressMasterRowMapper;

	@Autowired
	public GeneratePressNotesRepository(JdbcTemplate jdbcTemplate, PressNoteMapper pressnoterowMapper,
			PressMasterRowMapper pressMasterRowMapper, Producer producer, PrScpConfiguration config) {
		this.jdbcTemplate = jdbcTemplate;
		this.pressnoterowMapper = pressnoterowMapper;
		this.pressMasterRowMapper = pressMasterRowMapper;
		this.producer = producer;
		this.config = config;
	}

	/**
     * Searches press note for given press note date ,file number in database
     * @param pressNote object
     * @return List of press note from search
     */
	public List<PressNote> getPressNote(PressNote pressNote) {
		LocalDate today = LocalDate.now();
		LocalDate periodDate = today.minusDays(Integer.parseInt(config.getPeriodPressNote()));
		String periodDays = (!pressNote.isDefaultGrid() ? "" : periodDate.toString());
		return jdbcTemplate.query(PrQueryBuilder.GET_PRESS_NOTE_QUERY,
				new Object[] { pressNote.getTenantId(), pressNote.getModuleCode(), pressNote.getPressNoteUuid(),
						pressNote.getPressNoteUuid(), pressNote.getFileNumber(), pressNote.getFileNumber(),
						pressNote.getPressNoteSubject(), pressNote.getPressNoteSubject(), pressNote.getFromDate(),
						pressNote.getFromDate(), pressNote.getToDate(), pressNote.getToDate(), periodDays, periodDays },
				pressnoterowMapper);
	}
	/**
     * Searches press note  in database
     * @param pressNote object
     * @return List of press master from search
     */
	public List<PressMaster> getPressNoteePressList(PressNote pressNote) {
		return jdbcTemplate.query(PrQueryBuilder.GET_PRESSNOTE_PRESS,
				new Object[] { pressNote.getTenantId(), pressNote.getModuleCode(), pressNote.getPressNoteUuid() },
				pressMasterRowMapper);
	}
	
	/**
     * Pushes the request on save topic
     *
     * @param JSONObject to generate press note
     */

	public void uploadPressNote(JSONObject data) {
		RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().requestBody(data).build();
		producer.push(config.getPressNoteSaveTopic(), infoWrapper);
	}
	

    /**
     * Searches press note for given press note date ,file number in database
     * @param pressNote object
     */
	public void checkPressNote(PressNote pressNote) {
		Map<String, String> errorMap = new HashMap<>();
		int i= jdbcTemplate.queryForObject(PrQueryBuilder.GET_PRESS_NOTE_EXIST_QUERY,
				new Object[] {pressNote.getPressNoteDate(),pressNote.getFileNumber(), pressNote.getTenantId(), pressNote.getModuleCode() },
				Integer.class);

		if(i>0)
		{
			errorMap.put(ErrorConstants.INVALID_PRESSNOTE,ErrorConstants.INVALID_PRESSNOTE_MESSAGE);
			throw new CustomException(errorMap);
		}
	}
	/**
     * Pushes the request on send invitation topic
     *
     * @param notificationReceiver to send press note notification
     */
	public void sendInvitation(NotificationReceiver notificationReceiver) {
		producer.push(config.getInvitationSendTopic(), notificationReceiver);
	}
	/**
     * Pushes the request on update topic
     *
     * @param JSONObject to update press note 
     */
	public void updatePressNote(JSONObject data) {
		RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().requestBody(data).build();
		producer.push(config.getPressNoteUpdateTopic(), infoWrapper);
	}

	/**
     * Searches press note in database for particular press note uuid
     * @param pressNote object
     */
	public Integer checkpressNote(PressNote pressNote) {
		return jdbcTemplate.queryForObject(PrQueryBuilder.GET_PRESS_NOTE_UUID_QUERY,
				new Object[] { pressNote.getTenantId(), pressNote.getModuleCode(), pressNote.getPressNoteUuid() },
				Integer.class);
	}

}
