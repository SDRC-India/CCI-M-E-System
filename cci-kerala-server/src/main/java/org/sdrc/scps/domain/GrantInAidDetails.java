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
 * @author Harsh Pratyush(harsh@sdrc.co.in)
 *
 */

@Entity
@Table(name = "mst_gratntInAidDetails")
public class GrantInAidDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int grantInAidDetailsId;

	private String q72;

	private String q73;

	@ManyToOne
	@JoinColumn(name="q74")
	private Options q74;

	@ManyToOne
	@JoinColumn(name="q75")
	private Options q75;

	private String q76;
	
	
	@ManyToOne
	@JoinColumn(name="submission_id_fk")
	private InstitutionData institutionData;
	
	private boolean isLive;
	

	private String q77;

	private String q78;
	
	private int indexTrackNum;
	
	private String q79;	
	


	public String getQ79() {
		return q79;
	}

	public void setQ79(String q79) {
		this.q79 = q79;
	}

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

	public int getGrantInAidDetailsId() {
		return grantInAidDetailsId;
	}

	public void setGrantInAidDetailsId(int grantInAidDetailsId) {
		this.grantInAidDetailsId = grantInAidDetailsId;
	}

	public String getQ72() {
		return q72;
	}

	public void setQ72(String q72) {
		this.q72 = q72;
	}

	public String getQ73() {
		return q73;
	}

	public void setQ73(String q73) {
		this.q73 = q73;
	}

	public Options getQ74() {
		return q74;
	}

	public void setQ74(Options q74) {
		this.q74 = q74;
	}

	public Options getQ75() {
		return q75;
	}

	public void setQ75(Options q75) {
		this.q75 = q75;
	}

	public String getQ76() {
		return q76;
	}

	public void setQ76(String q76) {
		this.q76 = q76;
	}

	public String getQ77() {
		return q77;
	}

	public void setQ77(String q77) {
		this.q77 = q77;
	}

	public String getQ78() {
		return q78;
	}

	public void setQ78(String q78) {
		this.q78 = q78;
	}

	public boolean isLive() {
		return isLive;
	}

	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}

}
