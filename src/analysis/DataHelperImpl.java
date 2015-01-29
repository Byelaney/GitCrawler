package analysis;

import http.HttpModule;

import java.util.ArrayList;
import java.util.Map;

import com.google.inject.Guice;
import com.google.inject.Injector;

import search.SearchGitHub;
import search.SearchModule;
import factory.DaoFactory;

public class DataHelperImpl implements DataHelper{

	@Override
	public int getSize(String developerName, String projectName,
			String releaseName) {
		int size = DaoFactory.getReleaseContribution().getSize(developerName, projectName, releaseName);
		
		return size;
	}

	@Override
	public ArrayList<String> getFiles(Map<String,String> dateMap,String developerName,String owner,String projectName,
			String releaseName) {
		// TODO Auto-generated method stub
		
		Injector injector = Guice.createInjector(new SearchModule(), new HttpModule());
		SearchGitHub searchGitHub = injector.getInstance(SearchGitHub.class);;
		
		return searchGitHub.getFiles(dateMap,developerName , owner, projectName,releaseName);
		
	}

}
