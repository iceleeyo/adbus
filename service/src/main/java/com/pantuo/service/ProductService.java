package com.pantuo.service;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;

import com.pantuo.dao.pojo.JpaBusOrderDetailV2;
import com.pantuo.dao.pojo.JpaBusOrderV2;
import com.pantuo.dao.pojo.JpaCardBoxMedia;
import com.pantuo.dao.pojo.JpaCpd;
import com.pantuo.dao.pojo.JpaProduct;
import com.pantuo.dao.pojo.JpaProduct.FrontShow;
import com.pantuo.dao.pojo.JpaProductLocation;
import com.pantuo.dao.pojo.JpaProductV2;
import com.pantuo.dao.pojo.JpaVideo32Group;
import com.pantuo.mybatis.domain.BusOrderDetailV2;
import com.pantuo.mybatis.domain.Product;
import com.pantuo.mybatis.domain.ProductV2;
import com.pantuo.pojo.TableRequest;
import com.pantuo.util.NumberPageUtil;
import com.pantuo.util.Pair;
import com.pantuo.web.view.MediaSurvey;
import com.pantuo.web.view.PlanRequest;
import com.pantuo.web.view.ProductView;

public interface ProductService {
    Page<JpaProduct> getAllProducts(int city,  boolean includeExclusive, String exclusiveUser,
    		TableRequest req,Principal principal);
    Page<JpaProduct> searchProducts(int city, Principal principal,
    		TableRequest req);

    Page<JpaProduct> getValidProducts(int city, JpaProduct.Type type,  boolean includeExclusive, String exclusiveUser,
    		TableRequest req ,FrontShow... fs);

    JpaProduct findById(int productId);

    void saveProduct(int city, JpaProduct product,MediaSurvey survey,HttpServletRequest request,Principal principal);

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
	Pair<Boolean, Long> saveBusOrderDetail(JpaBusOrderDetailV2 prod);
	Page<JpaBusOrderDetailV2> searchBusOrderDetailV2(int orderid,int pid, long seriaNum, int city, Principal principal, TableRequest req);
	Pair<Boolean, String> saveProductV2(ProductV2 productV2,MediaSurvey survey, long seriaNum, String userId);
	Page<JpaProductV2> searchProductV2s(int city, Principal principal, TableRequest req);
	Pair<Boolean, String> buyBodyPro(int pid, int city, String userId);
	Page<JpaBusOrderV2> searchBusOrderV2(int city, Principal principal, TableRequest req, String type);
	Pair<Boolean, String> removeBusOrderDetail(Principal principal, int city, int id);  
	
	
	
	
	
	Pair<Boolean, String>  buildPlan(  int city,long seriaNum, Principal principal); 
	
	Pair<Boolean, String> delPlan(int id,   Principal principal);
	public Pair<Boolean, PlanRequest> addPlan(int city,long seriaNum,String select ,int  number,String startDate1,Principal principal);
	
	public List<BusOrderDetailV2> getOrderDetailV2BySeriNum(long seriaNum,Principal principal);
	
	
	public Double querySelectPrice( int city,String  select);
	Pair<Boolean, String> changeProStats(int proId, int enable);
	
	/**
	 * 
	 */  
	
	JpaProductV2 findV2ById(int productId);
	Pair<Boolean, String> changeProV2Stats(int proId, String enable);
	JpaCpd findCpdById(int id);
	Pair<Boolean, String> checkProHadBought(int productId);
	List<JpaCardBoxMedia> selectProByMedias(String meids);

	public void fillImg(JpaProduct product);
	Pair<Boolean, String> saveBodyCombo(ProductV2 productV2, JpaBusOrderDetailV2 detailV2, MediaSurvey survey,
			String userId, int city, int orderDetailV2Id, int productV2Id);
	Long acountPrice(JpaBusOrderDetailV2 prod);
	Page<JpaBusOrderDetailV2> findAllBusOrderDetailV2(int city, Principal principal, TableRequest req);
	String getBodyProViewJson(int id);
	List<JpaProductLocation> getProTags(String belongTag);
	String selectLocationIdsByProId(int id);
	List<JpaVideo32Group> getAllVideo32Group();
}
