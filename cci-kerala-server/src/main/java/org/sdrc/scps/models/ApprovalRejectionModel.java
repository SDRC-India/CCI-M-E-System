/**
 * 
 */
package org.sdrc.scps.models;

import java.util.List;

/**
 * @author SDRC_DEV
 *
 */
public class ApprovalRejectionModel {
	
	private boolean approved;
	
	private String remarks;
	
	private List<Integer> rejectedSections;
	
	private List<QuestionModel> questionModels;
	
	private int formId;

	
	
	public int getFormId() {
		return formId;
	}

	public void setFormId(int formId) {
		this.formId = formId;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public List<Integer> getRejectedSections() {
		return rejectedSections;
	}

	public void setRejectedSections(List<Integer> rejectedSections) {
		this.rejectedSections = rejectedSections;
	}

	public List<QuestionModel> getQuestionModels() {
		return questionModels;
	}

	public void setQuestionModels(List<QuestionModel> questionModels) {
		this.questionModels = questionModels;
	}
	
}
