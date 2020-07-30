package org.egov.prscp.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.prscp.web.models.InviteGuest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class InvitationGuestRowMapper implements ResultSetExtractor<List<InviteGuest>> {

	@Autowired
	private ObjectMapper mapper;

	@Override
	public List<InviteGuest> extractData(ResultSet rs) throws SQLException, DataAccessException {
		Map<String, InviteGuest> userMap = new HashMap<>();
		while (rs.next()) {

			String id = rs.getString("event_guest_uuid");
			if (userMap.get(id) == null) {
				InviteGuest inviteGuest = InviteGuest.builder().eventGuestUuid(id)
						.guestName((rs.getString("guest_mobile") == null ? "" : rs.getString("guest_name")))
						.guestMobile((rs.getString("guest_mobile") == null ? "" : rs.getString("guest_mobile")))
						.guestEmail((rs.getString("guest_mobile") == null ? "" : rs.getString("guest_email")))
						.eventGuestType(
								(rs.getString("event_guest_type") == null ? "" : rs.getString("event_guest_type")))
						.tenantId((rs.getString("tenant_Id") == null ? "" : rs.getString("tenant_Id")))
						.departmentName(
								(rs.getString("department_name") == null ? "" : rs.getString("department_name")))
						.departmentUuid(
								(rs.getString("department_uuid") == null ? "" : rs.getString("department_uuid")))
						.moduleCode((rs.getString("module_code") == null ? "" : rs.getString("module_code")))
						.userUuid((rs.getString("user_uuid") == null ? "" : rs.getString("user_uuid")))
						.eventDetailUuid(
								(rs.getString("event_detail_uuid") == null ? "" : rs.getString("event_detail_uuid")))
						.sentFlag(rs.getBoolean("sent_flag")).isActive(rs.getBoolean("is_active")).build();
				userMap.put(id, inviteGuest);
			}
		}
		return new ArrayList<>(userMap.values());
	}
}
