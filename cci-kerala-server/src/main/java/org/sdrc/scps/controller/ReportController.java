/**
 * 
 */
package org.sdrc.scps.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.sdrc.scps.domain.Area;
import org.sdrc.scps.models.SummaryReportModel;
import org.sdrc.scps.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */
@RestController
public class ReportController {
	
	@Autowired
	private ReportService reportService;

	
	/**
	 * @author Sourav Keshari Nath Generate the excel sheet
	 */
	@PreAuthorize("hasAnyAuthority('report')")
	@PostMapping(value = "/downloadInmatesRawData")
	public void generateExcelTemplate(@RequestParam("districtId") int districtId,HttpServletResponse httpServletResponse,OAuth2Authentication auth) throws IOException {

		File file = reportService.generateInmatesRawData(auth,districtId);
		try {
			String mimeType;
			mimeType = "application/octet-stream";
			httpServletResponse.setContentType(mimeType);
			httpServletResponse.setHeader("Content-Disposition",
					String.format("attachment; filename=\"%s\"", file.getName()));
			httpServletResponse.setContentLength((int) file.length());
			InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
			FileCopyUtils.copy(inputStream, httpServletResponse.getOutputStream());

		} finally {
			httpServletResponse.getOutputStream().close();
			if (file != null) {
				file.delete();
			}
		}
	}
	@PreAuthorize("hasAuthority('report')")
	@GetMapping("getDistrict")
	ResponseEntity<List<Area>> getDistrict(OAuth2Authentication auth) {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(reportService.getDistrict(auth));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@PreAuthorize("hasAuthority('report')")
	@GetMapping("getSummaryReportTable")
	ResponseEntity<SummaryReportModel> getSummaryReportTable(OAuth2Authentication auth,@RequestParam("areaId")int areaId,int areaLevelId) {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(reportService.getSummaryReportTable(auth,areaId,areaLevelId));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
}
