package org.sdrc.scps.repository;

import java.util.List;

import org.sdrc.scps.domain.Options;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.transaction.annotation.Transactional;

@RepositoryDefinition(domainClass = Options.class, idClass = Integer.class)
public interface OptionRepositry {

	List<Options> save(List<Options> options);

	@Transactional
	Options save(Options optionObject);

	/**
	 * @param cciTypeId
	 * @return
	 */
	List<Options> findByOptionTypeOptionTypeId(int cciTypeId);

}
