package com.xgitlink.lib.git.mo;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BranchVo {

	/**
	 * 分支编号
	 */
	private String id;
	
	/**
	 * 分支名称
	 */
	private String name;
	
	/**
	 * 分支创建人名称
	 */
	private String creatorName;
	
	/**
	 * 分支创建人邮箱
	 */
	private String creatorEmail;
	
	/**
	 * 分支创建时间
	 */
	private LocalDateTime creatorTime;
	
	/**
	 * 分支代码最后修改时间
	 */
	private LocalDateTime updateTime;
	
	/**
	 * 分支代码最后修改人名称
	 */
	private String updateName;
	
	/**
	 * 分支代码最后修改人邮箱
	 */
	private String updateEmail;
	
	/**
	 * 分支代码指定时间内提交次数
	 */
	private int commitCount;
	
}
