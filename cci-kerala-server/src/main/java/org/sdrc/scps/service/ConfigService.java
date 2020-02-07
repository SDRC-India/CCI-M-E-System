/**
 * 
 */
package org.sdrc.scps.service;

/**
 * @author Harsh Pratyush(harsh@sdrc.co.in)
 *
 */
public interface ConfigService {
	
	/**
	 * This method will read the area template from the master area list and update the master areaList table 
	 * @return
	 */
	public boolean updateArea();
	
	
	/**
	 * This method will configure the question template for dyanamic form
	 * @return
	 */
	public boolean configureQuestionTemplate();
	
	
	/**
	 *  This method will configure CCI
	 * @return
	 */
	public boolean configCCI();
	
	
	/**
	 * This method will configure the dummy user
	 * @return
	 */
	public boolean configUsersDummy();

}
