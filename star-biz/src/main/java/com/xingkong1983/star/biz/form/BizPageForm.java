package com.xingkong1983.star.biz.form;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BizPageForm {

	/**
	 * 需要显示的分页编号
	 */
	private Integer pageNo;

	/**
	 * 每页展示条数
	 */
	private Integer pageSize;

	/**
	 * 排序列表
	 */
	private List<BizSortForm> sortList;

}
