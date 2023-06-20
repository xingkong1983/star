package com.xingkong1983.star.core.tool;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j          
class OsToolTest {


	@Test
	void testSingleCmd() {
		
		
		String cmd[] = {"netstat", "-ano"};
		BizCmd bizCmd = new BizCmd(cmd);
		String result = bizCmd.exec();
		log.info("结果是"+result);
		log.info(bizCmd.getOutText());
		log.info("testSingleCmd end");

	}

	
}
