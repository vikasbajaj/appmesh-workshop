package com.ecs.workshop.bookcatalogue.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Book {

	private Integer id;
	private String name;
	private float price;
	private String genre;
	private String authorname;
	private String authoremailid;
	
	public Book() {
	}
	public Book(Integer id, String name, float price, String genre, String authorname,String authoremailid) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.genre = genre;
		this.authorname = authorname;
		this.authoremailid = authoremailid;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getAuthorname() {
		return authorname;
	}

	public void setAuthorname(String authorname) {
		this.authorname = authorname;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAuthoremailid() {
		return authoremailid;
	}
	public void setAuthoremailid(String authoremailid) {
		this.authoremailid = authoremailid;
	}
}
