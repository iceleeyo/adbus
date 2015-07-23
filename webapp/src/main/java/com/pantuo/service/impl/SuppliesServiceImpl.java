package com.pantuo.service.impl;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.mysema.query.types.Predicate;
import com.pantuo.ActivitiConfiguration;
import com.pantuo.dao.InvoiceRepository;
import com.pantuo.dao.ProductRepository;
import com.pantuo.dao.UserDetailRepository;
import com.pantuo.dao.pojo.JpaAttachment;
import com.pantuo.dao.pojo.JpaInvoice;
import com.pantuo.dao.pojo.JpaProduct;
import com.pantuo.dao.pojo.JpaSupplies;
import com.pantuo.dao.pojo.QJpaProduct;
import com.pantuo.dao.pojo.QUserDetail;
import com.pantuo.dao.pojo.UserDetail;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pantuo.mybatis.domain.Attachment;
import com.pantuo.mybatis.domain.AttachmentExample;
import com.pantuo.mybatis.domain.Industry;
import com.pantuo.mybatis.domain.IndustryExample;
import com.pantuo.mybatis.domain.Invoice;
import com.pantuo.mybatis.domain.InvoiceDetail;
import com.pantuo.mybatis.domain.InvoiceDetailExample;
import com.pantuo.mybatis.domain.InvoiceExample;
import com.pantuo.mybatis.domain.Orders;
import com.pantuo.mybatis.domain.OrdersExample;
import com.pantuo.mybatis.domain.Supplies;
import com.pantuo.mybatis.domain.SuppliesExample;
import com.pantuo.mybatis.persistence.AttachmentMapper;
import com.pantuo.mybatis.persistence.IndustryMapper;
import com.pantuo.mybatis.persistence.InvoiceDetailMapper;
import com.pantuo.mybatis.persistence.InvoiceMapper;
import com.pantuo.mybatis.persistence.OrdersMapper;
import com.pantuo.mybatis.persistence.SuppliesMapper;
import com.pantuo.service.AttachmentService;
import com.pantuo.service.SuppliesService;
import com.pantuo.service.UserServiceInter;
import com.pantuo.service.ActivitiService.SystemRoles;
import com.pantuo.util.BeanUtils;
import com.pantuo.util.BusinessException;
import com.pantuo.util.NumberPageUtil;
import com.pantuo.util.Pair;
import com.pantuo.util.Request;
import com.pantuo.web.view.InvoiceView;
import com.pantuo.web.view.SuppliesView;

@Service
public class SuppliesServiceImpl implements SuppliesService {

	@Autowired
	SuppliesMapper suppliesMapper;
	@Autowired
	InvoiceMapper invoiceMapper;
	@Autowired
	AttachmentMapper attachmentMapper;
	@Autowired
	OrdersMapper ordersMapper;
	@Autowired
	InvoiceDetailMapper invoiceDetailMapper;
	@Autowired
	IndustryMapper industryMapper;
	@Autowired
	AttachmentService attachmentService;
	@Autowired
	InvoiceRepository InvoiceRepo;
	@Autowired
	UserDetailRepository userDetailRepo;
	@Autowired
	UserServiceInter userService;

	public Pair<Object, String> addSupplies(int city, Supplies obj, Principal principal, HttpServletRequest request) {
		obj.setCity(city);
		Pair<Object, String> r = null;
		if (StringUtils.isBlank(obj.getName())) {
			return r = new Pair<Object, String>(null, "素材说明不能为空!");
		}

		try {
			if (Request.hasAuth(principal, SystemRoles.ShibaOrderManager.name())) {
				if (StringUtils.isNoneBlank(obj.getUserId())) {
					if (!userService.isUserHaveGroup(obj.getUserId(), SystemRoles.advertiser.name())) {
						return new Pair<Object, String>(null, obj.getUserId() + " 不是广告主,素材保存失败！");
					}

					obj.setUserId(obj.getUserId());
				} else {
					obj.setUserId(Request.getUserId(principal));
				}
			} else {
				obj.setUserId(Request.getUserId(principal));
			}

			obj.setCreated(new Date());
			obj.setUpdated(obj.getCreated());
			com.pantuo.util.BeanUtils.filterXss(obj);
			int dbId = suppliesMapper.insert(obj);
			if (dbId > 0) {
				attachmentService.saveAttachment(request, Request.getUserId(principal), obj.getId(),
						JpaAttachment.Type.su_file,null);
			}
			r = new Pair<Object, String>(obj, "物料上传成功！");
		} catch (BusinessException e) {
			r = new Pair<Object, String>(null, "素材文件保存失败");
		}
		return r;
	}

	public Pair<Boolean, String> addInvoice(int city,JpaInvoice obj, Principal principal, HttpServletRequest request) {
		Pair<Boolean, String> r = null;
		try {
			InvoiceExample example =new InvoiceExample();
			InvoiceExample.Criteria c=example.createCriteria();
			c.andUserIdEqualTo( Request.getUserId(principal));
			List<Invoice> invoices=invoiceMapper.selectByExample(example);
			if(invoices.size()>0 && obj.getId()>0){
				JpaInvoice jpaInvoice=InvoiceRepo.findOne(obj.getId());
				jpaInvoice.setUpdated(new Date());
				jpaInvoice.setAccountnum(obj.getAccountnum());
				jpaInvoice.setBankname(obj.getBankname());
				jpaInvoice.setFixphone(obj.getFixphone());
				jpaInvoice.setMailaddr(obj.getMailaddr());
				jpaInvoice.setRegisaddr(obj.getRegisaddr());
				jpaInvoice.setTaxrenum(obj.getTaxrenum());
				jpaInvoice.setTitle(obj.getTitle());
				jpaInvoice.setContactman(obj.getContactman());
				jpaInvoice.setPhonenum(obj.getPhonenum());
				BeanUtils.filterXss(jpaInvoice);
				InvoiceRepo.save(jpaInvoice);
				attachmentService.upInvoiceAttachments(request, Request.getUserId(principal), jpaInvoice.getId(),
						JpaAttachment.Type.fp_file,null);
			}else{
			obj.setCity(city);
			obj.setCreated(new Date());
			obj.setUpdated(obj.getCreated());
			obj.setUserId(Request.getUserId(principal));
			BeanUtils.filterXss(obj);
			InvoiceRepo.save(obj);
			attachmentService.saveAttachment(request, Request.getUserId(principal), obj.getId(),
					JpaAttachment.Type.fp_file,null);
			
			}
			r = new Pair<Boolean, String>(true, "发票保存成功！");
		} catch (BusinessException e) {
			r = new Pair<Boolean, String>(false, "发票保存失败");
		}
		return r;
	}

	public Pair<Boolean, String> savequlifi(Principal principal, HttpServletRequest request,String description) {
		Pair<Boolean, String> r = null;
		try {
			Predicate query = QUserDetail.userDetail.username.eq(Request.getUserId(principal));
			UserDetail userDetail = userDetailRepo.findOne(query);
			if(userDetail!=null){
				attachmentService.saveAttachment(request, Request.getUserId(principal), userDetail.getId(),
						JpaAttachment.Type.user_qualifi,description);
				userDetail.setUstats(UserDetail.UStats.upload);
				userDetailRepo.save(userDetail);
				r = new Pair<Boolean, String>(true, "保存成功");
			}
		} catch (BusinessException e) {
			r = new Pair<Boolean, String>(false, "保存失败");
		}
		return r;
	}

	public Pair<Boolean, String> removeSupplies(int supplies_id, Principal principal, HttpServletRequest request) {
		Supplies t = suppliesMapper.selectByPrimaryKey(supplies_id);
		if (t != null && StringUtils.equals(Request.getUserId(principal), t.getUserId())) {
			List<Attachment> atts = attachmentService.querysupFile(principal, supplies_id);
			if (!atts.isEmpty()) {
				for (Attachment attachment : atts) {
					attachmentService.removeAttachment(request, Request.getUserId(principal), attachment.getId());
				}
			}
			suppliesMapper.deleteByPrimaryKey(supplies_id);
			return new Pair<Boolean, String>(true, "素材文件删除成功");
		} else {
			return new Pair<Boolean, String>(false, "该素材不存在或是素材属主不对!");
		}
	}

	public List<Supplies> queryMyList(int city, NumberPageUtil page, String name, JpaProduct.Type type,
			Principal principal) {
		SuppliesExample ex = getExample(city, name, type, principal);
		ex.setOrderByClause("created desc");
		ex.setLimitStart(page.getLimitStart());
		ex.setLimitEnd(page.getPagesize());
		return suppliesMapper.selectByExample(ex);
	}

	public int countMyList(int city, String name, JpaProduct.Type type, Principal principal) {

		return suppliesMapper.countByExample(getExample(city, name, type, principal));
	}

	public SuppliesExample getExample(int city, String name, JpaProduct.Type type, Principal principal) {
		SuppliesExample example = new SuppliesExample();
		SuppliesExample.Criteria ca = example.createCriteria();
		ca.andCityEqualTo(city);
		if (StringUtils.isNoneBlank(name)) {
			ca.andNameLike("%" + name + "%");
		}
		if (type != null) {
			ca.andSuppliesTypeEqualTo(type.ordinal());
		}
		ca.andUserIdEqualTo(Request.getUserId(principal));
		return example;
	}

	public SuppliesView getSuppliesDetail(int supplies_id, Principal principal) {
		SuppliesView v = null;
		Supplies supplies = suppliesMapper.selectByPrimaryKey(supplies_id);
		if (supplies != null) {
			v = new SuppliesView();
			List<Attachment> files = attachmentService.querysupFile(principal, supplies_id);
			v.setFiles(files);
			v.setMainView(supplies);
		}
		return v;
	}
	public Pair<Boolean, String> delSupp(int Suppid,Principal principal) {
	    OrdersExample example=new OrdersExample();
	    OrdersExample.Criteria criteria=example.createCriteria();
	    criteria.andSuppliesIdEqualTo(Suppid);
	    if (Request.hasOnlyAuth(principal, ActivitiConfiguration.ADVERTISER)) {
	    	criteria.andUserIdEqualTo(Request.getUserId(principal));
	    }
	     List<Orders> orders= ordersMapper.selectByExample(example);
	     if(orders.size()>0){
	    	 return	new Pair<Boolean, String>(true, "该物料已有订单关联，不能删除！"); 
	     }
		int a=suppliesMapper.deleteByPrimaryKey(Suppid);
		if(a>0){
			AttachmentExample example2=new AttachmentExample();
			AttachmentExample.Criteria criteria2=example2.createCriteria();
			AttachmentExample.Criteria criteria3=example2.createCriteria();
		    criteria2.andMainIdEqualTo(Suppid);
		    criteria2.andTypeEqualTo(4);
		    criteria2.andUserIdEqualTo(Request.getUserId(principal));
		    criteria3.andTypeEqualTo(3);
		    criteria3.andUserIdEqualTo(Request.getUserId(principal));
		    example2.or(criteria3);
		    List<Attachment> attas=attachmentMapper.selectByExample(example2);
		    for (Attachment attachment : attas) {
		    	if(attachment!=null){
		    		attachmentMapper.deleteByPrimaryKey(attachment.getId());
		    	}
			}
	    	return	new Pair<Boolean, String>(true, "删除物料成功！");
		}
		return	new Pair<Boolean, String>(true, "删除物料失败！");
}
	public InvoiceView getInvoiceDetail(int orderid, Principal principal) {
		Orders orders=ordersMapper.selectByPrimaryKey(orderid);
		InvoiceView v = null;
		InvoiceDetail invoiceDetail=null;
         if(orders!=null && orders.getInvoiceId()!=null){
        	 invoiceDetail=invoiceDetailMapper.selectByPrimaryKey(orders.getInvoiceId());
		}
		if (invoiceDetail!= null) {
			v = new InvoiceView();
			List<Attachment> files = attachmentService.queryinvoiceF(principal, invoiceDetail.getMainid());
			v.setFiles(files);
			v.setDetailView(invoiceDetail);
		}
		return v;
	}

	public SuppliesView getQua(int supplies_id, Principal principal) {
		SuppliesView v = null;
		Supplies supplies = suppliesMapper.selectByPrimaryKey(supplies_id);
		if (supplies != null) {
			v = new SuppliesView();
			List<Attachment> files = attachmentService.queryQua(principal, supplies_id);
			v.setFiles(files);
			v.setMainView(supplies);
		}
		return v;
	}

	public Supplies selectSuppliesById(Integer suppliesId) {

		return suppliesMapper.selectByPrimaryKey(suppliesId);
	}

	public int updateSupplies(int city, Supplies supplies) {
		if (supplies.getCity() == null) {
			supplies.setCity(city);
		} else if (city != supplies.getCity()) {
			return 0;
		}
		if (supplies.getSeqNumber() != null) {
			Supplies sup = suppliesMapper.selectByPrimaryKey(supplies.getId());
			sup.setSeqNumber(supplies.getSeqNumber());
			return suppliesMapper.updateByPrimaryKey(sup);
		}
		return 1;
	}

	public List<Supplies> querySuppliesByUser(int city, Principal principal) {
		SuppliesExample example = new SuppliesExample();
		SuppliesExample.Criteria criteria = example.createCriteria();
		criteria.andCityEqualTo(city);
		if (principal != null) {
			criteria.andUserIdEqualTo(Request.getUserId(principal));
		}
		return suppliesMapper.selectByExample(example);
	}

	@Override
	public List<Industry> getIndustry() {
		IndustryExample example=new IndustryExample();
		IndustryExample.Criteria criteria=example.createCriteria();
		return industryMapper.selectByExample(example);
	}

}
