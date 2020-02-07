/**
 * 
 */
package org.sdrc.scps.service;

import java.io.File;
import java.util.List;

import org.sdrc.scps.domain.Area;
import org.sdrc.scps.models.SummaryReportModel;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */

public interface ReportService {
	/**
	 * @param districtId 
	 * @return
	 */
	public File generateInmatesRawData(OAuth2Authentication auth, int districtId);

	/**
	 * @return
	 */
	public List<Area> getDistrict(OAuth2Authentication auth);
	
	
	
	/**
	 * 
	 * @param auth
	 * @return
	 */
	public SummaryReportModel getSummaryReportTable(OAuth2Authentication auth, int areaId,int areaLevelId);
}
