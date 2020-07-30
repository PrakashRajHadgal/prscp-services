package org.egov.prscp.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.egov.common.contract.request.User;
import org.egov.prscp.util.UserUtil;
import org.egov.prscp.web.models.CommitteeDetail;
import org.egov.prscp.web.models.CommitteeMember;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CommitteeMasterRowMapper implements ResultSetExtractor<List<CommitteeDetail>> {

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private UserUtil userUtil;

	@Override
	public List<CommitteeDetail> extractData(ResultSet rs) throws SQLException {

		List<CommitteeDetail> committeeMap = new ArrayList<>();

		try {
			while (rs.next()) {

				CommitteeDetail committeeDetail = CommitteeDetail.builder()
						.committeeUuid(rs.getString("committee_uuid") == null ? "" : rs.getString("committee_uuid"))
						.committeeName(rs.getString("committee_name") == null ? "" : rs.getString("committee_name"))
						.committeeDescription(rs.getString("committee_description") == null ? ""
								: rs.getString("committee_description"))
						.isActive(rs.getBoolean("is_active"))
						.tenantId(rs.getString("tenant_id") == null ? "" : rs.getString("tenant_id"))
						.moduleCode(rs.getString("module_code") == null ? "" : rs.getString("module_code"))
						.createdOn(rs.getString("created_time") == null ? "" : rs.getString("created_time"))
						.totalCommitteeMember(rs.getString("totalCommitteeMember") == null ? ""
								: rs.getString("totalCommitteeMember"))
						.build();

				JsonNode userInfo = userUtil
						.getUserId((rs.getString("created_by") == null ? "" : rs.getString("created_by")));

				if (userInfo != null) {
					User user = mapper.readValue(mapper.writeValueAsString(userInfo.get("user").get(0)), User.class);
					committeeDetail.setCreatorName(user == null ? "" : user.getName());
				} else {
					committeeDetail.setCreatorName("");
				}

				// Committe Members
				List<CommitteeMember> committeeMembers = new ArrayList<>();
				if (rs.getString("committeeMembers") != null) {
					committeeMembers = Arrays
							.asList(mapper.readValue(rs.getString("committeeMembers"), CommitteeMember[].class));
				}
				committeeDetail.setCommitteeMember(committeeMembers);

				committeeMap.add(committeeDetail);
			}
		} catch (Exception e) {
			throw new CustomException("COMMITTE_EXCEPTION", e.getMessage());
		}
		return committeeMap;
	}

}
