package org.egov.prscp.web.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Getter
@EqualsAndHashCode
public class EmailAttachment {
	private String name;
	private String url;
	private String mimeType;
}