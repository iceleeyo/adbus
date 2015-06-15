package com.pantuo.service;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.pantuo.dao.pojo.JpaAttachment;
import com.pantuo.mybatis.domain.Attachment;
import com.pantuo.util.BusinessException;
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
	public void saveAttachment(HttpServletRequest request, String user_id, int main_id, JpaAttachment.Type file_type,String description)
			throws BusinessException;
	public void  upInvoiceAttachments(HttpServletRequest request, String user_id, int main_id, JpaAttachment.Type file_type,String description)
			throws BusinessException;
	/**
	 * 
	 * 根据文件id删除文件
	 *
	 * @param request
	 * @param user_id
	 * @param att_id
	 * @return
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	public Pair<Boolean, String> removeAttachment(HttpServletRequest request,String user_id, int att_id);
	
	/**
	 * 
	 * 查素材下面的文件
	 *
	 * @param main_id
	 * @return
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	//public List<Attachment> queryFile(Principal principal, int main_id);
	
	
	
	public Attachment selectById(int id);
	public List<Attachment> queryQua(Principal principal, int main_id);
	public List<Attachment> queryinvoiceF(Principal principal, int id);
	public List<Attachment> queryAllFile(Principal principal, int main_id);
	public List<Attachment> querysupFile(Principal principal, int main_id);
	public List<Attachment> queryContracF(Principal principal, int main_id);

}
