package com.pantuo.service.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pantuo.mybatis.domain.ContractExample;
import com.pantuo.mybatis.domain.Product;
import com.pantuo.mybatis.domain.ProductExample;
import com.pantuo.mybatis.persistence.ProductMapper;
import com.pantuo.service.ProductService;
import com.pantuo.util.NumberPageUtil;

@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	ProductMapper productMapper;
	
	
	
	 public int countMyList(String name,String code, HttpServletRequest request) {
			return productMapper.countByExample(getExample(name, code));
		}
	    public ProductExample getExample(String name, String code) {
	    	ProductExample example = new ProductExample();
	    	ProductExample.Criteria ca = example.createCriteria();
			if (StringUtils.isNoneBlank(name)) {
			//	ca.andContractNameLike("%" + name + "%");
			}
			if (StringUtils.isNoneBlank(code)&&Long.parseLong(code)>0) {
				//ca.andContractNumEqualTo(Long.parseLong(code));
			}
			return example;
		}
	    public List<Product> queryContractList(NumberPageUtil page, String name, String code, HttpServletRequest request) {
	    	ProductExample ex = getExample(name, code);
			ex.setOrderByClause("id desc");
			ex.setLimitStart(page.getLimitStart());
			ex.setLimitEnd(page.getPagesize());
			return productMapper.selectByExample(ex);
		}
}
