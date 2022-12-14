package com.xingkong1983.star.biz.vo;

import com.xingkong1983.star.core.print.IPrint;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BizPageResponseVo extends BizResponseVo implements IPrint {

	/**
	 * 构造函数
	 * @param code 错误编码
	 * @param message 消息
	 * @param log 日志
	 * @param data 数据
	 */
	public BizPageResponseVo(int code, String message, String log, Object data) {
		super(code, message, log, data);
	}

	/**
	 * 每页展示数据条数
	 */
	private long pageSize;

	/**
	 * 当前显示的数据页编号
	 */
	private long pageNo;

	/**
	 * 数据总条数
	 */
	private long totalNum;

	/**
	 * 数据总页数
	 */
	private long totalPage;

}
