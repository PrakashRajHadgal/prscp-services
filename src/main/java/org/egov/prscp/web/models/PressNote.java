package org.egov.prscp.web.models;

import java.util.List;

import javax.validation.constraints.Size;

import org.json.simple.JSONArray;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * A Object holds the basic data for a Press Note
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PressNote {

	@JsonProperty("pressNoteUuid")
	private String pressNoteUuid;

	@JsonProperty("pressNoteSubject")
	private String pressNoteSubject;

	@JsonProperty("pressNoteDate")
	private String pressNoteDate;

	@JsonProperty("fileNumber")
	private String fileNumber;

	@JsonProperty("noteContent")
	private String noteContent;

	@JsonProperty("noteDocument")
	private JSONArray noteDocument;

	@JsonProperty("emailContent")
	private JSONArray emailContent;

	@JsonProperty("smsContent")
	private String smsContent;

	@JsonProperty("publicationList")
	private List<PublicationList> publicationList;

	@JsonProperty("templateType")
	private String templateType;

	private String publicList;

	@JsonProperty("isActive")
	private boolean isActive;

	@JsonProperty("tenantId")
	private String tenantId;

	@Size(max = 64)
	@JsonProperty("createdBy")
	private String createdBy;

	@Size(max = 64)
	@JsonProperty("lastModifiedBy")
	private String lastModifiedBy;

	@JsonProperty("createdTime")
	private Long createdTime;

	@JsonProperty("lastModifiedTime")
	private Long lastModifiedTime;

	@JsonProperty("documentAttachment")
	private JSONArray documentAttachment;

	private String notifiactionTemplateUuid;

	@JsonProperty("moduleCode")
	private String moduleCode;

	@JsonProperty("fromDate")
	private String fromDate;

	@JsonProperty("toDate")
	private String toDate;

	@JsonProperty("defaultGrid")
	private boolean defaultGrid;

	@JsonProperty("sourceUuid")
	private String sourceUuid;

}
