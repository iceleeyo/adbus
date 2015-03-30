package com.pantuo.service.impl;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pantuo.mybatis.domain.Attachment;
import com.pantuo.mybatis.domain.Supplies;
import com.pantuo.mybatis.persistence.SuppliesMapper;
import com.pantuo.service.AttachmentService;
import com.pantuo.service.SuppliesService;
import com.pantuo.util.BusinessException;
import com.pantuo.util.Pair;
import com.pantuo.util.Request;

@Service
public class SuppliesServiceImpl implements SuppliesService {

	@Autowired
	SuppliesMapper suppliesMapper;
	@Autowired
	AttachmentService attachmentService;

	@Override
	public Pair<Boolean, String> addSupplies(Supplies obj, HttpServletRequest request) {
		Pair<Boolean, String> r = null;
		if (StringUtils.isBlank(obj.getName())) {
			return r = new Pair<Boolean, String>(false, "素材说明不能为空!");
		}
		try {
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

	@Override
	public Pair<Boolean, String> removeSupplies(int supplies_id, HttpServletRequest request) {
		Supplies t = suppliesMapper.selectByPrimaryKey(supplies_id);
		if (t != null && StringUtils.equals(Request.getUserId(request), t.getUserId())) {
			List<Attachment> atts= attachmentService.queryFile(request, supplies_id);
			if(!atts.isEmpty()) {
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

}
