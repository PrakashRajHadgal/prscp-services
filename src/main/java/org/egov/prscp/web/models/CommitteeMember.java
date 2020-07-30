package org.egov.prscp.web.models;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
/**
 * A Object holds the basic data for a committee
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class CommitteeMember {

	@Size(max = 64)
	@JsonProperty("committeeMemberUuid")
	private String committeeMemberUuid;

	@Size(max = 64)
	@JsonProperty("userUuid")
	private String userUuid;

	@Size(max = 256)
	@JsonProperty("departmentUuid")
	private String departmentUuid;

	@Size(max = 64)
	@JsonProperty("departmentName")
	private String departmentName;

	@Size(max = 256)
	@JsonProperty("committeeUuid")
	private String committeeUuid;

	@JsonProperty("isActive")
	private boolean isActive;

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
