package com.pantuo.simulate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.pantuo.dao.pojo.JpaBusline;
import com.pantuo.mybatis.domain.BusLock;
import com.pantuo.mybatis.domain.BusLockExample;
import com.pantuo.mybatis.domain.BusinessCompany;
import com.pantuo.mybatis.domain.BusinessCompanyExample;
import com.pantuo.mybatis.persistence.BusLockMapper;
import com.pantuo.mybatis.persistence.BusSelectMapper;
import com.pantuo.mybatis.persistence.BusinessCompanyMapper;
import com.pantuo.mybatis.persistence.UserAutoCompleteMapper;
import com.pantuo.vo.LineBatchUpdateView;
import com.pantuo.vo.ModelCountView;
import com.pantuo.web.view.CountMonthView;
import com.pantuo.web.view.LineDateCount;

/**
 * 
 * <b><code>CountMonth</code></b>
 * <p>
 * 月发布统计报表
 * </p>
 * <b>Creation Time:</b> 2015年11月6日 下午3:48:08
 * @author xiaoli
 * @since pantuo 1.0-SNAPSHOT
 */
@Component
public class CountMonth implements Runnable ,ScheduleStatsInter{
	private static Logger log = LoggerFactory.getLogger(CountMonth.class);

	public static CountMonthView _emptyCount = new CountMonthView();
	public static Map<Integer, CountMonthView> map = new java.util.concurrent.ConcurrentHashMap<Integer, CountMonthView>();
	public static Date today=new Date();
	@Autowired
	BusinessCompanyMapper companyMapper;
	@Autowired
	UserAutoCompleteMapper autoCompleteMapper;

	//@Scheduled(fixedRate = 5000)
	@Scheduled(cron = "0/50 * * * * ?")
	public void work() {
		if (statControl.isRunning) {
			try {
				act(today);
			} finally {
				statControl.runover();
			}
		}
		
	}
	
	
      public Map<Integer, Integer>  list2map(List<ModelCountView> list){
    	     if(list.isEmpty()){
    		  return null;
    	     }
    	    Map<Integer, Integer> map=new HashMap<Integer, Integer>();
    		for (ModelCountView modelCountView : list) {
    			map.put(modelCountView.getLeval(), modelCountView.getBusnum());
			}
    		return map;
      }
      public Map<Integer, Integer>  list2map2(List<ModelCountView> list){
    	  if(list.isEmpty()){
    		  return null;
    	     }
    	  Map<Integer, Integer> map=new HashMap<Integer, Integer>();
    		  for (ModelCountView modelCountView : list) {
    			  map.put(modelCountView.getDays(), modelCountView.getBusnum());
    		  }
    		  return map;
      }
	public void act(Date date) {
		String monthstr=new SimpleDateFormat("yyyy-MM-dd").format(date);
		BusinessCompanyExample example = new BusinessCompanyExample();
		List<BusinessCompany> companylist = companyMapper.selectByExample(example);
        for (BusinessCompany businessCompany : companylist) {
        	if(!map.containsKey(businessCompany.getId())){
        		map.put(businessCompany.getId(), new CountMonthView());
        		map.get(businessCompany.getId()).setCompanyName(businessCompany.getName());
        	}
		}
        for(Map.Entry<Integer, CountMonthView> onemap:map.entrySet()){
        	CountMonthView view=onemap.getValue();
        	view.setContractNum(autoCompleteMapper.getContactCountBycomid(onemap.getKey(),monthstr));
        	Map<Integer, Integer> levalMap=list2map(autoCompleteMapper.getlevelbusnumBycomid(onemap.getKey(),monthstr));
        	Map<Integer, Integer> dasyMap=list2map2(autoCompleteMapper.getdaysbusnumBycomid(onemap.getKey(),monthstr));
        	view.setNor_Snum(levalMap==null?0:(levalMap.containsKey(JpaBusline.Level.S.ordinal())?levalMap.get(JpaBusline.Level.S.ordinal()):0));
        	view.setNor_APPnum(levalMap==null?0:(levalMap.containsKey(JpaBusline.Level.APP.ordinal())?levalMap.get(JpaBusline.Level.APP.ordinal()):0));
        	view.setNor_APnum(levalMap==null?0:(levalMap.containsKey(JpaBusline.Level.AP.ordinal())?levalMap.get(JpaBusline.Level.AP.ordinal()):0));
        	view.setNor_Anum(levalMap==null?0:(levalMap.containsKey(JpaBusline.Level.A.ordinal())?levalMap.get(JpaBusline.Level.A.ordinal()):0));
        	view.setNor_xiaoji(view.getNor_Anum()+view.getNor_APnum()+view.getNor_APPnum()+view.getNor_Snum());
        	
        	view.setXu_Anum(0);
        	view.setXu_APnum(0);
        	view.setXu_APPnum(0);
        	view.setXu_Snum(0);
        	view.setXu_xiaoji(0);
        	
        	view.setOne_num(dasyMap==null?0:(dasyMap.containsKey(30)?dasyMap.get(30):0));
        	view.setThree_num(dasyMap==null?0:(dasyMap.containsKey(90)?dasyMap.get(90):0));
        	view.setSix_num(dasyMap==null?0:(dasyMap.containsKey(180)?dasyMap.get(180):0));
        	view.setTwelve_num(dasyMap==null?0:(dasyMap.containsKey(360)?dasyMap.get(360):0));
        	view.setDays_xiaoji(view.getNor_xiaoji());
        	view.setOther_num(view.getDays_xiaoji()-view.getOne_num()-view.getThree_num()-view.getSix_num()-view.getTwelve_num());
        }
        
        log.info("月发布统计："+map);
	}

	@Override
	public void run() {
		work();
	}

	public CountMonthView getCountMonthView(Integer companyid) {
		return map.containsKey(companyid) ? map.get(companyid) : _emptyCount;
	}
	public StatsMonitor statControl = new StatsMonitor(this);
	@Override
	public StatsMonitor getStatsMonitor() {
		return statControl;
	}

}
