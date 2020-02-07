/**
 * 
 */
package org.sdrc.scps.domain;

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
@Table(name="police_case_reports")
public class PoliceCaseReports {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int policeCaseReportIds;
	
	private String q53;
	private String q54;
	private String q55;	
	private String q56;	
	private String q57;	
	private String q58;	

	
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


	public boolean isLive() {
		return isLive;
	}


	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}


	public InstitutionData getInstitutionData() {
		return institutionData;
	}


	public void setInstitutionData(InstitutionData institutionData) {
		this.institutionData = institutionData;
	}


	public int getPoliceCaseReportIds() {
		return policeCaseReportIds;
	}


	public void setPoliceCaseReportIds(int policeCaseReportIds) {
		this.policeCaseReportIds = policeCaseReportIds;
	}


	public String getQ53() {
		return q53;
	}


	public void setQ53(String q53) {
		this.q53 = q53;
	}


	public String getQ54() {
		return q54;
	}


	public void setQ54(String q54) {
		this.q54 = q54;
	}


	public String getQ55() {
		return q55;
	}


	public void setQ55(String q55) {
		this.q55 = q55;
	}


	public String getQ56() {
		return q56;
	}


	public void setQ56(String q56) {
		this.q56 = q56;
	}


	public String getQ57() {
		return q57;
	}


	public void setQ57(String q57) {
		this.q57 = q57;
	}


	public String getQ58() {
		return q58;
	}


	public void setQ58(String q58) {
		this.q58 = q58;
	}

}
