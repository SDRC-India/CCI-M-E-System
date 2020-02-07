/**
 * 
 */
package org.sdrc.scps.repository;

import org.sdrc.scps.domain.QuestionOptionTypeMapping;
import org.springframework.data.repository.RepositoryDefinition;

/**
 * @author Harsh Pratyush(harsh@sdrc.co.in)
 *
 */


@RepositoryDefinition(domainClass=QuestionOptionTypeMapping.class,idClass=Integer.class)
public interface QuestionOptionTypeMappingRepository {
	
	QuestionOptionTypeMapping save(QuestionOptionTypeMapping questionOptionTypeMapping);

}
