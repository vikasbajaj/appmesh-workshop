package com.appmesh.demo.enquiry.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.appmesh.demo.enquiry.api.entity.Enquiry;

public interface EnquiryRepository extends JpaRepository<Enquiry, Integer>{
}
