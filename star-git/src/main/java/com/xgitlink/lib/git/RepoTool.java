package com.xgitlink.lib.git;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.InitCommand;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.http.server.GitServlet;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;

import com.xgitlink.lib.git.mo.RepoFileMo;
import com.xingkong1983.star.core.tool.OsTool;
import com.xingkong1983.star.core.tool.StringTool;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RepoTool {
	/**
	 * 创建一个仓库
	 * 
	 * @param repoPath 仓库的路径
	 * @return 创建成功返回为真，创建失败返回为假
	 */
	public static boolean create(String repoPath) {
		File repoFile = new File(repoPath);
		if (repoFile.exists() && repoFile.isDirectory()) {
			log.info("[-_-] err: repo is exist, create repo failed, dir: " + repoFile);
			return false;
		}

		// 创建仓库
		InitCommand initCmd = Git.init().setGitDir(repoFile);
		try (Git git = initCmd.call()) {
			log.info("[^_^] ok: create repo ok, repo: " + repoFile);
		} catch (Exception e) {
			log.error("", e);
			return false;
		}
		log.info("[^_^] Create Repo ok.");
		return true;
	}

	/**
	 * 通过克隆创建一个仓库
	 * 
	 * @param repoPath
	 * @param remoteRepoUrl
	 * @return
	 */
	public static boolean createByclone(String repoPath, String remoteRepoUrl) {
		File repoFile = new File(repoPath);
		// 检查仓库目录是否已经存在
		if (repoFile.exists() && repoFile.isDirectory()) {
			log.info("[-_-] err: repo is exist, clone repo failed, dir: " + repoFile);
			return false;
		}

		// 创建仓库
		CloneCommand cloneCmd = Git.cloneRepository().setURI(remoteRepoUrl).setGitDir(repoFile);
		try (Git git = cloneCmd.call()) {
			log.info("[^_^] ok: clone repo ok, repo: " + repoFile);
		} catch (Exception e) {
			log.error("", e);
			return false;
		}
		log.info("[^_^] CreateByclone ok.");
		return true;
	}

	/**
	 * 打开仓库，返回Repository对象
	 * 
	 * @param repoPath
	 * @return
	 * @throws IOException
	 */
	public static Repository open(String repoPath) throws IOException {
		File repoFile = new File(repoPath);
		Repository repository = new FileRepositoryBuilder().setGitDir(repoFile).build();
		return repository;
	}

	/**
	 * 打开仓库，返回 Git 对象
	 * 
	 * @param repoPath
	 * @return
	 * @throws IOException
	 */
	public static Git openToGit(String repoPath) throws IOException {
		File repoFile = new File(repoPath);
		Git git = Git.open(repoFile);
		return git;
	}

	/**
	 * 获取指定文件路径的提交日志
	 * 
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public static RepoFileMo getRepoFileMoFromTreeWalk(TreeWalk tw, Git git, Repository db) throws Exception {
		String curPath = tw.getPathString();
		RepoFileMo repoFileMo = new RepoFileMo(tw);
		Iterable<RevCommit> commitLog = git.log().add(db.resolve(Constants.HEAD)).addPath(curPath).call();
		for (RevCommit revCommit : commitLog) {
			repoFileMo.setCommitInfo(revCommit);
		}
		return repoFileMo;
	}

	/**
	 * 获取仓库文件列表 根据仓库地址获取某个分支下某个目录的详细文件列表，只列出当前目录下的所有文件，不进行遍历操作
	 * 
	 * @param repoPath   仓库地址
	 * @param branchName 仓库分支名称
	 * @param dirPath    仓库子目录路径
	 * @return
	 */
	public static List<RepoFileMo> getRepoFileList(String repoPath, String branchName, String path) {
		Repository db = null;

		Git git = null;
		Ref head = null;
		RevWalk rw = null;
		TreeWalk tw = null;
		List<RepoFileMo> repoFileMoList = new ArrayList<RepoFileMo>();
		try {
			db = open(repoPath);
			git = new Git(db);
			head = db.exactRef(branchName);
			if (head == null || head.getObjectId() == null) {

				return null;
			}
			RevCommit commit = null;
			rw = new RevWalk(db);
			commit = rw.parseCommit(head.getObjectId());

			tw = new TreeWalk(db);
			tw.reset(commit.getTree());

			if (StringTool.isNotEmpty(path)) {
				while (tw.next()) {
					String curPath = tw.getPathString();
					if (tw.isSubtree()) {
						if (!path.equals(curPath)) {

							if (path.startsWith(curPath)) {
								log.debug("进入路径：" + curPath);
								tw.enterSubtree();
							} else {
								log.debug("不进入路径：" + curPath);
							}
							continue;
						} else {
							log.debug("找到路径：" + curPath);
							if (path.startsWith(curPath)) {
								RepoFileMo repoFileMo = getRepoFileMoFromTreeWalk(tw, git, db);
								repoFileMoList.add(repoFileMo);
								tw.enterSubtree();
							}
							break;
						}
					} else {
						continue;
					}
				}
			}
			log.debug("遍历目录：");

			while (tw.next()) {
				String curPath = tw.getPathString();
				if (curPath.startsWith(path)) {
					if (tw.isSubtree()) {
						log.debug("当前的路径是：" + curPath);
					} else {
						log.debug("当前的文件是：" + curPath);
					}

					RepoFileMo repoFileMo = getRepoFileMoFromTreeWalk(tw, git, db);
					repoFileMoList.add(repoFileMo);
				}
			}

			for (RepoFileMo curRepoFileMo : repoFileMoList) {

				log.info(curRepoFileMo.toString());
			}

		} catch (Exception e) {
			log.error("错误消息", e);
		} finally {
			OsTool.close(git);
			OsTool.close(rw);
			OsTool.close(tw);
			OsTool.close(db);
		}
		return repoFileMoList;
	}

	/**
	 * 找到对应的目录
	 * 
	 * @param path
	 */
	public void findDir(String path) {

	}

	/**
	 * 获取分支列表
	 * 
	 * @param repoPath
	 * @return
	 */
	public static List<String> getBranchList(String repoPath) {
		List<String> branchList = new ArrayList<>();
		Git git = null;
		try {
			git = openToGit(repoPath);
			List<Ref> refList = git.branchList().setListMode(ListBranchCommand.ListMode.ALL).call();
			for (Ref ref : refList) {
				branchList.add(ref.getName());
			}

		} catch (GitAPIException | IOException e) {
			log.error("获取不到分支列表", e);
		} finally {
			OsTool.close(git);
		}

		return branchList;
	}

	/**
	 * 
	 * @param REPO_PATH
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public static void httpServlet(String repoPath, HttpServletRequest request, HttpServletResponse response) throws IOException {
		File repository = new File(repoPath);
		Git git;
		try {
			git = Git.open(repository);
			// Create and configure the Git servlet
			GitServlet gitServlet = new GitServlet();
			gitServlet.setRepositoryResolver((req, name) -> git.getRepository());
			gitServlet.service(request, response);
			
		} catch ( ServletException e) {
			log.error("git仓库出现错误",e);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		finally {
			
		}
	}
}
