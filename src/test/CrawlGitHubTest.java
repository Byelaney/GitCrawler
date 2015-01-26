package test;

import java.io.File;
import java.util.concurrent.ExecutionException;





import com.google.inject.Guice;
import com.google.inject.Injector;

import crawler.CrawlGitHub;
import entity.Project;
import scmclient.GitClient;
import scmclient.ScmModule;

public class CrawlGitHubTest {
	
	private GitClient gitClient;
	
	
	public static void main(String []args){
		
		GitClient gitClient =  new GitClient();
		File destinationFolder = new File("Downloads/");
		CrawlGitHub a = new CrawlGitHub(gitClient, destinationFolder);
		Project p = new Project("mct", "description", "https://github.com/nasa/mct");
		a.downloadProject(p);
		
		
	}
	
	
	/*
	public void setup() {
		Injector injector = Guice.createInjector(new ScmModule());
		this.gitClient = injector.getInstance(GitClient.class);
	}

	
	public void testCrawlGithub() throws InterruptedException, ExecutionException {
		CrawlGitHub crawl = new CrawlGitHub(gitClient, new File("D:\\Files/AAA"));
		Project p = new Project("Music beets", "description", "https://github.com/sampsyo/beets");
		crawl.downloadProject(p);
		}
		*/
}
