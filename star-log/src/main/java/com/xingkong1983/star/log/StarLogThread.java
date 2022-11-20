package com.xingkong1983.star.log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.concurrent.LinkedBlockingQueue;

public class StarLogThread extends Thread {

	private final static int MAX_QUEUE_LEN = 200000;
	private static LinkedBlockingQueue<String> linkedBlockingQueue = new LinkedBlockingQueue<>(MAX_QUEUE_LEN);
	private File file;
	private Writer writer = null;
	private String logName;
	private String path;
	private long lastDay = 0;

	public static void push(String message) {
		if (!linkedBlockingQueue.offer(message)) {
			StarLogTool.print("log lost.");
		}
	}

	/**
	 * 加载日志
	 * 
	 * @throws IOException
	 */
	public void loadLogFile() throws IOException {
		long today = StarLogTool.getToday();
		if (today != lastDay) {
			lastDay = today;
			StarLogTool.close(writer);
			this.path = StarLogTool.getCurrentPath() + "logs/";
			StarLogTool.createDir(this.path);
			logName = path + this.lastDay + ".log";
			StarLogTool.print("");
			StarLogTool.print("----------------------------------------");
			StarLogTool.print("StarLog logName: \n" +logName);
			StarLogTool.print("----------------------------------------");
			StarLogTool.print("");
			this.file = new File(logName);
			this.writer = new FileWriter(file, Charset.forName("utf-8"), true);
			this.writer.write("\n----------------------------------------\n");
			StarLogTool.print("\n----------------------------------------");
			this.writer.write("StarLog begin.         \n");
			StarLogTool.print("StarLog begin.           ");
			this.writer.write("----------------------------------------\n\n");
			StarLogTool.print("----------------------------------------\n");
			this.writer.flush();
			System.out.flush();
			
		}

	}

	@Override
	public void run() {
		long seqId = 0;

		try {

			// 比较日期是否已经发生变化，如果日期发生了变化，新起一个文件

			while (!isInterrupted()) {
				loadLogFile();
				String message = linkedBlockingQueue.poll();
				Long curId = Thread.currentThread().getId();
				if (message == null) {
					StarLogTool.sleepms(10);
				} else {
					seqId++;
					String seqStr = String.format("%06d", seqId);
					message = curId+"+["+ seqStr + "] " + message;
					writer.write( message + "\n");
					StarLogTool.print(message);
					writer.flush();
					System.out.flush();
				}
			}
			writer.flush();
		} catch (IOException e) {
			StarLogTool.print("[starLog]日志文件不能打开，没有记录日志");
			StarLogTool.print(e);
			return;
		} finally {
			StarLogTool.close(writer);
		}
		StarLogTool.print("[StarLog] End.          ");
		System.out.flush();
	}

	public static void begin() {
		StarLogThread logTool = new StarLogThread();
		logTool.start();
	}
}
