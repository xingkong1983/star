package com.xingkong1983.star.core.tool;

import com.alibaba.fastjson.JSON;

public interface IPrint{
	/**
	 * 返回美化的 Json 字符串
	 * @return 对象的 Json 字符串
	 */
	default String toJson() {
		String simpleName = this.getClass().getSimpleName();
		String className = this.getClass().getName();
		String json = JSON.toJSONString(this, true);
		return "\r\n"+simpleName+" ("+className+"):\r\n"+json;
	}
	
	/**
	 * 打印美化的 Json 字符串
	 */
	default void print() {
		System.out.println(toJson());
	}
}