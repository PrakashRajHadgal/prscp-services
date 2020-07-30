package org.egov.prscp.web.models;
/**
 * A Object holds the basic data for a Idgen
 */
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
public class IdGenModel {
	private Integer count;
	private String idName;
	private String tenantId;
	private String id;
}
