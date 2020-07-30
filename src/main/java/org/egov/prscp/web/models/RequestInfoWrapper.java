package org.egov.prscp.web.models;

import javax.validation.constraints.NotNull;

import org.egov.common.contract.request.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class RequestInfoWrapper {

	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;

	@JsonProperty("RequestBody")
	private Object requestBody;

	@JsonProperty("AuditDetails")
	AuditDetails auditDetails;

	@JsonProperty("applicationType")
	private String applicationType;

	@JsonProperty("tenantId")
	private String tenantId;

	@JsonProperty("applicationStatus")
	private String applicationStatus;

	@JsonProperty("applicationId")
	private String applicationId;

	@JsonProperty("actions")
	private String actions;
}
