/**
 * 
 */
package org.sdrc.scps.controller;

import java.util.List;
import java.util.Map;

import org.sdrc.scps.domain.Attachment;
import org.sdrc.scps.models.CCILandingPageModel;
import org.sdrc.scps.models.DataEntryQuestionModel;
import org.sdrc.scps.models.QuestionModel;
import org.sdrc.scps.models.ResponseModel;
import org.sdrc.scps.service.DataEntryService;
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
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */

@RestController
public class DataEntryController {

	@Autowired
	private DataEntryService dataEntryService;

	@PreAuthorize("hasAuthority('dataentry')")
	@GetMapping("getQuestion")
	ResponseEntity<List<DataEntryQuestionModel>> getQuestion(
			int formId,
			OAuth2Authentication auth,
			@RequestParam(name = "submissioId", required = false) Integer submissioId) {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(
					dataEntryService.getQuestion(formId, auth, submissioId));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(null);
		}

	}

	@PreAuthorize("hasAnyAuthority('dataentry','dashboard')")
	@GetMapping("getViewData")
	ResponseEntity<List<DataEntryQuestionModel>> getViewData(
			int formId,
			OAuth2Authentication auth,
			@RequestParam(name = "submissioId", required = false) Integer submissioId) {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(
					dataEntryService.getViewData(formId, auth, submissioId));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(null);
		}

	}

	@PreAuthorize("hasAuthority('dataentry')")
	@GetMapping("getLandingData")
	ResponseEntity<CCILandingPageModel> getLandingData(OAuth2Authentication auth) {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(
					dataEntryService.getLandingPageData(auth));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(null);
		}

	}

	@PreAuthorize("hasAuthority('dataentry')")
	@PostMapping("draftInstitutionInmates")
	ResponseEntity<ResponseModel> draftInstitutionInmates(
			@RequestBody List<QuestionModel> questionModels,
			OAuth2Authentication auth) {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(
					dataEntryService.draftInstitutionInmates(questionModels,
							auth));

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(null);
		}
	}

	@PreAuthorize("hasAuthority('dataentry')")
	@PostMapping("finalizeInstitutionInmates")
	ResponseEntity<ResponseModel> finalizeInstitutionInmates(
			@RequestBody List<QuestionModel> questionModels,
			OAuth2Authentication auth) {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(
					dataEntryService.finalizeInstitutionInmates(questionModels,
							auth));

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(null);
		}
	}

	@PreAuthorize("hasAuthority('dataentry')")
	@PostMapping("uploadFile")
	public ResponseEntity<Attachment> handleFileUpload(
			@RequestParam("file") MultipartFile file,
			@RequestParam("formId") int formId,
			@RequestParam("columnName") String columnName) {

		try {
			return ResponseEntity.status(HttpStatus.OK).body(
					dataEntryService.uploadFile(file, formId, columnName));

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(
					null);
		}
	}

	@PreAuthorize("hasAuthority('dataentry')")
	@GetMapping("getInstitutionLandingData")
	ResponseEntity<Map<String, Object>> getInstitutionLandingData(
			OAuth2Authentication auth) {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(
					dataEntryService.getInstitutionLandingData(auth));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(null);
		}

	}

	@PreAuthorize("hasAuthority('dataentry')")
	@GetMapping("deleteSubmission")
	ResponseEntity<ResponseModel> deleteSubmission(@RequestParam int formId,
			@RequestParam int submissionId, OAuth2Authentication auth) {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(dataEntryService.deleteSubmission(formId, auth, submissionId));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(null);
		}
	}

}
