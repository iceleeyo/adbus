package com.pantuo.service;
import java.security.Principal;

import org.springframework.data.domain.Page;

import com.pantuo.dao.pojo.JpaBlackAd;
import com.pantuo.dao.pojo.JpaContract;
import com.pantuo.pojo.TableRequest;

import org.springframework.data.domain.Sort;


public interface ContractServiceData {
	Page<JpaContract> getAllContracts(int city, TableRequest req,Principal principal);
    Page<JpaContract> getValidContracts(int city, int page, int pageSize, Sort sort);
    
    Page<JpaBlackAd> getAllblackAd(int city, TableRequest req,Principal principal);
    Page<JpaBlackAd> getValidblackAd(int city, int page, int pageSize, Sort sort);

	
}
