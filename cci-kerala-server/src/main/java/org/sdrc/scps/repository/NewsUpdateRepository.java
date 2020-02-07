/**
 * 
 */
package org.sdrc.scps.repository;

import java.util.List;

import org.sdrc.scps.domain.NewsUpdate;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Harsh Pratyush
 *
 */
@RepositoryDefinition(domainClass=NewsUpdate.class,idClass=Integer.class)
public interface NewsUpdateRepository {

	List<NewsUpdate> findByIsLiveTrue();
	
	
	@Transactional
	NewsUpdate save(NewsUpdate newsUpdate);
	
	
	NewsUpdate findByNewsUpdateId(int newsUpdateId);


	/**
	 * @return
	 */
	List<NewsUpdate> findByIsLiveTrueOrderByUpdatedDateDesc();
	
}
