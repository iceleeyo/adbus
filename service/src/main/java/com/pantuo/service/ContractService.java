package com.pantuo.service;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mysema.query.types.expr.BooleanExpression;
import com.pantuo.dao.BlackAdRepository;
import com.pantuo.dao.ContractRepository;
import com.pantuo.dao.pojo.JpaAttachment;
import com.pantuo.dao.pojo.JpaBlackAd;
import com.pantuo.dao.pojo.JpaContract;
import com.pantuo.dao.pojo.QJpaContract;
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
import com.pantuo.mybatis.domain.Industry;
import com.pantuo.mybatis.domain.Orders;
import com.pantuo.mybatis.domain.OrdersExample;
import com.pantuo.mybatis.persistence.AttachmentMapper;
import com.pantuo.mybatis.persistence.BlackAdMapper;
import com.pantuo.mybatis.persistence.ContractIdMapper;
import com.pantuo.mybatis.persistence.ContractMapper;
import com.pantuo.mybatis.persistence.CpdProductMapper;
import com.pantuo.mybatis.persistence.IndustryMapper;
import com.pantuo.mybatis.persistence.OrdersMapper;
import com.pantuo.service.ActivitiService.SystemRoles;
import com.pantuo.service.security.Request;
import com.pantuo.util.BusinessException;
import com.pantuo.util.DateUtil;
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
	@Autowired
	ContractRepository contractRepository;

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
        if (Request.hasOnlyAuth(principal, SystemRoles.advertiser.name()) ) {
        	ca.andUserIdEqualTo(Request.getUserId(principal));
        }
		if (StringUtils.isNoneBlank(name)) {
			ca.andContractNameLike("%" + name + "%");
		}
		if (StringUtils.isNoneBlank(code)) {
			ca.andContractCodeEqualTo(code);
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

	public List<ContractView> getContractDetail(int contract_id, Principal principal) {
		List<ContractView> list=new ArrayList<ContractView>();
		BooleanExpression query=QJpaContract.jpaContract.id.eq(contract_id);
		JpaContract j=contractRepository.findOne(query);
		if(j.getParentid()!=0){
			JpaContract j2=contractRepository.findOne(j.getParentid());
			ContractView v = new ContractView();
			List<Attachment> files = attachmentService.queryContracF(principal, j2.getId());
			v.setFiles(files);
			v.setJpaContract(j2);
			list.add(v);
			v = new ContractView();
			 files = attachmentService.queryContracF(principal, j.getId());
			v.setFiles(files);
			v.setJpaContract(j);
			list.add(v);
		}else{
		query=query.or(QJpaContract.jpaContract.parentid.eq(contract_id));
			List<JpaContract> cons=	(List<JpaContract>) contractRepository.findAll(query);
			for (JpaContract jpaContract : cons) {
				ContractView v = new ContractView();
				List<Attachment> files = attachmentService.queryContracF(principal, jpaContract.getId());
				v.setFiles(files);
				v.setJpaContract(jpaContract);
				list.add(v);
			}
		}
		return list;
	}

	public ContractView findContractById(int contract_id, Principal principal) {
		   ContractView v = new ContractView();
		   JpaContract con=contractRepository.findOne(contract_id);
			v.setJpaContract(con);
			List<Attachment> files = attachmentService.queryContracF(principal,contract_id);
			v.setFiles(files);
			return v;
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


	public Contract selectContractById(int contractId) {
		return contractMapper.selectByPrimaryKey(contractId);
	}
	
	@Autowired
	CpdProductMapper cpdProductMapper;
	
	public String getContractId() {
		int contractId = 1;
		String date = new SimpleDateFormat("yy-MM-dd").format(System.currentTimeMillis());

		if (DataInitializationService.CONTRACT_ID_MAP.containsKey(date)) {
			cpdProductMapper.updateContractId(date);
			contractId = DataInitializationService.CONTRACT_ID_MAP.get(date).incrementAndGet();
			StringBuilder sb = new StringBuilder();
			sb.append("WBM（XS）-");
			sb.append(date);
			sb.append("-");
			sb.append(contractId < 10 ? "0" + contractId : contractId);
			return sb.toString();
		} else {
			return "Fetch ContractId Error";
		}

	}

	public Pair<Boolean, String> saveContract(String startDate1, String endDate1, String signDate1,
			JpaContract contract, String userId, int city, HttpServletRequest request) {
		Pair<Boolean, String> r = null;
			try {
				contract.setStartDate(DateUtil.longDf.get().parse(startDate1));
				contract.setEndDate(DateUtil.longDf.get().parse(endDate1));
				contract.setSignDate(DateUtil.longDf.get().parse(signDate1));
				contract.setCity(city);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if(contract.getId()>0){
				if(contractRepository.save(contract)!=null){
					return new Pair<Boolean, String>(true, "合同修改成功");
				}else{
					return new Pair<Boolean, String>(false, "合同修改失败");
				}
			}
			contract.setUpload(false);
			contract.setCreated(new Date());
			contract.setStats(JpaContract.Status.not_started);
			contract.setCreator(userId);
			com.pantuo.util.BeanUtils.filterXss(contract);
			if (contractRepository.save(contract)!=null) {
				try {
					attachmentService.saveAttachment(request, userId, contract.getId(), JpaAttachment.Type.ht_fj,null);
				} catch (BusinessException e) {
					e.printStackTrace();
				}
				r = new Pair<Boolean, String>(true, "合同创建成功！");
			}
		return r;
	}
}
