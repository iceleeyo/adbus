package com.pantuo.dynamic.service;

import java.util.List;


import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Service;

import com.pantuo.service.UserServiceInter;
import com.pantuo.util.HttpTookit;
import com.pantuo.util.Pair;

import org.springframework.beans.factory.annotation.Autowired;

/*groovy 闭包实现*/

/*
def impl = [execute:{ println'test' },
	executew:{ str ->
		println str
		return "str-:"+str
	}] as GroovySimpleInterface

*/
