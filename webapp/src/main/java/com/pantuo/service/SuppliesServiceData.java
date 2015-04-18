package com.pantuo.service;
import java.security.Principal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import com.pantuo.dao.pojo.JpaSupplies;


public interface SuppliesServiceData {
	Page<JpaSupplies> getAllSupplies(Principal principal,String name, int page, int pageSize, Sort sort);
 
    Page<JpaSupplies> getValidSupplies(int page, int pageSize, Sort sort);

	
}
