package com.pantuo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;

import com.pantuo.dao.pojo.JpaBusline;
import com.pantuo.util.Pair;

public interface BusMapService { 
	
	public Pair<Double, Double> getLocationFromAddress(Model model,String address) ;
	
	
	
	  Page<JpaBusline> getAllBuslines(Model model,int city, String address,int page, int pageSize, Sort sort);
	
}
