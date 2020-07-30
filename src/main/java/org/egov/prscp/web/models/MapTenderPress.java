package org.egov.prscp.web.models;

import org.json.simple.JSONArray;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
/**
 * A Object holds the basic data for a Tender Notice and press master
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MapTenderPress {

	@JsonProperty("mapTenderPressUuid")
	private String mapTenderPressUuid;

	@JsonProperty("pressMasterUuid")
	private String pressMasterUuid;

	@JsonProperty("tenderNoticeUuid")
	private String tenderNoticeUuid;

	@JsonProperty("isActive")
	private boolean isActive;

	@JsonProperty("tenantId")
	private String tenantId;

	@JsonProperty("createdBy")
	private String createdBy;

	@JsonProperty("lastModifiedBy")
	private String lastModifiedBy;

	@JsonProperty("createdTime")
	private Long createdTime;

	@JsonProperty("lastModifiedTime")
	private Long lastModifiedTime;

	@JsonProperty("documentAttachment")
	private JSONArray documentAttachment;

	private String notifiactionTemplateUuid;

	@JsonProperty("moduleCode")
	private String moduleCode;

	@JsonProperty("notifyStatus")
	private boolean notifyStatus;

}
