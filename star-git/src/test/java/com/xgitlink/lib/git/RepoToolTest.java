package com.xgitlink.lib.git;

import java.util.List;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import com.xgitlink.lib.git.mo.RepoDirMo;
import com.xingkong1983.star.core.tool.OsTool;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@DisplayName("仓库工具测试")
public class RepoToolTest {

	private String TEST_DIR = "";

	@Test
	@Order(4)
	@DisplayName("测试创建仓库")
	void testCreate() {
		RepoTool.create("D:/000-gitrepo/redis.git");
	}
//	
//
//	@Test
//	@Order(3)
//	@DisplayName("测试通过网络克隆一个仓库")
//	void createByclone() {
//		RepoTool.createByclone("D:/git-repo/Tailwind-CSS-Lab.git",
//				"https://github.com/xingkong1983/Tailwind-CSS-Lab.git");
//	}
//
	@Test
	@Order(2)
	@DisplayName("测试获取分支列表")
	void testGetBranchList() {
		List<String> branchList = RepoTool.getBranchList("D:/000-gitrepo/Tailwind-CSS-Lab/.git");

		for (String branch : branchList) {
			log.info(branch);
		}
	}

	@Test
	@Order(1)
	@DisplayName("测试获取仓库对应目录信息")
	void testGetRepoDirMo() {

		//OsTool.printEnv();

		RepoDirMo repoDirMo = null;
		log.info("====================================");
		log.info("测试获取仓库对应目录信息");
		log.info("====================================");
		try {
			repoDirMo = RepoTool.getRepoDirMo("D:/000-gitrepo/Tailwind-CSS-Lab/.git", "refs/heads/main", "");
		} catch (RevisionSyntaxException | GitAPIException e) {
			log.info("", e);
		}
		repoDirMo.print();
	}
}
