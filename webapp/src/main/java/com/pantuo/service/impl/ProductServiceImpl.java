package com.pantuo.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
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

    public Page<JpaProduct> getAllProducts(int city, String name, int page, int pageSize, Sort sort) {
        if (page < 0)
            page = 0;
        if (pageSize < 1)
            pageSize = 1;
        sort = (sort == null ? new Sort("id") : sort);
        Pageable p = new PageRequest(page, pageSize, sort);
        BooleanExpression query = city >= 0 ? QJpaProduct.jpaProduct.city.eq(city) : QJpaProduct.jpaProduct.city.goe(0);
        if (name != null && !StringUtils.isEmpty(name)) {
            query = query.and(QJpaProduct.jpaProduct.name.like("%" + name + "%"));
        }
        return productRepo.findAll(query, p);
    }

  //  @Override
    public Page<JpaProduct> getValidProducts(int city, JpaProduct.Type type,  int page, int pageSize, Sort sort) {
        if (page < 0)
            page = 0;
        if (pageSize < 1)
            pageSize = 1;
        sort = (sort == null ? new Sort(Sort.Direction.DESC, "id") : sort);
        Pageable p = new PageRequest(page, pageSize, sort);
        BooleanExpression query = city >= 0 ? QJpaProduct.jpaProduct.city.eq(city) : QJpaProduct.jpaProduct.city.goe(0);
        if (type != null) {
            query = query.and(QJpaProduct.jpaProduct.type.eq(type));
        }
        query = query.and(QJpaProduct.jpaProduct.enabled.isTrue());
        return productRepo.findAll(query, p);
    }

    //  @Override
    public JpaProduct findById(int productId) {
        return productRepo.findOne(productId);
    }

  //  @Override
    public void saveProduct(int city, JpaProduct product) {
        product.setCity(city);
        com.pantuo.util.BeanUtils.filterXss(product);
        productRepo.save(product);
    }

    public int countMyList(int city, String name,String code, HttpServletRequest request) {
        return productMapper.countByExample(getExample(city, name, code));
    }
    public ProductExample getExample(int city, String name, String code) {
        ProductExample example = new ProductExample();
        ProductExample.Criteria ca = example.createCriteria();
        ca.andCityEqualTo(city);
        if (StringUtils.isNoneBlank(name)) {
        //	ca.andContractNameLike("%" + name + "%");
        }
        if (StringUtils.isNoneBlank(code)&&Long.parseLong(code)>0) {
            //ca.andContractNumEqualTo(Long.parseLong(code));
        }
        return example;
    }
    public List<Product> queryContractList(int city, NumberPageUtil page, String name, String code, HttpServletRequest request) {
        ProductExample ex = getExample(city, name, code);
        ex.setOrderByClause("id desc");
        ex.setLimitStart(page.getLimitStart());
        ex.setLimitEnd(page.getPagesize());
        return productMapper.selectByExample(ex);
    }

    public Product selectProById(Integer productId) {
        return productMapper.selectByPrimaryKey(productId);
    }
}
