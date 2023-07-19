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
	 * @param object
	 * @param message
	 */
	public void notNull( Object object, String message ) {
		if (object == null) {
			throw new BizException(this.code, message);
		}
	}

	/**
	 * 断言对象为空
	 * @param object
	 * @param message
	 */
	public void isNull( Object object, String message ) {
		if (object != null) {
			throw new BizException(this.code, message);
		}
	}

	/**
	 * 断言密码相同
	 * @param password
	 * @param dbPassword
	 * @param message
	 */
	public void isEqualPassword( String password, String dbPassword, String message ) {
		notNull(password, message);
		notNull(dbPassword, message);
		if (!BCrypt.checkpw(password, dbPassword)) {
			throw new BizException(this.code, message);
		}
	}

	public void isEqual( Long a, Long b, String message ) {
		notNull(a, message);
		notNull(b, message);
		if (a.longValue() != b.longValue()) {
			throw new BizException(this.code, message);
		}
	}

	public void isEqual( Integer a, Integer b, String message ) {
		notNull(a, message);
		notNull(b, message);
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
	 * @param strA
	 * @param strB
	 * @param message
	 */
	public void isEqual( String strA, String strB, String message ) {
		notNull(strA, message);
		notNull(strB, message);
		if (!(strA.equals(strB))) {
			throw new BizException(this.code, message);
		}
	}
	
	/**
	 * 断言字符串不相同
	 * @param strA
	 * @param strB
	 * @param message
	 */
	public void isNotEqual( String strA, String strB, String message ) {
		if (strA.equals(strB)) {
			throw new BizException(this.code, message);
		}
	}

	/**
	 * 如果对象大小大于0，表示有结果
	 * @param object
	 * @param msg
	 */
	public void hasResult( Long object, String msg ) {
		if (object == null || object <= 0) {
			throw new BizException(this.code, msg, null);
		}
	}

	/**
	 * 如果对象大小大于0，表示有结果
	 * @param object
	 * @param msg
	 */
	public void hasResult( int object, String msg ) {
		if (object <= 0) {
			throw new BizException(this.code, msg, null);
		}
	}

	/**
	 * 如果没有权限
	 * @param isCheck
	 * @param msg
	 */
	public void isPermissions( Boolean isCheck, String msg ) {
		if (!isCheck) {
			throw new BizException(this.code, msg);
		}
	}

	/**
	 * 上传失败
	 * @param permissions
	 * @param msg
	 */
	public void uploadError( boolean permissions, String msg ) {
		if (permissions) {
			throw new BizException(this.code, msg);
		}
	}

}
