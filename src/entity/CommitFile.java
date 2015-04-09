package entity;

public class CommitFile extends GitHubEntity{
	private String sha;
	private String filename;
	private String status;
	private int additions;
	private int deletions;
	private int changes;
	private String contents_url;
	private String commit_sha;
	
	public CommitFile(){
		super();
	}
	
	public CommitFile(String sha, String filename, String status,
			int additions, int deletions, int changes, String contents_url,
			String commit_sha) {
		super();
		this.sha = sha;
		this.filename = filename;
		this.status = status;
		this.additions = additions;
		this.deletions = deletions;
		this.changes = changes;
		this.contents_url = contents_url;
		this.commit_sha = commit_sha;
	}

	public String getSha() {
		return sha;
	}

	public void setSha(String sha) {
		this.sha = sha;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getAdditions() {
		return additions;
	}

	public void setAdditions(int additions) {
		this.additions = additions;
	}

	public int getDeletions() {
		return deletions;
	}

	public void setDeletions(int deletions) {
		this.deletions = deletions;
	}

	public int getChanges() {
		return changes;
	}

	public void setChanges(int changes) {
		this.changes = changes;
	}

	public String getContents_url() {
		return contents_url;
	}

	public void setContents_url(String contents_url) {
		this.contents_url = contents_url;
	}

	public String getCommit_sha() {
		return commit_sha;
	}

	public void setCommit_sha(String commit_sha) {
		this.commit_sha = commit_sha;
	}

	@Override
	public String getURL() {
		return commit_sha;
	}
	
	
	
}
