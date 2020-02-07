/**
 * 
 */
package org.sdrc.scps.repository;

import java.util.List;

import org.sdrc.scps.domain.ManagementDetails;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Harsh Pratyush
 *
 */

@RepositoryDefinition(domainClass=ManagementDetails.class,idClass=Integer.class)
public interface ManagementsDetailsRepository {

	@Transactional
	void deleteByInstitutionDataInstutionId(int instutionId);

	@Transactional
	<S extends ManagementDetails> List<S> save(Iterable<S> managementDetails);

}
