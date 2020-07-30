package org.egov.prscp.web.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@Builder
@ToString
public class Email {
	private String toAddress;
	private String subject;
	private String body;
	private boolean html;
	private List<EmailAttachment> attachments;
}
