package org.egov.prscp;

import org.egov.prscp.repository.EventPressMasterRepositoryTest;
import org.egov.prscp.service.EventPressMasterServiceTest;
import org.egov.prscp.web.controllers.EventPressMasterControllerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ EventPressMasterServiceTest.class, EventPressMasterControllerTest.class,
		EventPressMasterRepositoryTest.class })
public class PressMasterAllTests {

}
