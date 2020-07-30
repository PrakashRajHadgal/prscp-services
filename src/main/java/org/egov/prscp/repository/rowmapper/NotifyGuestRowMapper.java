package org.egov.prscp.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.prscp.web.models.NotifyGuest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class NotifyGuestRowMapper implements ResultSetExtractor<List<NotifyGuest>> {

	@Autowired
	private ObjectMapper mapper;

	@Override
	public List<NotifyGuest> extractData(ResultSet rs) throws SQLException, DataAccessException {
		Map<String, NotifyGuest> userMap = new HashMap<>();
		while (rs.next()) {
			String id = rs.getString("event_guest_uuid");
			NotifyGuest externaluser = NotifyGuest.builder().build();
			externaluser.setEventId(rs.getString("event_guest_uuid") == null ? "" : rs.getString("event_guest_uuid"));
			externaluser.setEventDetailUuid(
					rs.getString("event_detail_uuid") == null ? "" : rs.getString("event_detail_uuid"));
			externaluser.setGuestType(rs.getString("event_guest_type") == null ? "" : rs.getString("event_guest_type"));
			externaluser
					.setDepartmentUuid(rs.getString("department_uuid") == null ? "" : rs.getString("department_uuid"));
			externaluser.setUserUuid(rs.getString("user_uuid") == null ? "" : rs.getString("user_uuid"));
			externaluser.setGuestName(rs.getString("guest_name") == null ? "" : rs.getString("guest_name"));
			externaluser.setGuestEmail(rs.getString("guest_email") == null ? "" : rs.getString("guest_email"));
			externaluser.setGuestMobile(rs.getString("guest_mobile") == null ? "" : rs.getString("guest_mobile"));
			externaluser.setNotificationTemplateUuid(rs.getString("notification_template_uuid") == null ? ""
					: rs.getString("notification_template_uuid"));
			externaluser.setSentFlag(rs.getBoolean("sent_flag"));
			externaluser.setActive(rs.getBoolean("is_active"));
			userMap.put(id, externaluser);
		}
		return new ArrayList<>(userMap.values());
	}

}
