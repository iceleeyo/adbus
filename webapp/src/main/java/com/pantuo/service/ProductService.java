package com.pantuo.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.pantuo.mybatis.domain.Product;
import com.pantuo.util.NumberPageUtil;

public interface ProductService {
	 public int countMyList(String name,String code, HttpServletRequest request) ;
	 public List<Product> queryContractList(NumberPageUtil page, String name, String code, HttpServletRequest request);

}
