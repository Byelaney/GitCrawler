package test;

import http.HttpModule;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import scmclient.GitClient;
import search.SearchGitHub;
import search.SearchModule;


import usefuldata.Developer;
import usefuldata.ProjectContribution;
import usefuldata.Vitality;
import analysis.DataHelperImpl;
import analysis.DevelopDigramImpl;
import analysis.Relation;
import analysis.RelationImpl;
import analysis.VitalityCount;

import com.google.inject.Guice;
import com.google.inject.Injector;

import crawler.CrawlGitHub;
import entity.Commit;
import entity.Contributor;
import entity.Language;
import entity.Project;
import entity.Release;
import entity.User;
import factory.DaoFactory;
import factory.DataFactoryImpl;

public class SearchGitHubTest {
	
	private SearchGitHub searchGitHub;
	private Project fakeProject;
	private SearchGitHub searchGitHub2;
	
	public static void main(String []args){
		SearchGitHubTest a = new SearchGitHubTest();
		a.setup();
		
		//a.testGetProjects();
		
		//a.testGetDeveloper();
		
		//a.testGetAllProjectContributors();
		
		
		//a.testGetAllProjectReleases();
		
		//a.testSearchByProjectName();
		
		//a.testGetAllProjectReleases();
		
		//a.testGetAllProjectCommits();

		
		//a.testSearchByProjectName();
		
		
		//a.testGetVitality();
		
		//a.testDownladAllRelease();
		
		//a.testGetContributions();
		
		//a.testGetVitality();
		
		//a.testGetFile();
		
		a.testGetMetaProject();
	}
	
	public void setup() {
		Injector injector = Guice.createInjector(new SearchModule(), new HttpModule());
		searchGitHub = injector.getInstance(SearchGitHub.class);
		
		Injector injector2 = Guice.createInjector(new SearchModule(), new HttpModule());
		searchGitHub2 = injector2.getInstance(SearchGitHub.class);
		//User user = new User("elixir-lang");
		//fakeProject = new Project("elixir", "", "git@github.com:elixir-lang/elixir.git");
		//fakeProject.setUser(user);
	}
	
	public void test1(){
		try{
			
			Project project = new Project(new User("nasa"), "mct");
			List<Contributor> contributors = searchGitHub.getAllProjectContributors(project);
			for(Contributor cb:contributors){
				if(!cb.getLogin().equals("VWoeltjen")){
				ProjectContribution pcp = searchGitHub.getAllProjectContribution("nasa", "mct", cb.getLogin(), 4193864);
				DaoFactory.getProjectContribution().addProjectContribution(pcp);
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public void testGetProjects(){
		try {			
			/*
			List<Project> projects = searchGitHub.getProjects("mct", "nasa", SearchGitHub.INFINITY);
			for(Project p:projects){
					if(p.getName().equals("mct") && p.getOwner().getLogin().equals("nasa")){
						System.out.println(p.getId());
						break;
					}
					
			}
			*/
			
			usefuldata.Project p = searchGitHub.getProject("mct", "nasa");
			if(p!=null){
				System.out.println(p.getId());
				System.out.println(p.getName());
				System.out.println(p.getOwner());
				System.out.println(p.getDescription());
				
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
		
	}
	
	
	/**
	 * search all developers and add into mysql
	 */
	public void testGetDeveloper(){
		try {			
			
			Project project = new Project(new User("nasa"), "mct");
			List<Developer> dp = searchGitHub.getDevelopers(project);
			for(Developer d:dp)
			DaoFactory.getDeveloperDao().addDeveloper(d);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}
	
	
	public void testSearchByProjectName() {
		try {			
			List<Project> projects = searchGitHub.getProjects("mct", 1, SearchGitHub.INFINITY);
			searchGitHub.getProjectLanguages(projects.get(0));
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}
	
	
	public void testFetchByProjectLanguages() {
		try {
			List<Language> langs = searchGitHub.getProjectLanguages(fakeProject);
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}
	
	
	public void testGetAllProjects() {
		try {
			List<Project> projects = searchGitHub.getAllProjects(0, 5);
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}
	
	
	public void testGetProjectsByLanguage() {
		try {
			List<Project> projects = searchGitHub.getAllProjectsByLanguage("java");
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}
	
	
	public void testGetAllProjectCommits() {
		try {

			Project project = new Project(new User("byelaney"),"codeforce");
			
			List<Commit> commits = searchGitHub.getAllProjectCommits(project);
			
			if(commits!=null){
			System.out.println(commits.size());
			for(Commit cc:commits){
				System.out.println(cc.getCommiter().getLogin());
				System.out.println(cc.getAdditionsCount());
				System.out.println(cc.getDeletionsCount());
				System.out.println(cc.getSha());
				System.out.println("---------------------");
			}
			
			
			}
			else
				System.out.println("project not found");
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}
	
	
	public void testGetAllProjectContributors() {
		try {
			
			/*Sanity test, if the list of contributors is null, something is wrong*/
			Project project = new Project(new User("nasa"), "mct");
			List<Contributor> contributors = searchGitHub.getAllProjectContributors(project);
			
			for(Contributor a:contributors){
				System.out.println(a.getId() + "  contributions:" + a.getContributions());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}
	
	
	public void testGetAllProjectReleases() {
		try {
			User u = new User("nasa");
			Project project = new Project(u, "mct");
			
			List<Release> releases = searchGitHub.getAllProjectReleases(project);
			
			
			for(Release a:releases){
				System.out.println(a.getName());
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}
	
	
	public void testGetVitality(){
		try{
			//List<Developer> devlops = DaoFactory.getProjectDao().getAllDevelopers(4193864);
			
			//Map<String,String> ss = searchGitHub.getReleaseDate("nasa","mct");
			
			
			
			/*
			for(Developer dev:devlops){
				Map<String,Integer> sas = searchGitHub.getVitality(ss,project,dev.getLogin());
				Set<String> asd = sas.keySet();
				
				for(String qwe:asd){
					if(sas.get(qwe) > 0){
						Vitality vitality = new Vitality();
						vitality.setVitality(sas.get(qwe));
						usefuldata.Release ress = DaoFactory.getReleaseDao().getRelease(4193864, qwe);
						DaoFactory.getVitalityDao().addVitality(vitality, dev.getId(), 4193864, ress.getId());
					}
					
				}
				
			}
			*/
			List<Developer> devlops = DaoFactory.getProjectDao().getAllDevelopers(4193864);
			
			Project project = new Project(new User("nasa"), "mct");
			
			for(Developer dev:devlops){
				List<Vitality> sas = searchGitHub.getVitality(project,dev.getLogin());
				VitalityCount vc = new VitalityCount();				
				List<Vitality> results = vc.handleVitalityRelease(sas, project.getName(), dev.getLogin());
				
				for(Vitality vvvv:results){
					DaoFactory.getVitalityDao().addVitality(vvvv);				
				}
				
				
			}
			
			
			
			
		}catch (Exception e) {
			e.printStackTrace();
			
		}
		
	}
	
	public void testDownladAllRelease(){
		try{
			Map<String,String> maps = searchGitHub.getReleaseUrls("nasa", "mct");
			
			GitClient gitClient =  new GitClient();
			File destinationFolder = new File("Downloads/");
			CrawlGitHub a = new CrawlGitHub(gitClient, destinationFolder);
			
			Set<String> keys = maps.keySet();
			for(String name:keys){
				String url = maps.get(name);
				a.httpDownload(url,"Downloads/mct/releases/",name +".zip");
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void testGetContributions(){
		try{
			
			//int i = searchGitHub.getContribution("nasa", "mct","7f0574327c8be70a91b8229d7beb59744dfce55e");
			
			//System.out.println(i);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public void testGetReleaseContributions(){
		try{
			
			/*
			List<usefuldata.Release> rs = DaoFactory.getReleaseDao().getAllRelease(4193864);
			List<Developer> developers = DaoFactory.getProjectDao().getAllDevelopers(4193864);
			
			Map<String,String> ss = searchGitHub.getReleaseDate("nasa","mct");
			
			Project project = new Project(new User("nasa"), "mct");
			
			for(int i =0;i<developers.size();i++){
				
				if(!developers.get(i).getLogin().equals("VWoeltjen"))
				{	
				Map<String,Integer> res = searchGitHub.getReleaseContribution(ss,project,developers.get(i).getLogin());
				Set<String> keys = res.keySet();
				
				for(String key_name:keys){
					for(int j = 0;j<rs.size();j++){
						if(key_name.equals(rs.get(j))){
							DaoFactory.getReleaseDao().addRelease(rs.get(j).getId(), developers.get(i).getId(), 4193864, res.get(key_name));	
						}
						
						
					}
					
				}
				
			}
				
			}
			
			*/
			
			Map<String,String> ss = searchGitHub.getReleaseDate("nasa","mct");
			Project project = new Project(new User("nasa"), "mct");
			
			List<Developer> dvls = DaoFactory.getProjectDao().getAllDevelopers(4193864);
			for(Developer dp:dvls){
				Map<String,Integer> res = searchGitHub.getReleaseContribution(ss,project,dp.getLogin());
				
				Set<String> keys = res.keySet();
				for(String key_name:keys){
					usefuldata.Release sars = DaoFactory.getReleaseDao().getRelease(4193864, key_name);
					if(res.get(key_name) > 0)
					DaoFactory.getReleaseDao().addRelease(sars.getId(),dp.getId(), 4193864,res.get(key_name));
				
				}
				
			}
			
			/*
			Map<String,Integer> res = searchGitHub.getReleaseContribution(ss,project,"dtran320");
			
			
			Set<String> keys = res.keySet();
			for(String key_name:keys){
				usefuldata.Release sars = DaoFactory.getReleaseDao().getRelease(4193864, key_name);
				if(res.get(key_name) > 0)
				DaoFactory.getReleaseDao().addRelease(sars.getId(),177059, 4193864,res.get(key_name));
			
			}
			*/
			
			
						
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public void testGetFile(){
		try{
			
//			ArrayList<String> files = searchGitHub.getFiles(ss, "kptran", "nasa", "mct","v1.7b3");
//			for(int i =0;i<files.size();i++){
//				System.out.println(files.get(i));
//			}
//			
			
			Map<String,String> ss = searchGitHub.getReleaseDate("nasa","mct");
			List<Developer> devs = DaoFactory.getProjectDao().getAllDevelopers(4193864);
			
			//ArrayList<String> developers = new ArrayList<String>();
			 			
			List<usefuldata.Release> rels = DaoFactory.getReleaseDao().getAllRelease(4193864);
			
			DevelopDigramImpl ddi = new DevelopDigramImpl();
			
			ArrayList<String> filenames = null;
			
			for(Developer d:devs){
				for(usefuldata.Release rrr:rels){
					
					filenames = searchGitHub.getFiles(ss, d.getLogin(), "nasa", "mct", rrr.getName());
					for(int i=0;i<filenames.size();i++){
						System.out.println(filenames.get(i));
					}
					
					if(!filenames.isEmpty()){
						String json_string = ddi.getDevelopDigramByVersion(filenames);
						if(json_string!=null)
						DaoFactory.getDeveloperEchartsDao().addDeveloperEcharts(4193864, rrr.getId(), d.getId(), json_string);
						
					}
					
				}
			}
			
			
			
			
			
//			DataHelperImpl dhpi = new DataHelperImpl();
//			ArrayList<String> files = dhpi.getFiles(ss, "kptran", "nasa", "mct", "v1.7b3");
//			for(int i =0;i<files.size();i++){
//				System.out.println(files.get(i));
//			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public void testGetMetaProject(){
		try {			
			List<Project> projects = searchGitHub.getProjects("mct", 1, SearchGitHub.INFINITY);
			for(Project p:projects){
				if(p.getName().equals("mct")){
					System.out.println(p.getId());
					System.out.println(p.getName());
					System.out.println(p.getDescription());
					
					System.out.println(p.getLanguage());
					System.out.println(p.getCheckoutURL());
					System.out.println(p.getSourceCodeURL());
					System.out.println(p.getCreatedAt().toString());
					System.out.println(p.getLastPushedAt().toString());
					
					System.out.println(p.isFork());
					System.out.println(p.hasDownloads());
					System.out.println(p.hasIssues());
					System.out.println(p.hasWiki());
					
					System.out.println(p.getWatchersCount());
					System.out.println(p.getForksCount());
					System.out.println(p.getIssuesCount());
					
					break;
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
	}
	
	
}