package com.xingkong1983.star.log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;

public class StarLogFile {

	private File file;
	private Writer writer = null;
	private String logName;
	private String path;
	private long lastDay = 0;

	public StarLogFile(String mod) {
		logName = "-" + mod + ".log";
	}

	/**
	 * 加载日志
	 * 
	 * @throws IOException
	 */
	public void load() throws IOException {
		long today = StarLogTool.getToday();
		if (today != lastDay) {
			lastDay = today;
			StarLogTool.close(writer);
			this.path = StarLogTool.getCurrentPath() + "../logs/";
			StarLogTool.createDir(this.path);
			logName = path + this.lastDay + logName;
			StarLogTool.print("");
			StarLogTool.print("----------------------------------------");
			StarLogTool.print("[StarLog] logName: \n" + logName);
			StarLogTool.print("----------------------------------------");
			StarLogTool.print("");
			this.file = new File(logName);
			this.writer = new FileWriter(file, Charset.forName("utf-8"), true);
			this.writer.write("\n----------------------------------------\n");
			StarLogTool.print("\n----------------------------------------");
			this.writer.write("[StarLog] begin.         \n");
			StarLogTool.print("[StarLog] begin.           ");
			this.writer.write("----------------------------------------\n\n");
			StarLogTool.print("----------------------------------------\n");
			this.writer.flush();
			System.out.flush();

		}
	}

	public void write(String message, Boolean isShow) throws IOException {
		writer.write(message + "\n");
		if (isShow) {
			StarLogTool.print(message);
		}

	}

	public void flush(Boolean isShow) throws IOException {
		writer.flush();
		if (isShow) {
			System.out.flush();
		}
	}

	public void close() {
		StarLogTool.close(writer);
	}
}
