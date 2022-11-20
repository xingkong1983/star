package com.xingkong1983.star.log;

import lombok.Data;

@Data
public class StarLogMo {

	private static char SPACE = ' ';
	private String curDateStr;
	private int lineNumber;
	private String methodName;
	private String fileName;
	private String className;
	private String threadName;
	private String threadId;
	private String headStr;
	private String levelNameStr;
	private int flag;

	public StarLogMo(StackTraceElement stackTrace, String levelName) {

		this.curDateStr = StarLogTool.getCurrentDateTimeStr();

		this.lineNumber = stackTrace.getLineNumber();
		this.methodName = stackTrace.getMethodName();
		this.fileName = stackTrace.getFileName();
		this.className = stackTrace.getClassName();
		this.threadName = Thread.currentThread().getName();
		this.threadId = "" + Thread.currentThread().getId();
		this.levelNameStr = String.format("%-5s", levelName);

		if (className.contains("biz")) {
			flag = StartLogEvent.FLAG_BUSINESS;
		} else {
			flag = StartLogEvent.FLAG_LOG;
		}

		this.headStr = levelNameStr + SPACE + curDateStr + SPACE + threadName + " (" + fileName + ":" + lineNumber
				+ ") : ";
	}
}
