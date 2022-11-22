package com.xingkong1983.star.core.tool;

import org.junit.jupiter.api.Test;

class OsToolTest {


	@Test
	void testSingleCmd() {
		
		
		String cmd = "netstat -ano";
		String result = CmdTool.exec(cmd);
		OsTool.print(cmd);
		OsTool.print(result);
		OsTool.print("testSingleCmd end");

	}

	@Test
	void testCmdList() {
		String[] cmdList = new String[] { "netstat -ano" };
		String result;
		for (String cmdText : cmdList) {
			result = CmdTool.exec(cmdText);
			OsTool.print(cmdText);
			OsTool.print(result);
		}
		OsTool.print("testCmdList end");
	}
}
