package com.xgitlink.lib.git.mo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommitVo {

	private String commitId;

	private String name;

	private String email;

	private String nickName;

	private LocalDateTime createTime;

	private String message;

	private String hash;

	private String avatar;

	private String spaceName;

	private Long uid;

}
