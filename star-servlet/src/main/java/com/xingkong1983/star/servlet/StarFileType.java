package com.xingkong1983.star.servlet;

import java.util.HashMap;
import java.util.Map;

public enum StarFileType {
	DOC("文档", 0),
	XLS("表格", 1),
	XLSX("表格", 2),
	PDF("pdf", 3),
	IMG("图片", 4),
	DVD("视频", 5),
	PKG("压缩包", 6);

	private final String TYPE_NAME;
	private final int TYPE_VALUE;

	StarFileType(String type_name, int type_value) {
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
		
		fileType.put(".jpg", StarFileType.IMG.getValue());
		fileType.put(".jpeg", StarFileType.IMG.getValue());
		fileType.put(".png", StarFileType.IMG.getValue());
		fileType.put(".webp", StarFileType.IMG.getValue());
		fileType.put(".gif", StarFileType.IMG.getValue());

		fileType.put(".doc", StarFileType.DOC.getValue());
		fileType.put(".docx", StarFileType.DOC.getValue());
		fileType.put(".txt", StarFileType.DOC.getValue());
		fileType.put(".xls", StarFileType.XLS.getValue());
		fileType.put(".xlsx", StarFileType.XLSX.getValue());
		fileType.put(".pdf", StarFileType.PDF.getValue());

		fileType.put(".mp4", StarFileType.DVD.getValue());
		fileType.put(".rm", StarFileType.DVD.getValue());
		fileType.put(".rar", StarFileType.PKG.getValue());
		fileType.put(".zip", StarFileType.PKG.getValue());

	}
}
