/**
 * 
 */
package org.sdrc.scps.repository;

import java.util.List;

import org.sdrc.scps.domain.GrantInAidDetails;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Harsh Pratyush(harsh@sdrc.co.in)
 *
 */

@RepositoryDefinition(domainClass=GrantInAidDetails.class,idClass=Integer.class)
public interface GrantInAidDetailsRepository {

	@Transactional
	void deleteByInstitutionDataInstutionId(int instutionId);

	@Transactional
	<S extends GrantInAidDetails> List<S> save(Iterable<S> grantInAidDetails);

}
