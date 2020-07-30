package org.egov.prscp.web.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@ToString
public class EmailRequest {
	private String email;
	private String subject;
	private String body;
	@JsonProperty("isHTML")
	private boolean isHTML;
	private List<EmailAttachment> attachments;

	public Email toDomain() {
		return Email.builder().toAddress(email).subject(subject).body(body).attachments(attachments).html(isHTML)
				.build();
	}
}
