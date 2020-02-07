/**
 * 
 */
package org.sdrc.scps.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.sdrc.scps.util.IndicatorTable;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */

@Entity
@Table(name="mst_summary_report_indicator")
public class SummaryReportIndicator {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int summaryReportIndicatorId;
    
    
    private String indicatorName;
    
    
    private int indicatorOrder;
    
    
    @Enumerated(EnumType.STRING)
    private IndicatorTable indicatorTable;
    
    private int queryIndex;
    
    
    private boolean isLive;
    
    
    private String coreIndicator;


	/**
	 * @return the summaryReportIndicatorId
	 */
	public int getSummaryReportIndicatorId() {
		return summaryReportIndicatorId;
	}


	/**
	 * @return the indicatorName
	 */
	public String getIndicatorName() {
		return indicatorName;
	}


	/**
	 * @return the indicatorOrder
	 */
	public int getIndicatorOrder() {
		return indicatorOrder;
	}


	/**
	 * @return the indicatorTable
	 */
	public IndicatorTable getIndicatorTable() {
		return indicatorTable;
	}


	/**
	 * @return the queryIndex
	 */
	public int getQueryIndex() {
		return queryIndex;
	}


	/**
	 * @return the isLive
	 */
	public boolean isLive() {
		return isLive;
	}


	/**
	 * @param summaryReportIndicatorId the summaryReportIndicatorId to set
	 */
	public void setSummaryReportIndicatorId(int summaryReportIndicatorId) {
		this.summaryReportIndicatorId = summaryReportIndicatorId;
	}


	/**
	 * @param indicatorName the indicatorName to set
	 */
	public void setIndicatorName(String indicatorName) {
		this.indicatorName = indicatorName;
	}


	/**
	 * @param indicatorOrder the indicatorOrder to set
	 */
	public void setIndicatorOrder(int indicatorOrder) {
		this.indicatorOrder = indicatorOrder;
	}


	/**
	 * @param indicatorTable the indicatorTable to set
	 */
	public void setIndicatorTable(IndicatorTable indicatorTable) {
		this.indicatorTable = indicatorTable;
	}


	/**
	 * @param queryIndex the queryIndex to set
	 */
	public void setQueryIndex(int queryIndex) {
		this.queryIndex = queryIndex;
	}


	/**
	 * @param isLive the isLive to set
	 */
	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}


	/**
	 * @return the coreIndicator
	 */
	public String getCoreIndicator() {
		return coreIndicator;
	}


	/**
	 * @param coreIndicator the coreIndicator to set
	 */
	public void setCoreIndicator(String coreIndicator) {
		this.coreIndicator = coreIndicator;
	}
    
    
    
    

}
