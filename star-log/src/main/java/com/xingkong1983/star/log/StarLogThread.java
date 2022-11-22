package com.xingkong1983.star.log;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

public class StarLogThread extends Thread {

	private final static int MAX_QUEUE_LEN = 200000;
	private static LinkedBlockingQueue<StartLogEvent> linkedBlockingQueue = new LinkedBlockingQueue<>(MAX_QUEUE_LEN);

	StarLogFile logFile;
	StarLogFile bizFile;

	public static void push(StartLogEvent event) {
		if (!linkedBlockingQueue.offer(event)) {
			StarLogTool.print("log lost.");
		}
	}

<<<<<<< .mine
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

			StarLogTool.print("----------------------------------------");
			StarLogTool.print("StarLog logName: \n" +logName);
			StarLogTool.print("----------------------------------------");

			this.file = new File(logName);
			this.writer = new FileWriter(file, Charset.forName("utf-8"), true);
			this.writer.write("----------------------------------------\n");
			StarLogTool.print("----------------------------------------");
			this.writer.write("StarLog begin.         \n");
			StarLogTool.print("StarLog begin.           ");
			this.writer.write("----------------------------------------\n");
			StarLogTool.print("----------------------------------------");
			this.writer.flush();
			System.out.flush();
			
		}

	}

=======

































>>>>>>> .theirs
	@Override
	public void run() {
		long seqId = 0;

		try {

			// 比较日期是否已经发生变化，如果日期发生了变化，新起一个文件
			logFile = new StarLogFile("log");
			bizFile = new StarLogFile("biz");
			while (!isInterrupted()) {
				logFile.load();
				bizFile.load();
				StartLogEvent event = linkedBlockingQueue.poll();
				Long curId = Thread.currentThread().getId();
				if (event == null) {
					// StarLogTool.sleepms(10);
					Thread.yield();
				} else {
					seqId++;
					String seqStr = String.format("%06d", seqId);
					String message = "+[" + seqStr + "] " + event.getMessage();
					logFile.write(message,true);
					if (StartLogEvent.FLAG_BUSINESS == event.getFlag()) {
						bizFile.write(message,false);
					}
					logFile.flush(true);
					bizFile.flush(false);

				}
			}

		} catch (IOException e) {
			StarLogTool.print("[starLog]日志文件不能打开，没有记录日志");
			StarLogTool.print(e);
			return;
		} finally {
			logFile.close();
			bizFile.close();
		}
		StarLogTool.print("[StarLog] End.          ");
		System.out.flush();
	}

	public static void begin() {
		StarLogThread logTool = new StarLogThread();
		logTool.start();
		
	}
}
