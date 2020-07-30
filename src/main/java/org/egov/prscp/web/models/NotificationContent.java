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
public class NotificationContent {

	@JsonProperty("receiverName")
	private String receiverName;

	@JsonProperty("receiverEmail")
	private String receiverEmail;

	@JsonProperty("receiverMobile")
	private String receiverMobile;

	@JsonProperty("emailSubjectContent")
	private String emailSubjectContent;

	@JsonProperty("emailBodyContent")
	private String emailBodyContent;

	@JsonProperty("emailAttachment")
	private String emailAttachment;

	@JsonProperty("smsContent")
	private String smsContent;

}
