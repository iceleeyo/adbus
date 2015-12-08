package com.pantuo.service;

import com.pantuo.mybatis.persistence.ReportMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author tliu
 */
@Service
public class BoxService {

    @Autowired
    private ReportMapper mapper;
}
