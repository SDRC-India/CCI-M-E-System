/**
 * 
 */
package org.sdrc.scps.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author SDRC_DEV
 *
 */

@Entity
@Table
public class SocialAuditReports {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int socialAuditReportsId;
	
	private String q63;
	
	private String q64;
	
	
	private String q66;
	
	@ManyToOne
	@JoinColumn(name="q67")
	private Options q67;
	
	@Temporal(TemporalType.DATE)
	private Date q68;
	
	private String q69;
	
	@ManyToOne
	@JoinColumn(name="q70")
	private Options q70;
	
	@Temporal(TemporalType.DATE)
	private Date q71;
	
	@ManyToOne
	@JoinColumn(name = "submission_id_fk")
	private InstitutionData institutionData;
	
	
	private int indexTrackNum;
	
	private boolean isLive;

	public int getSocialAuditReportsId() {
		return socialAuditReportsId;
	}

	public void setSocialAuditReportsId(int socialAuditReportsId) {
		this.socialAuditReportsId = socialAuditReportsId;
	}

	public String getQ63() {
		return q63;
	}

	public void setQ63(String q63) {
		this.q63 = q63;
	}

	public String getQ64() {
		return q64;
	}

	public void setQ64(String q64) {
		this.q64 = q64;
	}

	public InstitutionData getInstitutionData() {
		return institutionData;
	}

	public void setInstitutionData(InstitutionData institutionData) {
		this.institutionData = institutionData;
	}

	public int getIndexTrackNum() {
		return indexTrackNum;
	}

	public void setIndexTrackNum(int indexTrackNum) {
		this.indexTrackNum = indexTrackNum;
	}

	public boolean isLive() {
		return isLive;
	}

	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}

	public String getQ66() {
		return q66;
	}

	public void setQ66(String q66) {
		this.q66 = q66;
	}

	public Options getQ67() {
		return q67;
	}

	public void setQ67(Options q67) {
		this.q67 = q67;
	}

	public Date getQ68() {
		return q68;
	}

	public void setQ68(Date q68) {
		this.q68 = q68;
	}

	public String getQ69() {
		return q69;
	}

	public void setQ69(String q69) {
		this.q69 = q69;
	}

	public Options getQ70() {
		return q70;
	}

	public void setQ70(Options q70) {
		this.q70 = q70;
	}

	public Date getQ71() {
		return q71;
	}

	public void setQ71(Date q71) {
		this.q71 = q71;
	}

	

}
