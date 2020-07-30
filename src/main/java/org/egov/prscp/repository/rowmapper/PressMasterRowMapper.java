package org.egov.prscp.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.prscp.web.models.PressMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class PressMasterRowMapper implements ResultSetExtractor<List<PressMaster>> {

	@Autowired
	private ObjectMapper mapper;

	@Override
	public List<PressMaster> extractData(ResultSet rs) throws SQLException, DataAccessException {
		Map<String, PressMaster> pressMap = new HashMap<>();
		List<PressMaster> list = new ArrayList<>();
		while (rs.next()) {
			String pressMasterUuid = rs.getString("press_master_uuid");
			PressMaster press = PressMaster.builder().pressMasterUuid(pressMasterUuid)
					.personnelName((rs.getString("personnel_name") == null ? "" : rs.getString("personnel_name")))
					.pressType((rs.getString("press_type") == null ? "" : rs.getString("press_type")))
					.publicationName((rs.getString("publication_name") == null ? "" : rs.getString("publication_name")))
					.mobile((rs.getString("mobile") == null ? "" : rs.getString("mobile")))
					.email((rs.getString("email") == null ? "" : rs.getString("email")))
					.isActive(rs.getBoolean("is_active"))
					.tenantId((rs.getString("tenant_id") == null ? "" : rs.getString("tenant_id"))).build();
			list.add(press);
		}
		return new ArrayList<>(list);
	}

}
