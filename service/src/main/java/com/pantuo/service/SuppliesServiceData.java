package com.pantuo.service;
import java.security.Principal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import com.pantuo.dao.pojo.JpaSupplies;
import com.pantuo.pojo.TableRequest;


public interface SuppliesServiceData {
	Page<JpaSupplies> getAllSupplies(int city, Principal principal,TableRequest req);
 
    Page<JpaSupplies> getValidSupplies(int city, int page, int pageSize, Sort sort);
    public JpaSupplies findById(int suppId);
    public void saveSupplies(JpaSupplies supplies);
	
}
