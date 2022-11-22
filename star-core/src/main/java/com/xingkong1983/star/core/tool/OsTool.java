package com.xingkong1983.star.core.tool;

import java.io.Closeable;
import java.util.Scanner;

public class OsTool {

	private final static int MAX_STACK_LEN = 10;


	/**
	 * 打印一行字符串
	 * 
	 * @param info
	 * @param show
	 */
	protected static void print( String info, Boolean show ) {
		System.out.println(info);
	}

	/**
	 * 打印一行字符串
	 * 
	 * @param info
	 */
	protected static void print( String info ) {
		System.out.println(info);
	}

	/**
	 * 读取一行字符串
	 * 
	 * @return
	 */
	protected static String readLine( ) {
		Scanner input = new Scanner(System.in);
		String result = input.nextLine();
		input.close();
		return result;
	}

	/**
	 * 打印异常信息
	 * 
	 * @param e
	 */
	protected static void print( Exception e ) {
		System.out.println(getErrorText(e));
	}

	/**
	 * 获取异常对象的堆栈的错误信息
	 * 
	 * @param e 异常对象
	 * @return
	 */
	public static String getErrorText( Exception e ) {
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

	/**
	 * 线程休息 count 秒 注意：这个函数不精确，用于不精确的场景
	 * 
	 * @param count 休息的秒数
	 */
	public static void sleep( int count ) {
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
	public static void sleepms( long count ) {
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
	public static void close( Closeable obj ) {
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
	public static void close( AutoCloseable obj ) {
		if (obj != null) {
			try {
				obj.close();
			} catch (Exception e) {
				print(e);
			}
		}
	}

	

}
