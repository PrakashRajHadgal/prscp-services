package org.egov.prscp.repository;

import org.egov.prscp.config.PrScpConfiguration;
import org.egov.prscp.producer.Producer;
import org.egov.prscp.web.models.NotificationReceiver;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class PressNoteRepositoryTest {

	@InjectMocks
	private GeneratePressNotesRepository repository;

	@Mock
	private JdbcTemplate jdbcTemplate;

	@Mock
	private Producer producer;

	@Mock
	private PrScpConfiguration config;

	@Test
	public void testUploadPressNote() {

		JSONObject aa = new JSONObject();
		repository.uploadPressNote(aa);
	}

	@Test
	public void testDeletePressNote() {
		JSONObject aa = new JSONObject();
		repository.updatePressNote(aa);
	}

	@Test
	public void sendInvitation() {
		NotificationReceiver notificationReceiver = NotificationReceiver.builder().moduleCode("ncw8qhc327yddb72fd23b2")
				.build();

		// JSONObject aa = new JSONObject();
		repository.sendInvitation(notificationReceiver);
	}
	/*
	 * @Test public void testGetPressNote() {
	 * 
	 * PressNote pressnote =
	 * PressNote.builder().pressNoteUuid("ncw8qhc327yddb72fd23b2").build();
	 * List<PressNote> list = new ArrayList<>(); list.add(pressnote);
	 * 
	 * Mockito.when(jdbcTemplate.query(Matchers.anyString(),
	 * Matchers.any(Object[].class),
	 * Matchers.any(ResultSetExtractor.class))).thenReturn(list);
	 * Assert.assertEquals(pressnote.getPressNoteUuid(), repository.getPressNote(new
	 * PressNote()).get(0).getPressNoteUuid()); }
	 */

}
