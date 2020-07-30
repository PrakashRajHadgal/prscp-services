package org.egov.prscp.util;

import java.util.Arrays;

import org.egov.common.contract.request.RequestInfo;
import org.egov.prscp.web.models.RequestInfoWrapper;
import org.egov.tracer.model.CustomException;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UserUtil {

	@Autowired
	RestTemplate restTemplate;

	@Value("${egov.user.hostname}")
	private String host;

	@Value("${egov.user.uri}")
	private String path;

	@Value("${egov.hrms.hostname}")
	private String hrmsHost;

	@Value("${egov.hrms.uri}")
	private String hrmspath;

	public JsonNode getUserId(String userId) throws JSONException {
		try {
			String url = host + path;
			JsonNode response = null;
			JSONObject userSearchRequest = new JSONObject();
			userSearchRequest.put("id", Arrays.asList(userId));
			try {
				response = restTemplate.postForObject(url, userSearchRequest, JsonNode.class);
			} catch (Exception e) {
				System.out.println("Error While Connecing user sevice");
			}
			return response;

		} catch (Exception e) {
			throw new CustomException("USER_SERVICE_EXCEPTION", e.getMessage());
		}
	}

	public JsonNode getUserUuid(String userId) throws JSONException {
		try {
			String url = host + path;
			JsonNode response = null;
			JSONObject userSearchRequest = new JSONObject();
			userSearchRequest.put("uuid", Arrays.asList(userId));
			try {
				response = restTemplate.postForObject(url, userSearchRequest, JsonNode.class);
			} catch (Exception e) {
				System.out.println("Error While Connecing user sevice");
			}
			return response;

		} catch (Exception e) {
			throw new CustomException("USER_SERVICE_EXCEPTION", e.getMessage());
		}
	}

	public JsonNode getUserByRole(RequestInfo requestInfo, String role, String tenantId) {
		String url = hrmsHost + hrmspath;
		JsonNode response = null;
		try {
			UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url).queryParam("roles", role)
					.queryParam("tenantId", tenantId);
			response = restTemplate.postForObject(builder.toUriString(),
					RequestInfoWrapper.builder().requestInfo(requestInfo).build(), JsonNode.class);
		} catch (Exception e) {
			log.info("Error While Connecing HRMS sevice");
		}
		if (response != null && response.get("Employees") != null) {

			response = response.get("Employees");
		}
		return response;
	}

}
