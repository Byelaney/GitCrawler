package crawler;

import java.util.ArrayList;
import java.util.List;

import entity.Comment;
import entity.Contributor;
import entity.Crawlindex;
import entity.Issue;
import entity.IssueLabel;
import entity.Language;
import entity.License;
import entity.Milestone;
import entity.Project;
import entity.PullRequest;
import entity.Release;
import entity.UnPublishedRelease;
import entity.User;

public abstract class DataSource {
	private String projectName;
	private String owner;
	private String destinationFolder;
	
	private ArrayList<String> release_location;
	private String latest_location;
	private List<Comment> comments;
	private List<Contributor> contributors;
	private List<Issue> issues;
	private IssueLabel issuelabel;
	private Language language;
	private License license;
	private Milestone milestone;
	private Project project;
	private PullRequest pullrequest;
	private List<Release> releases;
	private List<UnPublishedRelease> unpublish_releases;
	private List<User> users;
		
	public DataSource(){
		super();
	}
	
	public DataSource(String projectName, String owner, String destinationFolder) {
		super();
		this.projectName = projectName;
		this.owner = owner;
		this.destinationFolder = destinationFolder;
	}

	/**
	 * get metadata to attributes
	 */
	public abstract void getData();

	public ArrayList<String> getRelease_location() {
		return release_location;
	}

	public void setRelease_location(ArrayList<String> release_location) {
		this.release_location = release_location;
	}

	public String getLatest_location() {
		return latest_location;
	}

	public void setLatest_location(String latest_location) {
		this.latest_location = latest_location;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public List<Contributor> getContributors() {
		return contributors;
	}

	public void setContributors(List<Contributor> contributors) {
		this.contributors = contributors;
	}

	public List<Issue> getIssues() {
		return issues;
	}

	public void setIssues(List<Issue> issues) {
		this.issues = issues;
	}

	public IssueLabel getIssuelabel() {
		return issuelabel;
	}

	public void setIssuelabel(IssueLabel issuelabel) {
		this.issuelabel = issuelabel;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public License getLicense() {
		return license;
	}

	public void setLicense(License license) {
		this.license = license;
	}

	public Milestone getMilestone() {
		return milestone;
	}

	public void setMilestone(Milestone milestone) {
		this.milestone = milestone;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public PullRequest getPullrequest() {
		return pullrequest;
	}

	public void setPullrequest(PullRequest pullrequest) {
		this.pullrequest = pullrequest;
	}

	public List<Release> getReleases() {
		return releases;
	}

	public void setReleases(List<Release> releases) {
		this.releases = releases;
	}

	public List<UnPublishedRelease> getUnpublish_releases() {
		return unpublish_releases;
	}

	public void setUnpublish_releases(List<UnPublishedRelease> unpublish_releases) {
		this.unpublish_releases = unpublish_releases;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getDestinationFolder() {
		return destinationFolder;
	}

	public void setDestinationFolder(String destinationFolder) {
		this.destinationFolder = destinationFolder;
	}

	public abstract Crawlindex getOutIndex();

	public abstract Crawlindex getInIndex();
	
}
