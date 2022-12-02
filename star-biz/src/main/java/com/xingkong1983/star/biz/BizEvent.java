package com.xingkong1983.star.biz;

import lombok.Data;

@Data
public class BizEvent {
	private int code;
	private String name;
	private String message;

	public BizEvent(int code, String name, String message) {
		this.code = code;
		this.name = name;
		this.message = message;
	}
}
