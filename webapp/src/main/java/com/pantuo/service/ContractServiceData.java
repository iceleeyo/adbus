package com.pantuo.service;
import org.springframework.data.domain.Page;

import com.pantuo.dao.pojo.JpaContract;
import org.springframework.data.domain.Sort;


public interface ContractServiceData {
	Page<JpaContract> getAllContracts(int city, String name, String code, int page, int pageSize, Sort sort);

    Page<JpaContract> getValidContracts(int city, int page, int pageSize, Sort sort);

	
}
