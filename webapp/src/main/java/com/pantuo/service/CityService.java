package com.pantuo.service;

import com.pantuo.dao.CityRepository;
import com.pantuo.dao.pojo.JpaCity;
import com.pantuo.mybatis.persistence.ReportMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tliu
 */
@Service
public class CityService {

    @Autowired
    private CityRepository cityRepo;

    private List<JpaCity> list = null;
    private Map<String, JpaCity> nameMap = new HashMap<String, JpaCity>();
    private Map<Integer, JpaCity> idMap = new HashMap<Integer, JpaCity>();

    public JpaCity fromName(String name) {
        return nameMap.get(name);
    }

    public JpaCity fromId(int id) {
        return idMap.get(id);
    }

    public List<JpaCity> list() {
        return list;
    }

    public void init() throws Exception {
        list = cityRepo.findAll();
        for (JpaCity city : list) {
            nameMap.put(city.getName(), city);
            idMap.put(city.getId(), city);
        }
    }
}
