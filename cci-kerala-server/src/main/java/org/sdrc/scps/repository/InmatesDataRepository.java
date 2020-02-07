/**
 * 
 */
package org.sdrc.scps.repository;

import java.util.List;

import org.sdrc.scps.domain.InmatesData;
import org.sdrc.scps.util.SubmissionStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Harsh Pratyush(harsh@sdrc.co.in)
 *
 */

@RepositoryDefinition(domainClass = InmatesData.class, idClass = Integer.class)
public interface InmatesDataRepository {

	InmatesData findByIsLiveTrueAndInmatesData(Integer childId);

	InmatesData findByIsLiveTrueAndQ1AndSubmissionStatus(String q1, SubmissionStatus pending);

	@Query("SELECT COUNT(*) FROM InmatesData im WHERE im.isLive = TRUE AND im.submissionStatus =:status "
			+ " AND  im.owner.area.areaId = :cciId AND (im.q64 IS NULL OR im.q64.optionId!=46)")
	Integer getStrengthForCCI(@Param("cciId") Integer cciId, @Param("status") SubmissionStatus status);

	List<InmatesData> findByIsLiveTrueAndOwnerAreaAreaId(Integer cciId);

	@Query("SELECT im.inmatesData,im.q1,im.q4,im.q7,im.submissionStatus  FROM InmatesData im WHERE im.isLive = TRUE AND im.owner.area.areaId= :cciId")
	List<Object[]> findHeaderDataByIsLiveTrueAndOwnerAreaAreaId(@Param("cciId") Integer cciId);

	@Transactional
	InmatesData save(InmatesData inmatesData);

	List<InmatesData> findByIsLiveTrueAndOwnerAreaAreaIdOrderByUpdatedDateDesc(Integer cciId);

	@Query("SELECT count(im),SUM(CASE WHEN im.q8.optionId = 62 THEN 1 ELSE 0 END),"
			+ "SUM(CASE WHEN im.q8.optionId = 63 THEN 1 ELSE 0 END),"
			+ "SUM(CASE WHEN im.q22.optionId = 46 THEN 1 ELSE 0 END)"
			+ ",SUM(CASE WHEN im.q24.optionId = 46 THEN 1 ELSE 0 END),"
			+ "SUM(CASE WHEN im.q59.optionId = 46 THEN 1 ELSE 0 END),"
			+ "SUM(CASE WHEN im.q34.optionId = 73 THEN 1 ELSE 0 END),SUM(CASE WHEN im.q8.optionId = 64 THEN 1 ELSE 0 END) ,"
			+ "SUM(CASE WHEN im.q29.optionId = 70 THEN 1 ELSE 0 END),SUM(CASE WHEN im.q29.optionId = 71 THEN 1 ELSE 0 END),"
			+ "SUM(CASE WHEN im.q29.optionId = 72 THEN 1 ELSE 0 END) "
			+ " FROM InmatesData im WHERE im.isLive = TRUE AND (im.q64 IS NULL OR im.q64.optionId!=46)"
			+ " AND im.owner.area.parentArea.areaId= :areaId AND im.submissionStatus =:status")
	List<Object[]> findDataByIsLiveTrueAndOwnerDistrict(@Param("areaId") Integer areaId,
			@Param("status") SubmissionStatus status);

	@Query("SELECT count(im),SUM(CASE WHEN im.q8.optionId = 62 THEN 1 ELSE 0 END),"
			+ "SUM(CASE WHEN im.q8.optionId = 63 THEN 1 ELSE 0 END),SUM(CASE WHEN im.q22.optionId = 46 THEN 1 ELSE 0 END)"
			+ ",SUM(CASE WHEN im.q24.optionId = 46 THEN 1 ELSE 0 END),"
			+ "SUM(CASE WHEN im.q59.optionId = 46 THEN 1 ELSE 0 END),"
			+ "SUM(CASE WHEN im.q34.optionId = 73 THEN 1 ELSE 0 END),SUM(CASE WHEN im.q8.optionId = 64 THEN 1 ELSE 0 END),"
			+ "SUM(CASE WHEN im.q29.optionId = 70 THEN 1 ELSE 0 END),SUM(CASE WHEN im.q29.optionId = 71 THEN 1 ELSE 0 END),"
			+ "SUM(CASE WHEN im.q29.optionId = 72 THEN 1 ELSE 0 END) "
			+ "FROM InmatesData im WHERE im.isLive = TRUE AND (im.q64 IS NULL OR im.q64.optionId!=46) AND im.owner.area.parentArea.parentArea.areaId= :areaId  AND im.submissionStatus =:status")
	List<Object[]> findDataByIsLiveTrueAndOwnerState(@Param("areaId") Integer areaId,
			@Param("status") SubmissionStatus status);

	@Query("SELECT count(im),SUM(CASE WHEN im.q8.optionId = 62 THEN 1 ELSE 0 END),"
			+ "SUM(CASE WHEN im.q8.optionId = 63 THEN 1 ELSE 0 END),SUM(CASE WHEN im.q22.optionId = 46 THEN 1 ELSE 0 END)"
			+ ",SUM(CASE WHEN im.q24.optionId = 46 THEN 1 ELSE 0 END),"
			+ "SUM(CASE WHEN im.q59.optionId = 46 THEN 1 ELSE 0 END),SUM(CASE WHEN im.q34.optionId = 73 THEN 1 ELSE 0 END) ,"
			+ "SUM(CASE WHEN im.q8.optionId = 64 THEN 1 ELSE 0 END),"
			+ "SUM(CASE WHEN im.q29.optionId = 70 THEN 1 ELSE 0 END),SUM(CASE WHEN im.q29.optionId = 71 THEN 1 ELSE 0 END),"
			+ "SUM(CASE WHEN im.q29.optionId = 72 THEN 1 ELSE 0 END) "
			+ "FROM InmatesData im WHERE im.isLive = TRUE AND im.submissionStatus =:status AND (im.q64 IS NULL OR im.q64.optionId!=46)")
	List<Object[]> findDataByIsLiveTrueAndOwnerCountry(@Param("status") SubmissionStatus status);

	InmatesData findByInmatesData(Integer submissionId);

	List<InmatesData> findByIsLiveTrueAndOwnerAreaParentAreaAreaIdAndSubmissionStatus(Integer cciId,
			SubmissionStatus pending);

	@Query("SELECT COUNT(im) FROM InmatesData im WHERE im.isLive = TRUE AND im.submissionStatus=:status AND im.owner.area.parentArea.areaId= :districtId")
	Integer findByIsLiveTrueAndOwnerAreaParentAreaAreaIdAndSubmissionStatusCount(@Param("districtId") Integer cciId,
			@Param("status") SubmissionStatus pending);

	@Query("SELECT COUNT(im),im.owner.area.areaId  " + "FROM InmatesData im WHERE im.isLive = TRUE AND "
			+ "(im.q64 IS NULL OR im.q64.optionId!=46) "
			+ "AND im.submissionStatus =:status GROUP BY im.owner.area.areaId")
	List<Object[]> findCountDataByIsLiveTrueAndOwnerAreaAreaId(@Param("status") SubmissionStatus status);

	/**
	 * @param approved
	 * @param optionYes
	 * @return
	 */
	List<InmatesData> findBySubmissionStatusAndQ64OptionId(SubmissionStatus approved, int optionYes);

	/**
	 * @param approved
	 * @param optionYes
	 * @param q1
	 * @return
	 */
	List<InmatesData> findBySubmissionStatusAndQ64OptionIdAndQ1(SubmissionStatus approved, int optionYes, String q1);

	List<InmatesData> findByIsLiveTrueAndOwnerAreaAreaIdAndSubmissionStatus(Integer cciId, SubmissionStatus pending);

	/**
	 * @param approved
	 * @return
	 */
	@Query("SELECT count(im),SUM(CASE WHEN im.q8.optionId = 62 THEN 1 ELSE 0 END),"
			+ "SUM(CASE WHEN im.q8.optionId = 63 THEN 1 ELSE 0 END),"
			+ "SUM(CASE WHEN im.q22.optionId = 46 THEN 1 ELSE 0 END)"
			+ ",SUM(CASE WHEN im.q24.optionId = 46 THEN 1 ELSE 0 END),"
			+ "SUM(CASE WHEN im.q59.optionId = 46 THEN 1 ELSE 0 END),"
			+ "SUM(CASE WHEN im.q28.optionId = 68 THEN 1 ELSE 0 END),"
			+ "SUM(CASE WHEN im.q8.optionId = 64 THEN 1 ELSE 0 END),SUM(CASE WHEN im.q34.optionId = 73 THEN 1 ELSE 0 END),"
			+ "SUM(CASE WHEN im.q29.optionId = 70 THEN 1 ELSE 0 END),SUM(CASE WHEN im.q29.optionId = 71 THEN 1 ELSE 0 END),"
			+ "SUM(CASE WHEN im.q29.optionId = 72 THEN 1 ELSE 0 END) "
			+ "FROM InmatesData im WHERE im.isLive = TRUE AND  (im.q64 IS NULL OR im.q64.optionId!=46) AND im.submissionStatus =:status AND  im.owner.area.areaId= :areaId ")
	List<Object[]> findDataByIsLiveTrueAndOwnerCCI(@Param("areaId") Integer areaId,
			@Param("status") SubmissionStatus approved);

	/**
	 * @param approved
	 * @return
	 */

	@Query(value = "SELECT SUM(CASE WHEN (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total0," + "" + ""
			+ "			SUM(CASE WHEN im.q8 = 63 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END)  as total1,"
			+ "			SUM(CASE WHEN im.q8 = 63 AND (CAST (im.q7 AS float))<=5 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END)  as total2,"
			+ "			SUM(CASE WHEN im.q8 = 63 AND (CAST (im.q7 AS float))>5 AND (CAST (im.q7 AS float))<=10 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END)  as total3,"
			+ "			SUM(CASE WHEN im.q8 = 63  AND (CAST (im.q7 AS float))>10 AND (CAST (im.q7 AS float))<=14 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END)  as total4,"
			+ "			SUM(CASE WHEN im.q8 = 63 AND (CAST (im.q7 AS float))>14 AND (CAST (im.q7 AS float))<=18  AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END)  as total5,"
			+ ""
			+ "			SUM(CASE WHEN im.q8 = 62 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END)  as total6,"
			+ "			SUM(CASE WHEN im.q8 = 62 AND (CAST (im.q7 AS float))<=5 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as   total7,"
			+ "			SUM(CASE WHEN im.q8 = 62 AND (CAST (im.q7 AS float))>5 AND (CAST (im.q7 AS float))<=10 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as  total8,"
			+ "			SUM(CASE WHEN im.q8 = 62 AND (CAST (im.q7 AS float))>10 AND (CAST (im.q7 AS float))<=14 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END)  as total9 ,"
			+ "			SUM(CASE WHEN im.q8 = 62 AND (CAST (im.q7 AS float))>14 AND (CAST (im.q7 AS float))<=18 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END)  as total10,"
			+ ""
			+ "			SUM(CASE WHEN im.q8 = 64 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END)  as total11,"
			+ "			SUM(CASE WHEN im.q8 = 64 AND (CAST (im.q7 AS float))<=5 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total12,"
			+ "			SUM(CASE WHEN im.q8 = 64 AND (CAST (im.q7 AS float))>5 AND (CAST (im.q7 AS float))<=10 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END)  as total13,"
			+ "			SUM(CASE WHEN im.q8 = 64 AND (CAST (im.q7 AS float))>10 AND (CAST (im.q7 AS float))<=14 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total14,"
			+ "			SUM(CASE WHEN im.q8 = 64 AND (CAST (im.q7 AS float))>14 AND (CAST (im.q7 AS float))<=18 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total15,"
			+ "" + "	"
			+ "			SUM(CASE WHEN im.q34 = 73 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END)  as total16 , "
			+ "			SUM(CASE WHEN im.q34 = 73 AND im.q8 = 63 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total17,"
			+ "			SUM(CASE WHEN im.q34 = 73 AND im.q8 = 63 AND (CAST (im.q7 AS float))<=5 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total18,"
			+ "			SUM(CASE WHEN im.q34 = 73 AND im.q8 = 63 AND (CAST (im.q7 AS float))>5 AND (CAST (im.q7 AS float))<=10 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total19,"
			+ "			SUM(CASE WHEN im.q34 = 73 AND im.q8 = 63 AND (CAST (im.q7 AS float))>10 AND (CAST (im.q7 AS float))<=14 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total20,"
			+ "			SUM(CASE WHEN im.q34 = 73 AND im.q8 = 63 AND (CAST (im.q7 AS float))>14 AND (CAST (im.q7 AS float))<=18 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total21,"
			+ ""
			+ "			SUM(CASE WHEN im.q34 = 73 AND im.q8 = 62 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total22,"
			+ "			SUM(CASE WHEN im.q34 = 73 AND im.q8 = 62 AND (CAST (im.q7 AS float))<=5 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total23,"
			+ "			SUM(CASE WHEN im.q34 = 73 AND im.q8 = 62 AND (CAST (im.q7 AS float))>5 AND (CAST (im.q7 AS float))<=10 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total24,"
			+ "			SUM(CASE WHEN im.q34 = 73 AND im.q8 = 62 AND (CAST (im.q7 AS float))>10 AND (CAST (im.q7 AS float))<=14 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total25,"
			+ "			SUM(CASE WHEN im.q34 = 73 AND im.q8 = 62 AND (CAST (im.q7 AS float))>14 AND (CAST (im.q7 AS float))<=18 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total26,"
			+ ""
			+ "			SUM(CASE WHEN im.q34 = 73 AND im.q8 = 64 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total27,"
			+ "			SUM(CASE WHEN im.q34 = 73 AND im.q8 = 64 AND (CAST (im.q7 AS float))<=5 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total28,"
			+ "			SUM(CASE WHEN im.q34 = 73 AND im.q8 = 64 AND (CAST (im.q7 AS float))>5 AND (CAST (im.q7 AS float))<=10 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total29,"
			+ "			SUM(CASE WHEN im.q34 = 73 AND im.q8 = 64 AND (CAST (im.q7 AS float))>10 AND (CAST (im.q7 AS float))<=14 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total30,"
			+ "			SUM(CASE WHEN im.q34 = 73 AND im.q8 = 64 AND (CAST (im.q7 AS float))>14 AND (CAST (im.q7 AS float))<=18 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total31,"
			+ ""
			+ "			SUM(CASE WHEN im.q40 = 46 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total32 ,"
			+ "			SUM(CASE WHEN im.q40 = 46 AND im.q8 = 63 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total33,"
			+ "			SUM(CASE WHEN im.q40 = 46 AND im.q8 = 63 AND (CAST (im.q7 AS float))<=5 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total34,"
			+ "			SUM(CASE WHEN im.q40 = 46 AND im.q8 = 63 AND (CAST (im.q7 AS float))>5 AND (CAST (im.q7 AS float))<=10 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total35,"
			+ "			SUM(CASE WHEN im.q40 = 46 AND im.q8 = 63 AND (CAST (im.q7 AS float))>10 AND (CAST (im.q7 AS float))<=14 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total36,"
			+ "			SUM(CASE WHEN im.q40 = 46 AND im.q8 = 63 AND (CAST (im.q7 AS float))>14 AND (CAST (im.q7 AS float))<=18 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total37,"
			+ ""
			+ "			SUM(CASE WHEN im.q40 = 46 AND im.q8 = 62 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total38,"
			+ "			SUM(CASE WHEN im.q40 = 46 AND im.q8 = 62 AND (CAST (im.q7 AS float))<=5 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total39,"
			+ "			SUM(CASE WHEN im.q40 = 46 AND im.q8 = 62 AND (CAST (im.q7 AS float))>5 AND (CAST (im.q7 AS float))<=10 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total40,"
			+ "			SUM(CASE WHEN im.q40 = 46 AND im.q8 = 62 AND (CAST (im.q7 AS float))>10 AND (CAST (im.q7 AS float))<=14 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total41,"
			+ "			SUM(CASE WHEN im.q40 = 46 AND im.q8 = 62 AND (CAST (im.q7 AS float))>14 AND (CAST (im.q7 AS float))<=18 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total42,"
			+ ""
			+ "			SUM(CASE WHEN im.q40 = 46 AND im.q8 = 64 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total43,"
			+ "			SUM(CASE WHEN im.q40 = 46 AND im.q8 = 64 AND (CAST (im.q7 AS float))<=5 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total44,"
			+ "			SUM(CASE WHEN im.q40 = 46 AND im.q8 = 64 AND (CAST (im.q7 AS float))>5 AND (CAST (im.q7 AS float))<=10 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total45,"
			+ "			SUM(CASE WHEN im.q40 = 46 AND im.q8 = 64 AND (CAST (im.q7 AS float))>10 AND (CAST (im.q7 AS float))<=14 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total46,"
			+ "			SUM(CASE WHEN im.q40 = 46 AND im.q8 = 64 AND (CAST (im.q7 AS float))>14 AND (CAST (im.q7 AS float))<=18 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total47,"
			+ ""
			+ "			SUM(CASE WHEN im.q28 = 69  AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total48,"
			+ "			SUM(CASE WHEN im.q29 = 70  AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total49,"
			+ "			SUM(CASE WHEN im.q29 = 71  AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total50,"
			+ "			SUM(CASE WHEN im.q29 = 72  AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total51,"
			+ "		"
			+ "			SUM(CASE WHEN im.q59 = 46  AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total52,"
			+ "			SUM(CASE WHEN im.q59 = 46 AND im.q8 = 63  AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total53,"
			+ "			SUM(CASE WHEN im.q59 = 46 AND im.q8 = 62  AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total54,"
			+ "			SUM(CASE WHEN im.q59 = 46 AND im.q8 = 64  AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total55,"
			+ "			SUM(CASE WHEN im.q64 = 46 THEN 1 ELSE 0 END) as total56,parent.area_name " + "			"
			+ "			FROM mst_inmates_data im inner join mst_user user1 on user1.user_id=im.owner_account_fk "
			+ "			inner join mst_area area on area.area_id_pk=user1.area_id_fk"
			+ "			inner join mst_area parent on parent.area_id_pk=area.parent_areaid"
			+ "			WHERE im.is_live = TRUE AND im.submission_status =:status"
			+ "			GROUP BY parent.area_name", nativeQuery = true)

	List<Object[]> findTheStatusReportForACountryAndSubmissionStatus(@Param("status") String approved);

	/**
	 * @param areaId
	 * @param approved
	 * @return
	 */

	@Query(value = "SELECT SUM(CASE WHEN (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total0," + "" + ""
			+ "			SUM(CASE WHEN im.q8 = 63 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END)  as total1,"
			+ "			SUM(CASE WHEN im.q8 = 63 AND (CAST (im.q7 AS float))<=5 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END)  as total2,"
			+ "			SUM(CASE WHEN im.q8 = 63 AND (CAST (im.q7 AS float))>5 AND (CAST (im.q7 AS float))<=10 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END)  as total3,"
			+ "			SUM(CASE WHEN im.q8 = 63  AND (CAST (im.q7 AS float))>10 AND (CAST (im.q7 AS float))<=14 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END)  as total4,"
			+ "			SUM(CASE WHEN im.q8 = 63 AND (CAST (im.q7 AS float))>14 AND (CAST (im.q7 AS float))<=18  AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END)  as total5,"
			+ ""
			+ "			SUM(CASE WHEN im.q8 = 62 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END)  as total6,"
			+ "			SUM(CASE WHEN im.q8 = 62 AND (CAST (im.q7 AS float))<=5 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as   total7,"
			+ "			SUM(CASE WHEN im.q8 = 62 AND (CAST (im.q7 AS float))>5 AND (CAST (im.q7 AS float))<=10 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as  total8,"
			+ "			SUM(CASE WHEN im.q8 = 62 AND (CAST (im.q7 AS float))>10 AND (CAST (im.q7 AS float))<=14 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END)  as total9 ,"
			+ "			SUM(CASE WHEN im.q8 = 62 AND (CAST (im.q7 AS float))>14 AND (CAST (im.q7 AS float))<=18 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END)  as total10,"
			+ ""
			+ "			SUM(CASE WHEN im.q8 = 64 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END)  as total11,"
			+ "			SUM(CASE WHEN im.q8 = 64 AND (CAST (im.q7 AS float))<=5 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total12,"
			+ "			SUM(CASE WHEN im.q8 = 64 AND (CAST (im.q7 AS float))>5 AND (CAST (im.q7 AS float))<=10 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END)  as total13,"
			+ "			SUM(CASE WHEN im.q8 = 64 AND (CAST (im.q7 AS float))>10 AND (CAST (im.q7 AS float))<=14 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total14,"
			+ "			SUM(CASE WHEN im.q8 = 64 AND (CAST (im.q7 AS float))>14 AND (CAST (im.q7 AS float))<=18 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total15,"
			+ "" + "	"
			+ "			SUM(CASE WHEN im.q34 = 73 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END)  as total16 , "
			+ "			SUM(CASE WHEN im.q34 = 73 AND im.q8 = 63 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total17,"
			+ "			SUM(CASE WHEN im.q34 = 73 AND im.q8 = 63 AND (CAST (im.q7 AS float))<=5 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total18,"
			+ "			SUM(CASE WHEN im.q34 = 73 AND im.q8 = 63 AND (CAST (im.q7 AS float))>5 AND (CAST (im.q7 AS float))<=10 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total19,"
			+ "			SUM(CASE WHEN im.q34 = 73 AND im.q8 = 63 AND (CAST (im.q7 AS float))>10 AND (CAST (im.q7 AS float))<=14 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total20,"
			+ "			SUM(CASE WHEN im.q34 = 73 AND im.q8 = 63 AND (CAST (im.q7 AS float))>14 AND (CAST (im.q7 AS float))<=18 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total21,"
			+ ""
			+ "			SUM(CASE WHEN im.q34 = 73 AND im.q8 = 62 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total22,"
			+ "			SUM(CASE WHEN im.q34 = 73 AND im.q8 = 62 AND (CAST (im.q7 AS float))<=5 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total23,"
			+ "			SUM(CASE WHEN im.q34 = 73 AND im.q8 = 62 AND (CAST (im.q7 AS float))>5 AND (CAST (im.q7 AS float))<=10 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total24,"
			+ "			SUM(CASE WHEN im.q34 = 73 AND im.q8 = 62 AND (CAST (im.q7 AS float))>10 AND (CAST (im.q7 AS float))<=14 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total25,"
			+ "			SUM(CASE WHEN im.q34 = 73 AND im.q8 = 62 AND (CAST (im.q7 AS float))>14 AND (CAST (im.q7 AS float))<=18 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total26,"
			+ ""
			+ "			SUM(CASE WHEN im.q34 = 73 AND im.q8 = 64 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total27,"
			+ "			SUM(CASE WHEN im.q34 = 73 AND im.q8 = 64 AND (CAST (im.q7 AS float))<=5 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total28,"
			+ "			SUM(CASE WHEN im.q34 = 73 AND im.q8 = 64 AND (CAST (im.q7 AS float))>5 AND (CAST (im.q7 AS float))<=10 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total29,"
			+ "			SUM(CASE WHEN im.q34 = 73 AND im.q8 = 64 AND (CAST (im.q7 AS float))>10 AND (CAST (im.q7 AS float))<=14 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total30,"
			+ "			SUM(CASE WHEN im.q34 = 73 AND im.q8 = 64 AND (CAST (im.q7 AS float))>14 AND (CAST (im.q7 AS float))<=18 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total31,"
			+ ""
			+ "			SUM(CASE WHEN im.q40 = 46 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total32 ,"
			+ "			SUM(CASE WHEN im.q40 = 46 AND im.q8 = 63 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total33,"
			+ "			SUM(CASE WHEN im.q40 = 46 AND im.q8 = 63 AND (CAST (im.q7 AS float))<=5 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total34,"
			+ "			SUM(CASE WHEN im.q40 = 46 AND im.q8 = 63 AND (CAST (im.q7 AS float))>5 AND (CAST (im.q7 AS float))<=10 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total35,"
			+ "			SUM(CASE WHEN im.q40 = 46 AND im.q8 = 63 AND (CAST (im.q7 AS float))>10 AND (CAST (im.q7 AS float))<=14 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total36,"
			+ "			SUM(CASE WHEN im.q40 = 46 AND im.q8 = 63 AND (CAST (im.q7 AS float))>14 AND (CAST (im.q7 AS float))<=18 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total37,"
			+ ""
			+ "			SUM(CASE WHEN im.q40 = 46 AND im.q8 = 62 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total38,"
			+ "			SUM(CASE WHEN im.q40 = 46 AND im.q8 = 62 AND (CAST (im.q7 AS float))<=5 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total39,"
			+ "			SUM(CASE WHEN im.q40 = 46 AND im.q8 = 62 AND (CAST (im.q7 AS float))>5 AND (CAST (im.q7 AS float))<=10 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total40,"
			+ "			SUM(CASE WHEN im.q40 = 46 AND im.q8 = 62 AND (CAST (im.q7 AS float))>10 AND (CAST (im.q7 AS float))<=14 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total41,"
			+ "			SUM(CASE WHEN im.q40 = 46 AND im.q8 = 62 AND (CAST (im.q7 AS float))>14 AND (CAST (im.q7 AS float))<=18 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total42,"
			+ ""
			+ "			SUM(CASE WHEN im.q40 = 46 AND im.q8 = 64 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total43,"
			+ "			SUM(CASE WHEN im.q40 = 46 AND im.q8 = 64 AND (CAST (im.q7 AS float))<=5 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total44,"
			+ "			SUM(CASE WHEN im.q40 = 46 AND im.q8 = 64 AND (CAST (im.q7 AS float))>5 AND (CAST (im.q7 AS float))<=10 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total45,"
			+ "			SUM(CASE WHEN im.q40 = 46 AND im.q8 = 64 AND (CAST (im.q7 AS float))>10 AND (CAST (im.q7 AS float))<=14 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total46,"
			+ "			SUM(CASE WHEN im.q40 = 46 AND im.q8 = 64 AND (CAST (im.q7 AS float))>14 AND (CAST (im.q7 AS float))<=18 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total47,"
			+ ""
			+ "			SUM(CASE WHEN im.q28 = 69  AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total48,"
			+ "			SUM(CASE WHEN im.q29 = 70  AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total49,"
			+ "			SUM(CASE WHEN im.q29 = 71  AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total50,"
			+ "			SUM(CASE WHEN im.q29 = 72  AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total51,"
			+ "		"
			+ "			SUM(CASE WHEN im.q59 = 46  AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total52,"
			+ "			SUM(CASE WHEN im.q59 = 46 AND im.q8 = 63  AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total53,"
			+ "			SUM(CASE WHEN im.q59 = 46 AND im.q8 = 62  AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total54,"
			+ "			SUM(CASE WHEN im.q59 = 46 AND im.q8 = 64  AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total55,"
			+ "			SUM(CASE WHEN im.q64 = 46 THEN 1 ELSE 0 END) as total56,parent.area_name " + "			"
			+ "			FROM mst_inmates_data im inner join mst_user user1 on user1.user_id=im.owner_account_fk "
			+ "			inner join mst_area area on area.area_id_pk=user1.area_id_fk"
			+ "			inner join mst_area parent on parent.area_id_pk=area.parent_areaid "
			+ "			WHERE im.is_live = TRUE AND im.submission_status =:status AND parent.parent_areaid=:stateId  "
			+ "			GROUP BY parent.area_name", nativeQuery = true)

	List<Object[]> findTheStatusReportForAStateAndSubmissionStatus(@Param("stateId") int areaId,
			@Param("status") String approved);

	/**
	 * @param areaId
	 * @param approved
	 * @return
	 */

	@Query(value = "SELECT SUM(CASE WHEN (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total0," + "" + ""
			+ "			SUM(CASE WHEN im.q8 = 63 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END)  as total1,"
			+ "			SUM(CASE WHEN im.q8 = 63 AND (CAST (im.q7 AS float))<=5 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END)  as total2,"
			+ "			SUM(CASE WHEN im.q8 = 63 AND (CAST (im.q7 AS float))>5 AND (CAST (im.q7 AS float))<=10 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END)  as total3,"
			+ "			SUM(CASE WHEN im.q8 = 63  AND (CAST (im.q7 AS float))>10 AND (CAST (im.q7 AS float))<=14 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END)  as total4,"
			+ "			SUM(CASE WHEN im.q8 = 63 AND (CAST (im.q7 AS float))>14 AND (CAST (im.q7 AS float))<=18  AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END)  as total5,"
			+ ""
			+ "			SUM(CASE WHEN im.q8 = 62 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END)  as total6,"
			+ "			SUM(CASE WHEN im.q8 = 62 AND (CAST (im.q7 AS float))<=5 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as   total7,"
			+ "			SUM(CASE WHEN im.q8 = 62 AND (CAST (im.q7 AS float))>5 AND (CAST (im.q7 AS float))<=10 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as  total8,"
			+ "			SUM(CASE WHEN im.q8 = 62 AND (CAST (im.q7 AS float))>10 AND (CAST (im.q7 AS float))<=14 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END)  as total9 ,"
			+ "			SUM(CASE WHEN im.q8 = 62 AND (CAST (im.q7 AS float))>14 AND (CAST (im.q7 AS float))<=18 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END)  as total10,"
			+ ""
			+ "			SUM(CASE WHEN im.q8 = 64 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END)  as total11,"
			+ "			SUM(CASE WHEN im.q8 = 64 AND (CAST (im.q7 AS float))<=5 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total12,"
			+ "			SUM(CASE WHEN im.q8 = 64 AND (CAST (im.q7 AS float))>5 AND (CAST (im.q7 AS float))<=10 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END)  as total13,"
			+ "			SUM(CASE WHEN im.q8 = 64 AND (CAST (im.q7 AS float))>10 AND (CAST (im.q7 AS float))<=14 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total14,"
			+ "			SUM(CASE WHEN im.q8 = 64 AND (CAST (im.q7 AS float))>14 AND (CAST (im.q7 AS float))<=18 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total15,"
			+ "" + "	"
			+ "			SUM(CASE WHEN im.q34 = 73 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END)  as total16 , "
			+ "			SUM(CASE WHEN im.q34 = 73 AND im.q8 = 63 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total17,"
			+ "			SUM(CASE WHEN im.q34 = 73 AND im.q8 = 63 AND (CAST (im.q7 AS float))<=5 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total18,"
			+ "			SUM(CASE WHEN im.q34 = 73 AND im.q8 = 63 AND (CAST (im.q7 AS float))>5 AND (CAST (im.q7 AS float))<=10 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total19,"
			+ "			SUM(CASE WHEN im.q34 = 73 AND im.q8 = 63 AND (CAST (im.q7 AS float))>10 AND (CAST (im.q7 AS float))<=14 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total20,"
			+ "			SUM(CASE WHEN im.q34 = 73 AND im.q8 = 63 AND (CAST (im.q7 AS float))>14 AND (CAST (im.q7 AS float))<=18 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total21,"
			+ ""
			+ "			SUM(CASE WHEN im.q34 = 73 AND im.q8 = 62 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total22,"
			+ "			SUM(CASE WHEN im.q34 = 73 AND im.q8 = 62 AND (CAST (im.q7 AS float))<=5 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total23,"
			+ "			SUM(CASE WHEN im.q34 = 73 AND im.q8 = 62 AND (CAST (im.q7 AS float))>5 AND (CAST (im.q7 AS float))<=10 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total24,"
			+ "			SUM(CASE WHEN im.q34 = 73 AND im.q8 = 62 AND (CAST (im.q7 AS float))>10 AND (CAST (im.q7 AS float))<=14 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total25,"
			+ "			SUM(CASE WHEN im.q34 = 73 AND im.q8 = 62 AND (CAST (im.q7 AS float))>14 AND (CAST (im.q7 AS float))<=18 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total26,"
			+ ""
			+ "			SUM(CASE WHEN im.q34 = 73 AND im.q8 = 64 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total27,"
			+ "			SUM(CASE WHEN im.q34 = 73 AND im.q8 = 64 AND (CAST (im.q7 AS float))<=5 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total28,"
			+ "			SUM(CASE WHEN im.q34 = 73 AND im.q8 = 64 AND (CAST (im.q7 AS float))>5 AND (CAST (im.q7 AS float))<=10 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total29,"
			+ "			SUM(CASE WHEN im.q34 = 73 AND im.q8 = 64 AND (CAST (im.q7 AS float))>10 AND (CAST (im.q7 AS float))<=14 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total30,"
			+ "			SUM(CASE WHEN im.q34 = 73 AND im.q8 = 64 AND (CAST (im.q7 AS float))>14 AND (CAST (im.q7 AS float))<=18 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total31,"
			+ ""
			+ "			SUM(CASE WHEN im.q40 = 46 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total32 ,"
			+ "			SUM(CASE WHEN im.q40 = 46 AND im.q8 = 63 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total33,"
			+ "			SUM(CASE WHEN im.q40 = 46 AND im.q8 = 63 AND (CAST (im.q7 AS float))<=5 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total34,"
			+ "			SUM(CASE WHEN im.q40 = 46 AND im.q8 = 63 AND (CAST (im.q7 AS float))>5 AND (CAST (im.q7 AS float))<=10 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total35,"
			+ "			SUM(CASE WHEN im.q40 = 46 AND im.q8 = 63 AND (CAST (im.q7 AS float))>10 AND (CAST (im.q7 AS float))<=14 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total36,"
			+ "			SUM(CASE WHEN im.q40 = 46 AND im.q8 = 63 AND (CAST (im.q7 AS float))>14 AND (CAST (im.q7 AS float))<=18 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total37,"
			+ ""
			+ "			SUM(CASE WHEN im.q40 = 46 AND im.q8 = 62 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total38,"
			+ "			SUM(CASE WHEN im.q40 = 46 AND im.q8 = 62 AND (CAST (im.q7 AS float))<=5 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total39,"
			+ "			SUM(CASE WHEN im.q40 = 46 AND im.q8 = 62 AND (CAST (im.q7 AS float))>5 AND (CAST (im.q7 AS float))<=10 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total40,"
			+ "			SUM(CASE WHEN im.q40 = 46 AND im.q8 = 62 AND (CAST (im.q7 AS float))>10 AND (CAST (im.q7 AS float))<=14 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total41,"
			+ "			SUM(CASE WHEN im.q40 = 46 AND im.q8 = 62 AND (CAST (im.q7 AS float))>14 AND (CAST (im.q7 AS float))<=18 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total42,"
			+ ""
			+ "			SUM(CASE WHEN im.q40 = 46 AND im.q8 = 64 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total43,"
			+ "			SUM(CASE WHEN im.q40 = 46 AND im.q8 = 64 AND (CAST (im.q7 AS float))<=5 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total44,"
			+ "			SUM(CASE WHEN im.q40 = 46 AND im.q8 = 64 AND (CAST (im.q7 AS float))>5 AND (CAST (im.q7 AS float))<=10 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total45,"
			+ "			SUM(CASE WHEN im.q40 = 46 AND im.q8 = 64 AND (CAST (im.q7 AS float))>10 AND (CAST (im.q7 AS float))<=14 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total46,"
			+ "			SUM(CASE WHEN im.q40 = 46 AND im.q8 = 64 AND (CAST (im.q7 AS float))>14 AND (CAST (im.q7 AS float))<=18 AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total47,"
			+ ""
			+ "			SUM(CASE WHEN im.q28 = 69  AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total48,"
			+ "			SUM(CASE WHEN im.q29 = 70  AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total49,"
			+ "			SUM(CASE WHEN im.q29 = 71  AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total50,"
			+ "			SUM(CASE WHEN im.q29 = 72  AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total51,"
			+ "		"
			+ "			SUM(CASE WHEN im.q59 = 46  AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total52,"
			+ "			SUM(CASE WHEN im.q59 = 46 AND im.q8 = 63  AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total53,"
			+ "			SUM(CASE WHEN im.q59 = 46 AND im.q8 = 62  AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total54,"
			+ "			SUM(CASE WHEN im.q59 = 46 AND im.q8 = 64  AND (im.q64 IS NULL OR im.q64!=46) THEN 1 ELSE 0 END) as total55,"
			+ "			SUM(CASE WHEN im.q64 = 46 THEN 1 ELSE 0 END) as total56,parent.area_name " + "			"
			+ "			FROM mst_inmates_data im inner join mst_user user1 on user1.user_id=im.owner_account_fk "
			+ "			inner join mst_area area on area.area_id_pk=user1.area_id_fk"
			+ "			inner join mst_area parent on parent.area_id_pk=area.parent_areaid"
			+ "			WHERE im.is_live = TRUE AND im.submission_status =:status AND parent.area_id_pk=:districtId  "
			+ "			GROUP BY parent.area_name", nativeQuery = true)

	List<Object[]> findTheStatusReportForADistrictAndSubmissionStatus(@Param("districtId") int areaId,
			@Param("status") String approved);

	@Query(" SELECT COUNT(DISTINCT im1.q1),im1.owner.area.areaId FROM InmatesData im1 WHERE im1.owner.area.areaId IN "
			+ " (SELECT im.owner.area.areaId  FROM InmatesData im WHERE im.isLive = TRUE AND "
			+ " (im.q64 IS NULL OR im.q64.optionId!=46) AND im.submissionStatus = :status AND im.owner.area.parentArea.areaId =:districtId) "
			+ " AND im1.isDeleted = FALSE AND im1.isLive = TRUE AND im1.submissionStatus IN  :statusShown AND (im1.q64 IS NULL OR im1.q64.optionId!=46) GROUP BY im1.owner.area.areaId")
	List<Object[]> findCountDataOfInmatesWithPendingData(@Param("status") SubmissionStatus status,
			@Param("districtId") int districtId,@Param("statusShown")List<SubmissionStatus> statusShown);

}
