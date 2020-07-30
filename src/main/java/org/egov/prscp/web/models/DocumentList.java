package org.egov.prscp.web.models;

import org.json.simple.JSONArray;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ApiModel(description = "A Object holds the basic data for a Public relation")
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2018-09-18T17:06:11.263+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class DocumentList {
	@JsonProperty("documentType")
	private String documentType;

	@JsonProperty("documentId")
	private JSONArray documentId;

	@JsonProperty("fileStoreId")
	private String fileStoreId;

}
