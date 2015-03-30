package com.pantuo.service;

import javax.servlet.http.HttpServletRequest;

import com.pantuo.util.BusinessException;

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
public interface AttachmentService {
	/**
	 * 
	 * 保存文件接口
	 *
	 * @param request
	 * @param user_id
	 * @param main_id
	 * @param file_type
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	public void saveAttachment(HttpServletRequest request, String user_id, int main_id, String file_type)
			throws BusinessException;

}
