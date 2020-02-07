/**
 * 
 */
package org.sdrc.scps.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sdrc.scps.models.DashboardLandingPageData;
import org.sdrc.scps.models.DataEntryQuestionModel;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

/**
 * @author Harsh Pratyush(harsh@sdrc.co.in)
 *
 */


public interface DashboardService {


	public DashboardLandingPageData getLandingPageData(OAuth2Authentication auth, int areaId,int areaLevelId);

	/**
	 * @param auth
	 * @param cciId
	 * @return
	 */
	public DashboardLandingPageData getDashboardInmateData(OAuth2Authentication auth, Integer cciId,String sqft);
	
	

	/**
	 * @param dataEntryModels
	 * @param auth
	 * @param response
	 * @param request
	 * @return
	 */
	public String generatePDF(List<DataEntryQuestionModel> dataEntryModels, OAuth2Authentication auth,
			HttpServletResponse response, HttpServletRequest request)  throws Exception;
	
}
