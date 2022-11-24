package com.xingkong1983.star.biz;

import com.xingkong1983.star.biz.vo.BizResponseVo;

import lombok.Data;

@Data
public class BizTool {
	// ==================================================
	// 响应成功常量定义
	// ==================================================
	public final static BizResponseVo OK = new BizResponseVo(200);
	public final static BizResponseVo OK_LOGIN = new BizResponseVo(2000);
	public final static BizResponseVo OK_LOGOUT = new BizResponseVo(2001);
	public final static BizResponseVo OK_FORM = new BizResponseVo(2002);
	public final static BizResponseVo OK_UPLOAD = new BizResponseVo(2003);


	// ==================================================
	// 响应错误常量定义
	// ==================================================
	public final static BizResponseVo ERR = new BizResponseVo(500);
	public final static BizResponseVo ERR_NOLOGIN = new BizResponseVo(5000);
	public final static BizResponseVo ERR_LOGIN = new BizResponseVo(5001);
	public final static BizResponseVo ERR_LOGOUT = new BizResponseVo(5002);
	public final static BizResponseVo ERR_FORM = new BizResponseVo(5003);
	public final static BizResponseVo ERR_SMS_CODE = new BizResponseVo(5004);
	public final static BizResponseVo ERR_UPLOAD = new BizResponseVo(5005);
	public final static BizResponseVo ERR_NO_COMPANY = new BizResponseVo(5006);
	

	// ==================================================
	// 断言定义
	// ==================================================
	
	/**
	 * 断言定义
	 */
	public final static BizAssert ASSERT_NOLOGIN = new BizAssert(5000);
	public final static BizAssert ASSERT_FORM = new BizAssert(6000);
	public final static BizAssert ASSERT_DB = new BizAssert(7001);
	public final static BizAssert ASSERT_REDIS = new BizAssert(7002);
	public final static BizAssert ASSERT_PERSSION = new BizAssert(7003);
	public final static BizAssert ASSERT_FILE = new BizAssert(7004);
}
