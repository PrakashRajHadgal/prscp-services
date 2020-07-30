package org.egov.prscp.web.models.Idgen;

import java.util.List;

import org.egov.common.contract.response.ResponseInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
/**
 * <h1>IdGenerationResponse</h1>

 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class IdGenerationResponse {

	private ResponseInfo responseInfo;

	private List<IdResponse> idResponses;

}
