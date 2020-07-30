package org.egov.prscp.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.prscp.web.models.PublicRelationEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;


@Component
public class EventRowMapper  implements ResultSetExtractor<List<PublicRelationEvent>> {


    @Autowired
    private ObjectMapper mapper;

	@Override
	public List<PublicRelationEvent> extractData(ResultSet rs) throws SQLException, DataAccessException {
		Map<String, PublicRelationEvent> eventMap = new HashMap<>();
		while (rs.next()) {
            String id = rs.getString("appnumber");
		PublicRelationEvent pr=new PublicRelationEvent();
		pr = PublicRelationEvent.builder().build();
		pr.setApplicationNumber(rs.getString("appnumber"));
		eventMap.put(id, pr);
		}
		return new ArrayList<>(eventMap.values());
	}







  



}
