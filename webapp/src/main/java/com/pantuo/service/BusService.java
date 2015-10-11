package com.pantuo.service;

import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;

import com.pantuo.dao.pojo.JpaBus;
import com.pantuo.dao.pojo.JpaBusModel;
import com.pantuo.dao.pojo.JpaBusOnline;
import com.pantuo.dao.pojo.JpaBusUpLog;
import com.pantuo.dao.pojo.JpaBusinessCompany;
import com.pantuo.dao.pojo.JpaBusline;
import com.pantuo.mybatis.domain.Bus;
import com.pantuo.mybatis.domain.BusOnline;
import com.pantuo.mybatis.domain.CountableBusLine;
import com.pantuo.mybatis.domain.CountableBusModel;
import com.pantuo.mybatis.domain.CountableBusinessCompany;
import com.pantuo.pojo.DataTablePage;
import com.pantuo.pojo.TableRequest;
import com.pantuo.util.Pair;
import com.pantuo.web.view.BusInfoView;
import com.pantuo.web.view.ContractLineDayInfo;

public interface BusService {

    long count();

    long countFree(int city, JpaBusline.Level level, JpaBus.Category category, Integer lineId, Integer busModelId, Integer companyId);

    Page<JpaBus> getAllBuses(int city, TableRequest req, int page, int pageSize, Sort sort, boolean fetchDisabled);
    
    public void exportBusExcel(TableRequest req, Page<JpaBus> busList, HttpServletResponse resp);
    
    
    public DataTablePage<BusInfoView> getMybatisAllBuses(int city, TableRequest req, int page, int pageSize, Sort sort,
			boolean fetchDisabled) ;

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

	Pair<Boolean, String> saveBus(Bus bus, int cityId, Principal principal, HttpServletRequest request) throws JsonGenerationException, JsonMappingException, IOException;

	Page<JpaBusUpLog> getbusUphistory(int cityId, TableRequest req, int page, int length, Sort sort);

	public Page<BusInfoView> queryBusinfoView2(TableRequest req, Page<JpaBusUpLog> page) throws JsonParseException, JsonMappingException, IOException;
	
	
	ContractLineDayInfo getContractBusLineTodayInfo(int publish_line_id);
	
	
	Pair<Boolean, String> checkFree(  String stday, int days,    int city,int publish_line_id);

	Pair<Boolean, String> batchOffline(String ids, String offday, Principal principal, int city)  throws ParseException;

	Page<JpaBusOnline> getbusOnlineList(int cityId, TableRequest req, int page, int length, Sort sort) ;
	
	public long getMoneyFromBusModel(JpaBusline.Level level,boolean doubleDecker);
	
}
