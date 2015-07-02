package com.pantuo.service.impl;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mysema.query.types.expr.BooleanExpression;
import com.pantuo.dao.CpdLogRepository;
import com.pantuo.dao.CpdRepository;
import com.pantuo.dao.pojo.JpaCpd;
import com.pantuo.dao.pojo.JpaCpdLog;
import com.pantuo.dao.pojo.JpaProduct;
import com.pantuo.dao.pojo.QJpaCpd;
import com.pantuo.mybatis.domain.Account;
import com.pantuo.mybatis.domain.AccountExample;
import com.pantuo.mybatis.domain.RoleCpd;
import com.pantuo.mybatis.domain.RoleCpdExample;
import com.pantuo.mybatis.domain.UserCpd;
import com.pantuo.mybatis.domain.UserCpdExample;
import com.pantuo.mybatis.persistence.AccountMapper;
import com.pantuo.mybatis.persistence.CpdProductMapper;
import com.pantuo.mybatis.persistence.OrdersMapper;
import com.pantuo.mybatis.persistence.RoleCpdMapper;
import com.pantuo.mybatis.persistence.UserCpdMapper;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.CpdService;
import com.pantuo.util.Pair;
import com.pantuo.util.Request;

@Service 
public class CpdServiceImpl implements CpdService {

	@Autowired
	CpdRepository cpdRepository;
	@Autowired
	AccountMapper accountMapper;
	@Autowired
	RoleCpdMapper roleCpdMapper;
	@Autowired
	UserCpdMapper userCpdMapper;
	@Autowired
	CpdLogRepository cpdLogRepository;
	@Autowired
	CpdProductMapper cpdProductMapper;
	OrdersMapper d;

	public void test() {
		
		Pageable p = new PageRequest(0, 20, new Sort("id"));
		BooleanExpression query = QJpaCpd.jpaCpd.id.eq(1);
		Page<JpaCpd> list=	cpdRepository.findAll(query, p);
		System.out.println(list.getContent().size());
		
		  query = QJpaCpd.jpaCpd.product.id.eq(1);
		 list=	cpdRepository.findAll(query, p);
		System.out.println(list.getContent().size());
		
		List<JpaCpd>  tt = cpdRepository.findAll();
		System.out.println(tt);

	}

	
	@Override
	public Pair<Boolean, String> setMyPrice(int cpdid, Principal principal, double myPrice) {
		JpaCpd jpaCpd=cpdRepository.findOne(QJpaCpd.jpaCpd.product.id.eq(cpdid));
		if (jpaCpd == null) {
			return new Pair<Boolean, String>(false,"产品信息丢失");
		}
		if (jpaCpd.getBiddingDate() != null && jpaCpd.getBiddingDate().getTime() < System.currentTimeMillis()) {
			return new Pair<Boolean, String>(false,"竞价结束");
		}
		AccountExample example=new AccountExample();
		AccountExample.Criteria criteria=example.createCriteria();
		criteria.andUserIdEqualTo(Request.getUserId(principal));
		List<Account> accounts=accountMapper.selectByExample(example);
		Account account = accounts.size()>0?accounts.get(0):null;
		if (account == null || account.getTotalMoney() == null || account.getTotalMoney() < myPrice) {
			return new Pair<Boolean, String>(false,"余额不足");
		}

		RoleCpdExample example2 = new RoleCpdExample();
		RoleCpdExample.Criteria criteria2 = example2.createCriteria();
		criteria2.andProductIdEqualTo(cpdid);
		criteria2.andComparePriceLessThan(myPrice);
		RoleCpd  roleCpd=new RoleCpd();
		roleCpd.setUserId(Request.getUserId(principal));
		roleCpd.setComparePrice(myPrice);
		int result = roleCpdMapper.updateByExampleSelective(roleCpd, example2);
		if (result == 0) {
			return new Pair<Boolean, String>(false,"竞价失败");
		} else {
			UserCpd userCpd=new UserCpd();
			userCpd.setCpdid(cpdid);
			userCpd.setComparePrice(myPrice);
			userCpd.setUserId(Request.getUserId(principal));
			userCpd.setCreated(new Date());
			userCpd.setType(JpaCpdLog.OverType.wait.ordinal());
			userCpdMapper.insert(userCpd);
			cpdProductMapper.updateCpdCompareCount(cpdid);
		}
		return new Pair<Boolean, String>(true,"竞价成功！");
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public void changeMoney(Principal principal, int cpdid, double myPrice) {
		UserCpdExample example = new UserCpdExample();
		UserCpdExample.Criteria criteria = example.createCriteria();
		criteria.andCpdidEqualTo(cpdid);
		criteria.andComparePriceLessThan(myPrice);
		criteria.andUserIdEqualTo(Request.getUserId(principal));
		example.setOrderByClause(" id desc ");
		List<UserCpd> logs = userCpdMapper.selectByExample(example);
		if (logs.size() > 0) {
			UserCpd log = logs.get(0);
			String userId = log.getUserId();
			log.setType(JpaCpdLog.OverType.over.ordinal());
			userCpdMapper.updateByPrimaryKey(log);
			Account account = accountMapper.selectByPrimaryKey(userId);
			account.setTotalMoney(account.getTotalMoney() + log.getComparePrice());
			account.setUpdated(new Date());
			accountMapper.updateByPrimaryKey(account);
		}
		Account account = accountMapper.selectByPrimaryKey(Request.getUserId(principal));
		account.setTotalMoney(account.getTotalMoney() - myPrice);
		account.setUpdated(new Date());

		accountMapper.updateByPrimaryKey(account);
	}

	@Override
	public Page<JpaCpdLog> queryCpdLog(int cpdid, Sort sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JpaCpd queryOneCpdDetail(int cpdid) {
		BooleanExpression query = QJpaCpd.jpaCpd.product.id.eq(cpdid);
		cpdProductMapper.updateCpdPv(cpdid);
		return cpdRepository.findOne(query);
	}

	@Override
	public Pair<Boolean, JpaCpd> saveOrUpdateCpd(JpaCpd cpd) {
		cpd.setState(JpaCpd.State.online);
		cpd.setIspay(JpaCpd.OverType.wait);
		 cpdRepository.save(cpd);
		 return new  Pair<Boolean, JpaCpd>(true,cpd);
			
	}

	@Override
	public Page<JpaCpd> getCompareProducts(int city, TableRequest req, Principal principal) {
		int page = req.getPage(), pageSize = req.getLength();
		Sort sort = req.getSort("id");
		if (page < 0)
			page = 0;
		if (pageSize < 1)
			pageSize = 1;
		sort = (sort == null ? new Sort("id") : sort);
		Pageable p = new PageRequest(page, pageSize, sort);
        BooleanExpression query = city >= 0 ? QJpaCpd.jpaCpd.product.city.eq(city) : QJpaCpd.jpaCpd.product.city.goe(0);
        String type = req.getFilter("type");
        if(StringUtils.isNotBlank(type)){
        	JpaProduct.Type f=JpaProduct.Type.valueOf(JpaProduct.Type.class, type);
        	query = query.and(QJpaCpd.jpaCpd.product.type.eq(f));
        }
        return cpdRepository.findAll(query, p);
	}
}
