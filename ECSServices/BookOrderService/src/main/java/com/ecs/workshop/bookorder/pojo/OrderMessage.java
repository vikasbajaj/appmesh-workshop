package com.ecs.workshop.bookorder.pojo;

public class OrderMessage {

	private Integer bookid;
	private String buyername;
	private String buyeremailid;
	private String bookname; 
	private String bookauthor;
	private String bookgenre;
	
	public OrderMessage() {
		
	}
	public OrderMessage(Integer bookid, String buyername, String buyderemailid, String bookname, String bookauthor,
			String bookgenre) {
		super();
		this.bookid = bookid;
		this.buyername = buyername;
		this.buyeremailid = buyderemailid;
		this.bookname = bookname;
		this.bookauthor = bookauthor;
		this.bookgenre = bookgenre;
	}
	public Integer getBookid() {
		return bookid;
	}
	public void setBookid(Integer bookid) {
		this.bookid = bookid;
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
