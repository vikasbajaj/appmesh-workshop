package com.ecs.workshop.bookcatalogue.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecs.workshop.bookcatalogue.entity.Book;

public interface BookCatalogueRepository extends JpaRepository<Book, Integer>{
}
