/**
 * 
 */
package org.sdrc.scps.repository;

import java.util.List;

import org.sdrc.scps.domain.Area;
import org.sdrc.scps.domain.InstitutionData;
import org.sdrc.scps.util.SubmissionStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Harsh Pratyush(harsh@sdrc.co.in)
 *
 */

@RepositoryDefinition(domainClass = InstitutionData.class, idClass = Integer.class)
public interface InstitutionDataRepository {

	InstitutionData findByIsLiveTrueAndSubmissionStatus(
			SubmissionStatus approved);

	InstitutionData findByIsLiveTrueAndSubmissionStatusAndQ7AreaId(
			SubmissionStatus approved, int areaId);

	InstitutionData findByIsLiveTrueAndInstutionId(Integer submissioId);

	List<InstitutionData> findByIsLiveTrueAndQ7AreaId(Integer cciId);

	@Transactional
	InstitutionData save(InstitutionData institutionData);

	List<InstitutionData> findByIsLiveTrueAndSubmissionStatusAndQ11AreaId(
			SubmissionStatus approved, int areaId);
	
	
	
	@Query("SELECT COUNT(im) FROM InstitutionData im WHERE  im.isLive IS TRUE AND  im.submissionStatus= :approved AND  im.q11.areaId=:districtId")
	Integer findByIsLiveTrueAndSubmissionStatusAndQ11AreaIdCount(
			@Param("approved")SubmissionStatus approved, 	@Param("districtId") int areaId);

	List<InstitutionData> findByIsDeletedFalseAndQ7AreaId(Integer cciId);

	InstitutionData findByInstutionId(Integer submissioId);

	List<InstitutionData> findByIsDeletedFalseAndQ7AreaIdOrderByInstutionIdDesc(
			Integer cciId);

	/**
	 * @param approved
	 * @return
	 */
	List<InstitutionData> findByIsDeletedFalseAndIsLiveTrueAndSubmissionStatusOrderByUpdatedDateDesc(
			SubmissionStatus approved);

	/**
	 * @param approved
	 * @param area
	 * @return
	 */
	List<InstitutionData> findByIsDeletedFalseAndIsLiveTrueAndSubmissionStatusAndQ10OrderByUpdatedDateDesc(
			SubmissionStatus approved, Area area);

	/**
	 * @param approved
	 * @param area
	 * @return
	 */
	List<InstitutionData> findByIsDeletedFalseAndIsLiveTrueAndSubmissionStatusAndQ11OrderByUpdatedDateDesc(
			SubmissionStatus approved, Area area);

	// @Transactional
	// @Modifying
	// @Query("UPDATE InstitutionData im SET im.isDeleted = TRUE AND im.isLive = FALSE WHERE im.instutionId :submissionId")
	// void deleteSubmission(@Param("submissionId")Integer submissionId);

	@Query("SELECT im.q17,im.q7.areaCode FROM InstitutionData im WHERE  im.isLive IS TRUE AND  im.submissionStatus= :approved")
	List<Object[]> getAllInstitutionJJREgdNo(
			@Param("approved") SubmissionStatus approved);

	/**
	 * @param approved
	 * @return
	 */

	@Query("SELECT SUM((CAST (bd.q34 AS float))),inst.q11.areaName,inst.q11.areaId "
			+ "  FROM InstitutionData inst  join inst.buildingDetails bd "
			+ "  where inst.isLive = true and inst.submissionStatus=:status GROUP BY inst.q11.areaName,inst.q11.areaId ")
	List<Object[]> findAvgSqftByIsLiveTrueAndOwnerCountry(
			@Param("status") SubmissionStatus approved);

	/**
	 * @param areaId
	 * @param approved
	 * @return
	 */

	@Query("SELECT SUM((CAST (bd.q34 AS float))),inst.q11.areaName,inst.q11.areaId "
			+ "  FROM InstitutionData inst  join inst.buildingDetails bd "
			+ "  where inst.isLive = true and inst.submissionStatus=:status AND inst.q10.areaId=:stateId GROUP BY inst.q11.areaName,inst.q11.areaId ")
	List<Object[]> findAvgSqftByIsLiveTrueAndOwnerState(
			@Param("stateId") int areaId,
			@Param("status") SubmissionStatus approved);

	@Query("SELECT SUM((CAST (bd.q34 AS float))),inst.q7.areaId "
			+ "  FROM InstitutionData inst  join inst.buildingDetails bd "
			+ "  where inst.isLive = true and inst.submissionStatus=:status GROUP BY inst.q7.areaId ")
	List<Object[]> findAvgSqftForCCIIsLiveTrueAndOwnerCountry(
			@Param("status") SubmissionStatus approved);

	/**
	 * @param areaId
	 * @param approved
	 * @return
	 */

	@Query("SELECT SUM((CAST (bd.q34 AS float))),inst.q7.areaId "
			+ "  FROM InstitutionData inst  join inst.buildingDetails bd "
			+ "  where inst.isLive = true and inst.submissionStatus=:status AND inst.q10.areaId=:stateId GROUP BY inst.q7.areaId ")
	List<Object[]> findAvgSqftForCCIIsLiveTrueAndOwnerState(
			@Param("stateId") int areaId,
			@Param("status") SubmissionStatus approved);

	/**
	 * @param areaId
	 * @param approved
	 * @return
	 */
	@Query("SELECT SUM(CAST(bd.q34 AS float)),inst.q7.areaId "
			+ "  FROM InstitutionData inst  join inst.buildingDetails bd "
			+ "  where inst.isLive = true and inst.submissionStatus=:status AND inst.q11.areaId=:districtId  GROUP BY inst.q7.areaId ")
	List<Object[]> findAvgSqftForCCIIsLiveTrueAndOwnerDistrict(
			@Param("districtId") int areaId,
			@Param("status") SubmissionStatus approved);

	/**
	 * @param areaId
	 * @param approved
	 * @return
	 */
	@Query("SELECT count(im),"
			+ "SUM(CASE WHEN im.q1.optionId=1 THEN 1 ELSE 0 END),SUM(CASE WHEN im.q1.optionId=1 AND im.q6.optionId=15 THEN 1 ELSE 0 END),"
			+ "SUM(CASE WHEN im.q1.optionId=1 AND im.q6.optionId=16 THEN 1 ELSE 0 END),SUM(CASE WHEN im.q1.optionId=1 AND im.q6.optionId=17 THEN 1 ELSE 0 END),"

			+ "SUM(CASE WHEN im.q1.optionId=2 THEN 1 ELSE 0 END),SUM(CASE WHEN im.q1.optionId=2 AND im.q6.optionId=15 THEN 1 ELSE 0 END),"
			+ "SUM(CASE WHEN im.q1.optionId=2 AND im.q6.optionId=16 THEN 1 ELSE 0 END),SUM(CASE WHEN im.q1.optionId=2 AND im.q6.optionId=17 THEN 1 ELSE 0 END),"

			+ "SUM(CASE WHEN im.q5.optionId=10 THEN 1 ELSE 0 END),SUM(CASE WHEN im.q5.optionId=11 THEN 1 ELSE 0 END),SUM(CASE WHEN im.q5.optionId=12 THEN 1 ELSE 0 END),"
			+ "SUM(CASE WHEN im.q5.optionId=14 THEN 1 ELSE 0 END),SUM(CASE WHEN im.q5.optionId=13 THEN 1 ELSE 0 END),"

			+ "SUM(CAST(im.q25 AS float)),SUM(CASE WHEN im.q1.optionId=1 THEN CAST(im.q25 AS float) ELSE 0 END),"
			+ "SUM(CASE WHEN im.q1.optionId=1 AND im.q6.optionId=15 THEN CAST(im.q25 AS float) ELSE 0 END),"
			+ "SUM(CASE WHEN im.q1.optionId=1 AND im.q6.optionId=16 THEN CAST(im.q25 AS float) ELSE 0 END),"
			+ "SUM(CASE WHEN im.q1.optionId=1 AND im.q6.optionId=17 THEN CAST(im.q25 AS float) ELSE 0 END),"

			+ "SUM(CASE WHEN im.q1.optionId=2 THEN CAST(im.q25 AS float) ELSE 0 END),"
			+ "SUM(CASE WHEN im.q1.optionId=2 AND im.q6.optionId=15 THEN CAST(im.q25 AS float) ELSE 0 END),"
			+ "SUM(CASE WHEN im.q1.optionId=2 AND im.q6.optionId=16 THEN CAST(im.q25 AS float) ELSE 0 END),"
			+ "SUM(CASE WHEN im.q1.optionId=2 AND im.q6.optionId=17 THEN CAST(im.q25 AS float) ELSE 0 END),"
			+ "SUM(CASE WHEN im.q51.optionId=46 THEN 1 ELSE 0 END),SUM(CASE WHEN im.q52.optionId=46 THEN 1 ELSE 0 END),"
			+ "SUM (CASE WHEN im.q19 < CURRENT_DATE THEN 1 ELSE 0 END),SUM (CASE WHEN im.q19 < CURRENT_DATE AND im.q1.optionId=1 THEN 1 ELSE 0 END)"
			+ ",SUM (CASE WHEN im.q19 < CURRENT_DATE AND im.q1.optionId=2 THEN 1 ELSE 0 END)"
			+ ",im.q11.areaName"

			+ " FROM InstitutionData im WHERE  im.isLive IS TRUE AND  im.submissionStatus= :status AND im.q11.areaId=:areaId GROUP BY im.q11.areaName")
	List<Object[]> findTheStatusReportForADistrictAndSubmissionStatus(
			@Param("areaId") int areaId,
			@Param("status") SubmissionStatus approved);

	/**
	 * @param areaId
	 * @param approved
	 * @return
	 */

	@Query("SELECT count(im),"
			+ "SUM(CASE WHEN im.q1.optionId=1 THEN 1 ELSE 0 END),SUM(CASE WHEN im.q1.optionId=1 AND im.q6.optionId=15 THEN 1 ELSE 0 END),"
			+ "SUM(CASE WHEN im.q1.optionId=1 AND im.q6.optionId=16 THEN 1 ELSE 0 END),SUM(CASE WHEN im.q1.optionId=1 AND im.q6.optionId=17 THEN 1 ELSE 0 END),"

			+ "SUM(CASE WHEN im.q1.optionId=2 THEN 1 ELSE 0 END),SUM(CASE WHEN im.q1.optionId=2 AND im.q6.optionId=15 THEN 1 ELSE 0 END),"
			+ "SUM(CASE WHEN im.q1.optionId=2 AND im.q6.optionId=16 THEN 1 ELSE 0 END),SUM(CASE WHEN im.q1.optionId=2 AND im.q6.optionId=17 THEN 1 ELSE 0 END),"

			+ "SUM(CASE WHEN im.q5.optionId=10 THEN 1 ELSE 0 END),SUM(CASE WHEN im.q5.optionId=11 THEN 1 ELSE 0 END),SUM(CASE WHEN im.q5.optionId=12 THEN 1 ELSE 0 END),"
			+ "SUM(CASE WHEN im.q5.optionId=14 THEN 1 ELSE 0 END),SUM(CASE WHEN im.q5.optionId=13 THEN 1 ELSE 0 END),"

			+ "SUM(CAST(im.q25 AS float)),SUM(CASE WHEN im.q1.optionId=1 THEN CAST(im.q25 AS float) ELSE 0 END),"
			+ "SUM(CASE WHEN im.q1.optionId=1 AND im.q6.optionId=15 THEN CAST(im.q25 AS float) ELSE 0 END),"
			+ "SUM(CASE WHEN im.q1.optionId=1 AND im.q6.optionId=16 THEN CAST(im.q25 AS float) ELSE 0 END),"
			+ "SUM(CASE WHEN im.q1.optionId=1 AND im.q6.optionId=17 THEN CAST(im.q25 AS float) ELSE 0 END),"

			+ "SUM(CASE WHEN im.q1.optionId=2 THEN CAST(im.q25 AS float) ELSE 0 END),"
			+ "SUM(CASE WHEN im.q1.optionId=2 AND im.q6.optionId=15 THEN CAST(im.q25 AS float) ELSE 0 END),"
			+ "SUM(CASE WHEN im.q1.optionId=2 AND im.q6.optionId=16 THEN CAST(im.q25 AS float) ELSE 0 END),"
			+ "SUM(CASE WHEN im.q1.optionId=2 AND im.q6.optionId=17 THEN CAST(im.q25 AS float) ELSE 0 END),"
			+ "SUM(CASE WHEN im.q51.optionId=46 THEN 1 ELSE 0 END),SUM(CASE WHEN im.q52.optionId=46 THEN 1 ELSE 0 END),"
			+ "SUM (CASE WHEN im.q19 < CURRENT_DATE THEN 1 ELSE 0 END),SUM (CASE WHEN im.q19 < CURRENT_DATE AND im.q1.optionId=1 THEN 1 ELSE 0 END)"
			+ ",SUM (CASE WHEN im.q19 < CURRENT_DATE AND im.q1.optionId=2 THEN 1 ELSE 0 END)"
			+ ",im.q11.areaName"

			+ " FROM InstitutionData im WHERE  im.isLive IS TRUE AND  im.submissionStatus= :status AND im.q10.areaId=:areaId GROUP BY im.q11.areaName")
	List<Object[]> findTheStatusReportForAStateAndSubmissionStatus(
			@Param("areaId") int areaId,
			@Param("status") SubmissionStatus approved);

	/**
	 * @param approved
	 * @return
	 */

	@Query("SELECT count(im),"
			+ "SUM(CASE WHEN im.q1.optionId=1 THEN 1 ELSE 0 END),SUM(CASE WHEN im.q1.optionId=1 AND im.q6.optionId=15 THEN 1 ELSE 0 END),"
			+ "SUM(CASE WHEN im.q1.optionId=1 AND im.q6.optionId=16 THEN 1 ELSE 0 END),SUM(CASE WHEN im.q1.optionId=1 AND im.q6.optionId=17 THEN 1 ELSE 0 END),"

			+ "SUM(CASE WHEN im.q1.optionId=2 THEN 1 ELSE 0 END),SUM(CASE WHEN im.q1.optionId=2 AND im.q6.optionId=15 THEN 1 ELSE 0 END),"
			+ "SUM(CASE WHEN im.q1.optionId=2 AND im.q6.optionId=16 THEN 1 ELSE 0 END),SUM(CASE WHEN im.q1.optionId=2 AND im.q6.optionId=17 THEN 1 ELSE 0 END),"

			+ "SUM(CASE WHEN im.q5.optionId=10 THEN 1 ELSE 0 END),SUM(CASE WHEN im.q5.optionId=11 THEN 1 ELSE 0 END),SUM(CASE WHEN im.q5.optionId=12 THEN 1 ELSE 0 END),"
			+ "SUM(CASE WHEN im.q5.optionId=14 THEN 1 ELSE 0 END),SUM(CASE WHEN im.q5.optionId=13 THEN 1 ELSE 0 END),"

			+ "SUM(CAST(im.q25 AS float)),SUM(CASE WHEN im.q1.optionId=1 THEN CAST(im.q25 AS float) ELSE 0 END),"
			+ "SUM(CASE WHEN im.q1.optionId=1 AND im.q6.optionId=15 THEN CAST(im.q25 AS float) ELSE 0 END),"
			+ "SUM(CASE WHEN im.q1.optionId=1 AND im.q6.optionId=16 THEN CAST(im.q25 AS float) ELSE 0 END),"
			+ "SUM(CASE WHEN im.q1.optionId=1 AND im.q6.optionId=17 THEN CAST(im.q25 AS float) ELSE 0 END),"

			+ "SUM(CASE WHEN im.q1.optionId=2 THEN CAST(im.q25 AS float) ELSE 0 END),"
			+ "SUM(CASE WHEN im.q1.optionId=2 AND im.q6.optionId=15 THEN CAST(im.q25 AS float) ELSE 0 END),"
			+ "SUM(CASE WHEN im.q1.optionId=2 AND im.q6.optionId=16 THEN CAST(im.q25 AS float) ELSE 0 END),"
			+ "SUM(CASE WHEN im.q1.optionId=2 AND im.q6.optionId=17 THEN CAST(im.q25 AS float) ELSE 0 END),"
			+ "SUM(CASE WHEN im.q51.optionId=46 THEN 1 ELSE 0 END),SUM(CASE WHEN im.q52.optionId=46 THEN 1 ELSE 0 END),"
			+ "SUM (CASE WHEN im.q19 < CURRENT_DATE THEN 1 ELSE 0 END),SUM (CASE WHEN im.q19 < CURRENT_DATE AND im.q1.optionId=1 THEN 1 ELSE 0 END)"
			+ ",SUM (CASE WHEN im.q19 < CURRENT_DATE AND im.q1.optionId=2 THEN 1 ELSE 0 END)"
			+ ",im.q11.areaName"

			+ " FROM InstitutionData im WHERE  im.isLive IS TRUE AND  im.submissionStatus= :status GROUP BY im.q11.areaName")
	List<Object[]> findTheStatusReportForACountryAndSubmissionStatus(
			@Param("status") SubmissionStatus approved);

	/**
	 * @param approved
	 * @param cciId
	 * @return
	 */
	
	@Query("SELECT im.q25,im.q7.areaId FROM InstitutionData im WHERE  im.isLive IS TRUE AND  im.submissionStatus= :staus AND im.q11.areaId=:areaId")
	List<Object[]> findSanctionedStrengthForEachCCIInDistrict(@Param("staus")SubmissionStatus approved,@Param("areaId") Integer cciId);

	/**
	 * @param areaId
	 * @param approved
	 * @return
	 */
	// @Query("SELECT SUM(CAST(bd.q34 AS INTEGER)),inst.q11 " +
	// "  FROM InstitutionData inst  join inst.buildingDetails bd " +
	// "  where inst.isLive = true and inst.submissionStatus=:approved GROUP BY inst.q11 ")
	// List<Object[]> findAvgSqftByIsLiveTrueAndOwnerDistrict(@Param("stateId")
	// int areaId,@Param("status") SubmissionStatus approved);

}
