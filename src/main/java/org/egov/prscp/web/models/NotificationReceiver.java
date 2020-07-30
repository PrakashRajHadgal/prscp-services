package org.egov.prscp.web.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class NotificationReceiver {

	@JsonProperty("tenantId")
	private String tenantId;

	@JsonProperty("moduleCode")
	private String moduleCode;

	@JsonProperty("receiverType") // EVENT, PRESSNOTE, TENDERNOTIC
	private String receiverType;

	@JsonProperty("receiverUuid") // EVENT UUID, PRESSNOTE UUID, TENDERNOTIC UUID
	private String receiverUuid;

	@JsonProperty("senderUuid")
	private String senderUuid;

	@JsonProperty("contactDetails")
	private List<ContactDetails> contactDetails;
}
