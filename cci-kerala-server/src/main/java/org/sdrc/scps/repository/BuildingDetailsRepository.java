/**
 * 
 */
package org.sdrc.scps.repository;

import java.util.List;

import org.sdrc.scps.domain.BuildingDetails;
import org.sdrc.scps.util.SubmissionStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Harsh Pratyush(harsh@sdr.co.in)
 *
 */

@RepositoryDefinition(domainClass=BuildingDetails.class,idClass=Integer.class)
public interface BuildingDetailsRepository {

	@Transactional
	void deleteByInstitutionDataInstutionId(int instutionId);

	@Transactional
	<S extends BuildingDetails> List<S> save(Iterable<S> buildingDetails);
	
	
	@Modifying 
	@Transactional
	@Query("UPDATE"
			+ " BuildingDetails building SET "
			+ "building.removable = :status  "
			+ " WHERE building.institutionData.instutionId = :institutionId ")
	void updateStatus(@Param("institutionId") int institutionId,@Param("status") boolean status);

	/**
	 * @param districtId
	 * @return
	 */
	
	@Query("Select building.indexTrackNum, building.q33BuildingName,building.institutionData.q7.areaId "
			+ " FROM BuildingDetails building WHERE building.institutionData.isLive IS TRUE"
			+ " AND  building.institutionData.submissionStatus=:status AND building.institutionData.q11.areaId=:districtId ")
	List<Object[]> findBuildingNameOfAllInstitutionOfaDistrict(@Param("districtId")int districtId,@Param("status")SubmissionStatus status);
}
