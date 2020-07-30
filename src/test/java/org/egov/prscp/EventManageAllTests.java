package org.egov.prscp;

import org.egov.prscp.repository.EventManagementRepositoryTest;
import org.egov.prscp.service.EventManagementServiceTest;
import org.egov.prscp.web.controllers.EventManagementControllerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ EventManagementControllerTest.class, EventManagementServiceTest.class,
		EventManagementRepositoryTest.class })
public class EventManageAllTests {

}
