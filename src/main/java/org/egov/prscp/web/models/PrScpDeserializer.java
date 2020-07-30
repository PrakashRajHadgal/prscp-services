package org.egov.prscp.web.models;

import java.util.HashMap;

import org.springframework.kafka.support.serializer.JsonDeserializer;

public class PrScpDeserializer extends JsonDeserializer<HashMap> {
	public PrScpDeserializer() {
		super(HashMap.class);
	}
}
