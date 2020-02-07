package org.sdrc.scps;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.annotation.PostConstruct;

import org.sdrc.scps.util.Constants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ComponentScan(basePackages = {  "org.sdrc.usermgmt.core","org.sdrc.scps" })
@EntityScan(basePackages = "org.sdrc.scps.domain")
@EnableAutoConfiguration(exclude = { MongoAutoConfiguration.class, MongoRepositoriesAutoConfiguration.class,
		MongoDataAutoConfiguration.class })
@EnableJpaRepositories(basePackages= {"org.sdrc.scps.repository"})
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableTransactionManagement
public class KeralaApplication {

	private final Path instPath = Paths.get(Constants.INSTITUTION_DIRC);
	private final Path inmatesPath = Paths.get(Constants.INMATES_DIRC);
	private final Path rootPath = Paths.get(Constants.ROOT_DIRC);
	private final Path tempPath = Paths.get(Constants.TEMP_DIRC);
	
	public static void main(String[] args) {
		SpringApplication.run(KeralaApplication.class, args);
	}

	@PostConstruct
	public void init() {
		try {
			if(!Files.isDirectory(rootPath))
			{
			Files.createDirectory(rootPath);
			}
			
			if(!Files.isDirectory(instPath))
			{
			Files.createDirectory(instPath);
			}
			
			if(!Files.isDirectory(inmatesPath))
			{
			Files.createDirectories(inmatesPath);
			}
			
			if(!Files.isDirectory(tempPath))
			{
			Files.createDirectories(tempPath);
			}
			
		} catch (IOException e) {
		}
	}

}
