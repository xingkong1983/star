package com.xingkong1983.star.core.tool;

import java.io.File;

public class PathTool {

	/**
	 * 判断文件或者文件夹是否存在
	 * @param fileName
	 * @return
	 */
	public static boolean isExists( String fileName ) {
		File file = new File(fileName);
		return file.exists();
	}

	/**
	 * 返回环境变量
	 * @return
	 */
	public static String getEnv( ) {
		String env = System.getProperty("env");
		if (StringTool.isEmpty(env)) {
			env = "dev";
			OsTool.print("[获取env配置] 没有配置环境变量,默认采用开发环境配置dev");
		}

		return env;
	}

	/**
	 * 返回应用当前路径
	 * @return
	 */
	public static String getCurrentPath( ) {
		String appPath = "";
		try {
			appPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
			return appPath;
		} catch (Exception e1) {
			appPath = "./";
		}
		return appPath;
	}
}
