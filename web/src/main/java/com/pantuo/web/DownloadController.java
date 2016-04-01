package com.pantuo.web;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.pantuo.mybatis.domain.Attachment;
import com.pantuo.service.AttachmentService;
import com.pantuo.util.FileOperateUtil;

@Controller
@RequestMapping("/downloadFile")
public class DownloadController {

	@Autowired
	AttachmentService attachmentService;

	@RequestMapping("/{userid}/{docid}")
	public ModelAndView download2(@PathVariable("userid") String userid, @PathVariable("docid") int docid,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		String contentType = "application/octet-stream";
		try {
			Attachment at = attachmentService.selectById(docid);
			if (at != null) {
				FileOperateUtil.download(request, response, at.getUrl(), contentType, at.getName());
			}
		} catch (Exception e) {
		}
		return null;
	}

	@RequestMapping("download")
	public ResponseEntity<byte[]> download(HttpServletRequest request) throws IOException {
		String path = request.getSession().getServletContext().getRealPath(com.pantuo.util.Constants.FILE_UPLOAD_DIR);
		path += "/9/1/屏幕快照 2015-04-09 下午11.02.11.png";
		File file = new File(path);
		HttpHeaders headers = new HttpHeaders();
		String fileName = new String("你好.png".getBytes("UTF-8"), "iso-8859-1");//为了解决中文名称乱码问题  
		headers.setContentDispositionFormData("attachment", fileName);
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
	}
}