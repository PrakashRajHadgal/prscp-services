package org.egov.prscp;

import org.egov.prscp.repository.EventLibraryManagementRepositoryTest;
import org.egov.prscp.service.EventLibraryManagementServiceTest;
import org.egov.prscp.web.controllers.EventLibraryManagementControllerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ EventLibraryManagementServiceTest.class, EventLibraryManagementControllerTest.class,
		EventLibraryManagementRepositoryTest.class })
public class LibraryAllTests {

}
