package com.xingkong1983.star.core.tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@EqualsAndHashCode(callSuper = true)
public class CmdInputStreamThread extends Thread {
	private InputStream stream;
	private String result="";

	public void run() {
		String line = null;

		try (BufferedReader in = new BufferedReader(new InputStreamReader(stream, Charset.forName("UTF-8")));) {
			while ((line = in.readLine()) != null) {
				this.result += line + "\r\n";
			}
		} catch (IOException e) {
			log.error("[-_-]",e);
		}
		log.info("StreamThread-"+threadId()+" is end.");
	}

	public CmdInputStreamThread(InputStream stream) {
		this.stream = stream;
	}
}
