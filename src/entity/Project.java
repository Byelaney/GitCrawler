package entity;

import java.util.Date;
import java.util.List;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Reference;

import util.Dates;

import com.google.gson.annotations.SerializedName;



/**
 * <h1>Project -  <i>Represents a software project </i> </h1> 
 * <p>
 * 	This Class support the base and maybe the most important Class, because like
 *  we can found at <a href="http://en.wikipedia.org/wiki/GitHub"><i>Wikipedia</i></a> <i>"GitHub is a web-based hosting service for software development projects that use the Git revision control system."</i>. 
 *  A Project object contains all entities that in some way are connected. 
 *  A user want to extract metrics from projects hosted on <a href="https://github.com/"><i>Github</i></a>, so this Class can give support to all data that a project can have 
 *	and consequently extract metrics from these. 
 * </p>
 *  
 * 
 */

@Entity("projects")
public class Project extends GitHubEntity {
	@SerializedName("id")
	@Indexed(unique=true, dropDups=true)
	@Id private int id;
	
	@SerializedName("name")
	private String name;

	@SerializedName("description")
	private String description;

	@SerializedName("language")
	private String language;
	private List<Language> languages;
	
	@Reference private List<Issue> issues;
	@Reference private List<Milestone> milestones;
	@Reference private List<Commit> commits;
	@Reference private List<Contributor> contributors;

	@Reference private User user;

	@SerializedName("clone_url")
	private String checkoutURL;

	@SerializedName("html_url")
	private String sourceCodeURL;

	@SerializedName("created_at")
	private Date createdAt;

	@SerializedName("pushed_at")
	private Date lastPushedAt;

	@SerializedName("fork")
	private boolean isFork;

	@SerializedName("has_downloads")
	private boolean hasDownloads;

	@SerializedName("has_issues")
	private boolean hasIssues;

	@SerializedName("has_wiki")
	private boolean hasWiki;

	@SerializedName("watchers_count")
	private int watchersCount;

	@SerializedName("forks")
	private int forks_count;

	@SerializedName("open_issues_count")
	private int issuesCount;

	public Project(){
		super();
	}
	
	/**
	 * 2-parameter complimentary constructor
	 * @param user the {@link User} to who the project belongs
	 * @param name the name of the {@link Project}
	 */
	public Project(User user, String name) {
		this.user = user;
		this.name = name;
		this.checkoutURL = "https://github.com/" + user.getLogin() + "/" + name + ".git";
		this.sourceCodeURL = "https://github.com/" + user.getLogin() + "/" + name;
	}
	
	/**
	 * 3-parameter constructor
	 * @param name the project name
	 * @param description the project description
	 * @param checkoutURL the project's source code URL
	 */
	public Project(String name, String description, String checkoutURL) {
		this.name = name;
		this.description = description;
		this.checkoutURL = checkoutURL;
	}

	
	
	/**
	 * Informs the name of the project
	 * @return the name of the project
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the name of the project
	 * @param name a {@link String} for the project's name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Informs the description of the project
	 * @return the String description of the project
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Sets the description of the project
	 * @param description a String for setting the description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Informs the project's author name
	 * @return a String correspondent to the name of the author of the project
	 */
	public User getOwner() {
		return this.user;
	}

	/**
	 * Informs the project's author name
	 * @param owner a {@link String} for the name of the project's author
	 */
	public void setOwner(User owner) {
		this.user = owner;
	}

	/**
	 * Informs the project's SCM
	 * @return
	 * @deprecated
	 */
	public SCM getSCM() {
		throw new UnsupportedOperationException("This method is not supported anymore.");
	}

	public void setSCM(SCM hg) {
		throw new UnsupportedOperationException("This method is not supported anymore.");
	}
	
	/**
	 * Informs the project's SCM URL
	 * @return
	 */
	public String getCheckoutURL() {
		return this.checkoutURL;
	}

	/**
	 * Sets the project's SCM URL
	 * @param scmURL
	 */
	public void setCheckoutURL(String scmURL) {
		this.checkoutURL = scmURL;
	}

	/**
	 * Informs the source code URL of the project
	 * @return a String correspondent to the source code URL of the project in question
	 */
//	public String getSourceCodeURL() {
//		return this.sourceCodeURL;
//	}

	/**
	 * Sets the source code URL for the project
	 * @param sourceCodeURL sets the URL String of the project's source code
	 */
//	public void setSourceCodeURL(String sourceCodeURL) {
//		this.sourceCodeURL = sourceCodeURL;
//	}

	/**
	 * Informs whether a project allow downloads or not
	 * @return true if the project allows source code download. Returns false otherwise.
	 */
	public boolean hasDownloads() {
		return this.hasDownloads;
	}

	/**
	 * Sets if a project allows downloads or not
	 * @param hasDownloads a {@link boolean} for setting whether the project enables downloads or not
	 */
	public void setHasDownloads(boolean hasDownloads) {
		this.hasDownloads = hasDownloads;
	}

	/**
	 * @return true if the project has issues. Returns false otherwise.
	 */
	public boolean hasIssues() {
		return this.hasIssues;
	}

	/**
	 * @param hasIssues a boolean value for setting if the project has Issues or not
	 */
	public void setHasIssues(boolean hasIssues) {
		this.hasIssues = hasIssues;
	}

	/**
	 * Informs whether the project has its own Wiki or not.
	 * @return true if the project has a Wiki. Returns false otherwise.
	 */
	public boolean hasWiki() {
		return this.hasWiki;
	}

	/**
	 * Sets whether a project has its own Wiki or not.
	 * @param hasWiki a {@link boolean} for setting whether the project has a Wiki or not.
	 */
	public void setHasWiki(boolean hasWiki) {
		this.hasWiki = hasWiki;
	}

	/**
	 * Informs how many people are watching the project or have "starred" it
	 * @return an {@link integer} informing how many people are currently watching the project on its forge
	 */
	public int getWatchersCount() {
		return this.watchersCount;
	}

	/**
	 * Sets how many people are watching the project
	 * @param watchersCount an integer for setting the number of people watching the project on its forge
	 */
	public void setWatchersCount(int watchersCount) {
		this.watchersCount = watchersCount;
	}

	/**
	 * Informs the number of forks the project has
	 * @return an integer correspondent to the number of forks
	 */
	public int getForksCount() {
		return this.forks_count;
	}

	/**
	 * An indicator of how many times the project has been forked.
	 * @param forksCount an integer for setting the number of forks the project has.
	 */
	public void setForksCount(int forksCount) {
		this.forks_count = forksCount;
	}

	/**
	 *
	 * Informs the number of open issues of the project
	 * @return an integer value correspondent to the amount of open issues
	 */
	public int getIssuesCount() {
		return this.issuesCount;
	}

	/**
	 * Sets the number of issues of a project
	 * @param issuesCount an integer for setting the number of Issues of the project
	 */
	public void setIssuesCount(int issuesCount) {
		this.issuesCount = issuesCount;
	}

	/**
	 * Tells whether a project is a fork of another or not
	 * @return a boolean value: true if it's a fork, false otherwise
	 */
	public boolean isFork() {
		return this.isFork;
	}

	/**
	 * Sets if the project is a fork of another or not
	 * @param value a boolean value for informing whether the project is a fork of another or not
	 */
	public void setIsFork(boolean value) {
		this.isFork = value;
	}

	/**
	 * Methods that deal with dates are below
	 * Notice that each setter method is overloaded to support Date and String parameters.
	 * When the parameter is provided as a String object, the setter method will perform the
	 * conversion to a date object
	 */

	/**
	 * Informs the creation date of the project
	 * @return a Date object correspondent to the project's creation date
	 */
	public Date getCreatedAt() {
		return this.createdAt;
	}

	/**
	 * Sets the creation date of the project
	 * @param createdAt a Date object for setting the creation date of the project
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * @param createdAtParam the String correspondent to the creation date of the project in question. e.g: 2012-04-28T15:40:35Z
	 */
	public void setCreatedAt(String createdAtParam) {
		if(createdAtParam.length() >10){
			Date createAtDate = new Dates("yyyy-MM-dd HH:mm:ss").format(createdAtParam);
			this.createdAt = createAtDate;
		}
		else{
			Date createAtDate = new Dates("yyyy-MM-dd").format(createdAtParam);
			this.createdAt = createAtDate;
		}
	}

	/**
	 * Returns the date of the latest push to the project
	 * @return a Date object of the latest push
	 */
	public Date getLastPushedAt() {
		return this.lastPushedAt;
	}

	/**
	 * Sets the date on which the last push has been submitted to the project's source code
	 * @param lastPushedAtParam the Date object correspondent to the date of the last push to the project
	 * in question
	 */
	public void setLastPushedAt(Date lastPushedAtParam) {
		this.lastPushedAt = lastPushedAtParam;
	}

	/**
	 * @param lastPushedAtParam the String correspondent to the date of the last push to the project
	 * in question. e.g: 2012-04-28T15:40:35Z
	 */
	public void setLastPushedAt(String lastPushedAtParam){
		if(lastPushedAtParam.length() >10){
			Date createAtDate = new Dates("yyyy-MM-dd HH:mm:ss").format(lastPushedAtParam);
			this.lastPushedAt = createAtDate;
		}
		else{
			Date createAtDate = new Dates("yyyy-MM-dd").format(lastPushedAtParam);
			this.lastPushedAt = createAtDate;
		}
	}

	/**
	 * Informs the name of prevailing programming language in a project
	 * @return name of programming language
	 */
	public String getLanguage() {
		return this.language;
	}

	/**
	 * Sets the name of prevailing programming language in a project
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * Returns the list of languages that compose the Project
	 * @return a {@link List} of {@link Language} objects
	 */
	public List<Language> getLanguages() {
		return this.languages;
	}

	/**
	 * Sets the list of languages that compose the Project
	 * @param a {@link List} of {@link Language} objects
	 */
	public void setLanguages(List<Language> langs) {
		this.languages = langs;
	}
	
	/**
	 * Returns the list of milestones of the Project
	 * @return a {@link List} of {@link Milestone} objects
	 */
	public List<Milestone> getMilestones() {
		return this.milestones;
	}

	public void setMilestones(List<Milestone> milestones) {
		this.milestones = milestones;
	}
	
	/**
	 * Returns the list of issues of the project
	 * @return a {@link List} of {@link Issue} objects
	 */
	public List<Issue> getIssues() {
		return this.issues;
	}
	
	public void setIssues(List<Issue> issues) {
		this.issues = issues;
	}
	
	/**
	 * Returns the list of commits of the project
	 * @return a {@link List} of {@link Commit} objects
	 */
	public List<Commit> getCommits() {
		return this.commits;
	}
	
	public void setCommits(List<Commit> commits) {
		this.commits = commits;
	}
	
	/**
	 * Returns the list of contributors of the project as GitHub users
	 * @return
	 */
	public List<Contributor> getContributors() {
		return this.contributors;
	}
	
	public void setContributors(List<Contributor> contributors) {
		this.contributors = contributors;
	}

	/**
	 * Returns the {@link User} object who is the author of the Project
	 * @return
	 */
	public User getUser() {
		return this.user;
	}

	/**
	 * Sets the {@link User} object who is the author of the Project
	 * @param user
	 */
	public void setUser(User user) {
		this.user = user;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Return true if the project is considered mature
	 * 
	 * A project is considered active if it has at least 1 {@link Commit} and more the
	 * 1 contributor
	 * 
	 * @return
	 */
	public boolean isActive() {
		if (commits == null && contributors == null) {
			return false;
		} else {
			return ((commits.size() > 1) && (contributors.size() > 2));			
		}
	}
	
	/**
	 * Returns true if the project is considered mature, and false otherwise.
	 * 
	 * A project is considered mature if it has at least three watchers, plus
	 * one fork, plus more than 100 commits in its own history, and more than
	 * five issues created in its own history.
	 * 
	 * @return
	 */
	public boolean isMature() {
		return ((watchersCount > 3) && (forks_count > 1) && (commits.size() > 100) && (issues.size() > 5));
	}
	
	/**
	 * Returns the well-formated github rest-api for this project
	 * @return 
	 */
	public String getURL() {
		return String.format("https://api.github.com/repos/%s/%s", this.getUser().getLogin(), this.getName());
	}

	/**
	 * If two projects have the same ID, then they are equals.
	 * */
	public boolean equals(Project project){
		if(this.id == project.getId()){
			return true;
		}
		else{
			return false;
		}
	}
	
	@Override
	public String toString() {
		return String.format("Project(%s, %s)", this.name, this.sourceCodeURL);
	}

	public String getSourceCodeURL() {
		return sourceCodeURL;
	}

	public void setSourceCodeURL(String sourceCodeURL) {
		this.sourceCodeURL = sourceCodeURL;
	}
	
	/**
	 * this method only used to transform to project used in gitcrawler database
	 * @param codes
	 * @return
	 */
	public usefuldata.Project ProjectTransform(int codes,String owner){
		usefuldata.Project p = new usefuldata.Project();
		p.setId(this.getId());
		p.setName(this.getName());
		p.setOwner(owner);
		p.setDescription(this.getDescription());
		p.setCodes(codes);
		
		return p;
	}
	
	
}