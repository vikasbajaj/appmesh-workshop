package com.ecs.workshop.bookorder.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecs.workshop.bookorder.entity.BookOrder;

public interface BookOrderRepository extends JpaRepository<BookOrder, Integer>{
}
