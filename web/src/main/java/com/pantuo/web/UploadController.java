package com.pantuo.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.pantuo.dao.pojo.JpaAttachment;
import com.pantuo.service.AttachmentService;
import com.pantuo.util.BusinessException;
import com.pantuo.service.security.Request;

/**
 * 
 * <b><code>UploadController</code></b>
 * <p>
 * 抄自网络 测试下 文件上传 具体视业务情况再处理
 * </p>
 * <b>Creation Time:</b> 2015年3月18日 下午5:09:14
 * @author impanxh@gmail.com
 * @since pantuotech 1.0-SNAPSHOT
 */
@Controller
@RequestMapping(produces = "application/json;charset=utf-8", value = "/upload")
public class UploadController {
	@Autowired
	AttachmentService attachmentService;
	 
	@RequestMapping(value = "/upload_enter", method = RequestMethod.GET)
	public String enter(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws IOException {
		// 进入下载界面  
		return "upload";

	}

	 /**
	  * 
	  * Comment here.
	  *
	  * @param request
	  * @param response
	  * @param model
	  * @throws IOException
	  * @since pantuotech 1.0-SNAPSHOT
	  */
	@RequestMapping(value = "storeFile", method = RequestMethod.POST)
	@ResponseBody
	public String  upload(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws IOException {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		// 得到上传的文件  
		MultipartFile mFile = multipartRequest.getFile("file");
		// 得到上传服务器的路径  
		String path = request.getSession().getServletContext().getRealPath("/WEB-INF/upload_temp/");
		// 得到上传的文件的文件名  
		String filename = mFile.getOriginalFilename();
		byte[] b = mFile.getBytes();
		path += "/" + filename;
		// 文件流写到服务器端  
		FileOutputStream outputStream = new FileOutputStream(path);
		outputStream.write(b);
		outputStream.close();
		return path;
	}
	@RequestMapping(value = "savePayvoucher/{orderid}", method = RequestMethod.POST)
	@ResponseBody
	public String  savePayvoucher(HttpServletRequest request, Principal principal,@PathVariable("orderid") int orderid,
			ModelMap model) throws IOException, BusinessException {
		JpaAttachment.Type fileType=JpaAttachment.Type.payvoucher;
		String type=request.getParameter("filetype");
		if(StringUtils.isNotBlank(type)){
			fileType=JpaAttachment.Type.valueOf(type);
		}
		return attachmentService.savePayvoucher(request, Request.getUserId(principal), orderid, fileType, null);
	}
	@RequestMapping(value = "saveSimpleFile", method = RequestMethod.POST)
	@ResponseBody
	public String  saveSimpleFile(HttpServletRequest request,HttpServletResponse response,Principal principal,
			ModelMap model) throws IOException, BusinessException {
		 response.setHeader("Access-Control-Allow-Origin", "*");
		return attachmentService.saveAttachmentSimple(request);
	}
	 @RequestMapping(value = "upload2" , method = RequestMethod.POST )  
	    public String upload2(HttpServletRequest request,HttpServletResponse response) throws IllegalStateException, IOException {  
	        //创建一个通用的多部分解析器  
	        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());  
	        //判断 request 是否有文件上传,即多部分请求  
	       System.out.println( request.getParameter("a1"));
	        if(multipartResolver.isMultipart(request)){  
	            //转换成多部分request    
	            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;  
	            //取得request中的所有文件名  
	            Iterator<String> iter = multiRequest.getFileNames();  
	            while(iter.hasNext()){  
	                MultipartFile file = multiRequest.getFile(iter.next());  
	                if(file != null){  
	                    String oriFileName = file.getOriginalFilename();  
	                    if(oriFileName.trim() !=""){  
	                        String fileName = "demoUpload" + oriFileName;  
	                        String path = "D:/temp/" + fileName;  
	                        File localFile = new File(path);  
	                        file.transferTo(localFile);  
	                    }  
	                }  
	            }  
	              
	        }  
	        return "success";  
	    }  
}
