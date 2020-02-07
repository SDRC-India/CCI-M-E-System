/**
 * 
 */
package org.sdrc.scps.controller;

import java.util.List;
import java.util.Map;

import org.sdrc.scps.models.ApprovalRejectionModel;
import org.sdrc.scps.models.DataEntryQuestionModel;
import org.sdrc.scps.models.NotificationModel;
import org.sdrc.scps.models.ResponseModel;
import org.sdrc.scps.service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 * @author Sourav Nath (sourav@sdrc.co.in)
 *
 */

@RestController
public class SubmissionController {

	@Autowired
	private SubmissionService submissionService;

	@PreAuthorize("hasAnyAuthority('submissionManagement')")
	@GetMapping("getApprovalPendingList")
	ResponseEntity<Map<String, Object>> getApprovalPendingList(OAuth2Authentication auth,
			@RequestParam(name = "id", required = false) Integer id) {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(submissionService.getApprovalPendingList(auth, id));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}

	}

	@PreAuthorize("hasAnyAuthority('submissionManagement')")
	@GetMapping("getViewDataForApproval")
	ResponseEntity<List<DataEntryQuestionModel>> getViewDataForApproval(int formId, OAuth2Authentication auth,
			@RequestParam(name = "submissioId", required = false) Integer submissioId) {
		try {
			return ResponseEntity.status(HttpStatus.OK)
					.body(submissionService.getViewDataForApproval(formId, auth, submissioId));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}

	}
	
	
	@PreAuthorize("hasAuthority('submissionManagement')")
	@PostMapping("approveRejectSubmission")
	ResponseEntity<ResponseModel> approveRejectSubmission(
			@RequestBody ApprovalRejectionModel approvalRejectionModel,
			OAuth2Authentication auth) {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(submissionService.approveRejectSubmission(approvalRejectionModel, auth));

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(null);
		}
	}
	
	@PreAuthorize("hasAuthority('submissionManagement')")
	@GetMapping("getPendingNotification")
	ResponseEntity<NotificationModel> getPendingNotification(
			OAuth2Authentication auth) {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(submissionService.getPendingNotification(auth));

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(null);
		}
	}
}
