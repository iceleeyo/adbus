package com.pantuo.web;

import com.pantuo.dao.pojo.*;
import com.pantuo.mybatis.domain.*;
import com.pantuo.pojo.DataTablePage;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.BusService;
import com.pantuo.service.TimeslotService;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

/**
 * @author tliu
 */
@Controller
@RequestMapping(produces = "application/json;charset=utf-8", value = "/bus")
public class BusController {
    private static Logger log = LoggerFactory.getLogger(BusController.class);

    @Autowired
    private BusService busService;

    @RequestMapping("ajax-list")
    @ResponseBody
    public DataTablePage<JpaBus> getAllBuses(TableRequest req,
                                                     @CookieValue(value="city", defaultValue = "-1") int cityId,
                                                     @ModelAttribute("city") JpaCity city
                                                     ) {
        if (city == null || city.getMediaType() != JpaCity.MediaType.body)
            return new DataTablePage(Collections.emptyList());

        return new DataTablePage(busService.getAllBuses(cityId, req,
                req.getPage(), req.getLength(), req.getSort("id"), false), req.getDraw());
    }


    @RequestMapping("ajax-all-lines")
    @ResponseBody
    public DataTablePage<JpaBusline> getAllLines(TableRequest req,
                                                 @CookieValue(value="city", defaultValue = "-1") int cityId,
                                                 @ModelAttribute("city") JpaCity city
    ) {
        if (city == null || city.getMediaType() != JpaCity.MediaType.body)
            return new DataTablePage(Collections.emptyList());

        String levelStr = req.getFilter("level");
        JpaBusline.Level level = null;
        if (!StringUtils.isBlank(levelStr)) {
            level = JpaBusline.Level.fromNameStr(levelStr);
        }
        return new DataTablePage(busService.getAllBuslines(cityId, level, req.getFilter("name"),
                req.getPage(), req.getLength(), req.getSort("id")), req.getDraw());
    }
    @RequestMapping(value = "/ajaxdetail/{id}")
    @ResponseBody
    public JpaBus ajaxdetail(@PathVariable int id,
                                Model model, HttpServletRequest request) {
       return  busService.findById(id);
    }
    @RequestMapping("ajax-all-models")
    @ResponseBody
    public DataTablePage<JpaBusModel> getAllModels(TableRequest req,
                                                 @CookieValue(value="city", defaultValue = "-1") int cityId,
                                                 @ModelAttribute("city") JpaCity city
    ) {
        if (city == null || city.getMediaType() != JpaCity.MediaType.body)
            return new DataTablePage(Collections.emptyList());

        return new DataTablePage(busService.getAllBusModels(cityId,
                req.getFilter("name"), req.getFilter("manufacturer"),
                req.getPage(), req.getLength(), req.getSort("id")), req.getDraw());
    }

    @RequestMapping("ajax-all-companies")
    @ResponseBody
    public DataTablePage<JpaBusinessCompany> getAllCompanies(TableRequest req,
                                                   @CookieValue(value="city", defaultValue = "-1") int cityId,
                                                   @ModelAttribute("city") JpaCity city
    ) {
        if (city == null || city.getMediaType() != JpaCity.MediaType.body)
            return new DataTablePage(Collections.emptyList());

        return new DataTablePage(busService.getAllBusinessCompanies(cityId, req.getFilter("name"),
                req.getFilter("contact"),
                req.getPage(), req.getLength(), req.getSort("id")), req.getDraw());
    }

    @RequestMapping(value = "/list")
    public String list() {
        return "bus_list";
    }

    @RequestMapping(value = "/lines")
    public String lines() {
        return "bus_lines";
    }

    @RequestMapping(value = "/models")
    public String models() {
        return "bus_models";
    }

    @RequestMapping(value = "/companies")
    public String companies() {
        return "bus_companies";
    }

    @RequestMapping("ajax-bus-lines")
    @ResponseBody
    public Iterable<CountableBusLine> getBuslines(
            @CookieValue(value = "city", defaultValue = "-1") int city,
            @RequestParam("level") JpaBusline.Level level,
            @RequestParam(value = "category", required = false) JpaBus.Category category,
            @RequestParam(value = "lineId", required = false) Integer lineId,
            @RequestParam(value = "modelId", required = false) Integer modelId,
            @RequestParam(value = "companyId", required = false) Integer companyId
    ) {
        return busService.getBuslines(city, level, category, lineId, modelId, companyId);
    }

    @RequestMapping("ajax-bus-models")
    @ResponseBody
    public Iterable<CountableBusModel> getBusModels(
            @CookieValue(value = "city", defaultValue = "-1") int city,
            @RequestParam("level") JpaBusline.Level level,
            @RequestParam(value = "category", required = false) JpaBus.Category category,
            @RequestParam(value = "lineId", required = false) Integer lineId,
            @RequestParam(value = "modelId", required = false) Integer modelId,
            @RequestParam(value = "companyId", required = false) Integer companyId
    ) {
        return busService.getBusModels(city, level, category, lineId, modelId, companyId);
    }


    @RequestMapping("ajax-bus-companies")
    @ResponseBody
    public Iterable<CountableBusinessCompany> getBusinessCompanies(
            @CookieValue(value = "city", defaultValue = "-1") int city,
            @RequestParam("level") JpaBusline.Level level,
            @RequestParam(value = "category", required = false) JpaBus.Category category,
            @RequestParam(value = "lineId", required = false) Integer lineId,
            @RequestParam(value = "modelId", required = false) Integer modelId,
            @RequestParam(value = "companyId", required = false) Integer companyId
    ) {
        return busService.getBusinessCompanies(city, level, category, lineId, modelId, companyId);
    }
}
