package com.xingkong1983.star.biz.vo;

public class BizSuccessCode {
	int code = 0;

	public BizSuccessCode(int code) {
		this.code = code;
	}

	public BizResponseVo success(String message) {
		return new BizResponseVo(code, message);
	}
	
	public BizResponseVo success(String message, Object data) {
		return new BizResponseVo(code, message, data);
	}
	
	public BizResponseVo success(String message, String log, Object data) {
		return new BizResponseVo(code, message, log, data);
	}
	
}
