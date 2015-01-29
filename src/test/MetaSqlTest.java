package test;

import http.HttpModule;

import java.util.List;

import search.SearchGitHub;
import search.SearchModule;
import usefuldata.Developer;

import com.google.inject.Guice;
import com.google.inject.Injector;

import entity.Commit;
import entity.Contributor;
import entity.Project;
import entity.User;
import factory.MetaDaoFactory;

public class MetaSqlTest {
	private SearchGitHub searchGitHub;
	
	public static void main(String []args){
		
		MetaSqlTest mst = new MetaSqlTest();
		mst.setup();
		mst.testCommitDao();
		
		
	}
	
	public void setup() {
		Injector injector = Guice.createInjector(new SearchModule(), new HttpModule());
		searchGitHub = injector.getInstance(SearchGitHub.class);
	
	}
	
	
	public void testContributorDao(){
		try {
			
			/*Sanity test, if the list of contributors is null, something is wrong*/
			Project project = new Project(new User("nasa"), "mct");
			List<Contributor> contributors = searchGitHub.getAllProjectContributors(project);
			
			for(Contributor a:contributors){
				MetaDaoFactory.getContributorDao().addContributor(a);
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
	}
	
	public void testCommitDao(){
		try {
			
			/*Sanity test, if the list of contributors is null, something is wrong*/
		
			//System.out.println(project.getId());
			
		
			entity.Project p = new Project(new User("nasa"), "mct");
			List<Developer> dp = searchGitHub.getDevelopers(p);
			
			for(int i = dp.size()-1;i>=0;i--){
				List<Commit> commits = searchGitHub.getProjectCommitsByCommiter(p, dp.get(i).getLogin());
				for(Commit cm:commits){
					MetaDaoFactory.getCommitDao().addCommit(cm, 4193864);
					
				}
				
			}
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
	}
	
	
	
}
