package com.pantuo.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.pantuo.service.impl.BusLineCheckServiceImpl;
/**
 * 
 * <b><code>IOTools</code></b>
 * <p>
 *  
 * </p>
 * <b>Creation Time:</b> 2013-4-24 下午3:04:09
 * @author Panxh
 * @since PanTuo 1.0-SNAPSHOT
 */
public class IOTools {
	private static Logger LOGGER = LoggerFactory.getLogger(IOTools.class);

	private IOTools() {
		throw new UnsupportedOperationException("Utility class");
	}

	/**
	 * Reads an inputstream to String
	 * @param in
	 * @param charsetName the charset name
	 * @return the result from reading the stream
	 * @throws java.io.IOException
	 */
	public static String inputSreamToString(final InputStream in, final String charsetName) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] b = new byte[4096];
		for (int n; (n = in.read(b)) != -1;) {
			bos.write(b, 0, n);
		}
		if (charsetName != null) {
			return bos.toString(charsetName);
		} else {
			return bos.toString();
		}
	}

	public static String readerToString(final Reader in) throws IOException {
		StringBuilder out = new StringBuilder();
		char[] b = new char[4096];
		for (int n; (n = in.read(b)) != -1;) {
			out.append(new String(b, 0, n));
		}
		return out.toString();
	}

	/**
	 * Silently closes a Closeable, logs exceptions
	 * 
	 * @param closeable
	 */
	public static void close(final Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			}
		} else {
			LOGGER.warn("Trying to close null Closeable");
		}
	}

	public static void writeToFile(Reader input, File output) throws IOException {
		FileWriter fw = new FileWriter(output);
		char[] b = new char[4096];
		for (int n; (n = input.read(b)) != -1;) {
			fw.write(b, 0, n);
		}
		close(fw);
	}

 
	private static final int LEN_BUFFER = 2048;

	public static void unzip(String zipFilePath, String unzipFilePath) throws IOException {
		File rootFile = new File(unzipFilePath);
		rootFile.mkdirs();

		ZipInputStream input = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFilePath)));
		ZipEntry entry = null;

		//Zip文件将里面的每个文件都作为一个ZipEntry, 父目录和子文件为两个单独的entry
		while ((entry = input.getNextEntry()) != null) {
			File tmpFile = new File(unzipFilePath + "/" + entry.getName());
			tmpFile.getParentFile().mkdirs();

			if (entry.isDirectory()) {
				tmpFile.mkdir();
			} else {
				BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(tmpFile));
				byte[] datas = new byte[LEN_BUFFER];
				int count;
				while ((count = input.read(datas)) != -1) {
					output.write(datas, 0, count);
				}
				output.close();
			}

			input.closeEntry();
		}

		input.close();
	}

	public static void main(String[] args) {
		try {
			IOTools.unzip("C:\\Users\\impanxh\\Desktop\\news.zip", "C:/Users/impanxh/Desktop/add/");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}