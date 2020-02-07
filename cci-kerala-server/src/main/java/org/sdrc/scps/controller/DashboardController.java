/**
 * 
 */
package org.sdrc.scps.controller;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sdrc.scps.models.DashboardLandingPageData;
import org.sdrc.scps.models.DataEntryQuestionModel;
import org.sdrc.scps.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
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
public class DashboardController {

	@Autowired
	private DashboardService dashboardService;

	@PreAuthorize("hasAuthority('dashboard')")
	@GetMapping("getDashboardData")
	ResponseEntity<DashboardLandingPageData> getDashboardData(OAuth2Authentication auth,@RequestParam("areaId")int areaId,int areaLevelId) {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(dashboardService.getLandingPageData(auth,areaId,areaLevelId));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@PreAuthorize("hasAuthority('dashboard')")
	@GetMapping("getDashboardInmateData")
	ResponseEntity<DashboardLandingPageData> getDashboardInmateData(OAuth2Authentication auth, Integer cciId,String sqft) {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(dashboardService.getDashboardInmateData(auth, cciId,sqft));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@PreAuthorize("hasAnyAuthority('dataentry','dashboard')")
	@PostMapping("exportSubmissionToPDF")
	ResponseEntity<InputStreamResource> getViewData(@RequestBody List<DataEntryQuestionModel> dataEntryModels, OAuth2Authentication auth, HttpServletResponse response,
			HttpServletRequest request) {
		String filePath="";
		try {
			 filePath = dashboardService.generatePDF(dataEntryModels, auth, response, request);
			File file = new File(filePath);

			HttpHeaders respHeaders = new HttpHeaders();
			respHeaders.add("Content-Disposition", "attachment; filename=" + file.getName());
			InputStreamResource isr = new InputStreamResource(new FileInputStream(file));
			
			new File(filePath).delete();
			return new ResponseEntity<InputStreamResource>(isr, respHeaders, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}


	}
}
