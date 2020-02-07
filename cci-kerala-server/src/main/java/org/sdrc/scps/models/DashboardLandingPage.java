/**
 * 
 */
package org.sdrc.scps.models;

import java.util.List;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */
public class DashboardLandingPage {

	
	private List<QuickStats> quickStarts;
	
	private List<GroupedIndicator> groupedIndicators;



	public List<QuickStats> getQuickStarts() {
		return quickStarts;
	}

	public void setQuickStarts(List<QuickStats> quickStarts) {
		this.quickStarts = quickStarts;
	}

	/**
	 * @return the groupedIndicators
	 */
	public List<GroupedIndicator> getGroupedIndicators() {
		return groupedIndicators;
	}

	/**
	 * @param groupedIndicators the groupedIndicators to set
	 */
	public void setGroupedIndicators(List<GroupedIndicator> groupedIndicators) {
		this.groupedIndicators = groupedIndicators;
	}


	
	
	
	
}
