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

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */

@Entity
public class BuildingDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int buildingDetailsId;

	@ManyToOne
	@JoinColumn(name = "submission_id_fk")
	private InstitutionData institutionData;

	private boolean isLive;

	private String q33;

	private String q34;

	private String q35;

	private String q36;

	private String q33BuildingName;

	private int indexTrackNum;

	@Column( columnDefinition = "boolean DEFAULT true")
	private boolean removable = true;

	/**
	 * @return the buildingDetailsId
	 */
	public int getBuildingDetailsId() {
		return buildingDetailsId;
	}

	/**
	 * @return the institutionData
	 */
	public InstitutionData getInstitutionData() {
		return institutionData;
	}

	/**
	 * @return the isLive
	 */
	public boolean isLive() {
		return isLive;
	}

	/**
	 * @return the q33
	 */
	public String getQ33() {
		return q33;
	}

	/**
	 * @return the q34
	 */
	public String getQ34() {
		return q34;
	}

	/**
	 * @return the q35
	 */
	public String getQ35() {
		return q35;
	}

	/**
	 * @return the q36
	 */
	public String getQ36() {
		return q36;
	}

	/**
	 * @return the q33BuildingName
	 */
	public String getQ33BuildingName() {
		return q33BuildingName;
	}

	/**
	 * @return the indexTrackNum
	 */
	public int getIndexTrackNum() {
		return indexTrackNum;
	}

	/**
	 * @param buildingDetailsId the buildingDetailsId to set
	 */
	public void setBuildingDetailsId(int buildingDetailsId) {
		this.buildingDetailsId = buildingDetailsId;
	}

	/**
	 * @param institutionData the institutionData to set
	 */
	public void setInstitutionData(InstitutionData institutionData) {
		this.institutionData = institutionData;
	}

	/**
	 * @param isLive the isLive to set
	 */
	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}

	/**
	 * @param q33 the q33 to set
	 */
	public void setQ33(String q33) {
		this.q33 = q33;
	}

	/**
	 * @param q34 the q34 to set
	 */
	public void setQ34(String q34) {
		this.q34 = q34;
	}

	/**
	 * @param q35 the q35 to set
	 */
	public void setQ35(String q35) {
		this.q35 = q35;
	}

	/**
	 * @param q36 the q36 to set
	 */
	public void setQ36(String q36) {
		this.q36 = q36;
	}

	/**
	 * @param q33BuildingName the q33BuildingName to set
	 */
	public void setQ33BuildingName(String q33BuildingName) {
		this.q33BuildingName = q33BuildingName;
	}

	/**
	 * @param indexTrackNum the indexTrackNum to set
	 */
	public void setIndexTrackNum(int indexTrackNum) {
		this.indexTrackNum = indexTrackNum;
	}

	/**
	 * @return the removable
	 */
	public boolean isRemovable() {
		return removable;
	}

	/**
	 * @param removable the removable to set
	 */
	public void setRemovable(boolean removable) {
		this.removable = removable;
	}

}
