package com.xingkong1983.star.biz.form;

import com.xingkong1983.star.core.tool.StringTool;

import lombok.Data;

@Data
public class BizSortForm {

	/**
	 * 排序列名
	 */
	private String columnName;

	/**
	 * 排序类型 desc asc
	 */
	private Boolean isAsc;

	/**
	 * 设置列名
	 * 
	 * @param columnName 列名
	 */
	public void setColumnName(String columnName) {
		this.columnName = StringTool.toUnderline(columnName);
	}

}
