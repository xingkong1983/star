package com.xingkong1983.star.log;

import java.io.Closeable;
import java.io.File;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StarLogTool {
	private final static int MAX_STACK_LEN = 10;

	private static String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS: ";

	/**
	 * 获取当前时间字符串
	 * 
	 * @return
	 */
	protected static String getCurrentDateTimeStr() {
		Date curDate = new Date();
		return new SimpleDateFormat(TIME_FORMAT).format(curDate);
	}

	/**
	 * Description: 创建一个由path指定的目录,如果改目录存在，则不会创建
	 * 
	 * @param path 文件目录
	 * @return 创建或者已经存在，返回true,否则返回false
	 */
	protected static boolean createDir(String path) {
		try {
			File file = new File(path);
			if (!file.exists() && !file.isDirectory()) {
				if (file.mkdirs()) {
					return true;
				} else {
					return false;
				}
			} else {
				return true;
			}
		} catch (Exception e) {
			print(e);
			return false;
		}
	}

	/**
	 * 获取当前时间字符串
	 * 
	 * @return
	 */
	protected static String getNowStr() {
		Date curDate = new Date();
		return new SimpleDateFormat(TIME_FORMAT).format(curDate);
	}

	/**
	 * 获取今天的数字
	 * 
	 * @return
	 */
	protected static long getToday() {
		Calendar date = Calendar.getInstance();
		long today = date.get(Calendar.YEAR) * 10000 + date.get(Calendar.MONTH) * 100 + date.get(Calendar.DAY_OF_MONTH);
		return today;
	}

	/**
	 * 返回应用当前路径
	 * 
	 * @return
	 */
	protected static String getCurrentPath() {
		String appPath = "";
		try {
			String URL = Thread.currentThread().getContextClassLoader().getResource("").getPath();
			appPath = URLDecoder.decode(URL,"UTF-8");
			return appPath;
		} catch (Exception e1) {
			appPath = "./";
		}
		return appPath;
	}

	/**
	 * 线程休息 count 秒 注意：这个函数不精确，用于不精确的场景
	 * 
	 * @param count 休息的秒数
	 */
	protected static void sleep(int count) {
		int i = count;
		try {
			while (i > 0) {
				Thread.sleep(1000);
				i--;
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 线程休息 count 毫秒 注意：这个函数不精确，用于不精确的场景
	 * 
	 * @param count
	 */
	protected static void sleepms(long count) {
		try {
			Thread.sleep(count);
		} catch (Exception e) {
		}
	}

	/**
	 * 关闭 Closeable 对象
	 * 
	 * @param obj Closeable对象
	 */
	protected static void close(Closeable obj) {
		if (obj != null) {
			try {
				obj.close();
			} catch (Exception e) {
				print(e);
			}
		}
	}

	/**
	 * 关闭 AutoCloseable 对象
	 * 
	 * @param obj AutoCloseable 对象
	 */
	protected static void close(AutoCloseable obj) {
		if (obj != null) {
			try {
				obj.close();
			} catch (Exception e) {
				print(e);
			}
		}
	}

	/**
	 * 打印一行字符串
	 * 
	 * @param info
	 */
	protected static void print(String info) {
		System.out.println(info);
	}

	/**
	 * 打印异常信息
	 * 
	 * @param e
	 */
	protected static void print(Exception e) {
		System.out.println(getErrorText(e));
	}

	/**
	 * 获取异常对象的堆栈的错误信息
	 * 
	 * @param e 异常对象
	 * @return
	 */
	protected static String getErrorText(Exception e) {
		StackTraceElement[] stack = e.getStackTrace();
		StringBuilder textLine = new StringBuilder("\r\n\r\n");
		textLine.append("ERR: " + e.getMessage() + "\r\n");
		int len = stack.length < MAX_STACK_LEN ? stack.length : MAX_STACK_LEN;
		for (int i = 0; i < len; i++) {
			StackTraceElement s = stack[i];
			textLine.append(
					String.format("[%02d] %s - (%s:%s)\r\n", i, s.getMethodName(), s.getFileName(), s.getLineNumber()));
		}
		textLine.append("\r\n");
		return textLine.toString() + e.toString() + "\r\n";
	}
}
