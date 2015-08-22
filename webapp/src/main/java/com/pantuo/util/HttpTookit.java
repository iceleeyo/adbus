package com.pantuo.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.regex.Matcher;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.PatternMatcherInput;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

import freemarker.template.Configuration;

/**
 * 
 */
public class HttpTookit {

	public static Log log = LogFactory.getLog(HttpTookit.class);
	public Configuration cfg;
	public static final int DEFAULT_CONNECTIONTIMEOUT = 2000;
	public static final int DEFAULT_SOTIMEOUT = 2000;

	public static String doGet(String url, String queryString) throws BusinessException {
		return doGet(url, queryString, null, DEFAULT_SOTIMEOUT);
	}

	public static String doGet(String url, String queryString, String charset, int soTimeout) throws BusinessException {
		return doGet(url, queryString, charset, DEFAULT_CONNECTIONTIMEOUT, soTimeout);
	}

	static Pattern pattern = null;
	static {
		try {
			PatternCompiler compiler = new Perl5Compiler();
			pattern = compiler.compile("^[A-Za-z_\\d\u4e00-\u9fa5]+$", Perl5Compiler.READ_ONLY_MASK
					| Perl5Compiler.CASE_INSENSITIVE_MASK | Perl5Compiler.MULTILINE_MASK);

		} catch (MalformedPatternException e) {
			throw new RuntimeException("init oro pattern exception ! ", e);
		}
	}

	public static boolean isContainChinese(String str) {
		java.util.regex.Pattern p = java.util.regex.Pattern.compile("[\u4e00-\u9fa5]");
		Matcher m = p.matcher(str);
		return m.find();
	}

	public static boolean regexNormalChar(String regexString) {
		PatternMatcher matcher = new Perl5Matcher();
		PatternMatcherInput input = new PatternMatcherInput(regexString);
		return matcher.matches(input, pattern);
	}

	//更具地址获取JSON String字符窜
	public static String doGet(String url, String queryString, String charset, int defaultConnectionTimeout,
			int soTimeout) throws BusinessException {
		String response = null;
		HttpMethod method = null;
		Reader reader = null;
		try {
			HttpClient client = new HttpClient();
			method = new GetMethod(url);
			client.getHttpConnectionManager().getParams().setConnectionTimeout(defaultConnectionTimeout);
			client.getHttpConnectionManager().getParams().setSoTimeout(soTimeout);
			if (null != queryString)
				method.setQueryString(URIUtil.encodeQuery(queryString));
			client.executeMethod(method);
			if (method.getStatusCode() == HttpStatus.SC_OK) {
				if (charset == null) {
					response = method.getResponseBodyAsString();
					// reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
					//response = IOTools.readerToString(reader);
				} else {
					reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(), charset));
					response = IOTools.readerToString(reader);
				}
				// response = method.getResponseBodyAsString();
			}
		} catch (URIException e) {
			log.error("  + queryString +   ", e);
		} catch (IOException e) {
			log.error("Url Address" + url + " error", e);
			if (e instanceof SocketTimeoutException) {
				throw new BusinessException("异常:对接爱WIFI平台网络超时");
			}
		} catch (Exception e) {
			log.error(e);
		} finally {
			IOTools.close(reader);
			if (method != null) {
				method.releaseConnection();
			}
		}
		return response;
	}
	public static byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024 * 10];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		inStream.close();
		return outStream.toByteArray();
	}

	//更具地址获取JSON String字符窜
	public static String doPost(String url, String context, String charset, int defaultConnectionTimeout, int soTimeout)
			throws BusinessException {
		String response = null;
		PostMethod method = null;
		Reader reader = null;
		try {
			HttpClient client = new HttpClient();
			method = new PostMethod(url);
			method.setRequestHeader("content-type", "application/json");
			client.getHttpConnectionManager().getParams().setConnectionTimeout(defaultConnectionTimeout);
			client.getHttpConnectionManager().getParams().setSoTimeout(soTimeout);
			StringRequestEntity requestEntity = new StringRequestEntity(context, "application/json", "UTF-8");
			method.setRequestEntity(requestEntity);
			client.executeMethod(method);
			if (method.getStatusCode() == HttpStatus.SC_OK) {
				if (charset == null) {
					response = method.getResponseBodyAsString();
					// reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
					//response = IOTools.readerToString(reader);
				} else {
					reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(), charset));
					response = IOTools.readerToString(reader);
				}
				// response = method.getResponseBodyAsString();
			}
		} catch (URIException e) {
			log.error("  + queryString +   ", e);
		} catch (IOException e) {
			String error = "异常:对接DSP平台网络超时," + " Url Address " + url + " error";
			log.error(error, e);
			if (e instanceof SocketTimeoutException) {
				throw new BusinessException(error, e);
			}
			if (e instanceof ConnectTimeoutException || e instanceof java.net.ConnectException) {
				throw new BusinessException(error, e);
			}
		} catch (Exception e) {
			log.error(e);
		} finally {
			IOTools.close(reader);
			if (method != null) {
				method.releaseConnection();
			}
		}
		return response;
	}

	public static byte[] getImageFromNetByUrl(String strUrl) {
		try {
			URL url = new URL(strUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(10 * 1000);
			conn.connect();
			InputStream inStream = conn.getInputStream();// 通过输入流获取图片数据
			byte[] btImg = readInputStream(inStream);// 得到图片的二进制数据
			return btImg;
		} catch (Exception e) {
		}
		return null;
	}

	public static InputStream getInPutStreamFromNetByUrl(String strUrl) {
		try {
			URL url = new URL(strUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(10 * 1000);
			conn.connect();
			InputStream inStream = conn.getInputStream();// 通过输入流获取图片数据
			return inStream;
		} catch (Exception e) {

		}
		return null;
	}

	
	public static void main(String[] args) {
//		System.out.println(regexNormalChar("你好"));
//		System.out.println(isContainChinese("你好"));
//		System.out.println(regexNormalChar("abc1731939你好"));
//	   String imgurl = "http://pic1.sc.chinaz.com/Files/pic/pic9/201502/apic9265_s.jpg";
//	   String checkMd5 = GlobalMethods.md5Encrypted(HttpTookit.getImageFromNetByUrl(imgurl));
//	   String dspsource = "mangguo";
//	   System.out.println(HttpTookit.ImpDspSrc(imgurl, checkMd5, dspsource));				
				
				
	}

}