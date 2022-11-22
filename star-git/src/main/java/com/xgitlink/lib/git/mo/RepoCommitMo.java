package com.xgitlink.lib.git.mo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.revwalk.RevCommit;

import lombok.Data;

@Data
public class RepoCommitMo {
	private static DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private String name;
	private String email;
	private String time;
	private String log;
	private String sha1;

	public RepoCommitMo(RevCommit commit) {
		PersonIdent authoIdent = commit.getAuthorIdent();
		this.name = authoIdent.getName();
		this.email = authoIdent.getEmailAddress();
		this.time =  format.format(authoIdent.getWhen());
		this.log = commit.getShortMessage();
		this.sha1 =   commit.getId().name();
	}
}
