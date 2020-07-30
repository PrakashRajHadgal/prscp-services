package org.egov.prscp.web.models;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.json.simple.JSONArray;

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
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Template {

	@NotEmpty(message = "Tenant Id should not be empty or null")
	@NotNull
	@JsonProperty("tenantId")
	private String tenantId;

	@NotEmpty(message = "Module Code should not be empty or null")
	@NotNull
	@JsonProperty("moduleCode")
	private String moduleCode;

	@JsonProperty("moduleName")
	private String moduleName;

	@NotEmpty(message = "Template Mapped Uuid should not be empty or null")
	@NotNull
	@JsonProperty("templateMappedUuid")
	private String templateMappedUuid;

	@JsonProperty("eventDetailUuid")
	private String eventDetailUuid;

	@JsonProperty("tenderNoticeUuid")
	private String tenderNoticeUuid;

	@JsonProperty("smsContent")
	private String smsContent;

	@JsonProperty("emailContent")
	private JSONArray emailContent;

	@NotEmpty(message = "Document List should not be empty or null")
	@NotNull
	@JsonProperty("documentAttachment")
	private JSONArray documentAttachment;

	@NotEmpty(message = "Template Type should not be empty or null")
	@NotNull
	@JsonProperty("templateType")
	private String templateType;

}
