package com.xingkong1983.star.servlet;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import javax.imageio.ImageIO;

import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.xingkong1983.star.biz.BizTool;
import com.xingkong1983.star.core.print.IPrint;
import com.xingkong1983.star.core.tool.FileTool;
import com.xingkong1983.star.core.tool.IDTool;
import com.xingkong1983.star.core.tool.ImgTool;
import com.xingkong1983.star.core.tool.OsTool;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class BizMultipartFile implements IPrint {

	public BizMultipartFile(MultipartFile multiFile) {
		this.multiFile = multiFile;
		uuid = IDTool.genUUID();
		originName = multiFile.getOriginalFilename();
		int lastIndex = originName.lastIndexOf(".");
		suffix = originName.substring(lastIndex).toLowerCase();
		type = BizFileType.get(suffix);
		this.size = BigDecimal.valueOf(multiFile.getSize());
		this.createTime = new Date();
	}

	/**
	 * 上传的文件对象
	 */
	MultipartFile multiFile;

	/**
	 * 文件的唯一编号
	 */
	private String uuid;

	/**
	 * 文件名称
	 */
	private String name;

	/**
	 * 文件原始名称
	 */
	private String originName;

	/**
	 * 文件存放路径父路径
	 */
	private String parentPath;

	/**
	 * 文件存放全路径
	 */
	private String fullPath;

	/**
	 * 文件类型 0 文档 1 表格 2 表格 3 pdf 4 图片 5 视频 6 压缩包
	 */
	private Integer type;

	/**
	 * 文件大小(KB) 计算
	 */
	private BigDecimal size;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 文件名后缀
	 */
	private String suffix;

	/**
	 * 判断是否是图片
	 */
	public boolean isImage( ) {
		if (type != BizFileType.IMG.getValue()) {
			return false;
		}

		if (ObjectUtils.isEmpty(multiFile) || multiFile.getSize() <= 0) {
			return false;
		}

		BufferedImage image = null;
		try {
			image = ImageIO.read(multiFile.getInputStream());
			if (image == null || image.getWidth() <= 0 || image.getHeight() <= 0) {
				return false;
			}
			return true;
		} catch (Exception e) {
			log.error(OsTool.getErrorText(e), e);
			return false;
		}
	}

	/**
	 * 判断是否是文档文件
	 * 
	 * @return
	 */
	public boolean isDoc( ) {
		if (type != BizFileType.DOC.getValue()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 保存图片文件
	 */
	public void saveImage( ) {
		Boolean isImage = isImage();
		BizTool.ASSERT_FILE.isTrue(isImage, "图片格式不对");
		save();
	}

	/**
	 * 保存文档文件
	 */
	public void saveDoc( ) {
		Boolean isDoc = isDoc();
		BizTool.ASSERT_FILE.isTrue(isDoc, "文档格式不对");
		save();
	}

	/**
	 * 保存文件
	 */
	public void save( ) {
		File f = new File(this.fullPath);
		try {

			if (!f.getParentFile().exists()) {
				f.mkdirs();
			}
			if (!f.exists()) {
				f.createNewFile();
			}
			this.multiFile.transferTo(f);
		} catch (Exception e) {
			log.error(OsTool.getErrorText(e), e);
			BizTool.ASSERT_FILE.throwException("文件保存失败:" + f.getName());
		}
	}

	/**
	 * 删除文件
	 */
	public void delete( ) {
		FileTool.delete(this.fullPath);
	}

	/**
	 * 重置大小并保存文件
	 * 
	 * @param path
	 * @param width
	 * @param height
	 * @throws IOException
	 */
	public void resizeTo( String path, int width, int height ) throws IOException {
		ImgTool.resizeImage(fullPath, path, width, height);
	}

}
