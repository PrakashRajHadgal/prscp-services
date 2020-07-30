package org.egov.prscp.web.models;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ApiModel(description = "A Object holds the basic data for a pr")
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2018-09-18T17:06:11.263+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PublicRelationEvent {
	@Size(max = 64)
	@JsonProperty("id")
	private String id = null;

	@NotNull
	@Size(max = 64)
	@JsonProperty("tenantId")
	private String tenantId = null;

	@Size(max = 64)
	@JsonProperty("applicationNumber")
	private String applicationNumber;

	@NotNull
	@Size(max = 64)
	@JsonProperty("financialYear")
	private String financialYear = null;

	@JsonProperty("validFrom")
	private Long validFrom = null;

	@JsonProperty("validTo")
	private Long validTo = null;

	/**
	 * 1. Perform action to change the state of the prscp services. 2. INITIATE, if
	 * application is getting submitted without required document. 3. APPLY, if
	 * application is getting submitted with application documents, in that case api
	 * will validate all the required application document. 4. APPROVE action is
	 * only applicable for specific role, that role has to be configurable at
	 * service level. Employee can approve a application only if application is in
	 * APPLIED state and Licesance fees is paid.
	 */

	@NotNull
	@Size(max = 64)
	@JsonProperty("action")
	private String action = null;

	@Size(max = 64)
	@JsonProperty("assignee")
	private String assignee = null;

	@Valid
	@JsonProperty("wfDocuments")
	private List<Document> wfDocuments;

	@Size(max = 64)
	@JsonProperty("status")
	private String status = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

	@Size(max = 128)
	private String comment;

}
