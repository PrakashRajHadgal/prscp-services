package org.egov.prscp;


import org.egov.prscp.repository.EventCommitteeMasterRepositoryTest;
import org.egov.prscp.service.EventCommitteeMasterServiceTest;
import org.egov.prscp.web.controllers.EventCommitteeMasterControllerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ EventCommitteeMasterRepositoryTest.class, EventCommitteeMasterControllerTest.class,
	EventCommitteeMasterServiceTest.class })
public class EventCommitteeMasterAllTests{

}
