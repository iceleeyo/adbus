package com.pantuo.web.upload;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * 
 * <b><code>CustomMultipartResolver</code></b>
 * <p>
 * Comment here.
 * </p>
 * <b>Creation Time:</b> 2015年5月12日 下午7:28:20
 * @author impanxh@gmail.com
 * @since pantuotech 1.0-SNAPSHOT
 */
public class CustomMultipartResolver extends CommonsMultipartResolver {
	//@Autowired
	//private FileUploadProgressListener progressListener;

	public void setFileUploadProgressListener(FileUploadProgressListener progressListener) {
		//this.progressListener = progressListener;
	}

	private static Logger log = LoggerFactory.getLogger(CustomMultipartResolver.class);

	public CustomMultipartResolver() {
		super();
	}

	public CustomMultipartResolver(ServletContext servletContext) {
		super(servletContext);
	}

	@Override
	@SuppressWarnings("unchecked")
	public MultipartParsingResult parseRequest(HttpServletRequest request) throws MultipartException {
		//log.info("this: " + this.toString()+ " " +progressListener.toString());
		String encoding = determineEncoding(request);
		FileUpload fileUpload = prepareFileUpload(encoding);
		//progressListener.setSession(request.getSession());
		//fileUpload.setProgressListener(progressListener);

		FileUploadProgressListener r = new FileUploadProgressListener(request.getSession());
		fileUpload.setProgressListener(r);
		try {
			List<FileItem> fileItems = ((ServletFileUpload) fileUpload).parseRequest(request);
			return parseFileItems(fileItems, encoding);
		} catch (FileUploadBase.SizeLimitExceededException ex) {
			throw new MaxUploadSizeExceededException(fileUpload.getSizeMax(), ex);
		} catch (FileUploadException ex) {
			throw new MultipartException("Could not parse multipart servlet request", ex);
		}
	}
}