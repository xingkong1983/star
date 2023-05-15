package com.xgitlink.lib.git;

import java.util.List;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.xgitlink.lib.git.mo.RepoDirMo;

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
		RepoTool.create("D:/000-gitrepo/redis.git");
	}
	
	
	@Test
	@Disabled
	@Order(3)
	@DisplayName("3.测试通过网络克隆一个仓库")
	void createByclone() {
		RepoTool.createByclone("D:/git-repo/test2.git",
				"https://github.com/xingkong1983/Tailwind-CSS-Lab.git");
	}

	@Test
	@Disabled
	@Order(2)
	@DisplayName("2.测试获取分支列表")
	void testGetBranchList() {
		List<String> branchList = RepoTool.getBranchList("D:/000-gitrepo/Tailwind-CSS-Lab/.git");

		for (String branch : branchList) {
			log.info(branch);
		}
	}

	@Test
	@Order(1)
	@DisplayName("1.测试获取仓库对应目录信息")
	void testGetRepoFileList() {

		//OsTool.printEnv();

		RepoDirMo repoDirMo = null;
		log.info("====================================");
		log.info("测试获取仓库对应目录信息");
		log.info("====================================");
		try {
			log.info("====================================");
			log.info("测试空目录");
			log.info("====================================");
			repoDirMo = RepoTool.getRepoFileList("D:/000-gitrepo/Tailwind-CSS-Lab/.git", "refs/heads/main", "");
			log.info("====================================");
			log.info("测试b-dir/b001-dir");
			log.info("====================================");
			
			repoDirMo = RepoTool.getRepoFileList("D:/000-gitrepo/Tailwind-CSS-Lab/.git", "refs/heads/main", "b-dir/b001-dir");
			
			log.info("====================================");
			log.info("测试a-dir");
			log.info("====================================");
			
			repoDirMo = RepoTool.getRepoFileList("D:/000-gitrepo/Tailwind-CSS-Lab/.git", "refs/heads/main", "a-dir");
		} catch (Exception e) {
			log.info("", e);
		}
		repoDirMo.print();
	}
}
