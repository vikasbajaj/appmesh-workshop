package com.ecs.workshop.bookorder.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class BookOrder {

	private Integer id;
	private String buyername;
	private String buyeremailid;
	private String bookname;
	private String bookauthor;
	private String bookgenre;
	
	public BookOrder() {
		
	}
	public BookOrder(Integer id, String buyername, String buyeremailid, String bookname, String bookauthor,
			String bookgenre) {
		super();
		this.id = id;
		this.buyername = buyername;
		this.buyeremailid = buyeremailid;
		this.bookname = bookname;
		this.bookauthor = bookauthor;
		this.bookgenre = bookgenre;
	}
	
	public BookOrder(String buyername, String buyeremailid, String bookname, String bookauthor,
			String bookgenre) {
		super();
		this.buyername = buyername;
		this.buyeremailid = buyeremailid;
		this.bookname = bookname;
		this.bookauthor = bookauthor;
		this.bookgenre = bookgenre;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getBuyername() {
		return buyername;
	}
	public void setBuyername(String buyername) {
		this.buyername = buyername;
	}
	public String getBuyeremailid() {
		return buyeremailid;
	}
	public void setBuyeremailid(String buyeremailid) {
		this.buyeremailid = buyeremailid;
	}
	public String getBookname() {
		return bookname;
	}
	public void setBookname(String bookname) {
		this.bookname = bookname;
	}
	public String getBookauthor() {
		return bookauthor;
	}
	public void setBookauthor(String bookauthor) {
		this.bookauthor = bookauthor;
	}
	public String getBookgenre() {
		return bookgenre;
	}
	public void setBookgenre(String bookgenre) {
		this.bookgenre = bookgenre;
	}
}
