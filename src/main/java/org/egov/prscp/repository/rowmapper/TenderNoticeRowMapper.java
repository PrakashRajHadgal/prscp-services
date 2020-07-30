package org.egov.prscp.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.contract.request.User;
import org.egov.prscp.util.UserUtil;
import org.egov.prscp.web.models.PublicationList;
import org.egov.prscp.web.models.TenderNotice;
import org.egov.tracer.model.CustomException;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class TenderNoticeRowMapper implements ResultSetExtractor<List<TenderNotice>> {

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private UserUtil userUtil;

	@Override
	public List<TenderNotice> extractData(ResultSet rs) throws SQLException {
		Map<String, TenderNotice> tenderMap = new HashMap<>();
		List<TenderNotice> tenderList = new ArrayList<>();
		JSONParser jsonParser = new JSONParser();
		try {
			while (rs.next()) {
				String tenderNoticeUuid = rs.getString("tender_notice_uuid");

				if (!tenderMap.containsKey(tenderNoticeUuid)) {

					JSONArray documentAttachment = (JSONArray) jsonParser
							.parse(rs.getString("tender_document") == null ? "" : rs.getString("tender_document"));

					TenderNotice tender = TenderNotice.builder().tenderNoticeUuid(tenderNoticeUuid)
							.tenderNoticeUuid((rs.getString("tender_notice_uuid") == null ? ""
									: rs.getString("tender_notice_uuid")))
							.tenderNoticeId(
									(rs.getString("tender_notice_id") == null ? "" : rs.getString("tender_notice_id")))
							.tenderSubject(
									(rs.getString("tender_subject") == null ? "" : rs.getString("tender_subject")))
							.tenderNoticeUuid((rs.getString("tender_notice_uuid") == null ? ""
									: rs.getString("tender_notice_uuid")))
							.tenderNoticeId(
									(rs.getString("tender_notice_id") == null ? "" : rs.getString("tender_notice_id")))
							.tenderSubject(
									(rs.getString("tender_subject") == null ? "" : rs.getString("tender_subject")))
							.tenderDate((rs.getString("tender_date") == null ? "" : rs.getString("tender_date")))
							.fileNumber((rs.getString("file_number") == null ? "" : rs.getString("file_number")))
							.noteContent((rs.getString("note_content") == null ? "" : rs.getString("note_content")))
							.notificationTemplateUuid((rs.getString("notification_template_uuid") == null ? ""
									: rs.getString("notification_template_uuid")))
							.publicationSize(
									(rs.getString("publication_size") == null ? "" : rs.getString("publication_size")))
							.tenderNoticeStatus((rs.getString("tender_notice_status") == null ? ""
									: rs.getString("tender_notice_status")))
							.notificationTemplateUuid((rs.getString("notification_template_uuid") == null ? ""
									: rs.getString("notification_template_uuid")))
							.publicationSize(
									(rs.getString("publication_size") == null ? "" : rs.getString("publication_size")))
							.tenderNoticeStatus((rs.getString("tender_notice_status") == null ? ""
									: rs.getString("tender_notice_status")))
							.tenantId((rs.getString("tenant_id") == null ? "" : rs.getString("tenant_id")))
							.moduleCode((rs.getString("module_code") == null ? "" : rs.getString("module_code")))
							.tenderDocument(documentAttachment).isActive(rs.getBoolean("is_active")).build();

					JsonNode userInfo = userUtil
							.getUserId((rs.getString("created_by") == null ? "" : rs.getString("created_by")));
					if (userInfo != null) {
						User user = mapper.readValue(mapper.writeValueAsString(userInfo.get("user").get(0)),
								User.class);
						tender.setUserType((user == null ? "" : user.getName()));
					}
					// publicationlist
					List<PublicationList> publicationList = null;
					if (rs.getString("pressmasters") != null) {
						publicationList = Arrays
								.asList(mapper.readValue(rs.getString("pressmasters"), PublicationList[].class));
					}
					tender.setPublicationList(publicationList);
					tenderMap.put(tenderNoticeUuid, tender);
					tenderList.add(tender);
				}
			}

		} catch (Exception e) {
			throw new CustomException("TENDERNOTICE_EXCEPTION", e.getMessage());
		}
		return tenderList;
	}

}
