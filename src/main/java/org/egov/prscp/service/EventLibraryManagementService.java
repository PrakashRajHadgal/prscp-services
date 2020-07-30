
package org.egov.prscp.service;

import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.prscp.repository.EventLibraryManagementRepository;
import org.egov.prscp.util.CommonConstants;
import org.egov.prscp.util.DeviceSource;
import org.egov.prscp.web.models.Library;
import org.egov.prscp.web.models.RequestInfoWrapper;
import org.egov.prscp.web.models.ResponseInfoWrapper;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class EventLibraryManagementService {

	private EventLibraryManagementRepository repository;
	private DeviceSource deviceSource;
	private final ObjectMapper objectMapper;

	@Autowired
	public EventLibraryManagementService(ObjectMapper objectMapper, EventLibraryManagementRepository repository,
			DeviceSource deviceSource) {
		this.objectMapper = objectMapper;
		this.repository = repository;
		this.deviceSource = deviceSource;
	}

	
	/**
	 * Upload Library for the given criteria
	 * @param requestInfoWrapper to upload Library
	 * @return Library Response
	 */
	public ResponseEntity<ResponseInfoWrapper> uploadLibrary(RequestInfoWrapper requestInfoWrapper,
			String requestHeader) {
		try {
			int count = 0;

			Library library = objectMapper.convertValue(requestInfoWrapper.getRequestBody(), Library.class);
			String sourceUuid = deviceSource.saveDeviceDetails(requestHeader, CommonConstants.DEVICE_TENDER,
					library.getTenantId(), library.getModuleCode(), requestInfoWrapper.getAuditDetails());
			library.setSourceUuid(sourceUuid);

			count = repository.checkEventUuid(library);
			if (count > 0) {
				library.setActive(true);
				library.setCreatedBy(requestInfoWrapper.getAuditDetails().getCreatedBy());
				library.setCreatedTime(requestInfoWrapper.getAuditDetails().getCreatedTime());
				List<Library> libr = repository.uploadLibrary(library);
				repository.sendUploadNotification(library);
				return new ResponseEntity<>(ResponseInfoWrapper.builder()
						.responseInfo(ResponseInfo.builder().status(CommonConstants.SUCCESS).build()).responseBody(libr)
						.build(), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(ResponseInfoWrapper.builder().responseInfo(ResponseInfo.builder()
						.status(CommonConstants.FAIL).msgId(CommonConstants.INVALID_EVENT).build()).build(),
						HttpStatus.OK);
			}
		} catch (Exception e) {
			throw new CustomException(CommonConstants.LIBRARY_EXCEPTION_CODE, e.getMessage());
		}
	}

	/**
	 * Get Library for the given criteria
	 * @param requestInfoWrapper to get Library
	 * @return Library Response
	 */
	public ResponseEntity<ResponseInfoWrapper> getLibrary(RequestInfoWrapper requestInfoWrapper) {
		try {
			int count = 0;
			Library library = objectMapper.convertValue(requestInfoWrapper.getRequestBody(), Library.class);
			count = repository.checkEventUuid(library);
			if (count > 0) {
				List<Library> libray1 = repository.getLibrary(library);
				return new ResponseEntity<>(ResponseInfoWrapper.builder()
						.responseInfo(ResponseInfo.builder().status(CommonConstants.SUCCESS).build())
						.responseBody(libray1).build(), HttpStatus.OK);
			} else {
				throw new CustomException(CommonConstants.LIBRARY_EXCEPTION_CODE, CommonConstants.INVALID_LIBRARY);
			}
		} catch (Exception e) {
			throw new CustomException(CommonConstants.LIBRARY_EXCEPTION_CODE, e.getMessage());
		}
	}

	/**
	 * Delete Library for the given criteria
	 * @param requestInfoWrapper to Delete Library
	 * @return Library Response
	 */
	public ResponseEntity<ResponseInfoWrapper> deleteLibrary(RequestInfoWrapper requestInfoWrapper) {
		try {
			int count = 0;
			Library library = objectMapper.convertValue(requestInfoWrapper.getRequestBody(), Library.class);
			count = repository.checkEventUuid(library);
			if (count > 0) {
				library.setActive(false);
				library.setLastModifiedBy(requestInfoWrapper.getAuditDetails().getLastModifiedBy());
				library.setLastModifiedTime(requestInfoWrapper.getAuditDetails().getLastModifiedTime());
				// library.setDocumentType(library.getDocumentList().get(0).getDocumentType());
				library.setDocumentId(library.getDocumentList().get(0).getDocumentId().toString());
				repository.deleteLibrary(library);

				return new ResponseEntity<>(ResponseInfoWrapper.builder()
						.responseInfo(ResponseInfo.builder().status(CommonConstants.SUCCESS).build())
						.responseBody(library).build(), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(ResponseInfoWrapper.builder().responseInfo(ResponseInfo.builder()
						.status(CommonConstants.FAIL).msgId(CommonConstants.INVALID_LIBRARY).build()).build(),
						HttpStatus.OK);
			}

		} catch (Exception e) {
			throw new CustomException(CommonConstants.LIBRARY_EXCEPTION_CODE, e.getMessage());
		}
	}

}