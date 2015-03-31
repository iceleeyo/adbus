package com.pantuo.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.pantuo.mybatis.domain.Contract;
import com.pantuo.util.NumberPageUtil;

public interface OrderService {
	 public int countMyList(String name,String code, HttpServletRequest request) ;
	 public List<Contract> queryContractList(NumberPageUtil page, String name, String code, HttpServletRequest request);

}
