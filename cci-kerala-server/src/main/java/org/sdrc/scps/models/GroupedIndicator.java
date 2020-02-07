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
public class GroupedIndicator {
	
	private String groupedIndName;
	
	private String indicatorName;
	
	private Set<String> chartsAvailable;
	
	private List<List<ChartData>> chartData;
	
	private String cssClass;
	
	private Map<String,String> legendClass;

	/**
	 * @return the groupedIndName
	 */
	public String getGroupedIndName() {
		return groupedIndName;
	}

	/**
	 * @return the indicatorName
	 */
	public String getIndicatorName() {
		return indicatorName;
	}

	/**
	 * @return the chartsAvailable
	 */
	public Set<String> getChartsAvailable() {
		return chartsAvailable;
	}

	/**
	 * @return the chartData
	 */
	public List<List<ChartData>> getChartData() {
		return chartData;
	}

	/**
	 * @param groupedIndName the groupedIndName to set
	 */
	public void setGroupedIndName(String groupedIndName) {
		this.groupedIndName = groupedIndName;
	}

	/**
	 * @param indicatorName the indicatorName to set
	 */
	public void setIndicatorName(String indicatorName) {
		this.indicatorName = indicatorName;
	}

	/**
	 * @param chartsAvailable the chartsAvailable to set
	 */
	public void setChartsAvailable(Set<String> chartsAvailable) {
		this.chartsAvailable = chartsAvailable;
	}

	/**
	 * @param chartData the chartData to set
	 */
	public void setChartData(List<List<ChartData>> chartData) {
		this.chartData = chartData;
	}

	/**
	 * @return the cssClass
	 */
	public String getCssClass() {
		return cssClass;
	}

	/**
	 * @param cssClass the cssClass to set
	 */
	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	/**
	 * @return the legendClass
	 */
	public Map<String, String> getLegendClass() {
		return legendClass;
	}

	/**
	 * @param legendClass the legendClass to set
	 */
	public void setLegendClass(Map<String, String> legendClass) {
		this.legendClass = legendClass;
	}
	
	
	

}
