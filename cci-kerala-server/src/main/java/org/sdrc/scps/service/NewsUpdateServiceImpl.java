/**
 * 
 */
package org.sdrc.scps.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.sdrc.scps.domain.NewsUpdate;
import org.sdrc.scps.models.NewsUpdateModel;
import org.sdrc.scps.models.ResponseModel;
import org.sdrc.scps.repository.NewsUpdateRepository;
import org.sdrc.scps.util.TokenInfoExtractor;
import org.sdrc.usermgmt.domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Harsh Pratyush
 *
 */

@Service
public class NewsUpdateServiceImpl implements NewsUpdateService {

	@Autowired
	private NewsUpdateRepository newsUpdateRepository;
	
	
	@Autowired
	private TokenInfoExtractor tokenInfoExtractor;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/YYYY");

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sdrc.scps.service.NewsUpdateService#getNewsUpdate()
	 */
	@Override
	public List<NewsUpdate> getNewsUpdate() {
		return newsUpdateRepository.findByIsLiveTrueOrderByUpdatedDateDesc();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sdrc.scps.service.NewsUpdateService#updateSaveNews(org.sdrc.scps.models.
	 * NewsUpdateModel)
	 */
	@Override
	@Transactional
	public ResponseModel updateSaveNews(NewsUpdateModel newsUpdateModel,OAuth2Authentication auth) {

		ResponseModel responseModel = new ResponseModel();
		if(newsUpdateModel.getNewsUpdateId() == 0)
		{
			if(newsUpdateRepository.findByIsLiveTrue().size()>=5)
			{
				responseModel.setStatusCode(HttpStatus.BAD_REQUEST.value());
				responseModel.setMessage("You can add maxium 5 news at a time. Please delete some old news");
				return responseModel;
			}	
		}
		NewsUpdate newsUpdate = new NewsUpdate();
		newsUpdate.setLive(true);
		newsUpdate.setNewsTitle(newsUpdateModel.getNewsTitle());
		newsUpdate.setNewsUrl(newsUpdateModel.getNewsUrl());
		
		if (newsUpdateModel.getNewsUpdateId() != 0) {
			newsUpdate.setNewsUpdateId(newsUpdateModel.getNewsUpdateId());	
			newsUpdate.setUpdatedBy(
					new Account(Integer.parseInt(tokenInfoExtractor.tokenInfo(auth).get("userId").toString())));
			responseModel.setMessage("News Updated Successfully");
		}
		
		else
		{
			newsUpdate.setCreatedBy(
					new Account(Integer.parseInt(tokenInfoExtractor.tokenInfo(auth).get("userId").toString())));
			responseModel.setMessage("News Added Successfully");
		}
		
		responseModel.setStatusCode(HttpStatus.OK.value());
	
		newsUpdateRepository.save(newsUpdate);
		
		return responseModel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sdrc.scps.service.NewsUpdateService#getNewsTable()
	 */
	@Override
	public Map<String, Object> getNewsTable() {

		List<NewsUpdate> newsUpdates = newsUpdateRepository.findByIsLiveTrueOrderByUpdatedDateDesc();

		Map<String, Object> newsTableMap = new LinkedHashMap<String, Object>();

		List<String> tableHeader = new ArrayList<String>();

		tableHeader.add("Created On");
		tableHeader.add("Updated On");
		tableHeader.add("Title");
		tableHeader.add("URL");
		tableHeader.add("Action");

		newsTableMap.put("tableHeader", tableHeader);

		List<Map<String, Object>> tableDataList = new ArrayList<Map<String, Object>>();

		for (NewsUpdate newsUpdate : newsUpdates) {
			Map<String, Object> tableData = new LinkedHashMap<String, Object>();
			tableData.put("Created On", sdf.format(newsUpdate.getCreatedDate()));
			tableData.put("Updated On", sdf.format(newsUpdate.getUpdatedDate()));
			tableData.put("Title", newsUpdate.getNewsTitle());
			

//			List<Map<String, String>> actionDetails = new ArrayList<Map<String, String>>();
//			Map<String, String> actionDetailsMap = new LinkedHashMap<String, String>();
//			actionDetailsMap.put("controlType", "link");
//			actionDetailsMap.put("value",
//					newsUpdate.getNewsUrl());
//			actionDetailsMap.put("class", "approved-child");
//			actionDetailsMap.put("tooltip", "View Child Data");
//			actionDetails.add(actionDetailsMap);
			tableData.put("URL",
					newsUpdate.getNewsUrl());
		
			
//			tableData.put("URL", newsUpdate.getNewsUrl());
			tableData.put("Action", getAction(true));
			tableData.put("id", newsUpdate.getNewsUpdateId());
			
			tableDataList.add(tableData);
		}
		newsTableMap.put("tableData", tableDataList);
		return newsTableMap;
	}

	private List<Map<String, String>> getAction(boolean check) {
		List<Map<String, String>> actionDetails = new ArrayList<Map<String, String>>();
		Map<String, String> actionDetailsMap = new LinkedHashMap<String, String>();

		actionDetailsMap = new LinkedHashMap<String, String>();
		actionDetailsMap.put("controlType", "button");
		actionDetailsMap.put("value", "");
		actionDetailsMap.put("type", "submit");
		actionDetailsMap.put("class", "btn btn-submit edit");
		actionDetailsMap.put("tooltip", "Edit");
		actionDetailsMap.put("icon", "fa-edit");
		actionDetails.add(actionDetailsMap);

		if (check) {
			actionDetailsMap = new LinkedHashMap<String, String>();
			actionDetailsMap.put("controlType", "button");
			actionDetailsMap.put("value", "");
			actionDetailsMap.put("type", "submit");
			actionDetailsMap.put("class", "btn btn-submit delete");
			actionDetailsMap.put("tooltip", "Delete");
			actionDetailsMap.put("icon", "fa-trash");
			actionDetails.add(actionDetailsMap);

		}

		return actionDetails;
	}

	/* (non-Javadoc)
	 * @see org.sdrc.scps.service.NewsUpdateService#deleteNews(int)
	 */
	@Override
	@Transactional
	public ResponseModel deleteNews(int newsUpdateId,OAuth2Authentication auth) {
			NewsUpdate newsUpdate=newsUpdateRepository.findByNewsUpdateId(newsUpdateId);
			ResponseModel responseModel = new ResponseModel();
			if(newsUpdate!=null)
			{
				newsUpdate.setUpdatedBy(
						new Account(Integer.parseInt(tokenInfoExtractor.tokenInfo(auth).get("userId").toString())));
				newsUpdate.setLive(false);
				responseModel.setStatusCode(HttpStatus.OK.value());
				responseModel.setMessage("News Deleted");
				return responseModel;
			}
			else
			{
				responseModel.setStatusCode(HttpStatus.BAD_REQUEST.value());
				responseModel.setMessage("No Data Found");
				return responseModel;
			}
			
			
	}

}
