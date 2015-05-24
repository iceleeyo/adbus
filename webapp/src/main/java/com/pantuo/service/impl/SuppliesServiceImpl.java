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
import com.pantuo.mybatis.domain.Invoice;
import com.pantuo.mybatis.domain.InvoiceExample;
import com.pantuo.mybatis.domain.Supplies;
import com.pantuo.mybatis.domain.SuppliesExample;
import com.pantuo.mybatis.persistence.InvoiceMapper;
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
	AttachmentService attachmentService;
	@Autowired
	InvoiceRepository InvoiceRepo;
	@Autowired
	UserDetailRepository userDetailRepo;
	@Autowired
	UserServiceInter userService;

	public Pair<Boolean, String> addSupplies(int city, Supplies obj, Principal principal, HttpServletRequest request) {
		obj.setCity(city);
		Pair<Boolean, String> r = null;
		if (StringUtils.isBlank(obj.getName())) {
			return r = new Pair<Boolean, String>(false, "素材说明不能为空!");
		}

		try {
			if (Request.hasAuth(principal, SystemRoles.ShibaOrderManager.name())) {
				if (StringUtils.isNoneBlank(obj.getUserId())) {
					if (!userService.isUserHaveGroup(obj.getUserId(), SystemRoles.advertiser.name())) {
						return new Pair<Boolean, String>(true, obj.getUserId() + " 不是广告主,素材保存失败！");
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
			r = new Pair<Boolean, String>(true, "物料上传成功！");
		} catch (BusinessException e) {
			r = new Pair<Boolean, String>(false, "素材文件保存失败");
		}
		return r;
	}

	public Pair<Boolean, String> addInvoice(JpaInvoice obj, Principal principal, HttpServletRequest request) {
		Pair<Boolean, String> r = null;
		try {
			obj.setCreated(new Date());
			obj.setUpdated(obj.getCreated());
			obj.setUserId(Request.getUserId(principal));
			BeanUtils.filterXss(obj);
			InvoiceRepo.save(obj);
			attachmentService.saveAttachment(request, Request.getUserId(principal), obj.getId(),
					JpaAttachment.Type.fp_file,null);
			r = new Pair<Boolean, String>(true, "创建发票成功！");
		} catch (BusinessException e) {
			r = new Pair<Boolean, String>(false, "创建发票失败");
		}
		return r;
	}

	public Pair<Boolean, String> savequlifi(Principal principal, HttpServletRequest request,String description) {
		Pair<Boolean, String> r = null;
		try {
			Predicate query = QUserDetail.userDetail.username.eq(Request.getUserId(principal));
			UserDetail userDetail = userDetailRepo.findOne(query);
			attachmentService.saveAttachment(request, Request.getUserId(principal), userDetail.getId(),
					JpaAttachment.Type.u_fj,description);
			r = new Pair<Boolean, String>(true, "上传成功！");
		} catch (BusinessException e) {
			r = new Pair<Boolean, String>(false, "上传失败");
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
			List<Attachment> files = attachmentService.queryAllFile(principal, supplies_id);
			v.setFiles(files);
			v.setMainView(supplies);
		}
		return v;
	}

	public InvoiceView getInvoiceDetail(String userid, Principal principal) {
		InvoiceView v = null;
		InvoiceExample example = new InvoiceExample();
		InvoiceExample.Criteria c = example.createCriteria();
		c.andUserIdEqualTo(userid);
		List<Invoice> ins = invoiceMapper.selectByExample(example);
		Invoice in = null;
		if (ins.size() > 0) {
			in = ins.get(0);
		}
		if (in != null) {
			v = new InvoiceView();
			List<Attachment> files = attachmentService.queryinvoiceF(principal, in.getId());
			v.setFiles(files);
			v.setMainView(in);
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

}
