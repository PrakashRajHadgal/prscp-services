package org.egov.prscp.web.models;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Size;

import org.json.simple.JSONArray;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
/**
 * A Object holds the basic data for a Tender Notice
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TenderNotice {

	@Size(max = 64)
	@JsonProperty("tenderNoticeUuid")
	private String tenderNoticeUuid;

	@Size(max = 150)
	@JsonProperty("tenderSubject")
	private String tenderSubject;

	@Size(max = 15)
	@JsonProperty("tenderDate")
	private String tenderDate;

	@JsonProperty("fileNumber")
	private String fileNumber;

	@JsonProperty("tenderDetail")
	private String tenderDetail;

	@Size(max = 64)
	@JsonProperty("publicationSize")
	private String publicationSize;

	@Size(max = 64)
	@JsonProperty("userType")
	private String userType;

	@Size(max = 64)
	@JsonProperty("tenderNoticeStatus")
	private String tenderNoticeStatus;

	@JsonProperty("isActive")
	private boolean isActive;

	@JsonProperty("tenantId")
	private String tenantId;

	@JsonProperty("tenderNoticeId")
	private String tenderNoticeId;

	@JsonProperty("createdBy")
	private String createdBy = null;

	@JsonProperty("createdByName")
	private String createdByName = null;

	@JsonProperty("notificationTemplateUuid")
	private String notificationTemplateUuid;

	@JsonProperty("lastModifiedBy")
	private String lastModifiedBy = null;

	@JsonProperty("createdTime")
	private Long createdTime = null;

	@JsonProperty("lastModifiedTime")
	private Long lastModifiedTime = null;

	@JsonProperty("noteContent")
	private String noteContent;

	@JsonProperty("tenderDocument")
	private JSONArray tenderDocument;

	private String tenderDocumentList;

	@JsonProperty("moduleCode")
	private String moduleCode;

	@JsonProperty("publicationList")
	private List<PublicationList> publicationList;

	/*
	 * @JsonProperty("tenderPressList") private JSONArray tenderPressList;
	 */

	@JsonProperty("emailContentJson")
	private JSONArray emailContentJson;

	private String emailContentData;

	@JsonProperty("smsContent")
	private String smsContent;

	@JsonProperty("notificationTemplateList")
	private ArrayList<NotificationTemplate> notificationTemplateList;

	@JsonProperty("fromDate")
	private String fromDate;

	@JsonProperty("toDate")
	private String toDate;

	@JsonProperty("defaultGrid")
	private boolean defaultGrid;

	@JsonProperty("sourceUuid")
	private String sourceUuid;
}
