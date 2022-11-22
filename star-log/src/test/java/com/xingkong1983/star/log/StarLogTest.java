package com.xingkong1983.star.log;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class StarLogTest {


	
	@Test
	void testLog() {
		int i = 0;
		int code = 4;
		while (i < 10000) {
			log.info("程序启动了");
			log.error("用户XXX登录失败了");
			log.warn("用户XXX登录次数过多");
			log.debug("用户的验证码时："+code);
			//StarLog.print("這是一行測試信息");
			i++;
			
		}
		System.out.print("测试结束");
	}

}
