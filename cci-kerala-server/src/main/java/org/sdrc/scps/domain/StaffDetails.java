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
@Table(name = "staff_details")
public class StaffDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int staffDetailsID;

	private String q43;

	@ManyToOne
	@JoinColumn(name="q44")
	private Options q44;

	private String q45;

	@Temporal(TemporalType.DATE)
	private Date q46;

	private String q47;

	private String q48;

	private String q49;

	private String q50;

	@ManyToOne
	@JoinColumn(name = "submission_id_fk")
	private InstitutionData institutionData;
	
	
	private int indexTrackNum;
	
	private boolean isLive;

	
	
	
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

	public int getStaffDetailsID() {
		return staffDetailsID;
	}

	public void setStaffDetailsID(int staffDetailsID) {
		this.staffDetailsID = staffDetailsID;
	}

	public String getQ43() {
		return q43;
	}

	public void setQ43(String q43) {
		this.q43 = q43;
	}

	public Options getQ44() {
		return q44;
	}

	public void setQ44(Options q44) {
		this.q44 = q44;
	}

	public String getQ45() {
		return q45;
	}

	public void setQ45(String q45) {
		this.q45 = q45;
	}

	public Date getQ46() {
		return q46;
	}

	public void setQ46(Date q46) {
		this.q46 = q46;
	}

	public String getQ47() {
		return q47;
	}

	public void setQ47(String q47) {
		this.q47 = q47;
	}

	public String getQ48() {
		return q48;
	}

	public void setQ48(String q48) {
		this.q48 = q48;
	}

	public String getQ49() {
		return q49;
	}

	public void setQ49(String q49) {
		this.q49 = q49;
	}

	public String getQ50() {
		return q50;
	}

	public void setQ50(String q50) {
		this.q50 = q50;
	}

	public InstitutionData getInstitutionData() {
		return institutionData;
	}

	public void setInstitutionData(InstitutionData institutionData) {
		this.institutionData = institutionData;
	}

}
