/**
 * 
 */
package org.sdrc.scps.service;

import java.util.List;

import org.sdrc.scps.models.CreateUserModel;
import org.sdrc.scps.models.ResponseModel;
import org.sdrc.scps.models.UserCreationSelectionDataModel;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

/**
 * @author Harsh Pratyush
 *
 */
public interface UserService {

	/**
	 *  This method will re
	 * @return
	 */
	UserCreationSelectionDataModel getAllSelectionData();
	
	List<Object> getAllUsers();
	
//	Map<String,String> getAllActiveCCIs();
	
	
	ResponseModel createUser(CreateUserModel createUserModel,OAuth2Authentication auth);
	
	
	ResponseModel createCCI(CreateUserModel createUserModel,OAuth2Authentication auth);
	
//	
//	ResponseModel activeDeactiveUser(int userId,boolean activate);
//	
//	ResponseModel changePassword(int userId,String newPassword);
}
