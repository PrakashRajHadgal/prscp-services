package org.egov.prscp.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ContactDetails {

	@JsonProperty("notificationResendUuid")
	private String notificationResendUuid;

	@JsonProperty("moduleCode")
	private String moduleCode;

	@JsonProperty("notificationMappedUuid")
	private String notificationMappedUuid;

	@JsonProperty("moduleName")
	private String moduleName;

	@JsonProperty("receiverUuid")
	private String receiverUuid;

	@JsonProperty("receiverName")
	private String receiverName;

	@JsonProperty("receiverEmail")
	private String receiverEmail;

	@JsonProperty("receiverMobile")
	private String receiverMobile;

	@JsonProperty("tenantId")
	private String tenantId;

	@JsonProperty("resentBy")
	private String resentBy;

	@JsonProperty("resentTime")
	private long resentTime;

}
