package com.xingkong1983.star.biz.vo;

public class BizErrorCode {
	int code = 0;

	public BizErrorCode(int code) {
		this.code = code;
	}
	
	public BizResponseVo error(String message) {
		return new BizResponseVo(code, message);
	}
	
	public BizResponseVo error(String message, Object data) {
		return new BizResponseVo(code, message, data);
	}
	
}
