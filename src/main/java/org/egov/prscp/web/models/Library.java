package org.egov.prscp.web.models;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
/**
 * A Object holds the basic data for a Library
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Library {
	// can we put audit details in this?

	@Size(max = 64)
	@NotNull
	@JsonProperty("libraryUuid")
	private String libraryUuid;

	@Size(max = 64)
	@NotNull
	@JsonProperty("eventDetailUuid")
	private String eventDetailUuid;

	private List<DocumentList> documentList;

	private String documentId;

	private String documentType;

	@NotNull
	@JsonProperty("isActive")
	private boolean isActive;

	@Size(max = 256)
	@JsonProperty("tenantId")
	private String tenantId;

	@JsonProperty("moduleCode")
	private String moduleCode;

	@JsonProperty("createdBy")
	private String createdBy;

	@JsonProperty("lastModifiedBy")
	private String lastModifiedBy;

	@JsonProperty("createdTime")
	private Long createdTime;

	@JsonProperty("lastModifiedTime")
	private Long lastModifiedTime;

	@JsonProperty("sourceUuid")
	private String sourceUuid;
}
