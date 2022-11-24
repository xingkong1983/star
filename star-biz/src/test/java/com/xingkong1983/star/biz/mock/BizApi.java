package com.xingkong1983.star.biz.mock;

import com.xingkong1983.star.biz.BizTool;
import com.xingkong1983.star.biz.vo.BizResponseVo;

public class BizApi {

	BizService bizService;
	
	public BizApi()
	{
		bizService =  new BizService();
	}
	
	// 管理员  /manng/user/api/uid
	// 用户
	
	// userUpdate
	// userUpdate(uid, )

	/**
	 * 用户登录
	 * 
	 * @param userName
	 * @param password
	 * @return
	 */
	public BizResponseVo login( String userName, String password, int count ) {

		BizTool.ASSERT_FORM.notNull(userName, "用户名不能为空");
		BizTool.ASSERT_FORM.notNull(password, "密码不能为空");

		
		// 权限对不对
		BizResponseVo res = bizService.login(userName, password, count);
		return res;
	}
}
