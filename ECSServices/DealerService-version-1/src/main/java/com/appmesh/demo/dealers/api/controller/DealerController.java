package com.appmesh.demo.dealers.api.controller;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.appmesh.demo.dealers.api.pojo.EnquiryMessage;

@RestController
@RequestMapping("/dealers")
public class DealerController {

	private static final Log logger = LogFactory.getLog(DealerController.class);

	@Autowired
	private JavaMailSender javaMailSender;
	
	@Autowired
	private Environment env;
	
	@GetMapping("/healthcheck")
	public String getHealthStatus() {
		logger.info("Dealer Service is up and running..................");
		return "Dealer Service is up and running..................";
	}
	
	@PostMapping("/newenquiry")
	public void newenquiry(@RequestBody EnquiryMessage enquiryMessage) {
		logger.info("Method Called: ---------------- newenquiry " + new Date());
		processEnquiry(enquiryMessage.getBuyeremailid(),enquiryMessage.getBuyername(),enquiryMessage.getCarbrand(),enquiryMessage.getCarid(), enquiryMessage.getCartype());
		notify(enquiryMessage);
	}
	private void processEnquiry(String buyeremailid, String buyername, String carbrand, String carid, String cartype) {
		logger.info("Processing enquiry......................");
		logger.info("Buyer Email Id: " + buyeremailid);
		logger.info("Buyer Name: " + buyername);
		logger.info("Car Brand: " + carbrand);
		logger.info("Car Id: " + carid);
		logger.info("Buyer Car Type: " + cartype);
	}
	private void notify(EnquiryMessage enquiryMessage) {
		logger.info("Dealers api - notify method called....................");
		StringBuilder builder = new StringBuilder();
		builder.append("************ ###### Version 1 ##### ***********");
		builder.append("************Email by AppMesh Demo***********");
		builder.append("************ #### Version 1 ###### *********** \r\n");
		builder.append("New Enquiry Received......\r\n");
		builder.append("Prospect Buyer emaild Id: " + enquiryMessage.getBuyeremailid() + " \r\n");
		builder.append("Prospect Buyer Name: " + enquiryMessage.getBuyername() + " \r\n");
		builder.append("Interested in Car Brand : " + enquiryMessage.getCarbrand() + " \r\n");
		builder.append("Interested in Car id: " + enquiryMessage.getCarid() + " \r\n");
		builder.append("Interested in Car Type : " + enquiryMessage.getCartype() + " \r\n");
				
		SimpleMailMessage msg = new SimpleMailMessage();
		String emailSentFrom = env.getProperty("workshop.email.from");
		logger.info("Sending Email from ************** " + emailSentFrom);
		msg.setFrom(emailSentFrom);
		msg.setTo(enquiryMessage.getBuyeremailid());
		msg.setSubject("New Enquiry -- AppMesh Demo");
		msg.setText(builder.toString());
		javaMailSender.send(msg);
		logger.info("*******Email sent*******");
	}
}
