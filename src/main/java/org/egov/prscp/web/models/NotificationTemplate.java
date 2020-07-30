package org.egov.prscp.web.models;

import javax.validation.constraints.Size;

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
public class NotificationTemplate {

	@JsonProperty("tenantId")
	private String tenantId;

	@JsonProperty("moduleCode")
	private String moduleCode;

	@JsonProperty("notificationTemplateUuid")
	private String notificationTemplateUuid;

	@JsonProperty("eventDetailUuid")
	private String eventDetailUuid;

	@JsonProperty("emailContent")
	private String emailContent;

	@JsonProperty("smsContent")
	private String smsContent;

	@JsonProperty("templateType")
	private String templateType;

	@JsonProperty("templateMappedUuid")
	private String templateMappedUuid;

	@JsonProperty("setdoc")
	private String setdoc;

	@Size(max = 64)
	@JsonProperty("createdBy")
	private String createdBy = null;

	@Size(max = 64)
	@JsonProperty("lastModifiedBy")
	private String lastModifiedBy = null;

	@JsonProperty("createdTime")
	private Long createdTime = null;

	@JsonProperty("lastModifiedTime")
	private Long lastModifiedTime = null;
}