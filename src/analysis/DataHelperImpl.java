package analysis;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import factory.DaoFactory;
import usefuldata.CommitDate;
import usefuldata.VersionDate;



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
	public ArrayList<String> getFiles(Map<String, String> dateMap,
			String developerName, String owner, String projectName,
			String releaseName) {
		// TODO Auto-generated method stub
		return null;
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
	public ArrayList<String> getFiles(String projectName, String developer) {
		// TODO Auto-generated method stub
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < 50; i++) {
			if (developer.equals("a" + i)) {
				list.add("./Users/bacchus/Workspaces/MyEclipse Professional 2014/DataAnalysis/src"
						+ i%10  + "/getCodeLinesCount.java");
			}
		}
		return list;
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

	/*
	 * public ArrayList<CommitDate> getCommits(String projectName, String
	 * release) { // TODO Auto-generated method stub
	 * 
	 * ArrayList<CommitDate> list = new ArrayList<CommitDate>();
	 * 
	 * for (int j = 2; j < 9; j++) { String version = "version" + j;
	 * 
	 * if (release.equals(version)) {
	 * 
	 * if(j==5) { for (int i = 0; i < 3; i++) { CommitDate commitDate = new
	 * CommitDate(); commitDate.setDate("2014-0" + (j-1) + "-10");
	 * commitDate.setName("a" + i);
	 * 
	 * list.add(commitDate); } for (int i =3; i < 6; i++) { CommitDate
	 * commitDate = new CommitDate(); commitDate.setDate("2014-0" + (j-1) +
	 * "-18"); commitDate.setName("a" + i);
	 * 
	 * list.add(commitDate); } for (int i = 6; i < 9; i++) { CommitDate
	 * commitDate = new CommitDate(); commitDate.setDate("2014-0"+(j-1)+"-21");
	 * commitDate.setName("a" + i);
	 * 
	 * list.add(commitDate); } }
	 * 
	 * for (int i = 0; i < 3; i++) { CommitDate commitDate = new CommitDate();
	 * commitDate.setDate("2014-0" + (j-1) + "-10"); commitDate.setName("a" +
	 * (i+(j-2)*9));
	 * 
	 * list.add(commitDate); } for (int i =3; i < 6; i++) { CommitDate
	 * commitDate = new CommitDate(); commitDate.setDate("2014-0" + (j-1) +
	 * "-18"); commitDate.setName("a" + (i+(j-2)*9));
	 * 
	 * list.add(commitDate); } for (int i = 6; i < 9; i++) { CommitDate
	 * commitDate = new CommitDate(); commitDate.setDate("2014-0"+(j-1)+"-21");
	 * commitDate.setName("a" + (i+(j-2)*9));
	 * 
	 * list.add(commitDate); } } } return list; }
	 */

	@Override
	public int getIssueCount(String projectName, String release) {
		// TODO Auto-generated method stub
		return (int) ((Math.random()) * 30 + 1);
	}

	@Override
	public int getCommentCount(String projectName, String release) {
		// TODO Auto-generated method stub
		return (int) ((Math.random()) * 50 + 1);
	}

	@Override
	public int getCodes(String projectName, String release) {
		
		int project_id = DaoFactory.getProjectDao().getProject(projectName).getId();	
		int codes = DaoFactory.getReleaseDao().getRelease(project_id, release).getCodes();
		
		return codes;
	}

	@Override
	public ArrayList<CommitDate> getCommits(String projectName) {
		// TODO Auto-generated method stub
		ArrayList<CommitDate> list = new ArrayList<CommitDate>();

		for (int i = 0; i < 5; i++) {
			list.add(getCommitDate("a" + i, 1));
		}// 只参加了第一个版本

		for (int i = 5; i < 10; i++) {
			list.add(getCommitDate("a" + i, 8));
		}// 参加了最后一个版本之后

		for (int i = 10; i < 15; i++) {
			list.add(getCommitDate("a" + i, 7));
		}// 参加了最后一个版本

		for (int i = 15; i < 20; i++) {
			list.add(getCommitDate("a" + i, 7));
			list.add(getCommitDate("a" + i, 1));
		}// 参加了最后一个版本，和第一个版本

		for (int i = 20; i < 25; i++) {
			list.add(getCommitDate("a" + i, 8));
			list.add(getCommitDate("a" + i, 1));
		}// 参加了最后一个版本之后，和第一个版本

		for (int i = 25; i < 30; i++) {
			list.add(getCommitDate("a" + i, 8));
			list.add(getCommitDate("a" + i, 1));
		}// 参加了最后一个版本之后，和最后一个版本

		for (int i = 30; i < 35; i++) {
			list.add(getCommitDate("a" + i, 8));
			list.add(getCommitDate("a" + i, 7));
			list.add(getCommitDate("a" + i, 6));
			list.add(getCommitDate("a" + i, 5));
			list.add(getCommitDate("a" + i, 4));
			list.add(getCommitDate("a" + i, 3));
			list.add(getCommitDate("a" + i, 2));
			list.add(getCommitDate("a" + i, 1));
		}// 参加了所有版本

		for (int i = 35; i < 40; i++) {

			list.add(getCommitDate("a" + i, 2));
			CommitDate cd = new CommitDate();
			cd.setName("a" + 1);
			cd.setDate("2014-02-11");
			list.add(cd);

		}// 在第二个版本提交两次

		for (int i = 40; i < 45; i++) {

			list.add(getCommitDate("a" + i, 2));
			CommitDate cd = new CommitDate();
			cd.setName("a" + 1);
			cd.setDate("2014-02-11");
			list.add(cd);

			list.add(getCommitDate("a" + i, 5));
			list.add(getCommitDate("a" + i, 5));
			list.add(getCommitDate("a" + i, 5));
			list.add(getCommitDate("a" + i, 5));
			list.add(getCommitDate("a" + i, 5));

		}// 在第二个版本提交两次,第五个牒本提交相同五次

		for (int i = 45; i < 50; i++) {

			list.add(getCommitDate("a" + i, 2));
			CommitDate cd = new CommitDate();
			cd.setName("a" + 1);
			cd.setDate("2014-02-11");
			list.add(cd);

			list.add(getCommitDate("a" + i, 5));
			list.add(getCommitDate("a" + i, 5));
			list.add(getCommitDate("a" + i, 5));
			list.add(getCommitDate("a" + i, 5));
			list.add(getCommitDate("a" + i, 5));

			int j = 5;
			while (j > 0) {
				list.add(getCommitDate("a" + i, 8));
				j--;

			}
			j = 5;
			while (j > 0) {
				list.add(getCommitDate("a" + i, 10));
				j--;
			}

		}// 在第二个版本提交两次,第五个牒本提交相同五次,最后一个版本后提交10次

		return list;
	}

	private CommitDate getCommitDate(String name, int version) {
		CommitDate cd = new CommitDate();
		cd.setName(name);
		if (version < 10)
			cd.setDate("2014-0" + (version + 1) + "-14");
		else
			cd.setDate("2014-" + (version + 1) + "-14");
		return cd;
	}

}
