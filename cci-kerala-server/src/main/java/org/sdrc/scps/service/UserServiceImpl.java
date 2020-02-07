/**
 * 
 */
package org.sdrc.scps.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.RandomStringUtils;
import org.json.simple.JSONObject;
import org.sdrc.scps.domain.Area;
import org.sdrc.scps.domain.AreaLevel;
import org.sdrc.scps.domain.User;
import org.sdrc.scps.models.AreaModel;
import org.sdrc.scps.models.CreateUserModel;
import org.sdrc.scps.models.ResponseModel;
import org.sdrc.scps.models.UserCreationSelectionDataModel;
import org.sdrc.scps.repository.AreaRepository;
import org.sdrc.scps.repository.InstitutionDataRepository;
import org.sdrc.scps.repository.UserRepository;
import org.sdrc.scps.util.Constants;
import org.sdrc.scps.util.SubmissionStatus;
import org.sdrc.scps.util.TokenInfoExtractor;
import org.sdrc.usermgmt.domain.Account;
import org.sdrc.usermgmt.domain.AccountDesignationMapping;
import org.sdrc.usermgmt.domain.Designation;
import org.sdrc.usermgmt.model.AuthorityControlType;
import org.sdrc.usermgmt.repository.AccountDesignationMappingRepository;
import org.sdrc.usermgmt.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Harsh Pratyush
 *
 */
@Service
public class UserServiceImpl implements UserService {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sdrc.scps.service.UserService#getAllSelectionData()
	 */

//	@Autowired
//	private DesignationRepository  jpaDesignationRepository;

	@Autowired
	private AreaRepository areaRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AccountDesignationMappingRepository accountDesignationMappingRepository;

	@Autowired
	private InstitutionDataRepository institutionDataRepository;

	@Autowired
	public JavaMailSender emailSender;

	@Autowired
	private TokenInfoExtractor tokenInfoExtractor;

	@Override
	public UserCreationSelectionDataModel getAllSelectionData() {
		UserCreationSelectionDataModel userCreationSelectionDataModel = new UserCreationSelectionDataModel();
//		List<Designation> designations=jpaDesignationRepository.findAll();
//		designations=designations.stream().filter(d->d.getId()!=1).collect(Collectors.toList());
//		userCreationSelectionDataModel.setDesignations(designations);
		List<Area> areas = areaRepository
				.findByAreaLevelAreaLevelIdInAndIsLiveTrueOrderByAreaNameAsc(Arrays.asList(2, 3));

		List<AreaModel> stateModels = new ArrayList<AreaModel>();
		List<AreaModel> districtModels = new ArrayList<AreaModel>();

		for (Area area : areas) {
			AreaModel areaModel = new AreaModel();

			areaModel.setAreaCode(area.getAreaCode());
			areaModel.setAreaId(area.getAreaId());
			areaModel.setAreaLevel(area.getAreaLevel().getAreaLevelId());
			areaModel.setAreaName(area.getAreaName());
			areaModel.setParentAreaId(area.getParentArea().getAreaId());

			if (areaModel.getAreaLevel() == Constants.DISTRICT_LEVEL)
				districtModels.add(areaModel);
			else
				stateModels.add(areaModel);

		}
		userCreationSelectionDataModel.setDistrict(districtModels);
		userCreationSelectionDataModel.setState(stateModels);
		return userCreationSelectionDataModel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sdrc.scps.service.UserService#getAllUsers()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getAllUsers() {

		List<Object[]> userObject = userRepository.getAllDataForUserTable();

		List<Object[]> jjRegdNosDatas = institutionDataRepository.getAllInstitutionJJREgdNo(SubmissionStatus.APPROVED);

		Map<String, String> institutionData = new HashMap<String, String>();
		for (Object[] jjRegdNosData : jjRegdNosDatas) {
			institutionData.put(jjRegdNosData[1].toString(), jjRegdNosData[0].toString());
		}

		Map<String, Map<String, Object>> mainDataMap = new LinkedHashMap<String, Map<String, Object>>();

		for (Object[] user : userObject) {
			Map<String, Object> tableData = new LinkedHashMap<String, Object>();

			int areaLevel = Integer.parseInt(user[7].toString());

			if (Integer.parseInt(user[6].toString()) == 1)
				continue;

			tableData.put("User Name", user[0].toString());
			tableData.put("Assigned Area", user[2].toString());
			tableData.put("Status", Boolean.parseBoolean(user[3].toString()) ? "Active" : "Disabled");
			tableData.put("Action", getAction(Boolean.parseBoolean(user[3].toString())));

			if (areaLevel == Constants.AREA_LEVEL) {
				if (institutionData.containsKey(user[4].toString())) {
					tableData.put("JJ-Regd. No.", institutionData.get(user[4].toString()));
				} else {
					tableData.put("JJ-Regd. No.", " - ");
				}

				tableData.put("District", user[8].toString());
			}

			tableData.put("id", Integer.parseInt(user[5].toString()));

			if (!mainDataMap.containsKey(user[1].toString())) {

				Map<String, Object> userTableMap = new LinkedHashMap<String, Object>();
				List<String> tableHeader = new ArrayList<String>();
				tableHeader.add("User Name");

				if (areaLevel == Constants.AREA_LEVEL) {
					tableHeader.add("JJ-Regd. No.");
					tableHeader.add("District");
				}

				tableHeader.add("Assigned Area");
				tableHeader.add("Status");
				tableHeader.add("Action");

				userTableMap.put("tableHeader", tableHeader);
				List<Map<String, Object>> tableDataList = new ArrayList<Map<String, Object>>();
				tableDataList.add(tableData);

				userTableMap.put("tableData", tableDataList);

				mainDataMap.put(user[1].toString(), userTableMap);
			} else {

				((List<Map<String, Object>>) mainDataMap.get(user[1].toString()).get("tableData")).add(tableData);
			}

//			tableDataList.add(tableData);
		}

		List<Object> dataArray = new ArrayList<Object>();
		mainDataMap.forEach((key, value) -> {
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			map.put("name", key);
			map.put("data", value);
			dataArray.add(map);
		});

		return dataArray;
	}

	private List<Map<String, String>> getAction(boolean check) {
		List<Map<String, String>> actionDetails = new ArrayList<Map<String, String>>();
		Map<String, String> actionDetailsMap = new LinkedHashMap<String, String>();

		actionDetailsMap = new LinkedHashMap<String, String>();
		actionDetailsMap.put("controlType", "button");
		actionDetailsMap.put("value", "");
		actionDetailsMap.put("type", "submit");
		actionDetailsMap.put("class", "btn btn-submit reset-pass");
		actionDetailsMap.put("tooltip", "Change Password");
		actionDetailsMap.put("icon", "fa-key");
		actionDetails.add(actionDetailsMap);

		if (check) {
			actionDetailsMap = new LinkedHashMap<String, String>();
			actionDetailsMap.put("controlType", "button");
			actionDetailsMap.put("value", "");
			actionDetailsMap.put("type", "submit");
			actionDetailsMap.put("class", "btn btn-submit disable");
			actionDetailsMap.put("tooltip", "Disable User");
			actionDetailsMap.put("icon", "fa-lock");
			actionDetails.add(actionDetailsMap);

		}

		else {
			actionDetailsMap = new LinkedHashMap<String, String>();
			actionDetailsMap.put("controlType", "button");
			actionDetailsMap.put("value", "");
			actionDetailsMap.put("type", "submit");
			actionDetailsMap.put("class", "btn btn-submit enable");
			actionDetailsMap.put("tooltip", "Enable User");
			actionDetailsMap.put("icon", "fa-unlock-alt");
			actionDetails.add(actionDetailsMap);

		}

		return actionDetails;
	}

//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see org.sdrc.scps.service.UserService#getAllActiveCCIs()
//	 */
//	@Override
//	public Map<String, String> getAllActiveCCIs() {
//		// TODO Auto-generated method stub
//		return null;
//	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sdrc.scps.service.UserService#createUser(org.sdrc.scps.models.
	 * CreateUserModel)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@Transactional
	public ResponseModel createUser(CreateUserModel createUserModel, OAuth2Authentication auth) {

		try {
			if (accountRepository.findByUserName(createUserModel.getUserName().toLowerCase().trim()) == null) {
				Object dataFromSession = tokenInfoExtractor.getUserModelInfo(auth);
				JSONObject jsonObject = new JSONObject((Map) dataFromSession);
				Area area = areaRepository.findByAreaId(createUserModel.getAreaId());
				User user = new User();

				Account account = new Account();

				account.setUserName(createUserModel.getUserName().toLowerCase());

				if (createUserModel.getEmail() != null)
					account.setEmail(createUserModel.getEmail());

				account.setUserName(account.getUserName().toLowerCase());

				String passwordHash="@"+RandomStringUtils.randomNumeric(3);
				account.setPassword(bCryptPasswordEncoder.encode(account.getUserName() +passwordHash));

				account.setAuthorityControlType(AuthorityControlType.DESIGNATION);

				account = accountRepository.save(account);
				user.setAccount(account);
				user.setFirstName("ICPS " + area.getAreaName());
				user.setArea(area);

				user = userRepository.save(user);

				AccountDesignationMapping accountDesignationMapping = new AccountDesignationMapping();

				accountDesignationMapping.setAccount(account);
				accountDesignationMapping.setDesignation(new Designation(area.getAreaLevel().getAreaLevelId()));
				accountDesignationMapping.setEnable(true);

				accountDesignationMappingRepository.save(accountDesignationMapping);

				ResponseModel responseModel = new ResponseModel();

				String emailBody = "<html> <body> Hello " + jsonObject.get("user_name") + ","
						+ "<br> <br> Please find below the details of user created for: <br> "
						+ "<br>User Name : " + account.getUserName()
						+ "<br>Password : " + account.getUserName()+passwordHash
						+ "<br><br>" + "Regards,<br> CCI-Monitoring Team" + "<br>" + " </body> </html>";
				
				sendMail(auth, emailBody);
				responseModel.setStatusCode(HttpStatus.OK.value());
				responseModel.setMessage("User Created.User details has been sent to your email-id ");
				return responseModel;

			}

			else {
				ResponseModel responseModel = new ResponseModel();

				responseModel.setStatusCode(HttpStatus.BAD_REQUEST.value());
				responseModel.setMessage("Duplicate User Name");
				return responseModel;
			}
		} catch (Exception e) {

			ResponseModel responseModel = new ResponseModel();

			responseModel.setStatusCode(HttpStatus.BAD_REQUEST.value());
			responseModel.setMessage("Error while creating user");
			return responseModel;
		

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sdrc.scps.service.UserService#createCCI(org.sdrc.scps.models.
	 * CreateUserModel)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@Transactional
	public ResponseModel createCCI(CreateUserModel createUserModel, OAuth2Authentication auth) {
		ResponseModel responseModel = new ResponseModel();
		try {
			
			Area district = areaRepository.findByAreaId(createUserModel.getDistrictId());
			if (district != null && createUserModel.getCciName() != null
					&& !createUserModel.getCciName().trim().equals("")) {
				Object dataFromSession = tokenInfoExtractor.getUserModelInfo(auth);
				JSONObject jsonObject = new JSONObject((Map) dataFromSession);
				
				String maxCode = areaRepository.findMaxAreaCodeByParentAreaId(district.getAreaId());

				String newCode = maxCode == null ? district.getAreaCode() + "001"
						: ("IND" + String.format("%08d", (Integer.parseInt(maxCode.split("IND")[1]) + 1)));

				Area cci = new Area();

				cci.setAreaName(createUserModel.getCciName());
				cci.setParentArea(district);
				cci.setAreaLevel(new AreaLevel(4));
				cci.setLive(true);
				cci.setAreaCode(newCode);

				areaRepository.save(cci);

				User user = new User();

				Account account = new Account();

				account.setUserName(createUserModel.getUserName());

				if (createUserModel.getEmail() != null)
					account.setEmail(createUserModel.getEmail());

				account.setUserName(cci.getParentArea().getParentArea().getAreaName().substring(0, 3).toLowerCase()
						+ "_"
						+ cci.getAreaCode().split(cci.getParentArea().getParentArea().getAreaCode())[1].toLowerCase());
				
				String passwordHash="@"+RandomStringUtils.randomNumeric(3);;

				account.setPassword(bCryptPasswordEncoder.encode(account.getUserName() + passwordHash));

				account.setAuthorityControlType(AuthorityControlType.DESIGNATION);

				account = accountRepository.save(account);
				user.setAccount(account);
				user.setFirstName(cci.getAreaName());
				user.setArea(cci);

				user = userRepository.save(user);

				AccountDesignationMapping accountDesignationMapping = new AccountDesignationMapping();

				accountDesignationMapping.setAccount(account);
				accountDesignationMapping.setDesignation(new Designation(cci.getAreaLevel().getAreaLevelId()));
				accountDesignationMapping.setEnable(true);

				accountDesignationMappingRepository.save(accountDesignationMapping);

				String emailBody = "<html> <body> Hello " + jsonObject.get("user_name") + ","
						+ "<br> <br> Please find below the details of user created for: <br> "
						+ "<br>CCI-Name : " + cci.getAreaName() 
						+ "<br>User Name : " + account.getUserName()
						+ "<br>Password : " + account.getUserName()+passwordHash
						+ "<br><br>" + "Regards,<br> CCI-Monitoring Team" + "<br>" + " </body> </html>";
				
				
				sendMail(auth, emailBody);

				responseModel.setStatusCode(HttpStatus.OK.value());
				responseModel.setMessage("CCI Created.User details has been sent to your email-id ");
				return responseModel;

			} else {

			}
		} catch (Exception e) {
			responseModel.setStatusCode(HttpStatus.BAD_REQUEST.value());
			responseModel.setMessage("Error while creating cci");
		}
		return responseModel;
	}

	@SuppressWarnings("rawtypes")
	private boolean sendMail(OAuth2Authentication auth, String emailBody) throws MessagingException {

		Object dataFromSession = tokenInfoExtractor.tokenInfo(auth);
		JSONObject jsonObject = new JSONObject((Map) dataFromSession);
		if (jsonObject.get("emailId") != null) {
			String emailId = jsonObject.get("emailId").toString();

			MimeMessage message = emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);

			helper.setTo(emailId.split(","));
			helper.setSubject("CCIMIS : New User Created");
			helper.setText(emailBody, true);
			emailSender.send(message);
		}

		return true;

	}

//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see org.sdrc.scps.service.UserService#activeDeactiveUser(int, boolean)
//	 */
//	@Override
//	public ResponseModel activeDeactiveUser(int userId, boolean activate) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see org.sdrc.scps.service.UserService#changePassword(int, java.lang.String)
//	 */
//	@Override
//	public ResponseModel changePassword(int userId, String newPassword) {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
