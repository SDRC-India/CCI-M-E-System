/**
 * 
 */
package org.sdrc.scps.service;

import java.util.List;
import java.util.Map;

import org.sdrc.scps.domain.NewsUpdate;
import org.sdrc.scps.models.NewsUpdateModel;
import org.sdrc.scps.models.ResponseModel;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

/**
 * @author Harsh Pratyush
 *
 */
public interface NewsUpdateService {

	/**
	 * 
	 * @return List<NewsUpdate> {@link NewsUpdate}
	 */
	public List<NewsUpdate> getNewsUpdate();

	/**
	 * 
	 * @param NewsUpdateModel
	 * @return ResponseModel
	 */
	public ResponseModel updateSaveNews(NewsUpdateModel newsUpdateModel,OAuth2Authentication auth);
	
	
	/**
	 * 
	 * @return Map<String,Object>
	 */
	public Map<String,Object> getNewsTable();
	
	
	public ResponseModel deleteNews(int newsId,OAuth2Authentication auth);

}
