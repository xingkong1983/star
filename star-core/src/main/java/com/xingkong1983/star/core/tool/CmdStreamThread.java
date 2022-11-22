package com.xingkong1983.star.core.tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CmdStreamThread extends Thread {
	private InputStream stream;
	private String result="";

	public void run() {
		String line = null;

		try (BufferedReader in = new BufferedReader(new InputStreamReader(stream, Charset.forName("GBK")));) {
			while ((line = in.readLine()) != null) {
				this.result += line + "\r\n";
				//OsTool.print(line);
			}
		} catch (IOException e) {
			OsTool.print(e);
		}
		OsTool.print("StreamThread-"+this.getId()+" is end.");
	}

	public CmdStreamThread(InputStream stream) {
		this.stream = stream;
	}
}
