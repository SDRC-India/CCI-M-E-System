package org.sdrc.scps.service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.Id;

import org.json.simple.JSONObject;
import org.sdrc.scps.domain.Area;
import org.sdrc.scps.domain.Attachment;
import org.sdrc.scps.domain.BuildingDetails;
import org.sdrc.scps.domain.InmatesData;
import org.sdrc.scps.domain.InstitutionData;
import org.sdrc.scps.domain.Options;
import org.sdrc.scps.domain.Question;
import org.sdrc.scps.domain.User;
import org.sdrc.scps.models.ApprovalRejectionModel;
import org.sdrc.scps.models.DataEntryQuestionModel;
import org.sdrc.scps.models.NotificationModel;
import org.sdrc.scps.models.OptionModel;
import org.sdrc.scps.models.QuestionModel;
import org.sdrc.scps.models.ResponseModel;
import org.sdrc.scps.repository.BuildingDetailsRepository;
import org.sdrc.scps.repository.InmatesDataRepository;
import org.sdrc.scps.repository.InstitutionDataRepository;
import org.sdrc.scps.repository.QuestionRepository;
import org.sdrc.scps.repository.UserRepository;
import org.sdrc.scps.util.Constants;
import org.sdrc.scps.util.SubmissionStatus;
import org.sdrc.scps.util.TokenInfoExtractor;
import org.sdrc.usermgmt.domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author Harsh Pratyush
 *
 */
@Service
@SuppressWarnings("rawtypes")
public class SubmissionServiceImpl implements SubmissionService {

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private TokenInfoExtractor tokenInfoExtractor;

	@Autowired
	private InstitutionDataRepository institutionDataRepository;

	@Autowired
	private InmatesDataRepository inmatesDataRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BuildingDetailsRepository buildingDetailsRepository;

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
//						System.out.println(value);
						responseMap.put(field.getName(), ((Area) value).getAreaName());
					} else if (value instanceof Options) {
//						System.out.println(value);
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
//		System.out.println(responseMap);

		return responseMap;
	}

//	private String getValuePrfecthed(Question question, JSONObject dataFromSession) {
//		JSONObject parsedData = new JSONObject();
//		for (String feature : question.getDefaultSetting().split(",")) {
//			feature = feature.trim();
//			if (feature.contains("fetchsession")) {
//				String jsonPath = feature.split(":")[1];
//
//				parsedData = dataFromSession;
//				for (String path : jsonPath.split("\\.")) {
//					if (parsedData.get(path) instanceof Map)
//
//						parsedData = new JSONObject((Map) parsedData.get(path));
//
//					else
//						return parsedData.get(path).toString();
//				}
//
//			}
//		}
//		return parsedData.toJSONString();
//	}

	@Override
	public Map<String, Object> getApprovalPendingList(OAuth2Authentication auth, int id) {
		Object dataFromSession = tokenInfoExtractor.getUserModelInfo(auth);
		JSONObject jsonObject = new JSONObject((Map) dataFromSession);
		Integer cciId = (id == Constants.INSTUTION_FORM)
				? Integer.parseInt(((new JSONObject((Map) jsonObject.get("area"))).get("areaId")).toString())
				: Integer.parseInt(((new JSONObject((Map) jsonObject.get("area"))).get("areaId")).toString());

		Map<String, Object> tableMap = new LinkedHashMap<String, Object>();
		Map<String, Object> prefetchedData = new LinkedHashMap<String, Object>();

		List<Object[]> headers = questionRepository.findReviewHeaderByFormId(
				id == Constants.INSTUTION_FORM ? Constants.INSTUTION_FORM : Constants.CHILD_FORM);

		Collections.sort(headers, new Comparator<Object[]>() {

			@Override
			public int compare(Object[] o1, Object[] o2) {
				return o1[1].toString().split("_")[0].compareTo(o2[1].toString().split("_")[0]);
			}
		});

		Map<Integer, Map<String, String>> formColumnName = new LinkedHashMap<Integer, Map<String, String>>();

		for (Object[] header : headers) {
			if (header[2].toString().equals("q26"))
				continue;

			Map<String, String> formColumnMap = null;
			if (formColumnName.containsKey(Integer.parseInt(header[0].toString()))) {
				formColumnMap = formColumnName.get(Integer.parseInt(header[0].toString()));
			} else
				formColumnMap = new LinkedHashMap<String, String>();

			formColumnMap.put(header[2].toString(),
					header[3] == null ? header[1].toString() : header[1].toString() + "@" + header[3].toString());

			formColumnName.put(Integer.parseInt(header[0].toString()), formColumnMap);
		}

		List<String> tableHeaders = new ArrayList<String>();

		for (String tableHeader : formColumnName
				.get(id == Constants.INSTUTION_FORM ? Constants.INSTUTION_FORM : Constants.CHILD_FORM).values()) {
			tableHeaders.add(tableHeader.split("@")[0].split("_")[1]);
		}
		
		if(id==Constants.CHILD_FORM)
		{
			tableHeaders.add("CCI Name");
		}
		tableHeaders.add("Status");
		tableHeaders.add("Action");
		tableMap.put("tableColumn", tableHeaders);


		List<Object> dataArray = new ArrayList<Object>();
		if (id == Constants.INSTUTION_FORM) {
			List<InstitutionData> institutionDatas = institutionDataRepository
					.findByIsLiveTrueAndSubmissionStatusAndQ11AreaId(SubmissionStatus.PENDING, cciId);
			
			for (InstitutionData institutionData : institutionDatas) {
				Map<String, Object> childData = new HashMap<String, Object>();
				prefetchedData = inspectForResponse(InstitutionData.class, institutionData);

				for (String keys : formColumnName.get(Constants.INSTUTION_FORM).keySet()) {
					childData.put(formColumnName.get(Constants.INSTUTION_FORM).get(keys).split("@")[0].split("_")[1],
							prefetchedData.get(keys) == null ? "N/A" : prefetchedData.get(keys).toString());
				}
				childData.put("id", institutionData.getInstutionId());

				childData.put("Action",
						Constants.getAction(institutionData.getSubmissionStatus(), institutionData.isLive()));
				childData.put("Status", institutionData.getSubmissionStatus());

				dataArray.add(childData);
			}
		} else {
			
			List<InmatesData> InmatesDatas = inmatesDataRepository
					.findByIsLiveTrueAndOwnerAreaParentAreaAreaIdAndSubmissionStatus(cciId, SubmissionStatus.PENDING);

			for (InmatesData inmatesData : InmatesDatas) {
				Map<String, Object> childData = new HashMap<String, Object>();
				prefetchedData = inspectForResponse(InmatesData.class, inmatesData);

				for (String keys : formColumnName.get(Constants.CHILD_FORM).keySet()) {
					childData.put(formColumnName.get(Constants.CHILD_FORM).get(keys).split("@")[0].split("_")[1],
							prefetchedData.get(keys) == null ? "N/A" : prefetchedData.get(keys).toString());
				}
				childData.put("CCI Name", inmatesData.getOwner().getArea().getAreaName());
				childData.put("id", inmatesData.getInmatesData());

				childData.put("Action", Constants.getAction(inmatesData.getSubmissionStatus(), inmatesData.isLive()));
				childData.put("Status", inmatesData.getSubmissionStatus());

				dataArray.add(childData);
			}
		}
		tableMap.put("tableData", dataArray);
		return tableMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sdrc.scps.service.SubmissionService#getViewDataForApproval(int,
	 * org.springframework.security.oauth2.provider.OAuth2Authentication,
	 * java.lang.Integer)
	 */
	@Override
	public List<DataEntryQuestionModel> getViewDataForApproval(int formId, OAuth2Authentication auth,
			Integer submissioId) throws Exception {

		QuestionModel questionModel = new QuestionModel();
		List<DataEntryQuestionModel> dataEntryQuestionModels = new ArrayList<DataEntryQuestionModel>();
		questionModel.setControlType("id");
		questionModel.setColumnName("id");
		questionModel.setKey(formId);

		InstitutionData approvedInstitution = null;
		
		List<Question> questions = questionRepository.findByFormFormIdAndIsLiveTrueOrderByQuestionOrderAsc(formId);

		Map<String, DataEntryQuestionModel> dataEntryQuestionModelMap = new LinkedHashMap<String, DataEntryQuestionModel>();

		List<Attachment> attachments = new ArrayList<Attachment>();

		Map<String, Object> prefetchedData = new LinkedHashMap<String, Object>();

		Set<Integer> rejectedSectionIds = new HashSet<Integer>();

		if (formId == Constants.INSTUTION_FORM) {

			InstitutionData institutionData = null;

			if (submissioId != null && submissioId != 0)
				institutionData = institutionDataRepository.findByInstutionId(submissioId);
			

				

			if (institutionData != null) {

				if (institutionData.getSubmissionStatus() != SubmissionStatus.APPROVED) {
					questionModel.setValue(institutionData.getInstutionId());
				}
				
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
				if (inmatesData.getSubmissionStatus() != SubmissionStatus.APPROVED) {
					questionModel.setValue(inmatesData.getInmatesData());
				}
				
				attachments = inmatesData.getAttachment();
				prefetchedData = inspect(InmatesData.class, inmatesData);
			}
			
			 approvedInstitution = institutionDataRepository
						.findByIsLiveTrueAndSubmissionStatusAndQ7AreaId(SubmissionStatus.APPROVED, inmatesData.getOwner().getArea().getAreaId());
			if (prefetchedData.get("qBuildingDetails") != null
					&& prefetchedData.get("qBuildingDetails").toString().trim() != "") {
				String value = prefetchedData.get("qBuildingDetails").toString();
				prefetchedData.put("qBuildingDetails",
						approvedInstitution.getBuildingDetails().stream()
								.filter(d -> Integer.parseInt(value)
										==d.getIndexTrackNum()).findFirst().get().getBuildingDetailsId());
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

			questionModel.setDisabled(question.isApprovalProcess() ? false : true);

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

//				String[] attachmentsStrings = prefetchedData.get(question.getColumnn()).toString().split(",");
				List<Attachment> attachmentQuestion = new ArrayList<Attachment>();
//				for (String attachmentString : attachmentsStrings) {
				// System.out.println(attachmentString);
				attachmentQuestion.addAll(attachments.stream()
						.filter(d -> d.getColumnName().equals(question.getColumnn())).collect(Collectors.toList()));
//				}
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
			
			if(question.getColumnn().equals("qBuildingDetails") && approvedInstitution!=null)
			{
				List<OptionModel> options = new ArrayList<OptionModel>();
				
				for(BuildingDetails buildingDetails:approvedInstitution.getBuildingDetails())
				{
					OptionModel option=new  OptionModel();
					option.setKey(buildingDetails.getBuildingDetailsId());
					option.setValue(buildingDetails.getQ33BuildingName());
					
					options.add(option);
				}
				questionModel.setOptions(options);
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

//							String[] attachmentsStrings = beginRepeatDataMap.get(clonedQuestionModel.getColumnName())
//									.toString().split(",");
							List<Attachment> attachmentQuestion = new ArrayList<Attachment>();
//							for (String attachmentString : attachmentsStrings) {
							attachmentQuestion.addAll(attachments.stream()
									.filter(d -> d.getColumnName()
											.equals(clonedQuestionModel.getColumnName() + "_"
													+ beginRepeatDataMap.get("indexTrackNum")))
									.collect(Collectors.toList()));
//							}
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

	private Map<String, Object> inspect(Class<?> className, Object institutionData) {
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

	private List<OptionModel> areaToOption(Area area) {
		List<OptionModel> optionModels = new ArrayList<OptionModel>();

		OptionModel optionModel = new OptionModel();
		optionModel.setKey(area.getAreaId());
		optionModel.setSelected(false);
		optionModel.setValue(area.getAreaName());
		optionModel.setOrder(area.getAreaId());
		optionModel.setParentKey(area.getParentArea()==null?0:area.getParentArea().getAreaId());
		optionModels.add(optionModel);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sdrc.scps.service.SubmissionService#approveRejectSubmission(org.sdrc.scps
	 * .models.ApprovalRejectionModel,
	 * org.springframework.security.oauth2.provider.OAuth2Authentication)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public ResponseModel approveRejectSubmission(ApprovalRejectionModel approvalRejectionModel,
			OAuth2Authentication auth) {
		ResponseModel responseModel = new ResponseModel();
		InstitutionData institutionData = new InstitutionData();
		InmatesData inmatesData = new InmatesData();
		InstitutionData approvedInstitutionForInmate = new InstitutionData();
		final ObjectMapper mapper = new ObjectMapper();

		Map<String, Object> datObject = new LinkedHashMap<String, Object>();
//		int submissionId = 0;
		List<QuestionModel> questionModels = approvalRejectionModel.getQuestionModels();
		for (QuestionModel questionModel : questionModels) {
			if (questionModel.getValue() != null) {
				switch (questionModel.getControlType()) {
				case "id":

					// if id is null then we will return error
					if (questionModel.getValue() == null
							|| Integer.parseInt(questionModel.getValue().toString()) == 0) {
						responseModel.setStatusCode(HttpStatus.NOT_FOUND.value());
						responseModel.setMessage("Data not found");
						return responseModel;
					}
//					submissionId = Integer.parseInt(questionModel.getValue().toString());
					
					if (questionModel.getKey() == Constants.CHILD_FORM) {

						// checking wether any other person has updated this record or not
						InmatesData data = inmatesDataRepository
								.findByIsLiveTrueAndInmatesData(Integer.parseInt(questionModel.getValue().toString()));
						if (data == null || data.getSubmissionStatus() != SubmissionStatus.PENDING) {
							responseModel.setStatusCode(HttpStatus.NOT_FOUND.value());
							responseModel.setMessage("Data not found");
							return responseModel;
						}
						
						approvedInstitutionForInmate = institutionDataRepository
								.findByIsLiveTrueAndSubmissionStatusAndQ7AreaId(SubmissionStatus.APPROVED, data.getOwner().getArea().getAreaId());

						datObject.put(Stream.of(InmatesData.class.getDeclaredFields())
								.filter(field -> field.isAnnotationPresent(Id.class)).findFirst().get().getName(),
								Integer.parseInt(questionModel.getValue().toString()));
					}

					else {
						// checking wether any other person has updated this record or not
						InstitutionData data = institutionDataRepository
								.findByIsLiveTrueAndInstutionId(Integer.parseInt(questionModel.getValue().toString()));
						if (data == null || data.getSubmissionStatus() != SubmissionStatus.PENDING) {
							responseModel.setStatusCode(HttpStatus.NOT_FOUND.value());
							responseModel.setMessage("Data not found");
							return responseModel;

						}

						datObject.put(Stream.of(InstitutionData.class.getDeclaredFields())
								.filter(field -> field.isAnnotationPresent(Id.class)).findFirst().get().getName(),
								Integer.parseInt(questionModel.getValue().toString()));

					}

					break;
				case "dropdown":
				case "radio":
					if (questionModel.getValue().toString().trim().equals("")) {
						continue;
					}
					if (questionModel.getColumnName().equalsIgnoreCase("qBuildingDetails") && approvedInstitutionForInmate!=null)
						datObject.put(questionModel.getColumnName(),
								approvedInstitutionForInmate.getBuildingDetails().stream()
										.filter(d -> d.getBuildingDetailsId() == Integer
												.parseInt(questionModel.getValue().toString())).findFirst().get().getIndexTrackNum());
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

					break;
				default:
					datObject.put(questionModel.getColumnName(), questionModel.getValue());
				}

			}

		}

		if (approvalRejectionModel.getFormId() == Constants.INSTUTION_FORM) {
			institutionData = mapper.convertValue(datObject, InstitutionData.class);
			institutionData.setLive(true);
			if (approvalRejectionModel.isApproved())
			{
				institutionData.setSubmissionStatus(SubmissionStatus.APPROVED);
				InstitutionData approvedInstitution =	institutionDataRepository.findByIsLiveTrueAndSubmissionStatusAndQ7AreaId(SubmissionStatus.APPROVED,institutionData.getQ7().getAreaId());
				if(approvedInstitution!=null)
				{
					approvedInstitution.setLive(false);
					institutionDataRepository.save(approvedInstitution);
				}
				
				
			}
			else {
				institutionData.setSubmissionStatus(SubmissionStatus.REJECTED);
				institutionData.setRemarks(approvalRejectionModel.getRemarks());
				String rejectedSection = null;
				for (Integer rejctedSectionId : approvalRejectionModel.getRejectedSections()) {
					if (rejectedSection == null) {
						rejectedSection = rejctedSectionId.toString();
					} else {
						rejectedSection += "," + rejctedSectionId;
					}

				}
				institutionData.setRejectedSections(rejectedSection);
			}
			institutionData.setDeleted(false);

			institutionData.setUpdatedBy(
					new Account(Integer.parseInt(tokenInfoExtractor.tokenInfo(auth).get("userId").toString())));
			
			

			institutionData = institutionDataRepository.save(institutionData);
			if(approvalRejectionModel.isApproved())
			{
				buildingDetailsRepository.updateStatus(institutionData.getInstutionId(), false);
			}

		}

		else {
			inmatesData = mapper.convertValue(datObject, InmatesData.class);
			inmatesData.setOwner(inmatesDataRepository.findByInmatesData(inmatesData.getInmatesData()).getOwner());
			inmatesData.setLive(true);
			if (approvalRejectionModel.isApproved())
			{
				inmatesData.setSubmissionStatus(SubmissionStatus.APPROVED);
				if(inmatesData.getQ50()!=null && inmatesData.getQ50().getOptionId()==Constants.OPTION_YES)
				{
					if(inmatesData.getQ56()!=null)
					{
						inmatesData.setOwner(userRepository.findByArea(inmatesData.getQ56()).get(0));
					}
				}
				
				InmatesData approvedInmate =	inmatesDataRepository.findByIsLiveTrueAndQ1AndSubmissionStatus(inmatesData.getQ1(), SubmissionStatus.APPROVED);
				if(approvedInmate!=null)
				{
					approvedInmate.setLive(false);
					inmatesDataRepository.save(approvedInmate);
				}
			}
			else {
				inmatesData.setSubmissionStatus(SubmissionStatus.REJECTED);
				inmatesData.setRemarks(approvalRejectionModel.getRemarks());
				String rejectedSection = null;
				for (Integer rejctedSectionId : approvalRejectionModel.getRejectedSections()) {
					if (rejectedSection == null) {
						rejectedSection = rejctedSectionId.toString();
					} else {
						rejectedSection += "," + rejctedSectionId;
					}

				}
				inmatesData.setRejectedSections(rejectedSection);
			}

			inmatesData.setUpdatedBy(
					new Account(Integer.parseInt(tokenInfoExtractor.tokenInfo(auth).get("userId").toString())));

//			inmatesData.setOwner(userRepository.findByAccount(
//					new Account(Integer.parseInt(tokenInfoExtractor.tokenInfo(auth).get("userId").toString()))));
			inmatesData = inmatesDataRepository.save(inmatesData);

		}
		responseModel.setStatusCode(HttpStatus.OK.value());
		responseModel.setMessage("Successfuly " + (approvalRejectionModel.isApproved() ? "Approved" : "Rejected"));

		return responseModel;
	}

	/* (non-Javadoc)
	 * @see org.sdrc.scps.service.SubmissionService#getPendingNotification(org.springframework.security.oauth2.provider.OAuth2Authentication)
	 */
	@Override
	public NotificationModel getPendingNotification(OAuth2Authentication auth) {
		Object dataFromSession = tokenInfoExtractor.getUserModelInfo(auth);
		JSONObject jsonObject = new JSONObject((Map) dataFromSession);
		Integer cciId =  Integer.parseInt(((new JSONObject((Map) jsonObject.get("area"))).get("areaId")).toString());
		
//		Map<Integer, Integer> inmateCountMap = new HashMap<Integer, Integer>();
		
//		int exceedingCCIs=0;
//		List<Object[]> inmatesCountDatas = inmatesDataRepository
//				.findCountDataOfInmatesWithPendingData(SubmissionStatus.PENDING,cciId,Arrays.asList(SubmissionStatus.APPROVED,SubmissionStatus.PENDING));
//		
//		List<Object[]> sanctionedStrengths = institutionDataRepository.findSanctionedStrengthForEachCCIInDistrict(SubmissionStatus.APPROVED,cciId);
//
//		for (Object[] inmatesCountData : inmatesCountDatas) {
//			inmateCountMap.put(Integer.parseInt(inmatesCountData[1].toString()), Integer.parseInt(inmatesCountData[0].toString()));
//		}
		
//		for (Object[] sanctionedStrength : sanctionedStrengths) {
//			if(inmateCountMap.containsKey(Integer.parseInt(sanctionedStrength[1].toString())) && 
//					inmateCountMap.get(Integer.parseInt(sanctionedStrength[1].toString()))>Integer.parseInt(sanctionedStrength[0].toString()))
//			{
//				exceedingCCIs++;
//			}
//		}
		
		
		int institutionCount = institutionDataRepository
				.findByIsLiveTrueAndSubmissionStatusAndQ11AreaIdCount(SubmissionStatus.PENDING, cciId);
		int inmatesDatasCount = inmatesDataRepository
				.findByIsLiveTrueAndOwnerAreaParentAreaAreaIdAndSubmissionStatusCount(cciId, SubmissionStatus.PENDING);
		
		NotificationModel notificationModel = new NotificationModel();
		notificationModel.setInmatesNo(inmatesDatasCount);
		notificationModel.setInstitutionNo(institutionCount);
//		notificationModel.setExceedingChilds(exceedingCCIs);
		
				return notificationModel;
	}
}
