package com.xgitlink.lib.git.mo;

import java.util.List;

import com.xingkong1983.star.core.print.IPrint;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class RepoDirMo implements IPrint {

	RepoCommitMo commitMo;
	List<RepoFileMo> fileMoList;

	public RepoDirMo(RepoCommitMo repoCommitMo, List<RepoFileMo> repoFileMoList) {
		this.commitMo = repoCommitMo;
		this.fileMoList = repoFileMoList;

	}
	
	public void print() {
		for( RepoFileMo curFileMo:fileMoList) {
			String name = curFileMo.getIsDir()?curFileMo.getName():curFileMo.getPath();
			String isDir = curFileMo.getIsDir()?"目录":"文件";
			log.info(isDir+" "+name+","+curFileMo.getCommitMo().getEmail()+","+curFileMo.getCommitMo().getLog()+","+curFileMo.getCommitMo().getTime());
		}
	}
}
