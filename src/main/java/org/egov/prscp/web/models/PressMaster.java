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
 * A Object holds the basic data for a Press Master
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PressMaster {

	@JsonProperty("tenantId")
	private String tenantId;

	@JsonProperty("moduleCode")
	private String moduleCode;

	@JsonProperty("pressMasterUuid")
	private String pressMasterUuid;

	@JsonProperty("personnelName")
	private String personnelName;

	@JsonProperty("pressType")
	private String pressType;

	@JsonProperty("publicationName")
	private String publicationName;

	@JsonProperty("email")
	private String email;

	@JsonProperty("mobile")
	private String mobile;

	@JsonProperty("isActive")
	private boolean isActive;

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

	@JsonProperty("defaultGrid")
	private boolean defaultGrid;
}
