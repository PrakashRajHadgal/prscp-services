package org.egov.prscp.web.models.user;

import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
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
public class UserDetailResponse {
	@JsonProperty("responseInfo")
	ResponseInfo responseInfo;

	@JsonProperty("user")
	List<OwnerInfo> user;
}
