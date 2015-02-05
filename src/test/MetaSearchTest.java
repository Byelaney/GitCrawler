package test;

import java.util.List;

import http.HttpModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

import entity.Commit;
import entity.Project;
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
		a.testGetAllCommits();
	}
	
	public void setup() {
		Injector injector = Guice.createInjector(new SearchModule(), new HttpModule());
		MetasearchGitHub = injector.getInstance(MetaSearchGitHub.class);
		searchGitHub2 = injector.getInstance(SearchGitHub.class);
	}
	
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
			
		}else
			System.out.println("project not found");
		
		
		
		}catch(Exception e){
			e.printStackTrace();
		}
	}

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

}
	