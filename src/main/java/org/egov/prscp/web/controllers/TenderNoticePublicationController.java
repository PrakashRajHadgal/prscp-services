package org.egov.prscp.web.controllers;

import javax.validation.Valid;

import org.egov.prscp.service.TenderNoticePublicationService;
import org.egov.prscp.web.models.RequestInfoWrapper;
import org.egov.prscp.web.models.ResponseInfoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/tender")
public class TenderNoticePublicationController {

	private final TenderNoticePublicationService tenderNoticePublicationService;

	@Autowired
	public TenderNoticePublicationController(TenderNoticePublicationService tenderNoticePublicationService) {
		this.tenderNoticePublicationService = tenderNoticePublicationService;
	}
	/**
	 * Creates Tender for the given criteria
	 * @param requestInfoWrapper to create tender 
	 * @return Tender Response
	 */
	@PostMapping(value = "/_create")
	public ResponseEntity<ResponseInfoWrapper> createTender(
			@RequestBody @Valid @Validated RequestInfoWrapper requestInfoWrapper,
			@RequestHeader("User-Agent") String request) {
		return tenderNoticePublicationService.createTender(requestInfoWrapper, request);
	}

	
	/**
	 * Update Tender for the given criteria
	 * @param requestInfoWrapper to update tender 
	 * @return Tender Response
	 */
	@PostMapping(value = "/_update")
	public ResponseEntity<ResponseInfoWrapper> updateTender(@Valid @RequestBody RequestInfoWrapper requestInfoWrapper) {
		return tenderNoticePublicationService.updateTender(requestInfoWrapper);
	}
	
	/**
	 * Get Tender for the given criteria
	 * @param requestInfoWrapper to get tender 
	 * @return Tender Response
	 */
	@PostMapping(value = "/_get")
	public ResponseEntity<ResponseInfoWrapper> getTender(@Valid @RequestBody RequestInfoWrapper requestInfoWrapper) {
		return tenderNoticePublicationService.getTender(requestInfoWrapper);
	}

	/**
	 * Publish Tender for the given criteria
	 * @param requestInfoWrapper to publish tender 
	 * @return Tender Response
	 */
	@PostMapping(value = "/_publish")
	public ResponseEntity<ResponseInfoWrapper> publish(@Valid @RequestBody RequestInfoWrapper requestInfoWrapper) {
		return tenderNoticePublicationService.publish(requestInfoWrapper);
	}
}
