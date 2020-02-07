/**
 * 
 */
package org.sdrc.scps.repository;

import java.util.List;

import org.sdrc.scps.domain.Area;
import org.sdrc.scps.domain.User;
import org.sdrc.usermgmt.domain.Account;
import org.sdrc.usermgmt.domain.Designation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Harsh Pratyush(harsh@sdrc.co.in)
 *
 */

@RepositoryDefinition(idClass=Integer.class,domainClass=User.class)
public interface UserRepository {

	@Transactional
	User save(User user);

	User findByAccount(Account account);

	/**
	 * @param q56
	 * @return
	 */
	List<User> findByArea(Area q56);

	/**
	 * @param area
	 * @return
	 */
	List<User> findByAreaAndAccountEnabledTrue(Area area);

	/**
	 * @return
	 */
	List<User> findAll();
	
	@Query(" SELECT ac.userName,desg.name,user.area.areaName,ac.enabled,user.area.areaCode"
			+ " ,ac.id,desg.id,user.area.areaLevel.areaLevelId,user.area.parentArea.areaName FROM User user INNER JOIN user.account ac"
			+ " INNER JOIN ac.accountDesignationMapping adm INNER JOIN adm.designation desg")
	List<Object []> getAllDataForUserTable();

	/**
	 * @param designation
	 * @return
	 */
	List<User> findByAccountAccountDesignationMappingDesignation(Designation designation);

}
