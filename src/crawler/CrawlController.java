package crawler;

import java.util.ArrayList;
import java.util.List;

import entity.Contributor;
import entity.Crawlindex;
import entity.Project;
import entity.User;
import http.HttpModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

import search.MetaSearchGitHub;
import search.SearchModule;

public class CrawlController {
	private MetaSearchGitHub MetasearchGitHub;
	private String projectName;
	private String projectOwner;
	private int project_id;
	
	private Crawlindex crawlIndex;
	
	public CrawlController(String projectName,String projectOwner){
		Injector injector = Guice.createInjector(new SearchModule(), new HttpModule());
		MetasearchGitHub = injector.getInstance(MetaSearchGitHub.class);
		this.projectName = projectName;
		this.projectOwner = projectOwner;
	}
	
	public entity.Project getProject(){
		try{
			Project p = MetasearchGitHub.getProject(projectName, projectOwner);
			if(p!=null){
				this.project_id = p.getId();
				return p;
			}else
				return null;
			}catch(Exception e){
				e.printStackTrace();
			}
		return null;
	}
	
	public List<Contributor> getContributors(){
		try{
			entity.Project p = new Project(new User(projectOwner), projectName);
			List<Contributor> cts = MetasearchGitHub.getAllProjectContributors(p);
			if(cts!=null){
				return cts;
			}else
				return null;
			}catch(Exception e){
				e.printStackTrace();
			}
		return null;
	}
	
	public List<Contributor> getContributors(int contributor_page){
		try{
			if(contributor_page == 0)
				contributor_page = 1;
			entity.Project p = new Project(new User(projectOwner), projectName);
			List<Contributor> cts = MetasearchGitHub.getAllProjectContributors(p,contributor_page);
			if(cts!=null){
				return cts;
			}else
				return null;
			}catch(Exception e){
				e.printStackTrace();
			}
		return null;		
	}
		
	public List<entity.Commit> getCommits(String developer){
		try {
			entity.Project p = new Project(new User(projectOwner), projectName);
			List<entity.Commit> commits = MetasearchGitHub.getProjectCommitsByCommiter(p,developer);
			if(commits!=null)
				return commits;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<entity.Commit> getCommits(int commit_page){
		try {
			if(commit_page == 0)
				commit_page = 1;
			entity.Project p = new Project(new User(projectOwner), projectName);
			List<entity.Commit> commits = MetasearchGitHub.getProjectCommits(p, commit_page);
			if(commits!=null)
				return commits;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
		
	public List<entity.Issue> getIssues(){
		try {
			entity.Project p = new Project(new User(projectOwner), projectName);
			List<entity.Issue> issues = MetasearchGitHub.getAllProjectIssues(p);
			if(issues!=null)
				return issues;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	public List<entity.Issue> getIssues(int issue_page){
		try {
			if(issue_page == 0)
				issue_page = 1;
			entity.Project p = new Project(new User(projectOwner), projectName);
			List<entity.Issue> issues = MetasearchGitHub.getAllProjectIssues(p,issue_page);
			if(issues!=null)
				return issues;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	public List<entity.UnPublishedRelease> getUnPublishedRelease(){
		try {
			List<entity.UnPublishedRelease> upr = MetasearchGitHub.getAllUnPublishedRelease(projectOwner, projectName);
			if(upr!=null)
				return upr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	public List<entity.UnPublishedRelease> getUnPublishedRelease(int upr_page){
		try {
			if(upr_page == 0)
				upr_page = 1;
			List<entity.UnPublishedRelease> upr = MetasearchGitHub.getAllUnPublishedRelease(projectOwner, projectName,upr_page);
			if(upr!=null)
				return upr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	public List<entity.CommitFile> getCommitFile(String sha){
		try {
			List<entity.CommitFile> cmfs = MetasearchGitHub.getCommitFiles(projectOwner, projectName, sha);
			if(cmfs!=null)
				return cmfs;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<entity.Comment> getComments(){
		try{
			List<entity.Comment> comments = MetasearchGitHub.getComments(projectName, projectOwner);
			if(comments!=null)
				return comments;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<entity.Comment> getComments(int comment_page){
		try{
			if(comment_page == 0)
				comment_page = 1;
			List<entity.Comment> comments = MetasearchGitHub.getComments(projectName, projectOwner,comment_page);
			if(comments!=null)
				return comments;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public entity.User getUser(String login){
		try{
			entity.User user = MetasearchGitHub.getUser(login);
			if(user!=null)
				return user;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<entity.User> getAllUsers(List<String> logins){
		List<entity.User> users = new ArrayList<entity.User>();
		for(String login:logins){
			User u = MetasearchGitHub.getUser(login);
			users.add(u);
		}
		
		return users;
	}
	
	public List<entity.Release> getPublishedRelease(){
		try{
			entity.Project p = new Project(new User(projectOwner), projectName);
			List<entity.Release> releases = MetasearchGitHub.getAllProjectReleases(p);
			if(releases!=null)
				return releases;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}

	public List<entity.Release> getPublishedRelease(int pbr_page){
		try{
			if(pbr_page == 0)
				pbr_page = 1;
			entity.Project p = new Project(new User(projectOwner), projectName);
			List<entity.Release> releases = MetasearchGitHub.getAllProjectReleases(p,pbr_page);
			if(releases!=null)
				return releases;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	
	public MetaSearchGitHub getMetasearchGitHub() {
		return MetasearchGitHub;
	}

	public void setMetasearchGitHub(MetaSearchGitHub metasearchGitHub) {
		MetasearchGitHub = metasearchGitHub;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectOwner() {
		return projectOwner;
	}

	public void setProjectOwner(String projectOwner) {
		this.projectOwner = projectOwner;
	}

	public Crawlindex getCrawlIndex() {
		return crawlIndex;
	}

	public void setCrawlIndex(Crawlindex crawlIndex) {
		this.crawlIndex = crawlIndex;
	}

	public int getProject_id() {
		return project_id;
	}

	public void setProject_id(int project_id) {
		this.project_id = project_id;
	}
	
	
}
