
package org.egov.prscp.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.prscp.config.PrScpConfiguration;
import org.egov.prscp.producer.Producer;
import org.egov.prscp.repository.EventCommitteeMasterRepository;
import org.egov.prscp.util.CommonConstants;
import org.egov.prscp.web.models.CommitteeDetail;
import org.egov.prscp.web.models.CommitteeMember;
import org.egov.prscp.web.models.RequestInfoWrapper;
import org.egov.prscp.web.models.ResponseInfoWrapper;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class EventCommitteeMasterService {

	private EventCommitteeMasterRepository repository;

	private PrScpConfiguration config;

	private ObjectMapper objectMapper;

	private Producer producer;

	@Autowired
	public EventCommitteeMasterService(EventCommitteeMasterRepository repository, PrScpConfiguration config,
			ObjectMapper objectMapper, Producer producer) {
		this.repository = repository;
		this.config = config;
		this.objectMapper = objectMapper;
		this.producer = producer;
	}

	/**
	 * Get committee for the given criteria
	 * @param requestInfoWrapper to get single or all committee
	 * @return list of Events
	 */
	public ResponseEntity<ResponseInfoWrapper> getCommittee(RequestInfoWrapper requestInfoWrapper) {
		try {
			CommitteeDetail committeeDetail = objectMapper.convertValue(requestInfoWrapper.getRequestBody(),
					CommitteeDetail.class);

			List<CommitteeDetail> data = repository.getCommittee(committeeDetail);

			return new ResponseEntity<>(ResponseInfoWrapper.builder()
					.responseInfo(ResponseInfo.builder().status(CommonConstants.SUCCESS).build()).responseBody(data)
					.build(), HttpStatus.OK);

		} catch (Exception e) {
			throw new CustomException(CommonConstants.COMMITTEE_MASTER_EXCEPTION_CODE, e.getMessage());
		}
	}

	
	/**
	 * Create committee for the given criteria
	 * @param requestInfoWrapper to create committee
	 * Pushes the request on save topic
	 * @return The object of created committee
	 */
	public ResponseEntity<ResponseInfoWrapper> createCommittee(RequestInfoWrapper requestInfoWrapper) {

		try {

			CommitteeDetail committeeDetail = objectMapper.convertValue(requestInfoWrapper.getRequestBody(),
					CommitteeDetail.class);
			List<CommitteeDetail> existingData = repository.getCommitteeByName(committeeDetail);
			if (existingData != null && !existingData.isEmpty()) {

				return new ResponseEntity<>(
						ResponseInfoWrapper.builder()
								.responseInfo(ResponseInfo.builder().status(CommonConstants.FAIL)
										.msgId(CommonConstants.COMMIITEE_IS_EXISTS).build())
								.responseBody(null).build(),
						HttpStatus.OK);
			} else {

				String committeeUuid = UUID.randomUUID().toString();
				List<CommitteeMember> listCommitteeMember = new ArrayList<>();
				CommitteeDetail committee = new CommitteeDetail();
				committee.setCommitteeUuid(committeeUuid);
				committee.setCommitteeDescription(committeeDetail.getCommitteeDescription());
				committee.setCommitteeName(committeeDetail.getCommitteeName());
				committee.setTenantId(committeeDetail.getTenantId());
				committee.setModuleCode(committeeDetail.getModuleCode());
				committee.setActive(true);
				committee.setCreatedBy(requestInfoWrapper.getAuditDetails().getCreatedBy());
				committee.setCreatedTime(requestInfoWrapper.getAuditDetails().getCreatedTime());
				committee.setLastModifiedBy(requestInfoWrapper.getAuditDetails().getLastModifiedBy());
				committee.setLastModifiedTime(requestInfoWrapper.getAuditDetails().getLastModifiedTime());

				for (CommitteeMember member : committeeDetail.getCommitteeMember()) {

					String memberUuid = UUID.randomUUID().toString();

					member.setCommitteeMemberUuid(memberUuid);
					member.setCommitteeUuid(committeeUuid);
					member.setActive(true);
					member.setTenantId(committeeDetail.getTenantId());
					member.setModuleCode(committeeDetail.getModuleCode());
					member.setCreatedBy(requestInfoWrapper.getAuditDetails().getCreatedBy());
					member.setCreatedTime(requestInfoWrapper.getAuditDetails().getCreatedTime());
					member.setLastModifiedBy(requestInfoWrapper.getAuditDetails().getLastModifiedBy());
					member.setLastModifiedTime(requestInfoWrapper.getAuditDetails().getLastModifiedTime());
					listCommitteeMember.add(member);

				}

				committee.setCommitteeMember(listCommitteeMember);
				List<CommitteeDetail> listCommittee = Arrays.asList(committee);
				RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().requestBody(listCommittee).build();
				producer.push(config.getCreateCommitteeTopic(), infoWrapper);

				return new ResponseEntity<>(ResponseInfoWrapper.builder()
						.responseInfo(ResponseInfo.builder().status(CommonConstants.SUCCESS).build())
						.responseBody(listCommittee).build(), HttpStatus.CREATED);
			}

		} catch (Exception e) {
			throw new CustomException(CommonConstants.COMMITTEE_MASTER_EXCEPTION_CODE, e.getMessage());
		}
	}

	/**
	 * Update committee for the given criteria
	 * @param requestInfoWrapper to update committee
	 * Pushes the request on update topic
	 * @return The object of updated committee
	 */
	public ResponseEntity<ResponseInfoWrapper> updateCommittee(RequestInfoWrapper requestInfoWrapper) {

		try {
			CommitteeDetail committeeDetail = objectMapper.convertValue(requestInfoWrapper.getRequestBody(),
					CommitteeDetail.class);
			List<CommitteeDetail> existingData = repository.getCommitteeByName(committeeDetail);
			if (existingData != null && !existingData.isEmpty()) {
				CommitteeDetail exsting = existingData.get(0);
				if (!exsting.getCommitteeUuid().equals(committeeDetail.getCommitteeUuid()))
					throw new CustomException(CommonConstants.COMMITTEE_MASTER_EXCEPTION_CODE,
							CommonConstants.COMMIITEE_IS_EXISTS);
			}

			CommitteeDetail committeeCriteria = CommitteeDetail.builder()
					.committeeUuid(committeeDetail.getCommitteeUuid()).tenantId(committeeDetail.getTenantId())
					.moduleCode(committeeDetail.getModuleCode()).build();
			List<CommitteeDetail> existingCommittee = repository.getCommittee(committeeCriteria);

			committeeDetail.setActive(true);
			committeeDetail.setLastModifiedBy(requestInfoWrapper.getAuditDetails().getLastModifiedBy());
			committeeDetail.setLastModifiedTime(requestInfoWrapper.getAuditDetails().getLastModifiedTime());
			committeeDetail.setCreatedTime(requestInfoWrapper.getAuditDetails().getCreatedTime());
			
			List<CommitteeMember> listCommitteeMember = new ArrayList<>();
			for (CommitteeMember member : committeeDetail.getCommitteeMember()) {
				String memberUuid = UUID.randomUUID().toString();
				member.setCommitteeMemberUuid(memberUuid);
				member.setCommitteeUuid(committeeDetail.getCommitteeUuid());
				member.setActive(true);
				member.setTenantId(committeeDetail.getTenantId());
				member.setModuleCode(committeeDetail.getModuleCode());
				member.setCreatedBy(requestInfoWrapper.getAuditDetails().getCreatedBy());
				member.setCreatedTime(requestInfoWrapper.getAuditDetails().getCreatedTime());
				member.setLastModifiedBy(requestInfoWrapper.getAuditDetails().getLastModifiedBy());
				member.setLastModifiedTime(requestInfoWrapper.getAuditDetails().getLastModifiedTime());
				listCommitteeMember.add(member);
			}
			committeeDetail.setCommitteeMember(listCommitteeMember);
			List<CommitteeDetail> listCommittee = Arrays.asList(committeeDetail);
			RequestInfoWrapper infoWrapper = RequestInfoWrapper.builder().requestBody(listCommittee).build();
			producer.push(config.getUpdateCommitteeTopic(), infoWrapper);

			return new ResponseEntity<>(ResponseInfoWrapper.builder()
					.responseInfo(ResponseInfo.builder().status(CommonConstants.SUCCESS).build())
					.responseBody(listCommittee).build(), HttpStatus.OK);

		} catch (Exception e) {
			throw new CustomException(CommonConstants.COMMITTEE_MASTER_EXCEPTION_CODE, e.getMessage());
		}
	}
}