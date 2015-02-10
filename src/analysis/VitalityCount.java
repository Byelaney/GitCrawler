package analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import usefuldata.Vitality;
import util.Dates;
import dao.DeveloperDao;
import dao.ProjectDao;
import dao.ReleaseDao;
import factory.DaoFactory;

/**
 * this method used to compute vitality with 
 * crawled rawVitality  
 * @author guanjun
 *
 */
public class VitalityCount {
	
	private ReleaseDao releaseDao;
	private ProjectDao projectDao;
	private DeveloperDao developerDao;
	
	public VitalityCount(){
		releaseDao = DaoFactory.getReleaseDao();
		projectDao = DaoFactory.getProjectDao();
		developerDao = DaoFactory.getDeveloperDao();
	}
	
	public List<Vitality> handleVitalityRelease(List<Vitality> rawVitality,String projectName,String developer){
		int project_id = projectDao.getProject(projectName).getId();
		int developer_id = developerDao.findDeveloper(developer).getId();
		
		List<usefuldata.Release> releases = releaseDao.getAllRelease(project_id);
		Map<String,String> dateMap = new HashMap<String,String>();
		for(int i = 0;i<releases.size();i++){
			String tagName = releases.get(i).getName();
			String tagDate = releases.get(i).getDate();
			
			dateMap.put(tagName, tagDate);
		}
		
		ArrayList<String> sorted_release = Dates.dateSort(dateMap);
		
		List<Vitality> results = new ArrayList<Vitality>();
		int position = 0;
		for(int j = 0;j<rawVitality.size();j++){
			rawVitality.get(j).setProject_id(project_id);
			rawVitality.get(j).setDeveloper_id(developer_id);;
			
			position = Dates.getDateIndex(rawVitality.get(j).getDate(),dateMap);
			
			if(position >= sorted_release.size())
				 position = sorted_release.size() - 1;
			
			String tagBelong = sorted_release.get(position);
			
			for(usefuldata.Release selectedRel:releases){
				if(selectedRel.getName().equals(tagBelong)){
					rawVitality.get(j).setRelease_id(selectedRel.getId());
					break;
				}
			}
			
			results.add(rawVitality.get(j));
		}
		
		return results;
	}
	

	
}
