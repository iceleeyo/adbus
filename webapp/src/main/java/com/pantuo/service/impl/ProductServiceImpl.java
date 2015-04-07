package com.pantuo.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.mysema.query.types.Predicate;
import com.pantuo.dao.ProductRepository;
import com.pantuo.dao.pojo.JpaProduct;
import com.pantuo.dao.pojo.QJpaProduct;
import com.pantuo.dao.pojo.UserDetail;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Autowired
    ProductRepository productRepo;

    public Page<JpaProduct> getAllProducts(String name, int page, int pageSize) {
        if (page < 0)
            page = 0;
        if (pageSize < 1)
            pageSize = 1;
        Pageable p = new PageRequest(page, pageSize, new Sort("id"));
        if (name == null || StringUtils.isEmpty(name)) {
            return  productRepo.findAll(p);
        } else {
            Predicate query = QJpaProduct.jpaProduct.name.like("%" + name + "%");
            return productRepo.findAll(query, p);
        }
    }

    @Override
    public JpaProduct findById(int productId) {
        return productRepo.findOne(productId);
    }

    public void createProduct(JpaProduct product) {
        productRepo.save(product);
    }

    @Override
    public void updateProduct(JpaProduct product) {
        productRepo.save(product);
    }

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
