/**
 * 
 */
package org.sdrc.scps.controller;

import java.util.List;
import java.util.Map;

import org.sdrc.scps.domain.NewsUpdate;
import org.sdrc.scps.models.NewsUpdateModel;
import org.sdrc.scps.models.ResponseModel;
import org.sdrc.scps.service.NewsUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Harsh Pratyush
 *
 */

@RestController
public class NewsController {

	@Autowired
	private NewsUpdateService newsUpdateService;

	@GetMapping("getNewsUpdate")
	List<NewsUpdate> getNewsUpdate() {
		return newsUpdateService.getNewsUpdate();
	}

	@PreAuthorize("hasAuthority('news_update')")
	@GetMapping("getNewsTable")
	Map<String, Object> getNewsTable() {
		return newsUpdateService.getNewsTable();
	}

	@PreAuthorize("hasAuthority('news_update')")
	@PostMapping("saveNews")
	ResponseEntity<ResponseModel> saveUpdateNew(@RequestBody NewsUpdateModel newsUpdateModel,OAuth2Authentication auth) {
		return ResponseEntity.status(HttpStatus.OK).body(newsUpdateService.updateSaveNews(newsUpdateModel,auth));
	}
	
	
	@PreAuthorize("hasAuthority('news_update')")
	@GetMapping("deleteNews")
	ResponseEntity<ResponseModel> deleteNews(@RequestParam("newsId")int newsId,OAuth2Authentication auth) {
		return ResponseEntity.status(HttpStatus.OK).body(newsUpdateService.deleteNews(newsId, auth));
	}

}
