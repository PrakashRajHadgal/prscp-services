package org.egov.prscp.web.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class SMSRequest {
	private String mobileNumber;
	private String message;

	public Sms toDomain() {
		return new Sms(mobileNumber, message, Priority.HIGH);
	}
}
