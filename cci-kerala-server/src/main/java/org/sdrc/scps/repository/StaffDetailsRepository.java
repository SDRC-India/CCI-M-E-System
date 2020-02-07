/**
 * 
 */
package org.sdrc.scps.repository;

import java.util.List;

import org.sdrc.scps.domain.StaffDetails;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Harsh PRatyush (harsh@sdrc.co.in)
 *
 */

@RepositoryDefinition(domainClass=StaffDetails.class,idClass=Integer.class)
public interface StaffDetailsRepository {

	@Transactional
	void deleteByInstitutionDataInstutionId(int instutionId);

	@Transactional
	<S extends StaffDetails> List<S> save(Iterable<S> staffDetails);

}
