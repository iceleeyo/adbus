package com.pantuo.service;
import org.springframework.data.domain.Page;
import com.pantuo.dao.pojo.JpaSupplies;
import org.springframework.data.domain.Sort;


public interface SuppliesServiceData {
	Page<JpaSupplies> getAllSupplies(String name, int page, int pageSize, Sort sort);
 
    Page<JpaSupplies> getValidSupplies(int page, int pageSize, Sort sort);

	
}
