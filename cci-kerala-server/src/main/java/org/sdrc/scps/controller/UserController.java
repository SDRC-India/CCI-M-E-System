/**
 * 
 */
package org.sdrc.scps.controller;

import java.util.List;

import org.sdrc.scps.models.CreateUserModel;
import org.sdrc.scps.models.ResponseModel;
import org.sdrc.scps.models.UserCreationSelectionDataModel;
import org.sdrc.scps.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */

@RestController
public class UserController {
	
	
	@Autowired
	UserService userService;
	
	@PreAuthorize("hasAuthority('USER_MGMT_ALL_API')")
	@GetMapping("getAllSelectionData")
	UserCreationSelectionDataModel getAllSelectionData()
	{
		return userService.getAllSelectionData();
	}
	
	
	@PreAuthorize("hasAuthority('USER_MGMT_ALL_API')")
	@GetMapping("getAllUsers")
	List<Object> getAllUsers()
	{
		return userService.getAllUsers();
	}
	
	
	@PreAuthorize("hasAuthority('USER_MGMT_ALL_API')")
	@PostMapping("createUserSubmit")
	ResponseModel createUser(@RequestBody CreateUserModel createUserModel,OAuth2Authentication auth)
	{
		return userService.createUser(createUserModel,auth);
	}
	
	@PreAuthorize("hasAuthority('USER_MGMT_ALL_API')")
	@PostMapping("createCCI")
	ResponseModel createCCI(@RequestBody CreateUserModel createUserModel,OAuth2Authentication auth)
	{
		return userService.createCCI(createUserModel,auth);
	}
}
