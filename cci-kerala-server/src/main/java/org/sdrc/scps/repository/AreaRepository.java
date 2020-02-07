/**
 * 
 */
package org.sdrc.scps.repository;

import java.util.List;

import org.sdrc.scps.domain.Area;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Harsh Pratyush
 *
 */

@RepositoryDefinition(idClass=Integer.class,domainClass=Area.class)
public interface AreaRepository {
	
	List<Area> findAll();

	List<Area> findByAreaLevelAreaLevelId(Integer areaLevelId);

	@Transactional
	Area save(Area area);
	
	
	@Query("SELECT MAX(ar.areaCode) FROM  Area ar where parentArea.areaId = :parentId")
	 String findMaxAreaCodeByParentAreaId(@Param("parentId") int parentId);

	List<Area> findByIsLiveTrue();

	List<Area> findByParentAreaAreaCode(String string);
	
	List<Area> findByParentArea(String string);

	/**
	 * @param districtLevel
	 * @return
	 */
	List<Area> findByAreaLevelAreaLevelIdAndIsLiveTrueOrderByAreaNameAsc(int districtLevel);

	/**
	 * @param asList
	 * @return
	 */
	List<Area> findByAreaLevelAreaLevelIdInAndIsLiveTrueOrderByAreaNameAsc(List<Integer> asList);

	/**
	 * @param areaId
	 * @return
	 */
	Area findByAreaId(int areaId);



}
