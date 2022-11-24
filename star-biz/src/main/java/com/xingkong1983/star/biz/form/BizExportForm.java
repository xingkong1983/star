package com.xingkong1983.star.biz.form;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BizExportForm extends BizPageForm {
	/**
	 * 导出类型
	 */
	private Integer type;

	/**
	 * 是否导出
	 */
	private Boolean export = false;

}
