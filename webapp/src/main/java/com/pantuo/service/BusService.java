package com.pantuo.service;

import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import com.pantuo.dao.pojo.JpaBus;
import com.pantuo.dao.pojo.JpaBusModel;
import com.pantuo.dao.pojo.JpaBusOnline;
import com.pantuo.dao.pojo.JpaBusUpLog;
import com.pantuo.dao.pojo.JpaBusinessCompany;
import com.pantuo.dao.pojo.JpaBusline;
import com.pantuo.dao.pojo.JpaLineUpLog;
import com.pantuo.dao.pojo.JpaPublishLine;
import com.pantuo.mybatis.domain.Bus;
import com.pantuo.mybatis.domain.BusOnline;
import com.pantuo.mybatis.domain.CountableBusLine;
import com.pantuo.mybatis.domain.CountableBusModel;
import com.pantuo.mybatis.domain.CountableBusinessCompany;
import com.pantuo.mybatis.domain.Modeldesc;
import com.pantuo.pojo.DataTablePage;
import com.pantuo.pojo.TableRequest;
import com.pantuo.util.Pair;
import com.pantuo.vo.CountView;
import com.pantuo.vo.ModelCountView;
import com.pantuo.web.view.AdjustLogView;
import com.pantuo.web.view.BusInfoView;
import com.pantuo.web.view.BusModelGroupView;
import com.pantuo.web.view.CarUseView;
import com.pantuo.web.view.ContractLineDayInfo;
import com.pantuo.web.view.PulishLineView;

public interface BusService {

    long count();

    long countFree(int city, JpaBusline.Level level, JpaBus.Category category, Integer lineId, Integer busModelId, Integer companyId);
    public DataTablePage<BusInfoView> getAllBusesForContract(int city, TableRequest req, int page, int pageSize,
			Sort sort, boolean fetchDisabled) ;
    Page<JpaBus> getAllBuses(int city, TableRequest req, int page, int pageSize, Sort sort, boolean fetchDisabled);
    
    public void exportBusExcel(TableRequest req, Page<JpaBus> busList, HttpServletResponse resp);
    
    
    public DataTablePage<BusInfoView> getMybatisAllBuses(int city, TableRequest req, int page, int pageSize, Sort sort,
			boolean fetchDisabled) ;
    
    
    public DataTablePage<AdjustLogView> getAdJustLog(int city, TableRequest req, int page, int pageSize, Sort sort
 			) ;

    JpaBus findById(int id);

    void saveBus(JpaBus bus);

    void saveBuses(Iterable<JpaBus> buses);

    Page<JpaBusline> getAllBuslines(int city, JpaBusline.Level level, String name, int page, int pageSize, Sort sort);
    
    /**
     * 
     * 空媒体统计
     *
     * @param city
     * @param req
     * @return
     * @since pantuo 1.0-SNAPSHOT
     */
    Page<CarUseView> getLinesUse(int city, TableRequest req);

    Page<JpaBusModel> getAllBusModels(int city, TableRequest req, String name, String manufacturer, int page, int pageSize, Sort sort);

    Page<JpaBusinessCompany> getAllBusinessCompanies(int city, String name, String contact, int page, int pageSize, Sort sort);

    Iterable<CountableBusLine> getBuslines(int city, JpaBusline.Level level, JpaBus.Category category, Integer lineId, Integer busModelId, Integer companyId);

    Iterable<CountableBusModel> getBusModels(int city, JpaBusline.Level level, JpaBus.Category category, Integer lineId, Integer busModelId, Integer companyId);

    Iterable<CountableBusinessCompany> getBusinessCompanies(int city, JpaBusline.Level level, JpaBus.Category category, Integer lineId, Integer busModelId, Integer companyId);
    public Page<BusInfoView> queryBusinfoView(TableRequest req, Page<JpaBus> page);
    
    public  Collection<BusModelGroupView> queryModelGroup4Contract(int city, TableRequest req, boolean fetchDisabled);
    public Collection<BusModelGroupView> queryModelGroup(TableRequest req, Page<JpaBus> page);

	Pair<Boolean, String> batchOnline(String ids, String stday, int days, int contractid, Principal principal, int city,int plid, int fday, String adtype, String print, String sktype)throws ParseException; 

	Page<JpaBusOnline> getbusOnlinehistory(int cityId, TableRequest req, int page, int length, Sort sort);
	
	
	
	
	public BusOnline offlineBusContract(int cityId,int id,int publishLineId , Principal principal);
	
	List<JpaBusinessCompany> getAllCompany(int cityId);
	/**
	 * 
	 * 调车.
	 *
	 * @param bus
	 * @param cityId
	 * @param principal
	 * @param request
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	Pair<Boolean, String> changeLine(String busIds, int newLineId, int cityId, Principal principal, HttpServletRequest request);

	Pair<Boolean, String> saveBus(Bus bus, String updated1, int cityId, Principal principal, HttpServletRequest request) throws JsonGenerationException, JsonMappingException, IOException,ParseException;

	Page<JpaBusUpLog> getbusUphistory(int cityId, TableRequest req, int page, int length, Sort sort);

	public Page<BusInfoView> queryBusinfoView2(TableRequest req, Page<JpaBusUpLog> page) throws JsonParseException, JsonMappingException, IOException;
	public Page<BusInfoView> queryBusinfoView3(TableRequest req, Page<JpaLineUpLog> page) throws JsonParseException, JsonMappingException, IOException;
	
	
	ContractLineDayInfo getContractBusLineTodayInfo(int publish_line_id);
	
	
	Pair<Boolean, String> checkFree(  String stday, int days,    int city,int publish_line_id);

	Pair<Boolean, String> batchOffline(String ids, String offday, Principal principal, int city)  throws ParseException;

	Page<JpaBusOnline> getbusOnlineList(int cityId, TableRequest req, int page, int length, Sort sort)throws ParseException ;
	
	public long getMoneyFromBusModel(JpaBusline.Level level,boolean doubleDecker);

	Page<JpaLineUpLog> getlineUphistory(int cityId, TableRequest req, int page, int length,Sort sort);
	
	
	DataTablePage<PulishLineView> queryOrders(int cityId, TableRequest req, int page, int length,Sort sort);

	Pair<Boolean, String> changeDate(String ids, String sday, int days, String eday, Principal principal, int city) throws ParseException ;

	CountView ModelCountlist(TableRequest req, Page<JpaPublishLine> jpabuspage);

	List<Modeldesc> findModedesc(String type);
	
}
