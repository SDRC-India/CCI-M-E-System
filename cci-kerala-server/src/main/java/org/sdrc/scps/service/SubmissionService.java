package org.sdrc.scps.service;

import java.util.List;
import java.util.Map;

import org.sdrc.scps.models.ApprovalRejectionModel;
import org.sdrc.scps.models.DataEntryQuestionModel;
import org.sdrc.scps.models.NotificationModel;
import org.sdrc.scps.models.ResponseModel;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

public interface SubmissionService {

	public Map<String, Object> getApprovalPendingList(OAuth2Authentication auth, int id);

	/**
	 * 
	 * @param formId
	 * @param auth
	 * @param submissioId
	 * @return
	 * @throws Exception
	 */
	public List<DataEntryQuestionModel> getViewDataForApproval(int formId, OAuth2Authentication auth,
			Integer submissioId) throws Exception;

	public ResponseModel approveRejectSubmission(ApprovalRejectionModel approvalRejectionModel,
			OAuth2Authentication auth);
	
	
	public NotificationModel getPendingNotification(OAuth2Authentication auth);
}
