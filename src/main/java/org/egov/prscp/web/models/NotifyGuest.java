package org.egov.prscp.web.models;

import javax.validation.constraints.NotNull;
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
public class NotifyGuest {

	@Size(max = 64)
	@NotNull
	@JsonProperty("notifyUuid")
	private String notifyUuid;

	@Size(max = 64)

	@JsonProperty("eventId")
	private String eventId;

	@Size(max = 64)
	@NotNull
	@JsonProperty("eventDetailUuid")
	private String eventDetailUuid;

	@Size(max = 50)
	@NotNull
	@JsonProperty("guestType")
	private String guestType;

	@Size(max = 64)

	@JsonProperty("departmentUuid")
	private String departmentUuid;

	@Size(max = 64)

	@JsonProperty("userUuid")
	private String userUuid;

	@Size(max = 255)

	@JsonProperty("guestName")
	private String guestName;

	@Size(max = 255)

	@JsonProperty("guestEmail")
	private String guestEmail;

	@Size(max = 50)

	@JsonProperty("guestMobile")
	private String guestMobile;

	@Size(max = 64)
	@JsonProperty("notificationTemplateUuid")
	private String notificationTemplateUuid;

	@JsonProperty("sentFlag")
	private boolean sentFlag;

	@NotNull
	@JsonProperty("isActive")
	private boolean isActive;

}
