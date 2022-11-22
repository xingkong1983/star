package com.xingkong1983.star.core.tool;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CmdExecThread extends Thread {

	private String cmdText;

	private String outText = "";
	private String errText = "";

	public void run( ) {
		execSingleCmd();
		OsTool.print("线程已经结束");
	}

	public String execSingleCmd( ) {
		OsTool.print("[^_^] EXEC:" + cmdText);
		try {
			Runtime runtime = Runtime.getRuntime();

			Process process = runtime.exec("cmd /c " + cmdText);

			CmdStreamThread inStreamThread = new CmdStreamThread(process.getInputStream());
			inStreamThread.start();

			CmdStreamThread errStreamThread = new CmdStreamThread(process.getErrorStream());
			errStreamThread.start();

			int exitVal = process.waitFor();
			outText = inStreamThread.getResult();
			errText = errStreamThread.getResult();

			OsTool.print("Exited with error code " + exitVal);
			return "ok";
		} catch (Exception e) {
			OsTool.print(e);
			return OsTool.getErrorText(e);
		}
	}

	public CmdExecThread(String cmdText) {
		this.cmdText = cmdText;
	}
}
