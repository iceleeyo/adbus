package com.pantuo.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FFmpegUtil {

	private static Logger log = LoggerFactory.getLogger(FFmpegUtil.class);

	public static void mpgToMp4(String filePath) {

		if (!OSinfoUtils.isLinux()) {
			return;
		}
		if (OSinfoUtils.isMacOSX() || OSinfoUtils.isMacOS()) {
			return;
		}
		if (!StringUtils.endsWith(filePath, "mpg")) {
			return;
		}
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
		CommandLine command = new CommandLine("/bin/sh");
		StringBuilder runShell = new StringBuilder();
		runShell.append(" ffmpeg -i ");
		runShell.append(filePath);
		runShell.append(" -vcodec libx264 -y ");
		runShell.append(StringUtils.replace(filePath, ".mpg", ".mp4"));
		log.info(runShell.toString());
		command.addArguments(new String[] { "-c", runShell.toString() }, false);
		DefaultExecutor exec = new DefaultExecutor();
		try {
			exec.setExitValues(null);
			PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream, errorStream);
			exec.setStreamHandler(streamHandler);
			String out = outputStream.toString("gbk");
			String error = errorStream.toString("gbk");
			log.info(out);
			log.error(error);
			exec.execute(command);
		} catch (IOException e) {
			log.error("linux shell sendmail error", e);
		}
	}
}
