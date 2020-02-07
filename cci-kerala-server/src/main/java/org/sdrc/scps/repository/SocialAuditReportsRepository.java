/**
 * 
 */
package org.sdrc.scps.repository;

import java.util.List;

import org.sdrc.scps.domain.SocialAuditReports;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Harsh Pratyush
 *
 */

@RepositoryDefinition(domainClass=SocialAuditReports.class,idClass=Integer.class)
public interface SocialAuditReportsRepository {

	@Transactional
	void deleteByInstitutionDataInstutionId(int instutionId);

	@Transactional
	<S extends SocialAuditReports> List<S> save(Iterable<S> socialAuditReports);
	
}
