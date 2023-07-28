package com.xingkong1983.star.biz;

import com.xingkong1983.star.biz.vo.BizErrorCode;
import com.xingkong1983.star.biz.vo.BizErrorFormCode;
import com.xingkong1983.star.biz.vo.BizResponseVo;
import com.xingkong1983.star.biz.vo.BizSuccessCode;
import com.xingkong1983.star.core.tool.OsTool;

import lombok.Data;

@Data
public class BizTool {

	public final static int CODE_OK = 200;
	public final static int CODE_ERROR = 500;

	// ==================================================
	// 响应成功常量定义
	// ==================================================
	public final static BizSuccessCode OK = new BizSuccessCode(200);
	public final static BizSuccessCode OK_LOGIN = new BizSuccessCode(2000);
	public final static BizSuccessCode OK_LOGOUT = new BizSuccessCode(2001);
	public final static BizSuccessCode OK_FORM = new BizSuccessCode(2002);
	public final static BizSuccessCode OK_UPLOAD = new BizSuccessCode(2003);

	// ==================================================
	// 响应错误常量定义
	// ==================================================
	public final static BizErrorCode ERR = new BizErrorCode(500);
	public final static BizErrorCode ERR_NOLOGIN = new BizErrorCode(5000);
	public final static BizErrorCode ERR_LOGIN = new BizErrorCode(5001);
	public final static BizErrorCode ERR_LOGOUT = new BizErrorCode(5002);
	public final static BizErrorFormCode ERR_FORM = new BizErrorFormCode(5003);
	public final static BizErrorCode ERR_SMS_CODE = new BizErrorCode(5004);
	public final static BizErrorCode ERR_UPLOAD = new BizErrorCode(5005);
	public final static BizErrorCode ERR_NO_COMPANY = new BizErrorCode(5006);

	// ==================================================
	// 断言定义
	// ==================================================

	public final static BizAssert ASSERT_NOLOGIN = new BizAssert(5000);
	public final static BizAssert ASSERT_FORM = new BizAssert(6000);
	public final static BizAssert ASSERT_DB = new BizAssert(7001);
	public final static BizAssert ASSERT_REDIS = new BizAssert(7002);
	public final static BizAssert ASSERT_PERSSION = new BizAssert(7003);
	public final static BizAssert ASSERT_FILE = new BizAssert(7004);

	public static BizResponseVo success(String message) {
		return success(message, null);
	}

	/**
	 * 返回成功的 Biz 响应对象
	 * @param message 消息
	 * @param data 数据对象
	 * @return 响应对象
	 */
	public static BizResponseVo success(String message, Object data) {
		BizResponseVo resVo = new BizResponseVo(CODE_OK, message, null, data);
		return resVo;
	}

	/**
	 * 返回出错的 Biz 响应对象
	 * @param message 消息
	 * @return 响应对象
	 */
	public static BizResponseVo error(String message) {
		return error(message, null);
	}

	/**
	 * 返回出错的 Biz 响应对象
	 * @param message 消息
	 * @param data 数据对象
	 * @return 响应对象
	 */
	public static BizResponseVo error(String message, Object data) {
		BizResponseVo resVo = new BizResponseVo(500, message, null, data);
		return resVo;
	}

	/**
	 * 返回出错的 Biz 响应对象
	 * @param e 异常对象
	 * @return 响应对象
	 */
	public static BizResponseVo error(Exception e) {
		return error(e, e.getMessage(), null);
	}

	/**
	 * 返回出错的 Biz 响应对象
	 * @param e
	 * @param message
	 * @return
	 */
	public static BizResponseVo error(Exception e, String message) {
		return error(e, message, null);
	}

	/**
	 * 返回出错的 Biz 响应对象
	 * @param e
	 * @param message
	 * @param data
	 * @return
	 */
	public static BizResponseVo error(Exception e, String message, Object data) {
		BizResponseVo resVo = null;
		if (e instanceof BizException) {
			BizException bizExp = (BizException) e;
			resVo = new BizResponseVo(bizExp.getCode(), message, OsTool.getErrorText(bizExp), data);
		} else {
			resVo = new BizResponseVo(CODE_ERROR, message, OsTool.getErrorText(e), data);
		}
		return resVo;
	}
	
	
}
