/**
 * 
 */
package org.sdrc.scps.repository;

import java.util.List;

import org.sdrc.scps.domain.OptionType;
import org.springframework.data.repository.RepositoryDefinition;

/**
 * @author Harsh Pratyush(harsh@sdrc.co.in)
 *
 */

@RepositoryDefinition(domainClass=OptionType.class,idClass=Integer.class)
public interface OptionTypeRepository {
	
	OptionType save(OptionType ooptionType);
	
	List<OptionType> findAll();

}
