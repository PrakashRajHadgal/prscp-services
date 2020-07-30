package org.egov.prscp.web.models.user;

import org.egov.common.contract.request.RequestInfo;
import org.egov.prscp.web.models.OwnerInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@ToString
public class CreateUserRequest {

	@JsonProperty("requestInfo")
	private RequestInfo requestInfo;

	@JsonProperty("user")
	private OwnerInfo user;

}
