package com.ecs.workshop.bookcatalogue.controller;

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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.ecs.workshop.bookcatalogue.entity.Book;
import com.ecs.workshop.bookcatalogue.pojo.NotificationMessage;
import com.ecs.workshop.bookcatalogue.pojo.OrderMessage;
import com.ecs.workshop.bookcatalogue.service.BookCatalogueService;

@RestController
@RequestMapping("/bookcatalogue")
public class BookCatalogueController {

	private static final Log logger = LogFactory.getLog(BookCatalogueController.class);

	@Autowired
	private BookCatalogueService bookService;

	@Autowired
	private Environment env;

	@GetMapping("/healthcheck")
	public String getHealthStatus() {
		logger.info("Book Catalogue Service is up and running..........");
		String orderServiceEndpoint = env.getProperty("orderservice.endpoint");
		String notificationServiceEndpoint = env.getProperty("notificationservice.endpoint");
		logger.info("Environment Variables set for this service");
		logger.info("Order Service Endpoint : " + orderServiceEndpoint);
		logger.info("Notification Service Endpoint : " + notificationServiceEndpoint);
		StringBuilder builder = new StringBuilder();
		builder.append("Environment Variables set for this service \r\n");
		builder.append("Order Service Endpoint : " + orderServiceEndpoint + "\r\n"); 
		builder.append("Notification Service Endpoint : " + notificationServiceEndpoint + "\r\n");
		return "Book Catalogue Service is up and running.............." + builder.toString();
	}
	
	@GetMapping("/books")
	public List<Book> getAllBooks() {
		logger.info("Method Called : getAllBooks " + new Date());
		return bookService.listAll();
	}
	
	@GetMapping("/books/{id}")
	public ResponseEntity<Book> getBook(@PathVariable Integer id) {
		logger.info("Method Called : getBook - <with id> " + id + " " + new Date());
		try {
			Book book = bookService.getBook(id);
			return new ResponseEntity<Book>(book, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<Book>(HttpStatus.NOT_FOUND);
		}
	}
	@PostMapping("/neworder")
	public void addNewBookOrder(@RequestBody OrderMessage orderMessage) {
		logger.info("Method Called : addNewBookOrder " + new Date());
		//Call Book Order Service to Place an Order
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		String orderServiceEndpoint = env.getProperty("orderservice.endpoint");
		logger.debug("Order Service Endpoint from properties files is " + orderServiceEndpoint);
		HttpEntity<OrderMessage> entity = new HttpEntity<>(orderMessage, headers);
		restTemplate.exchange(orderServiceEndpoint, HttpMethod.POST, entity, Void.class);
	}
	@PostMapping("/books")
	public void addNewBook(@RequestBody Book book) {
		logger.info("Method Called : addNewBook " + new Date());
		bookService.save(book);
		
		String notificationText = "Book added." + 
								 "\r\n \r\nBook Name: " + book.getName() + " | Author Name: " + book.getAuthorname() + 
								 " | Book Genre: " + book.getGenre() + " | Book Price: " + book.getPrice();
		NotificationMessage notificationMessage = new NotificationMessage("New Book added",	notificationText, book.getAuthoremailid(), book.getAuthorname());
		nofity(notificationMessage);
	}

	@PutMapping("/books/{id}")
	public ResponseEntity<?> update(@RequestBody Book book, @PathVariable Integer id) {
		try {
			Book existingBook = bookService.getBook(id);
			if (existingBook != null)
				logger.info("Book exist in the system with book id : " + id);
			bookService.save(book);
			String notificationText = "Book updated. Book: " + existingBook.getName() + " Author: " + existingBook.getAuthorname()
			+ " has been updated.\r\n\r\nUpdated Details are  \r\n\r\nBook Name : " + book.getName() + " Author Name is "
			+ book.getAuthorname() + " | Book Genre : " + book.getGenre() + " | Book Price : " + book.getPrice();
				
			NotificationMessage notificationMessage = new NotificationMessage("Book updated",notificationText, book.getAuthoremailid(), book.getAuthorname());
			nofity(notificationMessage);
			
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	@DeleteMapping("/books/{id}")
	public void delete(@PathVariable Integer id) {
		Book existingBook = bookService.getBook(id);
		String notificationText = "Book deleted." + " \r\n\r\nBook: " + existingBook.getName() + "Author Name: " + existingBook.getAuthorname();
		String authoremailId = existingBook.getAuthoremailid();
		String authorName = existingBook.getAuthorname();
		bookService.deleteBook(id);
		NotificationMessage notificationMessage = new NotificationMessage("Book Deleted",	notificationText, authoremailId, authorName);
		nofity(notificationMessage);
		
	}
	private void nofity(NotificationMessage message) {
		logger.info("Method Called : nofity " + new Date());
		logger.info("This is Notification Message " + message);
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		String notificationServiceEndpoint = env.getProperty("notificationservice.endpoint");
		logger.info("Notification Service Endpoint : " + notificationServiceEndpoint);
		HttpEntity<NotificationMessage> entity = new HttpEntity<>(message, headers);
		restTemplate.exchange(notificationServiceEndpoint, HttpMethod.POST, entity, Void.class);
	}
}
