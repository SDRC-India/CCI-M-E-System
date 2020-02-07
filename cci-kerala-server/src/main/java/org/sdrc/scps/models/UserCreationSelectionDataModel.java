/**
 * 
 */
package org.sdrc.scps.models;

import java.util.List;

import org.sdrc.usermgmt.domain.Designation;

/**
 * @author Harsh Pratyush
 *
 */
public class UserCreationSelectionDataModel {
	
	private List<Designation> designations;
	
	private List<AreaModel> state;
	
	private List<AreaModel> district;
	

	/**
	 * @return the designations
	 */
	public List<Designation> getDesignations() {
		return designations;
	}

	/**
	 * @return the state
	 */
	public List<AreaModel> getState() {
		return state;
	}

	/**
	 * @return the district
	 */
	public List<AreaModel> getDistrict() {
		return district;
	}

	/**
	 * @param designations the designations to set
	 */
	public void setDesignations(List<Designation> designations) {
		this.designations = designations;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(List<AreaModel> state) {
		this.state = state;
	}

	/**
	 * @param district the district to set
	 */
	public void setDistrict(List<AreaModel> district) {
		this.district = district;
	}



}
