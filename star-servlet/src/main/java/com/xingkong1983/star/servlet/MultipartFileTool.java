package com.xingkong1983.star.servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import org.springframework.web.multipart.MultipartFile;

import com.xingkong1983.star.core.tool.OsTool;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MultipartFileTool {

	/**
	 * 上传图片
	 * 
	 * @param multiFile
	 * @param fileName
	 */
	public static void save(MultipartFile multiFile, String fileName) {
		try {
			File f = new File(fileName);
			if (!f.exists()) {
				f.mkdirs();
			}
			multiFile.transferTo(f);
			log.info(multiFile.getOriginalFilename());
		} catch (Exception e) {
			log.error("", e);
		}
	}

	/**
	 * 将图片转换成Base64编码
	 * 
	 * @param imgFile 待处理图片
	 * @return
	 */
	public static String getImgStr(MultipartFile imgFile) {
		// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理

		InputStream in = null;
		byte[] data = null;
		// 读取图片字节数组
		try {
			in = imgFile.getInputStream();
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			log.error("", e);
		} finally {
			OsTool.close(in);
		}
		return Base64.getEncoder().encodeToString(data);
	}

	/**
	 * 将图片转换成Base64编码
	 * 
	 * @param imgFile 待处理图片
	 * @return
	 */
	public static String imageToBase64(MultipartFile imgFile) {
		// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理

		InputStream in = null;
		byte[] data = null;
		// 读取图片字节数组
		try {
			in = imgFile.getInputStream();
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			log.error("",e);
		}
		return Base64.getEncoder().encodeToString(data);
	}

}
