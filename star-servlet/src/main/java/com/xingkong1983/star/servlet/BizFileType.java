package com.xingkong1983.star.servlet;

import java.util.HashMap;
import java.util.Map;

public enum BizFileType {
	DOC("文档", 0),
	XLS("表格", 1),
	XLSX("表格", 2),
	PDF("pdf", 3),
	IMG("图片", 4),
	DVD("视频", 5),
	PKG("压缩包", 6);

	private final String TYPE_NAME;
	private final int TYPE_VALUE;

	BizFileType(String type_name, int type_value) {
		TYPE_NAME = type_name;
		TYPE_VALUE = type_value;
	}

	public int getValue( ) {
		return TYPE_VALUE;
	}
	
	public static final Map<String, Integer> fileType = new HashMap();
	
	public static int get(String suffix) {
		return fileType.get(suffix);
	}

	static {
		
		fileType.put(".jpg", BizFileType.IMG.getValue());
		fileType.put(".jpeg", BizFileType.IMG.getValue());
		fileType.put(".png", BizFileType.IMG.getValue());
		fileType.put(".webp", BizFileType.IMG.getValue());
		fileType.put(".gif", BizFileType.IMG.getValue());

		fileType.put(".doc", BizFileType.DOC.getValue());
		fileType.put(".docx", BizFileType.DOC.getValue());
		fileType.put(".txt", BizFileType.DOC.getValue());
		fileType.put(".xls", BizFileType.XLS.getValue());
		fileType.put(".xlsx", BizFileType.XLSX.getValue());
		fileType.put(".pdf", BizFileType.PDF.getValue());

		fileType.put(".mp4", BizFileType.DVD.getValue());
		fileType.put(".rm", BizFileType.DVD.getValue());
		fileType.put(".rar", BizFileType.PKG.getValue());
		fileType.put(".zip", BizFileType.PKG.getValue());

	}
}
