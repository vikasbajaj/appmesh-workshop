package com.appmesh.demo.dealers.api.pojo;

public class EnquiryMessage {

	private String buyername;
	private String buyeremailid;
	private String cartype;
	private String carbrand;
	private String carid;
	public EnquiryMessage() {
	}
	public EnquiryMessage(String buyername, String buyeremailid, String cartype, String carbrand, String carid) {
		super();
		this.buyername = buyername;
		this.buyeremailid = buyeremailid;
		this.cartype = cartype;
		this.carbrand = carbrand;
		this.carid = carid;
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
