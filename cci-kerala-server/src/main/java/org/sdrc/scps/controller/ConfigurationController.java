/**
 * 
 */
package org.sdrc.scps.controller;

import org.sdrc.scps.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */

@RestController
public class ConfigurationController {
	
	@Autowired
	private ConfigService configService;
	
	
	@PostMapping("bypass/configArea")
	private boolean configArea()
	{
		return configService.updateArea();
	}
	
	
	@PostMapping("bypass/configCCI")
	private boolean configCCI()
	{
		return configService.configCCI();
	}
	
	
	@PostMapping("bypass/configUsersDummy")
	private boolean configUsersDummy()
	{
		return configService.configUsersDummy();
	}

	
	@PostMapping("bypass/configureQuestionTemplate")
	private boolean configureQuestionTemplate()
	{
		return configService.configureQuestionTemplate();
	}

}
