/**
 * 
 */
package org.sdrc.scps;

import org.sdrc.usermgmt.core.annotations.EnableUserManagementWithJWTJPASecurityConfiguration;
import org.sdrc.usermgmt.core.util.UgmtClientCredentials;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author Harsh Pratyush
 *
 */
@Component
@EnableUserManagementWithJWTJPASecurityConfiguration
public class Loader {

	@Bean
	public UgmtClientCredentials ugmtClientCredentials() {
		return new UgmtClientCredentials("scpsKerala", "scpsKerala@123#!");
	}

}
