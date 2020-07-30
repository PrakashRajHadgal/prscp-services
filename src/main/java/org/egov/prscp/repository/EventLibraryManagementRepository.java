package org.egov.prscp.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.egov.prscp.config.PrScpConfiguration;
import org.egov.prscp.producer.Producer;
import org.egov.prscp.repository.builder.PrQueryBuilder;
import org.egov.prscp.repository.rowmapper.LibraryRowMapper;
import org.egov.prscp.web.models.DocumentList;
import org.egov.prscp.web.models.Library;
import org.egov.prscp.web.models.RequestInfoWrapper;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class EventLibraryManagementRepository {

	private JdbcTemplate jdbcTemplate;

	private LibraryRowMapper libraryrowMapper;

	private Producer producer;

	private PrScpConfiguration config;

	@Autowired
	public EventLibraryManagementRepository(JdbcTemplate jdbcTemplate, LibraryRowMapper libraryrowMapper,
			Producer producer, PrScpConfiguration config) {
		this.jdbcTemplate = jdbcTemplate;
		this.libraryrowMapper = libraryrowMapper;

		this.producer = producer;
		this.config = config;
	}
	/**
     * Pushes the request on library upload topic
     *
     * @param Library Object to upload Library
     */
	public List<Library> uploadLibrary(Library library) {
		List<Library> libs = new ArrayList<>();

		for (DocumentList libraryobj : library.getDocumentList()) {
			Library librarys = new Library();
			librarys.setActive(true);
			librarys.setEventDetailUuid(library.getEventDetailUuid());
			librarys.setLibraryUuid(UUID.randomUUID().toString());
			librarys.setDocumentType(libraryobj.getDocumentType());
			librarys.setSourceUuid(library.getSourceUuid());
			JSONArray documentid = libraryobj.getDocumentId();
			librarys.setDocumentId(documentid.toJSONString());
			librarys.setCreatedTime(library.getCreatedTime());
			librarys.setCreatedBy(library.getCreatedBy());
			librarys.setModuleCode(library.getModuleCode());
			librarys.setTenantId(library.getTenantId());
			libs.add(librarys);
		}

		RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().requestBody(libs).build();
		producer.push(config.getLibrarySaveTopic(), infoWrapper);
		return libs;
	}

	/**
     * Searches library uuid in database 
     * @param Library object
     */
	public List<Library> getLibrary(Library library) {
		return jdbcTemplate.query(
				PrQueryBuilder.GET_LIBRARY_QUERY, new Object[] { library.getEventDetailUuid(),
						library.getEventDetailUuid(), library.getTenantId(), library.getModuleCode() },
				libraryrowMapper);
	}

	/**
     * Pushes the request on delete topic
     *
     * @param Library Object to delete Library
     */
	public void deleteLibrary(Library library) {
		RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().requestBody(library).build();
		producer.push(config.getLibraryDeletTopic(), infoWrapper);
	}
	
	/**
     * Pushes the request on send invitation topic
     *
     * @param notificationReceiver to send Library upload notification
     */
	public void sendUploadNotification(Library library) {
		producer.push(config.getUploadLibraryNotification(), library);
	}

	/**
     * Searches event uuid in database 
     * @param Library object
     */
	public Integer checkEventUuid(Library library) {
		return jdbcTemplate.queryForObject(PrQueryBuilder.CHECK_EVEVT_UUID,
				new Object[] { library.getEventDetailUuid(), library.getModuleCode(), library.getTenantId() },
				Integer.class);
	}
}
