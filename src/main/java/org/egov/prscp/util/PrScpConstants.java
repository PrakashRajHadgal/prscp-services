package org.egov.prscp.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class PrScpConstants {

	public static final String MDM_TEMPLATE_MASTER = "RAINMAKER-PR";
	public static final String MDM_TEMPLATE_MODULE = "notificationTemplates";
	public static final String MDMS_TEMPLATE_PATH = "$.MdmsRes.RAINMAKER-PR";
	public static final String MDMS_TEMPLATE_MASTER_PATH = "$.MdmsRes.RAINMAKER-PR.notificationTemplates";

	public static final String PROPERTY_JSONPATH = "$.Properties[0].propertyId";
	public static final String NOTIFICATION_LOCALE = "en_IN";
	public static final String COMMON_MASTERS_MODULE = "common-masters";

	public static final String INVALID_TENANT_ID_MDMS_KEY = "INVALID TENANTID";

	public static final String INVALID_TENANT_ID_MDMS_MSG = "No data found for this tenentID";

		
	public static final String VARIABLE_USERACTIVE = "userActive";

	public static final String VARIABLE_CREATEDBY = "createdBy";

	public static final String VARIABLE_LASTMODIFIEDBY = "lastModifiedBy";

	public static final String VARIABLE_CREATEDTIME = "createdTime";

	public static final String VARIABLE_LASTMODIFIEDTIME = "lastModifiedTime";

	

	

	public PrScpConstants() {
	}

}
