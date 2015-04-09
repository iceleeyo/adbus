package com.pantuo.service;

import com.pantuo.dao.pojo.JpaProduct;
import com.pantuo.dao.pojo.JpaTimeslot;
import com.pantuo.mybatis.domain.Product;
import com.pantuo.util.NumberPageUtil;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface TimeslotService {

    long count();

    Page<JpaTimeslot> getAllTimeslots(String name, int page, int pageSize);

    JpaTimeslot findById(int id);

    void saveProduct(JpaTimeslot timeslot);

    void saveProducts(Iterable<JpaTimeslot> timeslots);


}
