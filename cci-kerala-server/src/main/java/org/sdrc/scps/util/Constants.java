/**
 * 
 */
package org.sdrc.scps.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */
public class Constants {

	public static final String AREA_EXCEL_PATH = "templates/Area_List_r1.xlsx";
	public static final String CCI_TEMPLATE = "templates/CCI-Template.xlsx";
	public static final String MAIL_ID_TEMPLATE = "templates/DCPU_Mail ID.xlsx";
	public static final String QUESTION_TEMPLATE = "templates/FormTemplate.xlsx";
	public static final String GROUP_ICON = "assets/img/capacity.png";
	public static final String BOY_ICON = "assets/img/boys.png";
	public static final String CCI_ICON = "assets/img/institution.png";
	public static final String GIRL_ICON = "assets/img/girls.png";
	public static final String ELIGIBLE_FOR_ADOPTION = "assets/img/adoption.png";
	public static final String FOSTER_CARE = "assets/img/foster-care.png";
	public static final String WITH_SPECIAL_NEED = "assets/img/special_need.png";
	public static final String RUN_AWAY = "assets/img/runaway.png";
	public static final int AREA_LEVEL = 4;
	public static final int INSTUTION_FORM = 1;
	public static final int CHILD_FORM = 2;
	public static final int OPTION_YES = 46;
	public static final int DECEASED_SECTION = 16;
	public static final String DECEASED_QUESTON_NAME = "q64";
	public static final String VALID_TILL_ICON = "assets/img/valid_till.png";
	public static final String BLANK_VALUE = "";
	public static final String ROOT_DIRC="/ccimis";
	public static final String INSTITUTION_DIRC=ROOT_DIRC+"/institution";
	public static final String INMATES_DIRC=ROOT_DIRC+"/inmates";
	public static final String TEMP_DIRC=ROOT_DIRC+"/TEMP";
	public static final String FILE_NAME="SubmissionDetails";
	public static final String DATA_SHEET_PATH = ROOT_DIRC;
	public static final int DISTRICT_LEVEL = 3;
	public static final double IMAGE_QUALITY = 280.00;
	public static final double MAX_FILE = 1024.00;
	public static final String IMAGE_DEFAULT = "templates/default.png";
	public static final String POLICE_CASE_REPORT = "assets/img/police-cases.png";
	public static final String BLACKLISTED_CCI = "assets/img/blacklisted-CCIs.png";
	public static final String WITH_FORMAL_EDUCATION = "assets/img/formal-education.png";
	public static final int CCI_TYPE_ID = 3;
	public static final String SQUARE_FIT ="assets/img/square_foot.png";
	public static final String TOTAL_STRENGTH = "assets/img/current_strength.png";


	public static List<Map<String, String>> getAction(SubmissionStatus status, boolean isLive) {
		List<Map<String, String>> actionDetails = new ArrayList<Map<String, String>>();
		Map<String, String> actionDetailsMap = new LinkedHashMap<String, String>();
		switch (status) {

		case APPROVED:
			actionDetailsMap = new LinkedHashMap<String, String>();
			if (isLive) {
				actionDetailsMap.put("controlType", "button");
				actionDetailsMap.put("value", "");
				actionDetailsMap.put("type", "submit");
				actionDetailsMap.put("class", "btn btn-submit approved-edit");
				actionDetailsMap.put("tooltip", "Edit");
				actionDetailsMap.put("icon", "fa-edit");
				actionDetails.add(actionDetailsMap);
			}
			actionDetailsMap = new LinkedHashMap<String, String>();
			actionDetailsMap.put("controlType", "button");
			actionDetailsMap.put("value", "");
			actionDetailsMap.put("type", "submit");
			actionDetailsMap.put("class", "btn btn-submit approved-view");
			actionDetailsMap.put("tooltip", "View");
			actionDetailsMap.put("icon", "fa-eye");
			actionDetails.add(actionDetailsMap);

			break;

		case PENDING:
			actionDetailsMap = new LinkedHashMap<String, String>();
			actionDetailsMap.put("controlType", "button");
			actionDetailsMap.put("value", "");
			actionDetailsMap.put("type", "submit");
			actionDetailsMap.put("class", "btn btn-submit pending-view");
			actionDetailsMap.put("tooltip", "View");
			actionDetailsMap.put("icon", "fa-eye");
			actionDetails.add(actionDetailsMap);
			break;

		case REJECTED:
			actionDetailsMap = new LinkedHashMap<String, String>();
			if (isLive) {
				actionDetailsMap.put("controlType", "button");
				actionDetailsMap.put("value", "");
				actionDetailsMap.put("type", "submit");
				actionDetailsMap.put("class", "btn btn-submit rejected-edit");
				actionDetailsMap.put("tooltip", "Edit");
				actionDetailsMap.put("icon", "fa-edit");
				actionDetails.add(actionDetailsMap);

				actionDetailsMap = new LinkedHashMap<String, String>();
				actionDetailsMap.put("controlType", "button");
				actionDetailsMap.put("value", "");
				actionDetailsMap.put("type", "submit");
				actionDetailsMap.put("class", "btn btn-submit rejected-delete");
				actionDetailsMap.put("tooltip", "Delete");
				actionDetailsMap.put("icon", "fa-trash");
				actionDetails.add(actionDetailsMap);
			} else {
				actionDetailsMap.put("controlType", "button");
				actionDetailsMap.put("value", "");
				actionDetailsMap.put("type", "submit");
				actionDetailsMap.put("class", "btn btn-submit rejected-view");
				actionDetailsMap.put("tooltip", "View");
				actionDetailsMap.put("icon", "fa-eye");
				actionDetails.add(actionDetailsMap);
			}
			break;

		case DRAFT:
			if (isLive) {
				actionDetailsMap = new LinkedHashMap<String, String>();
				actionDetailsMap.put("controlType", "button");
				actionDetailsMap.put("value", "");
				actionDetailsMap.put("type", "submit");
				actionDetailsMap.put("class", "btn btn-submit draft-edit");
				actionDetailsMap.put("tooltip", "Edit");
				actionDetailsMap.put("icon", "fa-edit");
				actionDetails.add(actionDetailsMap);

				actionDetailsMap = new LinkedHashMap<String, String>();
				actionDetailsMap.put("controlType", "button");
				actionDetailsMap.put("type", "submit");
				actionDetailsMap.put("value", "");
				actionDetailsMap.put("class", "btn btn-submit draft-delete");
				actionDetailsMap.put("tooltip", "Delete");
				actionDetailsMap.put("icon", "fa-trash");
				actionDetails.add(actionDetailsMap);
			} else {
				actionDetailsMap.put("controlType", "button");
				actionDetailsMap.put("value", "");
				actionDetailsMap.put("type", "submit");
				actionDetailsMap.put("class", "btn btn-submit draft-view");
				actionDetailsMap.put("tooltip", "View");
				actionDetailsMap.put("icon", "fa-eye");
				actionDetails.add(actionDetailsMap);
			}
			break;

		}
		return actionDetails;
	}

}
