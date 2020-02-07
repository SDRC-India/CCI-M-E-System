/**
 * 
 */
package org.sdrc.scps.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */


@Entity
@Table(name="mst_management_details")
public class ManagementDetails {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int managementDetailsId;
	
	@Column(name="q37")
	private String q37;
	
	@ManyToOne
	@JoinColumn(name="q38")
	private Options q38;
	
	private String q39;
	
	private String q40;
	
	private String q41;
	
	private String q42;
	
	
	@ManyToOne
	@JoinColumn(name="submission_id_fk")
	private InstitutionData institutionData;
	
	private boolean isLive;
	
	private int indexTrackNum;
	
	

	 
	public int getIndexTrackNum() {
		return indexTrackNum;
	}

	public void setIndexTrackNum(int indexTrackNum) {
		this.indexTrackNum = indexTrackNum;
	}

	public InstitutionData getInstitutionData() {
		return institutionData;
	}

	public void setInstitutionData(InstitutionData institutionData) {
		this.institutionData = institutionData;
	}

	public int getManagementDetailsId() {
		return managementDetailsId;
	}

	public void setManagementDetailsId(int managementDetailsId) {
		this.managementDetailsId = managementDetailsId;
	}

	public String getQ37() {
		return q37;
	}

	public void setQ37(String q37) {
		this.q37 = q37;
	}

	public Options getQ38() {
		return q38;
	}

	public void setQ38(Options q38) {
		this.q38 = q38;
	}

	public String getQ39() {
		return q39;
	}

	public void setQ39(String q39) {
		this.q39 = q39;
	}

	public String getQ40() {
		return q40;
	}

	public void setQ40(String q40) {
		this.q40 = q40;
	}

	public String getQ41() {
		return q41;
	}

	public void setQ41(String q41) {
		this.q41 = q41;
	}

	public String getQ42() {
		return q42;
	}

	public void setQ42(String q42) {
		this.q42 = q42;
	}

	public boolean isLive() {
		return isLive;
	}

	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}
	
	
	


}
