package com.pantuo.service;

import com.pantuo.dao.IndustryRepository;
import com.pantuo.dao.pojo.JpaIndustry;
import com.pantuo.mybatis.persistence.ReportMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author tliu
 */
@Service
public class IndustryService {

    @Autowired
    private IndustryRepository repo;

    public List<JpaIndustry> getIndustries(List<Integer> ids) {
        return repo.findAll(ids);
    }
}
