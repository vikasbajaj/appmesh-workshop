package com.appmesh.demo.enquiry.api.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Enquiry {

	private Integer id;
	private String buyername;
	private String buyeremailid;
	private String cartype;
	private String carbrand;
	private String carid;
	
	public Enquiry() {
		
	}
	public Enquiry(Integer id, String buyername, String buyeremailid, String cartype, String carbrand, String carid) {
		this.id = id;
		this.buyername = buyername;
		this.buyeremailid = buyeremailid;
		this.cartype = cartype;
		this.carbrand = carbrand;
		this.carid = carid;
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
	public String getCartype() {
		return cartype;
	}
	public void setCartype(String cartype) {
		this.cartype = cartype;
	}
	public String getCarbrand() {
		return carbrand;
	}
	public void setCarbrand(String carbrand) {
		this.carbrand = carbrand;
	}
	public String getCarid() {
		return carid;
	}
	public void setCarid(String carid) {
		this.carid = carid;
	}
}
