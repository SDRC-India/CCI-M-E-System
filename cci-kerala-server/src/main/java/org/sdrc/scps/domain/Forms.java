/**
 * 
 */
package org.sdrc.scps.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author Harsh Pratyush(harsh@sdrc.co.in)
 *
 */

@Entity
@Table(name="mst_forms")
public class Forms {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="form_id_fk")
	private int formId;
	
	
	@Column(nullable=false)
	private String formTitle;
	
	
	@Column(nullable=false)
	private String submissionUrl;
	
	
	@Column(nullable=false)
	private String approvalUrl;
	
	
	@Column(nullable=false)
	private boolean isLive;
	
	
	@Temporal(TemporalType.DATE)
	@Column(nullable=false)
	private Date startDate;
	
	
	@Temporal(TemporalType.DATE)
	@Column(nullable=true)
	private Date endDate;


	@Column(nullable=false)
	private String tableName;


	public Forms() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Forms(int formId) {
		super();
		this.formId = formId;
	}


	public int getFormId() {
		return formId;
	}


	public void setFormId(int formId) {
		this.formId = formId;
	}


	public String getFormTitle() {
		return formTitle;
	}


	public void setFormTitle(String formTitle) {
		this.formTitle = formTitle;
	}


	public String getSubmissionUrl() {
		return submissionUrl;
	}


	public void setSubmissionUrl(String submissionUrl) {
		this.submissionUrl = submissionUrl;
	}


	public String getApprovalUrl() {
		return approvalUrl;
	}


	public void setApprovalUrl(String approvalUrl) {
		this.approvalUrl = approvalUrl;
	}


	public boolean isLive() {
		return isLive;
	}


	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}


	public Date getStartDate() {
		return startDate;
	}


	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}


	public Date getEndDate() {
		return endDate;
	}


	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	
}
