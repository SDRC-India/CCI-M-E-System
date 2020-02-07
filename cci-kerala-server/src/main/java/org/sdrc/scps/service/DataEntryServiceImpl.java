package org.sdrc.scps.service;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.json.simple.JSONObject;
import org.sdrc.scps.domain.Area;
import org.sdrc.scps.domain.Attachment;
import org.sdrc.scps.domain.BuildingDetails;
import org.sdrc.scps.domain.GrantInAidDetails;
import org.sdrc.scps.domain.InmatesData;
import org.sdrc.scps.domain.InmatesIdGeneration;
import org.sdrc.scps.domain.InmatesPhoto;
import org.sdrc.scps.domain.InstitutionData;
import org.sdrc.scps.domain.ManagementDetails;
import org.sdrc.scps.domain.Options;
import org.sdrc.scps.domain.PoliceCaseReports;
import org.sdrc.scps.domain.Question;
import org.sdrc.scps.domain.SocialAuditReports;
import org.sdrc.scps.domain.StaffDetails;
import org.sdrc.scps.domain.User;
import org.sdrc.scps.models.CCILandingPageModel;
import org.sdrc.scps.models.DataEntryQuestionModel;
import org.sdrc.scps.models.OptionModel;
import org.sdrc.scps.models.PostSubmissionModel;
import org.sdrc.scps.models.QuestionModel;
import org.sdrc.scps.models.ResponseModel;
import org.sdrc.scps.repository.AttachmentRepository;
import org.sdrc.scps.repository.BuildingDetailsRepository;
import org.sdrc.scps.repository.GrantInAidDetailsRepository;
import org.sdrc.scps.repository.InmatesDataRepository;
import org.sdrc.scps.repository.InmatesIdGenerationRepository;
import org.sdrc.scps.repository.InmatesPhotoRepository;
import org.sdrc.scps.repository.InstitutionDataRepository;
import org.sdrc.scps.repository.ManagementsDetailsRepository;
import org.sdrc.scps.repository.PoliceCaseReportsRepository;
import org.sdrc.scps.repository.QuestionRepository;
import org.sdrc.scps.repository.SocialAuditReportsRepository;
import org.sdrc.scps.repository.StaffDetailsRepository;
import org.sdrc.scps.repository.UserRepository;
import org.sdrc.scps.util.Constants;
import org.sdrc.scps.util.PostSubmissionMailThread;
import org.sdrc.scps.util.SubmissionStatus;
import org.sdrc.scps.util.TokenInfoExtractor;
import org.sdrc.usermgmt.domain.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

/**
 * 
 * @author Harsh Pratyush
 *
 */
@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DataEntryServiceImpl implements DataEntryService {

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private TokenInfoExtractor tokenInfoExtractor;

	@Autowired
	private InstitutionDataRepository institutionDataRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private InmatesDataRepository inmatesDataRepository;

	@Autowired
	private AttachmentRepository attachmentRepository;

	@Autowired
	private InmatesIdGenerationRepository inmatesIdGenerationRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ManagementsDetailsRepository managementsDetailsRepository;

	@Autowired
	private StaffDetailsRepository staffDetailsRepository;

	@Autowired
	private PoliceCaseReportsRepository policeCaseReportsRepository;

	@Autowired
	private GrantInAidDetailsRepository grantInAidDetailsRepository;

	@Autowired
	private SocialAuditReportsRepository socialAuditReportsRepository;

	@Autowired
	private InmatesPhotoRepository inmatesPhotoRepository;

//	@Autowired
//	private PostSubmissionMailThread postSubmissionMailThread;

	@Autowired
	public JavaMailSender emailSender;

	@Autowired
	private BuildingDetailsRepository buildingDetailsRepository;

	private final Path instPath = Paths.get(Constants.INSTITUTION_DIRC);
	private final Path inmatesPath = Paths.get(Constants.INMATES_DIRC);

	private final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY");

	private final SimpleDateFormat sdfDateType = new SimpleDateFormat("yyyy-MM-dd");

	private static final Logger logger = LoggerFactory.getLogger(PostSubmissionMailThread.class);

	@Override
	public List<DataEntryQuestionModel> getQuestion(int formId, OAuth2Authentication auth, Integer submissioId)
			throws Exception {

		boolean disabled = false;
		boolean deceased = false;
		boolean submitDisabled=false;
		QuestionModel questionModel = new QuestionModel();
		List<DataEntryQuestionModel> dataEntryQuestionModels = new ArrayList<DataEntryQuestionModel>();
		questionModel.setControlType("id");
		questionModel.setColumnName("id");
		questionModel.setKey(formId);
		String rejectedReason = "";
		InstitutionData approvedInstitution = null;

		// getting questions for a formId
		List<Question> questions = questionRepository.findByFormFormIdAndIsLiveTrueOrderByQuestionOrderAsc(formId);

		Map<String, DataEntryQuestionModel> dataEntryQuestionModelMap = new LinkedHashMap<String, DataEntryQuestionModel>();

		Object dataFromSession = tokenInfoExtractor.getUserModelInfo(auth);

		Map<String, Object> prefetchedData = new HashMap<String, Object>();

		JSONObject jsonObject = new JSONObject((Map) dataFromSession);
		// getting cci id
		Integer cciId = Integer.parseInt(((new JSONObject((Map) jsonObject.get("cci"))).get("areaId")).toString());
		Set<Integer> rejectedSectionIds = new HashSet<Integer>();

		List<Attachment> attachments = new ArrayList<Attachment>();
		if (formId == Constants.INSTUTION_FORM) {

			InstitutionData institutionData = null;

			// if data is request is for any specific submission we will fetch it
			if (submissioId != null && submissioId != 0)
				institutionData = institutionDataRepository.findByIsLiveTrueAndInstutionId(submissioId);

			// we will fetch the latest approved data for the user beloging to the cci
			else
				institutionData = institutionDataRepository
						.findByIsLiveTrueAndSubmissionStatusAndQ7AreaId(SubmissionStatus.APPROVED, cciId);

			// if submission status is approved then
			if (institutionData == null || institutionData.getSubmissionStatus() == SubmissionStatus.APPROVED) {
				// we will check for any later rejected data
				InstitutionData institutionDataRejected = institutionDataRepository
						.findByIsLiveTrueAndSubmissionStatusAndQ7AreaId(SubmissionStatus.REJECTED, cciId);

				// we will check for draft value also
				InstitutionData institutionDataDraft = institutionDataRepository
						.findByIsLiveTrueAndSubmissionStatusAndQ7AreaId(SubmissionStatus.DRAFT, cciId);

				// if draft is available user will get draft version
				if (institutionDataDraft != null)
					institutionData = institutionDataDraft;
				
				// if rejected available user will get the rejected version
				else if (institutionDataRejected != null)
					institutionData = institutionDataRejected;

			}

			if (institutionData != null) {

				// if not approved we will set the value in id column to we will update the current version if neeeded to update
				if (institutionData.getSubmissionStatus() != SubmissionStatus.APPROVED) {
					questionModel.setValue(institutionData.getInstutionId());
				}

				// if there is any submission pending for the approval then user will get the form as disabled and he cannot procced further
				InstitutionData pendingInstitution = institutionDataRepository
						.findByIsLiveTrueAndSubmissionStatusAndQ7AreaId(SubmissionStatus.PENDING, cciId);
				if (pendingInstitution != null)
					disabled = true;

				// if rejected we will get the rejected remarks
				if (institutionData.getSubmissionStatus() == SubmissionStatus.REJECTED
						&& institutionData.getRejectedSections() != null) {
					rejectedSectionIds = Arrays.asList(institutionData.getRejectedSections().split(",")).stream()
							.map(Integer::parseInt).collect(Collectors.toSet());
					rejectedReason = institutionData.getRemarks() == null ? "" : institutionData.getRemarks();
				}
				attachments = institutionData.getAttachments();
				
				// getting data in a map if we are having any data with us in Map<String,Object> as key= columnName and Value=value in id or string or number
				prefetchedData = inspect(InstitutionData.class, institutionData);
			} else {
				InstitutionData pendingInstitution = institutionDataRepository
						.findByIsLiveTrueAndSubmissionStatusAndQ7AreaId(SubmissionStatus.PENDING, cciId);
				if (pendingInstitution != null)
					disabled = true;
			}

			// getting the current strength of institution
			prefetchedData.put("q26",
					String.valueOf(inmatesDataRepository.getStrengthForCCI(cciId, SubmissionStatus.APPROVED)));
		} else if (formId == Constants.CHILD_FORM && submissioId != null && submissioId != 0) {

			// if particular submission requested
			InmatesData inmatesData = inmatesDataRepository.findByIsLiveTrueAndInmatesData(submissioId);

			if (inmatesData != null && inmatesData.getSubmissionStatus() == SubmissionStatus.APPROVED) {

				// checking wethere inmate is deceased or not
				List<InmatesData> inmateDeceased = inmatesDataRepository.findBySubmissionStatusAndQ64OptionIdAndQ1(
						SubmissionStatus.APPROVED, Constants.OPTION_YES, inmatesData.getQ1());
				if (inmateDeceased != null && inmateDeceased.size() > 0) {
					deceased = true;
				}
				
				// getting draft version
				InmatesData draftInmatesData = inmatesDataRepository
						.findByIsLiveTrueAndQ1AndSubmissionStatus(inmatesData.getQ1(), SubmissionStatus.DRAFT);
				InmatesData rejectInmatesData = inmatesDataRepository
						.findByIsLiveTrueAndQ1AndSubmissionStatus(inmatesData.getQ1(), SubmissionStatus.REJECTED);

				// if draft version found user will get draft version
				if (draftInmatesData != null) {
					inmatesData = draftInmatesData;
					// if rejected version found then user will get the rejected version of the form and he/she will work on rejected version
				} else if (rejectInmatesData != null) {

					inmatesData = rejectInmatesData;
				}

			}
			// getting rejected remarks if any

			if (inmatesData != null && inmatesData.getRejectedSections() != null
					&& inmatesData.getSubmissionStatus() == SubmissionStatus.REJECTED) {
				rejectedSectionIds = Arrays.asList(inmatesData.getRejectedSections().split(",")).stream()
						.map(Integer::parseInt).collect(Collectors.toSet());
				rejectedReason = inmatesData.getRemarks() == null ? "" : inmatesData.getRemarks();
			}

			if (inmatesData != null) {
				if (inmatesData.getSubmissionStatus() != SubmissionStatus.APPROVED) {
					questionModel.setValue(inmatesData.getInmatesData());
				}
				{
					
					if (inmatesData.getSubmissionStatus() == SubmissionStatus.APPROVED && inmatesData.getQ50() != null
							&& inmatesData.getQ50().getOptionId() == Constants.OPTION_YES
							&& inmatesData.getQ56() == null) {
						disabled = true;
					}

				}

				InmatesData pendingInmatesData = inmatesDataRepository
						.findByIsLiveTrueAndQ1AndSubmissionStatus(inmatesData.getQ1(), SubmissionStatus.PENDING);
				if (pendingInmatesData != null)
					disabled = true;
				attachments = inmatesData.getAttachment();
				prefetchedData = inspect(InmatesData.class, inmatesData);
			} else {
				InmatesIdGeneration inmatesIdGeneration = inmatesIdGenerationRepository.findByAreaAreaId(cciId);
				if (inmatesIdGeneration == null) {
					inmatesIdGeneration = new InmatesIdGeneration();
					inmatesIdGeneration.setArea(new Area(cciId));
					inmatesIdGeneration.setLastId(
							((new JSONObject((Map) jsonObject.get("cci"))).get("areaCode")).toString() + "001");

				} else {
					// genereating a new id of inmates for a fresh entry
					String lastId = inmatesIdGeneration.getLastId();
					int lastNum = Integer.parseInt(
							lastId.split((new JSONObject((Map) jsonObject.get("cci"))).get("areaCode").toString())[1]);
					lastNum++;
					lastId = (new JSONObject((Map) jsonObject.get("cci"))).get("areaCode").toString()
							+ String.format("%03d", lastNum);
					inmatesIdGeneration.setLastId(lastId);
				}

				inmatesIdGeneration = inmatesIdGenerationRepository.save(inmatesIdGeneration);

			}
			
			//getting the approved buildings for the cci which inmate belongs
			approvedInstitution = institutionDataRepository
					.findByIsLiveTrueAndSubmissionStatusAndQ7AreaId(SubmissionStatus.APPROVED, cciId);
			if (prefetchedData.get("qBuildingDetails") != null
					&& prefetchedData.get("qBuildingDetails").toString().trim() != "") {
				String value = prefetchedData.get("qBuildingDetails").toString();
				prefetchedData.put("qBuildingDetails",
						approvedInstitution.getBuildingDetails().stream()
								.filter(d -> Integer.parseInt(value) == d.getIndexTrackNum()).findFirst().get()
								.getBuildingDetailsId());
			}
			
			if(approvedInstitution==null)
				submitDisabled=true;
				
		} else if (formId == Constants.CHILD_FORM && submissioId == 0) {

			
			approvedInstitution = institutionDataRepository
					.findByIsLiveTrueAndSubmissionStatusAndQ7AreaId(SubmissionStatus.APPROVED, cciId);

			InmatesIdGeneration inmatesIdGeneration = inmatesIdGenerationRepository.findByAreaAreaId(cciId);
			if (inmatesIdGeneration == null) {
				inmatesIdGeneration = new InmatesIdGeneration();
				inmatesIdGeneration.setArea(new Area(cciId));
				inmatesIdGeneration
						.setLastId(((new JSONObject((Map) jsonObject.get("cci"))).get("areaCode")).toString() + "001");
			} else {
				
				// genereating a new id of inmates for a fresh entry
				String lastId = inmatesIdGeneration.getLastId();
				int lastNum = Integer.parseInt(
						lastId.split((new JSONObject((Map) jsonObject.get("cci"))).get("areaCode").toString())[1]);
				lastNum++;
				lastId = (new JSONObject((Map) jsonObject.get("cci"))).get("areaCode").toString()
						+ String.format("%03d", lastNum);
				inmatesIdGeneration.setLastId(lastId);
			}
			inmatesIdGeneration = inmatesIdGenerationRepository.save(inmatesIdGeneration);
			prefetchedData.put("q1", inmatesIdGeneration.getLastId());
			
			if(approvedInstitution==null)
				submitDisabled=true;
		}

		DataEntryQuestionModel dataEntryQuestionModel = new DataEntryQuestionModel();
		dataEntryQuestionModel.setId(questions.get(0).getSection().getSectionId());
		if (rejectedSectionIds.contains(dataEntryQuestionModel.getId()))
			dataEntryQuestionModel.setRejected(true);
		else
			dataEntryQuestionModel.setRejected(false);

		dataEntryQuestionModel.setRejectedRemark(rejectedReason);

		dataEntryQuestionModel.setName(questions.get(0).getSection().getSectionName());
		dataEntryQuestionModel.setSectionOrder(questions.get(0).getSection().getSectionOrder());
		dataEntryQuestionModel.setSubmitDisabled(submitDisabled);
		List<QuestionModel> questionModels = new ArrayList<QuestionModel>();
		questionModels.add(questionModel);
		dataEntryQuestionModel.setQuestions(questionModels);
		dataEntryQuestionModel.setDisabled(disabled);
		dataEntryQuestionModelMap.put(dataEntryQuestionModel.getName(), dataEntryQuestionModel);

		for (Question question : questions) {

			if (question.isApprovalProcess())
				continue;

			questionModel = new QuestionModel();
			questionModel.setAllChecked(false);
			questionModel.setColumnName(question.getColumnn());
			questionModel.setControlType(question.getControlType());
			questionModel.setType(question.getInputType());
			if (question.getFileExtensions() != null)
				questionModel.setFileExtension(question.getFileExtensions().split(","));
			if (question.getDefaultSetting() != null)
				questionModel.setDisabled(question.getDefaultSetting().contains("disabled"));
			else
				questionModel.setDisabled(question.isApprovalProcess());

			if (!questionModel.isDisabled())
				questionModel.setDisabled(disabled);

			if (!questionModel.isDisabled()) {
				questionModel.setDisabled(deceased
						? question.getSection().getSectionId() == Constants.DECEASED_SECTION
								&& !question.getColumnn().trim().equals(Constants.DECEASED_QUESTON_NAME) ? false : true
						: false);
			}

			if (question.getDependecy()) {
				questionModel.setDependentCondition(question.getDependentCondition().split(","));
				questionModel.setParentColumns(question.getDependentColumn().split(","));
			}

			if (question.getControlType() != "beginRepeat" && question.getGroupId() == null)
				questionModel.setValue(prefetchedData.get(question.getColumnn()) != null
						? prefetchedData.get(question.getColumnn()).toString()
						: null);

			if (question.getControlType().equals("multiSelect"))
				questionModel.setValue(prefetchedData.get(question.getColumnn()) != null
						? Arrays.asList(prefetchedData.get(question.getColumnn()).toString().split(",")).stream()
								.mapToInt(Integer::parseInt).toArray()
						: null);

			if (question.getControlType().equals("file") && prefetchedData.get(question.getColumnn()) != null) {

				// String[] attachmentsStrings =
				// prefetchedData.get(question.getColumnn()).toString().split(",");
				List<Attachment> attachmentQuestion = new ArrayList<Attachment>();
				// for (String attachmentString : attachmentsStrings)
				{
					// System.out.println(attachmentString);
					attachmentQuestion.addAll(attachments.stream()
							.filter(d -> d.getColumnName().equals(question.getColumnn())).collect(Collectors.toList()));
				}
				questionModel.setValue(attachmentQuestion);
				questionModel.setFileValues(prefetchedData.get(question.getColumnn()).toString().split(","));
			}

			questionModel.setCurrentDate(sdfDateType.format(new Date()));
			questionModel.setPlaceHolder(question.getPlaceHolder() == null ? "" : question.getPlaceHolder());
			questionModel.setFileSize((double) 102400000);
			questionModel.setKey(question.getQuestionId());
			questionModel
					.setSerialNumb(question.getSection().getSectionOrder() + "." + question.getQuestionSerial() + "  ");
			questionModel.setLabel(question.getQuestionName() != null ? question.getQuestionName() : "");
			if (question.getConstraints() != null) {
				for (String s : question.getConstraints().split(",")) {
					if (s.contains("minlength=")) {
						questionModel.setMinLength(Integer.parseInt(s.split("minlength=")[1].trim()));
					}

					if (s.contains("maxlength=")) {
						questionModel.setMaxLength(Integer.parseInt(s.split("maxlength=")[1].trim()));
					}

					if (s.contains("minDate=")) {
						if (s.split("minDate=")[1].trim() == "today")
							questionModel.setMinDate(sdfDateType.format(new Date()));
						else if (s.split("minDate=")[1].trim().contains("Yr"))
						{
							Calendar calendar = Calendar.getInstance();
							calendar.setTime(new Date());
							calendar.add(Calendar.YEAR, -Integer.parseInt(s.split("minDate=")[1].trim().split("Yr")[1]));
							questionModel.setMinDate(sdfDateType.format(calendar.getTime()));
						}
						else
							questionModel.setMinDate(s.split("minDate=")[1].trim());
					}

					if (s.contains("maxDate=")) {
						if (s.split("maxDate=")[1].trim() == "today")
							questionModel.setMaxDate(sdfDateType.format(new Date()));
						else
							questionModel.setMaxDate(s.split("maxDate=")[1].trim());
					}

					if (s.contains("maxSize")) {
						questionModel.setFileSize(Double.parseDouble(s.split("maxSize=")[1].trim()));
					}
					
					if (s.contains("max=")) {
						questionModel.setMax(Integer.parseInt((s.split("max=")[1].trim())));
					}
					
				}
			}

			if (question.getInputType().contains("multiple"))
				questionModel.setMultiple(true);

			if (question.getQuestionOptionTypeMapping() != null) {
				questionModel.setOptions(formatOption(question));
			}

			if (question.getFeatures() != null && question.getFeatures().contains("fetchsession")) {
				questionModel.setOptions(formatOptionPrefectSession(question, jsonObject));
			}

			if (question.getDefaultSetting() != null && question.getDefaultSetting().contains("fetchTable")) {
				questionModel.setOptions(formatOptionPrefectTable(question));
			}

			if (question.getFeatures() != null && question.getFeatures().contains("parent")) {

				for (String feature : question.getFeatures().split(",")) {
					feature = feature.trim();
					if (feature.contains("parent:")) {
						questionModel.setOptionsParentColumn(feature.split(":")[1]);

					}
				}
			}
			
			
			if (question.getDefaultSetting() != null && question.getDefaultSetting().contains("info")) {

				for (String feature : question.getDefaultSetting().split(",")) {
					feature = feature.trim();
					if (feature.contains("info:")) {
						questionModel.setInfoMessage(feature.split(":")[1]);
						questionModel.setInfoAvailable(true);

					}
				}
			}

			if (question.getColumnn().equals("qBuildingDetails") && approvedInstitution != null) {
				List<OptionModel> options = new ArrayList<OptionModel>();

				for (BuildingDetails buildingDetails : approvedInstitution.getBuildingDetails()) {
					OptionModel option = new OptionModel();
					option.setKey(buildingDetails.getBuildingDetailsId());
					option.setValue(buildingDetails.getQ33BuildingName());

					options.add(option);
				}
				questionModel.setOptions(options);
			}

			questionModel.setRequired(question.isFinalizeMandatory());
			questionModel.setTriggable(false);

			if (question.getDefaultSetting() != null && question.getDefaultSetting().contains("prefetch")
					&& questionModel.getValue() == null
					&& !question.getDefaultSetting().contains("prefetchfetchDate")) {
				questionModel.setValue(getValuePrfecthed(question, jsonObject));
			}

			if (question.getDefaultSetting() != null && question.getDefaultSetting().contains("prefetchfetchDate")
					&& questionModel.getValue() == null) {
				questionModel.setValue(sdfDateType.format(new Date()));
			}

			if (question.getGroupId() != null) {
				questionModel.setGroupParentId(question.getGroupId());

				int minimumRepeats = 1;
				if (question.getDefaultSetting() != null && question.getDefaultSetting().contains("minimumRepeats")) {
					for (String feature : question.getDefaultSetting().split(",")) {
						feature = feature.trim();
						if (feature.contains("minimumRepeats")) {
							minimumRepeats = Integer.parseInt(feature.split(":")[1]);
						}
					}
				}

				List<?> prefetchedDataForRepeat = (List<?>) prefetchedData.get(question.getGroupId());
				if (prefetchedDataForRepeat != null && prefetchedDataForRepeat.size() > 0) {

					for (int i = 0; i < prefetchedDataForRepeat.size(); i++) {
						Map<String, Object> beginRepeatDataMap = inspect(prefetchedDataForRepeat.get(i).getClass(),
								prefetchedDataForRepeat.get(i));

						QuestionModel clonedQuestionModel = (QuestionModel) questionModel.clone();
						clonedQuestionModel.setValue(beginRepeatDataMap.get(clonedQuestionModel.getColumnName()) != null
								? beginRepeatDataMap.get(clonedQuestionModel.getColumnName()).toString()
								: null);

						if (clonedQuestionModel.getControlType().equals("multiSelect"))
							clonedQuestionModel
									.setValue(beginRepeatDataMap.get(clonedQuestionModel.getColumnName()) != null
											? beginRepeatDataMap.get(clonedQuestionModel.getColumnName()).toString()
													.split(",")
											: null);
						if (clonedQuestionModel.getControlType().equals("file")
								&& beginRepeatDataMap.get(clonedQuestionModel.getColumnName()) != null) {

							// String[] attachmentsStrings =
							// beginRepeatDataMap.get(clonedQuestionModel.getColumnName())
							// .toString().split(",");
							List<Attachment> attachmentQuestion = new ArrayList<Attachment>();
							// for (String attachmentString :
							// attachmentsStrings)
							{
								attachmentQuestion.addAll(attachments.stream()
										.filter(d -> d.getColumnName().equals(
												question.getColumnn() + "_" + beginRepeatDataMap.get("indexTrackNum")))
										.collect(Collectors.toList()));

							}
							clonedQuestionModel.setValue(attachmentQuestion);
							clonedQuestionModel
									.setFileValues(beginRepeatDataMap.get(question.getColumnn()).toString().split(","));
						}

						clonedQuestionModel.setIndexNumberTrack(
								question.getColumnn() + "_" + beginRepeatDataMap.get("indexTrackNum"));
						clonedQuestionModel.setRemovable(beginRepeatDataMap.get("removable") != null
								? Boolean.valueOf(beginRepeatDataMap.get("removable").toString())
								: true);

						if (dataEntryQuestionModelMap.get(question.getSection().getSectionName()).getQuestions()
								.stream().filter(d -> d.getColumnName().trim().equals(question.getGroupId().trim()))
								.findFirst().get().getChildQuestionModels() != null
								&& dataEntryQuestionModelMap.get(question.getSection().getSectionName()).getQuestions()
										.stream()
										.filter(d -> d.getColumnName().trim().equals(question.getGroupId().trim()))
										.findFirst().get().getChildQuestionModels().size() > i) {
							dataEntryQuestionModelMap.get(question.getSection().getSectionName()).getQuestions()
									.stream().filter(d -> d.getColumnName().trim().equals(question.getGroupId().trim()))
									.findFirst().get().getChildQuestionModels().get(i).add(clonedQuestionModel);

						}

						else {

							List<List<QuestionModel>> childQuestionModels = null;
							if (dataEntryQuestionModelMap.get(question.getSection().getSectionName()).getQuestions()
									.stream().filter(d -> d.getColumnName().trim().equals(question.getGroupId().trim()))
									.findFirst().get().getChildQuestionModels() == null) {
								childQuestionModels = new ArrayList<List<QuestionModel>>();
							} else {
								childQuestionModels = dataEntryQuestionModelMap
										.get(question.getSection().getSectionName()).getQuestions().stream()
										.filter(d -> d.getColumnName().trim().equals(question.getGroupId().trim()))
										.findFirst().get().getChildQuestionModels();
							}

							List<QuestionModel> childQuestionModel = new ArrayList<QuestionModel>();
							childQuestionModel.add(clonedQuestionModel);
							childQuestionModels.add(childQuestionModel);
							dataEntryQuestionModelMap.get(question.getSection().getSectionName()).getQuestions()
									.stream().filter(d -> d.getColumnName().trim().equals(question.getGroupId().trim()))
									.findFirst().get().setChildQuestionModels(childQuestionModels);

						}

					}

				}

				else {

					for (int i = 0; i < minimumRepeats; i++) {
						QuestionModel clonedQuestionModel = (QuestionModel) questionModel.clone();
						clonedQuestionModel.setIndexNumberTrack(question.getColumnn() + "_" + i);
						if (dataEntryQuestionModelMap.get(question.getSection().getSectionName()).getQuestions()
								.stream().filter(d -> d.getColumnName().trim().equals(question.getGroupId().trim()))
								.findFirst().get().getChildQuestionModels() != null
								&& dataEntryQuestionModelMap.get(question.getSection().getSectionName()).getQuestions()
										.stream()
										.filter(d -> d.getColumnName().trim().equals(question.getGroupId().trim()))
										.findFirst().get().getChildQuestionModels().size() > i) {
							dataEntryQuestionModelMap.get(question.getSection().getSectionName()).getQuestions()
									.stream().filter(d -> d.getColumnName().trim().equals(question.getGroupId().trim()))
									.findFirst().get().getChildQuestionModels().get(i).add(clonedQuestionModel);

						}

						else {
							List<List<QuestionModel>> childQuestionModels = null;
							if (dataEntryQuestionModelMap.get(question.getSection().getSectionName()).getQuestions()
									.stream().filter(d -> d.getColumnName().trim().equals(question.getGroupId().trim()))
									.findFirst().get().getChildQuestionModels() == null) {
								childQuestionModels = new ArrayList<List<QuestionModel>>();
							} else {
								childQuestionModels = dataEntryQuestionModelMap
										.get(question.getSection().getSectionName()).getQuestions().stream()
										.filter(d -> d.getColumnName().trim().equals(question.getGroupId().trim()))
										.findFirst().get().getChildQuestionModels();
							}

							List<QuestionModel> childQuestionModel = new ArrayList<QuestionModel>();
							childQuestionModel.add(clonedQuestionModel);
							childQuestionModels.add(childQuestionModel);
							dataEntryQuestionModelMap.get(question.getSection().getSectionName()).getQuestions()
									.stream().filter(d -> d.getColumnName().trim().equals(question.getGroupId().trim()))
									.findFirst().get().setChildQuestionModels(childQuestionModels);
						}

					}

				}

			} else {
				if (dataEntryQuestionModelMap.containsKey(question.getSection().getSectionName())) {
					dataEntryQuestionModelMap.get(question.getSection().getSectionName()).getQuestions()
							.add(questionModel);
				} else {
					dataEntryQuestionModel = new DataEntryQuestionModel();
					dataEntryQuestionModel.setId(question.getSection().getSectionId());
					if (rejectedSectionIds.contains(dataEntryQuestionModel.getId()))
						dataEntryQuestionModel.setRejected(true);
					else
						dataEntryQuestionModel.setRejected(false);

					dataEntryQuestionModel.setSubmitDisabled(submitDisabled);
					dataEntryQuestionModel.setRejectedRemark(rejectedReason);
					dataEntryQuestionModel.setDisabled(disabled);

					dataEntryQuestionModel.setName(question.getSection().getSectionName());
					dataEntryQuestionModel.setSectionOrder(question.getSection().getSectionOrder());
					questionModels = new ArrayList<QuestionModel>();
					questionModels.add(questionModel);
					dataEntryQuestionModel.setQuestions(questionModels);
					dataEntryQuestionModelMap.put(dataEntryQuestionModel.getName(), dataEntryQuestionModel);

				}
			}

		}

		dataEntryQuestionModels = new ArrayList<DataEntryQuestionModel>(dataEntryQuestionModelMap.values());

		return dataEntryQuestionModels;
	}

	private Map<String, Object> inspect(Class<?> className, Object institutionData) {
		Field[] fields = className.getDeclaredFields();
		Map<String, Object> responseMap = new HashMap<String, Object>();

		for (Field field : fields) {
			String getter = "";

			if (field.getType().getSimpleName() == "boolean" && field.getName().toLowerCase().contains("removable")) {
				getter = "is" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
			} else if (field.getType().getSimpleName() != "boolean") {
				getter = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
			}

			if (getter.equals(""))
				continue;

			try {

				Method method = className.getMethod(getter);
				Object value = "";
				value = method.invoke(institutionData);

				if (value != null) {
					if (value instanceof Area) {
						responseMap.put(field.getName(), ((Area) value).getAreaId());
						responseMap.put(field.getName() + "_Obj", ((Area) value));
					} else if (value instanceof Options) {
						responseMap.put(field.getName(), ((Options) value).getOptionId());
					} else if (value instanceof List<?>) {
						responseMap.put(field.getName(), value);
					} else {
						responseMap.put(field.getName(), value);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return responseMap;
	}

	private Map<String, Object> inspectForResponse(Class<?> className, Object institutionData) {
		Field[] fields = className.getDeclaredFields();
		Map<String, Object> responseMap = new HashMap<String, Object>();

		for (Field field : fields) {
			if (field.getType().getSimpleName() == "boolean") {
				continue;
			}

			try {
				String getter = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
				Method method = className.getMethod(getter);
				Object value = "";
				value = method.invoke(institutionData);

				if (value != null) {

					if (value instanceof Area) {
						// System.out.println(value);
						responseMap.put(field.getName(), ((Area) value).getAreaName());
					} else if (value instanceof Options) {
						// System.out.println(value);
						responseMap.put(field.getName(), ((Options) value).getOptionName());
					} else if (value instanceof List<?>) {
						responseMap.put(field.getName(), value);
					} else if (!(value instanceof Account || value instanceof User)) {
						responseMap.put(field.getName(), value);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// System.out.println(responseMap);

		return responseMap;
	}

	private List<OptionModel> formatOptionPrefectTable(Question question) {
		List<OptionModel> optionModels = new ArrayList<OptionModel>();
		for (String feature : question.getDefaultSetting().split(",")) {
			feature = feature.trim();
			if (feature.contains("fetchTable")) {
				String queryString = feature.split(":")[1];

				Query query = entityManager.createNativeQuery(queryString);
				List<Object[]> datas = query.getResultList();
				for (Object[] data : datas) {
					OptionModel optionModel = new OptionModel();
					optionModel.setKey(Integer.parseInt(data[0].toString()));
					optionModel.setSelected(false);
					optionModel.setValue(data[2].toString());
					optionModel.setOrder(Integer.parseInt(data[0].toString()));
					optionModel.setParentKey(Integer.parseInt(data[5].toString()));
					optionModel.setLive(Boolean.parseBoolean(data[3].toString()));

					optionModels.add(optionModel);
				}

			}
		}
		return optionModels;
	}

	private List<OptionModel> areaToOption(Area area) {
		List<OptionModel> optionModels = new ArrayList<OptionModel>();

		OptionModel optionModel = new OptionModel();
		optionModel.setKey(area.getAreaId());
		optionModel.setSelected(false);
		optionModel.setValue(area.getAreaName());
		optionModel.setOrder(area.getAreaId());
		optionModel.setParentKey(area.getParentArea() == null ? 0 : area.getParentArea().getAreaId());
		optionModels.add(optionModel);
		return optionModels;

	}

	private String getValuePrfecthed(Question question, JSONObject dataFromSession) {
		JSONObject parsedData = new JSONObject();
		for (String feature : question.getDefaultSetting().split(",")) {
			feature = feature.trim();
			if (feature.contains("fetchsession")) {
				String jsonPath = feature.split(":")[1];

				parsedData = dataFromSession;
				for (String path : jsonPath.split("\\.")) {
					if (parsedData.get(path) instanceof Map)

						parsedData = new JSONObject((Map) parsedData.get(path));

					else
						return parsedData.get(path).toString();
				}

			}
		}
		return parsedData.toJSONString();
	}

	private List<OptionModel> formatOptionPrefectSession(Question question, JSONObject dataFromSession) {
		List<OptionModel> optionModels = new ArrayList<OptionModel>();
		for (String feature : question.getFeatures().split(",")) {
			feature = feature.trim();
			if (feature.contains("fetchsession")) {
				String jsonPath = feature.split(":")[1];

				JSONObject parsedData = new JSONObject();
				parsedData = dataFromSession;
				for (String path : jsonPath.split("\\.")) {

					parsedData = new JSONObject((Map) parsedData.get(path));
				}
				String json_string = new Gson().toJson(parsedData);
				Area area = new Gson().fromJson(json_string, Area.class);

				OptionModel optionModel = new OptionModel();
				optionModel.setKey(area.getAreaId());
				optionModel.setSelected(false);
				optionModel.setValue(area.getAreaName());
				optionModel.setOrder(area.getAreaId());
				optionModels.add(optionModel);

			}
		}
		return optionModels;
	}

	private List<OptionModel> formatOption(Question question) {
		List<OptionModel> optionModels = new ArrayList<OptionModel>();
		question.getQuestionOptionTypeMapping().getOptionType().getOptions().forEach(d -> {
			OptionModel optionModel = new OptionModel();
			optionModel.setKey(d.getOptionId());
			optionModel.setOrder(d.getOptionOrder());
			optionModel.setSelected(false);
			optionModel.setValue(d.getOptionName());
			optionModels.add(optionModel);

		});

		return optionModels;
	}

	@Override
	public CCILandingPageModel getLandingPageData(OAuth2Authentication auth) {

		Object dataFromSession = tokenInfoExtractor.getUserModelInfo(auth);

		JSONObject jsonObject = new JSONObject((Map) dataFromSession);
		Integer cciId = Integer.parseInt(((new JSONObject((Map) jsonObject.get("cci"))).get("areaId")).toString());
		boolean instituionDataRejected = false;
		Map<String, Object> prefetchedData = new LinkedHashMap<String, Object>();

		InstitutionData institutionData = null;
		institutionData = institutionDataRepository
				.findByIsLiveTrueAndSubmissionStatusAndQ7AreaId(SubmissionStatus.APPROVED, cciId);

		// if (institutionDataRepository
		// .findByIsLiveTrueAndSubmissionStatusAndQ7AreaId(
		// SubmissionStatus.REJECTED, cciId) != null)
		// instituionDataRejected = true;

		List<Object[]> headers = questionRepository.findReviewHeader();

		Collections.sort(headers, new Comparator<Object[]>() {

			@Override
			public int compare(Object[] o1, Object[] o2) {
				return o1[1].toString().split("_")[0].compareTo(o2[1].toString().split("_")[0]);
			}
		});

		Map<Integer, Map<String, String>> formColumnName = new LinkedHashMap<Integer, Map<String, String>>();

		for (Object[] header : headers) {
			Map<String, String> formColumnMap = null;
			if (formColumnName.containsKey(Integer.parseInt(header[0].toString()))) {
				formColumnMap = formColumnName.get(Integer.parseInt(header[0].toString()));
			} else
				formColumnMap = new LinkedHashMap<String, String>();

			formColumnMap.put(header[2].toString(),
					header[3] == null ? header[1].toString() : header[1].toString() + "@" + header[3].toString());

			formColumnName.put(Integer.parseInt(header[0].toString()), formColumnMap);
		}
		if (institutionData != null)
			prefetchedData = inspectForResponse(InstitutionData.class, institutionData);

		CCILandingPageModel cciLandingPageModel = new CCILandingPageModel();
		Map<String, String> cciInformation = new LinkedHashMap<String, String>();
		for (String d : formColumnName.get(Constants.INSTUTION_FORM).keySet()) {

			if (formColumnName.get(Constants.INSTUTION_FORM).get(d).contains("@")) {
				Question question = new Question();
				question.setDefaultSetting(formColumnName.get(Constants.INSTUTION_FORM).get(d).split("@")[1]
						.replaceAll("areaId", "areaName"));
				prefetchedData.put(d, getValuePrfecthed(question, jsonObject));
			}

		}

		for (String d : formColumnName.get(Constants.INSTUTION_FORM).keySet()) {

			cciInformation.put(formColumnName.get(Constants.INSTUTION_FORM).get(d).split("@")[0].split("_")[1].trim(),
					prefetchedData.containsKey(d) ? prefetchedData.get(d).toString() : "N/A");
		}
		cciInformation.put(formColumnName.get(Constants.INSTUTION_FORM).get("q26").split("@")[0].split("_")[1].trim(),
				String.valueOf(inmatesDataRepository.getStrengthForCCI(cciId, SubmissionStatus.APPROVED)));
		cciInformation.put("rejected", String.valueOf(instituionDataRejected));

		cciLandingPageModel.setCciInformation(cciInformation);
		List<InmatesData> inmatesDatas = inmatesDataRepository
				.findByIsLiveTrueAndOwnerAreaAreaIdOrderByUpdatedDateDesc(cciId);

		Map<String, Object> inmatesDetailsMap = new LinkedHashMap<String, Object>();
		List<String> tableHeaders = new ArrayList<String>();

		for (String tableHeader : formColumnName.get(Constants.CHILD_FORM).values()) {
			tableHeaders.add(tableHeader.split("@")[0].split("_")[1]);
		}
		tableHeaders.add("Action");
		List<SubmissionStatus> submissionStatusList = Arrays.asList(SubmissionStatus.values());

		List<Map<String, Object>> tableData = new ArrayList<Map<String, Object>>();

		for (SubmissionStatus status : submissionStatusList) {
			List<InmatesData> inmatesDatasStatusWise = inmatesDatas.stream()
					.filter(d -> d.getSubmissionStatus() == status).collect(Collectors.toList());
			Map<String, Object> dataMaps = new LinkedHashMap<String, Object>();
			List<Object> dataArray = new ArrayList<Object>();
			dataMaps.put("name", status);
			for (InmatesData inmatesData : inmatesDatasStatusWise) {
				Map<String, Object> childData = new HashMap<String, Object>();
				prefetchedData = inspectForResponse(InmatesData.class, inmatesData);
				for (String keys : formColumnName.get(Constants.CHILD_FORM).keySet()) {
					childData.put(formColumnName.get(Constants.CHILD_FORM).get(keys).split("@")[0].split("_")[1],
							prefetchedData.get(keys) == null ? "N/A" : prefetchedData.get(keys).toString());
				}
				childData.put("id", inmatesData.getInmatesData());

				childData.put("Action", Constants.getAction(status, inmatesData.isLive()));

				dataArray.add(childData);
			}
			dataMaps.put("data", dataArray);
			tableData.add(dataMaps);
		}

		inmatesDetailsMap.put("tableColumn", tableHeaders);
		inmatesDetailsMap.put("tableData", tableData);
		cciLandingPageModel.setInmatesDetailsMap(inmatesDetailsMap);

		return cciLandingPageModel;
	}

	@Override
	@Transactional
	public ResponseModel draftInstitutionInmates(List<QuestionModel> questionModels, OAuth2Authentication auth) {
		ResponseModel responseModel = new ResponseModel();
		InstitutionData institutionData = new InstitutionData();
		InmatesData inmatesData = new InmatesData();
		Object dataFromSession = tokenInfoExtractor.getUserModelInfo(auth);
		JSONObject jsonObject = new JSONObject((Map) dataFromSession);
		Integer cciId = Integer.parseInt(((new JSONObject((Map) jsonObject.get("cci"))).get("areaId")).toString());
		final ObjectMapper mapper = new ObjectMapper();
		List<Attachment> attachements = new ArrayList<Attachment>();
		List<Attachment> deltedAttachements = new ArrayList<Attachment>();

		InstitutionData approvedInstitution = institutionDataRepository
				.findByIsLiveTrueAndSubmissionStatusAndQ7AreaId(SubmissionStatus.APPROVED, cciId);

		Map<String, Object> datObject = new LinkedHashMap<String, Object>();
		Map<String, Object> beginDataObject = new LinkedHashMap<String, Object>();
		int formId = 0;
		int submissionId = 0;
		for (QuestionModel questionModel : questionModels) {
			if (questionModel.getValue() != null || questionModel.getControlType().equals("id")
					|| questionModel.getControlType().equals("beginRepeat")
					|| questionModel.getControlType().equals("beginRepeatImageRow")) {
				switch (questionModel.getControlType()) {
				case "id":
					formId = questionModel.getKey();

					// if id value ==0 or null then we will check wether any other draft version is available or not
					if (questionModel.getValue() == null
							|| Integer.parseInt(questionModel.getValue().toString()) == 0) {
						if (questionModel.getKey() == Constants.CHILD_FORM) {
							InmatesData savedInmate = inmatesDataRepository.findByIsLiveTrueAndQ1AndSubmissionStatus(
									questionModels.stream().filter(d -> d.getColumnName().equals("q1")).findFirst()
											.get().getValue().toString(),
									SubmissionStatus.DRAFT);

							// if any draft version available then we will return error
							if (savedInmate != null) {
								responseModel.setStatusCode(HttpStatus.NOT_MODIFIED.value());
								responseModel.setMessage("Looks like you are working on a old version");
								return responseModel;
							} else {
								// if any submission in pending we will return it with a error
								InmatesData pendingInmate = inmatesDataRepository
										.findByIsLiveTrueAndQ1AndSubmissionStatus(
												questionModels.stream().filter(d -> d.getColumnName().equals("q1"))
														.findFirst().get().getValue().toString(),
												SubmissionStatus.DRAFT);
								if (pendingInmate != null) {
									responseModel.setStatusCode(HttpStatus.CONFLICT.value());
									responseModel.setMessage("Already one submission in pending");
									return responseModel;
								}
							}

						} else if (questionModel.getKey() == Constants.INSTUTION_FORM) {
							InstitutionData institutionDataDraft = institutionDataRepository
									.findByIsLiveTrueAndSubmissionStatusAndQ7AreaId(SubmissionStatus.DRAFT, cciId);

							// in case of any draft version available
							if (institutionDataDraft != null) {
								responseModel.setStatusCode(HttpStatus.NOT_MODIFIED.value());
								responseModel.setMessage("Looks like you are working on a old version");
								return responseModel;
							} else {
								// in case of any pending data available
								InstitutionData pendingInstitution = institutionDataRepository
										.findByIsLiveTrueAndSubmissionStatusAndQ7AreaId(SubmissionStatus.PENDING,
												cciId);
								if (pendingInstitution != null) {
									responseModel.setStatusCode(HttpStatus.CONFLICT.value());
									responseModel.setMessage("Already one submission in pending");
									return responseModel;
								}
							}

						}

						List<QuestionModel> fileModels = questionModels.stream()
								.filter(d -> d.getControlType().equals("file")).collect(Collectors.toList());
						;
						for (QuestionModel fileModel : fileModels) {
							if (fileModel.getValue() != null && !(fileModel.getValue() instanceof String
									&& fileModel.getValue().toString().trim().equals(""))) {
								for (Object attachmentObject : (List<?>) fileModel.getValue()) {
									Attachment attachment = mapper.convertValue(attachmentObject, Attachment.class);
									// attachment.setAttachmentId(null);
									attachements.add(attachment);
								}

							}

						}

						continue;
					}
					
					submissionId = Integer.parseInt(questionModel.getValue().toString());
					if (questionModel.getKey() == Constants.CHILD_FORM) {

						// setting the rejected data false
						InmatesData data = inmatesDataRepository
								.findByIsLiveTrueAndInmatesData(Integer.parseInt(questionModel.getValue().toString()));
						if (data.getSubmissionStatus() == SubmissionStatus.REJECTED) {
							data.setLive(false);
							inmatesDataRepository.save(data);

							List<QuestionModel> fileModels = questionModels.stream()
									.filter(d -> d.getControlType().equals("file")).collect(Collectors.toList());
							;
							for (QuestionModel fileModel : fileModels) {
								if (fileModel.getValue() != null && !(fileModel.getValue() instanceof String
										&& fileModel.getValue().toString().trim().equals(""))) {
									for (Object attachmentObject : (List<?>) fileModel.getValue()) {
										Attachment attachment = mapper.convertValue(attachmentObject, Attachment.class);
										attachment.setAttachmentId(null);
										attachements.add(attachment);
									}

								}

							}

						} else if (data.getSubmissionStatus() != SubmissionStatus.APPROVED) {
							List<QuestionModel> fileModels = questionModels.stream()
									.filter(d -> d.getControlType().equals("file")).collect(Collectors.toList());
							;
							for (QuestionModel fileModel : fileModels) {
								if (fileModel.getValue() != null && !(fileModel.getValue() instanceof String
										&& fileModel.getValue().toString().trim().equals(""))) {
									for (Object attachmentObject : (List<?>) fileModel.getValue()) {
										Attachment attachment = mapper.convertValue(attachmentObject, Attachment.class);
										// attachment.setAttachmentId(null);
										attachements.add(attachment);
									}

								}

								if (fileModel.getDeletedFileValue() != null
										&& fileModel.getDeletedFileValue().length > 0) {
									for (Object attachmentObject : fileModel.getDeletedFileValue()) {
										Attachment attachment = mapper.convertValue(attachmentObject, Attachment.class);
										// attachment.setAttachmentId(null);
										deltedAttachements.add(attachment);
									}

								}

							}
							datObject.put(Stream.of(InmatesData.class.getDeclaredFields())
									.filter(field -> field.isAnnotationPresent(Id.class)).findFirst().get().getName(),
									Integer.parseInt(questionModel.getValue().toString()));
						} else {
							List<QuestionModel> fileModels = questionModels.stream()
									.filter(d -> d.getControlType().equals("file")).collect(Collectors.toList());
							;
							for (QuestionModel fileModel : fileModels) {
								if (fileModel.getValue() != null && !(fileModel.getValue() instanceof String
										&& fileModel.getValue().toString().trim().equals(""))) {
									for (Object attachmentObject : (List<?>) fileModel.getValue()) {
										Attachment attachment = mapper.convertValue(attachmentObject, Attachment.class);
										attachment.setAttachmentId(null);
										attachements.add(attachment);
									}

								}

							}
						}
					}

					else {
						InstitutionData data = institutionDataRepository
								.findByIsLiveTrueAndInstutionId(Integer.parseInt(questionModel.getValue().toString()));
						//setting record false in case of rejected version
						if (data.getSubmissionStatus() == SubmissionStatus.REJECTED) {
							data.setLive(false);
							institutionDataRepository.save(data);

							List<QuestionModel> fileModels = questionModels.stream()
									.filter(d -> d.getControlType().equals("file")).collect(Collectors.toList());
							;
							for (QuestionModel fileModel : fileModels) {
								if (fileModel.getValue() != null && !(fileModel.getValue() instanceof String
										&& fileModel.getValue().toString().trim().equals(""))) {
									for (Object attachmentObject : (List<?>) fileModel.getValue()) {
										Attachment attachment = mapper.convertValue(attachmentObject, Attachment.class);
										attachment.setAttachmentId(null);
										attachements.add(attachment);
									}

								}

							}

						} else if (data.getSubmissionStatus() != SubmissionStatus.APPROVED) {

							List<QuestionModel> fileModels = questionModels.stream()
									.filter(d -> d.getControlType().equals("file")).collect(Collectors.toList());
							;
							for (QuestionModel fileModel : fileModels) {
								if (fileModel.getValue() != null && !(fileModel.getValue() instanceof String
										&& fileModel.getValue().toString().trim().equals(""))) {
									for (Object attachmentObject : (List<?>) fileModel.getValue()) {
										Attachment attachment = mapper.convertValue(attachmentObject, Attachment.class);
										attachements.add(attachment);
									}

								}

								if (fileModel.getDeletedFileValue() != null
										&& fileModel.getDeletedFileValue().length > 0) {
									for (Object attachmentObject : fileModel.getDeletedFileValue()) {
										Attachment attachment = mapper.convertValue(attachmentObject, Attachment.class);
										// attachment.setAttachmentId(null);
										deltedAttachements.add(attachment);
									}

								}

							}

							datObject.put(Stream.of(InstitutionData.class.getDeclaredFields())
									.filter(field -> field.isAnnotationPresent(Id.class)).findFirst().get().getName(),
									Integer.parseInt(questionModel.getValue().toString()));
						}

						else {
							List<QuestionModel> fileModels = questionModels.stream()
									.filter(d -> d.getControlType().equals("file")).collect(Collectors.toList());
							;
							for (QuestionModel fileModel : fileModels) {
								if (fileModel.getValue() != null && !(fileModel.getValue() instanceof String
										&& fileModel.getValue().toString().trim().equals(""))) {
									for (Object attachmentObject : (List<?>) fileModel.getValue()) {
										Attachment attachment = mapper.convertValue(attachmentObject, Attachment.class);
										attachment.setAttachmentId(null);
										attachements.add(attachment);
									}

								}

								if (fileModel.getDeletedFileValue() != null
										&& fileModel.getDeletedFileValue().length > 0) {
									for (Object attachmentObject : fileModel.getDeletedFileValue()) {
										Attachment attachment = mapper.convertValue(attachmentObject, Attachment.class);
										// attachment.setAttachmentId(null);
										deltedAttachements.add(attachment);
									}

								}

							}
						}

					}

					break;
				case "dropdown":
				case "radio":
					if (questionModel.getValue().toString().trim().equals("")) {
						continue;
					}

					if (questionModel.getColumnName().equalsIgnoreCase("qBuildingDetails"))
						datObject.put(questionModel.getColumnName(),
								approvedInstitution.getBuildingDetails().stream()
										.filter(d -> d.getBuildingDetailsId() == Integer
												.parseInt(questionModel.getValue().toString())).findFirst().get().getIndexTrackNum());
					else
						datObject.put(questionModel.getColumnName(),
								Integer.parseInt(questionModel.getValue().toString()));
					break;

				case "file":
					String fileData = null;
					if (questionModel.getValue() instanceof String) {
						continue;
					}
					List<Object> attachments = (List<Object>) questionModel.getValue();
					for (Object map : attachments) {
						final ObjectMapper mapper1 = new ObjectMapper();
						Attachment d = mapper1.convertValue(map, Attachment.class);
						if (fileData == null) {
							fileData = d.getAttachmentId().toString();
						} else {
							fileData += "," + d.getAttachmentId();
						}
					}
					datObject.put(questionModel.getColumnName(), fileData);
					break;
				case "multiSelect":
					if (questionModel.getValue() instanceof String) {
						continue;
					}
					String joined1 = null;
					for (Integer value : (ArrayList<Integer>) questionModel.getValue()) {
						if (joined1 == null) {
							joined1 = value.toString();
						} else {
							joined1 += "," + value;
						}
					}

					datObject.put(questionModel.getColumnName(), joined1);
					break;
				case "beginRepeat":
				case "beginRepeatImageRow":
					List<Map<String, Object>> childDataObjectMapList = new ArrayList<>();
					for (List<QuestionModel> childQuestionModels : questionModel.getChildQuestionModels()) {
						Map<String, Object> childDataObject = new LinkedHashMap<String, Object>();
						for (QuestionModel childQuestionModel : childQuestionModels) {
							if (!childQuestionModel.isRemovable())
								childDataObject.put("removable", childQuestionModel.isRemovable());
							if (childQuestionModel.getValue() != null) {
								switch (childQuestionModel.getControlType()) {

								case "dropdown":
								case "radio":
									if (childQuestionModel.getValue().toString().trim().equals("")) {
										continue;
									}
									childDataObject.put(childQuestionModel.getColumnName(),
											Integer.parseInt(childQuestionModel.getValue().toString()));
									break;

								case "file":
									String fileData1 = null;
									if (childQuestionModel.getValue() instanceof String) {
										continue;
									}

									for (Object attachmentObject : (List<?>) childQuestionModel.getValue()) {
										Attachment attachment = mapper.convertValue(attachmentObject, Attachment.class);
										if (submissionId == 0) {
											attachment.setAttachmentId(null);
										}

										attachements.add(attachment);
									}

									if (childQuestionModel.getDeletedFileValue() != null
											&& childQuestionModel.getDeletedFileValue().length > 0) {
										for (Object attachmentObject : childQuestionModel.getDeletedFileValue()) {
											if (submissionId != 0) {

												Attachment attachment = mapper.convertValue(attachmentObject,
														Attachment.class);
												// attachment.setAttachmentId(null);
												deltedAttachements.add(attachment);
											}

										}

									}

									List<Object> attachments1 = (List<Object>) childQuestionModel.getValue();
									for (Object map : attachments1) {
										final ObjectMapper mapper1 = new ObjectMapper();
										Attachment d = mapper1.convertValue(map, Attachment.class);
										if (fileData1 == null) {
											fileData1 = d.getAttachmentId().toString();
										} else {
											fileData1 += "," + d.getAttachmentId();
										}
									}
									childDataObject.put(childQuestionModel.getColumnName(), fileData1);
									break;
								case "multiSelect":
									if (childQuestionModel.getValue() instanceof String) {
										continue;
									}
									String joined = String.join(",", (CharSequence[]) childQuestionModel.getValue());
									childDataObject.put(childQuestionModel.getColumnName(), joined);
									break;
								case "beginRepeat":
									break;
								case "heading":
									break;
								case "beginRepeatImageRow":
									break;
								default:
									childDataObject.put(childQuestionModel.getColumnName(),
											childQuestionModel.getValue());

								}
								childDataObject.put("indexTrackNum", childQuestionModel.getIndexNumberTrack()
										.split(childQuestionModel.getColumnName() + "_")[1]);
							}

						}
						if (!childDataObject.isEmpty()) {
							childDataObjectMapList.add(childDataObject);
						}

					}
					if (childDataObjectMapList.size() > 0)
						beginDataObject.put(questionModel.getColumnName(), childDataObjectMapList);
					break;
				default:
					datObject.put(questionModel.getColumnName(), questionModel.getValue());
				}

			}

		}

		if (formId == Constants.INSTUTION_FORM) {
			institutionData = mapper.convertValue(datObject, InstitutionData.class);
			institutionData.setLive(true);
			institutionData.setSubmissionStatus(SubmissionStatus.DRAFT);
			institutionData.setDeleted(false);
			if (institutionData.getInstutionId() == 0) {
				institutionData.setCreatedBy(
						new Account(Integer.parseInt(tokenInfoExtractor.tokenInfo(auth).get("userId").toString())));
			} else {

				for (Attachment attachment : deltedAttachements) {
					attachment.setInmateData(null);
					attachment.setIsDeleted(true);
					attachment.setInstitutionData(null);
					attachmentRepository.save(attachment);
				}

				institutionData.setUpdatedBy(
						new Account(Integer.parseInt(tokenInfoExtractor.tokenInfo(auth).get("userId").toString())));

				grantInAidDetailsRepository.deleteByInstitutionDataInstutionId(institutionData.getInstutionId());
				policeCaseReportsRepository.deleteByInstitutionDataInstutionId(institutionData.getInstutionId());
				staffDetailsRepository.deleteByInstitutionDataInstutionId(institutionData.getInstutionId());
				managementsDetailsRepository.deleteByInstitutionDataInstutionId(institutionData.getInstutionId());
				socialAuditReportsRepository.deleteByInstitutionDataInstutionId(institutionData.getInstutionId());
				buildingDetailsRepository.deleteByInstitutionDataInstutionId(institutionData.getInstutionId());

			}

			institutionData = institutionDataRepository.save(institutionData);
			List<GrantInAidDetails> grantInAidDetails = new ArrayList<GrantInAidDetails>();
			List<ManagementDetails> managementDetails = new ArrayList<ManagementDetails>();
			List<StaffDetails> staffDetails = new ArrayList<StaffDetails>();
			List<PoliceCaseReports> policeCaseReports = new ArrayList<PoliceCaseReports>();
			List<SocialAuditReports> socialAuditReports = new ArrayList<SocialAuditReports>();
			List<BuildingDetails> buildingDetails = new ArrayList<BuildingDetails>();
			for (String key : beginDataObject.keySet()) {
				for (Object data : (List<?>) beginDataObject.get(key)) {

					{

						switch (key.trim()) {
						case "managementDetails":
							ManagementDetails managementDetail = mapper.convertValue(data, ManagementDetails.class);
							managementDetail.setInstitutionData(institutionData);
							managementDetail.setLive(true);
							managementDetails.add(managementDetail);
							break;

						case "staffDetails":
							StaffDetails staffDetail = mapper.convertValue(data, StaffDetails.class);
							staffDetail.setInstitutionData(institutionData);
							staffDetail.setLive(true);
							staffDetails.add(staffDetail);
							break;

						case "policeCaseReports":
							PoliceCaseReports policeCaseReport = mapper.convertValue(data, PoliceCaseReports.class);
							policeCaseReport.setInstitutionData(institutionData);
							policeCaseReport.setLive(true);
							policeCaseReports.add(policeCaseReport);
							break;

						case "grantAidDetails":
							GrantInAidDetails grantInAidDetail = mapper.convertValue(data, GrantInAidDetails.class);
							grantInAidDetail.setInstitutionData(institutionData);
							grantInAidDetail.setLive(true);
							grantInAidDetails.add(grantInAidDetail);
							break;

						case "socialAuditReports":
							SocialAuditReports socialAuditReport = mapper.convertValue(data, SocialAuditReports.class);
							socialAuditReport.setInstitutionData(institutionData);
							socialAuditReport.setLive(true);
							socialAuditReports.add(socialAuditReport);
							break;

						case "buildingDetails":
							BuildingDetails buildingDetail = mapper.convertValue(data, BuildingDetails.class);
							buildingDetail.setInstitutionData(institutionData);
							buildingDetail.setLive(true);
							buildingDetails.add(buildingDetail);
							break;

						}
					}

				}

			}

			if (grantInAidDetails.size() > 0) {
				grantInAidDetailsRepository.save(grantInAidDetails);
			}

			if (policeCaseReports.size() > 0) {
				policeCaseReportsRepository.save(policeCaseReports);
			}

			if (staffDetails.size() > 0) {
				staffDetailsRepository.save(staffDetails);
			}

			if (managementDetails.size() > 0) {
				managementsDetailsRepository.save(managementDetails);
			}

			if (socialAuditReports.size() > 0) {
				socialAuditReportsRepository.save(socialAuditReports);
			}

			if (buildingDetails.size() > 0) {
				buildingDetailsRepository.save(buildingDetails);
			}
			for (Attachment attachment : attachements) {
				attachment.setInstitutionData(institutionData);
				attachmentRepository.save(attachment);
			}

		}

		else {

			inmatesData = mapper.convertValue(datObject, InmatesData.class);
			inmatesData.setLive(true);
			inmatesData.setSubmissionStatus(SubmissionStatus.DRAFT);
			if (inmatesData.getInmatesData() == 0)
				inmatesData.setCreatedBy(
						new Account(Integer.parseInt(tokenInfoExtractor.tokenInfo(auth).get("userId").toString())));
			else {
				for (Attachment attachment : deltedAttachements) {
					attachment.setInmateData(null);
					attachment.setIsDeleted(true);
					attachment.setInstitutionData(null);
					attachmentRepository.save(attachment);
				}
				inmatesData.setUpdatedBy(
						new Account(Integer.parseInt(tokenInfoExtractor.tokenInfo(auth).get("userId").toString())));
				inmatesPhotoRepository.deleteByInmatesDataInmatesData(inmatesData.getInmatesData());
			}
			inmatesData.setOwner(userRepository.findByAccount(
					new Account(Integer.parseInt(tokenInfoExtractor.tokenInfo(auth).get("userId").toString()))));
			inmatesData = inmatesDataRepository.save(inmatesData);

			List<InmatesPhoto> inmatesPhotos = new ArrayList<InmatesPhoto>();
//			System.out.println(beginDataObject);
			for (String key : beginDataObject.keySet()) {
				for (Object data : (List<?>) beginDataObject.get(key)) {

					{

						switch (key.trim()) {
						case "inmatesPhoto":
							InmatesPhoto inmatesPhoto = mapper.convertValue(data, InmatesPhoto.class);
							inmatesPhoto.setInmatesData(inmatesData);
							inmatesPhoto.setLive(true);
							inmatesPhotos.add(inmatesPhoto);
							break;

						}
					}

				}

			}

			if (inmatesPhotos.size() > 0) {
				inmatesPhotoRepository.save(inmatesPhotos);
			}

			for (Attachment attachment : attachements) {
				attachment.setInmateData(inmatesData);
				attachmentRepository.save(attachment);

			}
		}
		responseModel.setStatusCode(HttpStatus.OK.value());
		responseModel.setMessage("Successfully Drafted ");

		return responseModel;
	}

	@Override
	public Attachment uploadFile(MultipartFile file, int formId, String columnName) throws Exception {
		try {

			Attachment attachment = new Attachment();

			attachment.setOriginalName(file.getOriginalFilename());

			String fileName;
			if (file.getOriginalFilename().trim().length() <= 5) {
				fileName = file.getOriginalFilename().trim();
			} else {
				fileName = file.getOriginalFilename().trim()
						.replaceAll("." + file.getOriginalFilename().trim().split("\\.")[1], "") + new Date().getTime()
						+ "." + file.getOriginalFilename().trim().split("\\.")[1];
			}
			if (formId == Constants.INSTUTION_FORM) {

				if (file.getContentType().contains("image") && getFileSizeKiloBytes(file) > Constants.MAX_FILE)
					compressImage(new File(this.instPath.resolve(fileName).toAbsolutePath().toString()), file);
				else
					Files.copy(file.getInputStream(), this.instPath.resolve(fileName));
				attachment.setFilePath(this.instPath.resolve(fileName).toAbsolutePath().toString());
			} else {
				if (file.getContentType().contains("image") && getFileSizeKiloBytes(file) > Constants.MAX_FILE)
					compressImage(new File(this.inmatesPath.resolve(fileName).toAbsolutePath().toString()), file);
				else
					Files.copy(file.getInputStream(), this.inmatesPath.resolve(fileName));

				attachment.setFilePath(this.inmatesPath.resolve(fileName).toAbsolutePath().toString());
			}

			attachment.setColumnName(columnName);
			attachment.setIsDeleted(false);
			attachment = attachmentRepository.save(attachment);
			return attachment;// this.rootLocation.resolve(fileName).toAbsolutePath().toString();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Failed to upload");

		}
	}

	@Override
	public Map<String, Object> getInstitutionLandingData(OAuth2Authentication auth) {

		Object dataFromSession = tokenInfoExtractor.getUserModelInfo(auth);
		JSONObject jsonObject = new JSONObject((Map) dataFromSession);
		Integer cciId = Integer.parseInt(((new JSONObject((Map) jsonObject.get("cci"))).get("areaId")).toString());

		Map<String, Object> tableMap = new LinkedHashMap<String, Object>();
		Map<String, Object> prefetchedData = new LinkedHashMap<String, Object>();

		List<Object[]> headers = questionRepository.findReviewHeaderByFormId(Constants.INSTUTION_FORM);

		Collections.sort(headers, new Comparator<Object[]>() {

			@Override
			public int compare(Object[] o1, Object[] o2) {
				return o1[1].toString().split("_")[0].compareTo(o2[1].toString().split("_")[0]);
			}
		});

		Map<Integer, Map<String, String>> formColumnName = new LinkedHashMap<Integer, Map<String, String>>();

		for (Object[] header : headers) {
			// if (header[2].toString().equals("q26"))
			// continue;

			Map<String, String> formColumnMap = null;
			if (formColumnName.containsKey(Integer.parseInt(header[0].toString()))) {
				formColumnMap = formColumnName.get(Integer.parseInt(header[0].toString()));
			} else
				formColumnMap = new LinkedHashMap<String, String>();

			// if (!header[2].toString().equals("q26"))
			formColumnMap.put(header[2].toString(),
					header[3] == null ? header[1].toString() : header[1].toString() + "@" + header[3].toString());
			// else
			// formColumnMap.put(header[2].toString(),
			// header[1].toString() );

			formColumnName.put(Integer.parseInt(header[0].toString()), formColumnMap);
		}

		List<String> tableHeaders = new ArrayList<String>();

		tableHeaders.add("Created On");
		for (String tableHeader : formColumnName.get(Constants.INSTUTION_FORM).values()) {
			tableHeaders.add(tableHeader.split("@")[0].split("_")[1]);
		}

		tableHeaders.add("Status");
		tableHeaders.add("Action");
		tableMap.put("tableColumn", tableHeaders);

		List<InstitutionData> institutionDatas = institutionDataRepository
				.findByIsDeletedFalseAndQ7AreaIdOrderByInstutionIdDesc(cciId);

		if (institutionDatas.stream()
				.filter(d -> d.getSubmissionStatus() == SubmissionStatus.APPROVED && !d.isDeleted() && d.isLive())
				.collect(Collectors.toList()).size() > 0) {
			InstitutionData institutionData1 = institutionDatas.stream()
					.filter(d -> d.getSubmissionStatus() == SubmissionStatus.APPROVED && !d.isDeleted() && d.isLive())
					.findFirst().get();
			prefetchedData = inspectForResponse(InstitutionData.class, institutionData1);
		}

		Map<String, String> cciInformation = new LinkedHashMap<String, String>();
		for (String d : formColumnName.get(Constants.INSTUTION_FORM).keySet()) {

			if (formColumnName.get(Constants.INSTUTION_FORM).get(d).contains("@")) {
				Question question = new Question();
				question.setDefaultSetting(formColumnName.get(Constants.INSTUTION_FORM).get(d).split("@")[1]
						.replaceAll("areaId", "areaName"));
				prefetchedData.put(d, getValuePrfecthed(question, jsonObject));
			}

		}

		for (String d : formColumnName.get(Constants.INSTUTION_FORM).keySet()) {

			cciInformation.put(formColumnName.get(Constants.INSTUTION_FORM).get(d).split("@")[0].split("_")[1].trim(),
					prefetchedData.containsKey(d) ? prefetchedData.get(d).toString() : "N/A");
		}
		cciInformation.put(formColumnName.get(Constants.INSTUTION_FORM).get("q26").split("@")[0].split("_")[1].trim(),
				String.valueOf(inmatesDataRepository.getStrengthForCCI(cciId, SubmissionStatus.APPROVED)));

		tableMap.put("cciInformation", cciInformation);

		List<Object> dataArray = new ArrayList<Object>();
		for (InstitutionData institutionData : institutionDatas) {
			Map<String, Object> childData = new HashMap<String, Object>();
			prefetchedData = inspectForResponse(InstitutionData.class, institutionData);

			for (String keys : formColumnName.get(Constants.INSTUTION_FORM).keySet()) {

				childData.put(formColumnName.get(Constants.INSTUTION_FORM).get(keys).split("@")[0].split("_")[1],
						prefetchedData.get(keys) == null ? "N/A" : prefetchedData.get(keys).toString());

			}
			childData.put("Created On", sdf.format(institutionData.getCreatedDate()));
			childData.put("id", institutionData.getInstutionId());

			childData.put("Action",
					Constants.getAction(institutionData.getSubmissionStatus(), institutionData.isLive()));
			childData.put("Status", institutionData.getSubmissionStatus());

			dataArray.add(childData);
		}

		tableMap.put("tableData", dataArray);
		return tableMap;
	}

	@Override
	public List<DataEntryQuestionModel> getViewData(int formId, OAuth2Authentication auth, Integer submissioId)
			throws Exception {

		QuestionModel questionModel = new QuestionModel();
		List<DataEntryQuestionModel> dataEntryQuestionModels = new ArrayList<DataEntryQuestionModel>();
		questionModel.setControlType("id");
		questionModel.setColumnName("id");
		questionModel.setKey(formId);
		InstitutionData approvedInstitution = null;

		List<Question> questions = questionRepository.findByFormFormIdAndIsLiveTrueOrderByQuestionOrderAsc(formId);

		Map<String, DataEntryQuestionModel> dataEntryQuestionModelMap = new LinkedHashMap<String, DataEntryQuestionModel>();

		List<Attachment> attachments = new ArrayList<Attachment>();

		Map<String, Object> prefetchedData = new HashMap<String, Object>();

		Set<Integer> rejectedSectionIds = new HashSet<Integer>();

		if (formId == Constants.INSTUTION_FORM) {

			InstitutionData institutionData = null;

			if (submissioId != null && submissioId != 0)
				institutionData = institutionDataRepository.findByInstutionId(submissioId);

			if (institutionData != null) {

//				if (institutionData.getSubmissionStatus() != SubmissionStatus.APPROVED) {
					questionModel.setValue(institutionData.getInstutionId());
//				}

				if (institutionData.getSubmissionStatus() == SubmissionStatus.REJECTED
						&& institutionData.getRejectedSections() != null) {
					rejectedSectionIds = Arrays.asList(institutionData.getRejectedSections().split(",")).stream()
							.map(Integer::parseInt).collect(Collectors.toSet());
				}
				attachments = institutionData.getAttachments();
				prefetchedData = inspect(InstitutionData.class, institutionData);
				prefetchedData.put("q26", inmatesDataRepository.getStrengthForCCI(institutionData.getQ7().getAreaId(),
						SubmissionStatus.APPROVED));
			}

		} else if (formId == Constants.CHILD_FORM && submissioId != null && submissioId != 0) {

			InmatesData inmatesData = inmatesDataRepository.findByIsLiveTrueAndInmatesData(submissioId);
			if (inmatesData != null && inmatesData.getSubmissionStatus() == SubmissionStatus.REJECTED) {
				rejectedSectionIds = Arrays.asList(inmatesData.getRejectedSections().split(",")).stream()
						.map(Integer::parseInt).collect(Collectors.toSet());
			}

			if (inmatesData != null) {
//				if (inmatesData.getSubmissionStatus() != SubmissionStatus.APPROVED) {
					questionModel.setValue(inmatesData.getInmatesData());
//				}

				attachments = inmatesData.getAttachment();
				prefetchedData = inspect(InmatesData.class, inmatesData);
			}

			approvedInstitution = institutionDataRepository.findByIsLiveTrueAndSubmissionStatusAndQ7AreaId(
					SubmissionStatus.APPROVED, inmatesData.getOwner().getArea().getAreaId());
			if (prefetchedData.get("qBuildingDetails") != null
					&& prefetchedData.get("qBuildingDetails").toString().trim() != "") {
				String value = prefetchedData.get("qBuildingDetails").toString();
				prefetchedData.put("qBuildingDetails",
						approvedInstitution.getBuildingDetails().stream()
								.filter(d -> Integer.parseInt(value) == d.getIndexTrackNum()).findFirst().get()
								.getBuildingDetailsId());
			}
		}

		DataEntryQuestionModel dataEntryQuestionModel = new DataEntryQuestionModel();
		dataEntryQuestionModel.setId(questions.get(0).getSection().getSectionId());
		if (rejectedSectionIds.contains(dataEntryQuestionModel.getId()))
			dataEntryQuestionModel.setRejected(true);
		else
			dataEntryQuestionModel.setRejected(false);

		dataEntryQuestionModel.setName(questions.get(0).getSection().getSectionName());
		dataEntryQuestionModel.setSectionOrder(questions.get(0).getSection().getSectionOrder());
		List<QuestionModel> questionModels = new ArrayList<QuestionModel>();
		questionModels.add(questionModel);
		dataEntryQuestionModel.setQuestions(questionModels);
		dataEntryQuestionModelMap.put(dataEntryQuestionModel.getName(), dataEntryQuestionModel);

		for (Question question : questions) {
			questionModel = new QuestionModel();
			questionModel.setAllChecked(false);
			questionModel.setColumnName(question.getColumnn());
			questionModel.setControlType(question.getControlType());
			questionModel.setType(question.getInputType());
			if (question.getFileExtensions() != null)
				questionModel.setFileExtension(question.getFileExtensions().split(","));

			questionModel.setDisabled(true);

			if (question.getDependecy()) {
				questionModel.setDependentCondition(question.getDependentCondition().split(","));
				questionModel.setParentColumns(question.getDependentColumn().split(","));
			}

			if (question.getControlType() != "beginRepeat" && question.getGroupId() == null)
				questionModel.setValue(prefetchedData.get(question.getColumnn()) != null
						? prefetchedData.get(question.getColumnn()).toString()
						: null);

			if (question.getControlType().equals("multiSelect"))
				questionModel.setValue(prefetchedData.get(question.getColumnn()) != null
						? Arrays.asList(prefetchedData.get(question.getColumnn()).toString().split(",")).stream()
								.mapToInt(Integer::parseInt).toArray()
						: null);

			if (question.getControlType().equals("file") && prefetchedData.get(question.getColumnn()) != null) {

				// String[] attachmentsStrings =
				// prefetchedData.get(question.getColumnn()).toString().split(",");
				List<Attachment> attachmentQuestion = new ArrayList<Attachment>();
				// for (String attachmentString : attachmentsStrings) {
				// System.out.println(attachmentString);
				attachmentQuestion.addAll(attachments.stream()
						.filter(d -> d.getColumnName().equals(question.getColumnn())).collect(Collectors.toList()));
				// }
				questionModel.setValue(attachmentQuestion);
			}

			questionModel.setFileSize((double) 1024000000);
			questionModel.setKey(question.getQuestionId());
			questionModel
					.setSerialNumb(question.getSection().getSectionOrder() + "." + question.getQuestionSerial() + "  ");
			questionModel.setLabel(
					question.getQuestionName() != null
							? question.getSection().getSectionOrder() + "." + question.getQuestionSerial() + "  "
									+ question.getQuestionName()
							: "");
			if (question.getConstraints() != null) {
				for (String s : question.getConstraints().split(",")) {
					if (s.contains("minlength=")) {
						questionModel.setMinLength(Integer.parseInt(s.split("minlength=")[1].trim()));
					}

					if (s.contains("maxlength=")) {
						questionModel.setMaxLength(Integer.parseInt(s.split("maxlength=")[1].trim()));
					}
				}
			}

			if (question.getInputType().contains("multiple"))
				questionModel.setMultiple(true);

			if (question.getQuestionOptionTypeMapping() != null) {
				questionModel.setOptions(formatOption(question));
			}

			if (question.getFeatures() != null && question.getFeatures().contains("fetchsession")) {
				questionModel.setOptions(prefetchedData.get(questionModel.getColumnName() + "_Obj") == null
						? new ArrayList<OptionModel>()
						: areaToOption(((Area) prefetchedData.get(questionModel.getColumnName() + "_Obj"))));
			}

			if (question.getDefaultSetting() != null && question.getDefaultSetting().contains("fetchTable")) {
				questionModel.setOptions(prefetchedData.get(questionModel.getColumnName() + "_Obj") == null
						? new ArrayList<OptionModel>()
						: areaToOption(((Area) prefetchedData.get(questionModel.getColumnName() + "_Obj"))));
			}

			if (question.getFeatures() != null && question.getFeatures().contains("parent")) {

				for (String feature : question.getFeatures().split(",")) {
					feature = feature.trim();
					if (feature.contains("parent:")) {
						questionModel.setOptionsParentColumn(feature.split(":")[1]);

					}
				}
			}

			if (question.getColumnn().equals("qBuildingDetails") && approvedInstitution != null) {
				List<OptionModel> options = new ArrayList<OptionModel>();

				for (BuildingDetails buildingDetails : approvedInstitution.getBuildingDetails()) {
					OptionModel option = new OptionModel();
					option.setKey(buildingDetails.getBuildingDetailsId());
					option.setValue(buildingDetails.getQ33BuildingName());

					options.add(option);
				}
				questionModel.setOptions(options);
			}

			questionModel.setRequired(question.isFinalizeMandatory());
			questionModel.setTriggable(false);

			if (question.getGroupId() != null) {
				questionModel.setGroupParentId(question.getGroupId());

				List<?> prefetchedDataForRepeat = (List<?>) prefetchedData.get(question.getGroupId());
				if (prefetchedDataForRepeat != null && prefetchedDataForRepeat.size() > 0) {

					for (int i = 0; i < prefetchedDataForRepeat.size(); i++) {
						Map<String, Object> beginRepeatDataMap = inspect(prefetchedDataForRepeat.get(i).getClass(),
								prefetchedDataForRepeat.get(i));

						QuestionModel clonedQuestionModel = (QuestionModel) questionModel.clone();
						clonedQuestionModel.setValue(beginRepeatDataMap.get(clonedQuestionModel.getColumnName()) != null
								? beginRepeatDataMap.get(clonedQuestionModel.getColumnName()).toString()
								: null);

						if (questionModel.getControlType().equals("multiSelect"))
							clonedQuestionModel
									.setValue(beginRepeatDataMap.get(clonedQuestionModel.getColumnName()) != null
											? beginRepeatDataMap.get(clonedQuestionModel.getColumnName()).toString()
													.split(",")
											: null);

						if (clonedQuestionModel.getControlType().equals("file")
								&& beginRepeatDataMap.get(clonedQuestionModel.getColumnName()) != null) {

							// String[] attachmentsStrings =
							// beginRepeatDataMap.get(clonedQuestionModel.getColumnName())
							// .toString().split(",");
							List<Attachment> attachmentQuestion = new ArrayList<Attachment>();
							// for (String attachmentString :
							// attachmentsStrings) {
							attachmentQuestion.addAll(attachments.stream()
									.filter(d -> d.getColumnName()
											.equals(clonedQuestionModel.getColumnName() + "_"
													+ beginRepeatDataMap.get("indexTrackNum")))
									.collect(Collectors.toList()));
							// }
							clonedQuestionModel.setValue(attachmentQuestion);
							clonedQuestionModel
									.setFileValues(beginRepeatDataMap.get(question.getColumnn()).toString().split(","));
						}

						clonedQuestionModel.setIndexNumberTrack(
								question.getColumnn() + "_" + beginRepeatDataMap.get("indexTrackNum"));

						if (dataEntryQuestionModelMap.get(question.getSection().getSectionName()).getQuestions()
								.stream().filter(d -> d.getColumnName().trim().equals(question.getGroupId().trim()))
								.findFirst().get().getChildQuestionModels() != null
								&& dataEntryQuestionModelMap.get(question.getSection().getSectionName()).getQuestions()
										.stream()
										.filter(d -> d.getColumnName().trim().equals(question.getGroupId().trim()))
										.findFirst().get().getChildQuestionModels().size() > i) {
							dataEntryQuestionModelMap.get(question.getSection().getSectionName()).getQuestions()
									.stream().filter(d -> d.getColumnName().trim().equals(question.getGroupId().trim()))
									.findFirst().get().getChildQuestionModels().get(i).add(clonedQuestionModel);

						}

						else {

							List<List<QuestionModel>> childQuestionModels = null;
							if (dataEntryQuestionModelMap.get(question.getSection().getSectionName()).getQuestions()
									.stream().filter(d -> d.getColumnName().trim().equals(question.getGroupId().trim()))
									.findFirst().get().getChildQuestionModels() == null) {
								childQuestionModels = new ArrayList<List<QuestionModel>>();
							} else {
								childQuestionModels = dataEntryQuestionModelMap
										.get(question.getSection().getSectionName()).getQuestions().stream()
										.filter(d -> d.getColumnName().trim().equals(question.getGroupId().trim()))
										.findFirst().get().getChildQuestionModels();
							}

							List<QuestionModel> childQuestionModel = new ArrayList<QuestionModel>();
							childQuestionModel.add(clonedQuestionModel);
							childQuestionModels.add(childQuestionModel);
							dataEntryQuestionModelMap.get(question.getSection().getSectionName()).getQuestions()
									.stream().filter(d -> d.getColumnName().trim().equals(question.getGroupId().trim()))
									.findFirst().get().setChildQuestionModels(childQuestionModels);

						}

					}

				}

				else {

					questionModel.setIndexNumberTrack(question.getColumnn() + "_" + 0);
					if (dataEntryQuestionModelMap.get(question.getSection().getSectionName()).getQuestions().stream()
							.filter(d -> d.getColumnName().trim().equals(question.getGroupId().trim())).findFirst()
							.get().getChildQuestionModels() != null) {
						dataEntryQuestionModelMap.get(question.getSection().getSectionName()).getQuestions().stream()
								.filter(d -> d.getColumnName().trim().equals(question.getGroupId().trim())).findFirst()
								.get().getChildQuestionModels().get(0).add(questionModel);

					}

					else {
						List<List<QuestionModel>> childQuestionModels = new ArrayList<List<QuestionModel>>();
						List<QuestionModel> childQuestionModel = new ArrayList<QuestionModel>();
						childQuestionModel.add(questionModel);
						childQuestionModels.add(childQuestionModel);
						dataEntryQuestionModelMap.get(question.getSection().getSectionName()).getQuestions().stream()
								.filter(d -> d.getColumnName().trim().equals(question.getGroupId().trim())).findFirst()
								.get().setChildQuestionModels(childQuestionModels);

					}

				}

			} else {
				if (dataEntryQuestionModelMap.containsKey(question.getSection().getSectionName())) {
					dataEntryQuestionModelMap.get(question.getSection().getSectionName()).getQuestions()
							.add(questionModel);
				} else {
					dataEntryQuestionModel = new DataEntryQuestionModel();
					dataEntryQuestionModel.setId(question.getSection().getSectionId());
					if (rejectedSectionIds.contains(dataEntryQuestionModel.getId()))
						dataEntryQuestionModel.setRejected(true);
					else
						dataEntryQuestionModel.setRejected(false);

					dataEntryQuestionModel.setName(question.getSection().getSectionName());
					dataEntryQuestionModel.setSectionOrder(question.getSection().getSectionOrder());
					questionModels = new ArrayList<QuestionModel>();
					questionModels.add(questionModel);
					dataEntryQuestionModel.setQuestions(questionModels);
					dataEntryQuestionModelMap.put(dataEntryQuestionModel.getName(), dataEntryQuestionModel);

				}
			}

		}

		dataEntryQuestionModels = new ArrayList<DataEntryQuestionModel>(dataEntryQuestionModelMap.values());

		return dataEntryQuestionModels;
	}

	@Override
	@Transactional
	public ResponseModel finalizeInstitutionInmates(List<QuestionModel> questionModels, OAuth2Authentication auth) {
		ResponseModel responseModel = new ResponseModel();
		InstitutionData institutionData = new InstitutionData();
		InmatesData inmatesData = new InmatesData();
		Object dataFromSession = tokenInfoExtractor.getUserModelInfo(auth);
		JSONObject jsonObject = new JSONObject((Map) dataFromSession);
		Integer cciId = Integer.parseInt(((new JSONObject((Map) jsonObject.get("cci"))).get("areaId")).toString());
		Map<String, Object> beginDataObject = new LinkedHashMap<String, Object>();
		final ObjectMapper mapper = new ObjectMapper();
		List<Attachment> attachements = new ArrayList<Attachment>();
		InstitutionData approvedInstitution = institutionDataRepository
				.findByIsLiveTrueAndSubmissionStatusAndQ7AreaId(SubmissionStatus.APPROVED, cciId);
		
		Map<String, Object> datObject = new LinkedHashMap<String, Object>();
		int formId = 0;
		for (QuestionModel questionModel : questionModels) {
			if (questionModel.getValue() != null || questionModel.getControlType().equals("id")
					|| questionModel.getControlType().equals("beginRepeat")
					|| questionModel.getControlType().equals("beginRepeatImageRow")) {
				switch (questionModel.getControlType()) {
				case "id":
					formId = questionModel.getKey();
					if (questionModel.getValue() == null
							|| Integer.parseInt(questionModel.getValue().toString()) == 0) {
						if (questionModel.getKey() == Constants.CHILD_FORM) {

							// if any pending data available we will return error
							InmatesData pendingInmate = inmatesDataRepository.findByIsLiveTrueAndQ1AndSubmissionStatus(
									questionModels.stream().filter(d -> d.getColumnName().equals("q1")).findFirst()
											.get().getValue().toString(),
									SubmissionStatus.PENDING);
							if (pendingInmate != null) {
								responseModel.setStatusCode(HttpStatus.CONFLICT.value());
								responseModel.setMessage("Already one submission in pending");
								return responseModel;

							}

						} else if (questionModel.getKey() == Constants.INSTUTION_FORM) {

							// if any pending data available we will return error
							InstitutionData pendingInstitution = institutionDataRepository
									.findByIsLiveTrueAndSubmissionStatusAndQ7AreaId(SubmissionStatus.PENDING, cciId);
							if (pendingInstitution != null) {
								responseModel.setStatusCode(HttpStatus.CONFLICT.value());
								responseModel.setMessage("Already one submission in pending");
								return responseModel;
							}

						}

						List<QuestionModel> fileModels = questionModels.stream()
								.filter(d -> d.getControlType().equals("file")).collect(Collectors.toList());
						;
						for (QuestionModel fileModel : fileModels) {
							if (fileModel.getValue() != null && !(fileModel.getValue() instanceof String
									&& fileModel.getValue().toString().trim().equals(""))) {
								for (Object attachmentObject : (List<?>) fileModel.getValue()) {
									Attachment attachment = mapper.convertValue(attachmentObject, Attachment.class);
									attachment.setAttachmentId(null);
									attachements.add(attachment);
								}

							}

						}

						continue;
					}

					if (questionModel.getKey() == Constants.CHILD_FORM) {
						// if current version is other than approved we will set this record as archive
						InmatesData data = inmatesDataRepository
								.findByIsLiveTrueAndInmatesData(Integer.parseInt(questionModel.getValue().toString()));
						if (data.getSubmissionStatus() != SubmissionStatus.APPROVED) {
							data.setLive(false);
							inmatesDataRepository.save(data);

						}
						List<QuestionModel> fileModels = questionModels.stream()
								.filter(d -> d.getControlType().equals("file")).collect(Collectors.toList());
						;
						for (QuestionModel fileModel : fileModels) {
							if (fileModel.getValue() != null && !(fileModel.getValue() instanceof String
									&& fileModel.getValue().toString().trim().equals(""))) {
								for (Object attachmentObject : (List<?>) fileModel.getValue()) {
									Attachment attachment = mapper.convertValue(attachmentObject, Attachment.class);
									attachment.setAttachmentId(null);
									attachements.add(attachment);
								}

							}

						}

					}

					else {
						InstitutionData data = institutionDataRepository
								.findByIsLiveTrueAndInstutionId(Integer.parseInt(questionModel.getValue().toString()));
						// if current version is other than approved we will set this record as archive
						if (data.getSubmissionStatus() != SubmissionStatus.APPROVED) {
							data.setLive(false);
							institutionDataRepository.save(data);
						}
						List<QuestionModel> fileModels = questionModels.stream()
								.filter(d -> d.getControlType().equals("file")).collect(Collectors.toList());
						;
						for (QuestionModel fileModel : fileModels) {
							if (fileModel.getValue() != null && !(fileModel.getValue() instanceof String
									&& fileModel.getValue().toString().trim().equals(""))) {
								for (Object attachmentObject : (List<?>) fileModel.getValue()) {
									Attachment attachment = mapper.convertValue(attachmentObject, Attachment.class);
									attachment.setAttachmentId(null);
									attachements.add(attachment);
								}

							}

						}

					}

					break;
				case "dropdown":
				case "radio":
					if (questionModel.getValue().toString().trim().equals("")) {
						continue;
					}
					
					if (questionModel.getColumnName().equalsIgnoreCase("qBuildingDetails") && approvedInstitution!=null)
						datObject.put(questionModel.getColumnName(),
								approvedInstitution.getBuildingDetails().stream()
										.filter(d -> d.getBuildingDetailsId() == Integer
												.parseInt(questionModel.getValue().toString())).findFirst().get().getIndexTrackNum());
					else if(questionModel.getColumnName().equalsIgnoreCase("qBuildingDetails") && approvedInstitution==null)
					{
						responseModel.setStatusCode(HttpStatus.CONFLICT.value());
						responseModel.setMessage("Please add building details first");
						return responseModel;
					}
					else	
					datObject.put(questionModel.getColumnName(), Integer.parseInt(questionModel.getValue().toString()));
					break;

				case "file":
					String fileData = null;
					if (questionModel.getValue() instanceof String) {
						continue;
					}
					List<Object> attachments = (List<Object>) questionModel.getValue();
					for (Object map : attachments) {
						final ObjectMapper mapper1 = new ObjectMapper();
						Attachment d = mapper1.convertValue(map, Attachment.class);
						if (fileData == null) {
							fileData = d.getAttachmentId().toString();
						} else {
							fileData += "," + d.getAttachmentId();
						}
					}
					datObject.put(questionModel.getColumnName(), fileData);
					break;
				case "multiSelect":
					if (questionModel.getValue() instanceof String) {
						continue;
					}
					String joined1 = null;
					for (Integer value : (ArrayList<Integer>) questionModel.getValue()) {
						if (joined1 == null) {
							joined1 = value.toString();
						} else {
							joined1 += "," + value;
						}
					}

					datObject.put(questionModel.getColumnName(), joined1);
					break;
				case "beginRepeat":
				case "beginRepeatImageRow":

					List<Map<String, Object>> childDataObjectMapList = new ArrayList<>();
					for (List<QuestionModel> childQuestionModels : questionModel.getChildQuestionModels()) {
						Map<String, Object> childDataObject = new LinkedHashMap<String, Object>();
						for (QuestionModel childQuestionModel : childQuestionModels) {

							if (!childQuestionModel.isRemovable())
								childDataObject.put("removable", childQuestionModel.isRemovable());

							if (childQuestionModel.getValue() != null) {
								switch (childQuestionModel.getControlType()) {

								case "dropdown":
								case "radio":
									if (childQuestionModel.getValue().toString().trim().equals("")) {
										continue;
									}
									childDataObject.put(childQuestionModel.getColumnName(),
											Integer.parseInt(childQuestionModel.getValue().toString()));
									break;

								case "file":
									String fileData1 = null;
									if (childQuestionModel.getValue() instanceof String) {
										continue;
									}
									List<Object> attachments1 = (List<Object>) childQuestionModel.getValue();
									for (Object map : attachments1) {
										final ObjectMapper mapper1 = new ObjectMapper();
										Attachment d = mapper1.convertValue(map, Attachment.class);
										if (fileData1 == null) {
											fileData1 = d.getAttachmentId().toString();
										} else {
											fileData1 += "," + d.getAttachmentId();
										}
										Attachment attachment = mapper.convertValue(map, Attachment.class);
										attachment.setAttachmentId(null);
										attachements.add(attachment);
									}

									childDataObject.put(childQuestionModel.getColumnName(), fileData1);
									break;
								case "multiSelect":
									if (childQuestionModel.getValue() instanceof String) {
										continue;
									}
									String joined = String.join(",", (CharSequence[]) childQuestionModel.getValue());
									childDataObject.put(childQuestionModel.getColumnName(), joined);
									break;
								case "beginRepeat":
									break;
								case "heading":
									break;
								case "beginRepeatImageRow":
									break;

								default:
									childDataObject.put(childQuestionModel.getColumnName(),
											childQuestionModel.getValue());

									childDataObject.put("indexTrackNum", childQuestionModel.getIndexNumberTrack()
											.split(childQuestionModel.getColumnName() + "_")[1]);
								}

							}

						}
						if (!childDataObject.isEmpty()) {
							childDataObjectMapList.add(childDataObject);
						}

					}
					if (childDataObjectMapList.size() > 0)
						beginDataObject.put(questionModel.getColumnName(), childDataObjectMapList);
					break;
				default:
					datObject.put(questionModel.getColumnName(), questionModel.getValue());
				}

			}

		}

		if (formId == Constants.INSTUTION_FORM) {
			institutionData = mapper.convertValue(datObject, InstitutionData.class);
			institutionData.setLive(true);
			institutionData.setSubmissionStatus(SubmissionStatus.PENDING);
			institutionData.setRejectedSections(null);
			institutionData.setRemarks(null);
			institutionData.setDeleted(false);
			if (institutionData.getInstutionId() == 0) {
				institutionData.setCreatedBy(
						new Account(Integer.parseInt(tokenInfoExtractor.tokenInfo(auth).get("userId").toString())));
			} else {
				institutionData.setUpdatedBy(
						new Account(Integer.parseInt(tokenInfoExtractor.tokenInfo(auth).get("userId").toString())));

				grantInAidDetailsRepository.deleteByInstitutionDataInstutionId(institutionData.getInstutionId());
				policeCaseReportsRepository.deleteByInstitutionDataInstutionId(institutionData.getInstutionId());
				staffDetailsRepository.deleteByInstitutionDataInstutionId(institutionData.getInstutionId());
				managementsDetailsRepository.deleteByInstitutionDataInstutionId(institutionData.getInstutionId());
				socialAuditReportsRepository.deleteByInstitutionDataInstutionId(institutionData.getInstutionId());
				buildingDetailsRepository.deleteByInstitutionDataInstutionId(institutionData.getInstutionId());

			}

			institutionData = institutionDataRepository.save(institutionData);
			List<GrantInAidDetails> grantInAidDetails = new ArrayList<GrantInAidDetails>();
			List<ManagementDetails> managementDetails = new ArrayList<ManagementDetails>();
			List<StaffDetails> staffDetails = new ArrayList<StaffDetails>();
			List<PoliceCaseReports> policeCaseReports = new ArrayList<PoliceCaseReports>();
			List<SocialAuditReports> socialAuditReports = new ArrayList<SocialAuditReports>();
			List<BuildingDetails> buildingDetails = new ArrayList<BuildingDetails>();
			for (String key : beginDataObject.keySet()) {
				for (Object data : (List<?>) beginDataObject.get(key)) {

					{

						switch (key.trim()) {
						case "managementDetails":
							ManagementDetails managementDetail = mapper.convertValue(data, ManagementDetails.class);
							managementDetail.setInstitutionData(institutionData);
							managementDetail.setLive(true);
							managementDetails.add(managementDetail);
							break;

						case "staffDetails":
							StaffDetails staffDetail = mapper.convertValue(data, StaffDetails.class);
							staffDetail.setInstitutionData(institutionData);
							staffDetail.setLive(true);
							staffDetails.add(staffDetail);
							break;

						case "policeCaseReports":
							PoliceCaseReports policeCaseReport = mapper.convertValue(data, PoliceCaseReports.class);
							policeCaseReport.setInstitutionData(institutionData);
							policeCaseReport.setLive(true);
							policeCaseReports.add(policeCaseReport);
							break;

						case "grantAidDetails":
							GrantInAidDetails grantInAidDetail = mapper.convertValue(data, GrantInAidDetails.class);
							grantInAidDetail.setInstitutionData(institutionData);
							grantInAidDetail.setLive(true);
							grantInAidDetails.add(grantInAidDetail);
							break;

						case "socialAuditReports":
							SocialAuditReports socialAuditReport = mapper.convertValue(data, SocialAuditReports.class);
							socialAuditReport.setInstitutionData(institutionData);
							socialAuditReport.setLive(true);
							socialAuditReports.add(socialAuditReport);
							break;

						case "buildingDetails":
							BuildingDetails buildingDetail = mapper.convertValue(data, BuildingDetails.class);
							buildingDetail.setInstitutionData(institutionData);
							buildingDetail.setLive(true);
							buildingDetails.add(buildingDetail);

						}
					}

				}

			}

			if (grantInAidDetails.size() > 0) {
				grantInAidDetailsRepository.save(grantInAidDetails);
			}

			if (policeCaseReports.size() > 0) {
				policeCaseReportsRepository.save(policeCaseReports);
			}

			if (staffDetails.size() > 0) {
				staffDetailsRepository.save(staffDetails);
			}

			if (managementDetails.size() > 0) {
				managementsDetailsRepository.save(managementDetails);
			}

			if (socialAuditReports.size() > 0) {
				socialAuditReportsRepository.save(socialAuditReports);
			}

			if (buildingDetails.size() > 0) {
				buildingDetailsRepository.save(buildingDetails);
			}

			for (Attachment attachment : attachements) {

				attachment.setInstitutionData(institutionData);
				attachmentRepository.save(attachment);

			}

		}

		else {

			inmatesData = mapper.convertValue(datObject, InmatesData.class);
			inmatesData.setLive(true);
			inmatesData.setSubmissionStatus(SubmissionStatus.PENDING);
			inmatesData.setRejectedSections(null);
			inmatesData.setRemarks(null);
			if (inmatesData.getInmatesData() == 0) {
				inmatesData.setCreatedBy(
						new Account(Integer.parseInt(tokenInfoExtractor.tokenInfo(auth).get("userId").toString())));
			}

			else {
				inmatesData.setUpdatedBy(
						new Account(Integer.parseInt(tokenInfoExtractor.tokenInfo(auth).get("userId").toString())));
				inmatesPhotoRepository.deleteByInmatesDataInmatesData(inmatesData.getInmatesData());
			}
			inmatesData.setOwner(userRepository.findByAccount(
					new Account(Integer.parseInt(tokenInfoExtractor.tokenInfo(auth).get("userId").toString()))));
			inmatesData = inmatesDataRepository.save(inmatesData);

			List<InmatesPhoto> inmatesPhotos = new ArrayList<InmatesPhoto>();
			for (String key : beginDataObject.keySet()) {
				for (Object data : (List<?>) beginDataObject.get(key)) {

					{

						switch (key.trim()) {
						case "inmatesPhoto":
							InmatesPhoto inmatesPhoto = mapper.convertValue(data, InmatesPhoto.class);
							inmatesPhoto.setInmatesData(inmatesData);
							inmatesPhoto.setLive(true);
							inmatesPhotos.add(inmatesPhoto);
							break;

						}
					}

				}

			}

			if (inmatesPhotos.size() > 0) {
				inmatesPhotoRepository.save(inmatesPhotos);
			}

			for (Attachment attachment : attachements) {
				attachment.setInmateData(inmatesData);
				attachmentRepository.save(attachment);
			}

			if (inmatesData.getQ64() != null && inmatesData.getQ64().getOptionId() == Constants.OPTION_YES) {
				PostSubmissionModel postSubmissionModel = new PostSubmissionModel();
				postSubmissionModel.setDcpuDistrictId(Integer
						.parseInt((new JSONObject((Map) (new JSONObject((Map) jsonObject.get("cci"))).get("parentArea"))
								.get("areaId")).toString()));
				postSubmissionModel.setInmateDetail(inmatesData);
				// postSubmissionWork(postSubmissionModel);
				//
				ExecutorService emailExecutor = Executors.newSingleThreadExecutor();
				emailExecutor.execute(new Runnable() {
					@Override
					public void run() {
						try {
							postSubmissionWork(postSubmissionModel);
						} catch (Exception e) {
							logger.error("failed", e);
						}
					}
				});
				emailExecutor.shutdown();
//				postSubmissionMailThread.setPostSubmissionModel(postSubmissionModel);
//				postSubmissionMailThread.start();
			}

		}
		responseModel.setStatusCode(HttpStatus.OK.value());
		responseModel.setMessage("Your Submission is under review");

		return responseModel;
	}

	@Override
	public ResponseModel deleteSubmission(int formId, OAuth2Authentication auth, Integer submissionId) {

		ResponseModel responseModel = new ResponseModel();
		if (formId == Constants.INSTUTION_FORM) {
			InstitutionData institutionData = institutionDataRepository.findByInstutionId(submissionId);
			institutionData.setLive(false);
			institutionData.setDeleted(true);
			institutionDataRepository.save(institutionData);

			responseModel.setStatusCode(HttpStatus.OK.value());
			responseModel.setMessage("Submission Deleted");
		} else if (formId == Constants.CHILD_FORM) {
			InmatesData inmateData = inmatesDataRepository.findByInmatesData(submissionId);
			inmateData.setLive(false);
			inmateData.setDeleted(true);
			inmatesDataRepository.save(inmateData);
			responseModel.setStatusCode(HttpStatus.OK.value());
			responseModel.setMessage("Submission Deleted");
		} else {
			responseModel.setStatusCode(HttpStatus.BAD_REQUEST.value());
			responseModel.setMessage("Bad Request");
		}
		return responseModel;
	}

	// private void sendMail(InmatesData inmateDetail,int dcpuDistrictId)
	// {
	// List<User> user = userRepository.findByArea(new Area(dcpuDistrictId));
	// if(user.size()>0)
	// {
	// try
	// {
	// Account account = user.get(0).getAccount();
	//
	// String contactNumber="Not available",email="Not Available";
	// Area cci = inmateDetail.getOwner().getArea();
	//
	// InstitutionData institutionData =
	// institutionDataRepository.findByIsLiveTrueAndSubmissionStatusAndQ7AreaId(SubmissionStatus.APPROVED,
	// cci.getAreaId());
	// if(institutionData!=null)
	// {
	// if(institutionData.getQ15()!=null)
	// contactNumber=institutionData.getQ15();
	//
	// if(institutionData.getQ16()!=null)
	// email=institutionData.getQ16();
	// }
	//
	// MimeMessage message = emailSender.createMimeMessage();
	// MimeMessageHelper helper = new MimeMessageHelper(message);
	// String emailBody = "<html> <body> Hello "+user.get(0).getFirstName()+","+
	// "<br> <br> Please find below the details of inmate who has been marked as
	// deceased: <br> "
	// + "<br>CCI-Name : "+ cci.getAreaName()
	// + "<br>Contact Number : " +contactNumber
	// + "<br>Email : " +email
	// + "<br>CHILD ID : " +inmateDetail.getQ1()
	// + "<br><br>" + "Regards,<br> CCI-Monitoring Team" + "<br>"
	// + " </body> </html>";
	// helper.setTo(account.getEmail().split(","));
	// helper.setSubject("CCIMIS : Inmate deceased notification");
	// helper.setText(emailBody,true);
	// emailSender.send(message);
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }
	//
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sdrc.scps.service.DataEntryService#postSubmissionWork(org.sdrc.scps
	 * .models.PostSubmissionModel)
	 */
	@Override
	public void postSubmissionWork(PostSubmissionModel postSubmissionModel) {
		List<User> user = userRepository
				.findByAreaAndAccountEnabledTrue(new Area(postSubmissionModel.getDcpuDistrictId()));
		if (user.size() > 0) {
			try {
				Account account = user.get(0).getAccount();

				List<User> parentUser = userRepository
						.findByAreaAndAccountEnabledTrue(user.get(0).getArea().getParentArea());

				String contactNumber = "Not available", email = "Not Available";
				Area cci = postSubmissionModel.getInmateDetail().getOwner().getArea();

				InstitutionData institutionData = institutionDataRepository
						.findByIsLiveTrueAndSubmissionStatusAndQ7AreaId(SubmissionStatus.APPROVED, cci.getAreaId());
				if (institutionData != null) {
					if (institutionData.getQ15() != null)
						contactNumber = institutionData.getQ15();

					if (institutionData.getQ16() != null)
						email = institutionData.getQ16();
				}

				MimeMessage message = emailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(message);
				String emailBody = "<html> <body> Hello " + user.get(0).getFirstName() + ","
						+ "<br> <br> Please find below the details of inmate who has been marked as deceased: <br> "
						+ "<br>CCI-Name : " + cci.getAreaName() + "<br>Contact Number : " + contactNumber
						+ "<br>Email : " + email + "<br>CHILD ID : " + postSubmissionModel.getInmateDetail().getQ1()
						+ "<br><br>" + "Regards,<br> CCI-Monitoring Team" + "<br>" + " </body> </html>";
				helper.setTo(account.getEmail().split(","));
				
				if (parentUser.size() > 0 && parentUser.get(0).getAccount().getEmail() != null)
				{
					List<String> ccEmails = new ArrayList<String>();
					for(User stateUser:parentUser)
					{
						if(stateUser.getAccount().getEmail() != null)
						{
							ccEmails.addAll(Arrays.asList(stateUser.getAccount().getEmail().split(",")));
						}
					}
					
					if(!ccEmails.isEmpty())
					helper.setCc(ccEmails.toArray(new String[ccEmails.size()]));
				}
					
				
				helper.setSubject("CCIMIS : Inmate deceased notification");
				helper.setText(emailBody, true);
				emailSender.send(message);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private void compressImage(File file, MultipartFile files) throws Exception {
//		System.out.println(getFileSizeKiloBytes(files));
		float compressionQuality = (float) ((Constants.IMAGE_QUALITY) / getFileSizeKiloBytes(files));

		BufferedOutputStream buffStream = new BufferedOutputStream(new FileOutputStream(file));

		BufferedImage bufferedImage = ImageIO.read(files.getInputStream());

		// Get image writers
		Iterator<ImageWriter> imageWriters = ImageIO.getImageWritersByFormatName("jpg");

		ImageWriter imageWriter = (ImageWriter) imageWriters.next();
		ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(buffStream);
		imageWriter.setOutput(imageOutputStream);

		ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();

		// Set the compress quality metrics
//		System.out.println(compressionQuality);
		imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		imageWriteParam.setCompressionQuality(compressionQuality);
		imageWriter.write(null, new IIOImage(bufferedImage, null, null), imageWriteParam);
		buffStream.close();

	}

	private static double getFileSizeKiloBytes(MultipartFile file) {
		return (double) file.getSize() / 1024;
	}

}
