package com.pantuo.service;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import com.pantuo.dao.pojo.JpaProduct;
import com.pantuo.dao.pojo.JpaProduct.FrontShow;
import com.pantuo.mybatis.domain.Product;
import com.pantuo.pojo.TableRequest;
import com.pantuo.util.NumberPageUtil;
import com.pantuo.web.view.ProductView;

public interface ProductService {
    Page<JpaProduct> getAllProducts(int city,  boolean includeExclusive, String exclusiveUser,
    		TableRequest req);
    Page<JpaProduct> searchProducts(int city, Principal principal,
    		TableRequest req);

    Page<JpaProduct> getValidProducts(int city, JpaProduct.Type type,  boolean includeExclusive, String exclusiveUser,
    		TableRequest req ,FrontShow... fs);

    JpaProduct findById(int productId);

    void saveProduct(int city, JpaProduct product);

    public int countMyList(int city, String name, String code, HttpServletRequest request);

    public List<Product> queryContractList(int city, NumberPageUtil page, String name, String code, HttpServletRequest request);

	Product selectProById(Integer productId);
	
	/**
	 * 
	 * 产品列表转换成vo
	 *
	 * @param list
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	Page<ProductView> getProductView( Page<JpaProduct> list);  

}
