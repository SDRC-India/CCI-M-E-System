/**
 * 
 */
package org.sdrc.scps.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.sdrc.scps.repository.AttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */

@Controller
public class CommonController {

	@Autowired
	private AttachmentRepository attachmentRepository;

	@GetMapping(value = "bypass/doc")
	public void downLoad(@RequestParam("fileId") long fileId, HttpServletResponse response) throws IOException {

		InputStream inputStream;
		String fileName = attachmentRepository.findByAttachmentId(fileId).getFilePath();
		try {
			fileName = fileName.trim().replaceAll("%3A", ":").replaceAll("%2F", "/").replaceAll("%5C", "/")
					.replaceAll("%2C", ",").replaceAll("\\\\", "/").replaceAll("\\+", " ").replaceAll("%22", "")
					.replaceAll("%3F", "?").replaceAll("%3D", "=");
			inputStream = new FileInputStream(fileName);
			String headerKey = "Content-Disposition";
			String headerValue = "";
			 String type = new java.io.File(fileName).getName().split("\\.")[new java.io.File(fileName).getName().split("\\.").length-1];
			{
				headerValue = String.format("inline; filename=\"%s\"", new java.io.File(fileName).getName());
				if(type.equalsIgnoreCase("pdf"))
				response.setContentType("application/pdf"); // for all file
				else
					response.setContentType("image/jpeg");
			}
			response.setHeader(headerKey, headerValue);

			ServletOutputStream outputStream = response.getOutputStream();
			FileCopyUtils.copy(inputStream, outputStream);
			inputStream.close();
			outputStream.flush();
			outputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
