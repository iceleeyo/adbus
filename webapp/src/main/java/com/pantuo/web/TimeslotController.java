package com.pantuo.web;

import com.pantuo.dao.pojo.JpaProduct;
import com.pantuo.pojo.DataTablePage;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.TimeslotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author tliu
 */
@Controller
@RequestMapping(produces = "application/json;charset=utf-8", value = "/timeslot")
public class TimeslotController {
    private static Logger log = LoggerFactory.getLogger(TimeslotController.class);

    @Autowired
    private TimeslotService timeslotService;

    @RequestMapping("ajax-list")
    @ResponseBody
    public DataTablePage<JpaProduct> getAllTimeslots(TableRequest req,
                                                     @CookieValue(value="city", defaultValue = "-1") int city) {

        return new DataTablePage(timeslotService.getAllTimeslots(city, req.getFilter("name"),
                req.getPage(), req.getLength(), req.getSort("id"), false), req.getDraw());
    }

    @RequestMapping(value = "/list")
    public String list() {
        return "timeslot_list";
    }

}
