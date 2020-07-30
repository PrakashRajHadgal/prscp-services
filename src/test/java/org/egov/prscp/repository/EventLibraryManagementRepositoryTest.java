package org.egov.prscp.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.prscp.config.PrScpConfiguration;
import org.egov.prscp.producer.Producer;
import org.egov.prscp.web.models.DocumentList;
import org.egov.prscp.web.models.Library;
import org.json.simple.JSONArray;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

@RunWith(MockitoJUnitRunner.class)
public class EventLibraryManagementRepositoryTest {

	@InjectMocks
	private EventLibraryManagementRepository repository;

	@Mock
	private JdbcTemplate jdbcTemplate;

	@Mock
	private Producer producer;

	@Mock
	private PrScpConfiguration config;

	@Test
	public void testUploadLibrary() {
		ArrayList<DocumentList> ds = new ArrayList<>();
		DocumentList dsobj = new DocumentList();
		dsobj.setDocumentType("type");
		JSONArray documentId = new JSONArray();
		documentId.add("1");
		dsobj.setDocumentId(documentId);
		ds.add(dsobj);
		Library library = Library.builder().documentList(ds).build();
		repository.uploadLibrary(library);
	}

	@Test
	public void testDeleteLibrary() {
		Library library = Library.builder().moduleCode("Test").eventDetailUuid("ncw8qhc327yddb72fd23b2").build();
		repository.deleteLibrary(library);
	}

	@Test
	public void testGetLibrary() {

		Library library = Library.builder().eventDetailUuid("ncw8qhc327yddb72fd23b2").build();
		List<Library> list = new ArrayList<>();
		list.add(library);

		Mockito.when(jdbcTemplate.query(Matchers.anyString(), Matchers.any(Object[].class),
				Matchers.any(ResultSetExtractor.class))).thenReturn(list);
		Assert.assertEquals(library.getLibraryUuid(), repository.getLibrary(new Library()).get(0).getLibraryUuid());
	}

	@Test
	public void testDeletePress() {
		Library library = Library.builder().eventDetailUuid("ncw8qhc327yddb72fd23b2").build();
		repository.deleteLibrary(library);
	}

	@Test
	public void testsendUploadNotification() {
		Library library = Library.builder().eventDetailUuid("ncw8qhc327yddb72fd23b2").build();
		repository.sendUploadNotification(library);
	}

	@Test
	public void testCheckEventUuid() {
		String CHECK_EVEVT_UUID = "select count(1) from egpr_event_detail where event_detail_uuid=? and module_code=? and tenant_id=?";
		Library lib = Library.builder().eventDetailUuid("asc3ce").moduleCode("PR").tenantId("ch").build();
		Mockito.when(jdbcTemplate.queryForObject(CHECK_EVEVT_UUID,
				new Object[] { lib.getEventDetailUuid(), lib.getModuleCode(), lib.getTenantId() }, Integer.class))
				.thenReturn(1);
		Assert.assertEquals(1, (int) repository.checkEventUuid(lib));
	}
}
