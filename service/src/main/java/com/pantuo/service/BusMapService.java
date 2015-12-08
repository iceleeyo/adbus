package com.pantuo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;

import com.pantuo.dao.pojo.JpaBusModel;
import com.pantuo.dao.pojo.JpaBusline;
import com.pantuo.pojo.TableRequest;
import com.pantuo.util.Pair;
import com.pantuo.web.view.JpaBuslineView;

public interface BusMapService { 
	
	public Pair<Double, Double> getLocationFromAddress(Model model,String address) ;
	
	
	
	  Page<JpaBusline> getAllBuslines(Model model,int city, String address,int page, int pageSize, Sort sort);
	  /**
	   * 
	   * 根据线路站点匹配
	   *
	   * @param model
	   * @param city
	   * @param address
	   * @param page
	   * @param pageSize
	   * @param sort
	   * @return
	   * @since pantuo 1.0-SNAPSHOT
	   */
	  Page<JpaBusline> querySiteLineSearch(Model model,int city, String address,int page, int pageSize, Sort sort);
	  /**
	   * @deprecated
	   * 增加车辆自运营数量
	   *
	   * @since pantuo 1.0-SNAPSHOT
	   */
	  public  Page<JpaBuslineView>   putLineCarToPageView(TableRequest req,Page<JpaBusline> page);



	public JpaBusline findLineById(int id);



	public JpaBusModel findModelById(int id);
	
}
