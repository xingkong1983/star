package com.xingkong1983.star.biz.mock;

import com.xingkong1983.star.biz.vo.BizResponseVo;

public class BizService {

	public BizResponseVo login( String userName, String password, int 100  ) {

		
		// 取一笔款
		// 100元，1元
		// 获取余额  // DB 层
		// 计算银行手续费  
		// 存最ye进去  // DB 层
		// 返回结果
		
		BizResponseVo resVo = null;
		if (userName == "root" && password == "keep") {
			
			// 模拟DB层
			// data mo => vo 
			resVo = BizResponseVo.ok("登录成功");
		} else {
			resVo = BizResponseVo.error("登录失败");
		}
		return resVo;
	}

}
