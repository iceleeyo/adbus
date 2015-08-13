package com.pantuo.service;

import java.security.Principal;
import java.text.ParseException;
import java.util.List;

import org.springframework.data.domain.Page;

import com.pantuo.dao.pojo.JpaBodyContract;
import com.pantuo.dao.pojo.JpaBus;
import com.pantuo.dao.pojo.JpaBusLock;
import com.pantuo.mybatis.domain.Bodycontract;
import com.pantuo.mybatis.domain.BusLock;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.ActivitiService.TaskQueryType;
import com.pantuo.util.Pair;
import com.pantuo.vo.GroupVo;
import com.pantuo.web.view.AutoCompleteView;
import com.pantuo.web.view.OrderView;

public interface BusLineCheckService {
	/**
	 * 
	 * 查某段时间线路可上刑期的库存 
	 * 
	 * 车辆总数-已上刑的上刑数量
	 *
	 * @param lineId
	 * @param category
	 * @param start
	 * @param end
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public int countByFreeCars(int lineId,Integer  modelId, JpaBus.Category category, String start, String end);

	/**
	 * 
	 * 搜寻线路
	 *
	 * @param name
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public List<AutoCompleteView> autoCompleteByName(int city, String name,JpaBus.Category category);
	/**
	 * 
	 * 查线路 类型
	 *
	 * @param lineId
	 * @param category
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public List<GroupVo> countCarTypeByLine(int lineId, JpaBus.Category category);

	public List<JpaBusLock> getBusLockListBySeriNum(long seriaNum);

	public Pair<Boolean, String> saveBusLock(BusLock buslock, String startD, String endD)throws ParseException ;

	public boolean removeBusLock(Principal principal, int city, long seriaNum, int id);

	public Pair<Boolean, String> saveBodyContract(Bodycontract bodycontract, long seriaNum, String userId);
	
	
	
	public Page<OrderView> queryOrders(int city, Principal principal, TableRequest req,TaskQueryType tqType);
	
	
	
	
	public JpaBodyContract selectBcById(int id);

	public Page<OrderView> getBodyContractList(int city, TableRequest req, Principal principal);

}
