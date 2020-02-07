/**
 * 
 */
package org.sdrc.scps.util;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;

/**
 * @author Harsh Pratyush(harsh@sdrc.co.in)
 *
 */

@Component
public class TokenInfoExtractor {

	
	@Autowired(required = false)
	private TokenStore tokenStore;
	
	public Map<String, Object> tokenInfo(OAuth2Authentication auth) {

		OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
		OAuth2AccessToken accessToken = tokenStore.readAccessToken(details.getTokenValue());
		return accessToken.getAdditionalInformation();

	}
	
	public Object getUserModelInfo(OAuth2Authentication auth) {
		
		Map<String, Object> tokenInfoMap = tokenInfo(auth);
		@SuppressWarnings("unchecked")
		Map<String, Object> sessionAreaMap = (Map<String, Object>) tokenInfoMap.get("sessionMap");
		return sessionAreaMap;
		
	}
	

}
