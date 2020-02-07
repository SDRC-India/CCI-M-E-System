/**
 * 
 */
package org.sdrc.scps.models;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Harsh Pratyush
 *
 */
public class SummaryReportModel {
	
	private List<String>  tableColumn;
	
	private List<Map<String,Object>> tableData;
	
	private Set<String> coreIndicator;
	
	private String reportGenerationDate;

	/**
	 * @return the tableColumn
	 */
	public List<String> getTableColumn() {
		return tableColumn;
	}

	/**
	 * @return the reportGenerationDate
	 */
	public String getReportGenerationDate() {
		return reportGenerationDate;
	}

	/**
	 * @param reportGenerationDate the reportGenerationDate to set
	 */
	public void setReportGenerationDate(String reportGenerationDate) {
		this.reportGenerationDate = reportGenerationDate;
	}

	/**
	 * @return the tableData
	 */
	public List<Map<String, Object>> getTableData() {
		return tableData;
	}

	/**
	 * @return the coreIndicator
	 */
	public Set<String> getCoreIndicator() {
		return coreIndicator;
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
	 * @param coreIndicator the coreIndicator to set
	 */
	public void setCoreIndicator(Set<String> coreIndicator) {
		this.coreIndicator = coreIndicator;
	}
	
	
	

}
