package com.pantuo.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Service;

import com.pantuo.service.UserServiceInter;
import com.pantuo.util.DateUtil;
import com.pantuo.util.HttpTookit;
import com.pantuo.util.Pair;

import org.springframework.beans.factory.annotation.Autowired;

import com.pantuo.dynamic.service.GroovySimpleInterface;
@Service
public class FooImpl implements GroovySimpleInterface  {
	@Autowired
	UserServiceInter iter;


	def w=[];
	
	public String executew(List<Long > w1){
		print w1;
		return "--"
	}
	public  void execute() {
		println ("hello!11");
		//	def a=String.valueOf11(111);
		println(iter.count()+10);
		def a=111;
		
		def list = [];
		def map=[:]
		map.a=12;
		println map;
		println map['a'];
		print "----"
		list << System.currentTimeMillis();;
		list<< 121111;
		println  w;
		//iter.count1();
		//	prin
		println list;
	}
}