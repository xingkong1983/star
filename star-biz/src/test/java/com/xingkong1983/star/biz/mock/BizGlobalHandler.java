package com.xingkong1983.star.biz.mock;

import com.xingkong1983.star.biz.BizException;
import com.xingkong1983.star.biz.vo.BizResponseVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BizGlobalHandler {
	BizApi bizApi = new BizApi();

	// 所有的异常处理
	public BizResponseVo login( String userName, String password ) {
		try {

			return bizApi.login(userName, password);
		} catch (BizException e) {
			return BizResponseVo.error(e);
		}
	}
}
