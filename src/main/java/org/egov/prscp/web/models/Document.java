package org.egov.prscp.web.models;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(of = { "id" })
public class Document {

	@Size(max = 64)
	@JsonProperty("id")
	private String id = null;

	@Size(max = 64)
	@JsonProperty("tenantId")
	private String tenantId = null;

	@Size(max = 64)
	@JsonProperty("documentType")
	private String documentType = null;

	@Size(max = 64)
	@JsonProperty("fileStoreId")
	private String fileStoreId = null;

	@Size(max = 64)
	@JsonProperty("documentUid")
	private String documentUid = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails;

}
