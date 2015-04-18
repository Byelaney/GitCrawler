package entity;

public class Crawlindex {
	private int project_id;
	
	private int commit_page;
	
	private int comment_page;
	
	private int issue_page;
	
	private int release_page;
	
	private int upbrelease_page;
	
	private int contributor_page;
	
	private int milestone_page;
	
	private int pullrequest_page;
	
	private int user_page;
	
	private int release_idx;
	
	private int closed_issue_page;
	
	public Crawlindex(){
		super();
	}
		

	public Crawlindex(int project_id, int commit_page, int comment_page,
			int issue_page, int release_page, int upbrelease_page,
			int contributor_page, int milestone_page, int pullrequest_page,
			int user_page, int release_idx,int closed_issue_page) {
		super();
		this.project_id = project_id;
		this.commit_page = commit_page;
		this.comment_page = comment_page;
		this.issue_page = issue_page;
		this.release_page = release_page;
		this.upbrelease_page = upbrelease_page;
		this.contributor_page = contributor_page;
		this.milestone_page = milestone_page;
		this.pullrequest_page = pullrequest_page;
		this.user_page = user_page;
		this.release_idx = release_idx;
		this.closed_issue_page = closed_issue_page;
	}


	public int getRelease_idx() {
		return release_idx;
	}


	public void setRelease_idx(int release_idx) {
		this.release_idx = release_idx;
	}




	public int getContributor_page() {
		return contributor_page;
	}




	public void setContributor_page(int contributor_page) {
		this.contributor_page = contributor_page;
	}




	public int getMilestone_page() {
		return milestone_page;
	}




	public void setMilestone_page(int milestone_page) {
		this.milestone_page = milestone_page;
	}




	public int getPullrequest_page() {
		return pullrequest_page;
	}




	public void setPullrequest_page(int pullrequest_page) {
		this.pullrequest_page = pullrequest_page;
	}




	public int getUser_page() {
		return user_page;
	}




	public void setUser_page(int user_page) {
		this.user_page = user_page;
	}




	public int getProject_id() {
		return project_id;
	}
	public void setProject_id(int project_id) {
		this.project_id = project_id;
	}
	public int getCommit_page() {
		return commit_page;
	}
	public void setCommit_page(int commit_page) {
		this.commit_page = commit_page;
	}
	public int getComment_page() {
		return comment_page;
	}
	public void setComment_page(int comment_page) {
		this.comment_page = comment_page;
	}
	public int getIssue_page() {
		return issue_page;
	}
	public void setIssue_page(int issue_page) {
		this.issue_page = issue_page;
	}
	public int getRelease_page() {
		return release_page;
	}
	public void setRelease_page(int release_page) {
		this.release_page = release_page;
	}
	public int getUpbrelease_page() {
		return upbrelease_page;
	}
	public void setUpbrelease_page(int upbrelease_page) {
		this.upbrelease_page = upbrelease_page;
	}


	public int getClosed_issue_page() {
		return closed_issue_page;
	}


	public void setClosed_issue_page(int closed_issue_page) {
		this.closed_issue_page = closed_issue_page;
	}
	
	
}
