package com.pantuo.service;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import com.pantuo.dao.pojo.JpaAttachment;
import com.pantuo.dao.pojo.JpaContract;
import com.pantuo.dao.pojo.JpaProduct;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.pantuo.mybatis.domain.Attachment;
import com.pantuo.mybatis.domain.Contract;
import com.pantuo.mybatis.domain.ContractExample;
import com.pantuo.mybatis.domain.Industry;
import com.pantuo.mybatis.persistence.ContractMapper;
import com.pantuo.mybatis.persistence.IndustryMapper;
import com.pantuo.service.ActivitiService.SystemRoles;
import com.pantuo.util.BusinessException;
import com.pantuo.util.NumberPageUtil;
import com.pantuo.util.Pair;
import com.pantuo.util.Request;
import com.pantuo.web.view.ContractView;
import com.pantuo.web.view.SuppliesView;

/**
 * @author xl
 */
@Service
public class ContractService {
	private static Logger log = LoggerFactory.getLogger(ContractService.class);

	@Autowired
	private IdentityService identityService;
	@Autowired
	ContractMapper contractMapper;
	@Autowired
	IndustryMapper industryMapper;
	@Autowired
	private ManagementService managementService;
	@Autowired
	AttachmentService attachmentService;
	@Autowired
	UserServiceInter userService;

	public Pair<Boolean, String> saveContract(int city, Contract con, String username, HttpServletRequest request) {
		con.setCity(city);
		Pair<Boolean, String> r = null;
		try {
			con.setIsUpload(false);
			con.setCreated(new Date());
			con.setStats(JpaContract.Status.not_started.ordinal());
			if (!userService.isUserHaveGroup(con.getUserId(), SystemRoles.advertiser.name())) {
				return new Pair<Boolean, String>(true, con.getUserId() + " 不是广告主,创建合同失败！");

			}
			con.setCreator(username);
			//			System.out.println(JpaContract.Status.not_started.ordinal());
			//			con.setStats(JpaContract.Status.not_started.ordinal());
			com.pantuo.util.BeanUtils.filterXss(con);
			int dbId = contractMapper.insert(con);
			if (dbId > 0) {
				attachmentService.saveAttachment(request, username, con.getId(), JpaAttachment.Type.ht_pic,null);
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
	

}
