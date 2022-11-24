package com.xingkong1983.star.biz;

import org.mindrot.jbcrypt.BCrypt;

import lombok.Data;

@Data
public class BizAssert {

	private int code;

	public BizAssert(int code) {
		this.code = code;
	}

	public void throwException( String message ) {
		throw new BizException(this.code, message);
	}

	/**
	 * 断言对象不为空
	 * 
	 * @param object  需要返回给客户端的数据
	 * @param message 需要返回给客户端的消息
	 * @param data
	 */
	public void notNull( Object object, String message ) {
		if (object == null) {
			throw new BizException(this.code, message);
		}
	}

	/**
	 * 断言对象为空
	 * 
	 * @param object  需要返回给客户端的数据
	 * @param message 需要返回给客户端的消息
	 * @param data
	 */
	public void isNull( Object object, String message ) {
		if (object != null) {
			throw new BizException(this.code, message);
		}
	}

	/**
	 * 断言密码相同
	 * 
	 * @param password
	 * @param dbPassword
	 * @param message
	 * @param data
	 */
	public void isEqualPassword( String password, String dbPassword, String message ) {
		if (!BCrypt.checkpw(password, dbPassword)) {
			throw new BizException(this.code, message);
		}
	}

	public void isEqual( Long a, Long b, String message ) {
		if (a.longValue() != b.longValue()) {
			throw new BizException(this.code, message);
		}
	}

	public void isEqual( Integer a, Integer b, String message ) {
		if (a.intValue() == b.intValue()) {
			throw new BizException(this.code, message);
		}
	}

	public void isTrue( Boolean a, String message ) {
		if (!a) {
			throw new BizException(this.code, message);
		}
	}

	public void isFalse( Boolean a, String message ) {
		if (a) {
			throw new BizException(this.code, message);
		}
	}

	/**
	 * 断言字符串相同
	 * 
	 * @param strA
	 * @param strB
	 * @param message
	 * @param data
	 */
	public void isEqual( String strA, String strB, String message ) {
		if (!(strA.equals(strB))) {
			throw new BizException(this.code, message);
		}
	}

	/**
	 * 如果对象大小大于0，表示有结果
	 * 
	 * @param result
	 * @param message 需要返回给客户端的消息
	 * @param data
	 */
	public void hasResult( Long object, String msg ) {
		if (object == null || object <= 0) {
			throw new BizException(this.code, msg, null);
		}
	}

	/**
	 * 如果对象大小大于0，表示有结果
	 *
	 * @param result
	 * @param message 需要返回给客户端的消息
	 * @param data
	 */
	public void hasResult( int object, String msg ) {
		if (object <= 0) {
			throw new BizException(this.code, msg, null);
		}
	}

	/**
	 * 如果没有权限
	 *
	 * @param isCheck 是否有权限
	 * @param msg
	 */
	public void isPermissions( Boolean isCheck, String msg ) {
		if (!isCheck) {
			throw new BizException(this.code, msg);
		}
	}

	/**
	 * 上传失败
	 *
	 * @param permissions 是否有权限
	 * @param msg
	 */
	public void uploadError( boolean permissions, String msg ) {
		if (permissions) {
			throw new BizException(this.code, msg);
		}
	}

}
