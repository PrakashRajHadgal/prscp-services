package org.egov.prscp;

import org.egov.prscp.repository.GeneratePressNotesRepositoryTest;
import org.egov.prscp.service.PressNoteServiceTest;
import org.egov.prscp.web.controllers.GeneratePressNoteControllerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ PressNoteServiceTest.class, GeneratePressNoteControllerTest.class,
		GeneratePressNotesRepositoryTest.class })
public class PressNoteAllTests {

}
