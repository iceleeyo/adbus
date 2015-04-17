package com.pantuo.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FileOperateUtil {

	 /**
     * 下载
     * 
     * @param request
     * @param response
     * @param storeName
     * @param contentType
     * @param realName
     * @throws Exception
     */
    public static void download(HttpServletRequest request,
            HttpServletResponse response, String storeName, String contentType,
            String realName) throws Exception {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        String ctxPath = request.getSession().getServletContext()
                .getRealPath(com.pantuo.util.Constants.FILE_UPLOAD_DIR).replaceAll("WEB-INF", "");
        String downLoadPath = ctxPath +File.separatorChar+ storeName;
        long fileLength = new File(downLoadPath).length();

        response.setContentType(contentType);
        response.setHeader("Content-disposition", "attachment; filename="
                + new String(realName.getBytes("utf-8"), "ISO8859-1"));
        response.setHeader("Content-Length", String.valueOf(fileLength));

        bis = new BufferedInputStream(new FileInputStream(downLoadPath));
        bos = new BufferedOutputStream(response.getOutputStream());
        byte[] buff = new byte[2048];
        int bytesRead;
        while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
            bos.write(buff, 0, bytesRead);
        }
        bis.close();
        bos.close();
    }

}
