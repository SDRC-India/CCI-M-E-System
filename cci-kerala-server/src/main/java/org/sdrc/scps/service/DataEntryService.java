/**
 * 
 */
package org.sdrc.scps.service;

import java.util.List;
import java.util.Map;

import org.sdrc.scps.domain.Attachment;
import org.sdrc.scps.models.CCILandingPageModel;
import org.sdrc.scps.models.DataEntryQuestionModel;
import org.sdrc.scps.models.PostSubmissionModel;
import org.sdrc.scps.models.QuestionModel;
import org.sdrc.scps.models.ResponseModel;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * @author Harsh Pratyush
 *
 */
public interface DataEntryService {
	
	 
	/**
	 * This method will return the question list according to formId
	 * 
	 * @param formId
	 * @param auth 
	 * @param childId 
	 * @return
	 * @throws JsonProcessingException 
	 * @throws Exception 
	 */
	public List<DataEntryQuestionModel> getQuestion(int formId, OAuth2Authentication auth, Integer submissioId) throws Exception;
	
	
	
	/**
	 * 
	 * @param auth
	 * @return
	 */
	public CCILandingPageModel getLandingPageData(OAuth2Authentication auth);
	
	
	/**
	 * 
	 * @param questionModel
	 * @return
	 */
	public ResponseModel draftInstitutionInmates(List<QuestionModel> questionModel,OAuth2Authentication auth);


	/**
	 * 
	 * @param questionModel
	 * @param auth
	 * @return
	 */
	
	public ResponseModel finalizeInstitutionInmates(List<QuestionModel> questionModel,OAuth2Authentication auth);
	
	/**
	 * This method will upload the file
	 * @param file
	 * @param columnName 
	 * @return
	 * @throws Exception
	 */
	public Attachment uploadFile(MultipartFile file,int formId, String columnName) throws Exception;


	/**
	 * 
	 * @param auth
	 * @return
	 */

	public Map<String,Object> getInstitutionLandingData(OAuth2Authentication auth);



	/**
	 * 
	 * @param formId
	 * @param auth
	 * @param submissioId
	 * @return
	 * @throws Exception
	 */
	public List<DataEntryQuestionModel> getViewData(int formId, OAuth2Authentication auth, Integer submissioId) throws Exception;

	
	public ResponseModel deleteSubmission(int formId, OAuth2Authentication auth, Integer submissioId);



	/**
	 * @param postSubmissionModel
	 */
	public void postSubmissionWork(PostSubmissionModel postSubmissionModel);
}
