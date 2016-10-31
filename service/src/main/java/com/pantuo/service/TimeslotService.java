package com.pantuo.service;

import com.pantuo.dao.pojo.JpaInfoImgSchedule;
import com.pantuo.dao.pojo.JpaProduct;
import com.pantuo.dao.pojo.JpaTimeslot;
import com.pantuo.mybatis.domain.Product;
import com.pantuo.pojo.TableRequest;
import com.pantuo.pojo.highchart.DayList;
import com.pantuo.util.NumberPageUtil;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import javax.servlet.http.HttpServletRequest;

import java.security.Principal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TimeslotService {

    long count();

    long sumDuration(int city);

    long sumPeakDuration(int city);

    Map<Integer, Long> getDurationByHour(int city);

    Page<JpaTimeslot> getAllTimeslots(int city, String name, int page, int pageSize, Sort sort, boolean fetchDisabled);

    JpaTimeslot findById(int id);

    void saveTimeslot(JpaTimeslot timeslot);

    void saveTimeslots(Iterable<JpaTimeslot> timeslots);

	List<JpaInfoImgSchedule> getInfoSchedule(int city, TableRequest req, Principal principal, String mtype) throws ParseException;

	List<JpaInfoImgSchedule> orderSchedule(TableRequest req, Principal principal, String mtype);

	String exportOrderSchedule(TableRequest req, Principal principal, String mtype);
}
