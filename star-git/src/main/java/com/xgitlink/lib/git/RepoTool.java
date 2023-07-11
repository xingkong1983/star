package com.xgitlink.lib.git;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;

import com.xgitlink.lib.git.mo.RepoFileMo;
import com.xingkong1983.star.core.tool.FileTool;
import com.xingkong1983.star.core.tool.OsTool;
import com.xingkong1983.star.core.tool.StringTool;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RepoTool {

	/**
	 * 创建一个仓库
	 * 
	 * @param repoPath    仓库的路径
	 * @param readMeText
	 * @param gitnore
	 * @param license
	 * @param templateDir gitignore 文件和 licnese
	 * @return 创建成功返回为真，创建失败返回为假
	 */
	public static boolean create(String repoPath, String readMeText, String gitignore, String license,
			String templateDir) {

		File repoFile = new File(repoPath);
		if (repoFile.exists() && repoFile.isDirectory()) {
			log.info("[-_-] err: repo is exist, create repo failed, dir: " + repoFile);
			return false;
		}
		Repository repository = null;
		Git git = null;
		try {

			repository = FileRepositoryBuilder.create(new File(repoPath, ".git"));
			repository.create();
			File localDir = repository.getWorkTree();
			String localPath = localDir.getPath();
			log.debug("============================");
			log.debug("localPath:" + localPath);
			log.debug("============================");

			git = new Git(repository);

			log.info("[^_^] ok: create repo ok, repo: " + repoPath);

			// 添加 ReadMe 文件,并把主分支设置为 main 分支

			String readMeFileName = localPath + "/ReadMe.md";

			boolean isNeedInit = false;
			if (StringTool.isNotEmpty(readMeText)) {

				isNeedInit = true;
				// 将新文件纳入git管理，不含删除的文件
				FileTool.writeText(readMeFileName, readMeText);
				git.add().addFilepattern("ReadMe.md").call();
				FileTool.delete(readMeFileName);

			}

			String gitignoreFileName = localPath + "/" + ".gitignore";
			if (StringTool.isNotEmpty(gitignore)) {
				isNeedInit = true;
				String tempGitignoreFileName = templateDir + "/gitignore/" + gitignore;
				FileTool.copyFile(tempGitignoreFileName, gitignoreFileName);
				git.add().addFilepattern(".gitignore").call();
				FileTool.delete(gitignoreFileName);
			}

			String licenseFileName = localPath + "/" + "LICENSE";
			if (StringTool.isNotEmpty(license)) {
				isNeedInit = true;
				String tempLicenseFileName = templateDir + "/license/" + license;

				FileTool.copyFile(tempLicenseFileName, licenseFileName);
				git.add().addFilepattern("LICENSE").call();
				FileTool.delete(licenseFileName);
			}
			if (isNeedInit) {
				git.commit().setMessage("Initial commit").call();
				if (hasBranch(git, "master")) {
					git.branchRename().setOldName("master").setNewName("main").call();
				}
			}

		} catch (Exception e) {
			log.error("", e);
			return false;
		} finally {
			OsTool.close(git);
			OsTool.close(repository);
		}

		log.info("[^_^] Create Repo ok.");
		return true;
	}

	/**
	 * 判断本地仓库是否存在有某分支，如果没有则创建
	 */
	private static boolean hasBranch(Git git, String branchName) throws GitAPIException {

		List<Ref> refList = git.branchList().call();
		String brancheNameStr = "refs/heads/" + branchName;
		for (Ref ref : refList) {
			if (ref.getName().equals(brancheNameStr)) {
				return true;
			}
		}
		return false;
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
	 * 获取根路径的提交日志
	 * 
	 * @param head
	 * @param rw
	 * @param git
	 * @param db
	 * @return
	 * @throws Exception
	 */
	public static RepoFileMo getRepoFileMoFromRoot(Ref head, RevWalk rw, Git git, Repository db) throws Exception {
		RepoFileMo repoFileMo = new RepoFileMo();
		RevCommit commit = rw.parseCommit(head.getObjectId());
		repoFileMo.setPath("");
		repoFileMo.setIsDir(true);
		repoFileMo.setFileName("");
		repoFileMo.setCommitInfo(commit);
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

			if (StringTool.isEmpty(path)) {
				RepoFileMo repoFileMo = getRepoFileMoFromRoot(head, rw, git, db);
				repoFileMoList.add(repoFileMo);
			} else {
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

			log.info("打印对象：");
			log.info("---------------------------------------------");
			for (RepoFileMo curRepoFileMo : repoFileMoList) {

				log.info(curRepoFileMo.toString());
			}
			log.info("---------------------------------------------");
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

}
