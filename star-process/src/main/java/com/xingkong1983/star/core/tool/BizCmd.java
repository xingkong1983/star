package com.xingkong1983.star.core.tool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data

public class BizCmd {

	private String cmdText;

	private String outText = "";
	private String errText = "";
	private ProcessBuilder pb = null;

	public BizCmd(String[] commandList) {

		List<String> cmdList = new ArrayList<>();
		cmdList.add("cmd");
		cmdList.add("/c");
		for (String cmdText : commandList) {
			cmdList.add(cmdText);
		}

		pb = new ProcessBuilder(cmdList);
		Map<String, String> env = pb.environment();
		env.put("LANG", "zh_CN.UTF-8");
		env.put("LC_ALL", "zh_CN.UTF-8");

	}

	public String exec() {
		log.info("[^_^] EXEC:" + cmdText);
		try {

			Process process = pb.start();

			CmdInputStreamThread inStreamThread = new CmdInputStreamThread(process.getInputStream());
			inStreamThread.start();

			CmdInputStreamThread errStreamThread = new CmdInputStreamThread(process.getErrorStream());
			errStreamThread.start();

			// TO: 写入数据
			int exitVal = process.waitFor();
			outText = inStreamThread.getResult();
			errText = errStreamThread.getResult();
//			log.info(outText);
//			log.info(errText);
			log.info("Exited with error code " + exitVal);
			return "ok";
		} catch (Exception e) {
			log.error("[-_-] execute cmd err.", e);
			return OsTool.getErrorText(e);
		}
	}

	public BizCmd(String cmdText) {
		this.cmdText = cmdText;
	}
}
