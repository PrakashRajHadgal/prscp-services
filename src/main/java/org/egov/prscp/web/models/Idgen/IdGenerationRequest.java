package org.egov.prscp.web.models.Idgen;

import java.util.List;

import org.egov.common.contract.request.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
/**
 * <h1>IdGenerationRequest</h1>

 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class IdGenerationRequest {

	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;

	private List<IdRequest> idRequests;

}
