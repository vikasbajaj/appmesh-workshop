package com.ecs.workshop.bookorder.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.ecs.workshop.bookorder.entity.BookOrder;
import com.ecs.workshop.bookorder.pojo.NotificationMessage;
import com.ecs.workshop.bookorder.pojo.OrderMessage;
import com.ecs.workshop.bookorder.service.BookOrderService;

@RestController
@RequestMapping("/bookorder")
public class BookOrderController {

	private static final Log logger = LogFactory.getLog(BookOrderController.class);

	@Autowired
	private BookOrderService bookOrderService;

	@Autowired
	private Environment env;

	@GetMapping("/healthcheck")
	public String getHealthStatus() {
		logger.info("Book Order Service is up and running...........");
		String notificationServiceEndpoint = env.getProperty("notificationservice.endpoint");
		logger.info("Environment Variables set for this service");
		logger.info("Notification Service Endpoint : " + notificationServiceEndpoint);
		StringBuilder builder = new StringBuilder();
		builder.append("Environment Variables set for this service \r\n");
		builder.append("Notification Service Endpoint : " + notificationServiceEndpoint + "\r\n");
		return "Book Order Service is up and running..........." + builder.toString();
	}
	
	@GetMapping("/orders")
	public List<BookOrder> getAllOrder() {
		logger.info("Method Called : getAllOrder " + new Date());
		return bookOrderService.listAll();
	}

	@GetMapping("/orders/{id}")
	public ResponseEntity<BookOrder> getOrder(@PathVariable Integer id) {
		logger.info("Method Called : getOrder - <with id> " + id + " " + new Date());
		try {
			BookOrder order = bookOrderService.getOrder(id);
			return new ResponseEntity<BookOrder>(order, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<BookOrder>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/orders")
	public void addNewBookOrder(@RequestBody OrderMessage orderMessage) {
		logger.info("Method Called : addNewBookOrder " + new Date());
		logger.info(orderMessage.getBookauthor() + " " + orderMessage.getBookgenre() + " " + 
				orderMessage.getBookname() + " " + orderMessage.getBuyeremailid() + " " + orderMessage.getBuyername());
		
		BookOrder order = new BookOrder(orderMessage.getBuyername(), orderMessage.getBuyeremailid(),
				orderMessage.getBookname(), orderMessage.getBookauthor(), orderMessage.getBookgenre());
		
		logger.info(order.getBookauthor() + " " + order.getBookgenre() + " " + 
					order.getBookname() + " " + order.getBuyeremailid() + " " + order.getBuyername());
		bookOrderService.save(order);
		String subject = "New Book Order, " + orderMessage.getBookname();
		String notificationText = "New Book Order. Book details are : \r\n \r\n"
				+ "Book Name: " + orderMessage.getBookname() + "\r\nBook Author: " + orderMessage.getBookauthor();
		NotificationMessage notificationMessage = new NotificationMessage(subject, notificationText,
				orderMessage.getBuyeremailid(), orderMessage.getBuyername());
		nofityBookBuyers(notificationMessage);
	}

	private void nofityBookBuyers(NotificationMessage message) {
		logger.info("Method Called : nofityBookBuyers " + new Date());
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		String notificationServiceEndpoint = env
				.getProperty("notificationservice.endpoint");
		logger.info("Notification Service URL: " + notificationServiceEndpoint);
		HttpEntity<NotificationMessage> entity = new HttpEntity<>(message, headers);
		restTemplate.exchange(notificationServiceEndpoint, HttpMethod.POST, entity, Void.class);
		
	}
}
