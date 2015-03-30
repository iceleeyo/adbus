package com.pantuo.service;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pantuo.mybatis.domain.Contract;
import com.pantuo.mybatis.domain.ContractExample;
import com.pantuo.mybatis.persistence.ContractMapper;

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
    
    @Transactional
    public int saveContract(Contract con) {
    		con.setContractNum((long) 12311);
    		con.setIsUpload(0);
    		con.setCreateTime(new Date());
    		con.setStats("unstart");
    	    return contractMapper.insert(con);
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
