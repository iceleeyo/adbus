package com.pantuo.service;

import com.pantuo.dao.pojo.*;
import com.pantuo.mybatis.domain.*;
import com.pantuo.pojo.TableRequest;
import com.pantuo.util.Pair;
import com.pantuo.web.view.BusInfoView;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.security.Principal;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface BusService {

    long count();

    long countFree(int city, JpaBusline.Level level, JpaBus.Category category, Integer lineId, Integer busModelId, Integer companyId);

    Page<JpaBus> getAllBuses(int city, TableRequest req, int page, int pageSize, Sort sort, boolean fetchDisabled);

    JpaBus findById(int id);

    void saveBus(JpaBus bus);

    void saveBuses(Iterable<JpaBus> buses);

    Page<JpaBusline> getAllBuslines(int city, JpaBusline.Level level, String name, int page, int pageSize, Sort sort);

    Page<JpaBusModel> getAllBusModels(int city, String name, String manufacturer, int page, int pageSize, Sort sort);

    Page<JpaBusinessCompany> getAllBusinessCompanies(int city, String name, String contact, int page, int pageSize, Sort sort);

    Iterable<CountableBusLine> getBuslines(int city, JpaBusline.Level level, JpaBus.Category category, Integer lineId, Integer busModelId, Integer companyId);

    Iterable<CountableBusModel> getBusModels(int city, JpaBusline.Level level, JpaBus.Category category, Integer lineId, Integer busModelId, Integer companyId);

    Iterable<CountableBusinessCompany> getBusinessCompanies(int city, JpaBusline.Level level, JpaBus.Category category, Integer lineId, Integer busModelId, Integer companyId);
    public Page<BusInfoView> queryBusinfoView(TableRequest req, Page<JpaBus> page);

	Pair<Boolean, String> batchOnline(String ids, String stday, int days, int contractid, Principal principal, int city,int plid)throws ParseException; 

	Page<JpaBusOnline> getbusOnlinehistory(int cityId, TableRequest req, int page, int length, Sort sort);
	
	
	
	
	public BusOnline offlineBusContract(int cityId,int id,int publishLineId , Principal principal);
	
	List<JpaBusinessCompany> getAllCompany(int cityId);
}
