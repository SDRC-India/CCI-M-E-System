/**
 * 
 */
package org.sdrc.scps.models;

/**
 * @author Harsh Pratyush
 *
 */
public class AreaModel {

	private int areaId;

	private String areaCode;

	private String areaName;

	private int parentAreaId;

	private boolean isLive;
	
	private int areaLevel;

	/**
	 * @return the areaId
	 */
	public int getAreaId() {
		return areaId;
	}

	/**
	 * @return the areaCode
	 */
	public String getAreaCode() {
		return areaCode;
	}

	/**
	 * @return the areaName
	 */
	public String getAreaName() {
		return areaName;
	}

	/**
	 * @return the parentAreaId
	 */
	public int getParentAreaId() {
		return parentAreaId;
	}

	/**
	 * @return the isLive
	 */
	public boolean isLive() {
		return isLive;
	}

	/**
	 * @return the areaLevel
	 */
	public int getAreaLevel() {
		return areaLevel;
	}

	/**
	 * @param areaId the areaId to set
	 */
	public void setAreaId(int areaId) {
		this.areaId = areaId;
	}

	/**
	 * @param areaCode the areaCode to set
	 */
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	/**
	 * @param areaName the areaName to set
	 */
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	/**
	 * @param parentAreaId the parentAreaId to set
	 */
	public void setParentAreaId(int parentAreaId) {
		this.parentAreaId = parentAreaId;
	}

	/**
	 * @param isLive the isLive to set
	 */
	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}

	/**
	 * @param areaLevel the areaLevel to set
	 */
	public void setAreaLevel(int areaLevel) {
		this.areaLevel = areaLevel;
	}
}
