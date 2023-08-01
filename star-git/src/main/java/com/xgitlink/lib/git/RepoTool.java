package com.xgitlink.lib.git;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.ArchiveCommand;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.archive.ZipFormat;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.RefUpdate;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.RemoteConfig;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.treewalk.TreeWalk;

import com.xgitlink.lib.git.mo.CommitVo;
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
			String templateDir, String committerName, String committerEmail) {

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
			
			String defaultBranchName = "main";
			if (isNeedInit) {
				CommitCommand commitCommand = git.commit();
				if(StringTool.isNotEmpty(committerName) || StringTool.isNotEmpty(committerEmail)) {
					commitCommand.setCommitter(committerName, committerEmail);
				}
				commitCommand.setMessage("Initial commit").call();
				if (hasBranch(git, "master")) {
					git.branchRename().setOldName("master").setNewName(defaultBranchName).call();
				}
			}
			
			RefUpdate refUpdate = repository.updateRef("HEAD");
	        refUpdate.link("refs/heads/" + defaultBranchName);

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
		cloneCmd.setMirror(true);
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
	public static RepoFileMo getRepoFileMoFromTreeWalk(TreeWalk tw, Git git, Repository db, String branchName) throws Exception {
		String curPath = tw.getPathString();
		RepoFileMo repoFileMo = new RepoFileMo(tw);
		try (RevWalk rw = new RevWalk(db)) {
			RevCommit commit = rw.parseCommit(db.resolve(branchName));
			Iterable<RevCommit> commitLog = git.log().add(commit).addPath(curPath).call();
			for (RevCommit revCommit : commitLog) {
				repoFileMo.setCommitInfo(revCommit);
			}
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
								RepoFileMo repoFileMo = getRepoFileMoFromTreeWalk(tw, git, db, branchName);
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

					RepoFileMo repoFileMo = getRepoFileMoFromTreeWalk(tw, git, db, branchName);
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
	
	/**
	 * Fork一个仓库
	 * @param originalRepoUrl 原始仓库访问地址 例如：http://localhost:9100/u8047/test.git
	 * @param newRepoPath 新仓库保存目录地址 例如：D:/opt/repo/8047/demo/.git
	 * @param branch 分支名(非必填)
	 * @return
	 */
	public static boolean forkRepository(String originalRepoUrl, String newRepoPath, String branch) {
		File repoFile = new File(newRepoPath);
		// 检查仓库目录是否已经存在
		if (repoFile.exists() && repoFile.isDirectory()) {
			log.info("[-_-] err: repo is exist, clone repo failed, dir: " + repoFile);
			return false;
		}
		
		Git git = null;
		try {
			CloneCommand cloneCmd = Git.cloneRepository().setURI(originalRepoUrl).setDirectory(repoFile);
			cloneCmd.setMirror(true);
			if(StringTool.isNotEmpty(branch)) {
				cloneCmd.setBranch(branch);
			}
			git = cloneCmd.call();
			git.remoteAdd().setName("upstream").setUri(new URIish(originalRepoUrl)).call();
		} catch (Exception e) {
			log.error("Fork仓库失败", e);
			try {
				if(newRepoPath.endsWith("/.git") || newRepoPath.endsWith("/.git/")) {
					FileUtils.deleteDirectory(repoFile.getParentFile());
				} else {
					FileUtils.deleteDirectory(repoFile);
				}
			} catch (IOException e1) {
				log.error("删除无效文件目录失败", e1);
			}			
			return false;
		} finally {
			OsTool.close(git);
		}
		return true;
	}
	
	/**
	 * 重命名仓库
	 * @param oldRepoPath
	 * @param newRepoPath
	 * @return
	 */
	public static boolean rename(String oldRepoPath, String newRepoPath) {
        try {
			// 重命名仓库的本地目录
			File oldRepoDir = new File(oldRepoPath);
			File newRepoDir = new File(newRepoPath);
			if (!oldRepoDir.renameTo(newRepoDir)) {
				log.info("Failed to rename repository directory.");
			    return false;
			}
			log.info("Repository renamed successfully.");
		} catch (Exception e) {
			log.error("仓库重命名失败", e);
			return false;
		}
        return true;
	}
	
	/**
	 * 打包成zip包
	 * @param repoPath 本地仓库路径
	 * @param branchOrTagName 分支或标签名称
	 * @param archiveFileRootPath 打包文件存放根路径
	 * @return
	 */
	public static String packZipFile(String repoPath, String branchOrTagName, String archiveFileRootPath) {
		Git git = null;
		FileInputStream in = null;
		FileOutputStream out = null;
		RevWalk rw = null;
		//压缩包保存地址
		String archiveFileSavePath = null;
        try {

			// 打开本地仓库
			Repository repo = new FileRepositoryBuilder()
			        .setGitDir(new File(repoPath))
			        .build();

			// 获取指定分支或标签的最新版本ID
			ObjectId branchId = repo.resolve(branchOrTagName);

			rw = new RevWalk(repo);
			RevCommit commit = rw.parseCommit(repo.resolve(branchOrTagName));

			// 创建Git对象
			git = new Git(repo);

			//取出最后一次提交的信息
			Iterable<RevCommit> commits = git.log().add(commit).setMaxCount(1).call();
			RevCommit lastCommit = commits.iterator().next();

	        // 检查缓存文件中的时间是否早于仓库中最后一次提交的时间
	        Properties props = new Properties();
	        String cacheFilePath = archiveFileRootPath + "/" + branchId.getName();

	        //获取.git文件夹的上一级目录
	        String repoPathTemp = repoPath.substring(0, repoPath.lastIndexOf("/"));

			archiveFileSavePath = cacheFilePath + repoPathTemp.substring(repoPathTemp.lastIndexOf("/")) + "-" + branchOrTagName + ".zip";

	        File cacheFile = new File(cacheFilePath + "/cache");
	        if(!cacheFile.getParentFile().exists()) {
	        	cacheFile.getParentFile().mkdirs();
	        }
	        if (cacheFile.exists()) {
	            in = new FileInputStream(cacheFile);
                props.load(in);
	            String lastPackTimeStr = props.getProperty("lastPackTime");
	            if (lastPackTimeStr != null) {
	                Date lastPackTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(lastPackTimeStr);
	                // 仓库没有更新，直接返回缓存的ZIP包
	                if (lastCommit.getCommitterIdent().getWhen().compareTo(lastPackTime) <= 0) {
                        log.info("The repository has not been updated since the last packaging. Returning the cached ZIP file.");
                        return archiveFileSavePath;
	                }
	            }
	        }

	        //压缩包里面的根文件夹名称
			String rootFileName = repoPathTemp.substring(repoPathTemp.lastIndexOf("/") + 1) + "-" + branchOrTagName + "/";

			// 打包成ZIP文件
			ArchiveCommand.registerFormat("zip", new ZipFormat());
			git.archive()
			        .setFormat("zip")
			        .setTree(branchId)
			        .setPrefix(rootFileName)
			        .setOutputStream(new FileOutputStream(archiveFileSavePath))
			        .call();

	        // 更新缓存文件
	        out = new FileOutputStream(cacheFile);
            props.setProperty("lastPackTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lastCommit.getCommitterIdent().getWhen()));
            props.store(out, "Last packaging information");

		} catch (Exception e) {
			log.error("仓库创建压缩包失败", e);

			return null;
		} finally {
			OsTool.close(in);
			OsTool.close(git);
			OsTool.close(out);
			OsTool.close(rw);
		}
        return archiveFileSavePath;
	}
	
	/**
	 * 获取仓库默认分支
	 * @param repoPath
	 * @return
	 */
	public static String getDefaultBranchName(String repoPath) {
		// 获取默认分支名称
		String defaultBranch = null;
		Repository repo = null;
		try {
			// 打开本地仓库
			repo = new FileRepositoryBuilder()
			        .setGitDir(new File(repoPath))
			        .build();
			defaultBranch = repo.getFullBranch();
		} catch (Exception e) {
			log.error("获取仓库默认分支信息失败", e);
		} finally {
			OsTool.close(repo);
		}
        return defaultBranch;
	}
	
	/**
	 * 切换仓库的默认分支
	 * @param repoPath
	 * @param branchName 新的默认分支名称
	 * @return
	 */
	public static boolean switchDefaultBranch(String repoPath, String branchName) {
		Git git = null;
		try {
			git = openToGit(repoPath);
			List<Ref> branches = git.branchList().call();
		    Ref targetBranch = null;
		    for (Ref branch : branches) {
		        if (branch.getName().equals("refs/heads/" + branchName)) {
		            targetBranch = branch;
		            break;
		        }
		    }
		    // 切换到目标分支
		    if (targetBranch != null) {
		        git.checkout().setName(targetBranch.getName()).call();
		    }
		} catch (Exception e) {
			log.error("切换仓库的默认分支信息失败", e);
			return false;
		} finally {
			OsTool.close(git);
		}
		return true;
	}
	
	/**
	 * 修改仓库分支名称
	 * @param repoPath
	 * @param oldName
	 * @param newName
	 * @return 1为修改成功，0为修改失败，-1为新名称已存在，-2为原分支名称没有找到
	 */
	public static int changeBranchName(String repoPath, String oldName, String newName) {
		int result = 1;
		Git git = null;
		try {
			git = openToGit(repoPath);
			List<Ref> branches = git.branchList().call();
			//判断新名称是否已存在
			for (Ref branch : branches) {
				if (branch.getName().equals("refs/heads/" + newName)) {
					result = -1;
					return result;
				}
			}
			//判断原名称是否已存在
			boolean flag = false;
			for (Ref branch : branches) {
				if (branch.getName().equals("refs/heads/" + oldName)) {
					flag = true;
					break;
				}
			}
			if(!flag) {
				result = -2;
				return result;
			}
			git.branchRename().setOldName(oldName).setNewName(newName).call();
		} catch (Exception e) {
			result = 0;
			log.error("修改仓库分支名称失败", e);
		} finally {
			OsTool.close(git);
		}
	    
	    return result;
	}

	public static List<CommitVo> getCommitList(String repoPath, String branchName) {
		List<CommitVo> result = new ArrayList<>();
		try (Git  git = openToGit(repoPath)){
			Iterable<RevCommit> commits = git.log().add(git.getRepository().resolve(branchName)).call();
			for (RevCommit commit : commits) {
				CommitVo commitVo = new CommitVo();
				commitVo.setCommitId(commit.getId().getName());
				commitVo.setName(commit.getAuthorIdent().getName());
				commitVo.setEmail(commit.getAuthorIdent().getEmailAddress());
				commitVo.setCreateTime(LocalDateTime.ofInstant(commit.getAuthorIdent().getWhen().toInstant(), ZoneOffset.ofHours(8)));
				commitVo.setMessage(commit.getFullMessage());
				commitVo.setHash(commitVo.getCommitId());
				result.add(commitVo);
			}
		} catch (Exception e) {
			log.error("获取提交记录失败",e);
		}
		return result.stream().sorted((a, b) -> {
			return b.getCreateTime().compareTo(a.getCreateTime());
		}).collect(Collectors.toList());
	}

	public static List<String> getTagList(String repoPath) {
		List<String> result = new ArrayList<>();
		try (Git git = openToGit(repoPath)){
			Iterable<Ref> tagList = git.tagList().call();
			for (Ref tag : tagList) {
				result.add(tag.getName());
			}
		} catch (Exception e) {
			log.error("获取tag记录失败",e);
		}
		return result;
	}

	public static ByteArrayInputStream getFile(String repoPath, String branchName, String filePath) {
		try (Git git = openToGit(repoPath);
			 Repository repository = git.getRepository();
			 RevWalk revWalk = new RevWalk(repository)){
			ObjectId headId = repository.resolve(branchName);
			RevCommit commit = revWalk.parseCommit(headId);
			RevTree tree = commit.getTree();
			try (TreeWalk treeWalk = TreeWalk.forPath(repository, filePath, tree)){
				ObjectId objectId = treeWalk.getObjectId(0);
				ObjectLoader loader = repository.open(objectId);
				return new ByteArrayInputStream(loader.getBytes());
			}
		} catch (Exception e) {
			log.error("获取文件失败",e);
		}
		return null;
	}

	/**
	 * 修改仓库分支名称
	 * @param repoPath
	 * @param newName
	 * @return 1为修改成功，0为修改失败，-1为新名称已存在
	 */
	public static int changeDefaultBranchName(String repoPath, String newName) {
		int result = 1;
		Git git = null;
		try {
			git = openToGit(repoPath);
			List<Ref> branches = git.branchList().call();
			//判断新名称是否已存在
			for (Ref branch : branches) {
				if (branch.getName().equals("refs/heads/" + newName)) {
					result = -1;
					return result;
				}
			}
			String oldName = git.getRepository().getBranch();
			git.branchRename().setOldName(oldName).setNewName(newName).call();
			
	        // 更新仓库的默认分支
	        Repository repository = git.getRepository();
	        RefUpdate refUpdate = repository.updateRef("HEAD");
	        refUpdate.setNewObjectId(refUpdate.getOldObjectId());
	        refUpdate.setForceUpdate(true);
	        refUpdate.setRefLogMessage("update default branch to " + newName, true);
	        refUpdate.update();
	        
		} catch (Exception e) {
			result = 0;
			log.error("修改仓库默认分支的名称失败", e);
		} finally {
			OsTool.close(git);
		}
	    
	    return result;
	}
	
	/**
	 * 删除仓库
	 * @param repoPath
	 * @return
	 */
	public static boolean deleteRepo(String repoPath) {
		Git git = null;
        try {
			// 打开仓库
			git = openToGit(repoPath);
			// 删除仓库
			File repoDir = git.getRepository().getDirectory().getParentFile();
			if (repoDir.exists()) {
				FileUtils.deleteDirectory(repoDir);
				log.info("Repository deleted successfully.");
			} else {
				log.info("Repository does not exist.");
			}
		} catch (IOException e) {
			log.error("删除仓库失败", e);
			return false;
		} finally {
			OsTool.close(git);
		}
        return true;
	}
	
	/**
	 * 同步原始仓库代码
	 * @param repoPath
	 * @param branchName
	 * @param upstreamUrl 原始仓库地址(可选，优先级高于仓库配置文件中的)
	 */
	public static boolean syncUpstream(String repoPath, String branchName, String upstreamUrl) {
		Git git = null;
		Repository localRepo = null;
        try {
			// 打开本地Fork仓库
			localRepo = new FileRepositoryBuilder().setGitDir(new File(repoPath)).build();
			git = new Git(localRepo);
			
			//判断是否需要更新配置文件中的原始仓库地址
			if(StringTool.isNotEmpty(upstreamUrl)) {
				String curUpstreamUrl = getUpstreamUrl(git);
				if(!upstreamUrl.equals(curUpstreamUrl)) {
					git.remoteSetUrl().setRemoteName("upstream").setRemoteUri(new URIish(upstreamUrl)).call();
				}
			}
			
			// 拉取原始仓库的更新
			git.fetch().setRemote("upstream").call();
			// 获取本地主分支和远程主分支的引用
			Ref upstreamBranch = localRepo.exactRef("refs/remotes/upstream/" + branchName);

			// 合并原始仓库的更新到本地主分支
			MergeResult mergeResult = git.merge()
			        .include(upstreamBranch.getObjectId())
			        .call();

			if (mergeResult.getMergeStatus().isSuccessful()) {
				log.info("Synced successfully.");
			} else {
				log.info("Sync failed.");
				return false;
			}
		} catch (Exception e) {
			log.error("同步原始仓库代码失败",e);
			return false;
		} finally {
			OsTool.close(git);
			OsTool.close(localRepo);
		}
        return true;
	}
	
	/**
	 * 从配置文件中获取原始仓库地址
	 * @param git
	 * @return
	 */
	public static String getUpstreamUrl(Git git) {
		String url = null;
		if(git == null) {
			return url;
		}
		try {
			// 获取所有RemoteConfig对象
			List<RemoteConfig> remoteConfigs = git.remoteList().call();
			if(remoteConfigs == null || remoteConfigs.isEmpty()) {
				return url;
			}
			// 遍历RemoteConfig对象，查找upstream信息
			for (RemoteConfig remoteConfig : remoteConfigs) {
				if("upstream".equals(remoteConfig.getName())) {
					List<URIish> urIishs = remoteConfig.getURIs();
					for(URIish item : urIishs) {
						url = item.getPath();
						break;
					}
				}
			}
		} catch (Exception e) {
			log.error("从配置文件中获取原始仓库地址失败",e);
		}
		return url;
	}
	
	/**
	 * 创建分支
	 * @param repoPath
	 * @param branchName
	 * @return
	 */
	public static boolean createBranch(String repoPath, String branchName) {
		Git git = null;
		try {
			git = openToGit(repoPath);
			List<Ref> branches = git.branchList().call();
			//判断新名称是否已存在
			for (Ref branch : branches) {
				if (branch.getName().equals("refs/heads/" + branchName)) {
					return false;
				}
			}
			git.branchCreate().setName(branchName).call();
		} catch (Exception e) {
			log.error("创建分支失败",e);
			return false;
		} finally {
			OsTool.close(git);
		}
		return true;
	}
	
	/**
	 * 删除分支
	 * @param repoPath
	 * @param branchName
	 * @return
	 */
	public static boolean deleteBranch(String repoPath, String branchName) {
		Git git = null;
		boolean result = false;
		try {
			git = openToGit(repoPath);
			List<String> branchs = git.branchDelete().setBranchNames(branchName).setForce(true).call();
			for (String name : branchs) {
				if(("refs/heads/" + branchName).equals(name)) {
					result = true;
					break;
				}
			}
		} catch (Exception e) {
			log.error("删除分支失败",e);
		} finally {
			OsTool.close(git);
		}
		return result;
	}
	
	public static void main(String[] args) {
		//System.out.println(packZipFile("D:/opt/repo/8047/demo", "main", "D:/test/cache/"));
		
		//System.out.println(changeBranchName("D:/opt/repo/8047/demo", "demo1", "test"));
		
//		switchDefaultBranch("D:/opt/repo/8047/demo", "main");
//		
//		List<String> list = getBranchList("D:/opt/repo/8047/demo");
//		list.forEach(item -> {
//			System.out.println(item);
//		});
//		System.out.println(getDefaultBranchName("D:/opt/repo/8047/test/.git"));
		//syncUpstream("D:\\opt\\repo\\8047\\test1\\.git", "main", "D:/opt/repo/8048/test");
		createBranch("D:\\opt\\repo\\8048\\demo1\\.git", "test");
//		deleteBranch("D:\\opt\\repo\\8048\\demo1\\.git", "test123");
	}

}
