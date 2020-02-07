/**
 * 
 */
package org.sdrc.scps.repository;

import org.sdrc.scps.domain.InmatesIdGeneration;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */

@RepositoryDefinition(domainClass=InmatesIdGeneration.class,idClass=Integer.class)
public interface InmatesIdGenerationRepository {
	
	@Transactional
	InmatesIdGeneration save(InmatesIdGeneration inmatesIdGeneration);

	InmatesIdGeneration findByAreaAreaId(Integer cciId);

}
