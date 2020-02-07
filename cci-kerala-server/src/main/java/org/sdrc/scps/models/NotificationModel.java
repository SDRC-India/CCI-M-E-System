/**
 * 
 */
package org.sdrc.scps.models;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */
public class NotificationModel {

	private int institutionNo;
	private int inmatesNo;
	private int exceedingChilds;
	private int total=institutionNo+inmatesNo+exceedingChilds;


	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	
	public int getInstitutionNo() {
		return institutionNo;
	}
	public void setInstitutionNo(int institutionNo) {
		this.institutionNo = institutionNo;
		this.total=this.institutionNo+this.inmatesNo+this.exceedingChilds;
	}
	public int getInmatesNo() {
		return inmatesNo;
	}
	public void setInmatesNo(int inmatesNo) {
		this.inmatesNo = inmatesNo;
		this.total=this.institutionNo+this.inmatesNo+this.exceedingChilds;
	}
	/**
	 * @return the exceedingChilds
	 */
	public int getExceedingChilds() {
		return exceedingChilds;
	}
	/**
	 * @param exceedingChilds the exceedingChilds to set
	 */
	public void setExceedingChilds(int exceedingChilds) {
		this.exceedingChilds = exceedingChilds;
		this.total=this.institutionNo+this.inmatesNo+this.exceedingChilds;
	}
	
	
	
}
