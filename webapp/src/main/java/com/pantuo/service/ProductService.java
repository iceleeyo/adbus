package com.pantuo.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.pantuo.dao.pojo.JpaProduct;
import com.pantuo.mybatis.domain.Product;
import com.pantuo.util.NumberPageUtil;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

public interface ProductService {
    Page<JpaProduct> getAllProducts(String name, int page, int pageSize, Sort sort);

    Page<JpaProduct> getValidProducts(int page, int pageSize, Sort sort);

    JpaProduct findById(int productId);

    void saveProduct(JpaProduct product);

    public int countMyList(String name, String code, HttpServletRequest request);

    public List<Product> queryContractList(NumberPageUtil page, String name, String code, HttpServletRequest request);

}
