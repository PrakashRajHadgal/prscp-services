package org.egov.prscp;

import org.egov.prscp.repository.NotificationTemplateRepositoryTest;
import org.egov.prscp.service.NotificationTemplateServiceTest;
import org.egov.prscp.web.controllers.NotificationTemplateControllerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ NotificationTemplateRepositoryTest.class, NotificationTemplateControllerTest.class,
		NotificationTemplateServiceTest.class })
public class NotificationAllTests {

}
