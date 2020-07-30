package org.egov.prscp.web.models;

import java.util.List;

import org.egov.common.contract.response.ResponseInfo;

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
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Errors {

	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo;
	@JsonProperty("Errors")
	private List<ErrorResponseInfo> error;

}
