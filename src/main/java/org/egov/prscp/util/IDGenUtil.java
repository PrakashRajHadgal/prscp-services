package org.egov.prscp.util;

import static java.util.Objects.isNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.prscp.web.models.Errors;
import org.egov.prscp.web.models.IdGenModel;
import org.egov.prscp.web.models.IdGenRequestModel;
import org.egov.prscp.web.models.ProcessInstanceRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class IDGenUtil {

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@Value("${egov.idgen.hostname}")
	private String host;

	@Value("${egov.idgen.uri}")
	private String path;

	@Value("${egov.wf.hostname}")
	private String workflowHost;

	@Value("${egov.wf.uri}")
	private String workflowPath;

	@Value("${egov.idgen.ack.name}")
	private String idName;

	/*public String generateApplicationId(String tenantId) {

		String url = host + path;
		ObjectMapper objectMapper = new ObjectMapper();
		RequestInfo requestInfo = new RequestInfo();
		IdGenModel generatedValue = null;
		String applicationId = null;
		List<IdGenModel> idList = Arrays
				.asList(IdGenModel.builder().count(1).idName(idName).tenantId(tenantId).build());
		IdGenRequestModel mcq = new IdGenRequestModel();
		mcq.setRequestInfo(requestInfo);
		mcq.setIdRequests(idList);

		JsonNode response = restTemplate.postForObject(url, mcq, JsonNode.class).findValue("idResponses");

		if (!isNull(response) && response.isArray()) {

			for (JsonNode objNode : response) {
				try {
					generatedValue = objectMapper.treeToValue(objNode, IdGenModel.class);
					applicationId = generatedValue.getId();
				} catch (JsonProcessingException e) {
					log.error("Failed to fetch roles from MDMS", e);
					throw new CustomException("MDMS_ROLE_FETCH_FAILED", "Unable to fetch roles from MDMS");
				}
			}
		}

		return applicationId;
	}*/
	/**
	 * Method to integrate with workflow
	 *
	 * takes the tender request as parameter constructs the work-flow request
	 *
	 * and sets the resultant status from wf-response back to tender object
	 *
	 * @param workflowRequest
	 */
	public ResponseInfo createWorkflowRequest(ProcessInstanceRequest workflowRequest) throws IOException {
		String url = workflowHost + workflowPath;
		ResponseInfo responseInfo = null;
		try {
			JsonNode response = restTemplate.postForObject(url, workflowRequest, JsonNode.class);

			if (!isNull(response)) {
				responseInfo = objectMapper.convertValue(response.get("ResponseInfo"), ResponseInfo.class);
				log.info("Workflow Created Success : " + responseInfo.getMsgId());
			} else {
				log.info("Workflow Creation Failed : Reason " + response);
				throw new CustomException("WORKFLOW_EXCEPTION", "Server Down");
			}
		} catch (HttpStatusCodeException e) {
			log.debug(e.getResponseBodyAsString());
			Errors errors = objectMapper.readValue(e.getResponseBodyAsString(), Errors.class);
			throw new CustomException("WORKFLOW_EXCEPTION",
					errors == null ? "Server Down" : errors.getError().get(0).getMessage());
		}
		return responseInfo;
	}
}
