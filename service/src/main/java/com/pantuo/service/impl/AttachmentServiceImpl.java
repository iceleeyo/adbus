package com.pantuo.service.impl;

import java.io.File;
import java.security.Principal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.mysema.query.types.Predicate;
import com.pantuo.dao.UserDetailRepository;
import com.pantuo.dao.pojo.JpaAttachment;
import com.pantuo.dao.pojo.QUserDetail;
import com.pantuo.dao.pojo.UserDetail;
import com.pantuo.mybatis.domain.Attachment;
import com.pantuo.mybatis.domain.AttachmentExample;
import com.pantuo.mybatis.persistence.AttachmentMapper;
import com.pantuo.service.AttachmentService;
import com.pantuo.util.BusinessException;
import com.pantuo.util.FFmpegUtil;
import com.pantuo.util.FileHelper;
import com.pantuo.util.GlobalMethods;
import com.pantuo.util.Pair;
import com.pantuo.service.security.Request;
import com.pantuo.web.upload.CustomMultipartResolver;

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
	@Autowired
	UserDetailRepository userDetailRepo;
	private static Logger log = LoggerFactory.getLogger(SuppliesServiceImpl.class);

	
	
	public String saveAttachmentSimple(HttpServletRequest request) throws BusinessException {
		StringBuffer r = new StringBuffer();
		try {
			CustomMultipartResolver multipartResolver = new CustomMultipartResolver(request.getSession()
					.getServletContext());
			if (multipartResolver.isMultipart(request)) {
				String path = request.getSession().getServletContext()
						.getRealPath(com.pantuo.util.Constants.FILE_UPLOAD_DIR).replaceAll("WEB-INF", "");
				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
				Iterator<String> iter = multiRequest.getFileNames();
				while (iter.hasNext()) {
					MultipartFile file = multiRequest.getFile(iter.next());
					if (file != null && !file.isEmpty()) {
						String oriFileName = file.getOriginalFilename();
						if (StringUtils.isNoneBlank(oriFileName)) {
							String storeName = GlobalMethods.md5Encrypted((System.currentTimeMillis() + oriFileName)
									.getBytes());
							Pair<String, String> p = FileHelper.getUploadFileName(path,
									storeName += FileHelper.getFileExtension(oriFileName, true));
							File localFile = new File(p.getLeft());
							file.transferTo(localFile);
							if (r.length() == 0) {
								r.append(p.getRight());
							} else {
								r.append(";" + p.getRight());
							}
						}
					}
				}
			}
			return r.toString();
		} catch (Exception e) {
			log.error("saveAttachmentSimple", e);
			throw new BusinessException("saveAttachment-error", e);
		}
	}
							
							
							
	public void saveAttachment(HttpServletRequest request, String user_id, int main_id, JpaAttachment.Type file_type,String description)
			throws BusinessException {

		try {
			CustomMultipartResolver multipartResolver = new CustomMultipartResolver(request.getSession()
					.getServletContext());
			log.info("userid:{},main_id:{},file_type:{}", user_id, main_id, file_type);
			if (multipartResolver.isMultipart(request)) {
				String path = request.getSession().getServletContext()
						.getRealPath(com.pantuo.util.Constants.FILE_UPLOAD_DIR).replaceAll("WEB-INF", "");
				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
				Iterator<String> iter = multiRequest.getFileNames();
				while (iter.hasNext()) {
					MultipartFile file = multiRequest.getFile(iter.next());
					if (file != null && !file.isEmpty()) {
						String oriFileName = file.getOriginalFilename();
						String fn=file.getName();
						if (StringUtils.isNoneBlank(oriFileName)) {

							String storeName = GlobalMethods.md5Encrypted((System.currentTimeMillis() + oriFileName)
									.getBytes());
							Pair<String, String> p = FileHelper.getUploadFileName(path,
									storeName += FileHelper.getFileExtension(oriFileName,true));
							File localFile = new File(p.getLeft());
							file.transferTo(localFile);
							FFmpegUtil.mpgToMp4(p.getLeft());
						    AttachmentExample example=new AttachmentExample();
							AttachmentExample.Criteria criteria=example.createCriteria();
							criteria.andMainIdEqualTo(main_id);
							criteria.andUserIdEqualTo(user_id);
							criteria.andTypeEqualTo(JpaAttachment.Type.user_qualifi.ordinal());
							List<Attachment> attachments=attachmentMapper.selectByExample(example);
							if(attachments.size()>0){
								        Attachment t=attachments.get(0);
										t.setUpdated(new Date());
										t.setName(oriFileName);
										t.setUrl(p.getRight());
										attachmentMapper.updateByPrimaryKey(t);
									
							}else{
							Attachment t = new Attachment();
							if(StringUtils.isNotBlank(description)){
								t.setDescription(description);
							}
							t.setMainId(main_id);
							if(StringUtils.equals(fn, "licensefile")){
								t.setType(JpaAttachment.Type.license.ordinal());
							}else if(fn.indexOf("qua")!=-1){
								t.setType(JpaAttachment.Type.u_fj.ordinal());
							}
							else if(StringUtils.equals(fn, "taxfile")){
								t.setType(JpaAttachment.Type.tax.ordinal());
							}
							else if(StringUtils.equals(fn, "taxpayerfile")){
								t.setType(JpaAttachment.Type.taxpayer.ordinal());
							}
							else if(StringUtils.equals(fn, "user_license")){
								t.setType(JpaAttachment.Type.user_license.ordinal());
							}
							else if(StringUtils.equals(fn, "user_code")){
								t.setType(JpaAttachment.Type.user_code.ordinal());
							}
							else if(StringUtils.equals(fn, "fp_other")){
								t.setType(JpaAttachment.Type.fp_other.ordinal());
							}
							else if(StringUtils.equals(fn, "user_tax")){
								t.setType(JpaAttachment.Type.user_tax.ordinal());
							}
							else{
								t.setType(file_type.ordinal());
							}
							t.setCreated(new Date());
							t.setUpdated(t.getCreated());
							t.setName(oriFileName);
							log.info(p.getRight());
							t.setUrl(p.getRight());
							t.setUserId(user_id);
							attachmentMapper.insert(t);
						}
						}
					}
				}
			}

		} catch (Exception e) {
			log.error("saveAttachment", e);
			throw new BusinessException("saveAttachment-error", e);
		}

	}
	@Override
	public String  savePayvoucher(HttpServletRequest request, String user_id, int main_id, JpaAttachment.Type file_type,String description)
			throws BusinessException {
		String result="";
		try {
			CustomMultipartResolver multipartResolver = new CustomMultipartResolver(request.getSession()
					.getServletContext());
			log.info("userid:{},main_id:{},file_type:{}", user_id, main_id, file_type);
			if (multipartResolver.isMultipart(request)) {
				String path = request.getSession().getServletContext()
						.getRealPath(com.pantuo.util.Constants.FILE_UPLOAD_DIR).replaceAll("WEB-INF", "");
				log.info("path=",path);
				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
				Iterator<String> iter = multiRequest.getFileNames();
				while (iter.hasNext()) {
					MultipartFile file = multiRequest.getFile(iter.next());
					if (file != null && !file.isEmpty()) {
						String oriFileName = file.getOriginalFilename();
						String fn=file.getName();
						if (StringUtils.isNoneBlank(oriFileName)) {
							
							String storeName = GlobalMethods.md5Encrypted((System.currentTimeMillis() + oriFileName)
									.getBytes());
							Pair<String, String> p = FileHelper.getUploadFileName(path,
									storeName += FileHelper.getFileExtension(oriFileName,true));
							File localFile = new File(p.getLeft());
							file.transferTo(localFile);
							AttachmentExample example=new AttachmentExample();
							AttachmentExample.Criteria criteria=example.createCriteria();
							criteria.andMainIdEqualTo(main_id);
							criteria.andUserIdEqualTo(user_id);
							criteria.andTypeEqualTo(file_type.ordinal());
							List<Attachment> attachments=attachmentMapper.selectByExample(example);
							if(attachments.size()>0){
								Attachment t=attachments.get(0);
								t.setUpdated(new Date());
								t.setName(oriFileName);
								t.setUrl(p.getRight());
								attachmentMapper.updateByPrimaryKey(t);
								result=p.getRight();
							}else{
								Attachment t = new Attachment();
								if(StringUtils.isNotBlank(description)){
									t.setDescription(description);
								}
								t.setMainId(main_id);
								t.setType(file_type.ordinal());
								t.setCreated(new Date());
								t.setUpdated(t.getCreated());
								t.setName(oriFileName);
								t.setUrl(p.getRight());
								t.setUserId(user_id);
								attachmentMapper.insert(t);
								result=p.getRight();
							}
						}
					}
				}
			}
			
		} catch (Exception e) {
			log.error("saveAttachment", e);
			throw new BusinessException("saveAttachment-error", e);
		}
		return result;
	}
	//查询发票附件
		public List<Attachment> queryinvoiceF(Principal principal, int main_id) {
			AttachmentExample example =new AttachmentExample();
			AttachmentExample.Criteria ca=example.createCriteria();
			AttachmentExample.Criteria ca2=example.createCriteria();
			AttachmentExample.Criteria ca3=example.createCriteria();
			AttachmentExample.Criteria ca4=example.createCriteria();
			ca.andMainIdEqualTo(main_id);
			ca2.andMainIdEqualTo(main_id);
			ca3.andMainIdEqualTo(main_id);
			ca4.andMainIdEqualTo(main_id);
			ca.andTypeEqualTo(JpaAttachment.Type.license.ordinal());
			ca2.andTypeEqualTo(JpaAttachment.Type.tax.ordinal());
			ca3.andTypeEqualTo(JpaAttachment.Type.taxpayer.ordinal());
			ca4.andTypeEqualTo(JpaAttachment.Type.fp_other.ordinal());
			example.or(ca2);
			example.or(ca3);
			example.or(ca4);
			return attachmentMapper.selectByExample(example);
		}
		
	//查询用户资质
	@Override
	public List<Attachment> findUserQulifi(String user_id) {
		Predicate query = QUserDetail.userDetail.username.eq(user_id);
		UserDetail userDetail = userDetailRepo.findOne(query);
		AttachmentExample example=new AttachmentExample();
		AttachmentExample.Criteria criteria=example.createCriteria();
		AttachmentExample.Criteria criteria2=example.createCriteria();
		AttachmentExample.Criteria criteria3=example.createCriteria();
		criteria.andUserIdEqualTo(user_id);
		criteria2.andUserIdEqualTo(user_id);
		criteria.andTypeEqualTo(JpaAttachment.Type.user_license.ordinal());
		criteria2.andTypeEqualTo(JpaAttachment.Type.user_tax.ordinal());
		criteria3.andTypeEqualTo(JpaAttachment.Type.user_code.ordinal());
		example.or(criteria2);
		example.or(criteria3);
		if(userDetail!=null){
			criteria.andMainIdEqualTo(userDetail.getId());
			criteria2.andMainIdEqualTo(userDetail.getId());
			criteria3.andMainIdEqualTo(userDetail.getId());
		}
		return attachmentMapper.selectByExample(example);
	}
	public void updateAttachments(HttpServletRequest request, String user_id, int main_id, JpaAttachment.Type file_type,String description)
			throws BusinessException {
		
		try {
			CustomMultipartResolver multipartResolver = new CustomMultipartResolver(request.getSession()
					.getServletContext());
			if (multipartResolver.isMultipart(request)) {
				String path = request.getSession().getServletContext()
						.getRealPath(com.pantuo.util.Constants.FILE_UPLOAD_DIR).replaceAll("WEB-INF", "");
				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
				Iterator<String> iter = multiRequest.getFileNames();
				while (iter.hasNext()) {
					MultipartFile file = multiRequest.getFile(iter.next());
					if (file != null && !file.isEmpty()) {
						String oriFileName = file.getOriginalFilename();
						String fn=file.getName();
						if (StringUtils.isNoneBlank(oriFileName)) {
							
							String storeName = GlobalMethods.md5Encrypted((System.currentTimeMillis() + oriFileName)
									.getBytes());
							Pair<String, String> p = FileHelper.getUploadFileName(path,
									storeName += FileHelper.getFileExtension(oriFileName,true));
							File localFile = new File(p.getLeft());
							file.transferTo(localFile);
							AttachmentExample example=new AttachmentExample();
							AttachmentExample.Criteria criteria=example.createCriteria();
							criteria.andMainIdEqualTo(main_id);
							criteria.andUserIdEqualTo(user_id);
							List<Attachment> attachments=attachmentMapper.selectByExample(example);
							for (Attachment t : attachments) {
								if(StringUtils.equals(fn, "licensefile")&& t.getType()==JpaAttachment.Type.license.ordinal()){
									t.setUpdated(new Date());
									t.setName(oriFileName);
									t.setUrl(p.getRight());
									attachmentMapper.updateByPrimaryKey(t);
								}
								else if(StringUtils.equals(fn, "taxfile") && t.getType()==JpaAttachment.Type.tax.ordinal()){
									t.setUpdated(new Date());
									t.setName(oriFileName);
									t.setUrl(p.getRight());
									attachmentMapper.updateByPrimaryKey(t);
								}
								else if(StringUtils.equals(fn, "fp_other") && t.getType()==JpaAttachment.Type.fp_other.ordinal()){
									t.setUpdated(new Date());
									t.setName(oriFileName);
									t.setUrl(p.getRight());
									attachmentMapper.updateByPrimaryKey(t);
								}
								else  if(StringUtils.equals(fn, "taxpayerfile") && t.getType()==JpaAttachment.Type.taxpayer.ordinal()){
									t.setUpdated(new Date());
									t.setName(oriFileName);
									t.setUrl(p.getRight());
									attachmentMapper.updateByPrimaryKey(t);
								}
								else if(StringUtils.equals(fn, "user_license") && t.getType()==JpaAttachment.Type.user_license.ordinal()){
								  t.setUpdated(new Date());
								  t.setName(oriFileName);
								  t.setUrl(p.getRight());
								  attachmentMapper.updateByPrimaryKey(t);
							  }
								else  if(StringUtils.equals(fn, "user_tax") && t.getType()==JpaAttachment.Type.user_tax.ordinal()){
								  t.setUpdated(new Date());
								  t.setName(oriFileName);
								  t.setUrl(p.getRight());
								  attachmentMapper.updateByPrimaryKey(t);
							  }
								else  if(StringUtils.equals(fn, "user_code") && t.getType()==JpaAttachment.Type.user_code.ordinal()){
								  t.setUpdated(new Date());
								  t.setName(oriFileName);
								  t.setUrl(p.getRight());
								  attachmentMapper.updateByPrimaryKey(t);
							  }
								
							}
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
  
	public List<Attachment> queryAllFile(Principal principal, int main_id) {
		AttachmentExample example =new AttachmentExample();
		AttachmentExample.Criteria ca=example.createCriteria();
		ca.andMainIdEqualTo(main_id);
		return attachmentMapper.selectByExample(example);
	}
	//查询素材附件以及绑定的资质
	public List<Attachment> querysupFile(Principal principal, int main_id) {
		AttachmentExample example =new AttachmentExample();
		AttachmentExample.Criteria ca=example.createCriteria();
		AttachmentExample.Criteria ca2=example.createCriteria();
		ca.andMainIdEqualTo(main_id);
		ca.andTypeEqualTo(JpaAttachment.Type.su_file.ordinal());
		ca2.andMainIdEqualTo(main_id);
		ca.andTypeEqualTo(JpaAttachment.Type.u_fj.ordinal());
		example.or(ca2);
		return attachmentMapper.selectByExample(example);
	}
	//查询图片素材
	public List<Attachment> queryimg(Principal principal, int main_id) {
		AttachmentExample example =new AttachmentExample();
		AttachmentExample.Criteria ca=example.createCriteria();
		ca.andMainIdEqualTo(main_id);
		ca.andTypeEqualTo(JpaAttachment.Type.su_file.ordinal());
		return attachmentMapper.selectByExample(example);
	}
	//查询合同附件
	public List<Attachment> queryContracF(Principal principal, int main_id) {
		AttachmentExample example =new AttachmentExample();
		AttachmentExample.Criteria ca=example.createCriteria();
		ca.andMainIdEqualTo(main_id);
		ca.andTypeEqualTo(JpaAttachment.Type.ht_fj.ordinal());
		return attachmentMapper.selectByExample(example);
	}
	//查询小样
	public List<Attachment> queryXiaoY(Principal principal, int main_id,String type) {
		AttachmentExample example =new AttachmentExample();
		AttachmentExample.Criteria ca=example.createCriteria();
		ca.andMainIdEqualTo(main_id);
		if(StringUtils.equals(type, "xiaoy")){
			ca.andTypeEqualTo(JpaAttachment.Type.xiaoY.ordinal());
		}else if(StringUtils.equals(type, "workp")){
			ca.andTypeEqualTo(JpaAttachment.Type.workP.ordinal());
		}
		return attachmentMapper.selectByExample(example);
	}
	
	//查询资质附件
	public List<Attachment> queryQua(Principal principal, int main_id) {
		AttachmentExample example =new AttachmentExample();
		AttachmentExample.Criteria ca=example.createCriteria();
		ca.andMainIdEqualTo(main_id);
		ca.andTypeEqualTo(JpaAttachment.Type.u_fj.ordinal());
		return attachmentMapper.selectByExample(example);
	}

	public Attachment selectById(int id) {
		
		
		return attachmentMapper.selectByPrimaryKey(id);
	}

}
