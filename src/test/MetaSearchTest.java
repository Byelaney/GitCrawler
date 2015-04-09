package test;

import java.util.List;

import http.HttpModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

import entity.Commit;
import entity.CommitFile;
import entity.Contributor;
import entity.Issue;
import entity.Project;
import entity.UnPublishedRelease;
import entity.User;
import factory.MetaDaoFactory;
import search.MetaSearchGitHub;
import search.SearchGitHub;
import search.SearchModule;
import usefuldata.Developer;

public class MetaSearchTest {
	private MetaSearchGitHub MetasearchGitHub;
	private SearchGitHub searchGitHub2;

	public static void main(String []args){
		MetaSearchTest a = new MetaSearchTest();
		a.setup();
		a.testgetAllProjectIssues();
	}
	
	public void setup() {
		Injector injector = Guice.createInjector(new SearchModule(), new HttpModule());
		MetasearchGitHub = injector.getInstance(MetaSearchGitHub.class);
		searchGitHub2 = injector.getInstance(SearchGitHub.class);
	}
	
	/**
	 * already test over
	 */
	public void testgetProject(){
		try{
		Project p = MetasearchGitHub.getProject("mct", "nasa");
		if(p!=null){
			System.out.println(p.getId());
			System.out.println(p.getName());
			System.out.println(p.getDescription());
			System.out.println(p.getLanguage());
			System.out.println(p.getCheckoutURL());
			System.out.println(p.getSourceCodeURL());
			System.out.println(p.getCreatedAt());
			System.out.println(p.getLastPushedAt());
			System.out.println(p.isFork());
			System.out.println(p.hasDownloads());
			System.out.println(p.hasIssues());
			System.out.println(p.hasWiki());
			System.out.println(p.getWatchersCount());
			System.out.println(p.getForksCount());
			System.out.println(p.getIssuesCount());
			
			MetaDaoFactory.getProjectDao().addProject(p);
			
			//System.out.println(p.getUser().getName());
			
		}else
			System.out.println("project not found");
		
		
		
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * already test over
	 */
	public void testGetAllCommits(){
		try {
			entity.Project p = new Project(new User("nasa"), "mct");
			List<Developer> dp = searchGitHub2.getDevelopers(p);
			
			for(int i = dp.size()-1;i>=0;i--){
				List<Commit> commits = MetasearchGitHub.getProjectCommitsByCommiter(p, dp.get(i).getLogin());
				for(Commit cm:commits){
					MetaDaoFactory.getCommitDao().addCommit(cm, 4193864);
					
				}	
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}

	/**
	 * already test over
	 */
	public void testGetUnPublishedRelease(){
		List<UnPublishedRelease> upbr = MetasearchGitHub.getAllUnPublishedRelease("nasa", "mct");
		if(upbr!=null){
			for(UnPublishedRelease r:upbr){
				MetaDaoFactory.getUnPublishedReleaseDao().addUnPublishedRelease(r, 4193864);
			}
		}
	}
	
	/**
	 * already test over 
	 * but has some bugs
	 * time 60000 with no response
	 */
	public void testGetCommitFiles(){
		
		int projectId = MetaDaoFactory.getProjectDao().getProject("nasa", "mct").getId();
		List<Commit> commits = MetaDaoFactory.getCommitDao().getCommits(projectId);
		
		
		for(int i = 537;i < commits.size();i++){
//			if(commits.get(i).getSha().equals("ab9761a663d890d3051eeda306c7dcc4b386ed1d")){
//				System.out.println(i);
//			}
			
			List<CommitFile> cmfs = MetasearchGitHub.getCommitFiles("nasa", "mct", commits.get(i).getSha());
			if(cmfs !=null){
				for(CommitFile commitfile:cmfs){
					MetaDaoFactory.getCommitFileDao().addCommitFile(commitfile);
				}
			}
				
		}
		
	}
	
	
	public void testGetUsers(){
		List<Contributor> ctbs = MetaDaoFactory.getContributorDao().getAllContributors(4193864);
		for(Contributor c:ctbs){
			entity.User user = MetasearchGitHub.getUser(c.getLogin());
			MetaDaoFactory.getUserDao().addUser(user);
		}
		
	}
	
	public void testgetAllProjectIssues(){
		entity.Project p = new Project(new User("codahale"), "bcrypt-ruby");
		List<Issue> issues = MetasearchGitHub.getAllProjectIssues(p);
		
		for(Issue i:issues){
			System.out.println(i.getTitle());
		}
		
	}
	
	public void testGetProjectComment(){
		List<entity.Comment> comments = MetasearchGitHub.getComments("mct", "nasa");
		for(entity.Comment c :comments){
			MetaDaoFactory.getCommentDao().addComment(c, 4193864);
//			System.out.println(c.getId());
//			System.out.println(c.getUrl());
//			System.out.println(c.getUser());
//			System.out.println(c.getUser_id());
//			System.out.println(c.getPosition());
//			System.out.println(c.getLine());
//			System.out.println(c.getPath());
//			System.out.println(c.getCommit_id());
//			System.out.println(c.getCreated_at());
//			System.out.println(c.getUpdated_at());
//			System.out.println(c.getBody());
//			
//			System.out.println("---------------------");
			
		}
		
	}
	
}
	