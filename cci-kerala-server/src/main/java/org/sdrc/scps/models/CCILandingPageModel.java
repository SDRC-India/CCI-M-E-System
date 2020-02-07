/**
 * 
 */
package org.sdrc.scps.models;

import java.util.Map;

/**
 * @author Harsh Pratyush(harsh@sdrc.co.in)
 *
 */
public class CCILandingPageModel {

	private Map<String,String> cciInformation;
	
	private Map<String,Object> inmatesDetailsMap;

	public Map<String, String> getCciInformation() {
		return cciInformation;
	}

	public void setCciInformation(Map<String, String> cciInformation) {
		this.cciInformation = cciInformation;
	}

	public Map<String, Object> getInmatesDetailsMap() {
		return inmatesDetailsMap;
	}

	public void setInmatesDetailsMap(Map<String,Object> inmatesDetailsMap) {
		this.inmatesDetailsMap = inmatesDetailsMap;
	}
	
	
}
