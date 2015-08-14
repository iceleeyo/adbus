package com.pantuo.service;

import com.pantuo.dao.CityRepository;
import com.pantuo.dao.pojo.JpaCity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public JpaCity fromName(String name, String media) {
        return nameMap.get(name + "/" + media);
    }

    public JpaCity fromName(String name, JpaCity.MediaType media) {
        return nameMap.get(name + "/" + media);
    }

    public JpaCity fromId(int id) {
    	
        return idMap.get(id);
    }

    public List<JpaCity> list(boolean distinct) {
        if (!distinct) {
            return list;
        } else {
            List<JpaCity> theList = new ArrayList<JpaCity>();
            for (JpaCity c : list) {
                boolean found = false;
                for (JpaCity c2 : theList) {
                    if (c2.getName().equals(c.getName())) {
                        found = true;
                    }
                }
                if (!found) {
                    theList.add(c);
                }
            }
            return theList;
        }
    }

    public List<JpaCity> listMedias(int cityId) {
        JpaCity city = fromId(cityId);
        if (city == null)
            city = list.get(0);
        List<JpaCity> theList = new ArrayList<JpaCity>();
        for (JpaCity c : list) {
            if (c.getName().equals(city.getName())) {
                theList.add(c);
            }
        }
        return theList;
    }

    public List<JpaCity> listMedias(String cityName) {
        List<JpaCity> theList = new ArrayList<JpaCity>();
        for (JpaCity c : list) {
            if (c.getName().equals(cityName)) {
                theList.add(c);
            }
        }
        return theList;
    }

    public void init() throws Exception {
        list = cityRepo.findAll();
        for (JpaCity city : list) {
            nameMap.put(city.getName() + "/" + city.getMediaType(), city);
            idMap.put(city.getId(), city);
        }
    }

}
