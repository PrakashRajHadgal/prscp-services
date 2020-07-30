package org.egov.prscp.web.models;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Collection of audit related fields used by most models
 */
@ApiModel(description = "Collection of audit related fields used by most models")
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2018-09-18T17:06:11.263+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class AuditDetails {

	@NotEmpty(message = "createdBy should not be empty or null")
	@JsonProperty("createdBy")
	private String createdBy;

	@NotEmpty(message = "lastModifiedBy should not be empty or null")
	@JsonProperty("lastModifiedBy")
	private String lastModifiedBy;

	@NotEmpty(message = "createdTime should not be empty or null")
	@JsonProperty("createdTime")
	private Long createdTime;

	@NotEmpty(message = "lastModifiedTime should not be empty or null")
	@JsonProperty("lastModifiedTime")
	private Long lastModifiedTime;

}
