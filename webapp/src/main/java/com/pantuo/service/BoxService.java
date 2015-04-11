package com.pantuo.service;

import com.pantuo.dao.BoxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author tliu
 */
@Service
public class BoxService {
    @Autowired
    private BoxRepository boxRepository;
}
