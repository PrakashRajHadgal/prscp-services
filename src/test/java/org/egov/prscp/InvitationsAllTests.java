package org.egov.prscp;

import org.egov.prscp.repository.EventInvetationRepositoryTest;
import org.egov.prscp.service.EventInvitationServiceTest;
import org.egov.prscp.web.controllers.EventInvitationControllerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ EventInvitationControllerTest.class, EventInvetationRepositoryTest.class,
		EventInvitationServiceTest.class })
public class InvitationsAllTests {

}
