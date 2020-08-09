package com.ecs.workshop.bookcatalogue.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecs.workshop.bookcatalogue.entity.Book;
import com.ecs.workshop.bookcatalogue.repository.BookCatalogueRepository;

@Service
public class BookCatalogueService {

	@Autowired
	private BookCatalogueRepository repo;
	
	public List<Book> listAll(){
		return repo.findAll();
	}
	public void save(Book employee) {
		repo.save(employee);
	}
	public Book getBook(Integer id) {
		return repo.findById(id).get();
	}
	public void deleteBook(Integer id) {
		repo.deleteById(id);
	}
}
