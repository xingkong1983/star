package com.xingkong1983.star.servlet;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;

import com.xingkong1983.star.core.tool.OsTool;

public class ServeletTool {
	/**
	 * 是否是Ajax异步请求
	 */
	public static boolean isAjax(HttpServletRequest request, String path) {
		String accept = request.getHeader("accept");
		if (accept != null && accept.indexOf("application/json") != -1) {
			return true;
		}

		String xRequestedWith = request.getHeader("X-Requested-With");
		if (xRequestedWith != null && xRequestedWith.indexOf("XMLHttpRequest") != -1) {
			return true;
		}

		String uri = request.getRequestURI();
		if (StringUtils.endsWithIgnoreCase(uri, ".json")) {
			return true;
		}

		if (StringUtils.endsWithIgnoreCase(path, ".json")) {
			return true;
		}
		return false;
	}

	/**
	 * 输出 json
	 * 
	 * @param response
	 * @param content
	 */
	public static void writeJson(HttpServletResponse response, String content) {
		try {
			// 设置将字符以"UTF-8"编码输出到客户端浏览器
			response.setCharacterEncoding("UTF-8");
			// 获取PrintWriter输出流
			PrintWriter out = response.getWriter();
			response.setHeader("content-type", "text/html;charset=UTF-8");
			out.write(content);
		} catch (Exception e) {
			OsTool.print(e);
		}
	}

	/**
	 * 输出 js
	 * 
	 * @param response
	 * @param content
	 */
	public static void writeJs(HttpServletResponse response, String content) {
		try {
			// 设置将字符以"UTF-8"编码输出到客户端浏览器
			response.setCharacterEncoding("UTF-8");
			// 获取PrintWriter输出流
			PrintWriter out = response.getWriter();
			response.setHeader("Content-Type", "application/javascript");
			out.write(content);
			out.flush();
			out.close();
		} catch (Exception e) {
			OsTool.print(e);
		}
	}
}
