package org.egov.prscp.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.egov.prscp.util.CommonConstants;
import org.egov.prscp.web.models.EventDetail;
import org.egov.prscp.web.models.InviteGuest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class EventDetailRowMapper implements ResultSetExtractor<List<EventDetail>> {

	@Autowired
	private ObjectMapper mapper;

	@Override
	public List<EventDetail> extractData(ResultSet rs) throws SQLException {
		List<EventDetail> list = new ArrayList<>();
		try {
			while (rs.next()) {

				EventDetail pr = EventDetail.builder().build();
				pr.setEventId(rs.getString("event_id") == null ? "" : rs.getString("event_id"));
				pr.setEventTitle(rs.getString("event_title") == null ? "" : rs.getString("event_title"));
				pr.setEventLocation(rs.getString("event_location") == null ? "" : rs.getString("event_location"));
				pr.setEventStatus(rs.getString("event_status") == null ? "" : rs.getString("event_status"));
				pr.setStartDate(rs.getString("start_date") == null ? "" : rs.getString("start_date"));
				pr.setArea(rs.getString("locality_name") == null ? "" : rs.getString("locality_name"));
				pr.setEndDate(rs.getString("end_date") == null ? "" : rs.getString("end_date"));
				pr.setStartTime(rs.getString("start_time") == null ? "" : rs.getString("start_time"));
				pr.setEndTime(rs.getString("end_Time") == null ? "" : rs.getString("end_Time"));
				pr.setOrganizerUsernName(
						rs.getString("organizer_user_name") == null ? "" : rs.getString("organizer_user_name"));
				pr.setStatus(rs.getString("status") == null ? "" : rs.getString("status"));
				pr.setOrganizerDepartmentName(rs.getString("organizer_department_name") == null ? ""
						: rs.getString("organizer_department_name"));
				pr.setFacebookUrl(rs.getString("facebook_url") == null ? "" : rs.getString("facebook_url"));
				pr.setTwitterUrl(rs.getString("twitter_url") == null ? "" : rs.getString("twitter_url"));
				pr.setInstagramUrl(rs.getString("instagram_url") == null ? "" : rs.getString("instagram_url"));
				pr.setEventDescription(
						rs.getString("event_description") == null ? "" : rs.getString("event_description"));
				pr.setEventType(rs.getString("event_type") == null ? "" : rs.getString("event_type"));
				pr.setEventString(rs.getString("event_image") == null ? "" : rs.getString("event_image"));
				pr.setEventBudget(rs.getDouble("event_budget"));
				pr.setActive(rs.getBoolean("is_active"));

				pr.setEventDetailUuid(
						rs.getString("event_detail_uuid") == null ? "" : rs.getString("event_detail_uuid"));
				pr.setSector(rs.getString("sector") == null ? "" : rs.getString("sector"));
				pr.setCommitteeUuid(rs.getString("committee_uuid") == null ? "" : rs.getString("committee_uuid"));
				pr.setTenantId(rs.getString("tenant_id") == null ? "" : rs.getString("tenant_id"));
				pr.setModuleCode(rs.getString("module_code") == null ? "" : rs.getString("module_code"));
				pr.setCommitteeName(rs.getString("committee_name") == null ? "" : rs.getString("committee_name"));

				List<InviteGuest> inviteGuests = new ArrayList<>();
				if (rs.getString("invitedguest") != null) {
					inviteGuests = Arrays.asList(mapper.readValue(rs.getString("invitedguest"), InviteGuest[].class));
				}
				
				pr.setInviteGuest(inviteGuests);

				list.add(pr);
			}
		} catch (Exception e) {
			throw new CustomException(CommonConstants.EVENT_EXCEPTION_CODE, e.getMessage());
		}
		return new ArrayList<>(list);
	}

}
