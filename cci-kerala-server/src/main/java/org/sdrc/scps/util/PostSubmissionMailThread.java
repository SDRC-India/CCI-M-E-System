/**
 * 
 */
package org.sdrc.scps.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.sdrc.scps.models.PostSubmissionModel;
import org.sdrc.scps.service.DataEntryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * This thread will do all the job that needs to be done after submission of a
 * particular form
 * 
 * @author Harsh Pratyush(harsh@sdrc.co.in)
 * @since version 1.0.0.0
 */
@Component
@Scope("singleton")
public class PostSubmissionMailThread extends Thread {
	
	private PostSubmissionModel postSubmissionModel;
	
	@Autowired
	private DataEntryService dataEntryService;

	public PostSubmissionModel getPostSubmissionModel() {
		return postSubmissionModel;
	}

	public void setPostSubmissionModel(PostSubmissionModel postSubmissionModel) {
		this.postSubmissionModel = postSubmissionModel;
	}
	private static final Logger logger = LoggerFactory.getLogger(PostSubmissionMailThread.class);
	@Override
	public void run() {

		try {
			logger.info("Mail sending start for "+this.postSubmissionModel.getInmateDetail().getQ1()); 	
			ExecutorService emailExecutor = Executors.newSingleThreadExecutor();
		    emailExecutor.execute(new Runnable() {
		        @Override
		        public void run() {
		            try {
		            	dataEntryService.postSubmissionWork(getPostSubmissionModel());
		            } catch (Exception e) {
		                logger.error("failed", e);
		            }
		        }
		    });
		    emailExecutor.shutdown();
			//dataEntryService.postSubmissionWork(getPostSubmissionModel());
			logger.info("Mail sent for"+this.postSubmissionModel.getInmateDetail().getQ1());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR : Exception while calling postSubmissionWork ", e);
		}
		
	}

	

}


