package com.xgitlink.lib.git;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.xgitlink.lib.git.mo.RepoFileMo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@DisplayName("仓库工具测试")
@TestMethodOrder(OrderAnnotation.class)
public class RepoToolTest {

	private String TEST_DIR = "";

	@Test
	@Disabled
	@Order(4)
	@DisplayName("4.测试创建仓库")
	void testCreate() {
		RepoTool.create("c:/gitrepo/xingkong/test0", "", "", "",
				"D:/001-xplaza/x-bbs/x-bbs-code/x-gitcore-data/option/");
		RepoTool.create("c:/gitrepo/xingkong/test1", "#test1", "Actionscript", null,
				"D:/001-xplaza/x-bbs/x-bbs-code/x-gitcore-data/option/");
		RepoTool.create("c:/gitrepo/xingkong/test2", "#test2", null, "Apache-2.0",
				"D:/001-xplaza/x-bbs/x-bbs-code/x-gitcore-data/option/");
		RepoTool.create("c:/gitrepo/xingkong/test3", "#test3", "Agda", "Autoconf-exception-2.0",
				"D:/001-xplaza/x-bbs/x-bbs-code/x-gitcore-data/option/");
		RepoTool.create("c:/gitrepo/xingkong/test4", "#test4", "Ansible", "CC-BY-NC-SA-3.0-IGO",
				"D:/001-xplaza/x-bbs/x-bbs-code/x-gitcore-data/option/");
	}

	@Test
	@Disabled
	@Order(3)
	@DisplayName("3.测试通过网络克隆一个仓库")
	void createByclone() {
		RepoTool.createByclone("c:/gitrepo/xingkong/test4", "https://github.com/xingkong1983/Tailwind-CSS-Lab.git");
	}

	@Test
//	@Disabled
	@Order(1)
	@DisplayName("2.测试获取分支列表")
	void testGetBranchList() {
		List<String> branchList = RepoTool.getBranchList("c:/gitrepo/xingkong/test4/.git");

		for (String branch : branchList) {
			log.info(branch);
		}
	}

	@Test
//	@Disabled
	@Order(2)
	@DisplayName("1.测试获取仓库对应目录信息")
	void testGetRepoFileList() {

		// OsTool.printEnv();

		List<RepoFileMo> repoFileList = null;
		log.info("====================================");
		log.info("测试获取仓库对应目录信息");
		log.info("====================================");
		try {
			log.info("====================================");
			log.info("测试空目录");
			log.info("====================================");
//			repoFileList = RepoTool.getRepoFileList("c:/gitrepo/xingkong/test4/.git", "refs/heads/main", "");
			log.info("====================================");
			log.info("测试b-dir/b001-dir");
			log.info("====================================");

			repoFileList = RepoTool.getRepoFileList("c:/gitrepo/xingkong/test4/.git", "refs/heads/main",
					"b-dir/b001-dir");

			log.info("====================================");
			log.info("测试a-dir");
			log.info("====================================");

			repoFileList = RepoTool.getRepoFileList("c:/gitrepo/xingkong/test4/.git", "refs/heads/main", "b-dir");
		} catch (Exception e) {
			log.info("", e);
		}

	}
}
