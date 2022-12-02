package com.xingkong1983.star.biz.mock;

import com.xingkong1983.star.biz.vo.BizResponseVo;

public class BizService {

	public BizResponseVo login( String userName, String password ) {
		
		BizResponseVo resVo = null;
		if (userName == "root" && password == "keep") {
			resVo = BizResponseVo.ok("登录成功");
		} else {
			resVo = BizResponseVo.error("登录失败");
		}
		return resVo;
	}

}
