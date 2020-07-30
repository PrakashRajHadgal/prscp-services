package org.egov.prscp.web.models;

import java.util.List;

import org.egov.common.contract.request.RequestInfo;

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
public class IdGenRequestModel {

	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;

	@JsonProperty("idRequests")
	private List<IdGenModel> idRequests;
}
