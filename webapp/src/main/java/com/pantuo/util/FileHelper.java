package com.pantuo.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang3.StringUtils;

public class FileHelper {

	static AtomicLong p = new AtomicLong(System.currentTimeMillis());

	/**
	 * 
	 * 产生0-32/0-8/fileName文件
	 *
	 * @param user
	 * @param upload_Dir
	 * @param upload_FileName
	 * @return
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	public static Pair<String, String> getUploadFileName(String upload_Dir, String upload_FileName) {
		long fristBucket = p.incrementAndGet() % 32;
		long bucket = Math.abs(upload_FileName.hashCode()) % 8;
		String deskRealyName = fristBucket + "/" + bucket + "/" + upload_FileName;
		GlobalMethods.createDir(upload_Dir + "/" + fristBucket + "/" + bucket);
		return new Pair<String, String>(upload_Dir + "/" + deskRealyName, deskRealyName);
	}

	public static String getFileDate(String str, int index) {

		return str.contains("/") ? str.split(File.separator)[index] : str.split("\\\\")[index];
	}

	public static String getFileDate1(String str, int index) {
		String[] tmp = str.replaceAll("\\\\", "/").split("/");
		return tmp[index];
	}

	public static String fId(String id) {
		return id.replaceAll("ID", "");
	}

	public static String getFileAlias(String args) {
		return args.contains(".") ? (args.substring(args.lastIndexOf("."))) : "";
	}

	public static File buildDir(String buildPath) {
		File parsentPath = new File(buildPath);
		if (!parsentPath.exists()) {
			parsentPath.mkdirs();
		}
		return parsentPath;
	}

	public static void readInputStream(InputStream inStream, File file) throws Exception {

		OutputStream os = null;
		try {
			os = new FileOutputStream(file);
			byte buffer[] = new byte[50 * 1024];

			int len = 0;
			while ((len = inStream.read(buffer)) != -1) {
				os.write(buffer, 0, len);

			}

			os.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				os.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 获取文件扩展名
	 * @param file
	 * @return
	 */
	public static String getFileExtension(String fileName, boolean haveSlide) {
		String r = StringUtils.EMPTY;
		if (StringUtils.isNoneBlank(fileName)) {
			if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
				r = fileName.substring(fileName.lastIndexOf(".") + (haveSlide ? 0 : 1));
			}
		}
		return r;
	}
 
	public static void scanDirectory(HashSet<String> existFiles, File sPath, String filter) {
		File[] files = sPath.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				String v = files[i].getPath();
				existFiles.add((v.split(filter)[1]));
			} else {
				scanDirectory(existFiles, files[i], filter);
			}
		}
	}
}
