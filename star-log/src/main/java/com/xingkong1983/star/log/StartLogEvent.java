package com.xingkong1983.star.log;

import lombok.Data;

@Data
public class StartLogEvent {
	public static int FLAG_LOG = 0;
	public static int FLAG_BUSINESS = 1; 
	private int flag;
	private String message;
	
	public StartLogEvent(int flag, String message) {
		this.flag = flag;
		this.message = message;
	}
}
