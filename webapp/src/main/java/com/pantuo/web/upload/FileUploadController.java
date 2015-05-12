package com.pantuo.web.upload;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 * <b><code>FileUploadController</code></b>
 * <p>
 *
 * </p>
 * <b>Creation Time:</b> 2015年5月12日 下午7:28:06
 * @author impanxh@gmail.com
 * @since pantuotech 1.0-SNAPSHOT
 */
@Controller
public class FileUploadController {

	/** 
	 * process 获取进度 
	 * @param request 
	 * @param response 
	 * @return 
	 * @throws Exception 
	 */
	@RequestMapping(value = "/upload/process", method = RequestMethod.GET)
	@ResponseBody
	public ProcessInfo jecprocess(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return (ProcessInfo) request.getSession().getAttribute("proInfo");
	}
}
