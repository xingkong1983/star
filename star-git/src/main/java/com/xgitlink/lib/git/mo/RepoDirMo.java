package com.xgitlink.lib.git.mo;

import java.util.List;

import com.xingkong1983.star.core.tool.IPrint;

import lombok.Data;

@Data
public class RepoDirMo implements IPrint {

	RepoCommitMo commitMo;
	List<RepoFileMo> fileMoList;

	public RepoDirMo(RepoCommitMo repoCommitMo, List<RepoFileMo> repoFileMoList) {
		this.commitMo = repoCommitMo;
		this.fileMoList = repoFileMoList;

	}
}
