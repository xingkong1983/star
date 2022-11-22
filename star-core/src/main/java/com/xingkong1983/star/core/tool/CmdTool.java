package com.xingkong1983.star.core.tool;

public class CmdTool {
	/**
	 * @param cmdText
	 * @return
	 */

	public static String exec( String cmdText ) {
		CmdExecThread cmdThread = new CmdExecThread(cmdText);

		cmdThread.start();
		OsTool.print("ThreadID:" + cmdThread.getId());
		try {
			cmdThread.join();
		} catch (InterruptedException e) {
			OsTool.print(e);
		}
		OsTool.print("执行完毕");
		return cmdThread.getOutText();
	}
}
