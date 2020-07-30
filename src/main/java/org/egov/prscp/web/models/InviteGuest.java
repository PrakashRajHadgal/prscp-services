package org.egov.prscp.web.models;

import javax.validation.constraints.Size;

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
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InviteGuest {

	@JsonProperty("tenantId")
	private String tenantId;

	@JsonProperty("moduleCode")
	private String moduleCode;

	@JsonProperty("eventGuestUuid")
	private String eventGuestUuid;

	@JsonProperty("eventDetailUuid")
	private String eventDetailUuid;

	@JsonProperty("externalFileStoreId")
	private String externalFileStoreId;

	@JsonProperty("eventGuestType")
	private String eventGuestType;

	@JsonProperty("departmentName")
	private String departmentName;

	@JsonProperty("departmentUuid")
	private String departmentUuid;

	@JsonProperty("notificationTemplateUuid")
	private String notificationTemplateUuid;

	@JsonProperty("userUuid")
	private String userUuid;

	@JsonProperty("guestName")
	private String guestName;

	@JsonProperty("guestEmail")
	private String guestEmail;

	@JsonProperty("guestMobile")
	private String guestMobile;

	@JsonProperty("sentFlag")
	private boolean sentFlag;

	@JsonProperty("isActive")
	private boolean isActive;

	@Size(max = 64)
	@JsonProperty("createdBy")
	private String createdBy = null;

	@Size(max = 64)
	@JsonProperty("lastModifiedBy")
	private String lastModifiedBy = null;

	@JsonProperty("createdTime")
	private Long createdTime = null;

	@JsonProperty("lastModifiedTime")
	private Long lastModifiedTime = null;

	@JsonProperty("sourceUuid")
	private String sourceUuid;
}
