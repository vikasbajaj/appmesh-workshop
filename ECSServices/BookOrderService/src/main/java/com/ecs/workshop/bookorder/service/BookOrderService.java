package com.ecs.workshop.bookorder.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecs.workshop.bookorder.entity.BookOrder;
import com.ecs.workshop.bookorder.repository.BookOrderRepository;

@Service
public class BookOrderService {

	@Autowired
	private BookOrderRepository repo;
	
	public List<BookOrder> listAll(){
		return repo.findAll();
	}
	public void save(BookOrder bookOrder) {
		repo.save(bookOrder);
	}
	public BookOrder getOrder(Integer id) {
		return repo.findById(id).get();
	}
}
