package com.xingkong1983.star.log;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class StarLogTest {


	
	@Test
	void testLog() {
		int i = 0;
		while (i < 10000) {
			log.info("打印信息信息");
			log.error("打印错误信息");
			log.warn("打印警告信息");
			log.debug("打印调试信息");
			//StarLog.print("這是一行測試信息");
			i++;
			
		}
		System.out.print("测试结束");
	}

}
