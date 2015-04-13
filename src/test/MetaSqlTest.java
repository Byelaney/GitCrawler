package test;

import http.HttpModule;

import java.util.List;

import search.MetaSearchGitHub;
import search.SearchGitHub;
import search.SearchModule;
import usefuldata.Developer;
import util.Dates;

import com.google.inject.Guice;
import com.google.inject.Injector;

import entity.Commit;
import entity.Contributor;
import entity.Project;
import entity.User;
import factory.MetaDaoFactory;

public class MetaSqlTest {
	private MetaSearchGitHub searchGitHub;
	private SearchGitHub searchGitHub2;
	
	public static void main(String []args){
		
//		MetaSqlTest mst = new MetaSqlTest();
//		mst.setup();
//		mst.testIssueDao();
//		
		
		Project p = MetaDaoFactory.getProjectDao().getProject("spinfo", "java");
		System.out.println( p == null);
		
//		List<Commit> ccs = MetaDaoFactory.getCommitDao().getCommits(4193864);
//		for(Commit c:ccs){
//			//c.setCommitDate(c.getCommitDate().toString());
//			
//			c.setCommitDate(Dates.metaDateFormat(c.getCommitDate().toString()));
//			MetaDaoFactory.getCommitDao().updateCommit(c, 4193864);
//		}
//		
	}
	
	public void setup() {
		Injector injector = Guice.createInjector(new SearchModule(), new HttpModule());
		searchGitHub = injector.getInstance(MetaSearchGitHub.class);
		
		searchGitHub2 = injector.getInstance(SearchGitHub.class);
	
	}
	
	
//	public void testContributorDao(){
//		try {
//			
//			/*Sanity test, if the list of contributors is null, something is wrong*/
//			Project project = new Project(new User("nasa"), "mct");
//			List<Contributor> contributors = searchGitHub.getAllProjectContributors(project);
//			
//			for(Contributor a:contributors){
//				MetaDaoFactory.getContributorDao().addContributor(a);
//				
//			}
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//			
//		}
//		
//	}
	
	public void testCommitDao(){
		try {
			
			/*Sanity test, if the list of contributors is null, something is wrong*/
		
			//System.out.println(project.getId());
			
			entity.Project p = new Project(new User("nasa"), "mct");
			List<Contributor> dp = MetaDaoFactory.getContributorDao().getAllContributors(4193864);
			
			for(int i = 4;i>=0;i--){
				//only 4 has not been crawled
				if(dp.get(i).getLogin().equals("VWoeltjen")){
					List<Commit> commits = searchGitHub.getProjectCommitsByCommiter(p, dp.get(i).getLogin());
					for(Commit cm:commits){
						MetaDaoFactory.getCommitDao().addCommit(cm, 4193864);
					}
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
	}
	
	public void testGetAllContributors(){
		try {
			List<Contributor> ctbs = MetaDaoFactory.getContributorDao().getAllContributors(4193864);
			for(Contributor c:ctbs){
				System.out.println(c.getLogin());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}
	
	public void testIssueDao(){
		try{
			List<entity.Issue> isss = MetaDaoFactory.getIssueDao().getAllMetaIssues(4193864);
			for(entity.Issue is:isss){
				MetaDaoFactory.getIssueDao().updateIssue(is,4193864);
				
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			
		}
		
	}
	
	
}
