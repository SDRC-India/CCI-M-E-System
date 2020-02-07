/**
 * 
 */
package org.sdrc.scps.service;

import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.time.DateUtils;
import org.json.simple.JSONObject;
import org.sdrc.scps.domain.Area;
import org.sdrc.scps.domain.Attachment;
import org.sdrc.scps.domain.InmatesData;
import org.sdrc.scps.domain.InstitutionData;
import org.sdrc.scps.domain.Options;
import org.sdrc.scps.domain.User;
import org.sdrc.scps.models.ChartData;
import org.sdrc.scps.models.DashboardLandingPage;
import org.sdrc.scps.models.DashboardLandingPageData;
import org.sdrc.scps.models.DataEntryQuestionModel;
import org.sdrc.scps.models.GroupedIndicator;
import org.sdrc.scps.models.QuestionModel;
import org.sdrc.scps.models.QuickStats;
import org.sdrc.scps.repository.AreaRepository;
import org.sdrc.scps.repository.InmatesDataRepository;
import org.sdrc.scps.repository.InstitutionDataRepository;
import org.sdrc.scps.repository.OptionRepositry;
import org.sdrc.scps.repository.QuestionRepository;
import org.sdrc.scps.util.Constants;
import org.sdrc.scps.util.HeaderFooter;
import org.sdrc.scps.util.SubmissionStatus;
import org.sdrc.scps.util.TokenInfoExtractor;
import org.sdrc.usermgmt.domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import com.itextpdf.text.pdf.draw.LineSeparator;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */

@SuppressWarnings("rawtypes")
@Service
public class DashboardServiceImpl implements DashboardService {

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private TokenInfoExtractor tokenInfoExtractor;

	@Autowired
	private InstitutionDataRepository institutionDataRepository;

	@Autowired
	private InmatesDataRepository inmatesDataRepository;

	@Autowired
	private AreaRepository areaRepository;

	@Autowired
	private OptionRepositry optionRepositry;

	private final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY");
	private final SimpleDateFormat sdfFull = new SimpleDateFormat("dd-MM-YYYY HH-mm-ss");

	private final List<String> colorCodes = Arrays.asList("#ab7253", "#eb9c73", "#eacb9f", "#428ead");
//	private final SimpleDateFormat sdfDateType = new SimpleDateFormat("yyyy-MM-dd");

	private final Path tempPath = Paths.get(Constants.TEMP_DIRC);

	private static DecimalFormat df = new DecimalFormat(".##");

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sdrc.scps.service.DashboardService#getLandingPageData(org.springframework
	 * .security.oauth2.provider.OAuth2Authentication)
	 */

	@Override
	public DashboardLandingPageData getLandingPageData(OAuth2Authentication auth, int areaId, int areaLevelId) {

		DashboardLandingPageData dashboardLandingPageData = new DashboardLandingPageData();

		Object dataFromSession = tokenInfoExtractor.getUserModelInfo(auth);
		List<Area> districts = new ArrayList<Area>();

		Area allArea = new Area();

		allArea.setAreaId(0);
		allArea.setAreaName("All");

		JSONObject jsonObject = new JSONObject((Map) dataFromSession);
		
		// checking area details from the session

		if (areaId == 0)
			areaId = Integer.parseInt(((new JSONObject((Map) jsonObject.get("area"))).get("areaId")).toString());

		if (areaLevelId == 0)
			areaLevelId = Integer
					.parseInt(new JSONObject((Map) (new JSONObject((Map) jsonObject.get("area"))).get("areaLevel"))
							.get("areaLevelId").toString());
		

		Map<String, Object> prefetchedData = new LinkedHashMap<String, Object>();

		// getting all the question which are having the header true 
		List<Object[]> headers = questionRepository.findReviewHeaderByFormId(Constants.INSTUTION_FORM);

		// getting all the option with option type of cci type
		List<Options> options = optionRepositry.findByOptionTypeOptionTypeId(Constants.CCI_TYPE_ID);

		Map<Integer, Integer> cciTypeCountMap = new HashMap<Integer, Integer>();

		Map<String, Integer> cciTypeCountMapExpiredLicense = new HashMap<String, Integer>();

		Map<String, Integer> cciTypeCountMapExpiringLicense = new HashMap<String, Integer>();

		Map<String, Integer> cciTypeGender = new HashMap<String, Integer>();

		Date currentDate = new Date();

		// setting calendar to 6 month after current date to check which ccis are having expiry date within 6 months
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentDate);
		calendar.add(Calendar.MONTH, 6);

		Date after6MonthDate = calendar.getTime();


		GroupedIndicator groupedIndicatorData = new GroupedIndicator();
		Set<String> availableChart = new HashSet<String>();
		List<ChartData> chartDataList = new ArrayList<ChartData>();
		Map<String,String> legend = new LinkedHashMap<String,String>();
		int legendNo=0;
		Collections.sort(headers, new Comparator<Object[]>() {

			@Override
			public int compare(Object[] o1, Object[] o2) {
				return o1[1].toString().split("_")[0].compareTo(o2[1].toString().split("_")[0]);
			}
		});

		Map<Integer, Map<String, String>> formColumnName = new LinkedHashMap<Integer, Map<String, String>>();

		// setting table header from headers list 
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

		List<String> tableHeaders = new ArrayList<String>();

		for (String tableHeader : formColumnName.get(Constants.INSTUTION_FORM).values()) {
			tableHeaders.add(tableHeader.split("@")[0].split("_")[1]);
		}

		tableHeaders.add("Average Sq. ft. area per child");
		tableHeaders.add("Maximum Strength Exceeded");
		tableHeaders.add("Action");
		dashboardLandingPageData.setTableColumn(tableHeaders);

		List<InstitutionData> institutionDatas = new ArrayList<InstitutionData>();
		List<Object[]> inmatesDatas = null;
		List<Object[]> avgSquareFit = null;
		Map<Integer, String> avgSqftData = new HashMap<Integer, String>();

		Map<Integer, String> inmateCountMap = new HashMap<Integer, String>();
		Map<Integer, Integer> inmateCountDistrictWiseMap = new HashMap<Integer, Integer>();


		

		List<Object[]> inmatesCountDatas = inmatesDataRepository
				.findCountDataByIsLiveTrueAndOwnerAreaAreaId(SubmissionStatus.APPROVED);

		List<Object[]> buildingDetailsForCCI = new ArrayList<Object[]>();

		Map<Integer, String> avgSqftDataCCI = new HashMap<Integer, String>();

		for (Object[] inmatesCountData : inmatesCountDatas) {
			inmateCountMap.put(Integer.parseInt(inmatesCountData[1].toString()), inmatesCountData[0].toString());
		}

		// we will get the the details according to area level id
		Map<String, Integer> cciDistributionList = new HashMap<String, Integer>();
		if (areaLevelId == 1) {
			institutionDatas = institutionDataRepository
					.findByIsDeletedFalseAndIsLiveTrueAndSubmissionStatusOrderByUpdatedDateDesc(
							SubmissionStatus.APPROVED);
			inmatesDatas = inmatesDataRepository.findDataByIsLiveTrueAndOwnerCountry(SubmissionStatus.APPROVED);
			avgSquareFit = institutionDataRepository.findAvgSqftByIsLiveTrueAndOwnerCountry(SubmissionStatus.APPROVED);
			districts.add(allArea);
			districts.addAll(
					areaRepository.findByAreaLevelAreaLevelIdAndIsLiveTrueOrderByAreaNameAsc(Constants.DISTRICT_LEVEL));
			buildingDetailsForCCI = institutionDataRepository
					.findAvgSqftForCCIIsLiveTrueAndOwnerCountry(SubmissionStatus.APPROVED);
		} else if (areaLevelId == 2) {
			institutionDatas = institutionDataRepository
					.findByIsDeletedFalseAndIsLiveTrueAndSubmissionStatusAndQ10OrderByUpdatedDateDesc(
							SubmissionStatus.APPROVED, new Area(areaId));
			inmatesDatas = inmatesDataRepository.findDataByIsLiveTrueAndOwnerState(areaId, SubmissionStatus.APPROVED);
			avgSquareFit = institutionDataRepository.findAvgSqftByIsLiveTrueAndOwnerState(areaId,
					SubmissionStatus.APPROVED);

			districts.add(allArea);
			districts.addAll(areaRepository
					.findByAreaLevelAreaLevelIdAndIsLiveTrueOrderByAreaNameAsc(Constants.DISTRICT_LEVEL));
			buildingDetailsForCCI = institutionDataRepository.findAvgSqftForCCIIsLiveTrueAndOwnerState(areaId,
					SubmissionStatus.APPROVED);
		} else {
			institutionDatas = institutionDataRepository
					.findByIsDeletedFalseAndIsLiveTrueAndSubmissionStatusAndQ11OrderByUpdatedDateDesc(
							SubmissionStatus.APPROVED, new Area(areaId));

			inmatesDatas = inmatesDataRepository.findDataByIsLiveTrueAndOwnerDistrict(areaId,
					SubmissionStatus.APPROVED);

			buildingDetailsForCCI = institutionDataRepository.findAvgSqftForCCIIsLiveTrueAndOwnerDistrict(areaId,
					SubmissionStatus.APPROVED);

			avgSquareFit = new ArrayList<Object[]>();
		}

		// setting avg sqft against institution
		avgSquareFit.forEach(d -> {
			avgSqftData.put(Integer.parseInt(d[2].toString()), d[0].toString());
		});

		// building details against ccis
		buildingDetailsForCCI.forEach(d -> {
			avgSqftDataCCI.put(Integer.parseInt(d[1].toString()), d[0].toString());
		});
		Integer sanctionedStrength = 0;
		int blacklistedCCI = 0, policeReport = 0,exceedingMaximumStringth=0;

		List<Map<String, Object>> dataArray = new ArrayList<Map<String, Object>>();
		for (InstitutionData institutionData : institutionDatas) {
			Map<String, Object> childData = new HashMap<String, Object>();
			prefetchedData = inspectForResponse(InstitutionData.class, institutionData);
			Area cci=institutionData.getQ7();
			for (String keys : formColumnName.get(Constants.INSTUTION_FORM).keySet()) {

				if (keys.equals("q26")) {
					childData.put(formColumnName.get(Constants.INSTUTION_FORM).get(keys).split("@")[0].split("_")[1],
							inmateCountMap.containsKey(cci.getAreaId())
									? inmateCountMap.get(cci.getAreaId())
									: 0);
					
					if(inmateCountMap.containsKey(cci.getAreaId()) && Integer.parseInt(inmateCountMap.get(cci.getAreaId())) >Integer.parseInt(institutionData.getQ25()))
					{
						exceedingMaximumStringth++;
					}
					
					if(inmateCountMap.containsKey(cci.getAreaId()) && Integer.parseInt(inmateCountMap.get(cci.getAreaId())) >Integer.parseInt(institutionData.getQ25()))
					childData.put("Maximum Strength Exceeded", "Yes");
					
					else
						childData.put("Maximum Strength Exceeded", "No");
				}

				else if (keys.equals("q7")) {
					List<Map<String, String>> actionDetails = new ArrayList<Map<String, String>>();
					Map<String, String> actionDetailsMap = new LinkedHashMap<String, String>();
					actionDetailsMap.put("controlType", "link");
					actionDetailsMap.put("value",
							prefetchedData.get(keys) == null ? "N/A" : prefetchedData.get(keys).toString());
					actionDetailsMap.put("class", "approved-child");
					actionDetailsMap.put("tooltip", "View Child Data");
					actionDetails.add(actionDetailsMap);
					childData.put(formColumnName.get(Constants.INSTUTION_FORM).get(keys).split("@")[0].split("_")[1],
							actionDetails);
				}

				else
					childData.put(formColumnName.get(Constants.INSTUTION_FORM).get(keys).split("@")[0].split("_")[1],
							prefetchedData.get(keys) == null ? "N/A" : prefetchedData.get(keys).toString());

			}
			int count = inmateCountMap.containsKey(Integer.parseInt(prefetchedData.get("q7_id").toString()))
					? Integer.parseInt(
							inmateCountMap.get(Integer.parseInt(prefetchedData.get("q7_id").toString())).toString())
					: 0;

			double sqft = avgSqftDataCCI.containsKey(Integer.parseInt(prefetchedData.get("q7_id").toString()))
					? Double.parseDouble(
							avgSqftDataCCI.get(Integer.parseInt(prefetchedData.get("q7_id").toString())).toString())
					: 0;

			childData.put("Average Sq. ft. area per child", sqft > 0 && count > 0 ? df.format(sqft / count) : "N/A");

			if (Integer.parseInt(prefetchedData.get("q51_id").toString()) == 46) {
				blacklistedCCI++;
			}

			if (Integer.parseInt(prefetchedData.get("q52_id").toString()) == 46) {
				policeReport++;
			}
			if (institutionData.getQ25() != null)
				sanctionedStrength += Integer.parseInt(institutionData.getQ25());

			childData.put("id", institutionData.getInstutionId());
			childData.put("cciid", prefetchedData.get("q7_id").toString());
			childData.put("districtId", prefetchedData.get("q11_id").toString());
			childData.put("lat",
					institutionData.getGeolocation() == null || institutionData.getGeolocation().trim().equals("") ? ""
							: institutionData.getGeolocation().split(",")[0]);
			childData.put("lng",
					institutionData.getGeolocation() == null || institutionData.getGeolocation().trim().equals("") ? ""
							: institutionData.getGeolocation().split(",")[1]);
			childData.put("draggable", false);

			childData.put("Action", getAction(true));

			if (inmateCountDistrictWiseMap.containsKey(Integer.parseInt(prefetchedData.get("q11_id").toString())))
				inmateCountDistrictWiseMap.put(Integer.parseInt(prefetchedData.get("q11_id").toString()),
						inmateCountDistrictWiseMap.get(Integer.parseInt(prefetchedData.get("q11_id").toString()))
								+ count);
			else
				inmateCountDistrictWiseMap.put(Integer.parseInt(prefetchedData.get("q11_id").toString()), count);

			if (cciTypeCountMap.containsKey(Integer.parseInt(prefetchedData.get("q5_id").toString())))
				cciTypeCountMap.put(Integer.parseInt(prefetchedData.get("q5_id").toString()),
						cciTypeCountMap.get(Integer.parseInt(prefetchedData.get("q5_id").toString())) + 1);
			else
				cciTypeCountMap.put(Integer.parseInt(prefetchedData.get("q5_id").toString()), 1);

			if (cciDistributionList.containsKey(prefetchedData.get("q1").toString()))
				cciDistributionList.put(prefetchedData.get("q1").toString(),
						cciDistributionList.get((prefetchedData.get("q1").toString())) + 1);
			else
				cciDistributionList.put(prefetchedData.get("q1").toString(), 1);

			if (institutionData.getQ19().before(new Date()) && !DateUtils.isSameDay(institutionData.getQ19(), new Date())) {
				if (cciTypeCountMapExpiredLicense.containsKey(prefetchedData.get("q1").toString()))
					cciTypeCountMapExpiredLicense.put(prefetchedData.get("q1").toString(),
							cciTypeCountMapExpiredLicense.get((prefetchedData.get("q1").toString())) + 1);
				else
					cciTypeCountMapExpiredLicense.put(prefetchedData.get("q1").toString(), 1);

				if (cciTypeCountMapExpiredLicense.containsKey("total"))
					cciTypeCountMapExpiredLicense.put("total", cciTypeCountMapExpiredLicense.get("total") + 1);
				else
					cciTypeCountMapExpiredLicense.put("total", 1);

			}

			if (institutionData.getQ19().after(new Date()) && institutionData.getQ19().before(after6MonthDate)) {
				if (cciTypeCountMapExpiringLicense.containsKey(prefetchedData.get("q1").toString()))
					cciTypeCountMapExpiringLicense.put(prefetchedData.get("q1").toString(),
							cciTypeCountMapExpiringLicense.get((prefetchedData.get("q1").toString())) + 1);
				else
					cciTypeCountMapExpiringLicense.put(prefetchedData.get("q1").toString(), 1);

				if (cciTypeCountMapExpiringLicense.containsKey("total"))
					cciTypeCountMapExpiringLicense.put("total", cciTypeCountMapExpiringLicense.get("total") + 1);
				else
					cciTypeCountMapExpiringLicense.put("total", 1);

			}

			if (cciTypeGender.containsKey(prefetchedData.get("q6").toString()))
				cciTypeGender.put(prefetchedData.get("q6").toString(),
						cciTypeGender.get((prefetchedData.get("q6").toString())) + 1);
			else
				cciTypeGender.put(prefetchedData.get("q6").toString(), 1);

			dataArray.add(childData);
		}

		List<QuickStats> cciQuickStats = new ArrayList<QuickStats>();

//		quickStarts.add(getQuickStats("No of CCI", String.valueOf(institutionDatas.size()), Constants.CCI_ICON));

		//Current Strength vs Maximum Strength
		cciQuickStats
				.add(getQuickStats("Current Strength Vs Maximum Strength", inmatesDatas.get(0)[0].toString()+"/"+sanctionedStrength, Constants.TOTAL_STRENGTH));

//		cciQuickStats.add(getQuickStats("Current Strength", inmatesDatas.get(0)[0].toString(), Constants.TOTAL_STRENGTH));

		
		cciQuickStats.add(getQuickStats("CCIs Exceeding Maximum Strength", String.valueOf(exceedingMaximumStringth), Constants.GROUP_ICON));
		
		
		cciQuickStats.add(
				getQuickStats("Blacklisted CCIs", String.valueOf(blacklistedCCI), Constants.BLACKLISTED_CCI));

		cciQuickStats.add(getQuickStats("CCIs with reported Police Cases",
				String.valueOf(policeReport), Constants.POLICE_CASE_REPORT));

		List<QuickStats> inmatesQuickStats = new ArrayList<QuickStats>();

//		inmatesQuickStats.add(getQuickStats("No of Boys",
//				inmatesDatas.get(0)[1] == null ? "0" : String.valueOf(inmatesDatas.get(0)[1].toString()),
//				Constants.BOY_ICON));
//		inmatesQuickStats.add(getQuickStats("No of Girls",
//				inmatesDatas.get(0)[2] == null ? "0" : String.valueOf(inmatesDatas.get(0)[2].toString()),
//				Constants.GIRL_ICON));

		inmatesQuickStats.add(getQuickStats("Children Eligible for Adoption",
				inmatesDatas.get(0)[1] == null ? "0" : inmatesDatas.get(0)[3].toString(),
				Constants.ELIGIBLE_FOR_ADOPTION));
		inmatesQuickStats.add(getQuickStats("Children Eligible for Foster Caring",
				inmatesDatas.get(0)[1] == null ? "0" : inmatesDatas.get(0)[4].toString(), Constants.FOSTER_CARE));
		inmatesQuickStats.add(getQuickStats("Children having Formal Education",
				inmatesDatas.get(0)[1] == null ? "0" : inmatesDatas.get(0)[6].toString(),
				Constants.WITH_FORMAL_EDUCATION));
		inmatesQuickStats.add(getQuickStats("Run Away Children",
				inmatesDatas.get(0)[1] == null ? "0" : inmatesDatas.get(0)[5].toString(), Constants.RUN_AWAY));

		Map<String, DashboardLandingPage> dashboardLandingPages = new LinkedHashMap<String, DashboardLandingPage>();

		DashboardLandingPage institutionDashboarLandingPage = new DashboardLandingPage();

		DashboardLandingPage cciDashboarLandingPage = new DashboardLandingPage();

		institutionDashboarLandingPage.setQuickStarts(cciQuickStats);
		cciDashboarLandingPage.setQuickStarts(inmatesQuickStats);

		List<GroupedIndicator> groupedInstitutionIndicatorsForInstitution = new ArrayList<GroupedIndicator>();

		chartDataList = new ArrayList<ChartData>();
		availableChart = new HashSet<String>();
		legend=new LinkedHashMap<String,String>();
		legendNo=0;
		for (String key : cciDistributionList.keySet()) {

			ChartData chartData = new ChartData();
			chartData.setAxis(key);
			chartData.setIndicatorName(key);
			chartData.setKey(key);
			chartData.setValue(String.valueOf((cciDistributionList.get(key) )));
			chartData.setCssColor(colorCodes.get(legendNo));
			legend.put(key, colorCodes.get(legendNo));
			legendNo++;
			chartDataList.add(chartData);

		}

		availableChart.add("pie");
		groupedIndicatorData = new GroupedIndicator();
		groupedIndicatorData.setChartData(Arrays.asList(chartDataList));
		groupedIndicatorData.setChartsAvailable(availableChart);
		groupedIndicatorData.setLegendClass(legend);
		groupedIndicatorData.setGroupedIndName("Percentage of CCI distribution by sector");
		groupedIndicatorData.setIndicatorName("Percentage of CCI distribution by sector");
		groupedIndicatorData.setCssClass("col-md-6 col-sm-6 col-xs-12 col-lg-3");

		groupedInstitutionIndicatorsForInstitution.add(groupedIndicatorData);

		// cciTypeCountMapExpiredLicense

		chartDataList = new ArrayList<ChartData>();
		availableChart = new HashSet<String>();
		legend=new LinkedHashMap<String,String>();
		legendNo=0;
		for (String key : cciTypeCountMapExpiredLicense.keySet()) {
			if (key.equals("total")) {
				continue;
			}
			ChartData chartData = new ChartData();
			chartData.setAxis(key);
			chartData.setIndicatorName(key);
			chartData.setKey(key);
			chartData.setValue(String.valueOf(
					cciTypeCountMapExpiredLicense.get(key)));
			
			chartData.setCssColor(colorCodes.get(legendNo));
			legend.put(key, colorCodes.get(legendNo));
			legendNo++;
			
			chartDataList.add(chartData);

		}

		availableChart.add("pie");
		groupedIndicatorData = new GroupedIndicator();
		groupedIndicatorData.setChartData(Arrays.asList(chartDataList));
		groupedIndicatorData.setChartsAvailable(availableChart);
		groupedIndicatorData.setLegendClass(legend);
		groupedIndicatorData.setGroupedIndName("Percentage of CCIs with expired JJ License");
		groupedIndicatorData.setIndicatorName("Percentage of CCIs with expired JJ License");
		groupedIndicatorData.setCssClass("col-md-6 col-sm-6 col-xs-12 col-lg-3");

		groupedInstitutionIndicatorsForInstitution.add(groupedIndicatorData);

		chartDataList = new ArrayList<ChartData>();
		availableChart = new HashSet<String>();
		legend=new LinkedHashMap<String,String>();
		legendNo=0;
		for (String key : cciTypeCountMapExpiringLicense.keySet()) {
			if (key.equals("total")) {
				continue;
			}
			ChartData chartData = new ChartData();
			chartData.setAxis(key);
			chartData.setIndicatorName(key);
			chartData.setKey(key);
			chartData.setValue(String.valueOf(
					(cciTypeCountMapExpiringLicense.get(key))));
			chartData.setCssColor(colorCodes.get(legendNo));
			legend.put(key, colorCodes.get(legendNo));
			legendNo++;
			
			chartDataList.add(chartData);

		}

		availableChart.add("pie");
		groupedIndicatorData = new GroupedIndicator();
		groupedIndicatorData.setChartData(Arrays.asList(chartDataList));
		groupedIndicatorData.setChartsAvailable(availableChart);
		groupedIndicatorData.setLegendClass(legend);
		groupedIndicatorData.setGroupedIndName("Percentage of CCIs with JJ License expiring in 6 months ");
		groupedIndicatorData.setIndicatorName("Percentage of CCIs with JJ License expiring in 6 months ");
		groupedIndicatorData.setCssClass("col-md-6 col-sm-6 col-xs-12 col-lg-3");

		groupedInstitutionIndicatorsForInstitution.add(groupedIndicatorData);

		// cciTypeGender

		chartDataList = new ArrayList<ChartData>();
		availableChart = new HashSet<String>();
		legend=new LinkedHashMap<String,String>();
		legendNo=0;
		for (String key : cciTypeGender.keySet()) {

			ChartData chartData = new ChartData();
			chartData.setAxis(key);
			chartData.setIndicatorName(key);
			chartData.setKey(key);
			chartData.setValue(String.valueOf((cciTypeGender.get(key) ) ));
			chartData.setCssColor(colorCodes.get(legendNo));
			legend.put(key, colorCodes.get(legendNo));
			legendNo++;
			
			chartDataList.add(chartData);

		}

		availableChart.add("pie");
		groupedIndicatorData = new GroupedIndicator();
		groupedIndicatorData.setChartData(Arrays.asList(chartDataList));
		groupedIndicatorData.setChartsAvailable(availableChart);
		groupedIndicatorData.setLegendClass(legend);
		groupedIndicatorData.setGroupedIndName("Percentage of CCI distribution by gender");
		groupedIndicatorData.setIndicatorName("Percentage of CCI distribution by gender");
		groupedIndicatorData.setCssClass("col-md-6 col-sm-6 col-xs-12 col-lg-3");

		groupedInstitutionIndicatorsForInstitution.add(groupedIndicatorData);

		chartDataList = new ArrayList<ChartData>();
		availableChart = new HashSet<String>();
		for (Options option : options) {

			ChartData chartData = new ChartData();
			chartData.setAxis(option.getOptionName());
			chartData.setIndicatorName(option.getOptionName());
			chartData.setKey(option.getOptionName());
			if (cciTypeCountMap.containsKey(option.getOptionId()) && institutionDatas.size() > 0)
				chartData.setValue(cciTypeCountMap.containsKey(option.getOptionId())
						? df.format((cciTypeCountMap.get(option.getOptionId()) * 100.00) / institutionDatas.size())
						: null);

			chartDataList.add(chartData);

		}

		availableChart.add("bar");
		groupedIndicatorData = new GroupedIndicator();
		groupedIndicatorData.setChartData(Arrays.asList(chartDataList));
		groupedIndicatorData.setChartsAvailable(availableChart);
		groupedIndicatorData.setGroupedIndName("Percentage of CCI distribution by type");
		groupedIndicatorData.setIndicatorName("Percentage of CCI distribution by type");
		groupedIndicatorData.setCssClass("col-md-4 col-sm-12 col-xs-12 col-lg-4");

		groupedInstitutionIndicatorsForInstitution.add(groupedIndicatorData);

		if (districts.size() > 0) {
			chartDataList = new ArrayList<ChartData>();
			availableChart = new HashSet<String>();
			for (Area district : districts) {
				if (district.getAreaId() == 0) {
					continue;
				} else {
					ChartData chartData = new ChartData();
					chartData.setAxis(district.getAreaName());
					chartData.setIndicatorName(district.getAreaName());
					chartData.setKey(district.getAreaName());
					if (inmateCountDistrictWiseMap.containsKey(district.getAreaId())
							&& inmateCountDistrictWiseMap.get(district.getAreaId()) > 0)
						chartData
								.setValue(
										avgSqftData.containsKey(district.getAreaId())
												? df.format(
														Double.parseDouble(avgSqftData.get(district.getAreaId()))
																/ inmateCountDistrictWiseMap.get(district.getAreaId()))
												: null);

					chartDataList.add(chartData);

				}
			}
			;

			availableChart.add("bar");
			groupedIndicatorData = new GroupedIndicator();
			groupedIndicatorData.setChartData(Arrays.asList(chartDataList));
			groupedIndicatorData.setChartsAvailable(availableChart);
			groupedIndicatorData.setGroupedIndName("Average Sq. ft. area per child");
			groupedIndicatorData.setIndicatorName("Average Sq. ft. area per child");
			groupedIndicatorData.setCssClass("col-md-8 col-sm-12 col-xs-12 col-lg-8");

			groupedInstitutionIndicatorsForInstitution.add(groupedIndicatorData);
		}
		institutionDashboarLandingPage.setGroupedIndicators(groupedInstitutionIndicatorsForInstitution);
		
		List<GroupedIndicator> groupedInmatesIndicatorsForInstitution = new ArrayList<GroupedIndicator>();

		chartDataList = new ArrayList<ChartData>();
		availableChart = new HashSet<String>();
		legend=new LinkedHashMap<String,String>();
		legendNo=0;
		for (int i : Arrays.asList(1, 2, 7)) {

			if(inmatesDatas.get(0)[i]!=null&&Integer.parseInt(inmatesDatas.get(0)[i].toString())==0)
				continue;
			
			ChartData chartData = new ChartData();

			switch (i) {
			case 1:
				chartData.setAxis("Boys");
				break;
			case 2:
				chartData.setAxis("Girls");
				break;

			case 7:
				chartData.setAxis("Transgenders");
				break;
			}

			chartData.setIndicatorName(chartData.getAxis());
			chartData.setKey(chartData.getAxis());
			
			if(inmatesDatas.get(0)[i]!=null)
			chartData.setValue(inmatesDatas.get(0)[i].toString());

			chartData.setCssColor(colorCodes.get(legendNo));
			legend.put(chartData.getAxis(), colorCodes.get(legendNo));
			legendNo++;
			
			chartDataList.add(chartData);

		}
		
		availableChart.add("pie");
		groupedIndicatorData = new GroupedIndicator();
		groupedIndicatorData.setChartData(Arrays.asList(chartDataList));
		groupedIndicatorData.setChartsAvailable(availableChart);
		groupedIndicatorData.setLegendClass(legend);
		groupedIndicatorData.setGroupedIndName("Percentage of inmate distribution by gender");
		groupedIndicatorData.setIndicatorName("Percentage of inmate distribution by gender");
		groupedIndicatorData.setCssClass("col-md-6 col-sm-6 col-xs-12 col-lg-6");

		groupedInmatesIndicatorsForInstitution.add(groupedIndicatorData);
		
		chartDataList = new ArrayList<ChartData>();
		availableChart = new HashSet<String>();
		legend=new LinkedHashMap<String,String>();
		legendNo=0;
		for (int i : Arrays.asList(8, 9, 10)) {

			if(inmatesDatas.get(0)[i]!=null&&Integer.parseInt(inmatesDatas.get(0)[i].toString())==0)
				continue;
			
			ChartData chartData = new ChartData();

			switch (i) {
			case 8:
				chartData.setAxis("Mental");
				break;
			case 9:
				chartData.setAxis("Physical");
				break;

			case 10:
				chartData.setAxis("Both");
				break;
			}

			chartData.setIndicatorName(chartData.getAxis());
			chartData.setKey(chartData.getAxis());
			if(inmatesDatas.get(0)[i]!=null)
			chartData.setValue(inmatesDatas.get(0)[i].toString());

			chartData.setCssColor(colorCodes.get(legendNo));
			legend.put(chartData.getAxis(), colorCodes.get(legendNo));
			legendNo++;
			
			chartDataList.add(chartData);

		}
		
		availableChart.add("pie");
		groupedIndicatorData = new GroupedIndicator();
		groupedIndicatorData.setChartData(Arrays.asList(chartDataList));
		groupedIndicatorData.setChartsAvailable(availableChart);
		groupedIndicatorData.setLegendClass(legend);
		//name changed
		groupedIndicatorData.setGroupedIndName("Proportion of Children with Special Need (by type of illness)");
		groupedIndicatorData.setIndicatorName("Proportion of Children with Special Need (by type of illness)");
		groupedIndicatorData.setCssClass("col-md-6 col-sm-6 col-xs-12 col-lg-6");

		groupedInmatesIndicatorsForInstitution.add(groupedIndicatorData);

		
		cciDashboarLandingPage.setGroupedIndicators(groupedInmatesIndicatorsForInstitution);

		dashboardLandingPages.put("Institution", institutionDashboarLandingPage);

		dashboardLandingPages.put("Inmates", cciDashboarLandingPage);

		dashboardLandingPageData.setDashboardLandingPages(dashboardLandingPages);

		dashboardLandingPageData.setDistricts(districts);

		dashboardLandingPageData.setTableData(dataArray);

		return dashboardLandingPageData;
	}

	private List<Map<String, String>> getAction(boolean check) {
		List<Map<String, String>> actionDetails = new ArrayList<Map<String, String>>();
		Map<String, String> actionDetailsMap = new LinkedHashMap<String, String>();

		actionDetailsMap = new LinkedHashMap<String, String>();
		actionDetailsMap.put("controlType", "button");
		actionDetailsMap.put("value", "");
		actionDetailsMap.put("type", "submit");
		actionDetailsMap.put("class", "btn btn-submit approved-view");
		actionDetailsMap.put("icon", "fa-eye");
		actionDetailsMap.put("tooltip", "View Data");
		actionDetails.add(actionDetailsMap);

		if (check) {
			actionDetailsMap = new LinkedHashMap<String, String>();
			actionDetailsMap.put("controlType", "button");
			actionDetailsMap.put("value", "");
			actionDetailsMap.put("type", "submit");
			actionDetailsMap.put("class", "btn btn-submit approved-map");
			actionDetailsMap.put("icon", "fa fa-map-marker");
			actionDetailsMap.put("tooltip", "View on Google Map");
			actionDetails.add(actionDetailsMap);

		}

		return actionDetails;
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
						responseMap.put(field.getName(), ((Area) value).getAreaName());
						responseMap.put(field.getName() + "_id", ((Area) value).getAreaId());
					} else if (value instanceof Options) {
						responseMap.put(field.getName(), ((Options) value).getOptionName());
						responseMap.put(field.getName() + "_id", ((Options) value).getOptionId());
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

	QuickStats getQuickStats(String label, String value, String img) {
		QuickStats quickStats = new QuickStats();

		quickStats.setImg(img);
		quickStats.setLabel(label);
		quickStats.setValue(value);

		return quickStats;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sdrc.scps.service.DashboardService#getDashboardInmateData(org.
	 * springframework.security.oauth2.provider.OAuth2Authentication,
	 * java.lang.Integer)
	 */
	@Override
	public DashboardLandingPageData getDashboardInmateData(OAuth2Authentication auth, Integer cciId, String sqft) {
		DashboardLandingPageData dashboardLandingPageData = new DashboardLandingPageData();
		DashboardLandingPage dashboarLandingPage = new DashboardLandingPage();
		if (cciId == null || cciId == 0) {
			return dashboardLandingPageData;
		}

		GroupedIndicator groupedIndicatorData = new GroupedIndicator();

		Set<String> availableChart = new HashSet<String>();

		List<ChartData> chartDataList = new ArrayList<ChartData>();

		Map<String, String> legend = new LinkedHashMap<String, String>();
		int legendNo = 0;

		List<InmatesData> inmatesDatas = inmatesDataRepository
				.findByIsLiveTrueAndOwnerAreaAreaIdAndSubmissionStatus(cciId, SubmissionStatus.APPROVED);

		// getting quick stats details in object[]
		List<Object[]> quickStat = inmatesDataRepository.findDataByIsLiveTrueAndOwnerCCI(cciId,
				SubmissionStatus.APPROVED);

		List<QuickStats> quickStarts = new ArrayList<QuickStats>();
		InstitutionData institutionData = institutionDataRepository
				.findByIsLiveTrueAndSubmissionStatusAndQ7AreaId(SubmissionStatus.APPROVED, cciId);
		quickStarts.add(getQuickStats("Validity of JJ License", sdf.format(institutionData.getQ19()),
				Constants.VALID_TILL_ICON));

		quickStarts.add(getQuickStats("Maximum Strength", institutionData.getQ25(), Constants.GROUP_ICON));

		quickStarts.add(getQuickStats("Current Strength",
				quickStat.get(0)[1] == null ? "0" : String.valueOf(quickStat.get(0)[0].toString()),
				Constants.TOTAL_STRENGTH));

//		Options insituteInmateType = institutionData.getQ6();
//
//		if (insituteInmateType.getOptionId() == 15 || insituteInmateType.getOptionId() == 17)
//			quickStarts.add(getQuickStats("No of Boys",
//					quickStat.get(0)[1] == null ? "0" : String.valueOf(quickStat.get(0)[1].toString()),
//					Constants.BOY_ICON));
//
//		if (insituteInmateType.getOptionId() == 16 || insituteInmateType.getOptionId() == 17)
//			quickStarts.add(getQuickStats("No of Girls",
//					quickStat.get(0)[2] == null ? "0" : String.valueOf(quickStat.get(0)[2].toString()),
//					Constants.GIRL_ICON));

		quickStarts.add(getQuickStats("Average Sq. ft. area per child", sqft, Constants.SQUARE_FIT));

		quickStarts.add(getQuickStats("Eligible for Adoption",
				quickStat.get(0)[3] == null ? "0" : String.valueOf(quickStat.get(0)[3].toString()),
				Constants.ELIGIBLE_FOR_ADOPTION));
		quickStarts.add(getQuickStats("Eligible for Foster Care",
				quickStat.get(0)[4] == null ? "0" : String.valueOf(quickStat.get(0)[4].toString()),
				Constants.FOSTER_CARE));
		quickStarts.add(getQuickStats("Children having Formal Education",
				quickStat.get(0)[8] == null ? "0" : String.valueOf(quickStat.get(0)[8].toString()),
				Constants.WITH_FORMAL_EDUCATION));
		quickStarts.add(getQuickStats("Run Away Children",
				quickStat.get(0)[5] == null ? "0" : String.valueOf(quickStat.get(0)[5].toString()),
				Constants.RUN_AWAY));
		dashboarLandingPage.setQuickStarts(quickStarts);

		Map<String, Object> prefetchedData = new LinkedHashMap<String, Object>();

		List<Object[]> headers = questionRepository.findReviewHeaderByFormId(Constants.CHILD_FORM);

		//sorting header aaccording to level 
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

		List<String> tableHeaders = new ArrayList<String>();

		for (String tableHeader : formColumnName.get(Constants.CHILD_FORM).values()) {
			tableHeaders.add(tableHeader.split("@")[0].split("_")[1]);
		}

		tableHeaders.add("Action");
		dashboardLandingPageData.setTableColumn(tableHeaders);

		List<Map<String, Object>> dataArray = new ArrayList<Map<String, Object>>();
		for (InmatesData inmatesData : inmatesDatas) {
			Map<String, Object> childData = new HashMap<String, Object>();
			prefetchedData = inspectForResponse(InmatesData.class, inmatesData);

			for (String keys : formColumnName.get(Constants.CHILD_FORM).keySet()) {

				childData.put(formColumnName.get(Constants.CHILD_FORM).get(keys).split("@")[0].split("_")[1],
						prefetchedData.get(keys) == null ? "N/A" : prefetchedData.get(keys).toString());
			}

			childData.put("id", inmatesData.getInmatesData());

			childData.put("Action", getAction(false));

			dataArray.add(childData);
		}
		dashboardLandingPageData.setTableData(dataArray);

		List<GroupedIndicator> groupedInmatesIndicatorsForInstitution = new ArrayList<GroupedIndicator>();

		chartDataList = new ArrayList<ChartData>();
		availableChart = new HashSet<String>();
		legend = new LinkedHashMap<String, String>();
		legendNo = 0;
		for (int i : Arrays.asList(1, 2, 7)) {

			if (quickStat.get(0)[i] != null && Integer.parseInt(quickStat.get(0)[i].toString()) == 0)
				continue;

			ChartData chartData = new ChartData();

			switch (i) {
			case 1:
				chartData.setAxis("Boys");
				break;
			case 2:
				chartData.setAxis("Girls");
				break;

			case 7:
				chartData.setAxis("Transgenders");
				break;
			}

			chartData.setIndicatorName(chartData.getAxis());
			chartData.setKey(chartData.getAxis());

			if (quickStat.get(0)[i] != null)
				chartData.setValue(quickStat.get(0)[i].toString());

			chartData.setCssColor(colorCodes.get(legendNo));
			legend.put(chartData.getAxis(), colorCodes.get(legendNo));
			legendNo++;

			chartDataList.add(chartData);

		}

		availableChart.add("pie");
		groupedIndicatorData = new GroupedIndicator();
		groupedIndicatorData.setChartData(Arrays.asList(chartDataList));
		groupedIndicatorData.setChartsAvailable(availableChart);
		groupedIndicatorData.setLegendClass(legend);
		groupedIndicatorData.setGroupedIndName("Percentage of inmate distribution by gender");
		groupedIndicatorData.setIndicatorName("Percentage of inmate distribution by gender");
		groupedIndicatorData.setCssClass("col-md-6 col-sm-6 col-xs-12 col-lg-6");

		groupedInmatesIndicatorsForInstitution.add(groupedIndicatorData);

		chartDataList = new ArrayList<ChartData>();
		availableChart = new HashSet<String>();
		legend = new LinkedHashMap<String, String>();
		legendNo = 0;
		for (int i : Arrays.asList(9, 10, 11)) {

			if (quickStat.get(0)[i] != null && Integer.parseInt(quickStat.get(0)[i].toString()) == 0)
				continue;

			ChartData chartData = new ChartData();

			switch (i) {
			case 9:
				chartData.setAxis("Mental");
				break;
			case 10:
				chartData.setAxis("Physical");
				break;

			case 11:
				chartData.setAxis("Both");
				break;
			}

			chartData.setIndicatorName(chartData.getAxis());
			chartData.setKey(chartData.getAxis());
			if (quickStat.get(0)[i] != null)
				chartData.setValue(quickStat.get(0)[i].toString());

			chartData.setCssColor(colorCodes.get(legendNo));
			legend.put(chartData.getAxis(), colorCodes.get(legendNo));
			legendNo++;

			chartDataList.add(chartData);

		}

		availableChart.add("pie");
		groupedIndicatorData = new GroupedIndicator();
		groupedIndicatorData.setChartData(Arrays.asList(chartDataList));
		groupedIndicatorData.setChartsAvailable(availableChart);
		groupedIndicatorData.setLegendClass(legend);
		groupedIndicatorData.setGroupedIndName("Percentage of children with Special Need ");
		groupedIndicatorData.setIndicatorName("Percentage of children with Special Need ");
		groupedIndicatorData.setCssClass("col-md-6 col-sm-6 col-xs-12 col-lg-6");

		groupedInmatesIndicatorsForInstitution.add(groupedIndicatorData);

		dashboarLandingPage.setGroupedIndicators(groupedInmatesIndicatorsForInstitution);

		Map<String, DashboardLandingPage> dashboardLandingPageMap = new HashMap<String, DashboardLandingPage>();
		dashboardLandingPageMap.put("inmates", dashboarLandingPage);
		dashboardLandingPageData.setDashboardLandingPages(dashboardLandingPageMap);
		return dashboardLandingPageData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sdrc.scps.service.DashboardService#generatePDF(java.util.List,
	 * org.springframework.security.oauth2.provider.OAuth2Authentication,
	 * javax.servlet.http.HttpServletResponse,
	 * javax.servlet.http.HttpServletRequest)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String generatePDF(List<DataEntryQuestionModel> dataEntryModels, OAuth2Authentication auth,
			HttpServletResponse response, HttpServletRequest request) throws Exception {

		String uri = request.getRequestURI();
		String url = request.getRequestURL().toString();
		Font sectionBold = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
		Font smallBold = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
		Font smallBoldWhite = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);
		Font dataFont = new Font(Font.FontFamily.HELVETICA, 10);
		BaseColor cellColor = WebColors.getRGBColor("#E8E3E2");
		BaseColor headerColor = WebColors.getRGBColor("#333a3b");
		final ObjectMapper mapper = new ObjectMapper();
//		dataEntryQuestionModels = new ArrayList<DataEntryQuestionModel>(dataEntryQuestionModelMap.values());

//		String ctxPath = request.getContextPath();

		url = url.replaceFirst(uri, "");

		//filtering question having column id as it contains id of submission
		Image formDataMainImage = null;
		QuestionModel idModel = dataEntryModels.get(0).getQuestions().stream()
				.filter(d -> d.getControlType().equals("id")).findFirst().get();
		int k = 0;
		InstitutionData idData = null;
		// if child form
		if (idModel.getKey() == Constants.CHILD_FORM) {

			QuestionModel idModelForInstitution = dataEntryModels.get(0).getQuestions().stream()
					.filter(d -> d.getColumnName().equals("id")).findFirst().get();

			// getting institution details related to that inmate
			if (idModelForInstitution.getValue() != null) {
				InmatesData data = inmatesDataRepository
						.findByInmatesData(Integer.parseInt(idModelForInstitution.getValue().toString()));
				idData = institutionDataRepository.findByIsLiveTrueAndSubmissionStatusAndQ7AreaId(
						SubmissionStatus.APPROVED, data.getOwner().getArea().getAreaId());
			}
//			List<String> imagePaths=inmatesPhotoRepository.findLatestPhotoByInmateId(dataEntryModels.get(0).getQuestions().get(1).getValue().toString());
			//getting questions with column names inmates photo which is begin repear
			List<List<QuestionModel>> questionModelsImage = dataEntryModels.get(0).getQuestions().stream()
					.filter(d -> d.getColumnName().equals("inmatesPhoto")).findFirst().get().getChildQuestionModels();

			questionModelsImage.sort(new Comparator<List<QuestionModel>>() {

				@Override
				public int compare(List<QuestionModel> o1, List<QuestionModel> o2) {

					if (Integer.parseInt(o1.stream().filter(d -> d.getColumnName().equalsIgnoreCase("photoq3"))
							.findFirst().get().getValue().toString()) > Integer
									.parseInt(o2.stream().filter(d -> d.getColumnName().equalsIgnoreCase("photoq3"))
											.findFirst().get().getValue().toString())) {
						return -1;
					} else if (Integer.parseInt(o1.stream().filter(d -> d.getColumnName().equalsIgnoreCase("photoq3"))
							.findFirst().get().getValue().toString()) < Integer
									.parseInt(o2.stream().filter(d -> d.getColumnName().equalsIgnoreCase("photoq3"))
											.findFirst().get().getValue().toString())) {
						return 1;
					}

					else {
						return 0;
					}
				}
			});
			QuestionModel imageModel = null;
			if (questionModelsImage.get(0).stream()
					.filter(d -> d.getType().equalsIgnoreCase("image") && d.getValue() != null).count() > 0) {
				imageModel = questionModelsImage.get(0).stream()
						.filter(d -> d.getType().equalsIgnoreCase("image") && d.getValue() != null).findFirst().get();
			}

			// if we are having image of inmates then we will add latest image of inmate
			if (imageModel != null && imageModel.getValue() != null) {

				try {
					formDataMainImage = Image.getInstance(mapper
							.convertValue(((List<?>) imageModel.getValue()).get(0), Attachment.class).getFilePath());
				} catch (Exception e) {
					formDataMainImage = Image.getInstance(
							ResourceUtils.getFile("classpath:" + Constants.IMAGE_DEFAULT).getAbsolutePath());
				}
			}

			// else we will put a default image
			else
				formDataMainImage = Image
						.getInstance(ResourceUtils.getFile("classpath:" + Constants.IMAGE_DEFAULT).getAbsolutePath());
		}

		Document document = new Document(PageSize.A4);
		// to be added
		String outputPath = this.tempPath.resolve(Constants.FILE_NAME + sdfFull.format(new java.util.Date()) + ".pdf")
				.toAbsolutePath().toString();
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outputPath));

		// setting Header Footer.PLS Refer to org.sdrc.scps.util.HeaderFooter
		HeaderFooter headerFooter = new HeaderFooter(url);
		writer.setPageEvent(headerFooter);

		document.open();
		document.addAuthor(url);
		Paragraph blankSpace = new Paragraph();
		blankSpace.setAlignment(Element.ALIGN_CENTER);
		blankSpace.setSpacingAfter(10);
		Chunk blankSpaceChunk = new Chunk("          ");
		blankSpace.add(blankSpaceChunk);
		document.add(blankSpace);

		// if its a inmates details then we will add summary of institution
		if (idModel.getKey() == Constants.CHILD_FORM && idData != null) {
			PdfPTable pdfPTable = new PdfPTable(2);
			pdfPTable.setWidthPercentage(95);
			float[] imageTableWidth = new float[] { 40f, 60f };
			pdfPTable.setWidths(imageTableWidth);

			PdfPCell pdfPCell = new PdfPCell(new Paragraph("Name of CCI : " + idData.getQ7().getAreaName(), smallBold));
			pdfPCell.setFixedHeight(20);
			pdfPCell.setBorderColor(BaseColor.WHITE);
//			pdfPCell.addElement(new Paragraph("Name of CCI : Urusuline Orphanage" , smallBoldWhite));
			pdfPTable.addCell(pdfPCell);
			pdfPCell = new PdfPCell();
			formDataMainImage.scaleToFit(150, 250);
//			formDataMainImage.setIndentationLeft(document.getPageSize().getWidth()-220);
			pdfPCell.setRowspan(3);
//			pdfPCell.setVerticalAlignment(Element.ALIGN_RIGHT);
			formDataMainImage.setAlignment(Image.ALIGN_RIGHT);
			pdfPCell.addElement(formDataMainImage);
			pdfPCell.setBorderColor(BaseColor.WHITE);
			pdfPTable.addCell(pdfPCell);

			pdfPCell = new PdfPCell(new Paragraph("Registration No : " + idData.getQ17(), smallBold));
			pdfPCell.setFixedHeight(20);
			pdfPCell.setBorderColor(BaseColor.WHITE);
//			pdfPCell.addElement(new Paragraph("Registration No : Urusuline Orphanage" , smallBoldWhite));
			pdfPTable.addCell(pdfPCell);

			pdfPCell = new PdfPCell(new Paragraph("Address  : " + idData.getQ9(), smallBold));
			pdfPCell.setFixedHeight(20);
			pdfPCell.setBorderColor(BaseColor.WHITE);
//			pdfPCell.addElement(new Paragraph("Address  : Urusuline Orphanage" , smallBoldWhite));
			pdfPTable.addCell(pdfPCell);

			document.add(pdfPTable);
		}
		document.add(Chunk.NEWLINE);

		for (DataEntryQuestionModel dataEntryQuestionModel : dataEntryModels) {
			Paragraph p = new Paragraph(
					dataEntryQuestionModel.getSectionOrder() + " : " + dataEntryQuestionModel.getName(), sectionBold);

			p.setSpacingAfter(10);
			document.add(p);

			document.add(new LineSeparator());
//			document.add(Chunk.NEWLINE);
			for (QuestionModel questionModel : dataEntryQuestionModel.getQuestions()) {

				// if control type == id the do nothing
				if (questionModel.getControlType().equals("id"))
					continue;

				// if not multiselect and having some options then will put a direct value
				if (!questionModel.getControlType().equals("multiSelect") && questionModel.getOptions() != null
						&& questionModel.getOptions().size() > 0 && questionModel.getValue() != null
						&& !questionModel.getValue().toString().trim().equals("")) {
					questionModel.setValue(questionModel.getOptions().stream()
							.filter(d -> d.getKey() == Integer.parseInt(questionModel.getValue().toString()))
							.findFirst().get().getValue());
				}

				// if multiselect then will put a comma seperated
				if (questionModel.getControlType().equals("multiSelect") && questionModel.getValue() != null
						&& !questionModel.getValue().toString().trim().equals("")) {
					String valuesFromOptions = "";
					for (Integer id : (ArrayList<Integer>) questionModel.getValue()) {
						String option = questionModel.getOptions().stream().filter(d -> d.getKey() == id).findFirst()
								.get().getValue();
						if (valuesFromOptions == "") {
							valuesFromOptions = option;
						} else {
							valuesFromOptions += "," + option;
						}
					}
					questionModel.setValue(valuesFromOptions);
				}

				// for file name will put comma seperated file names
				if (questionModel.getControlType().equals("file") && questionModel.getValue() != null
						&& !questionModel.getValue().toString().trim().equals("")) {

					String files = "";
					for (Object attachmentObject : (List<?>) questionModel.getValue()) {
						Attachment attachment = mapper.convertValue(attachmentObject, Attachment.class);
						if (files == "") {
							files = attachment.getOriginalName();
						} else {
							files += "," + attachment.getOriginalName();
						}

					}
					questionModel.setValue(files);
				}

				// if type is beginrepeat we will put data in a begin repeat
				if (questionModel.getChildQuestionModels() != null
						&& questionModel.getControlType().equalsIgnoreCase("beginRepeat")) {

					if (questionModel.getLabel() != "") {
						Paragraph paragraph = new Paragraph(
								questionModel.getDependentCondition() != null ? "\t \t " + questionModel.getLabel()
										: questionModel.getLabel(),
								smallBold);
						paragraph.setSpacingAfter(10);
						document.add(paragraph);

					}
//				document.add(new LineSeparator());

					PdfPTable beginRepeatTable = new PdfPTable(questionModel.getChildQuestionModels().get(0).size());
					beginRepeatTable.setWidthPercentage(100f);
					beginRepeatTable.setHeaderRows(1);
					for (QuestionModel questionModel1 : questionModel.getChildQuestionModels().get(0)) {

						PdfPCell headerCell = new PdfPCell(new Paragraph(questionModel1.getLabel(), smallBoldWhite));
						headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
						headerCell.setBackgroundColor(headerColor);
						headerCell.setBorderColor(BaseColor.WHITE);
						beginRepeatTable.addCell(headerCell);
					}

					int i = 0;
					for (List<QuestionModel> questionModels : questionModel.getChildQuestionModels()) {
							// setting data of begin repeat in a table each row with one begin repeat details
						for (QuestionModel questionModel1 : questionModels) {

//							System.out.println(questionModel1);
							if (!questionModel1.getControlType().equals("multiSelect")
									&& questionModel1.getOptions() != null && questionModel1.getValue() != null && !questionModel1.getOptions().isEmpty()
									&& !questionModel1.getValue().toString().trim().equals("")) {
//								System.out.println(questionModel1.getOptions());
								questionModel1.setValue(questionModel1.getOptions().stream().filter(
										d -> d.getKey() == Integer.parseInt(questionModel1.getValue().toString()))
										.findFirst().get().getValue());
							}

							if (questionModel1.getControlType().equals("multiSelect")
									&& questionModel1.getValue() != null
									&& !questionModel1.getValue().toString().trim().equals("")) {
								String valuesFromOptions = "";
								for (Integer id : Arrays.asList(questionModel1.getValue().toString().split(","))
										.stream().mapToInt(Integer::parseInt).toArray()) {
									String option = questionModel1.getOptions().stream().filter(d -> d.getKey() == id)
											.findFirst().get().getValue();
									if (valuesFromOptions == "") {
										valuesFromOptions = option;
									} else {
										valuesFromOptions += "," + option;
									}
								}
								questionModel1.setValue(valuesFromOptions);
							}

							if (questionModel1.getControlType().equals("file") && questionModel1.getValue() != null
									&& !questionModel1.getValue().toString().trim().equals("")) {

								String files = "";
								for (Object attachmentObject : (List<?>) questionModel1.getValue()) {
									Attachment attachment = mapper.convertValue(attachmentObject, Attachment.class);
									if (files == "") {
										files = attachment.getOriginalName();
									} else {
										files += "," + attachment.getOriginalName();
									}

								}

								questionModel1.setValue(files);
							}

							PdfPCell responseCell = new PdfPCell(new Paragraph(
									questionModel1.getValue() == null ? "" : questionModel1.getValue().toString(),
									dataFont));
							responseCell.setBorderColor(BaseColor.WHITE);
							responseCell.setHorizontalAlignment(Element.ALIGN_CENTER);
							if (i % 2 == 0) {
								responseCell.setBackgroundColor(cellColor);

							} else {
								responseCell.setBackgroundColor(BaseColor.LIGHT_GRAY);

							}
							responseCell.setBorderColor(BaseColor.WHITE);
							beginRepeatTable.addCell(responseCell);

						}

						i++;
					}

					document.add(beginRepeatTable);

				} else if (!questionModel.getControlType().equals("heading")
						&& questionModel.getChildQuestionModels() == null) {

					if (questionModel.getLabel() != "") {

//					PdfPTable responseTable = new PdfPTable(idModel.getKey()==Constants.CHILD_FORM && k==0?3:2);
						PdfPTable responseTable = new PdfPTable(2);
						float[] responseTableWidths;

//					if(idModel.getKey()==Constants.CHILD_FORM && k==0)
//						responseTableWidths = new float[] { 40f, 60f,60f };
//					else
						responseTableWidths = new float[] { 40f, 60f };
						responseTable.setWidths(responseTableWidths);

						responseTable.setWidthPercentage(100f);
						PdfPCell questionCell = new PdfPCell(new Paragraph(
								questionModel.getLabel() == null ? "" : questionModel.getLabel(), dataFont));
						questionCell.setBorderColor(BaseColor.WHITE);
						questionCell.setHorizontalAlignment(Element.ALIGN_LEFT);
						if (questionModel.getDependentCondition() != null) {
							questionCell.setIndent(10);
						}
						questionCell.setPaddingTop(10f);
						questionCell.setPaddingBottom(2f);
						responseTable.addCell(questionCell);

						PdfPCell responseCell = new PdfPCell();
						Chunk responeChunk = new Chunk();
						responeChunk
								.append(questionModel.getValue() != null ? "\t" + questionModel.getValue().toString()
										: Constants.BLANK_VALUE);
						responeChunk.setFont(dataFont);
						Paragraph paragraph = new Paragraph();
						paragraph.add(responeChunk);
						paragraph.setSpacingAfter(5f);
//					responseCell.setPhrase(paragraph);
						responseCell.setBorderColor(BaseColor.WHITE);
						responseCell.addElement(paragraph);
						responseCell.addElement(new DottedLineSeparator());

						responseCell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
						if (questionModel.getDependentCondition() != null) {
							responseCell.setIndent(10);
						}

						responseCell.setPaddingTop(questionModel.getValue() != null ? 5f : 15f);
						responseCell.setPaddingBottom(2f);

						responseTable.addCell(responseCell);

						if (formDataMainImage != null && k == 0) {
//						responseCell = new PdfPCell();
//						formDataMainImage.scaleToFit(170, 400);
//						formDataMainImage.setIndentationLeft(document.getPageSize().getWidth()-220);
//						formDataMainImage.setAlignment(Image.ALIGN_RIGHT);
//						document.add(formDataMainImage);
//						responseCell.addElement(formDataMainImage);
//						responseCell.setBorderColor(BaseColor.WHITE);
//						responseCell.setRowspan(2);
//						responseTable.addCell(responseCell);
						}

						document.add(responseTable);

					}
				} else if (questionModel.getControlType().equals("heading")) {

					if (questionModel.getLabel() != "") {
						Paragraph paragraph = new Paragraph(
								questionModel.getDependentCondition() != null ? "\t \t " + questionModel.getLabel()
										: questionModel.getLabel(),
								smallBold);
						paragraph.setSpacingAfter(10);
						document.add(paragraph);
						document.add(new LineSeparator());

					}
				}
				k++;
			}
		}

//		document.setPageCount(1);
//		if(formDataMainImage!=null)
//		{
//			formDataMainImage.setAbsolutePosition(420, 600);
//			formDataMainImage.scaleToFit(170, 400);
//			formDataMainImage.setIndentationLeft(document.getPageSize().getWidth()-220);
//			formDataMainImage.
//			document.add(formDataMainImage);
//		}

		document.close();

		return outputPath;
	}

}
