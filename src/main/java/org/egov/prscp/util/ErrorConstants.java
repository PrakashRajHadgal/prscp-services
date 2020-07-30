package org.egov.prscp.util;

import org.springframework.stereotype.Component;

@Component
public class ErrorConstants {

	public static final String MISSING_OR_INVALID_EVENT_OBJECT = "EVENT_OBJECT";
	public static final String MISSING_OR_INVALID_EVENT_OBJECT_MESSAGE = "Null or Invalid Event Request";

	public static final String MISSING_OR_INVALID_START_DATE_CODE = "START_DATE";
	public static final String MISSING_OR_INVALID_START_DATE_MESSAGE = "Blank or Invalid Start Date";

	public static final String MISSING_OR_INVALID_START_TIME_CODE = "START_TIME";
	public static final String MISSING_OR_INVALID_START_TIME_MESSAGE = "Blank or Invalid Start Time";

	public static final String MISSING_OR_INVALID_END_DATE_CODE = "END_DATE";
	public static final String MISSING_OR_INVALID_END_DATE_MESSAGE = "Blank or Invalid End Date";

	public static final String MISSING_OR_INVALID_END_TIME_CODE = "END_TIME";
	public static final String MISSING_OR_INVALID_END_TIME_MESSAGE = "Blank or Invalid End Time";
	
	public static final String INVALID_PRESSNOTE = "PRESS NOTE:";
	public static final String INVALID_PRESSNOTE_MESSAGE = "Press Note Already Exist";
	
	public static final String INVALID_TENDER_NOTICE = "TENDER NOTICE:";
	public static final String INVALID_TENDER_NOTICE_MESSAGE = "Tender Notice Already Exist";	
	
	public static final String INVALID_EVENT = "Event";
	public static final String INVALID_EVENT_MESSAGE= "Event Already Exist";
	
}
