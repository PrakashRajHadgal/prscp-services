package org.egov.prscp.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.prscp.config.PrScpConfiguration;
import org.egov.prscp.producer.Producer;
import org.egov.prscp.web.models.PressMaster;
import org.egov.prscp.web.models.PressNote;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import junit.framework.Assert;

@RunWith(MockitoJUnitRunner.class)
public class GeneratePressNotesRepositoryTest {

	@InjectMocks
	private GeneratePressNotesRepository reposotory;

	@Mock
	private JdbcTemplate jdbcTemplate;

	@Mock
	private Producer producer;

	@Mock
	private PrScpConfiguration config;

	@Test
	public void testGetPressNote() {
		PressNote press = PressNote.builder().pressNoteUuid("asd3dcd32dc2323").build();
		List<PressNote> list = new ArrayList<>();
		list.add(press);
		Mockito.when(config.getPeriodPressNote()).thenReturn("1");
		Mockito.when(jdbcTemplate.query(Matchers.anyString(), Matchers.any(Object[].class),
				Matchers.any(ResultSetExtractor.class))).thenReturn(list);
		Assert.assertEquals("asd3dcd32dc2323", reposotory.getPressNote(new PressNote()).get(0).getPressNoteUuid());
	}

	@Test
	public void testGetPressNoteePressList() {
		PressMaster press = PressMaster.builder().pressMasterUuid("asd3dcd32dc2323").build();
		List<PressMaster> list = new ArrayList<>();
		list.add(press);
		Mockito.when(jdbcTemplate.query(Matchers.anyString(), Matchers.any(Object[].class),
				Matchers.any(ResultSetExtractor.class))).thenReturn(list);
		Assert.assertEquals("asd3dcd32dc2323",
				reposotory.getPressNoteePressList(new PressNote()).get(0).getPressMasterUuid());
	}

	@Test
	public void testUploadPressNote() {
		reposotory.uploadPressNote(null);
	}

	@Test
	public void testSendInvitation() {
		reposotory.sendInvitation(null);
	}

	@Test
	public void testUpdatePressNote() {
		reposotory.updatePressNote(null);
	}

	@Test
	public void testCheckpressNote() {
		Mockito.when(jdbcTemplate.query(Matchers.anyString(), Matchers.any(Object[].class),
				Matchers.any(ResultSetExtractor.class))).thenReturn(1);
		PressNote note = PressNote.builder().build();
		Mockito.when(reposotory.checkpressNote(note)).thenReturn(1);
		Assert.assertEquals(1, (int) reposotory.checkpressNote(note));
	}

}
