package com.xgitlink.lib.git;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.InitCommand;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;

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
	public static boolean create( String repoPath ) {
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
	public static boolean createByclone( String repoPath, String remoteRepoUrl ) {
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
	public static Repository open( String repoPath ) throws IOException {
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
	public static Git openToGit( String repoPath ) throws IOException {
		File repoFile = new File(repoPath);
		Git git = Git.open(repoFile);
		return git;
	}

	/**
	 * 获取仓库某个路径下的所有文件和最后的提交记录
	 * @param repoPath
	 * @param branchName
	 * @param dirPath
	 * @return
	 * @throws RevisionSyntaxException
	 * @throws NoHeadException
	 * @throws GitAPIException
	 */
	public static RepoDirMo getRepoDirMo( String repoPath, String branchName, String dirPath )
			throws RevisionSyntaxException, NoHeadException, GitAPIException {
		Repository repository = null;
		Git git = null;
		Ref head;
		RevWalk walk = null;
		TreeWalk treeWalk = null;
		RepoCommitMo repoCommitMo = null;
		List<RepoFileMo> repoFileMoList = new ArrayList<RepoFileMo>();
		try {
			repository = open(repoPath);
			git = new Git(repository);
			head = repository.exactRef(branchName);
			walk = new RevWalk(repository);
			if (head != null && head.getObjectId() != null) {
				RevCommit commit = walk.parseCommit(head.getObjectId());
				repoCommitMo = new RepoCommitMo(commit);
				RevTree tree = commit.getTree();
				treeWalk = new TreeWalk(walk);
				treeWalk.reset(tree);
				treeWalk.setRecursive(true);
			
				while (treeWalk.next()) {
					RepoFileMo repoFileMo = new RepoFileMo(treeWalk);
					repoFileMoList.add(repoFileMo);

					String filePath = treeWalk.getPathString();
					Iterable<RevCommit> commitLog = git.log().add(repository.resolve(Constants.HEAD)).addPath(filePath)
							.call();
					for (RevCommit revCommit : commitLog) {
						repoFileMo.setRepoCommitMo(revCommit);
						break;
					}

				}
				log.info("数据读取完毕");
			} else {
				log.info("这是一个空仓库");
			}
		} catch (IOException e) {
			log.error("", e);
		} finally {
			OsTool.close(walk);
			OsTool.close(treeWalk);
			OsTool.close(repository);

		}
		RepoDirMo repoDirMo = new RepoDirMo(repoCommitMo, repoFileMoList);
		return repoDirMo;
	}

	/**
	 * 获取分支列表
	 * 
	 * @param repoPath
	 * @return
	 */
	public static List<String> getBranchList( String repoPath ) {
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
