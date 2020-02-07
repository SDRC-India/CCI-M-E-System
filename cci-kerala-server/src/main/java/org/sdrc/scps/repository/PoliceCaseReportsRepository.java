/**
 * 
 */
package org.sdrc.scps.repository;

import java.util.List;

import org.sdrc.scps.domain.PoliceCaseReports;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */

@RepositoryDefinition(domainClass=PoliceCaseReports.class,idClass=Integer.class)
public interface PoliceCaseReportsRepository {

	@Transactional
	void deleteByInstitutionDataInstutionId(int instutionId);

	@Transactional
	<S extends PoliceCaseReports> List<S> save(Iterable<S> policeCaseReports);

}
