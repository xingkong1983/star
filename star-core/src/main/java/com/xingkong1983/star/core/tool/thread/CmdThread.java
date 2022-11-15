package com.xingkong1983.star.core.tool.thread;

import java.io.InputStream;

import com.xingkong1983.star.core.tool.OsTool;

import lombok.Data;

@Data
public class CmdThread extends Thread {
	
	 
	/**
	 * 流处理
	 * @param stream
	 */
	private static void processStream(InputStream stream) {
		
		
	}
	private String cmdText;
	
	private String outText="";
	private String errText = "";

	public void run() {
		execSingleCmd();
		OsTool.print("线程已经结束");
	}
	
	public String execSingleCmd() {
		OsTool.print("[^_^] EXEC:"+ cmdText);
		try {
			Runtime runtime = Runtime.getRuntime();
			
			Process process = runtime.exec("cmd /c "+cmdText);
			
			StreamThread inStreamThread = new StreamThread(process.getInputStream());
			inStreamThread.start();
			
			StreamThread errStreamThread = new StreamThread(process.getErrorStream());
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

	public CmdThread(String cmdText) {
		this.cmdText = cmdText;
	}
}
