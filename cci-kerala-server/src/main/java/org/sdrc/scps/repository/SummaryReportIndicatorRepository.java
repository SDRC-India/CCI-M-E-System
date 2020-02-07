/**
 * 
 */
package org.sdrc.scps.repository;

import java.util.List;

import org.sdrc.scps.domain.SummaryReportIndicator;
import org.springframework.data.repository.RepositoryDefinition;

/**
 * @author Harsh Pratyush
 *
 */

@RepositoryDefinition(domainClass=SummaryReportIndicator.class,idClass=Integer.class)
public interface SummaryReportIndicatorRepository {

	/**
	 * @return
	 */
	List<SummaryReportIndicator> findByIsLiveTrueOrderByIndicatorOrderAsc();

}
