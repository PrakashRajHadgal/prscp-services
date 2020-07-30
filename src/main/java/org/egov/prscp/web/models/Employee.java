package org.egov.prscp.web.models;

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
public class Employee {

	private Long id;

	private String uuid;

	private String code;

	private String employeeStatus;

	private String employeeType;

	private Long dateOfAppointment;

	private Boolean IsActive;

	private String tenantId;

	private AuditDetails auditDetails;

	private User user;

}
