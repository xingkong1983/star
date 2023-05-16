package com.xgitlink.lib.git.mo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.TreeWalk;

import com.xingkong1983.star.core.print.IPrint;
import com.xingkong1983.star.core.tool.DateTool;

import lombok.Data;

@Data
public class RepoFileMo implements IPrint {
	private static DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private String path;
	private String fileName;
	private Boolean isDir;
	
	private String name;
	private String email;
	private LocalDateTime time;
	private String log;
	private String sha1;
	

	public RepoFileMo(TreeWalk treeWalk ) {
		this.fileName = treeWalk.getNameString();
		this.path = treeWalk.getPathString();
		this.isDir = treeWalk.isSubtree() ? true : false;
	}
	
	public void setCommitInfo(RevCommit commit) {
		PersonIdent authoIdent = commit.getAuthorIdent();
		this.name = authoIdent.getName();
		this.email = authoIdent.getEmailAddress();
		this.time =DateTool.date2LocalDateTime(authoIdent.getWhen());
		this.log = commit.getShortMessage();
		this.sha1 =   commit.getId().name();
	}
	
	public String toString() {
		String typeStr = this.getIsDir()?"[DIR]":"[File]";
		String commitTime = DateTool.getString(time, DateTool.Format.CHINESE_DATETIME);
		String fileInfo = String.format("%s,%s,%s,由%s(%s)在 %s 提交,日志:%s", typeStr, path,fileName, name, email, commitTime, log);
		return fileInfo;
		
	}
}
