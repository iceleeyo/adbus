package com.pantuo.service;

import com.pantuo.dao.BoxRepository;
import com.pantuo.dao.pojo.Box;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author tliu
 */
@Service
public class BoxService {
    @Autowired
    private BoxRepository boxRepository;
}
