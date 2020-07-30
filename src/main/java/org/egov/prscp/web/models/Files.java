package org.egov.prscp.web.models;

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
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Files {

	@JsonProperty("fileStoreId")
	private String fileStoreId;

	@JsonProperty("id")
	private String id;

	@JsonProperty("url")
	private String url;

}
