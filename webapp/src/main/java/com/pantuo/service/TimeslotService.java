package com.pantuo.service;

import com.pantuo.dao.pojo.JpaProduct;
import com.pantuo.dao.pojo.JpaTimeslot;
import com.pantuo.mybatis.domain.Product;
import com.pantuo.pojo.highchart.DayList;
import com.pantuo.util.NumberPageUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TimeslotService {

    long count();

    long sumDuration();

    long sumPeakDuration();

    Map<Integer, Long> getDurationByHour();

    Page<JpaTimeslot> getAllTimeslots(String name, int page, int pageSize, Sort sort, boolean fetchDisabled);

    JpaTimeslot findById(int id);

    void saveProduct(JpaTimeslot timeslot);

    void saveProducts(Iterable<JpaTimeslot> timeslots);
}
