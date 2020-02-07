/**
 * 
 */
package org.sdrc.scps.service;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sdrc.scps.domain.Area;
import org.sdrc.scps.domain.User;
import org.sdrc.scps.repository.UserRepository;
import org.sdrc.scps.util.Constants;
import org.sdrc.usermgmt.core.util.IUserManagementHandler;
import org.sdrc.usermgmt.domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */

@Service
public class SessionMapInitializerClass implements IUserManagementHandler {
	
	@Autowired
	private UserRepository userRepository;

	/* (non-Javadoc)
	 * @see org.sdrc.usermgmt.core.util.IUserManagementHandler#getAllAuthorities()
	 */
	@Override
	public List<?> getAllAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.sdrc.usermgmt.core.util.IUserManagementHandler#saveAccountDetails(java.util.Map, java.lang.Object)
//	 */
	@Override
	public boolean saveAccountDetails(Map<String, Object> arg0, Object arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.sdrc.usermgmt.core.util.IUserManagementHandler#sessionMap(java.lang.Object)
	 */
	@Override
	public Map<String, Object> sessionMap(Object account) {
		
		Map<String, Object> sessionMap = new HashMap<>();

		User user = userRepository.findByAccount(((Account) account));

		Area area = user.getArea();
		sessionMap.put("landing",((Account)account).getAccountDesignationMapping().get(0).getDesignation().getCode());
		if(user.getArea().getAreaLevel().getAreaLevelId()==Constants.AREA_LEVEL)
		sessionMap.put("cci", area);
		else
			sessionMap.put("area", area);

		sessionMap.put("user_name",user.getFirstName());	
		return sessionMap;

	}

	/* (non-Javadoc)
	 * @see org.sdrc.usermgmt.core.util.IUserManagementHandler#updateAccountDetails(java.util.Map, java.lang.Object, java.security.Principal)
	 */
	@Override
	public boolean updateAccountDetails(Map<String, Object> arg0, Object arg1, Principal arg2) {
		// TODO Auto-generated method stub
		return false;
	}

}
