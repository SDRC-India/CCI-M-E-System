/**
 * 
 */
package org.sdrc.scps.models;

import java.util.List;
import java.util.Map;

import org.sdrc.scps.domain.Area;

/**
 * @author Harsh Pratyush
 *
 */
public class DashboardLandingPageData {
	
	private Map<String,DashboardLandingPage> dashboardLandingPages;
	
	private List<String>  tableColumn;
	
	private List<Map<String,Object>> tableData;
	
	private List<Area> districts;

	/**
	 * @return the dashboardLandingPages
	 */
	public Map<String, DashboardLandingPage> getDashboardLandingPages() {
		return dashboardLandingPages;
	}

	/**
	 * @return the tableColumn
	 */
	public List<String> getTableColumn() {
		return tableColumn;
	}

	/**
	 * @return the tableData
	 */
	public List<Map<String, Object>> getTableData() {
		return tableData;
	}

	/**
	 * @return the districts
	 */
	public List<Area> getDistricts() {
		return districts;
	}

	/**
	 * @param dashboardLandingPages the dashboardLandingPages to set
	 */
	public void setDashboardLandingPages(Map<String, DashboardLandingPage> dashboardLandingPages) {
		this.dashboardLandingPages = dashboardLandingPages;
	}

	/**
	 * @param tableColumn the tableColumn to set
	 */
	public void setTableColumn(List<String> tableColumn) {
		this.tableColumn = tableColumn;
	}

	/**
	 * @param tableData the tableData to set
	 */
	public void setTableData(List<Map<String, Object>> tableData) {
		this.tableData = tableData;
	}

	/**
	 * @param districts the districts to set
	 */
	public void setDistricts(List<Area> districts) {
		this.districts = districts;
	} 
	
	
	

}
