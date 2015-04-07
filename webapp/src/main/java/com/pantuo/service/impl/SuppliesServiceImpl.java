package com.pantuo.service.impl;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.pantuo.dao.pojo.JpaProduct;
import com.pantuo.dao.pojo.JpaSupplies;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pantuo.mybatis.domain.Attachment;
import com.pantuo.mybatis.domain.Supplies;
import com.pantuo.mybatis.domain.SuppliesExample;
import com.pantuo.mybatis.persistence.SuppliesMapper;
import com.pantuo.service.AttachmentService;
import com.pantuo.service.SuppliesService;
import com.pantuo.util.BusinessException;
import com.pantuo.util.NumberPageUtil;
import com.pantuo.util.Pair;
import com.pantuo.util.Request;
import com.pantuo.web.view.SuppliesView;

@Service
public class SuppliesServiceImpl implements SuppliesService {

	@Autowired
	SuppliesMapper suppliesMapper;
	@Autowired
	AttachmentService attachmentService;

	public Pair<Boolean, String> addSupplies(Supplies obj, HttpServletRequest request) {
		Pair<Boolean, String> r = null;
		if (StringUtils.isBlank(obj.getName())) {
			return r = new Pair<Boolean, String>(false, "素材说明不能为空!");
		}
		try {
			obj.setUserId(Request.getUserId(request));
			obj.setCreated(new Date());
			obj.setUpdated(obj.getCreated());
			int dbId = suppliesMapper.insert(obj);
			if (dbId > 0) {
				attachmentService.saveAttachment(request, Request.getUserId(request), obj.getId(), "su_file");
			}
			r = new Pair<Boolean, String>(true, "success");
		} catch (BusinessException e) {
			r = new Pair<Boolean, String>(false, "素材文件保存失败");
		}
		return r;
	}

	public Pair<Boolean, String> removeSupplies(int supplies_id, HttpServletRequest request) {
		Supplies t = suppliesMapper.selectByPrimaryKey(supplies_id);
		if (t != null && StringUtils.equals(Request.getUserId(request), t.getUserId())) {
			List<Attachment> atts = attachmentService.queryFile(request, supplies_id);
			if (!atts.isEmpty()) {
				for (Attachment attachment : atts) {
					attachmentService.removeAttachment(request, Request.getUserId(request), attachment.getId());
				}
			}
			suppliesMapper.deleteByPrimaryKey(supplies_id);
			return new Pair<Boolean, String>(true, "素材文件删除成功");
		} else {
			return new Pair<Boolean, String>(false, "该素材不存在或是素材属主不对!");
		}
	}

	public List<Supplies> queryMyList(NumberPageUtil page, String name, JpaProduct.Type type, HttpServletRequest request) {
		SuppliesExample ex = getExample(name, type, request);
		ex.setOrderByClause("created desc");
		ex.setLimitStart(page.getLimitStart());
		ex.setLimitEnd(page.getPagesize());
		return suppliesMapper.selectByExample(ex);
	}

	public int countMyList(String name, JpaProduct.Type type, HttpServletRequest request) {

		return suppliesMapper.countByExample(getExample(name, type, request));
	}

	public SuppliesExample getExample(String name, JpaProduct.Type type, HttpServletRequest request) {
		SuppliesExample example = new SuppliesExample();
		SuppliesExample.Criteria ca = example.createCriteria();
		if (StringUtils.isNoneBlank(name)) {
			ca.andNameLike("%" + name + "%");
		}
		if (type != null) {
			ca.andSuppliesTypeEqualTo(type.ordinal());
		}
		ca.andUserIdEqualTo(Request.getUserId(request));
		return example;
	}

	//@Override
	public SuppliesView getSuppliesDetail(int supplies_id, HttpServletRequest request) {
		SuppliesView v = null;
		Supplies supplies = suppliesMapper.selectByPrimaryKey(supplies_id);
		if (supplies != null) {
			v = new SuppliesView();
			List<Attachment> files = attachmentService.queryFile(request, supplies_id);
			v.setFiles(files);
			v.setMainView(supplies);
		}
		return v;
	}

}
