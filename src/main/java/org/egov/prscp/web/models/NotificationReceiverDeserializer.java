package org.egov.prscp.web.models;

import org.springframework.kafka.support.serializer.JsonDeserializer;

public class NotificationReceiverDeserializer extends JsonDeserializer<NotificationReceiver> {
	public NotificationReceiverDeserializer() {
		super(NotificationReceiver.class);
	}
}
