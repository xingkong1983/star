package com.xingkong1983.star.unipay;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class BigDecimalTool {

	public static final BigDecimal CENT_SCALE = new BigDecimal(100).setScale(6);

	/**
	 * 输入分的字符串，返回元为单位的BigDecimal，小数点为6位
	 * 
	 * @param feeStr
	 * @return
	 */
	public static BigDecimal getYuanFromCent(String feeStr) {
		return new BigDecimal(feeStr).setScale(6).divide(CENT_SCALE);
	}

	/**
	 * 输入元的BigDecimal，返回分的字符串，没有小数点
	 * 
	 * @param fee
	 * @return
	 */
	public static String getCentFromYuan(BigDecimal fee) {
		fee = fee.setScale(6).multiply(CENT_SCALE);
		fee.setScale(0, BigDecimal.ROUND_HALF_UP);
		DecimalFormat dataFormat = new DecimalFormat("0");
		String feeStr = dataFormat.format(fee);
		return feeStr;
	}

	/**
	 * 输入元的字符串，返回分为单位的字符串
	 * 
	 * @param feeStr
	 * @return
	 */
	public static BigDecimal getYuan(String feeStr) {
		return new BigDecimal(feeStr).setScale(6);
	}
	/**
	 * 小于1元，按金额的0.01%计费 
	 * 1元-10万元（含），1元/笔 
	 * 10万元到100万元（含），金额的0.01%计费，18元封顶 
	 * 100万元以上，20元/笔
	 */
	public static final BigDecimal MIN_TAX = new BigDecimal("1").setScale(6,BigDecimal.ROUND_HALF_DOWN); // 保底 1 元
	public static final BigDecimal MAX_TAX = new BigDecimal("18").setScale(6,BigDecimal.ROUND_HALF_DOWN); // 最高 18 元
	public static final BigDecimal MAX_TEST_MONEY = new BigDecimal("1").setScale(6,BigDecimal.ROUND_HALF_DOWN); // 最大测试金额为1元以下
	public static final BigDecimal MID_MONEY = new BigDecimal("100000").setScale(6,BigDecimal.ROUND_HALF_DOWN); // 10 万
	public static final BigDecimal SUPER_MAX_TAX = new BigDecimal("20").setScale(6,BigDecimal.ROUND_HALF_DOWN); // 单笔交易100万元以上,最高 20 元
	public static final BigDecimal SUPER_MONEY = new BigDecimal("1000000").setScale(6,BigDecimal.ROUND_HALF_DOWN); // 100 万
	public static BigDecimal getUnipayTax(BigDecimal fee) {		
		BigDecimal curTax = fee.setScale(6,BigDecimal.ROUND_HALF_DOWN).multiply(new BigDecimal("0.0001").setScale(6,BigDecimal.ROUND_HALF_DOWN));
		// 如果金额小于1元, 按金额的0.01%计费
		if (fee.compareTo(MAX_TEST_MONEY) < 0 )
		{
			return curTax.setScale(6,BigDecimal.ROUND_HALF_DOWN);
		}
		// 如果金额在 1元到10万元之间, 1元1笔计费
		if (fee.compareTo(MAX_TEST_MONEY) >= 0 & fee.compareTo(MID_MONEY) <= 0) {
			return MIN_TAX.setScale(6,BigDecimal.ROUND_HALF_DOWN);
		}
		// 如果金额在 10万元到100万元之间, 金额的0.01%计费
		if (fee.compareTo(MID_MONEY) > 0 & fee.compareTo(SUPER_MONEY) < 0) {
			if (curTax.compareTo(MIN_TAX) <= 0) {
				return MIN_TAX.setScale(6,BigDecimal.ROUND_HALF_DOWN);
			}
			if (curTax.compareTo(MAX_TAX) > 0) {
				return MAX_TAX.setScale(6,BigDecimal.ROUND_HALF_DOWN);
			}
		}
		// 如果金额大于 100万
		if (fee.compareTo(SUPER_MONEY) >= 0) {
			return SUPER_MAX_TAX.setScale(6,BigDecimal.ROUND_HALF_DOWN);
		}
		return curTax;
	}
	
//	public static void main(String[] args) {
//		BigDecimal feeList[] = {
//				new BigDecimal("0.01").setScale(6,BigDecimal.ROUND_HALF_DOWN),
//				new BigDecimal("0.99").setScale(6,BigDecimal.ROUND_HALF_DOWN),
//				new BigDecimal("1.00").setScale(6,BigDecimal.ROUND_HALF_DOWN),
//				new BigDecimal("14.00").setScale(6,BigDecimal.ROUND_HALF_DOWN),
//				new BigDecimal("99999.99").setScale(6,BigDecimal.ROUND_HALF_DOWN),
//				new BigDecimal("100000.00").setScale(6,BigDecimal.ROUND_HALF_DOWN),
//				new BigDecimal("100000.01").setScale(6,BigDecimal.ROUND_HALF_DOWN),
//				new BigDecimal("110000.01").setScale(6,BigDecimal.ROUND_HALF_DOWN),
//				new BigDecimal("999999.99").setScale(6,BigDecimal.ROUND_HALF_DOWN),
//				new BigDecimal("1000000.00").setScale(6,BigDecimal.ROUND_HALF_DOWN),
//				new BigDecimal("2000000.00").setScale(6,BigDecimal.ROUND_HALF_DOWN),
//				new BigDecimal("3000000.00").setScale(6,BigDecimal.ROUND_HALF_DOWN)
//		};
//		
//		for ( BigDecimal curItem : feeList) {
//			BigDecimal tax = getUnipayTax(curItem);
//			BigDecimal unionFee = curItem.add(tax);
//			OsTool.console.write("金额:"+curItem+" 费率:"+tax+" 总计付款给银联"+unionFee, true);
//		}
//	}
}
