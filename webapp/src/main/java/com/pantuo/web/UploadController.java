package com.pantuo.web;

import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

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
@RequestMapping(produces = "application/json;charset=utf-8")
public class UploadController {

	 
	@RequestMapping(value = "upload_enter", method = RequestMethod.GET)
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
	public void upload(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws IOException {
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
	}

}
