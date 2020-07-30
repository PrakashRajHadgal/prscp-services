package org.egov.prscp.web.models;

import java.util.List;

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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CommitteeDetail {

	@Size(max = 64)
	@JsonProperty("committeeUuid")
	private String committeeUuid;

	@Size(max = 255)
	@JsonProperty("committeeName")
	private String committeeName;

	@Size(max = 255)
	@JsonProperty("committeeDescription")
	private String committeeDescription;

	@JsonProperty("isActive")
	private boolean isActive;

	@JsonProperty("createdOn")
	private String createdOn;

	@JsonProperty("creatorName")
	private String creatorName;

	@JsonProperty("totalCommitteeMember")
	private String totalCommitteeMember;

	@JsonProperty("committeeMember")
	private List<CommitteeMember> committeeMember;

	@Size(max = 256)
	@JsonProperty("tenantId")
	private String tenantId;

	@Size(max = 256)
	@JsonProperty("moduleCode")
	private String moduleCode;

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
