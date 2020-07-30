package org.egov.prscp.web.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FileStore {

	@JsonProperty("fileStoreIds")
	private List<Files> fileStoreIds;
}
