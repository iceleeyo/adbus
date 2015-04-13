package com.pantuo.service;
import org.springframework.data.domain.Page;
import com.pantuo.dao.pojo.JpaSupplies;


public interface SuppliesServiceData {
	Page<JpaSupplies> getAllSupplies(String name, int page, int pageSize);
 
    Page<JpaSupplies> getValidSupplies(int page, int pageSize);

	
}
