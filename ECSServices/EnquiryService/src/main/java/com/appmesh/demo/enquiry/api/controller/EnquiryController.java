package com.appmesh.demo.enquiry.api.controller;

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

import com.appmesh.demo.enquiry.api.entity.Enquiry;
import com.appmesh.demo.enquiry.api.pojo.EnquiryMessage;
import com.appmesh.demo.enquiry.api.service.EnquiryService;

@RestController
@RequestMapping("/enquiry")
public class EnquiryController {

	private static final Log logger = LogFactory.getLog(EnquiryController.class);

	@Autowired
	private EnquiryService enquiryService;

	@Autowired
	private Environment env;

	@GetMapping("/healthcheck")
	public String getHealthStatus() {
		logger.info("Enquiry Service is up and running..............");
		return "Enquiry Service is up and running.............";
	}
	
	@GetMapping("/enquiries")
	public List<Enquiry> getAllEnquiries() {
		logger.info("Method Called : getAllEnquiries " + new Date());
		return enquiryService.listAll();
	}
	
	@GetMapping("/enquiries/{id}")
	public ResponseEntity<Enquiry> getEnquiry(@PathVariable Integer id) {
		logger.info("Method Called : getEnquiry - <with id> " + id + " " + new Date());
		try {
			Enquiry enquiry = enquiryService.getEnquiry(id);
			return new ResponseEntity<Enquiry>(enquiry, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<Enquiry>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/callexternalapi")
	public String callExternalApi() {
		logger.info("Service Invoked----------Method Called : callExternalApi " + new Date());
		RestTemplate restTemplate = new RestTemplate();
		String externalServiceendpoint = env.getProperty("external.api.endpoint");
		logger.info(externalServiceendpoint);
	    String result = restTemplate.getForObject(externalServiceendpoint, String.class);
	    logger.info("Result from external Endpoint is " + result);
	    return result;
	}
	
	@PostMapping("/newenquiry")
	public void newEnquiry(@RequestBody Enquiry enquiry) {
		logger.info("Service Invoked----------Method Called : newEnquiry " + new Date());
		enquiryService.save(enquiry);		
		EnquiryMessage enquiryMessage = new EnquiryMessage(enquiry.getBuyername(), enquiry.getBuyeremailid(), enquiry.getCartype(), enquiry.getCarbrand(), enquiry.getCarid());
		sendToDealer(enquiryMessage);
	}
	private void sendToDealer(EnquiryMessage enquiryMessage) {
		logger.info("Method Called : --------------------- sendToDealer " + new Date());
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		String dealerServiceEndpoint = env.getProperty("dealer.api.endpoint");
		logger.info("Dealer Service endpoint: " + dealerServiceEndpoint);
		logger.info("********Calling Dealer Service");
		HttpEntity<EnquiryMessage> entity = new HttpEntity<>(enquiryMessage, headers);
		restTemplate.exchange(dealerServiceEndpoint, HttpMethod.POST, entity, Void.class);
	}
}
