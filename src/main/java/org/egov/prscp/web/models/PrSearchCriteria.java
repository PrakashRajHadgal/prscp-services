package org.egov.prscp.web.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class PrSearchCriteria {

	@JsonProperty("tenantId")
	private String tenantId;

	@JsonProperty("status")
	private String status;

	@JsonProperty("ids")
	private List<String> ids;

	@JsonProperty("applicationNumber")
	private String applicationNumber;

	@JsonProperty("mobileNumber")
	private String mobileNumber;

	@JsonIgnore
	private String accountId;

	@JsonProperty("fromDate")
	private Long fromDate = null;

	@JsonProperty("toDate")
	private Long toDate = null;

	@JsonProperty("offset")
	private Integer offset;

	@JsonProperty("limit")
	private Integer limit;

	@JsonIgnore
	private List<String> ownerIds;

}
