package com.pantuo.service.impl;

import java.io.File;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.pantuo.mybatis.domain.Attachment;
import com.pantuo.mybatis.persistence.AttachmentMapper;
import com.pantuo.service.AttachmentService;
import com.pantuo.util.BusinessException;
import com.pantuo.util.FileHelper;
import com.pantuo.util.Pair;

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

	public void saveAttachment(HttpServletRequest request, String user_id, int main_id, String file_type)
			throws BusinessException {

		try {
			CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession()
					.getServletContext());
			log.info("userid:{},main_id:{},file_type:{}", user_id, main_id, file_type);
			if (multipartResolver.isMultipart(request)) {
				String path = request.getSession().getServletContext().getRealPath("/WEB-INF/upload_temp/");
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
							t.setCreateTime(new Date());
							t.setEditTime(t.getCreateTime());
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
}
