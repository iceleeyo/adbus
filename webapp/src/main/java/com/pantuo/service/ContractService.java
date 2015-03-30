package com.pantuo.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pantuo.mybatis.domain.Contract;
import com.pantuo.mybatis.domain.ContractExample;
import com.pantuo.mybatis.domain.Supplies;
import com.pantuo.mybatis.domain.SuppliesExample;
import com.pantuo.mybatis.persistence.ContractMapper;
import com.pantuo.util.BusinessException;
import com.pantuo.util.NumberPageUtil;
import com.pantuo.util.Pair;

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
    private ManagementService managementService;
	@Autowired
	AttachmentService attachmentService;
    
    public Pair<Boolean, String> saveContract(Contract con,HttpServletRequest request) {
    	Pair<Boolean, String> r = null;
		try {
    		con.setIsUpload(0);
    		con.setCreateTime(new Date());
    		con.setStats("unstart");
			int dbId = contractMapper.insert(con);
			if (dbId > 0) {
				attachmentService.saveAttachment(request, "pxh", con.getId(), "ht_pic");
					r = new Pair<Boolean, String>(true, "合同创建成功！");
			}
		} catch (BusinessException e) {
			r = new Pair<Boolean, String>(false, "合同创建失败");
		}
		return r;
	}
    public int deleteContract(Integer id) {
    	return contractMapper.deleteByPrimaryKey(id);
    }
    public int updateContract(Contract con){
    	ContractExample example=new ContractExample();
    	ContractExample.Criteria criteria=example.createCriteria();
    	criteria.andIdEqualTo(con.getId());
    	con.setIsUpload(1);
    	return contractMapper.updateByExample(con, example);
    }
    public int countMyList(String name,String code, HttpServletRequest request) {
		return contractMapper.countByExample(getExample(name, code));
	}
    public ContractExample getExample(String name, String code) {
    	ContractExample example = new ContractExample();
    	ContractExample.Criteria ca = example.createCriteria();
		if (StringUtils.isNoneBlank(name)) {
			ca.andContractNameLike("%" + name + "%");
		}
		if (StringUtils.isNoneBlank(code)&&Long.parseLong(code)>0) {
			ca.andContractNumEqualTo(Long.parseLong(code));
		}
		return example;
	}
    public List<Contract> queryContractList(NumberPageUtil page, String name, String code, HttpServletRequest request) {
    	ContractExample ex = getExample(name, code);
		ex.setOrderByClause("create_time desc");
		ex.setLimitStart(page.getLimitStart());
		ex.setLimitEnd(page.getPagesize());
		return contractMapper.selectByExample(ex);
	}
}
