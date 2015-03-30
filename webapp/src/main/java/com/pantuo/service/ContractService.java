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
import com.pantuo.mybatis.persistence.ContractMapper;
import com.pantuo.util.BusinessException;
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
    
    @Transactional
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
    		

    @Transactional
    public int deleteContract(Integer id) {
    	return contractMapper.deleteByPrimaryKey(id);
    }

    @Transactional
    public int updateContract(Contract con){
    	return contractMapper.updateByPrimaryKey(con);
    }
    @Transactional
    public List<Contract> findContracts() {
    	ContractExample example=new ContractExample();
    	ContractExample.Criteria criteria=example.createCriteria();
       return contractMapper.selectByExample(example);
    }
}
