package org.egov.prscp;

import org.egov.prscp.repository.TenderNoticePublicationRepositoryTest;
import org.egov.prscp.service.TenderNoticePublicationServiceTest;
import org.egov.prscp.web.controllers.TenderNoticePublicationControllerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TenderNoticePublicationServiceTest.class, TenderNoticePublicationControllerTest.class,
		TenderNoticePublicationRepositoryTest.class })
public class TenderNoticePublicationAllTests {

}
