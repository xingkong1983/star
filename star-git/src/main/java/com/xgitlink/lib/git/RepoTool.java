package com.xgitlink.lib.git;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.InitCommand;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.FileMode;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.eclipse.jgit.treewalk.filter.PathFilterGroup;

import com.xgitlink.lib.git.mo.RepoCommitMo;
import com.xgitlink.lib.git.mo.RepoDirMo;
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

	public static RepoCommitMo getLastCommitMo(Repository db, String branchName)
			throws MissingObjectException, IncorrectObjectTypeException, IOException {
		Ref head;
		RevWalk walk = new RevWalk(db);
		head = db.exactRef(branchName);

		RepoCommitMo repoCommitMo = null;

		if (head != null && head.getObjectId() != null) {
			RevCommit commit = walk.parseCommit(head.getObjectId());
			repoCommitMo = new RepoCommitMo(commit);
			OsTool.close(walk);
		}
		return repoCommitMo;
	}

	/**
	 * 获取指定文件路径的提交日志
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 * @throws GitAPIException
	 * @throws AmbiguousObjectException
	 * @throws IncorrectObjectTypeException
	 * @throws MissingObjectException
	 * @throws NoHeadException
	 * @throws RevisionSyntaxException
	 */
	public static RevCommit getFileCommitLog(Git git, Repository db, String filePath)
			throws RevisionSyntaxException, NoHeadException, MissingObjectException, IncorrectObjectTypeException,
			AmbiguousObjectException, GitAPIException, IOException {
		Iterable<RevCommit> commitLog = git.log().add(db.resolve(Constants.HEAD)).addPath(filePath).call();
		for (RevCommit revCommit : commitLog) {
			return revCommit;
		}
		return null;
	}

	/**
	 * 获取仓库文件列表 根据仓库地址获取某个分支下某个目录的详细文件列表，只列出当前目录下的所有文件，不进行遍历操作
	 * 
	 * @param repoPath   仓库地址
	 * @param branchName 仓库分支名称
	 * @param dirPath    仓库子目录路径
	 * @return
	 */
	public static RepoDirMo getRepoFileList(String repoPath, String branchName, String dirPath) {
		Repository db = null;
		try {
			db = open(repoPath);
			getDirObjectId(db, branchName, dirPath);
		} catch (Exception e) {
			log.error("遍历出现错误", e);
		} finally {
			OsTool.close(db);
		}
		return null;
	}

	/**
	 * 获取仓库某个路径下的所有文件和最后的提交记录
	 * 
	 * @param repoPath
	 * @param branchName
	 * @param dirPath
	 * @return
	 * @throws RevisionSyntaxException
	 * @throws NoHeadException
	 * @throws GitAPIException
	 */
//	public static RepoDirMo getRepoDirMo(String repoPath, String branchName, String dirPath)
//			throws RevisionSyntaxException, NoHeadException, GitAPIException {
//		Repository db = null;
//		Git git = null;
//
//		TreeWalk treeWalk = null;
//		// RepoCommitMo repoCommitMo = null;
//		// List<RepoFileMo> repoFileMoList = new ArrayList<RepoFileMo>();
//		try {
//			db = open(repoPath);
//			git = new Git(db);
//			treeWalk = new TreeWalk(db);
//			// repoCommitMo = getLastCommitMo(db, branchName);
//			treeWalk.setRecursive(false);
//			treeWalk.reset();
//
//			while (treeWalk.next()) {
//				// RepoFileMo repoFileMo = new RepoFileMo(treeWalk);
//				// repoFileMoList.add(repoFileMo);
//				String filePath = treeWalk.getPathString();
//				log.info(filePath);
//				// RevCommit revCommit = getFileCommitLog(git, db, filePath);
//				// repoFileMo.setRepoCommitMo(revCommit);
//			}
//			log.info("数据读取完毕");
//
//		} catch (Exception e) {
//			log.error("遍历出现错误", e);
//		} finally {
//
//			OsTool.close(treeWalk);
//			OsTool.close(db);
//
//		}
//		// RepoDirMo repoDirMo = new RepoDirMo(repoCommitMo, repoFileMoList);
//		return null;
//	}

	/**
	 * 获取子目录的提交编号
	 * 
	 * @param db
	 * @param path
	 * @param commit
	 * @return
	 */
	public static ObjectId getDirObjectId(Repository db, String branchName, String path) {
		ObjectId objectId = null;
		Git git = null;
		Ref head = null;
		RevWalk rw = null;
		TreeWalk tw = null;
		try {
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
			
			String curPath = tw.getPathString();
			List<RepoFileMo> repoFileMoList = new ArrayList<RepoFileMo>();
			RevCommit revCommit = getFileCommitLog(git, db, curPath);
			RepoFileMo repoFileMo = new RepoFileMo(tw);
			repoFileMo.setRepoCommitMo(revCommit);
			repoFileMoList.add(repoFileMo);
			while (tw.next()) {
				curPath = tw.getPathString();
				if (curPath.startsWith(path)) {
					if (tw.isSubtree()) {
						log.debug("当前的路径是：" + curPath);
					} else {
						log.debug("当前的文件是：" + curPath);
					}
					
					revCommit = getFileCommitLog(git, db, curPath);
					repoFileMo = new RepoFileMo(tw);
					repoFileMo.setRepoCommitMo(revCommit);
					repoFileMoList.add(repoFileMo);
				}
			}
			log.info(repoFileMo.toJson());

		} catch (Exception e) {
			log.error("错误消息", e);
		} finally {
			OsTool.close(rw);
			OsTool.close(tw);
		}
		return objectId;
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
}
