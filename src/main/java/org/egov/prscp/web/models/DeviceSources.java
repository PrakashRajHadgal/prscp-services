package org.egov.prscp.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class DeviceSources {

	@JsonProperty("tenantId")
	private String tenantId;

	@JsonProperty("moduleCode")
	private String moduleCode;

	@JsonProperty("sourceUuid")
	private String sourceUuid;

	@JsonProperty("moduleType")
	private String moduleType;

	@JsonProperty("deviceType")
	private String deviceType;

	@JsonProperty("deviceDetails")
	private String deviceDetails;

	@JsonProperty("createdBy")
	private String createdBy;

	@JsonProperty("createdTime")
	private Long createdTime;
}
