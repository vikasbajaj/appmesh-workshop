package com.ecs.workshop.emailnotification.controller;

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

import com.ecs.workshop.emailnotification.pojo.NotificationMessage;

@RestController
@RequestMapping("/emailnotification")
public class EmailNotificationController {

	private static final Log logger = LogFactory.getLog(EmailNotificationController.class);

	@Autowired
	private JavaMailSender javaMailSender;
	
	@Autowired
	private Environment env;
	
	@GetMapping("/healthcheck")
	public String getHealthStatus() {
		logger.info("Email Notification Service is up and running.............");
		String emailFrom = env.getProperty("email.from");
		logger.info("Environment Variables set for this service");
		logger.info("Email From : " + emailFrom);
		StringBuilder builder = new StringBuilder();
		builder.append("Environment Variables set for this service \r\n");
		builder.append("Email rom : " + emailFrom + "\r\n");
		return "Email Notification Service is up and running............." + builder.toString();
	}
	
	@PostMapping("/notify")
	public void sendnotification(@RequestBody NotificationMessage notificationMessage) {
		logger.info("Method Called : sendnotification " + new Date());
		notify(notificationMessage);
	}

	private void notify(NotificationMessage notificationMessage) {
		logger.info("Method Called : notify " + new Date());
		logger.info("***********" + notificationMessage.getAddressNotificationTo() + " "
				+ notificationMessage.getNotificationSentToEmailAddress() + " "
				+ notificationMessage.getNotificationText() + " " + notificationMessage.getSubject());
		SimpleMailMessage msg = new SimpleMailMessage();
		StringBuffer buffer = new StringBuffer("***********Email sent by Workshop************* \r\n");
		buffer.append(notificationMessage.getNotificationText());
		buffer.append("\r\nRegards, \r\n");
		buffer.append("AWS Workshop\r\n");
		String emailSentFrom = env.getProperty("email.from");
		logger.info("Sending Email from ************** " + emailSentFrom);
		msg.setFrom(emailSentFrom);
		msg.setTo(notificationMessage.getNotificationSentToEmailAddress());
		msg.setSubject(notificationMessage.getSubject());
		msg.setText(buffer.toString());
		javaMailSender.send(msg);
		logger.info("*******Email sent*******");
	}
}
