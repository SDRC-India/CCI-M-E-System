/**
 * 
 */
package org.sdrc.scps.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.sdrc.scps.util.SubmissionStatus;
import org.sdrc.usermgmt.domain.Account;
import org.springframework.data.annotation.CreatedBy;

/**
 * @author harsh
 *
 */

@Entity
@Table(name = "mst_institution_data")
public class InstitutionData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int instutionId;;

	@ManyToOne
	@JoinColumn(name = "q1")
	private Options q1;

	@ManyToOne
	@JoinColumn(name = "q2")
	private Options q2;

	private String q3;
	private String q4;

	@ManyToOne
	@JoinColumn(name = "q5")
	private Options q5;

	@ManyToOne
	@JoinColumn(name = "q6")
	private Options q6;

	@ManyToOne
	@JoinColumn(name = "q7")
	private Area q7;

	private String q8;

	private String q9;

	@ManyToOne
	@JoinColumn(name = "q10")
	private Area q10;

	@ManyToOne
	@JoinColumn(name = "q11")
	private Area q11;

	private String q12;

	private String q13;
	private String q14;

	private String q15;

	private String q16;

	private String q17;
	@Temporal(TemporalType.DATE)
	private Date q18;

	@Temporal(TemporalType.DATE)
	private Date q19;

	private String q20;
	private String q21;

	@Temporal(TemporalType.DATE)
	private Date q22;

	@Temporal(TemporalType.DATE)
	private Date q23;

	private String q24;

	private String q25;

	private String q26;

	@ManyToOne
	@JoinColumn(name = "q27")
	private Options q27;

	private String q28;

	private String q29;

	private String q30;

	private String q31;

	private String q32;

	@OneToMany(mappedBy = "institutionData")
	private List<BuildingDetails> buildingDetails;

	/**
	 * @return the buildingDetails
	 */
	public List<BuildingDetails> getBuildingDetails() {
		return buildingDetails;
	}

	/**
	 * @param buildingDetails the buildingDetails to set
	 */
	public void setBuildingDetails(List<BuildingDetails> buildingDetails) {
		this.buildingDetails = buildingDetails;
	}

	@OneToMany(mappedBy = "institutionData")
	private List<ManagementDetails> managementDetails;

	@OneToMany(mappedBy = "institutionData")
	private List<StaffDetails> staffDetails;

	@ManyToOne
	@JoinColumn(name = "q51")
	private Options q51;

	@ManyToOne
	@JoinColumn(name = "q52")
	private Options q52;

	@OneToMany(mappedBy = "institutionData")
	private List<PoliceCaseReports> policeCaseReports;

	@ManyToOne
	@JoinColumn(name = "q59")
	private Options q59;

	private String q60;

	@Temporal(TemporalType.DATE)
	private Date q61;

	@ManyToOne
	@JoinColumn(name = "q62")
	private Options q62;

	@OneToMany(mappedBy = "institutionData")
	private List<SocialAuditReports> socialAuditReports;

	public List<SocialAuditReports> getSocialAuditReports() {
		return socialAuditReports;
	}

	public void setSocialAuditReports(List<SocialAuditReports> socialAuditReports) {
		this.socialAuditReports = socialAuditReports;
	}

	@OneToMany(mappedBy = "institutionData")
	private List<GrantInAidDetails> grantAidDetails;

	private String q81;

	private String q82;

	private String q83;

	private String q84;

	@ManyToOne
	@JoinColumn(name = "q85")
	private Options q85;

	private String q86;

	@ManyToOne
	@JoinColumn(name = "q87")
	private Options q87;

	@ManyToOne
	@JoinColumn(name = "q88")
	private Options q88;

	@ManyToOne
	@JoinColumn(name = "q89")
	private Options q89;

	@ManyToOne
	@JoinColumn(name = "q200")
	private Options q200;

	private String q201;

	private String q202;

	private String q203;

	@ManyToOne
	@JoinColumn(name = "q500")
	private Options q500;
	
	private String q80;

	private String remarks;

	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private Date createdDate;

	@CreatedBy
	@ManyToOne
	@JoinColumn(name = "created_account_fk")
	private Account createdBy;

	@UpdateTimestamp
	private Date updatedDate;

	@ManyToOne
	@JoinColumn(name = "updated_account_fk")
	private Account updatedBy;

	private String rejectedSections;

	private boolean isLive;

	@Enumerated(EnumType.STRING)
	private SubmissionStatus submissionStatus;

	private String geolocation;

	private boolean isDeleted = true;

	@OneToMany(mappedBy = "institutionData", cascade = CascadeType.ALL)
	private List<Attachment> attachments;

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getGeolocation() {
		return geolocation;
	}

	public void setGeolocation(String geolocation) {
		this.geolocation = geolocation;
	}

	public int getInstutionId() {
		return instutionId;
	}

	public void setInstutionId(int instutionId) {
		this.instutionId = instutionId;
	}

	public Options getQ1() {
		return q1;
	}

	public void setQ1(Options q1) {
		this.q1 = q1;
	}

	public Options getQ2() {
		return q2;
	}

	public void setQ2(Options q2) {
		this.q2 = q2;
	}

	public String getQ3() {
		return q3;
	}

	public void setQ3(String q3) {
		this.q3 = q3;
	}

	public String getQ4() {
		return q4;
	}

	public void setQ4(String q4) {
		this.q4 = q4;
	}

	public String getQ80() {
		return q80;
	}

	public void setQ80(String q80) {
		this.q80 = q80;
	}

	public Options getQ5() {
		return q5;
	}

	public void setQ5(Options q5) {
		this.q5 = q5;
	}

	public Options getQ6() {
		return q6;
	}

	public void setQ6(Options q6) {
		this.q6 = q6;
	}

	public Area getQ7() {
		return q7;
	}

	public void setQ7(Area q7) {
		this.q7 = q7;
	}

	public String getQ8() {
		return q8;
	}

	public void setQ8(String q8) {
		this.q8 = q8;
	}

	public String getQ9() {
		return q9;
	}

	public void setQ9(String q9) {
		this.q9 = q9;
	}

	public Area getQ10() {
		return q10;
	}

	public void setQ10(Area q10) {
		this.q10 = q10;
	}

	public Area getQ11() {
		return q11;
	}

	public void setQ11(Area q11) {
		this.q11 = q11;
	}

	public String getQ12() {
		return q12;
	}

	public void setQ12(String q12) {
		this.q12 = q12;
	}

	public String getQ13() {
		return q13;
	}

	public void setQ13(String q13) {
		this.q13 = q13;
	}

	public String getQ14() {
		return q14;
	}

	public void setQ14(String q14) {
		this.q14 = q14;
	}

	public String getQ15() {
		return q15;
	}

	public void setQ15(String q15) {
		this.q15 = q15;
	}

	public String getQ16() {
		return q16;
	}

	public void setQ16(String q16) {
		this.q16 = q16;
	}

	public String getQ17() {
		return q17;
	}

	public void setQ17(String q17) {
		this.q17 = q17;
	}

	public Date getQ18() {
		return q18;
	}

	public void setQ18(Date q18) {
		this.q18 = q18;
	}

	public Date getQ19() {
		return q19;
	}

	public void setQ19(Date q19) {
		this.q19 = q19;
	}

	public String getQ20() {
		return q20;
	}

	public void setQ20(String q20) {
		this.q20 = q20;
	}

	public String getQ21() {
		return q21;
	}

	public void setQ21(String q21) {
		this.q21 = q21;
	}

	public Date getQ22() {
		return q22;
	}

	public void setQ22(Date q22) {
		this.q22 = q22;
	}

	public Date getQ23() {
		return q23;
	}

	public void setQ23(Date q23) {
		this.q23 = q23;
	}

	public String getQ24() {
		return q24;
	}

	public void setQ24(String q24) {
		this.q24 = q24;
	}

	public String getQ25() {
		return q25;
	}

	public void setQ25(String q25) {
		this.q25 = q25;
	}

	public String getQ26() {
		return q26;
	}

	public void setQ26(String q26) {
		this.q26 = q26;
	}

	public Options getQ27() {
		return q27;
	}

	public void setQ27(Options q27) {
		this.q27 = q27;
	}

	public String getQ28() {
		return q28;
	}

	public void setQ28(String q28) {
		this.q28 = q28;
	}

	public String getQ29() {
		return q29;
	}

	public void setQ29(String q29) {
		this.q29 = q29;
	}

	public String getQ30() {
		return q30;
	}

	public void setQ30(String q30) {
		this.q30 = q30;
	}

	public String getQ31() {
		return q31;
	}

	public void setQ31(String q31) {
		this.q31 = q31;
	}

	public String getQ32() {
		return q32;
	}

	public void setQ32(String q32) {
		this.q32 = q32;
	}

	public void setManagementDetails(List<ManagementDetails> managementDetails) {
		this.managementDetails = managementDetails;
	}

	public void setStaffDetails(List<StaffDetails> staffDetails) {
		this.staffDetails = staffDetails;
	}

	public void setPoliceCaseReports(List<PoliceCaseReports> policeCaseReports) {
		this.policeCaseReports = policeCaseReports;
	}

	public void setGrantAidDetails(List<GrantInAidDetails> grantAidDetails) {
		this.grantAidDetails = grantAidDetails;
	}

	public Options getQ51() {
		return q51;
	}

	public void setQ51(Options q51) {
		this.q51 = q51;
	}

	public Options getQ52() {
		return q52;
	}

	public void setQ52(Options q52) {
		this.q52 = q52;
	}

	public Options getQ59() {
		return q59;
	}

	public void setQ59(Options q59) {
		this.q59 = q59;
	}

	public String getQ60() {
		return q60;
	}

	public void setQ60(String q60) {
		this.q60 = q60;
	}

	public Date getQ61() {
		return q61;
	}

	public void setQ61(Date q61) {
		this.q61 = q61;
	}

	public Options getQ62() {
		return q62;
	}

	public void setQ62(Options q62) {
		this.q62 = q62;
	}

	public List<ManagementDetails> getManagementDetails() {
		return managementDetails;
	}

	public List<StaffDetails> getStaffDetails() {
		return staffDetails;
	}

	public List<PoliceCaseReports> getPoliceCaseReports() {
		return policeCaseReports;
	}

	public List<GrantInAidDetails> getGrantAidDetails() {
		return grantAidDetails;
	}

	public String getQ81() {
		return q81;
	}

	public void setQ81(String q81) {
		this.q81 = q81;
	}

	public String getQ82() {
		return q82;
	}

	public void setQ82(String q82) {
		this.q82 = q82;
	}

	public String getQ83() {
		return q83;
	}

	public void setQ83(String q83) {
		this.q83 = q83;
	}

	public String getQ84() {
		return q84;
	}

	public void setQ84(String q84) {
		this.q84 = q84;
	}

	public Options getQ85() {
		return q85;
	}

	public void setQ85(Options q85) {
		this.q85 = q85;
	}

	public String getQ86() {
		return q86;
	}

	public void setQ86(String q86) {
		this.q86 = q86;
	}

	public Options getQ87() {
		return q87;
	}

	public Options getQ200() {
		return q200;
	}

	public void setQ200(Options q200) {
		this.q200 = q200;
	}

	public String getQ201() {
		return q201;
	}

	public void setQ201(String q201) {
		this.q201 = q201;
	}

	public String getQ202() {
		return q202;
	}

	public void setQ202(String q202) {
		this.q202 = q202;
	}

	public String getQ203() {
		return q203;
	}

	public void setQ203(String q203) {
		this.q203 = q203;
	}

	public void setQ87(Options q87) {
		this.q87 = q87;
	}

	public Options getQ88() {
		return q88;
	}

	public void setQ88(Options q88) {
		this.q88 = q88;
	}

	public Options getQ89() {
		return q89;
	}

	public void setQ89(Options q89) {
		this.q89 = q89;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Account getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Account createdBy) {
		this.createdBy = createdBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Account getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Account updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getRejectedSections() {
		return rejectedSections;
	}

	public void setRejectedSections(String rejectedSections) {
		this.rejectedSections = rejectedSections;
	}

	public boolean isLive() {
		return isLive;
	}

	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}

	public SubmissionStatus getSubmissionStatus() {
		return submissionStatus;
	}

	public void setSubmissionStatus(SubmissionStatus submissionStatus) {
		this.submissionStatus = submissionStatus;
	}

	public List<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}

	public Options getQ500() {
		return q500;
	}

	public void setQ500(Options q500) {
		this.q500 = q500;
	}

	
}
