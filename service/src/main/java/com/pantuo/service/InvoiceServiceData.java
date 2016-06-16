package com.pantuo.service;
import java.security.Principal;

import org.springframework.data.domain.Page;

import com.pantuo.dao.pojo.JpaContract;
import com.pantuo.dao.pojo.JpaInvoice;
import com.pantuo.pojo.TableRequest;

import org.springframework.data.domain.Sort;


public interface InvoiceServiceData {
	Page<JpaInvoice> getAllInvoice(int city, TableRequest req,Principal principal);

    Page<JpaInvoice> getValidInvoice(int city, int page, int pageSize, Sort sort);

	
}
