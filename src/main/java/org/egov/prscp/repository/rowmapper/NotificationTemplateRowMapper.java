package org.egov.prscp.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.prscp.web.models.NotificationTemplate;
import org.egov.tracer.model.CustomException;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

@Component
public class NotificationTemplateRowMapper implements ResultSetExtractor<NotificationTemplate> {
	@Override
	public NotificationTemplate extractData(ResultSet rs) throws SQLException, DataAccessException {
		NotificationTemplate notificationTemplate = null;

		while (rs.next()) {
			try {
				JSONParser jsonParser = new JSONParser();
				JSONArray documents = null;

				if (rs.getString("document_attachment") != null) {
					documents = (JSONArray) jsonParser.parse(rs.getString("document_attachment").toString());
				}
				notificationTemplate = NotificationTemplate.builder()
						.smsContent(rs.getString("sms_content") != null ? rs.getString("sms_content") : "")
						.emailContent(rs.getString("email_content") != null ? rs.getString("email_content") : "")
						.moduleCode((rs.getString("module_code") == null ? "" : rs.getString("module_code")))
						.setdoc((documents == null ? "" : documents.toJSONString()))
						.templateType((rs.getString("template_type") == null ? "" : rs.getString("template_type")))
						.templateMappedUuid((rs.getString("template_mapped_uuid") == null ? ""
								: rs.getString("template_mapped_uuid")))
						.tenantId((rs.getString("tenant_id") == null ? "" : rs.getString("tenant_id")))
						.notificationTemplateUuid((rs.getString("notification_template_uuid") == null ? ""
								: rs.getString("notification_template_uuid")))
						.build();
			} catch (Exception e) {
				throw new CustomException("NOTIFICATION_TEMPLATE", e.getMessage());
			}
		}
		return notificationTemplate;
	}
}
