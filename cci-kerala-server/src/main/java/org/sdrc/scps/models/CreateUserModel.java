/**
 * 
 */
package org.sdrc.scps.models;

/**
 * @author Harsh Pratyush
 *
 */
public class CreateUserModel {

	private String userName; // set user name of user form group

	private String email; // set email

//	private String phoneNo; // set phone no

	private int areaId; // set selected state of user form group

	private String cciName; // set cci name of cci form group

	private int districtId;// set district id of cci form group

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @return the phoneNo
	 */
//	public String getPhoneNo() {
//		return phoneNo;
//	}

	/**
	 * @return the areaId
	 */
	public int getAreaId() {
		return areaId;
	}

	/**
	 * @return the cciName
	 */
	public String getCciName() {
		return cciName;
	}

	/**
	 * @return the districtId
	 */
	public int getDistrictId() {
		return districtId;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @param phoneNo the phoneNo to set
	 */
//	public void setPhoneNo(String phoneNo) {
//		this.phoneNo = phoneNo;
//	}

	/**
	 * @param areaId the areaId to set
	 */
	public void setAreaId(int areaId) {
		this.areaId = areaId;
	}

	/**
	 * @param cciName the cciName to set
	 */
	public void setCciName(String cciName) {
		this.cciName = cciName;
	}

	/**
	 * @param districtId the districtId to set
	 */
	public void setDistrictId(int districtId) {
		this.districtId = districtId;
	}

}
