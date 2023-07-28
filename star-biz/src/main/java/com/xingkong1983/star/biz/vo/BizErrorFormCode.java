package com.xingkong1983.star.biz.vo;

import java.util.List;

public class BizErrorFormCode {
	int code = 0;

	public BizErrorFormCode(int code) {
		this.code = code;
	}
	
	public BizResponseVo error(String message, List<BizFiledVo> data) {
		return new BizResponseVo(code, message, data);
	}
	
}
