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
public class SendInvitation {

	@JsonProperty("tenantId")
	private String tenantId;

	@JsonProperty("moduleCode")
	private String moduleCode;

	@JsonProperty("eventDetailUuid")
	private String eventDetailUuid;

	@JsonProperty("smsContent")
	private String smsContent;

	@JsonProperty("emailContent")
	private String emailContent;

	@JsonProperty("templateType")
	private String templateType;
}
