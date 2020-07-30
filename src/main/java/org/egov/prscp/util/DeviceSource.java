package org.egov.prscp.util;

import java.util.UUID;

import org.egov.prscp.config.PrScpConfiguration;
import org.egov.prscp.producer.Producer;
import org.egov.prscp.repository.EventManegementRepository;
import org.egov.prscp.web.models.AuditDetails;
import org.egov.prscp.web.models.DeviceSources;
import org.egov.prscp.web.models.RequestInfoWrapper;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;

@Component
public class DeviceSource {

	@Autowired
	private EventManegementRepository repository;
	@Autowired
	private Producer producer;
	@Autowired
	private PrScpConfiguration config;

	public String saveDeviceDetails(String request, String moduleType, String tenantId, String moduleCode,
			AuditDetails auditDetails) {

		JSONObject deviceDetails = new JSONObject();
		UserAgent userAgent = UserAgent.parseUserAgentString(request);
		Browser bwr = userAgent.getBrowser();
		OperatingSystem os = userAgent.getOperatingSystem();

		deviceDetails.put("BrowserName", bwr.getName());
		deviceDetails.put("BrowserType", bwr.getBrowserType().getName());
		deviceDetails.put("BrowserEnginee", bwr.getRenderingEngine().name());
		deviceDetails.put("OperatingSystem", os.getName());
		deviceDetails.put("DeviceType", os.getDeviceType().getName());

		String sourceUuid = UUID.randomUUID().toString();
		DeviceSources deviceSources = DeviceSources.builder().sourceUuid(sourceUuid).moduleCode(moduleCode)
				.deviceDetails(deviceDetails.toJSONString())
				.deviceType(os.getDeviceType() == null ? "" : os.getDeviceType().getName()).tenantId(tenantId)
				.moduleType(moduleType).createdBy(auditDetails.getCreatedBy())
				.createdTime(auditDetails.getCreatedTime()).build();

		RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().requestBody(deviceSources).build();
		producer.push(config.getRequestDeviceSource(), infoWrapper);
		return sourceUuid;
	}

}
