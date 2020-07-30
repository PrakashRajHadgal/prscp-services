package org.egov.prscp.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.prscp.web.models.PressNote;
import org.egov.prscp.web.models.PublicationList;
import org.egov.tracer.model.CustomException;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class PressNoteMapper implements ResultSetExtractor<List<PressNote>> {

	@Autowired
	private ObjectMapper mapper;

	@Override
	public List<PressNote> extractData(ResultSet rs) throws SQLException, DataAccessException {
		Map<String, PressNote> libraryMap = new HashMap<>();
		List<PressNote> listPress = new ArrayList<>();
		JSONParser jsonParser = new JSONParser();
		try {
			while (rs.next()) {
				String id = rs.getString("press_note_uuid");

				if (!libraryMap.containsKey(id)) {

					JSONArray documents = new JSONArray();
					JSONArray email = new JSONArray();
					if (rs.getString("document_attachment") != null) {
						documents = (JSONArray) jsonParser.parse(rs.getString("document_attachment"));
					}

					if (rs.getString("email_content") != null) {
						email = (JSONArray) jsonParser.parse(rs.getString("email_content"));
					}

					PressNote note = new PressNote();
					note = note.builder().build();
					note.setPressNoteUuid(id);
					note.setPressNoteSubject(
							rs.getString("press_note_subject") == null ? "" : rs.getString("press_note_subject"));
					note.setNoteContent(rs.getString("note_content") == null ? "" : rs.getString("note_content"));
					note.setPressNoteDate(
							rs.getDate("press_note_date") == null ? "" : rs.getDate("press_note_date").toString());
					note.setFileNumber(rs.getString("file_number") == null ? "" : rs.getString("file_number"));

					// publicationlist
					/*
					 * PublicationList pr = PublicationList.builder().build();
					 * pr.setMobile(rs.getString("mobile"));
					 * pr.setPressMasterUuid(rs.getString("press_master_uuid"));
					 * pr.setEmail(rs.getString("email"));
					 * pr.setPublicationName(rs.getString("publication_name"));
					 * pr.setPressType(rs.getString("press_type"));
					 * pr.setPersonnelName(rs.getString("personnel_name")); listtoadd.add(pr);
					 */
					List<PublicationList> publicationList = null;
					if (rs.getString("pressmasters") != null) {
						publicationList = Arrays
								.asList(mapper.readValue(rs.getString("pressmasters"), PublicationList[].class));
					}
					// template

					note.setSmsContent(rs.getString("sms_content"));
					note.setEmailContent(email);
					note.setDocumentAttachment(documents);
					note.setTemplateType(rs.getString("template_type"));
					note.setActive(true);
					note.setCreatedBy(rs.getString("created_by"));
					note.setModuleCode(rs.getString("module_code"));
					note.setTenantId(rs.getString("tenant_id"));
					note.setCreatedTime(rs.getLong("created_time"));
					note.setLastModifiedBy(rs.getString("last_modified_by"));
					note.setLastModifiedTime(rs.getLong("last_modified_time"));
					note.setPublicationList(publicationList);

					libraryMap.put(id, note);
					listPress.add(note);
				}
			}
		} catch (Exception e) {
			throw new CustomException("TENDERNOTICE_EXCEPTION", e.getMessage());
		}
		return listPress;
	}

}
