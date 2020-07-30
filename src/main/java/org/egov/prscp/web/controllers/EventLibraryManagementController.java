package org.egov.prscp.web.controllers;

import javax.validation.Valid;

import org.egov.prscp.service.EventLibraryManagementService;
import org.egov.prscp.web.models.RequestInfoWrapper;
import org.egov.prscp.web.models.ResponseInfoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/library")
public class EventLibraryManagementController {

	private final EventLibraryManagementService eventLibraryManagementService;

	@Autowired
	public EventLibraryManagementController(EventLibraryManagementService eventLibraryManagementService) {
		this.eventLibraryManagementService = eventLibraryManagementService;
	}

	/**
	 * Upload Library for the given criteria
	 * @param requestInfoWrapper to upload Library
	 * @return Library Response
	 */
	@PostMapping(value = "/_upload")
	public ResponseEntity<ResponseInfoWrapper> uploadLibrary(@RequestBody RequestInfoWrapper requestInfoWrapper,
			@RequestHeader("User-Agent") String request) {
		return eventLibraryManagementService.uploadLibrary(requestInfoWrapper, request);
	}

	/**
	 * Get Library for the given criteria
	 * @param requestInfoWrapper to get Library
	 * @return Library Response
	 */
	@PostMapping(value = "/_get")
	public ResponseEntity<ResponseInfoWrapper> getLibrary(@RequestBody RequestInfoWrapper requestInfoWrapper) {
		return eventLibraryManagementService.getLibrary(requestInfoWrapper);

	}
	/**
	 * Delete Library for the given criteria
	 * @param requestInfoWrapper to Delete Library
	 * @return Library Response
	 */
	@PostMapping(value = "/_delete")
	public ResponseEntity<ResponseInfoWrapper> deletePress(@Valid @RequestBody RequestInfoWrapper requestInfoWrapper) {
		return eventLibraryManagementService.deleteLibrary(requestInfoWrapper);
	}
}
