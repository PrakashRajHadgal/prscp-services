package org.egov.prscp.web.models;

import java.util.List;

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
public class GuestsResend {

	@JsonProperty("tenantId")
	private String tenantId;

	@JsonProperty("moduleCode")
	private String moduleCode;

	@JsonProperty("moduleName") // EVENT, TENDER, PRESSNOTE
	private String moduleName;

	@JsonProperty("invitationUuid")
	private String invitationUuid;

	@JsonProperty("contactDetails")
	private List<ContactDetails> contactDetails;

	@JsonProperty("defaultAll")
	private boolean defaultAll;
}
