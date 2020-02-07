/**
 * 
 */
package org.sdrc.scps.repository;

import java.util.List;

import org.sdrc.scps.domain.Question;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */

@RepositoryDefinition(domainClass = Question.class, idClass = Integer.class)
public interface QuestionRepository {

	List<Question> findAll();

	@Transactional
	Question save(Question question);

//	List<Question> findByFormFormId(int formId);

	List<Question> findByFormFormIdAndIsLiveTrue(int formId);

	Question findByColumnn(String columnn);

	List<Question> findByFormFormIdAndIsLiveTrueOrderByQuestionOrderAsc(int formId);

	Question findByColumnnAndFormFormId(String dependentColumn, int numericCellValue);

//	List<Question> findByReviewHeaderTrueAndIsLiveTrueAndFormsIsLiveTrue();

	@Query("SELECT qs.form.formId , qs.reviewName ,qs.columnn,qs.defaultSetting FROM Question qs WHERE qs.reviewHeader = TRUE AND qs.isLive= TRUE AND  "
			+ " qs.form.isLive = TRUE")
	List<Object[]> findReviewHeader();
	
	
	@Query("SELECT qs.form.formId , qs.reviewName ,qs.columnn,qs.defaultSetting FROM Question qs WHERE qs.reviewHeader = TRUE AND qs.isLive= TRUE AND  "
			+ " qs.form.isLive = TRUE AND qs.form.formId = :formId")
	List<Object[]> findReviewHeaderByFormId(@Param("formId")int formId);
}
