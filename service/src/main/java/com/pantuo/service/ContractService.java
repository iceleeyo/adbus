package com.pantuo.service;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pantuo.dao.BlackAdRepository;
import com.pantuo.dao.pojo.JpaAttachment;
import com.pantuo.dao.pojo.JpaBlackAd;
import com.pantuo.dao.pojo.JpaContract;
import com.pantuo.mybatis.domain.Attachment;
import com.pantuo.mybatis.domain.AttachmentExample;
import com.pantuo.mybatis.domain.BlackAd;
import com.pantuo.mybatis.domain.BlackAdExample;
import com.pantuo.mybatis.domain.Bus;
import com.pantuo.mybatis.domain.BusContract;
import com.pantuo.mybatis.domain.BusContractExample;
import com.pantuo.mybatis.domain.BusExample;
import com.pantuo.mybatis.domain.Contract;
import com.pantuo.mybatis.domain.ContractExample;
import com.pantuo.mybatis.domain.ContractId;
import com.pantuo.mybatis.domain.ContractIdExample;
import com.pantuo.mybatis.domain.Industry;
import com.pantuo.mybatis.domain.Orders;
import com.pantuo.mybatis.domain.OrdersExample;
import com.pantuo.mybatis.persistence.AttachmentMapper;
import com.pantuo.mybatis.persistence.BlackAdMapper;
import com.pantuo.mybatis.persistence.BusContractMapper;
import com.pantuo.mybatis.persistence.BusMapper;
import com.pantuo.mybatis.persistence.ContractIdMapper;
import com.pantuo.mybatis.persistence.ContractMapper;
import com.pantuo.mybatis.persistence.IndustryMapper;
import com.pantuo.mybatis.persistence.OrdersMapper;
import com.pantuo.service.security.Request;
import com.pantuo.util.BusinessException;
import com.pantuo.util.NumberPageUtil;
import com.pantuo.util.Pair;
import com.pantuo.web.view.ContractView;

/**
 * @author xl
 */
@Service
public class ContractService {
	private static Logger log = LoggerFactory.getLogger(ContractService.class);

	@Autowired
	private IdentityService identityService;
	@Autowired
	ContractIdMapper   contractIdMapper;
	@Autowired
	ContractMapper contractMapper;
	@Autowired
	OrdersMapper ordersMapper;
	@Autowired
	BusMapper busMapper;
	@Autowired
	BusContractMapper busContractMapper;
	@Autowired
	AttachmentMapper attachmentMapper;
	@Autowired
	IndustryMapper industryMapper;
	@Autowired
	private ManagementService managementService;
	@Autowired
	AttachmentService attachmentService;
	@Autowired
	UserServiceInter userService;
	@Autowired
	BlackAdRepository  blackAdRepository;
	@Autowired
	BlackAdMapper blackAdMapper;
	public Pair<Boolean, String> saveContract(int city, Contract con, String username, HttpServletRequest request) {
		Pair<Boolean, String> r = null;
		try {
//			if(StringUtils.isNotBlank(con.getUserId())){
//				if (!userService.isUserHaveGroup(con.getUserId(), SystemRoles.advertiser.name())) {
//					return new Pair<Boolean, String>(false, con.getUserId() + " 不是广告主,保存失败！");
//				}
//			}
			if(null!=con.getId() && con.getId()>0){
				Contract contract=contractMapper.selectByPrimaryKey(con.getId());
				contract.setUpdated(new Date());
				contract.setContractCode(con.getContractCode());
				contract.setAmounts(con.getAmounts());
				contract.setContractName(con.getContractName());
				contract.setContractType(con.getContractType());
				contract.setStartDate(con.getStartDate());
				contract.setEndDate(con.getEndDate());
				contract.setIndustryId(con.getIndustryId());
				contract.setRemark(con.getRemark());
				contract.setUserId(con.getUserId());
				int a=contractMapper.updateByPrimaryKey(contract);
				if(a>0){
					return new Pair<Boolean, String>(true, "合同修改成功");
				}else{
					return new Pair<Boolean, String>(false, "合同修改失败");
				}
			}
			con.setCity(city);
			con.setIsUpload(false);
			con.setCreated(new Date());
			con.setStats(JpaContract.Status.not_started.ordinal());
//			if (!userService.isUserHaveGroup(con.getUserId(), SystemRoles.advertiser.name())) {
//				return new Pair<Boolean, String>(true, con.getUserId() + " 不是广告主,创建合同失败！");
//
//			}
			con.setCreator(username);
			//			System.out.println(JpaContract.Status.not_started.ordinal());
			//			con.setStats(JpaContract.Status.not_started.ordinal());
			com.pantuo.util.BeanUtils.filterXss(con);
			int dbId = contractMapper.insert(con);
			if (dbId > 0) {
				attachmentService.saveAttachment(request, username, con.getId(), JpaAttachment.Type.ht_fj,null);
				r = new Pair<Boolean, String>(true, "合同创建成功！");
			}
		} catch (BusinessException e) {
			r = new Pair<Boolean, String>(false, "合同创建失败");
		}
		return r;
	}

	/**
	 * 
	 * 检查合同号是否存在
	 *
	 * @param contract_code
	 * @return
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	public Contract queryContractByCode(String contract_code) {
		ContractExample example = new ContractExample();
		ContractExample.Criteria criteria = example.createCriteria();
		criteria.andContractCodeEqualTo(contract_code);
		criteria.andStatsEqualTo(JpaContract.Status.starting.ordinal());
		List<Contract> list = contractMapper.selectByExample(example);
		return list.isEmpty() ? null : list.get(0);
	}

	public Pair<Boolean, String> queryCode(String contract_code) {
		Contract t = queryContractByCode(contract_code);
		return ObjectUtils.equals(t, null) ? new Pair<Boolean, String>(false, "系统查不到相应的合同号！")
				: new Pair<Boolean, String>(true, StringUtils.EMPTY);
	}
	public Pair<Boolean, String> delContract(int contract_id) {
		    OrdersExample example=new OrdersExample();
		    OrdersExample.Criteria criteria=example.createCriteria();
		    criteria.andContractIdEqualTo(contract_id);
		     List<Orders> orders= ordersMapper.selectByExample(example);
		     if(orders.size()>0){
		    	 return	new Pair<Boolean, String>(true, "该合同已绑定订单，不能删除！"); 
		     }
			int a=contractMapper.deleteByPrimaryKey(contract_id);
			if(a>0){
				AttachmentExample example2=new AttachmentExample();
				AttachmentExample.Criteria criteria2=example2.createCriteria();
			    criteria2.andMainIdEqualTo(contract_id);
			    criteria2.andTypeEqualTo(1);
			    List<Attachment> attas=attachmentMapper.selectByExample(example2);
			    for (Attachment attachment : attas) {
			    	if(attachment!=null){
			    		attachmentMapper.deleteByPrimaryKey(attachment.getId());
			    	}
				}
		    	return	new Pair<Boolean, String>(true, "删除合同成功！");
			}
			return	new Pair<Boolean, String>(true, "删除合同失败！");
	}

	public int deleteContract(Integer id) {
		return contractMapper.deleteByPrimaryKey(id);
	}

	public int updateContract(int city, Contract con) {
        if (con.getCity() == null) {
            con.setCity(city);
        } else if (city != con.getCity()) {
            return 0;
        }
		ContractExample example = new ContractExample();
		ContractExample.Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(con.getId());
		con.setIsUpload(true);
		return contractMapper.updateByExample(con, example);
	}

	public int countMyList(int city, String name, String code, Principal principal) {
		return contractMapper.countByExample(getExample(city, name, code, principal));
	}

	public ContractExample getExample(int city, String name, String code, Principal principal) {
		ContractExample example = new ContractExample();
		ContractExample.Criteria ca = example.createCriteria();
        ca.andCityEqualTo(city);
		if (StringUtils.isNoneBlank(name)) {
			ca.andContractNameLike("%" + name + "%");
		}
		if (StringUtils.isNoneBlank(code)) {
			ca.andContractCodeEqualTo(code);
		}
        if (principal != null) {
            ca.andUserIdEqualTo(Request.getUserId(principal));
        }
		return example;
	}

	public List<Contract> queryContractList(int city, NumberPageUtil page, String name, String code, Principal principal) {
		ContractExample ex = getExample(city, name, code, principal);
		ex.setOrderByClause("created desc");
		ex.setLimitStart(page.getLimitStart());
		ex.setLimitEnd(page.getPagesize());
		return contractMapper.selectByExample(ex);
	}
	public List<Contract> queryParentContractList(int city) {
		ContractExample example = new ContractExample();
		ContractExample.Criteria ca = example.createCriteria();
        ca.andCityEqualTo(city);
        ca.andParentidEqualTo(0);
		return contractMapper.selectByExample(example);
	}

	public ContractView getContractDetail(int contract_id, Principal principal) {
		ContractView v = null;
		Contract con = contractMapper.selectByPrimaryKey(contract_id);
		if (con != null) {
			v = new ContractView();
			if(con.getIndustryId()!=null){
				Industry industry=industryMapper.selectByPrimaryKey(con.getIndustryId());
				if(industry!=null&&industry.getName()!=null){
					v.setIndustryname(industry.getName());
				}
				}
			List<Attachment> files = attachmentService.queryContracF(principal, contract_id);
			v.setFiles(files);
			v.setMainView(con);
		}
		return v;
	}

	public ContractView findContractById(int contract_id, Principal principal) {
		ContractView v = new ContractView();
		ContractExample example=new ContractExample();
		ContractExample.Criteria criteria=example.createCriteria();
		criteria.andIdEqualTo(contract_id);
		List<Contract> invoices=contractMapper.selectByExample(example);
		if(invoices.size()>0){
			v.setMainView(invoices.get(0));
			List<Attachment> files = attachmentService.queryContracF(principal, invoices.get(0).getId());
			v.setFiles(files);
			return v;
		}
		return null;
	}

	public Pair<Boolean, String> saveBlackAd(int city, JpaBlackAd blackAd, String userId, HttpServletRequest request) {
		blackAd.setCreaterUser(userId);
		blackAd.setMain_type(JpaBlackAd.Main_type.online);
		blackAdRepository.save(blackAd);
		return new Pair<Boolean, String>(true, "保存成功");
	}

	public List<BlackAd> queryAllBlackAd() {
		BlackAdExample example=new BlackAdExample();
		BlackAdExample.Criteria criteria=example.createCriteria();
		criteria.andMainTypeEqualTo(JpaBlackAd.Main_type.online.ordinal());
		example.setOrderByClause("sort_number desc");;
		return blackAdMapper.selectByExample(example);
	}

	public int saveIndustry(Industry industry) {
		industry.setEnabled(true);
		industry.setCreated(new Date());
		return industryMapper.insert(industry);
	}

	public List<Contract> querybodyContractList(int cityId) {
		ContractExample example=new ContractExample();
		ContractExample.Criteria criteria=example.createCriteria();
		criteria.andContractTypeEqualTo("车身广告");
		criteria.andCityEqualTo(cityId);
		return contractMapper.selectByExample(example);
	}
public Bus findBusByPlateNum(String plateNumber){
	BusExample example=new BusExample();
	BusExample.Criteria criteria=example.createCriteria();
	criteria.andPlateNumberEqualTo(plateNumber);
	List<Bus> list=busMapper.selectByExample(example);
	if(list.size()>0){
		return list.get(0);
	}
	return null;
	
}
	public Pair<Boolean, String> saveBusContract(int city, String plateNumber,int contractid,String startdate,String enddate) throws ParseException {
		Date starDate=(Date) new SimpleDateFormat("yyyy-MM-dd").parseObject(startdate);
		Date endDate=(Date) new SimpleDateFormat("yyyy-MM-dd").parseObject(enddate);
		String[] pn=plateNumber.split(",");
		for(int i=0;i<pn.length;i++){
			if(StringUtils.isNotBlank(pn[i])){
				Bus bus=findBusByPlateNum(pn[i]);
				if(bus!=null){
					if(checkEnable(bus.getId(),contractid,starDate,endDate)){
						return new Pair<Boolean, String>(false, "车牌号为'"+bus.getPlateNumber()+"'的车上下刊时间冲突，保存失败");
					}
					BusContract busContract=new BusContract();
					busContract.setEnable(true);
					busContract.setCity(city);
					busContract.setStartDate(starDate);
					busContract.setEndDate(endDate);
					busContract.setContractid(contractid);
					busContract.setBusid(bus.getId());
					busContractMapper.insert(busContract);
				}else{
					return new Pair<Boolean, String>(false, "车牌号'"+pn[i]+"'不存在");
				}
			}
		}
		return new Pair<Boolean, String>(true, "保存成功");
	}

	private boolean checkEnable(int busid, int contractid, Date starDate, Date endDate) {
		BusContractExample example=new BusContractExample();
		BusContractExample.Criteria criteria=example.createCriteria();
		BusContractExample.Criteria criteria2=example.createCriteria();
		BusContractExample.Criteria criteria3=example.createCriteria();
		criteria.andBusidEqualTo(busid);
		criteria.andContractidEqualTo(contractid);
		criteria.andStartDateLessThanOrEqualTo(starDate);
		criteria.andEndDateGreaterThanOrEqualTo(starDate);
		criteria2.andBusidEqualTo(busid);
		criteria2.andContractidEqualTo(contractid);
		criteria2.andStartDateLessThanOrEqualTo(endDate);
		criteria2.andEndDateGreaterThanOrEqualTo(endDate);
		criteria3.andBusidEqualTo(busid);
		criteria3.andContractidEqualTo(contractid);
		criteria3.andStartDateGreaterThanOrEqualTo(starDate);
		criteria3.andEndDateLessThanOrEqualTo(endDate);
		example.or(criteria2);
		if(busContractMapper.selectByExample(example).size()>0){
			return true;
		}
		return false;
	}

	public Contract selectContractById(int contractId) {
		return contractMapper.selectByPrimaryKey(contractId);
	}
	
	static Lock lock =new ReentrantLock();
	public String getContractId() {
		int r = 1;
		String date = new SimpleDateFormat("yy-MM-dd").format(System.currentTimeMillis());

		ContractIdExample example = new ContractIdExample();
		example.createCriteria().andDateObjEqualTo(date);
		List<ContractId> list = contractIdMapper.selectByExample(example);
		boolean isUpdate = false;
		if (list.size() == 0) {
			try {
				ContractId id = new ContractId();
				id.setCount(r);
				id.setDateObj(date);
				contractIdMapper.insert(id);
			} catch (Exception e) {
				if (StringUtils.contains(e.getMessage(), "duplicate")) {
					isUpdate = true;
				}
			}
		} else {
			isUpdate = true;
		}
		if (isUpdate) {
			try {
				lock.lock();
				ContractId c = list.get(0);
				int db = c.getCount() + 1;
				c.setCount(db);
				if (contractIdMapper.updateByPrimaryKey(c) > 0) {
					r = db;
				}
			} finally {
				lock.unlock();
			}
		}

		return "WBM（XS）-" + date + "-" + (r < 10 ? "0" + r : r);
	}
}
