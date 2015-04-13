package com.pantuo.service;
import org.springframework.data.domain.Page;

import com.pantuo.dao.pojo.JpaContract;


public interface ContractServiceData {
	Page<JpaContract> getAllContracts(String name, int page, int pageSize);

    Page<JpaContract> getValidContracts(int page, int pageSize);

	
}
