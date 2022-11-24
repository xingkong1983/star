package com.xgitlink.lib.git.mo;

import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.TreeWalk;

import com.xingkong1983.star.core.print.IPrint;

import lombok.Data;

@Data
public class RepoFileMo implements IPrint {
	
	private String path;
	private String name;
	private Boolean isDir;
	private RepoCommitMo commitMo;

	public RepoFileMo(TreeWalk treeWalk ) {
		this.name = treeWalk.getNameString();
		this.path = treeWalk.getPathString();
		this.isDir = treeWalk.isSubtree() ? true : false;
	}
	
	public void setRepoCommitMo(RevCommit commit) {
		this.commitMo = new RepoCommitMo(commit);
	}
}
