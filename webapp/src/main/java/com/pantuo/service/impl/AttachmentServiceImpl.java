package com.pantuo.service.impl;

import java.io.File;
import java.security.Principal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.pantuo.dao.pojo.JpaAttachment;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.pantuo.mybatis.domain.Attachment;
import com.pantuo.mybatis.domain.AttachmentExample;
import com.pantuo.mybatis.persistence.AttachmentMapper;
import com.pantuo.service.AttachmentService;
import com.pantuo.util.BusinessException;
import com.pantuo.util.FileHelper;
import com.pantuo.util.Pair;
import com.pantuo.util.Request;

/**
 * 
 * <b><code>AttachmentService</code></b>
 * <p>
 * 文件接口
 * </p>
 * <b>Creation Time:</b> Mar 30, 2015 11:40:53 AM
 * @author impanxh@gmail.com
 * @since pantuotech 1.0-SNAPSHOT
 */

@Service
public class AttachmentServiceImpl implements AttachmentService { 

	@Autowired
	AttachmentMapper attachmentMapper;
	private static Logger log = LoggerFactory.getLogger(SuppliesServiceImpl.class);

	public void saveAttachment(HttpServletRequest request, String user_id, int main_id, JpaAttachment.Type file_type)
			throws BusinessException {

		try {
			CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession()
					.getServletContext());
			log.info("userid:{},main_id:{},file_type:{}", user_id, main_id, file_type);
			if (multipartResolver.isMultipart(request)) {
				String path = request.getSession().getServletContext()
						.getRealPath(com.pantuo.util.Constants.FILE_UPLOAD_DIR);
				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
				Iterator<String> iter = multiRequest.getFileNames();
				while (iter.hasNext()) {
					MultipartFile file = multiRequest.getFile(iter.next());
					if (file != null && !file.isEmpty()) {
						String oriFileName = file.getOriginalFilename();
						if (StringUtils.isNoneBlank(oriFileName)) {

							Pair<String, String> p = FileHelper.getUploadFileName(path, oriFileName);
							File localFile = new File(p.getLeft());
							file.transferTo(localFile);
							Attachment t = new Attachment();
							t.setMainId(main_id);
                            t.setType(file_type.name());
							t.setCreated(new Date());
							t.setUpdated(t.getCreated());
							t.setName(oriFileName);
							t.setUrl(p.getRight());
							t.setUserId(user_id);
							attachmentMapper.insert(t);
						}
					}
				}
			}

		} catch (Exception e) {
			log.error("saveAttachment", e);
			throw new BusinessException("saveAttachment-error", e);
		}

	}

	 
	
	public Pair<Boolean, String> removeAttachment(HttpServletRequest request, String user_id, int att_id) {
		Attachment t = attachmentMapper.selectByPrimaryKey(att_id);
		if (t != null && StringUtils.equals(user_id, t.getUserId())) {
			if (StringUtils.isNoneBlank(t.getUrl())) {
				String path = request.getSession().getServletContext()
						.getRealPath(com.pantuo.util.Constants.FILE_UPLOAD_DIR);
				File f = new File(path + "/" + t.getUrl());
				f.delete();
			}
			attachmentMapper.deleteByPrimaryKey(att_id);
			return new Pair<Boolean, String>(true, "素材文件删除成功");  
		} else {
			return new Pair<Boolean, String>(false, "该素材不存在或是素材属主不对!");
		}
	}

	public List<Attachment> queryFile(Principal principal, int main_id) {
		AttachmentExample example =new AttachmentExample();
		AttachmentExample.Criteria ca=example.createCriteria();
		ca.andMainIdEqualTo(main_id);
		ca.andUserIdEqualTo(Request.getUserId(principal));
		return attachmentMapper.selectByExample(example);
	}
}
