package com.xingkong1983.star.servlet;

import java.util.HashMap;
import java.util.Map;

public enum BizFileType {
	OTHER("其他", -1),
	DOC("文档", 0),
	XLS("表格", 1),
	XLSX("表格", 2),
	PDF("pdf", 3),
	IMG("图片", 4),
	DVD("视频", 5),
	PKG("压缩包", 6),
	EXE("安装包", 7);

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
		if(suffix != null) {
			suffix = suffix.toLowerCase();
		}
		Integer type = fileType.get(suffix);
		if(type == null) {
			type = OTHER.getValue();
		}
		return type;
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
		
		fileType.put(".exe", BizFileType.EXE.getValue());
		fileType.put(".rpm", BizFileType.EXE.getValue());
		fileType.put(".bin", BizFileType.EXE.getValue());
		fileType.put(".deb", BizFileType.EXE.getValue());
		fileType.put(".dmg", BizFileType.EXE.getValue());

	}
}
