package org.egov.prscp.web.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
/**
 * A Object holds the basic data for a Press Note and press master
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PressNoteMap {
	private String mapPressNoteUuid;
	private String pressMasterUuid;
	private String pressNoteUuid;
	private boolean notifyStatus;
	private String moduleCode;
	private String tenantId;
	private Boolean isActive;
	private String createdBy;
	private String lastModifiedBy;
	private Long createdTime;
	private Long lastModifiedTime;
}
