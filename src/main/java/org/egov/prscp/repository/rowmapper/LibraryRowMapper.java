package org.egov.prscp.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.prscp.web.models.DocumentList;
import org.egov.prscp.web.models.Library;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class LibraryRowMapper implements ResultSetExtractor<List<Library>> {

	@Autowired
	private ObjectMapper mapper;

	@Override
	public List<Library> extractData(ResultSet rs) throws SQLException, DataAccessException {
		Map<String, Library> libraryMap = new HashMap<>();
		ArrayList<DocumentList> documen = new ArrayList<>();
		Library library = new Library();
		Library librarys = new Library();
		while (rs.next()) {
			String id = rs.getString("library_uuid");
			librarys.setLibraryUuid(rs.getString("library_uuid") == null ? "" : rs.getString("library_uuid"));
			librarys.setTenantId(rs.getString("tenant_id") == null ? "" : rs.getString("tenant_id"));
			librarys.setActive(rs.getBoolean("is_active"));
			DocumentList ds = new DocumentList();
			ds.setDocumentType(rs.getString("document_type") == null ? "" : rs.getString("document_type"));
			ds.setFileStoreId(rs.getString("uploaded_document") == null ? "" : rs.getString("uploaded_document"));
			documen.add(ds);
			while (rs.next()) {
				DocumentList ds1 = new DocumentList();
				ds1.setDocumentType(rs.getString("document_type") == null ? "" : rs.getString("document_type"));
				ds1.setFileStoreId(rs.getString("uploaded_document") == null ? "" : rs.getString("uploaded_document"));
				documen.add(ds1);
			}
			librarys.setDocumentList(documen);
			libraryMap.put(id, librarys);

			break;
		}
		return new ArrayList<>(libraryMap.values());
	}

}
