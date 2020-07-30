package org.egov.prscp;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ EventCommitteeMasterAllTests.class, InvitationsAllTests.class, LibraryAllTests.class,
		NotificationAllTests.class, PressNoteAllTests.class, TenderNoticePublicationAllTests.class,
		EventManageAllTests.class, PressMasterAllTests.class })
public class AllTests {

}
