package crawler;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;

import entity.Project;
import scmclient.GitClient;

import com.google.inject.Inject;


public class CrawlGitHub extends ForgeCrawler{
	
	private final GitClient gitClient;
	private final File destinationFolder;

	@Inject
	public CrawlGitHub(GitClient gitClient, File destinationFolder) {
		this.gitClient = gitClient;
		this.destinationFolder = destinationFolder;
	}

	@Override
	public File downloadProject(Project project) throws DownloadException {
		String projectName = project.getName();
		String projectUrl = project.getCheckoutURL();
		File projectDestinationFolder = new File(destinationFolder, projectName);

		System.out.println(String.format("Downloading %s project..", project.getName()));

		try {
			this.gitClient.clone(projectUrl, projectDestinationFolder);
			System.out.println(String.format("Done! The project is available at %s", projectDestinationFolder.getAbsolutePath()));
			return projectDestinationFolder;
		} catch (Exception e) {
			String error = String.format("Unable to download %s (%s) project", project.getName(), project.getCheckoutURL());
			System.out.println(error);
			throw new DownloadException(error);
		}
	}
	
	/**
	 * use Apache common IO to download
	 * @param httpUrl
	 * @param path
	 * @param saveFile
	 * @return 
	 */
	public boolean httpDownload(String httpUrl,String path,String saveFile){  
	      
		File file = new File(path + saveFile);
		try{
		FileUtils.copyURLToFile(new URL(httpUrl),file);
		System.out.println("now downloading ... " + saveFile);
		}catch(IOException ioe){
			try {
				//wait 5 minutes
				Thread.sleep(1000 * 60 * 5);
				httpDownload(httpUrl,path,saveFile);
			
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
		System.out.println(saveFile + " completed!");
		return true;
	   }
	
	
}
