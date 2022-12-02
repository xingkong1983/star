package com.xingkong1983.star.biz;

import org.junit.jupiter.api.Test;

import com.xingkong1983.star.biz.mock.BizApi;
import com.xingkong1983.star.biz.mock.BizGlobalHandler;
import com.xingkong1983.star.biz.vo.BizResponseVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class BizToolTest {
	@Test
	void testBizApi( ) {
	
		BizGlobalHandler bizGlobalHandler = new BizGlobalHandler();
		
		BizResponseVo resVo = bizGlobalHandler.login("root", "keep");

		log.info(resVo.toJson());
		
		 resVo = bizGlobalHandler.login("root", "1234");

		log.info(resVo.toJson());

		resVo = bizGlobalHandler.login(null, "keep");
		log.info(resVo.toJson());

		resVo = bizGlobalHandler.login("root", null);
		log.info(resVo.toJson());
	}


}
