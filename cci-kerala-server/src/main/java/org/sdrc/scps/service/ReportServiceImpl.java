/**
 * 
 */
package org.sdrc.scps.service;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import org.sdrc.scps.domain.Area;
import org.sdrc.scps.domain.Attachment;
import org.sdrc.scps.domain.InmatesData;
import org.sdrc.scps.domain.Options;
import org.sdrc.scps.domain.Question;
import org.sdrc.scps.domain.SummaryReportIndicator;
import org.sdrc.scps.domain.User;
import org.sdrc.scps.models.SummaryReportModel;
import org.sdrc.scps.repository.AreaRepository;
import org.sdrc.scps.repository.BuildingDetailsRepository;
import org.sdrc.scps.repository.InmatesDataRepository;
import org.sdrc.scps.repository.InstitutionDataRepository;
import org.sdrc.scps.repository.QuestionRepository;
import org.sdrc.scps.repository.SummaryReportIndicatorRepository;
import org.sdrc.scps.util.Constants;
import org.sdrc.scps.util.SubmissionStatus;
import org.sdrc.scps.util.TokenInfoExtractor;
import org.sdrc.usermgmt.domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * @author Harsh Pratyush
 *
 */
@Service
public class ReportServiceImpl implements ReportService {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sdrc.scps.service.ReportService#generateInmatesRawData()
	 */

	@Autowired
	private TokenInfoExtractor tokenInfoExtractor;

	@Autowired
	private InmatesDataRepository inmatesDataRepository;

	@Autowired
	private AreaRepository areaRepository;

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private BuildingDetailsRepository buildingDetailsRepository;

	@Autowired
	private InstitutionDataRepository institutionDataRepository;

	@Autowired
	private SummaryReportIndicatorRepository summaryReportIndicatorRepository;

	@Override
	public File generateInmatesRawData(OAuth2Authentication auth, int districtId) {

		File fileWritten = null;
		try {

			String path = Constants.TEMP_DIRC; // messageSource.getMessage("childinfo.output.path",
												// null, null);
			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}

			String filename = path + "Data_Entry_Template_";

			FileOutputStream outputStream;
			XSSFWorkbook workbook = new XSSFWorkbook();

			XSSFFont font = workbook.createFont();
			font.setBold(true);
			CellStyle style = workbook.createCellStyle();

			// style.setAlignment(CellStyle.ALIGN_CENTER);
			// Setting font to style
			style.setFont(font);

			// Create a Sheet
			XSSFSheet sheet1 = workbook.createSheet("RawData");
			List<InmatesData> InmatesDatas = inmatesDataRepository
					.findByIsLiveTrueAndOwnerAreaParentAreaAreaIdAndSubmissionStatus(
							districtId, SubmissionStatus.APPROVED);
			List<Question> questions = questionRepository
					.findByFormFormIdAndIsLiveTrueOrderByQuestionOrderAsc(Constants.CHILD_FORM);
			// List<Object[]> areaDataRetrived =
			// areaJpaRepository.getAllAreaNames();

			List<Object[]> buildingDetailsOfWholeDistrict = buildingDetailsRepository
					.findBuildingNameOfAllInstitutionOfaDistrict(districtId,
							SubmissionStatus.APPROVED);
			setValueInSheet(sheet1, questions, InmatesDatas, style,
					buildingDetailsOfWholeDistrict);

			// Write the output to a file
			fileWritten = File.createTempFile(filename + "_", ".xlsx");
			outputStream = new FileOutputStream(fileWritten);
			workbook.write(outputStream);
			workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
			// LOGGER.error("Error while workbook write."+e);
		}
		return fileWritten;
	}

	private void setValueInSheet(XSSFSheet sheet, List<Question> questions,
			List<InmatesData> dataRetrived, CellStyle style,
			List<Object[]> buildingDetailsOfWholeDistrict) {
		int rowNum = 0;
		int colNum = 0;
		// Create a Row
		XSSFRow headerRow = sheet.createRow(rowNum++);
		Cell cell = headerRow.createCell(colNum);

		headerRow = sheet.createRow(0);

		int i = 0;
		cell = headerRow.createCell(i);
		cell.setCellValue("Si No.");
		cell.setCellStyle(style);
		i++;

		cell = headerRow.createCell(i);
		cell.setCellValue("CCI Name");
		cell.setCellStyle(style);
		i++;
		Map<String, Map<String, Object>> headerDataMap = new LinkedHashMap<String, Map<String, Object>>();
		for (Question question : questions) {

			if (!question.getControlType().equals("heading")
					&& !question.getControlType().contains("beginRepeat")
					&& question.getGroupId() == null) {
				cell = headerRow.createCell(i);
				cell.setCellValue(question.getQuestionName());
				cell.setCellStyle(style);
			}
			Map<String, Object> headerData = new LinkedHashMap<String, Object>();
			headerData.put("question", question);
			headerData.put("index", i);
			if (question.getControlType().equals("multiSelect")) {
				headerData.put("options", question
						.getQuestionOptionTypeMapping().getOptionType()
						.getOptions());
			}
			if (!question.getControlType().equals("heading")
					&& !question.getControlType().contains("beginRepeat")
					&& question.getGroupId() == null) {
				headerDataMap.put(question.getColumnn(), headerData);
			}
			if (!question.getControlType().equals("heading")
					&& !question.getControlType().contains("beginRepeat")
					&& question.getGroupId() == null)
				i++;
		}
		XSSFRow row;
		int siNo = 1;
		if (dataRetrived != null) {

			for (InmatesData inmatesData : dataRetrived) {
				List<Attachment> attachments = inmatesData.getAttachment();
				row = sheet.createRow(rowNum++);
				cell = row.createCell(0);
				cell.setCellValue(siNo++);
				cell = row.createCell(1);
				Area cci = inmatesData.getOwner().getArea();
				cell.setCellValue(cci.getAreaName());

				Map<String, Object> response = inspectForResponse(
						InmatesData.class, inmatesData);
				String value = response.get("qBuildingDetails").toString();
				response.put(
						"qBuildingDetails",
						buildingDetailsOfWholeDistrict
								.stream()
								.filter(d -> Integer.parseInt(value) == Integer
										.parseInt(d[0].toString())
										&& Integer.parseInt(d[2].toString()) == cci
												.getAreaId()).findFirst().get()[1]);

				for (String key : response.keySet()) {
					if (headerDataMap.containsKey(key)) {
						Question question = (Question) headerDataMap.get(key)
								.get("question");

						if (question.getControlType().equals("multiSelect")
								&& response.get(key).toString().trim() != "") {
							String valuesFromOptions = "";

							for (Integer id : Arrays
									.asList(response.get(key).toString()
											.split(",")).stream()
									.mapToInt(Integer::parseInt).toArray()) {
								@SuppressWarnings("unchecked")
								List<Options> options = (List<Options>) headerDataMap
										.get(key).get("options");
								String option = options.stream()
										.filter(d -> d.getOptionId() == id)
										.findFirst().get().getOptionName();
								if (valuesFromOptions == "") {
									valuesFromOptions = option;
								} else {
									valuesFromOptions += "," + option;
								}
							}
							response.put(key, valuesFromOptions);
						}

						if (question.getControlType().equals("file")
								&& response.get(key).toString().trim() != "") {

							String files = "";
							for (Attachment attachment : attachments
									.stream()
									.filter(d -> d.getColumnName().equals(
											question.getColumnn()))
									.collect(Collectors.toList())) {
								if (files == "") {
									files = attachment.getOriginalName();
								} else {
									files += "," + attachment.getOriginalName();
								}

							}
							response.put(key, files);
						}

						cell = row.createCell(Integer.parseInt(headerDataMap
								.get(key).get("index").toString()));
						cell.setCellValue(response.get(key).toString());
					}
				}
			}
		}

		sheet.createFreezePane(0, 1);

	}

	private Map<String, Object> inspectForResponse(Class<?> className,
			Object institutionData) {
		Field[] fields = className.getDeclaredFields();
		Map<String, Object> responseMap = new HashMap<String, Object>();

		for (Field field : fields) {
			if (field.getType().getSimpleName() == "boolean") {
				continue;
			}

			try {
				String getter = "get"
						+ field.getName().substring(0, 1).toUpperCase()
						+ field.getName().substring(1);
				Method method = className.getMethod(getter);
				Object value = "";
				value = method.invoke(institutionData);

				if (value != null) {

					if (value instanceof Area) {
						responseMap.put(field.getName(),
								((Area) value).getAreaName());
						responseMap.put(field.getName() + "_id",
								((Area) value).getAreaId());
					} else if (value instanceof Options) {
						responseMap.put(field.getName(),
								((Options) value).getOptionName());
						responseMap.put(field.getName() + "_id",
								((Options) value).getOptionId());
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

		return responseMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sdrc.scps.service.ReportService#getDistrict()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List<Area> getDistrict(OAuth2Authentication auth) {
		Object dataFromSession = tokenInfoExtractor.getUserModelInfo(auth);
		JSONObject jsonObject = new JSONObject((Map) dataFromSession);
		List<Area> districts = new ArrayList<Area>();
		// Integer areaId = Integer.parseInt(((new JSONObject((Map)
		// jsonObject.get("area"))).get("areaId")).toString());
		Integer areaLevelId = Integer.parseInt(new JSONObject(
				(Map) (new JSONObject((Map) jsonObject.get("area")))
						.get("areaLevel")).get("areaLevelId").toString());
		final ObjectMapper mapper = new ObjectMapper();
		if (areaLevelId == Constants.DISTRICT_LEVEL) {
			districts.add(mapper.convertValue(((Map) jsonObject.get("area")),
					Area.class));
		}

		else {
			districts = areaRepository
					.findByAreaLevelAreaLevelIdAndIsLiveTrueOrderByAreaNameAsc(Constants.DISTRICT_LEVEL);
		}

		return districts;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sdrc.scps.service.ReportService#getSummaryReportTable(org.springframework
	 * .security.oauth2.provider.OAuth2Authentication)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public SummaryReportModel getSummaryReportTable(OAuth2Authentication auth,
			int areaId, int areaLevelId) {

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		
		SummaryReportModel summaryReportModel = new SummaryReportModel();

		Object dataFromSession = tokenInfoExtractor.getUserModelInfo(auth);

		
		Set<String> cciCoreIndicator= new LinkedHashSet<String>();
		//getting all the summary report indicator
		List<SummaryReportIndicator> summaryReportIndicators = summaryReportIndicatorRepository
				.findByIsLiveTrueOrderByIndicatorOrderAsc();

		List<Object[]> institutionDatas = null;
		List<Object[]> inmatesDatas = null;
		
		Map<Integer,List<List<String>>> institutionDataMap = new HashMap<Integer,List<List<String>>>();
		
		Map<Integer,List<List<String>>> inmatesDataMap = new HashMap<Integer,List<List<String>>>();

		List<Area> districts = new ArrayList<Area>();

		JSONObject jsonObject = new JSONObject((Map) dataFromSession);

		// we will get data according to user level
		if (areaId == 0)
			areaId = Integer.parseInt(((new JSONObject((Map) jsonObject
					.get("area"))).get("areaId")).toString());

		if (areaLevelId == 0)
			areaLevelId = Integer.parseInt(new JSONObject(
					(Map) (new JSONObject((Map) jsonObject.get("area")))
							.get("areaLevel")).get("areaLevelId").toString());

		if (areaLevelId == 1) {
			institutionDatas = institutionDataRepository.findTheStatusReportForACountryAndSubmissionStatus(SubmissionStatus.APPROVED);
			inmatesDatas = inmatesDataRepository.findTheStatusReportForACountryAndSubmissionStatus(SubmissionStatus.APPROVED.toString());
			districts
					.addAll(areaRepository
							.findByAreaLevelAreaLevelIdAndIsLiveTrueOrderByAreaNameAsc(Constants.DISTRICT_LEVEL));
		} else if (areaLevelId == 2) {
			institutionDatas = institutionDataRepository.findTheStatusReportForAStateAndSubmissionStatus(areaId,SubmissionStatus.APPROVED);
			inmatesDatas = inmatesDataRepository.findTheStatusReportForAStateAndSubmissionStatus(areaId,SubmissionStatus.APPROVED.toString());
			districts
					.addAll(areaRepository
							.findByAreaLevelAreaLevelIdAndIsLiveTrueOrderByAreaNameAsc(Constants.DISTRICT_LEVEL));
		} else {
			institutionDatas = institutionDataRepository.findTheStatusReportForADistrictAndSubmissionStatus(areaId,SubmissionStatus.APPROVED);
			districts
			.add(areaRepository
					.findByAreaId(areaId));
			inmatesDatas = inmatesDataRepository.findTheStatusReportForADistrictAndSubmissionStatus(areaId,SubmissionStatus.APPROVED.toString());;

		}
		List<String> tableHeaders = new ArrayList<String>();
		
		//putting data against the index of it in object [] of string
		for(Object data[]:institutionDatas)
		{
			int i=0;
			for(Object value:data)
			{
				
				if(value!=null)
				{
					if(institutionDataMap.containsKey(i))
					{
						List<String> valueData=new ArrayList<String>();
						valueData.add(value.toString());
						valueData.add(data[data.length-1].toString());
						institutionDataMap.get(i).add(valueData);
					}
					
					else
					{
						List<List<String>> valueDatas= new ArrayList<List<String>>();
						List<String> valueData=new ArrayList<String>();
						valueData.add(value.toString());
						valueData.add(data[data.length-1].toString());
						valueDatas.add(valueData);
						institutionDataMap.put(i, valueDatas);
					}
				}
				
				i++;
			}
		}
		
		//putting data against the index of it in object [] of string
		for(Object data[]:inmatesDatas)
		{
			int i=0;
			for(Object value:data)
			{
				
				if(value!=null)
				{
					if(inmatesDataMap.containsKey(i))
					{
						List<String> valueData=new ArrayList<String>();
						valueData.add(value.toString());
						valueData.add(data[data.length-1].toString());
						inmatesDataMap.get(i).add(valueData);
					}
					
					else
					{
						List<List<String>> valueDatas= new ArrayList<List<String>>();
						List<String> valueData=new ArrayList<String>();
						valueData.add(value.toString());
						valueData.add(data[data.length-1].toString());
						valueDatas.add(valueData);
						inmatesDataMap.put(i, valueDatas);
					}
				}
				
				i++;
			}
		}

		tableHeaders.add("Indicator");
		tableHeaders.add("Kerala");
		
		districts.forEach(district->{
			tableHeaders.add(district.getAreaName());
		});
		
		Map<String, Object> areaDefaultValueMap = new HashMap<String,Object>();
		for(Area district:districts)
		{
			areaDefaultValueMap.put(district.getAreaName(), 0);
		}
		
	List<Map<String, Object>> tableData = new ArrayList<Map<String, Object>>();
		for(SummaryReportIndicator summaryReportIndicator:summaryReportIndicators)
		{
			Map<String, Object> tableDataMap = new HashMap<String,Object>();
			
			
			
			tableDataMap.put("Indicator", summaryReportIndicator.getIndicatorName());
			tableDataMap.put("coreIndicator", summaryReportIndicator.getCoreIndicator());
			if(!cciCoreIndicator.contains(summaryReportIndicator.getCoreIndicator()))
			cciCoreIndicator.add(summaryReportIndicator.getCoreIndicator());
			
			switch(summaryReportIndicator.getIndicatorTable())
			{
			
			case INMATES:
				if(inmatesDataMap.containsKey(summaryReportIndicator.getQueryIndex())) {
					int totalValue = 0;
					//gettinng data from the inmatedatamap according to query index defined in the db
					for(List<String> values: inmatesDataMap.get(summaryReportIndicator.getQueryIndex()))
					{
						tableDataMap.put(values.get(1), (int)Double.parseDouble(values.get(0)));
						totalValue = totalValue + (int)Double.parseDouble(values.get(0));

					}
				    tableDataMap.put("Kerala", totalValue);
				}
				break;
				
				
			case INSTITUTION:
				if(institutionDataMap.containsKey(summaryReportIndicator.getQueryIndex())) {
					int totalValue = 0;
					//gettinng data from the institutionDataMap according to query index defined in the db
					for(List<String> values: institutionDataMap.get(summaryReportIndicator.getQueryIndex()))
					{
						tableDataMap.put(values.get(1), (int)Double.parseDouble(values.get(0)));
						totalValue = totalValue + (int)Double.parseDouble(values.get(0));

					}
				    tableDataMap.put("Kerala", totalValue);
				}
				break;
			}
			
			areaDefaultValueMap.forEach(tableDataMap::putIfAbsent);
			
			tableData.add(tableDataMap);
		}
		
		
		
		summaryReportModel.setTableColumn(tableHeaders);
		summaryReportModel.setCoreIndicator(cciCoreIndicator);
		summaryReportModel.setTableData(tableData);
		summaryReportModel.setReportGenerationDate(dateFormat.format(date));

		return summaryReportModel;
	}

}
