package com.appmesh.demo.enquiry.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appmesh.demo.enquiry.api.entity.Enquiry;
import com.appmesh.demo.enquiry.api.repository.EnquiryRepository;

@Service
public class EnquiryService {

	@Autowired
	private EnquiryRepository repo;
	
	public List<Enquiry> listAll(){
		return repo.findAll();
	}
	public void save(Enquiry employee) {
		repo.save(employee);
	}
	public Enquiry getEnquiry(Integer id) {
		return repo.findById(id).get();
	}
	public void deleteEnquiry(Integer id) {
		repo.deleteById(id);
	}
}
