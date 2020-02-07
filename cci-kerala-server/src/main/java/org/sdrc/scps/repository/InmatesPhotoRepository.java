/**
 * 
 */
package org.sdrc.scps.repository;

import java.util.List;

import org.sdrc.scps.domain.InmatesPhoto;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */

@RepositoryDefinition(domainClass=InmatesPhoto.class,idClass=Integer.class)
public interface InmatesPhotoRepository {


	@Transactional
	void deleteByInmatesDataInmatesData(int inmateId);

	@Transactional
	<S extends InmatesPhoto> List<S> save(Iterable<S> inmatesPhotos);


	
}
