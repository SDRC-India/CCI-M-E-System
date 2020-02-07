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

import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * @author Harsh
 *
 */

@Entity
@Table(name = "mst_inmates_data")
public class InmatesData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int inmatesData;;

	private String q1;

	@OneToMany(mappedBy="inmatesData")
	private  List<InmatesPhoto> inmatesPhoto;

	@ManyToOne
	@JoinColumn(name = "q3")
	private Options q3;

	private String q4;

	@ManyToOne
	@JoinColumn(name = "q5")
	private Options q5;

	@Temporal(TemporalType.DATE)
	private Date q6;

	private String q7;

	@ManyToOne
	@JoinColumn(name = "q8")
	private Options q8;

	private String q9;

	private String q11;

	private String q12;

	private String q13;

	private String q14;

	@ManyToOne
	@JoinColumn(name = "q15")
	private Options q15;

	@ManyToOne
	@JoinColumn(name = "q16")
	private Options q16;

	private String q17;

	private String q18;

	private String q19;
	
	@Temporal(TemporalType.DATE)
	private Date qDateAdmission;

	private String q20;
	
	@Temporal(TemporalType.DATE)
	private Date q20Date;

	private String q21;
	
	/**
	 * We will keep the index track num of the building details 
	 */
	private String qBuildingDetails;

	@ManyToOne
	@JoinColumn(name = "q22")
	private Options q22;

	private String q23;

	@ManyToOne
	@JoinColumn(name = "q24")
	private Options q24;

	@ManyToOne
	@JoinColumn(name = "q25")
	private Options q25;

	@ManyToOne
	@JoinColumn(name = "q26")
	private Options q26;

	private String q27;

	@ManyToOne
	@JoinColumn(name = "q28")
	private Options q28;

	@ManyToOne
	@JoinColumn(name = "q29")
	private Options q29;

	private String q30;

	@ManyToOne
	@JoinColumn(name = "q31")
	private Options q31;

	private String q32;

	@ManyToOne
	@JoinColumn(name = "q33")
	private Options q33;

	@ManyToOne
	@JoinColumn(name = "q34")
	private Options q34;

	private String q35;

	private String q36;

	private String q37;

	private String q38;

	private String q39;

	@ManyToOne
	@JoinColumn(name = "q40")
	private Options q40;

	private String q41;

	@ManyToOne
	@JoinColumn(name = "q42")
	private Options q42;

	@ManyToOne
	@JoinColumn(name = "q43")
	private Options q43;

	private String q44;

	private String q45;

	private String q46;

	private String q47;

	@Temporal(TemporalType.DATE)
	private Date q48;

	private String q49;

	@ManyToOne
	@JoinColumn(name = "q50")
	private Options q50;

	private String q51;

	@Temporal(TemporalType.DATE)
	private Date q52;

	private String q53;

	@ManyToOne
	@JoinColumn(name = "q54")
	private Area q54;

	@ManyToOne
	@JoinColumn(name = "q55")
	private Area q55;

	@ManyToOne
	@JoinColumn(name = "q56")
	private Area q56;

	private String q56Other;

	private String q57;

	@Temporal(TemporalType.DATE)
	private Date q58;

	@ManyToOne
	@JoinColumn(name = "q59")
	private Options q59;

	@ManyToOne
	@JoinColumn(name = "q60")
	private Options q60;

	private String q61;

	@ManyToOne
	@JoinColumn(name = "q62")
	private Options q62;

	@ManyToOne
	@JoinColumn(name = "q63")
	private Options q63;

	@ManyToOne
	@JoinColumn(name = "q64")
	private Options q64;

	private String q65;

	@Temporal(TemporalType.DATE)
	private Date q66;

	@ManyToOne
	@JoinColumn(name = "q67")
	private Options q67;

	@ManyToOne
	@JoinColumn(name = "q68")
	private Options q68;

	@ManyToOne
	@JoinColumn(name = "q69")
	private Options q69;

	private String q70;

	@ManyToOne
	@JoinColumn(name = "q71")
	private Options q71;

	private String q72;

	private String remarks;

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

	private boolean isDeleted = false;

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	@Enumerated(EnumType.STRING)
	private SubmissionStatus submissionStatus;

	@ManyToOne
	@JoinColumn(name = "owner_account_fk")
	private User owner;

//	private boolean isDeleted;

//	public boolean isDeleted() {
//		return isDeleted;
//	}
//
//	public void setDeleted(boolean isDeleted) {
//		this.isDeleted = isDeleted;
//	}

	@OneToMany(mappedBy = "inmateData", cascade = CascadeType.ALL)
	private List<Attachment> attachment;

	public int getInmatesData() {
		return inmatesData;
	}

	public void setInmatesData(int inmatesData) {
		this.inmatesData = inmatesData;
	}

	public String getQ1() {
		return q1;
	}

	public void setQ1(String q1) {
		this.q1 = q1;
	}

//	public String getQ2() {
//		return q2;
//	}
//
//	public void setQ2(String q2) {
//		this.q2 = q2;
//	}

	public Options getQ3() {
		return q3;
	}

	public void setQ3(Options q3) {
		this.q3 = q3;
	}

	public String getQ4() {
		return q4;
	}

	public void setQ4(String q4) {
		this.q4 = q4;
	}

	public Options getQ5() {
		return q5;
	}

	public void setQ5(Options q5) {
		this.q5 = q5;
	}

	public Date getQ6() {
		return q6;
	}

	public void setQ6(Date q6) {
		this.q6 = q6;
	}

	public String getQ7() {
		return q7;
	}

	public void setQ7(String q7) {
		this.q7 = q7;
	}

	public Options getQ8() {
		return q8;
	}

	public void setQ8(Options q8) {
		this.q8 = q8;
	}

	public String getQ9() {
		return q9;
	}

	public void setQ9(String q9) {
		this.q9 = q9;
	}

	public String getQ11() {
		return q11;
	}

	public void setQ11(String q11) {
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

	public Options getQ15() {
		return q15;
	}

	public void setQ15(Options q15) {
		this.q15 = q15;
	}

	public Options getQ16() {
		return q16;
	}

	public void setQ16(Options q16) {
		this.q16 = q16;
	}

	public String getQ17() {
		return q17;
	}

	public void setQ17(String q17) {
		this.q17 = q17;
	}

	public String getQ18() {
		return q18;
	}

	public void setQ18(String q18) {
		this.q18 = q18;
	}

	public String getQ19() {
		return q19;
	}

	public void setQ19(String q19) {
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

	public Options getQ22() {
		return q22;
	}

	public void setQ22(Options q22) {
		this.q22 = q22;
	}

	public String getQ23() {
		return q23;
	}

	public void setQ23(String q23) {
		this.q23 = q23;
	}

	public Options getQ24() {
		return q24;
	}

	public void setQ24(Options q24) {
		this.q24 = q24;
	}

	public Options getQ25() {
		return q25;
	}

	public void setQ25(Options q25) {
		this.q25 = q25;
	}

	public Options getQ26() {
		return q26;
	}

	public void setQ26(Options q26) {
		this.q26 = q26;
	}

	public String getQ27() {
		return q27;
	}

	public void setQ27(String q27) {
		this.q27 = q27;
	}

	public Options getQ28() {
		return q28;
	}

	public void setQ28(Options q28) {
		this.q28 = q28;
	}

	public Options getQ29() {
		return q29;
	}

	public void setQ29(Options q29) {
		this.q29 = q29;
	}

	public String getQ30() {
		return q30;
	}

	public void setQ30(String q30) {
		this.q30 = q30;
	}

	public Options getQ31() {
		return q31;
	}

	public void setQ31(Options q31) {
		this.q31 = q31;
	}

	public String getQ32() {
		return q32;
	}

	public void setQ32(String q32) {
		this.q32 = q32;
	}

	public Options getQ33() {
		return q33;
	}

	public void setQ33(Options q33) {
		this.q33 = q33;
	}

	public Options getQ34() {
		return q34;
	}

	public void setQ34(Options q34) {
		this.q34 = q34;
	}

	public String getQ35() {
		return q35;
	}

	public void setQ35(String q35) {
		this.q35 = q35;
	}

	public String getQ36() {
		return q36;
	}

	public void setQ36(String q36) {
		this.q36 = q36;
	}

	public String getQ37() {
		return q37;
	}

	public void setQ37(String q37) {
		this.q37 = q37;
	}

	public String getQ38() {
		return q38;
	}

	public void setQ38(String q38) {
		this.q38 = q38;
	}

	public String getQ39() {
		return q39;
	}

	public void setQ39(String q39) {
		this.q39 = q39;
	}

	public Options getQ40() {
		return q40;
	}

	public void setQ40(Options q40) {
		this.q40 = q40;
	}

	public String getQ41() {
		return q41;
	}

	public void setQ41(String q41) {
		this.q41 = q41;
	}

	public Options getQ42() {
		return q42;
	}

	public void setQ42(Options q42) {
		this.q42 = q42;
	}

	public Options getQ43() {
		return q43;
	}

	public void setQ43(Options q43) {
		this.q43 = q43;
	}

	public String getQ44() {
		return q44;
	}

	public void setQ44(String q44) {
		this.q44 = q44;
	}

	public String getQ45() {
		return q45;
	}

	public void setQ45(String q45) {
		this.q45 = q45;
	}

	public String getQ46() {
		return q46;
	}

	public void setQ46(String q46) {
		this.q46 = q46;
	}

	public String getQ47() {
		return q47;
	}

	public void setQ47(String q47) {
		this.q47 = q47;
	}

	public Date getQ48() {
		return q48;
	}

	public void setQ48(Date q48) {
		this.q48 = q48;
	}

	public String getQ49() {
		return q49;
	}

	public void setQ49(String q49) {
		this.q49 = q49;
	}

	public Options getQ50() {
		return q50;
	}

	public void setQ50(Options q50) {
		this.q50 = q50;
	}

	public String getQ51() {
		return q51;
	}

	public void setQ51(String q51) {
		this.q51 = q51;
	}

	public Date getQ52() {
		return q52;
	}

	public void setQ52(Date q52) {
		this.q52 = q52;
	}

	public String getQ53() {
		return q53;
	}

	public void setQ53(String q53) {
		this.q53 = q53;
	}

	public Area getQ54() {
		return q54;
	}

	public void setQ54(Area q54) {
		this.q54 = q54;
	}

	public Area getQ55() {
		return q55;
	}

	public void setQ55(Area q55) {
		this.q55 = q55;
	}

	public Area getQ56() {
		return q56;
	}

	public void setQ56(Area q56) {
		this.q56 = q56;
	}

	public String getQ57() {
		return q57;
	}

	public void setQ57(String q57) {
		this.q57 = q57;
	}

	public Date getQ58() {
		return q58;
	}

	public void setQ58(Date q58) {
		this.q58 = q58;
	}

	public Options getQ59() {
		return q59;
	}

	public void setQ59(Options q59) {
		this.q59 = q59;
	}

	public Options getQ60() {
		return q60;
	}

	public void setQ60(Options q60) {
		this.q60 = q60;
	}

	public String getQ61() {
		return q61;
	}

	public void setQ61(String q61) {
		this.q61 = q61;
	}

	public Options getQ62() {
		return q62;
	}

	public void setQ62(Options q62) {
		this.q62 = q62;
	}

	public Options getQ63() {
		return q63;
	}

	public void setQ63(Options q63) {
		this.q63 = q63;
	}

	public Options getQ64() {
		return q64;
	}

	public void setQ64(Options q64) {
		this.q64 = q64;
	}

	public String getQ65() {
		return q65;
	}

	public void setQ65(String q65) {
		this.q65 = q65;
	}

	public Date getQ66() {
		return q66;
	}

	public void setQ66(Date q66) {
		this.q66 = q66;
	}

	public Options getQ67() {
		return q67;
	}

	public void setQ67(Options q67) {
		this.q67 = q67;
	}

	public Options getQ68() {
		return q68;
	}

	public void setQ68(Options q68) {
		this.q68 = q68;
	}

	public Options getQ69() {
		return q69;
	}

	public void setQ69(Options q69) {
		this.q69 = q69;
	}

	public String getQ70() {
		return q70;
	}

	public void setQ70(String q70) {
		this.q70 = q70;
	}

	public Options getQ71() {
		return q71;
	}

	public void setQ71(Options q71) {
		this.q71 = q71;
	}

	public String getQ72() {
		return q72;
	}

	public void setQ72(String q72) {
		this.q72 = q72;
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

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public List<Attachment> getAttachment() {
		return attachment;
	}

	public void setAttachment(List<Attachment> attachment) {
		this.attachment = attachment;
	}

	public String getQ56Other() {
		return q56Other;
	}

	public void setQ56Other(String q56Other) {
		this.q56Other = q56Other;
	}

	/**
	 * @return the inmatesPhoto
	 */
	public List<InmatesPhoto> getInmatesPhoto() {
		return inmatesPhoto;
	}

	/**
	 * @param inmatesPhoto the inmatesPhoto to set
	 */
	public void setInmatesPhoto(List<InmatesPhoto> inmatesPhoto) {
		this.inmatesPhoto = inmatesPhoto;
	}

	/**
	 * @return the qDateAdmission
	 */
	public Date getQDateAdmission() {
		return qDateAdmission;
	}

	/**
	 * @return the q20Date
	 */
	public Date getQ20Date() {
		return q20Date;
	}

	/**
	 * @param qDateAdmission the qDateAdmission to set
	 */
	public void setqDateAdmission(Date qDateAdmission) {
		this.qDateAdmission = qDateAdmission;
	}

	/**
	 * @param q20Date the q20Date to set
	 */
	public void setQ20Date(Date q20Date) {
		this.q20Date = q20Date;
	}

	/**
	 * @return the qDateAdmission
	 */
	public Date getqDateAdmission() {
		return qDateAdmission;
	}

	/**
	 * @return the qBuildingDetails
	 */
	public String getQBuildingDetails() {
		return qBuildingDetails;
	}

	/**
	 * @param qBuildingDetails the qBuildingDetails to set
	 */
	@JsonSetter(value="qBuildingDetails")
	public void setQBuildingDetails(String qBuildingDetails) {
		this.qBuildingDetails = qBuildingDetails;
	}



}
