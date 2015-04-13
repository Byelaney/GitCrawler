package crawler;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import scmclient.GitClient;
import util.FileHelper;
import entity.Comment;
import entity.Commit;
import entity.CommitFile;
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
import factory.MetaDaoFactory;

/**
 * crawl all data to server
 * @author guanjun
 *
 */
public class CrawlModule extends DataSource{
	private CrawlController crawlController;
	private CrawlGitHub crawlGitHub;
	private File destinationFile;
	
	private String latest_path;
	private String unpublished_path;//filepath for unpublished_releases
	private ArrayList<String> unpublished_file = new ArrayList<String>();//filename for unpublished_releases
	private ArrayList<String> release_location = new ArrayList<String>();
	
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
	
	private int commit_page;
	
	private Crawlindex IncrawlIndex;
	private Crawlindex OutcrawlIndex;
	
	private String projectName;
	private String owner;
	
	public CrawlModule(String projectName,String projectOwner,String destinationFolder){
		this.crawlController = new CrawlController(projectName,projectOwner);
		destinationFile = new File(destinationFolder);
		crawlGitHub= new CrawlGitHub(new GitClient(), destinationFile);
				
		this.projectName = projectName;
		this.owner = projectOwner;
		Project p = MetaDaoFactory.getProjectDao().getProject(projectOwner, projectName);
				
		if(p == null)
			this.IncrawlIndex = null;
		else{
			this.IncrawlIndex = MetaDaoFactory.getCrawlindexDao().getCrawlindex(p.getId());
		}
			
	}	
	
	/**
	 * crawl all info and download releases to server
	 */
	public void getData(){
		int release_idx = 0;
		
		if(this.IncrawlIndex == null){
			
			CrawlProjectInfo();
			CrawlCommitsToDB();
				
			downLoadOneProject();		
			downLoadUnpublish_releases(this.getUnpublish_releases());						
			release_idx = this.getUnpublish_releases().size();
		}
		
		else{	
			System.out.println("trying to update this project info...");
			
			CrawlProjectInfo();
			CrawlCommitsToDB(this.IncrawlIndex.getCommit_page());
			
			downLoadOneProject();
			
			List<UnPublishedRelease> uprs = this.getUnpublish_releases();
			List<UnPublishedRelease> upr = new ArrayList<UnPublishedRelease>();
			int i = this.IncrawlIndex.getRelease_idx()-1;
			if(i<0)
				i = 0;
			
			for(;i<uprs.size();i++){
				upr.add(uprs.get(i));			
			}
			
			release_idx = uprs.size();
			if(!upr.isEmpty())
				downLoadUnpublish_releases(upr);
		}
		
		/* 
		   milestone pullrequest_page not yet
		   update crawlIndex
		*/
		
		this.OutcrawlIndex = crawlController.getMetasearchGitHub().getCrawlIndex();
		this.OutcrawlIndex.setCommit_page(this.commit_page);
		this.OutcrawlIndex.setUser_page(this.OutcrawlIndex.getContributor_page());
		this.OutcrawlIndex.setProject_id(crawlController.getProject_id());
		this.OutcrawlIndex.setRelease_idx(release_idx);
				
	}
	
	
	public void CrawlProjectInfo(){
		if(this.IncrawlIndex == null){
			project = crawlController.getProject();
			
			//sometimes the language is UpperCase
			project.setLanguage(project.getLanguage().toLowerCase());
			contributors = crawlController.getContributors();
			issues = crawlController.getIssues();
			comments = crawlController.getComments();
			releases = crawlController.getPublishedRelease();
			unpublish_releases = crawlController.getUnPublishedRelease();		
			users = crawlController.getAllUsers(this.getLogins(contributors));
		}else{
			project = crawlController.getProject();
						
			project.setLanguage(project.getLanguage().toLowerCase());
			contributors = crawlController.getContributors(IncrawlIndex.getContributor_page());
			issues = crawlController.getIssues(IncrawlIndex.getIssue_page());
			comments = crawlController.getComments(IncrawlIndex.getComment_page());
			releases = crawlController.getPublishedRelease(IncrawlIndex.getRelease_page());
			unpublish_releases = crawlController.getUnPublishedRelease(IncrawlIndex.getUpbrelease_page());		
			users = crawlController.getAllUsers(this.getLogins(contributors));	
		}		
	}
	
	public void CrawlCommitsToDB(){
		int num = 0;
		for(Contributor c:contributors){
			List<Commit> commits = crawlController.getCommits(c.getLogin());	
			num += commits.size();
			for(Commit commit:commits){
				MetaDaoFactory.getCommitDao().addCommit(commit, project.getId());
				List<CommitFile> cmf = crawlController.getCommitFile(commit.getSha());
				if(cmf != null)
					MetaDaoFactory.getCommitFileDao().addCommitFiles(cmf);
			}		
		}	
		if(num < 80){
			this.commit_page = 1;
		}else if(num == 80){
			this.commit_page = 2;
		}else{
			this.commit_page = num/80 + 1;
		}
		
		
	}
	
	public void CrawlCommitsToDB(int commit_page){
		List<Commit> commits = crawlController.getCommits(commit_page);			
		int num = commits.size();
		for(Commit commit:commits){
			int flag = MetaDaoFactory.getCommitDao().CheckaddCommit(commit, project.getId(),commit.getCommiter().getId());
			if(flag == 1){
				List<CommitFile> cmf = crawlController.getCommitFile(commit.getSha());
				if(cmf != null)
					MetaDaoFactory.getCommitFileDao().addCommitFiles(cmf);
			}
		}
		
		if(num < 80){
			this.commit_page = 1;
		}else if(num == 80){
			this.commit_page = 2;
		}else{
			this.commit_page = num/80 + 1;
		}
	}
	
	public void downLoadOneProject(){
		String projectName = crawlController.getProjectName();
		String checkoutURL = "https://github.com/" + crawlController.getProjectOwner() + "/" + projectName;
		Project p = new Project(projectName, "description", checkoutURL);
		if(destinationFile.exists())
			FileHelper.deleteDirectory(destinationFile.getAbsolutePath());	
		try{
			latest_path = crawlGitHub.downloadProject(p).getAbsolutePath();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public void downLoadUnpublish_releases(List<UnPublishedRelease> unpublish_releases){
		String projectName = crawlController.getProjectName();
		String owner = crawlController.getProjectOwner();
		unpublished_path = "Downloads/"+owner+"_"+projectName +"/" ;
		
		for(UnPublishedRelease upr:unpublish_releases){
			String httpurl = upr.getZipball_url();
			crawlGitHub.httpDownload(httpurl, unpublished_path, upr.getName() + ".zip");
			unpublished_file.add(upr.getName() + ".zip");
			
			release_location.add(unpublished_path + upr.getName() + ".zip");
		}
		
	}
	
	public List<String> getLogins(List<Contributor> contributors){
		List<String> logins = new ArrayList<String>();
		for(Contributor ctb:contributors){
			String login = ctb.getLogin();
			logins.add(login);
		}
		return logins;
	}
		
	public String getLatest_path(){
		return this.latest_path;
	}

	public CrawlController getCrawlController() {
		return crawlController;
	}

	public void setCrawlController(CrawlController crawlController) {
		this.crawlController = crawlController;
	}

	public CrawlGitHub getCrawlGitHub() {
		return crawlGitHub;
	}

	public void setCrawlGitHub(CrawlGitHub crawlGitHub) {
		this.crawlGitHub = crawlGitHub;
	}

	public File getDestinationFile() {
		return destinationFile;
	}

	public void setDestinationFile(File destinationFile) {
		this.destinationFile = destinationFile;
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

	public void setLatest_path(String latest_path) {
		this.latest_path = latest_path;
	}

	public String getUnpublished_path() {
		return unpublished_path;
	}

	public void setUnpublished_path(String unpublished_path) {
		this.unpublished_path = unpublished_path;
	}

	public ArrayList<String> getUnpublished_file() {
		return unpublished_file;
	}

	public void setUnpublished_file(ArrayList<String> unpublished_file) {
		this.unpublished_file = unpublished_file;
	}

	public ArrayList<String> getRelease_location() {
		return release_location;
	}

	public void setRelease_location(ArrayList<String> release_location) {
		this.release_location = release_location;
	}

	public ArrayList<String> developers(){
		ArrayList<String> developers = new ArrayList<String>();
		for(Contributor b:this.contributors)
			developers.add(b.getLogin());
		
		return developers;
	}
	
	public Map<String, String> date_map(){
		Map<String, String> date_map = new HashMap<String, String>();
		for(UnPublishedRelease upr:this.unpublish_releases)
			date_map.put(upr.getName(), upr.getDate());
		
		return date_map;
	}
	
	public ArrayList<String> ReleaseName(){
		ArrayList<String> releaseName = new ArrayList<String> ();
		for(int i = 0;i<this.unpublish_releases.size();i++)
			releaseName.add(this.unpublish_releases.get(i).getName());
		
		return releaseName;
	}
	
	public ArrayList<usefuldata.Developer> Developers(){
		ArrayList<usefuldata.Developer> developers = new ArrayList<usefuldata.Developer>();
		for(int i = 0;i<this.users.size();i++){
			developers.add(this.users.get(i).DeveloperTransform());
		}
		return developers;
	}
	
	public List<String> allReleaseDates(){
		List<String> dates = new ArrayList<String>();
		for(int i = 0;i<this.unpublish_releases.size();i++)
			dates.add(this.unpublish_releases.get(i).getDate());
		
		return dates;
	}

	public int getCommit_page() {
		return commit_page;
	}

	public void setCommit_page(int commit_page) {
		this.commit_page = commit_page;
	}

	public Crawlindex getIncrawlIndex() {
		return IncrawlIndex;
	}

	public void setIncrawlIndex(Crawlindex incrawlIndex) {
		IncrawlIndex = incrawlIndex;
	}

	public Crawlindex getOutcrawlIndex() {
		return OutcrawlIndex;
	}

	public void setOutcrawlIndex(Crawlindex outcrawlIndex) {
		OutcrawlIndex = outcrawlIndex;
	}

	public Crawlindex getCrawlIndex() {
		return OutcrawlIndex;
	}

	@Override
	public Crawlindex getOutIndex() {
		return OutcrawlIndex;
	}

	@Override
	public Crawlindex getInIndex() {
		return IncrawlIndex;
	}
	
	public String getLatest_location() {
		return latest_path;
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

	
	
}
