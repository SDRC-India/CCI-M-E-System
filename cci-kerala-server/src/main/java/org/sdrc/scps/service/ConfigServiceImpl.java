/**
 * 
 */
package org.sdrc.scps.service;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.sdrc.scps.domain.Area;
import org.sdrc.scps.domain.AreaLevel;
import org.sdrc.scps.domain.Forms;
import org.sdrc.scps.domain.OptionType;
import org.sdrc.scps.domain.Options;
import org.sdrc.scps.domain.Question;
import org.sdrc.scps.domain.QuestionOptionTypeMapping;
import org.sdrc.scps.domain.Sections;
import org.sdrc.scps.domain.User;
import org.sdrc.scps.repository.AreaRepository;
import org.sdrc.scps.repository.OptionRepositry;
import org.sdrc.scps.repository.OptionTypeRepository;
import org.sdrc.scps.repository.QuestionOptionTypeMappingRepository;
import org.sdrc.scps.repository.QuestionRepository;
import org.sdrc.scps.repository.SectionRepository;
import org.sdrc.scps.repository.UserRepository;
import org.sdrc.scps.util.Constants;
import org.sdrc.usermgmt.domain.Account;
import org.sdrc.usermgmt.domain.AccountDesignationMapping;
import org.sdrc.usermgmt.domain.Designation;
import org.sdrc.usermgmt.model.AuthorityControlType;
import org.sdrc.usermgmt.repository.AccountDesignationMappingRepository;
import org.sdrc.usermgmt.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */

@Service
public class ConfigServiceImpl implements ConfigService {

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
	private SectionRepository sectionRepository;

	@Autowired
	private QuestionOptionTypeMappingRepository questionOptionTypeMappingRepository;

	@Autowired
	private OptionRepositry optionRepositry;

	@Autowired
	private OptionTypeRepository optionTypeRepository;

	@Autowired
	private QuestionRepository questionRepository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sdrc.scps.service.ConfigService#updateArea()
	 */
	@Override
	@Transactional
	public boolean updateArea() {

		try {
			FileInputStream fileInputStream = new FileInputStream(
					ResourceUtils.getFile("classpath:" + Constants.AREA_EXCEL_PATH));

			List<Area> areaList = areaRepository.findAll();

			Map<String, Area> areaMap = areaList.stream().collect(Collectors.toMap(Area::getAreaCode, area -> area));

			XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);

			XSSFSheet sheet = workbook.getSheetAt(0);

			for (int i = 0; i <= sheet.getLastRowNum(); i++) {
				Cell areaCode = sheet.getRow(i).getCell(1);
				Cell areaName = sheet.getRow(i).getCell(2);
				Cell parentAreaCode = sheet.getRow(i).getCell(3);
				Cell areaLevel = sheet.getRow(i).getCell(4);

				Area area = new Area();

				area.setAreaCode(areaCode.getStringCellValue());
				area.setAreaName(areaName.getStringCellValue());
				if (parentAreaCode != null)
					area.setParentArea(areaMap.get(parentAreaCode.getStringCellValue()));

				if ((int) areaLevel.getNumericCellValue() == 3) {
					String code = area.getAreaCode().split(parentAreaCode.getStringCellValue())[1];
					System.out.println(code);
					if (code.contains("z") || code.contains("Z") || code.contains("_")) {
					} else {
						System.out.println(code);
						System.out.println(String.format("%02d", (Integer.parseInt(code))));
						area.setAreaCode(
								parentAreaCode.getStringCellValue() + String.format("%02d", (Integer.parseInt(code))));

					}
				}

				area.setAreaLevel(new AreaLevel((int) areaLevel.getNumericCellValue()));
				area.setLive(area.getAreaCode().contains("IND032") || area.getAreaCode().equals("IND") ? true : false);

				area = areaRepository.save(area);
				areaMap.put(area.getAreaCode(), area);
			}

			workbook.close();
			return true;

		}

		catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sdrc.scps.service.ConfigService#configureQuestionTemplate()
	 */
	@Override
	@Transactional
	public boolean configureQuestionTemplate() {

		List<Sections> sections = sectionRepository.findAll();
		List<Question> questions = questionRepository.findAll();

		Map<String, Sections> sectionMap = new HashMap<String, Sections>();
		sections.forEach(d -> {
			sectionMap.put(d.getSectionName() + "_" + d.getForm().getFormId(), d);
		});
		List<OptionType> optionTypes = optionTypeRepository.findAll();

		Map<String, OptionType> optionTypeMap = new HashMap<String, OptionType>();
		optionTypes.forEach(d -> {
			optionTypeMap.put(d.getOptionTypeName(), d);
		});
		Map<String, Question> questionMap = new HashMap<String, Question>();

		questions.forEach(d -> {
			questionMap.put(d.getQuestionNameId() + '_' + d.getForm().getFormId(), d);
		});

		try {
			FileInputStream fileInputStream = new FileInputStream(
					ResourceUtils.getFile("classpath:" + Constants.QUESTION_TEMPLATE));

			XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);

			XSSFSheet sheet = workbook.getSheetAt(0);

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Question question = new Question();
				question.setLive(true);
				if (questionMap.containsKey(sheet.getRow(i).getCell(0).getStringCellValue() + "_"
						+ (int) sheet.getRow(i).getCell(8).getNumericCellValue())) {
					continue;
				}
				for (int colId = 0; colId <= 24; colId++) {
					Cell col = sheet.getRow(i).getCell(colId);

					System.out.println(col);
					switch (colId + 1) {
					case 1:// questionNameId
						question.setQuestionNameId(col.getStringCellValue());
						break;
					case 2:// questionName
						question.setQuestionName(
								col.getCellType() == CellType.STRING || col.getCellType() == CellType.BLANK
										? col.getStringCellValue()
										: String.valueOf((int) col.getNumericCellValue()));
						break;
					case 3:// question order
						question.setQuestionOrder((int) col.getNumericCellValue());
						break;
					case 4:// question col
						question.setColumnn(col.getStringCellValue());
						break;
					case 5:// question control
						question.setControlType(col.getStringCellValue());
						break;
					case 6: // question input type
						question.setInputType(col.getStringCellValue());
						break;
					case 7:
						if (sectionMap.containsKey(col.getStringCellValue() + "_"
								+ (int) sheet.getRow(i).getCell(8).getNumericCellValue()))
							question.setSection(sectionMap.get(col.getStringCellValue() + "_"
									+ (int) sheet.getRow(i).getCell(8).getNumericCellValue()));
						else {
							Sections section = new Sections();
							section.setForm(new Forms((int) sheet.getRow(i).getCell(8).getNumericCellValue()));
							section.setSectionName(col.getStringCellValue());
							section.setSectionOrder(1);
							section.setLive(true);
							section = sectionRepository.save(section);
							sectionMap.put(section.getSectionName() + "_" + section.getForm().getFormId(), section);
							question.setSection(section);
						}

						break;
					case 8: // QuestionOption

						break;

					case 9:// from
						question.setForm(new Forms((int) col.getNumericCellValue()));
						break;
					case 10:// reviewHeader
						question.setReviewHeader(col.getBooleanCellValue());
						break;
					case 11: // reviewName
						if (col != null)
							question.setReviewName(col.getStringCellValue());
						break;
					case 12: // dependecy
						if (col != null)
							question.setDependecy(col.getBooleanCellValue());
						break;
					case 13: // dependentColumn
						if (col != null)
							question.setDependentColumn(col.getStringCellValue());
						break;
					case 14: // dependentCondition
						if (col != null) {
							String condition = "";
							int k = 0;
							for (String cell : col.getStringCellValue().split(",")) {
								if (cell.contains("#")) {
									Question qustn = questionRepository.findByColumnnAndFormFormId(
											question.getDependentColumn().split(",")[k],
											(int) sheet.getRow(i).getCell(8).getNumericCellValue());
									String typeCondition = cell.split("#")[0].replaceAll("#", "");
									if (condition == "") {
										condition = typeCondition + "#"
												+ qustn.getQuestionOptionTypeMapping().getOptionType().getOptions()
														.stream()
														.filter(d -> d.getOptionName().trim()
																.equalsIgnoreCase(cell.split("#")[1].trim()))
														.findFirst().get().getOptionId();
									} else {
										condition += "," + typeCondition + "#"
												+ qustn.getQuestionOptionTypeMapping().getOptionType().getOptions()
														.stream()
														.filter(d -> d.getOptionName().trim()
																.equalsIgnoreCase(cell.split("#")[1].trim()))
														.findFirst().get().getOptionId();
									}
								}

								else {
									// successfully
									if (condition == "") {
										condition = cell;
									} else
										condition += "," + cell;

								}
								k++;
							}

							question.setDependentCondition(condition);

						}
						break;
					case 15: // fileExtensions
						if (col != null)
							question.setFileExtensions(col.getStringCellValue());
						break;
					case 16: // constraints
						if (col != null)
							question.setConstraints(col.getStringCellValue());
						break;
					case 17: // saveMandatory
						question.setSaveMandatory(col.getBooleanCellValue());
						break;
					case 18: // finalizeMandatory
						question.setFinalizeMandatory(col.getBooleanCellValue());
						break;
					case 19: // approvalProcess
						question.setApprovalProcess(col.getBooleanCellValue());
						break;
					case 20: // groupId
						if (col != null)
							question.setGroupId(col.getStringCellValue());
						break;
					case 21: // isTriggable
						if (col != null)
							question.setTriggable(col.getBooleanCellValue());
						break;
					case 22: // features
						if (col != null)
							question.setFeatures(col.getStringCellValue());
						break;
					case 23: // defaultSetting
						if (col != null)
							question.setDefaultSetting(col.getStringCellValue());
						break;

					case 24: // serial no
						if (col != null) {
							question.setQuestionSerial(
									col.getCellType() == CellType.STRING || col.getCellType() == CellType.BLANK
											? col.getStringCellValue()
											: String.valueOf(col.getNumericCellValue()));
							question.setQuestionSerial(question.getQuestionSerial().replaceAll("\\.0", ""));
						}
						break;

					case 25: // Placeholder
						if (col != null)
							question.setPlaceHolder(col.getStringCellValue());
						break;
					}

				}
				Cell col = sheet.getRow(i).getCell(7);
				question.setCreatedBy("harsh");
				question = questionRepository.save(question);

				if (col != null) {
					QuestionOptionTypeMapping questionOptionTypeMapping = new QuestionOptionTypeMapping();
					questionOptionTypeMapping.setQuestion(question);
					if (optionTypeMap.containsKey(col.getStringCellValue().trim().split(":")[0].replaceAll(":", ""))) {
						questionOptionTypeMapping.setOptionType(
								optionTypeMap.get(col.getStringCellValue().trim().split(":")[0].replaceAll(":", "")));

					} else {
						String optionTypeDetails = col.getStringCellValue().trim();
						String optionTypeName = optionTypeDetails.split(":")[0].replaceAll(":", "");

						OptionType optionType = new OptionType();
						optionType.setLive(true);
						optionType.setOptionTypeName(optionTypeName);
						optionType = optionTypeRepository.save(optionType);

						String options = optionTypeDetails.split(":")[1].replaceAll(":", "");
						for (String option : options.split(",")) {

							int order = 1;
							if (!option.trim().isEmpty()) {
								Options optionObject = new Options();
								optionObject.setLive(true);
								optionObject.setOptionName(option.trim());
								optionObject.setOptionOrder(order++);
								optionObject.setOptionType(optionType);
								optionRepositry.save(optionObject);
							}
						}

						questionOptionTypeMapping.setOptionType(optionType);
						optionTypeMap.put(optionType.getOptionTypeName(), optionType);
					}

					questionOptionTypeMappingRepository.save(questionOptionTypeMapping);
					questionMap.put(question.getColumnn(), question);
				}
			}

			workbook.close();
			return true;

		}

		catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	@Override
	@Transactional
	public boolean configCCI() {

		try {
			FileInputStream fileInputStream = new FileInputStream(
					ResourceUtils.getFile("classpath:" + Constants.CCI_TEMPLATE));

			List<Area> areaList = areaRepository.findByParentAreaAreaCode("IND032");

			Map<String, Area> areaMap = areaList.stream().collect(Collectors.toMap(Area::getAreaName, area -> area));

			XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);

			XSSFSheet sheet = workbook.getSheetAt(0);

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
//				Cell areaCode= sheet.getRow(i).getCell(1);
				Cell areaName = sheet.getRow(i).getCell(1);
				Cell parentAreaCode = sheet.getRow(i).getCell(2);
				// Cell areaLevel = sheet.getRow(i).getCell(2);

				Area area = new Area();
				System.out.println(i);
				String maxCode = areaRepository
						.findMaxAreaCodeByParentAreaId(areaMap.get(parentAreaCode.getStringCellValue()).getAreaId());

				String newCode = maxCode == null
						? areaMap.get(parentAreaCode.getStringCellValue()).getAreaCode() + "001"
						: ("IND" + String.format("%08d", (Integer.parseInt(maxCode.split("IND")[1]) + 1)));

				System.out.println(newCode);
				area.setAreaCode(newCode);
				area.setAreaName(areaName.getStringCellValue());
				if (parentAreaCode != null)
					area.setParentArea(areaMap.get(parentAreaCode.getStringCellValue()));

				area.setAreaLevel(new AreaLevel(4));
				area.setLive(true);

				area = areaRepository.save(area);

				areaMap.put(area.getAreaCode(), area);
			}

			workbook.close();
			return true;

		}

		catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	@Override
	@Transactional
	public boolean configUsersDummy() {
		List<Area> areas = areaRepository.findByIsLiveTrue();

		try {
			FileInputStream fileInputStream = new FileInputStream(
					ResourceUtils.getFile("classpath:" + Constants.MAIL_ID_TEMPLATE));

			XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
			XSSFSheet sheet = workbook.getSheetAt(0);
			Map<String, String> mailIds = new LinkedHashMap<String, String>();

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Cell areaName = sheet.getRow(i).getCell(0);
				Cell mailId = sheet.getRow(i).getCell(1);

				mailIds.put(areaName.getStringCellValue().toLowerCase(), mailId.getStringCellValue());
			}

			workbook.close();
			for (Area area : areas) {
				User user = new User();
				
				if (area.getAreaLevel().getAreaLevelId() == 1)
					continue;
				
				
				Account account = new Account();

				if (area.getAreaLevel().getAreaLevelId() > 2) {
					account.setUserName(area.getAreaLevel().getAreaLevelId() == 3
							? area.getParentArea().getAreaName().substring(0, 3) + "_"
									+ area.getAreaCode().split(area.getParentArea().getAreaCode())[1]
							: area.getParentArea().getParentArea().getAreaName().substring(0, 3) + "_"
									+ area.getAreaCode().split(area.getParentArea().getParentArea().getAreaCode())[1]);
				} else
					account.setUserName(area.getAreaName());

				if (area.getAreaLevel().getAreaLevelId() == 2 || area.getAreaLevel().getAreaLevelId() == 3)
					account.setEmail(mailIds.get(area.getAreaName().toLowerCase()));

				account.setUserName(account.getUserName().toLowerCase());

				account.setPassword(bCryptPasswordEncoder.encode(account.getUserName() + "@123"));

				account.setAuthorityControlType(AuthorityControlType.DESIGNATION);

				account = accountRepository.save(account);
				user.setAccount(account);
				
				if(area.getAreaLevel().getAreaLevelId() == 2)
				user.setFirstName("ICPS "+area.getAreaName());
				
				else  if(area.getAreaLevel().getAreaLevelId() == 3)
					user.setFirstName("DCPU "+area.getAreaName());
				
				else
					user.setFirstName(area.getAreaName());
				
				user.setArea(area);

				user = userRepository.save(user);

				AccountDesignationMapping accountDesignationMapping = new AccountDesignationMapping();

				accountDesignationMapping.setAccount(account);
				accountDesignationMapping.setDesignation(new Designation(area.getAreaLevel().getAreaLevelId()));
				accountDesignationMapping.setEnable(true);

				accountDesignationMappingRepository.save(accountDesignationMapping);
				
				
				if(area.getAreaLevel().getAreaLevelId() == 2)
				{
					 account = new Account();

					account.setUserName("admin");

					account.setUserName(account.getUserName().toLowerCase());
					account.setEmail("icpscci2@gmail.com");

					account.setPassword(bCryptPasswordEncoder.encode(account.getUserName() + "@123"));

					account.setAuthorityControlType(AuthorityControlType.DESIGNATION);

					account = accountRepository.save(account);
					user = new User();
					user.setAccount(account);
					user.setFirstName("admin");
					user.setArea(area);

					user = userRepository.save(user);

					 accountDesignationMapping = new AccountDesignationMapping();

					accountDesignationMapping.setAccount(account);
					accountDesignationMapping.setDesignation(new Designation(1));
					accountDesignationMapping.setEnable(true);

					accountDesignationMappingRepository.save(accountDesignationMapping);
				}

			}
			return true;
		} catch (Exception e) {
			return false;
		}

	}

}
