package com.pantuo.util;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

public class GlobalMethods {
	private static final Logger log = Logger.getLogger(GlobalMethods.class);
	private static final int MAX_FILE_SIZE = 10240;


    public static ThreadLocal<SimpleDateFormat> sdf = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            return sdf;
        }
    };

    //	static {
	//		initDirectory();
	//	}

	//    private final static void initDirectory() {
	//    	String path = getAbosluteSaveFilePath(Constants.getSaveDirectory(
	//				Constants.SYS_TYPE_ADVT), Constants.FILE_FUNC_TYPE_CREATE);
	//		if (createDir(path)) {
	//    		log.debug("Initialized file saving directory " + path);
	//    	} else {
	//    		log.error("Failed to initialize file saving directory " + path);
	//    	}
	//		path = getAbosluteSaveFilePath(Constants.getSaveDirectory(
	//				Constants.SYS_TYPE_ADVT), Constants.FILE_FUNC_TYPE_REMOVE);
	//		if (createDir(path)) {
	//    		log.debug("Initialized file saving directory " + path);
	//    	} else {
	//    		log.error("Failed to initialize file saving directory " + path);
	//    	}
	//	}

	public static boolean createDir(String destDirName) {
		File dir = new File(destDirName);
		if (dir.exists()) {
			log.debug("mkdir - " + destDirName + " existed.");
			return true;
		}
		if (!destDirName.endsWith(File.separator)) {
			destDirName = destDirName + File.separator;
		}

		if (dir.mkdirs()) {
			return true;
		} else {
			log.error("mkdir failed - " + destDirName);
			return false;
		}
	}

	public static boolean removeDir(String dir) {
		File dirFile = new File(dir);
		if (dirFile.exists()) {
			if (dirFile.delete()) {
				return true;
			} else {
				return false;
			}
		} else {
			log.debug("remve dir - " + dir + " is not existed.");
			return true;
		}
	}

	/**
	 * 删除目录（文件夹）以及目录下的文件
	 * @param   sPath 被删除目录的文件路径
	 * @return  目录删除成功返回true，否则返回false
	 */
	public static boolean deleteDirectory(String sPath) {
		//如果sPath不以文件分隔符结尾，自动添加文件分隔符
		if (!sPath.endsWith(File.separator)) {
			sPath = sPath + File.separator;
		}
		File dirFile = new File(sPath);
		//如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		boolean flag = true;
		//删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			//删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			} //删除子目录
			else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag)
			return false;
		//删除当前目录
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Add n Month on specified date
	 * @param date format - "yyyy/MM/dd"
	 * @param n
	 * @return result date (format - "yyyy/MM/dd")
	 */
	public static String addMonth(String date, int n) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(sdf.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		calendar.add(Calendar.MONTH, n);
		return sdf.format(calendar.getTime());
	}

	/**
	 * Add n Day on specified date
	 * @param date format - "yyyy/MM/dd"
	 * @param n
	 * @return result date (format - "yyyy/MM/dd")
	 */
	public static String addDay(String date, int n) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(sdf.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		calendar.add(Calendar.DATE, n);
		return sdf.format(calendar.getTime());
	}

	public static int substractMonth(String dateStr1, String dateStr2) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		int ret = 0;
		try {
			Calendar cal1 = Calendar.getInstance();
			cal1.setTime(sdf.parse(dateStr1));
			//			cal1.get(Calendar.YEAR);
			//			cal1.get(Calendar.MONTH);

			Calendar cal2 = Calendar.getInstance();
			cal2.setTime(sdf.parse(dateStr2));
			//			cal2.get(Calendar.YEAR);
			//			cal2.get(Calendar.MONTH);

			ret = (cal2.get(Calendar.YEAR) - cal1.get(Calendar.YEAR)) * 12
					+ (cal2.get(Calendar.MONTH) - cal1.get(Calendar.MONTH));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static long substractDate(String dateStr1, String dateStr2) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		long ret = 0;
		try {
			Calendar cal1 = Calendar.getInstance();
			cal1.setTime(sdf.parse(dateStr1));

			Calendar cal2 = Calendar.getInstance();
			cal2.setTime(sdf.parse(dateStr2));

			ret = (cal2.getTime().getTime() - cal1.getTime().getTime()) / (24 * 60 * 60 * 1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static String getToday() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		return sdf.format(calendar.getTime());
	}

	public static boolean saveFile(File file, String filePathName) {
		if (file == null || filePathName == null || filePathName.trim().isEmpty())
			return true;

		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(filePathName));
			bis = new BufferedInputStream(new FileInputStream(file));
			byte[] buffer = new byte[MAX_FILE_SIZE];
			int len = 0;
			while ((len = bis.read(buffer)) > 0) {
				bos.write(buffer, 0, len);
			}
			return true;
		} catch (Exception e) {
			log.error("Faile to save as " + file.getAbsolutePath() + " to " + filePathName);
			e.printStackTrace();
		} finally {
			close(bos, bis);
		}
		return false;
	}

	public static void close(BufferedOutputStream bos, BufferedInputStream bis) {
		if (bis != null) {
			try {
				bis.close();
			} catch (IOException e) {
				System.out.println("BufferedInputStream Closure Failed");
				e.printStackTrace();
			}
		}
		if (bos != null) {
			try {
				bos.close();
			} catch (IOException e) {
				System.out.println("BufferedOutputStream Closure Failed");
				e.printStackTrace();
			}
		}
	}

	public static boolean deleteFile(String filePath) {
		if (filePath == null || filePath.trim().isEmpty()) {
			return true;
		}
		try {
			log.debug("Delete File: " + filePath);
			java.io.File delFile = new java.io.File(filePath);
			delFile.delete();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static String md5Encrypted(byte[] source) {
		String s = null;
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			md.update(source);
			byte tmp[] = md.digest();
			char str[] = new char[16 * 2];
			int k = 0;
			for (int i = 0; i < 16; i++) {
				byte byte0 = tmp[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			s = new String(str);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			s = new String(source);
		}
		return s;
	}

	public static String encryptFileName(String name) {
		// TODO: in test mode, no encrypted
		return name;
		//return md5Encrypted(name.getBytes());
	}

	public static String generateFileName(String originalName, String id) {
		String suffix = "";
		if (originalName == null || originalName.isEmpty()) {
			return null;
		}

		int pos = originalName.lastIndexOf(".");

		if (pos > 0 && pos < originalName.length() - 1) {
			suffix = originalName.substring(pos);
		}
		StringBuilder sb = new StringBuilder();
		sb.append(encryptFileName(id));
		sb.append("_");
		sb.append(Calendar.getInstance().getTimeInMillis());
		sb.append(suffix);
		return sb.toString();
	}
 

	public static boolean copyFile(String sourceFile, String targetFile) {
		BufferedWriter bw = null;
		BufferedReader br = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targetFile), "utf-8"));
			br = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFile), "utf-8"));

			String line = null;
			while ((line = br.readLine()) != null) {
				bw.write(line + "\n");
			}
			bw.flush();
			return true;
		} catch (Exception ex) {
			log.error(ex.getLocalizedMessage());
			ex.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				bw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public static boolean copyBinaryFile(String sourceFile, String targetFile) {
		FileInputStream fis = null;
		FileOutputStream fos = null;
		FileChannel fc1 = null;
		FileChannel fc2 = null;
		try {
			fis = new FileInputStream(sourceFile);
			fos = new FileOutputStream(targetFile);

			fc1 = fis.getChannel();
			fc2 = fos.getChannel();
			fc2.transferFrom(fc1, 0, fc1.size());
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (fc1 != null)
					fc1.close();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			try {
				if (fc2 != null)
					fc2.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				if (fis != null)
					fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (fos != null)
					fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	//    public static boolean copyBinaryFile(String sourceFile, String targetFile) {
	//		BufferedOutputStream bw = null;
	//		BufferedInputStream br = null;
	//		try {
	//			bw = new BufferedOutputStream(new FileOutputStream(targetFile));
	//			
	//			br = new BufferedInputStream(new FileInputStream(sourceFile));
	//			byte[] bt = new byte[1024];
	//			int count = 0;
	//			while ((count = br.read(bt)) != -1) {
	//				bw.write(bt, 0, bt.length);
	//			}
	//			bw.flush();
	//			return true;
	//		} catch (Exception ex) {
	//			log.error(ex.getLocalizedMessage());
	//			ex.printStackTrace();
	//		} finally {
	//			try {
	//				br.close();
	//			} catch (Exception e) {
	//				e.printStackTrace();
	//			}
	//			try {
	//				bw.close();
	//			} catch (Exception e) {
	//				e.printStackTrace();
	//			}
	//		}
	//		return false;
	//	}

	//	public static boolean moveFile(String srcFile, String destPath) {
	//		// File (or directory) to be moved
	//		File file = new File(srcFile);
	//
	//		// Destination directory
	//		File dir = new File(destPath);
	//
	//		// Move file to new directory
	//		boolean success = file.renameTo(new File(dir, file.getName()));
	//
	//		log.debug("Move File " + srcFile + " To " + destPath);
	//		if (!success) {
	//			log.debug("Failed to move file");
	//		}
	//		return success;
	//	}

	public static void moveFile(String srcFile, String destFile) {
		log.debug(System.getProperty("os.name"));
		String cmd;
		if (System.getProperty("os.name").indexOf("window") > -1
				|| System.getProperty("os.name").indexOf("Window") > -1) {
			cmd = "move " + srcFile + " " + destFile; // Windows
		} else {
			cmd = "mv " + srcFile + " " + destFile; //Linux
		}
		log.debug(cmd);
		executeCommand(cmd);
	}

	public static void copyDirV2(String srcDir, String targetDir) {
		log.debug(System.getProperty("os.name"));
		String cmd;
		if (System.getProperty("os.name").indexOf("window") > -1
				|| System.getProperty("os.name").indexOf("Window") > -1) {
			cmd = "xcopy " + srcDir + "*.* " + targetDir + " /S"; // Windows
		} else {
			cmd = "cp -r " + srcDir + "*.* " + targetDir; //Linux
		}
		log.debug(cmd);
		executeCommand(cmd);
	}

	public static boolean copyDir(String srcDir, String targetDir) {
		long start = System.currentTimeMillis();
		boolean ret = true;
		File target = new File(targetDir);
		if (!target.exists()) {
			if (!createDir(targetDir)) {
				log.error("copyDirV2 - Create dir failed. " + targetDir);
				ret = false;
			}
		}
		if (ret) {
			File source = new File(srcDir);
			File[] allFile = source.listFiles();
			if (allFile != null) {
				for (File file : allFile) {
					if (file.isDirectory()) {
						if (!copyDir(file.getPath(), targetDir + File.separator + file.getName())) {
							log.error("copyDirV2 - Copy dir failed. " + file.getPath() + " to "
									+ (targetDir + File.separator + file.getName()));
							ret = false;
							break;
						}
					} else {
						if (!copyBinaryFile(file.toString(), targetDir + File.separator + file.getName())) {
							log.error("copyDirV2 - Copy file failed. " + file.toString() + " to "
									+ (targetDir + File.separator + file.getName()));
							ret = false;
							break;
						}
					}
				}
			} else {
				log.debug("Directory " + srcDir + " is empty.");
			}
		}
		long end = System.currentTimeMillis();
		log.debug("copyDirV2 takes " + (end - start) + " ms to copy " + srcDir + " to " + targetDir);
		return true;
	}

	public static boolean copyDir(String srcDir, String targetDir, String filterPath) {
		long start = System.currentTimeMillis();
		boolean ret = true;
		File target = new File(targetDir);
		if (!target.exists()) {
			if (!createDir(targetDir)) {
				log.error("copyDirV2 - Create dir failed. " + targetDir);
				ret = false;
			}
		}
		if (ret) {
			File source = new File(srcDir);
			File[] allFile = source.listFiles();
			if (allFile != null) {
				for (File file : allFile) {
					if (file.isDirectory()) {
						if (!file.getPath().endsWith(filterPath)) {
							if (!copyDir(file.getPath(), targetDir + File.separator + file.getName(),filterPath)) {
								log.error("copyDirV2 - Copy dir failed. " + file.getPath() + " to "
										+ (targetDir + File.separator + file.getName()));
								ret = false;
								break;
							}

						}
					} else {
						if (!copyBinaryFile(file.toString(), targetDir + File.separator + file.getName())) {
							log.error("copyDirV2 - Copy file failed. " + file.toString() + " to "
									+ (targetDir + File.separator + file.getName()));
							ret = false;
							break;
						}
					}
				}
			} else {
				log.debug("Directory " + srcDir + " is empty.");
			}
		}
		long end = System.currentTimeMillis();
		log.debug("copyDirV2 takes " + (end - start) + " ms to copy " + srcDir + " to " + targetDir);
		return true;
	}

	public static void executeCommand(String cmd) {
		Runtime run = Runtime.getRuntime();
		try {
			Process p = run.exec(cmd);
			BufferedInputStream in = new BufferedInputStream(p.getInputStream());
			BufferedReader inBr = new BufferedReader(new InputStreamReader(in));
			String lineStr;
			while ((lineStr = inBr.readLine()) != null)
				log.debug(lineStr);
			if (p.waitFor() != 0) {
				if (p.exitValue() == 1)
					log.error("Command failed: " + cmd);
			}
			inBr.close();
			in.close();
		} catch (Exception e) {
			log.error("Execute command failed - " + cmd, e);
		}
	}

	public static boolean unpackPageDir(String zipFilePath, String base) throws FileNotFoundException, IOException {
		CheckedInputStream csumi = new CheckedInputStream(new FileInputStream(zipFilePath), new CRC32());
		ZipInputStream in2 = new ZipInputStream(csumi);
		BufferedInputStream bi = new BufferedInputStream(in2);
		java.util.zip.ZipEntry ze;

		File decompressDirFile = new File(base);
		if (!decompressDirFile.exists()) {
			decompressDirFile.mkdirs();
		}

		// iterate every item in package
		while ((ze = in2.getNextEntry()) != null) {
			String entryName = ze.getName();
			if (ze.isDirectory()) {
				decompressDirFile = new File(base + "/" + entryName);
				if (!decompressDirFile.exists()) {
					decompressDirFile.mkdirs();
				}
			} else {
				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(base + "/" + entryName));
				byte[] buffer = new byte[1024];
				int readCount = bi.read(buffer);

				while (readCount != -1) {
					bos.write(buffer, 0, readCount);
					readCount = bi.read(buffer);
				}
				bos.close();
			}
		}
		bi.close();
		return false;
	}

	public static void zip(ZipOutputStream out, File f, String base) throws Exception {
		if (f.isDirectory()) {
			File[] fl = f.listFiles();
			out.putNextEntry(new ZipEntry(base + "/"));
			base = base.length() == 0 ? "" : base + "/";
			for (int i = 0; i < fl.length; i++) {
				zip(out, fl[i], base + fl[i].getName());
			}
		} else {
			out.putNextEntry(new ZipEntry(base));
			FileInputStream in = new FileInputStream(f);
			byte[] bs = new byte[10240];
			int b;
			while ((b = in.read(bs)) != -1) {
				out.write(bs, 0, b);
			}
			in.close();
		}
	}

	public static boolean isImageFile(File file) {
		try {
			BufferedImage image = ImageIO.read(file);
			if (image != null) {
				return true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error(ex);
		}
		return false;
	}

	/**
	 * Read content of file to a string
	 * @param fileName
	 * @return
	 */
	public static String readFileToString(String fileName) {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "utf-8"));

			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (Exception ex) {
			log.error(ex.getLocalizedMessage());
			ex.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	
	
	public static String readFileToStringBr(String fileName) {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "utf-8"));

			String line = null;
			
			while ((line = br.readLine()) != null) {
				sb.append("<p>" + line + "</p>");
			}
		} catch (Exception ex) {
			log.error(ex.getLocalizedMessage());
			ex.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	
	public static boolean writeToFile(String content, String file) {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
			bw.write(content);
			bw.flush();
			return true;
		} catch (Exception ex) {
			log.error(ex.getLocalizedMessage());
			ex.printStackTrace();
		} finally {
			try {
				bw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
}
