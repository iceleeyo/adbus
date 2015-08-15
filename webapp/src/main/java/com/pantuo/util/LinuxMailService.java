package com.pantuo.util;

import java.io.IOException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pantuo.service.impl.MailServiceImpl;
/**
 * 
 * <b><code>LinuxMailService</code></b>
 * <p>
 * linux 上发邮件
 * </p>
 * <b>Creation Time:</b> 2015年7月23日 下午3:50:33
 * @author impanxh@gmail.com
 * @since pantuo 1.0-SNAPSHOT
 */
public class LinuxMailService {
	private static Logger log = LoggerFactory.getLogger(MailServiceImpl.class);
	public static final String OURMAIL = "system@busme.cn";

	public static void sendMail(String tomail, String title, String context) {
		//echo "Hello From the dark voice<br>111" | mailx -r system@busme.cn  -a 'Content-Type: text/html' -s '[北巴广告交易系统]待办事项通知' ahua3515@163.com
		CommandLine command = new CommandLine("/bin/sh");
		StringBuilder runShell = new StringBuilder();
		runShell.append(" echo '");
		runShell.append(context);
		runShell.append("'  |mailx -r ");
		runShell.append(OURMAIL);
		runShell.append(" -a 'Content-Type: text/html; charset=utf-8' -s '");//Content-Type: text/html; charset=utf-8
		runShell.append(title);
		runShell.append("' ");
		runShell.append(tomail);
		command.addArguments(new String[] { "-c", runShell.toString() }, false);
		DefaultExecutor exec = new DefaultExecutor();
		try {
			exec.execute(command);
		} catch (IOException e) {
			log.error("linux shell sendmail error", e);
		}
	}

	public static void main(String[] args) throws ExecuteException, IOException {
		String title = "'[北巴广告交易系统]待办事项通知'", tomail = " 269048814@qq.com";
		String p = "ni<br>'ni11' \"ddd\"  <font color='red'>11<font>";
		sendMail("269048814@qq.com", title, p);
	}

}
