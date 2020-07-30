package org.egov.prscp.web.controllers;

import org.egov.prscp.service.EventCommitteeMasterService;
import org.egov.prscp.web.models.RequestInfoWrapper;
import org.egov.prscp.web.models.ResponseInfoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/committee")
public class EventCommitteeMasterController {

	private final EventCommitteeMasterService eventCommitteeMasterService;
	
	
	@Autowired
	public EventCommitteeMasterController(EventCommitteeMasterService eventCommitteeMasterService) {
		this.eventCommitteeMasterService = eventCommitteeMasterService;
	}
	/**
	 * Get committee for the given criteria
	 * @param requestInfoWrapper to get single or all committees
	 * @return committee Response
	 */

	@PostMapping(value = "/_get")
	public ResponseEntity<ResponseInfoWrapper> getCommittee(@RequestBody RequestInfoWrapper requestInfoWrapper) {
		return eventCommitteeMasterService.getCommittee(requestInfoWrapper);
	}
	/**
	 * Create committee for the given criteria
	 * @param requestInfoWrapper to create committee
	 * @return committee Response
	 */
	@PostMapping(value = "/_create")
	public ResponseEntity<ResponseInfoWrapper> createCommittee(@RequestBody RequestInfoWrapper requestInfoWrapper) {
		return eventCommitteeMasterService.createCommittee(requestInfoWrapper);
	}
	/**
	 * Update committee for the given criteria
	 * @param requestInfoWrapper to update committee
	 * @return Committee Response
	 */
	@PostMapping(value = "/_update")
	public ResponseEntity<ResponseInfoWrapper> updateCommittee(@RequestBody RequestInfoWrapper requestInfoWrapper) {
		return eventCommitteeMasterService.updateCommittee(requestInfoWrapper);
	}

}
