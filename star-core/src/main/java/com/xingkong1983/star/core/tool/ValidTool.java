package com.xingkong1983.star.core.tool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidTool {

	public final static class password {
		public final static String regx = "^(?=.*?[a-zA-Z])(?=.*?[0-9]).{6,32}$";
		public final static String message = "密码长度为6-32位, 需包含字母和数字";
	}

	public final static class mobile {
		public final static String regx = "(1)\\d{10}$";
		public final static Pattern pattern = Pattern.compile(regx);
		public final static String message = "请输入正确的手机号码";
	}

	public final static class email {
		public final static String regx = "^\\\\w+(\\\\w|[.]\\\\w+)+@\\\\w+([.]\\\\w+){1,3}";
		public final static Pattern pattern = Pattern.compile(regx);
		public final static String message = "请输入正确的邮箱";
	}

	public final static class IDCode {
		public final static String regx = "^([1-6][1-9]|50)\\d{4}(18|19|20)\\d{2}((0[1-9])|10|11|12)(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";
		public final static Pattern pattern = Pattern.compile(regx);
		public final static String message = "请输入正确的身份证号码";
	}

	public final static class postcode {
		public final static String regx = "^(0[1-7]|1[0-356]|2[0-7]|3[0-6]|4[0-7]|5[1-7]|6[1-7]|7[0-5]|8[013-6])\\d{4}$";
		public final static Pattern pattern = Pattern.compile(regx);
		public final static String message = "请输入正确的邮政编码";
	}
	
	/**
	 * Description: 判断是不是手机号码
	 * 
	 * @param mobile
	 * @return
	 */
	public static boolean isMobile( String mobile ) {
		Matcher m = ValidTool.mobile.pattern.matcher(mobile);
		return m.matches();
	}

	/**
	 * Description: 判断是否是Email
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail( String email ) {
		Matcher m = ValidTool.email.pattern.matcher(email);
		return m.matches();
	}

}
