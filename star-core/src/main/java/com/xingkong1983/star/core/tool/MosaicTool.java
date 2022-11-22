package com.xingkong1983.star.core.tool;


public class MosaicTool {

	private final static String XSTR = "XXXXXXXXXXXXXXXXXXXX";
	private final static String SSTR = "********************";

	/**
	 * Description: 获取马赛克手机号码
	 * 
	 * @param mobile
	 * @return
	 */
	public static String getMosaicMobile( String mobile ) {
		int maxLength = mobile.length();
		String lastNumText = mobile.substring(maxLength - 2, maxLength);
		int sLength = maxLength - 6;
		String mosaicMobile = mobile.substring(0, 4) + SSTR.substring(0, sLength) + lastNumText;
		return mosaicMobile;
	}

	/**
	 * Description: 获取马赛克银行号码
	 * 
	 * @param cardNo
	 * @return
	 */
	public static String getMosaicBankCardNo( String cardNo ) {
		/*
		 * 银行卡的卡号长度及结构符合ISO7812-1有关规定,由13-19位数字表示,具体由以下几部分组成: 9 XXXXX X......X X
		 * 发卡银行标识代码 自定义位 校验位 发卡行标识代码指发卡行标识代码标识发卡机构,由6位数字表示,第一位固定为"9",后5位由BIN注册管理机构分配.
		 * 自定义位是指发卡行自定义位,由6-12位数字组成. 校验位是指卡号的最后一位数字,根据校验位前的数字计算得到.
		 * BIN注册管理机构是指负责BIN注册管理的机构.
		 */
		if (StringTool.isEmpty(cardNo)) {
			cardNo = "XXXXXXXXXXXXX";
		}

		int maxLength = cardNo.length();
		String lastNumText = cardNo.substring(maxLength - 4, maxLength);
		int xLength = maxLength - 11;

		String mosaicMobile = cardNo.substring(0, 7) + XSTR.substring(0, xLength) + lastNumText;
		return mosaicMobile;
	}
}
