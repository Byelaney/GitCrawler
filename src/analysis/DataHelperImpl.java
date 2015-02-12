package analysis;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entity.Commit;
import entity.CommitFile;
import entity.UnPublishedRelease;
import factory.DaoFactory;
import factory.MetaDaoFactory;
import usefuldata.Comment;
import usefuldata.CommitDate;
import usefuldata.VersionDate;
import util.Dates;



public class DataHelperImpl implements DataHelper {

	@Override
	public int getSize(String developerName, String projectName,
			String releaseName) {
		
		int developer_id = DaoFactory.getDeveloperDao().findDeveloper(developerName).getId();
		int project_id = DaoFactory.getProjectDao().getProject(projectName).getId();
		int release_id = DaoFactory.getReleaseDao().getRelease(project_id, releaseName).getId();
		
		int contributions = DaoFactory.getReleaseContribution().getReleaseContributions(developer_id, project_id, release_id);
		return contributions;
	}

	@Override
	public int getSize(String developerName, String projectName) {
		
		int developer_id = DaoFactory.getDeveloperDao().findDeveloper(developerName).getId();
		int project_id = DaoFactory.getProjectDao().getProject(projectName).getId();
		
		int contributions = DaoFactory.getProjectContribution().getProjectContributions(developer_id, project_id);
		return contributions;
	}


	@Override
	public ArrayList<String> getAllDeveloperNames(String projectName) {
		int project_id = DaoFactory.getProjectDao().getProject(projectName).getId();
		ArrayList<Integer> developer_ids = DaoFactory.getProjectContribution().getAllProjectContributors(project_id);
		ArrayList<String> names = new ArrayList<String>();
		for(int i = 0;i<developer_ids.size();i++){
			String login = DaoFactory.getDeveloperDao().findDeveloperForName(developer_ids.get(i)).getLogin();
			names.add(login);
		}
	
		return names;
	}

	

	@Override
	public ArrayList<VersionDate> getVersions(String projectName) {
		int project_id = DaoFactory.getProjectDao().getProject(projectName).getId();
		List<usefuldata.Release> releases = DaoFactory.getReleaseDao().getAllRelease(project_id);
		ArrayList<VersionDate> versionDates = new ArrayList<VersionDate>();
		for(int i =0;i<releases.size();i++){
			VersionDate vd = new VersionDate();
			vd.setVersion(releases.get(i).getName());
			vd.setDate(releases.get(i).getDate());
			vd.setOrder(i + 1);
			
			versionDates.add(vd);
		}
		
		return versionDates;
	}



	@Override
	public int getCodes(String projectName, String release) {
		
		int project_id = DaoFactory.getProjectDao().getProject(projectName).getId();	
		int codes = DaoFactory.getReleaseDao().getRelease(project_id, release).getCodes();
		
		return codes;
	}

	@Override
	public ArrayList<CommitDate> getCommits(String projectName) {
		int projectId = DaoFactory.getProjectDao().getProject(projectName).getId();
		List<Commit> commits = MetaDaoFactory.getCommitDao().getCommits(projectId);
		ArrayList<CommitDate> cds = new ArrayList<CommitDate>();
		
		for(Commit cmt:commits){
			CommitDate cd = new CommitDate();
			cd.setName(cmt.getCommiter().getLogin());
			cd.setDate(cmt.getCommitDate().toString());
			cds.add(cd);
		}
		
		return cds;
	}


	@Override
	public int getReleasetSize(String developerName, String projectName,
			String releaseName) {	
		int developer_id = DaoFactory.getDeveloperDao().findDeveloper(developerName).getId();
		int project_id = DaoFactory.getProjectDao().getProject(projectName).getId();
		int release_id = DaoFactory.getReleaseDao().getRelease(project_id, releaseName).getId();
		int contributions = DaoFactory.getReleaseContribution().getReleaseContributions(developer_id, project_id, release_id);
		
		return contributions;
	}

	@Override
	public ArrayList<String> getFiles(String projectName, String release,
			String developer) {
		int developer_id = DaoFactory.getDeveloperDao().findDeveloper(developer).getId();
		int project_id = DaoFactory.getProjectDao().getProject(projectName).getId();
		
		UnPublishedRelease upbr = MetaDaoFactory.getUnPublishedReleaseDao().getUnPublishedRelease(release, project_id);
		List<UnPublishedRelease> releases = MetaDaoFactory.getUnPublishedReleaseDao().getAllUnPublishedReleases(project_id);
		List<Commit> commits = MetaDaoFactory.getCommitDao().getCommits(project_id, developer_id);
		
		ArrayList<String> shas = new ArrayList<String>();
		
		int upbr_idx = 0;
		
		Map<String,String> release_dates = new HashMap<String,String>();
		for(int i =0;i<releases.size();i++){
			if(upbr.getName().equals(releases.get(i).getName())){
				upbr_idx = i;
			}
			release_dates.put(releases.get(i).getName(), releases.get(i).getDate());
		}
		
		for(Commit c:commits){
			int idx = Dates.getDateIndex(Dates.dateToString(c.getCommitDate()), release_dates);
			if(idx == upbr_idx){
				shas.add(c.getSha());
			}
		}
		
		ArrayList<String> files = new ArrayList<String>();
		
		for(String sha:shas){
			CommitFile commitFile = MetaDaoFactory.getCommitFileDao().getCommitFile(sha);
			files.add(commitFile.getFilename());
		}
		
		return files;
	}

	@Override
	public ArrayList<String> getFiles(String projectName, String developer,
			String start, String end) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<usefuldata.Issue> getIssues(String projectName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Comment> getCommentsCount(String projectName) {
		// TODO Auto-generated method stub
		return null;
	}

}
