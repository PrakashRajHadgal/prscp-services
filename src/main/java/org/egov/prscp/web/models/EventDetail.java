package org.egov.prscp.web.models;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.json.simple.JSONArray;

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
@NoArgsConstructor
@AllArgsConstructor
/**
 * A Object holds the basic data for a event
 */
@ToString
public class EventDetail {

	@Size(max = 64)
	@NotNull
	@JsonProperty("eventDetailUuid")
	private String eventDetailUuid;

	@Size(max = 255)
	@NotNull
	@JsonProperty("moduleCode")
	private String moduleCode;

	@Size(max = 255)
	@NotNull
	@JsonProperty("eventTitle")
	private String eventTitle;

	@Size(max = 255)
	@NotNull
	@JsonProperty("eventLocation")
	private String eventLocation;

	@Size(max = 64)
	@NotNull
	@JsonProperty("sector")
	private String sector;

	@Size(max = 64)
	@JsonProperty("organizerDepartmentUuid")
	private String organizerDepartmentUuid;
	@Size(max = 64)
	@JsonProperty("organizerDepartmentName")
	private String organizerDepartmentName;

	@Size(max = 64)
	@JsonProperty("organizerDepartmentUserUuid")
	private String organizerDepartmentUserUuid;

	@Size(max = 64)
	@JsonProperty("organizerUsernName")
	private String organizerUsernName;

	@Size(max = 64)
	@JsonProperty("facebookUrl")
	private String facebookUrl;

	@Size(max = 255)
	@JsonProperty("twitterUrl")
	private String twitterUrl;

	@Size(max = 255)
	@JsonProperty("instagramUrl")
	private String instagramUrl;

	@Size(max = 512)
	@JsonProperty("startDate")
	private String startDate;

	@Size(max = 512)
	@JsonProperty("startTime")
	private String startTime;

	@Size(max = 512)
	@JsonProperty("endDate")
	private String endDate;

	@Size(max = 512)
	@JsonProperty("endTime")
	private String endTime;

	@Size(max = 1024)
	@JsonProperty("eventDescription")
	private String eventDescription;

	@Size(max = 64)
	@NotNull
	@JsonProperty("eventType")
	private String eventType;

	@JsonProperty("eventImage")
	private JSONArray eventImage;

	@NotNull
	@JsonProperty("eventBudget")
	private Double eventBudget;

	@Size(max = 64)
	@JsonProperty("committeeUuid")
	private String committeeUuid;

	@Size(max = 64)
	@JsonProperty("committeeName")
	private String committeeName;

	@Size(max = 64)
	@JsonProperty("eventStatus")
	private String eventStatus;

	@Size(max = 64)
	@JsonProperty("status")
	private String status;

	@NotNull
	@JsonProperty("isActive")
	private boolean isActive;

	@Size(max = 64)
	@JsonProperty("createdBy")
	private String createdBy;

	@JsonProperty("createdTime")
	private Long createdTime;

	@JsonProperty("createdDate")
	private Long createdDate;

	@Size(max = 64)
	@JsonProperty("lastModifiedBy")
	private String lastModifiedBy;

	@JsonProperty("lastModifiedTime")
	private Long lastModifiedTime;

	@JsonProperty("lastModifiedDate")
	private Long lastModifiedDate;

	@Size(max = 64)
	@JsonProperty("tenantId")
	private String tenantId;

	@Size(max = 64)
	@JsonProperty("eventId")
	private String eventId;

	private String eventString;

	@Size(max = 64)
	@JsonProperty("notificationTemplateUuid")
	private String notificationTemplateUuid;
	@Size(max = 256)
	@JsonProperty("area")
	private String area;

	@JsonProperty("defaultGrid")
	private boolean defaultGrid;

	@JsonProperty("inviteGuest")
	private List<InviteGuest> inviteGuest;

	@JsonProperty("sourceUuid")
	private String sourceUuid;
}
