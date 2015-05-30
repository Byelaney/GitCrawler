package analysis;


import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import entity.Commit;
import entity.CommitFile;
import entity.Project;
import entity.UnPublishedRelease;
import factory.DaoFactory;
import factory.MetaDaoFactory;
import usefuldata.Comment;
import usefuldata.CommitDate;
import usefuldata.Issue;
import usefuldata.VersionDate;
import usefuldata.Vitality;
import util.Dates;



public class DataHelperImpl implements DataHelper {

	private entity.Project project;
	private List<UnPublishedRelease> ups;
	
	public DataHelperImpl(){
		super();
	}
	
	public DataHelperImpl(String projectName,String owner){
		this.project = MetaDaoFactory.getProjectDao().getProject(owner,projectName);
		this.ups = MetaDaoFactory.getUnPublishedReleaseDao().getAllUnPublishedReleases(this.project.getId());
	}
	
	
	@Override
	public int getSize(String developerName, String projectName,
			String releaseName,String owner) {
		
		int developer_id = MetaDaoFactory.getContributorDao().getContributor(developerName).getId();
		int project_id = MetaDaoFactory.getProjectDao().getProject(owner,projectName).getId();
		int release_id = MetaDaoFactory.getUnPublishedReleaseDao().getUnPublishedRelease(releaseName, project_id).getId();
		
		//int contributions = DaoFactory.getReleaseContribution().getReleaseContributions(developer_id, project_id, release_id);
		
		int contributions = getContributions(developer_id, project_id, release_id);
		
		return contributions;
	}

	@Override
	public int getSize(String developerName, String projectName,String owner) {
		
		int developer_id = MetaDaoFactory.getContributorDao().getContributor(developerName).getId();
		int project_id = MetaDaoFactory.getProjectDao().getProject(owner,projectName).getId();
		
		//int contributions = DaoFactory.getProjectContribution().getProjectContributions(developer_id, project_id);
		
		int contributions = MetaDaoFactory.getCommitDao().Contributions(project_id, developer_id);
		
		return contributions;
	}


	@Override
	public ArrayList<String> getAllDeveloperNames(String projectName,String owner) {
		int project_id = MetaDaoFactory.getProjectDao().getProject(owner,projectName).getId();
		
		List<entity.Contributor> all_contributors = MetaDaoFactory.getContributorDao().getAllContributors(project_id);
		
		//ArrayList<Integer> developer_ids = DaoFactory.getProjectContribution().getAllProjectContributors(project_id);
		
		ArrayList<String> names = new ArrayList<String>();
		for(int i = 0;i<all_contributors.size();i++){
			//String login = DaoFactory.getDeveloperDao().findDeveloperForName(developer_ids.get(i)).getLogin();
			String login = all_contributors.get(i).getLogin();
			if(login!=null)
			names.add(login);
		}
	
		return names;
	}

	

	@Override
	public ArrayList<VersionDate> getVersions(String projectName,String owner) {
		Project p = MetaDaoFactory.getProjectDao().getProject(owner,projectName);
		
		if(p==null)
			return null;
		else
		{
			int project_id = p.getId();
			//List<usefuldata.Release> releases = DaoFactory.getReleaseDao().getAllRelease(project_id);
			List<entity.UnPublishedRelease> uprs =  MetaDaoFactory.getUnPublishedReleaseDao().getAllUnPublishedReleases(project_id);
			
			ArrayList<VersionDate> versionDates = new ArrayList<VersionDate>();
			for(int i =0;i<uprs.size();i++){
				VersionDate vd = new VersionDate();
				vd.setVersion(uprs.get(i).getName());
				vd.setDate(uprs.get(i).getDate());
				vd.setOrder(i + 1);				
				versionDates.add(vd);
			}
			
			return versionDates;
		}
		
	}



	@Override
	public int getCodes(String projectName, String release,String owner) {
		
		int project_id = MetaDaoFactory.getProjectDao().getProject(owner,projectName).getId();	

		/**
		 * this could cause some bugs 
		 * remember to invoke this method
		 * after data into the dao database
		 */
		int codes = DaoFactory.getReleaseDao().getRelease(project_id, release).getCodes();
		
		return codes;
	}

	@Override
	public ArrayList<CommitDate> getCommits(String projectName,String owner) {
		int projectId = MetaDaoFactory.getProjectDao().getProject(owner,projectName).getId();
		List<Commit> commits = MetaDaoFactory.getCommitDao().getCommits(projectId);
		ArrayList<CommitDate> cds = new ArrayList<CommitDate>();
		
		for(Commit cmt:commits){
			CommitDate cd = new CommitDate();
			cd.setName(cmt.getCommiter().getLogin());
			cd.setDate(Dates.dateToString(cmt.getCommitDate()));
			cds.add(cd);
		}
		
		return cds;
	}


	@Override
	public int getReleaseSize(String developerName, String projectName,
			String releaseName,String owner) {	
		int developer_id = MetaDaoFactory.getContributorDao().getContributor(developerName).getId();
		int project_id = MetaDaoFactory.getProjectDao().getProject(owner,projectName).getId();
		int release_id = MetaDaoFactory.getUnPublishedReleaseDao().getUnPublishedRelease(releaseName, project_id).getId();
		
		int contributions = getContributions(developer_id, project_id, release_id);
		
		return contributions;
	}

	@Override
	public ArrayList<String> getFiles(String projectName, String release,
			String developer,String owner) {
		int developer_id = MetaDaoFactory.getContributorDao().getContributor(developer).getId();
		int project_id = MetaDaoFactory.getProjectDao().getProject(owner,projectName).getId();
		
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
			if(commitFile!=null)
			files.add(commitFile.getFilename());
		}
		
		return files;
	}
	
	
	@Override
	public ArrayList<String> getFiles(String projectName, String developer,
			String start, String end,String owner) {
		int developer_id = MetaDaoFactory.getContributorDao().getContributor(developer).getId();
		int project_id = MetaDaoFactory.getProjectDao().getProject(owner,projectName).getId();
		List<Commit> metacommits = MetaDaoFactory.getCommitDao().getCommits(project_id, developer_id);
		List<String> shas = new ArrayList<String>();
		for(Commit c:metacommits){
			int left = Dates.compare_date(start, Dates.dateToString(c.getCommitDate()));
			int right = Dates.compare_date(end, Dates.dateToString(c.getCommitDate()));
			//left should be 0 or -1
			//right should be 0 or 1
			if(left== 0 || left == -1){
				if(right ==0 || right == 1){
					shas.add(c.getSha());
				}
			}
		}
		ArrayList<String> files = new ArrayList<String>();
		for(String sha:shas){
			CommitFile cf = MetaDaoFactory.getCommitFileDao().getCommitFile(sha);
			if(cf!=null)
				files.add(cf.getFilename());
		}
		
		return files;
	}

	@Override
	public ArrayList<usefuldata.Issue> getIssues(String projectName,String owner) {
		int project_id = MetaDaoFactory.getProjectDao().getProject(owner,projectName).getId();
		ArrayList<usefuldata.Issue> issues = MetaDaoFactory.getIssueDao().getAllIssues(project_id);
		return issues;
	}

	@Override
	public ArrayList<Comment> getComments(String projectName,String owner) {
		int project_id = MetaDaoFactory.getProjectDao().getProject(owner,projectName).getId();
		ArrayList<Comment> comments = MetaDaoFactory.getCommentDao().getUsefulComments(projectName, project_id);
		return comments;
	}

	private int getContributions(int developer_id,int project_id,int release_id){
		int contributions = 0;
		UnPublishedRelease upr = MetaDaoFactory.getUnPublishedReleaseDao().getUnPublishedRelease(release_id);
		
		List<String> dates = new ArrayList<String>();
		//List<UnPublishedRelease> all = MetaDaoFactory.getUnPublishedReleaseDao().getAllUnPublishedReleases(project_id);
		if(this.ups != null){
			for(UnPublishedRelease u:this.ups){
				if(u!=null)
				dates.add(u.getDate());
			}
		}
		
		List<Commit> commits = MetaDaoFactory.getCommitDao().getCommits(project_id, developer_id);
		
		for(int i = 0;i<commits.size();i++){
			String date = Dates.dateToString(commits.get(i).getCommitDate());
			if(Dates.BelongToRelease(date,upr.getDate(),dates)){
				contributions += commits.get(i).getAdditionsCount() + commits.get(i).getDeletionsCount();		
			}
			
		}
		
		return contributions;
	}

	@Override
	public int getReleaseCommits(String projectName, String release,String owner) {
		String start_time = "";
		String end_time = "";
		int project_id = MetaDaoFactory.getProjectDao().getProject(owner,projectName).getId();
		List<UnPublishedRelease> uprs = MetaDaoFactory.getUnPublishedReleaseDao().getAllUnPublishedReleases(project_id);
		for(int i = 0;i<uprs.size();i++){
			if(uprs.get(i).getName().equals(release)){
				start_time = uprs.get(i).getDate();
				if(i!=uprs.size()-1)
					end_time = uprs.get(i+1).getDate();
				
				break;
			}
			
		}
		
		int num = 0;
		if(!end_time.equals(""))
			num = MetaDaoFactory.getCommitDao().releaseCommits(project_id, start_time, end_time);
		else
			num = MetaDaoFactory.getCommitDao().releaseCommits(project_id, start_time);
		
		return num;
	}

	@Override
	public int getIssueNum(String projectName, String release,String owner) {
		String start_time = "";
		String end_time = "";
		int project_id = MetaDaoFactory.getProjectDao().getProject(owner,projectName).getId();
		List<UnPublishedRelease> uprs = MetaDaoFactory.getUnPublishedReleaseDao().getAllUnPublishedReleases(project_id);
		for(int i = 0;i<uprs.size();i++){
			if(uprs.get(i).getName().equals(release)){
				start_time = uprs.get(i).getDate();
				if(i!=uprs.size()-1)
					end_time = uprs.get(i+1).getDate();
				
				break;
			}
			
		}
		
		int num = 0;
		if(!end_time.equals(""))
			num = MetaDaoFactory.getIssueDao().IssueNum(project_id, start_time, end_time);
		else
			num = MetaDaoFactory.getIssueDao().IssueNum(project_id, start_time);
		
		return num;
	}

	@Override
	public int getComprehensive() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTest() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getDocument() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Vitality> getVitality(String projectName, String developer,String owner) {
		int developer_id = MetaDaoFactory.getContributorDao().getContributor(developer).getId();
		int project_id = MetaDaoFactory.getProjectDao().getProject(owner,projectName).getId();
		List<Vitality> vitalities = new ArrayList<Vitality>();
				
		List<Commit> commits = MetaDaoFactory.getCommitDao().getCommits(project_id, developer_id);
		Map<String,Integer> date_commits = new HashMap<String,Integer>();
		
		List<String> all_dates = new ArrayList<String>();	
		if(this.ups != null){
			for(UnPublishedRelease u:this.ups){
				if(u!=null)
					all_dates.add(u.getDate());
			}
		}
			
		for(int i = 0;i<commits.size();i++){						
			String date = Dates.dateToString(commits.get(i).getCommitDate());			
			if(date_commits.containsKey(date)){
				Integer value = date_commits.get(date);
				date_commits.replace(date, value + 1);
			}
			else
				date_commits.put(date, 1);
		}
		
		Set<String> date_set = date_commits.keySet();
		for(String d:date_set){
			for(UnPublishedRelease u :this.ups){
				if(Dates.BelongToRelease(d, u.getDate(), all_dates)){
					Vitality v = new Vitality();
					v.setId(0);
					v.setProject_id(project_id);
					v.setRelease_id(u.getId());
					v.setDeveloper_id(developer_id);
					v.setDate(d);
					v.setVitality(date_commits.get(d));
					
					vitalities.add(v);
					break;
				}
			}
						
		}
		
		
		return vitalities;
	}

	@Override
	public String packFile(String filepath) {
		// TODO Auto-generated method stub		
		File fileDownload = new File(filepath);
		if (fileDownload.isDirectory()) {			
			createZip(filepath, filepath+".zip");
			return filepath + ".zip";
		}
		return filepath;
		
	}
	
	 private void createZip(String sourcePath, String zipPath) {
	        FileOutputStream fos = null;
	        ZipOutputStream zos = null;
	        try {
	            fos = new FileOutputStream(zipPath);
	            zos = new ZipOutputStream(fos);
	            writeZip(new File(sourcePath), "", zos);
	        } catch (FileNotFoundException e) {
	        } finally {
	            try {
	                if (zos != null) {
	                    zos.close();
	                }
	            } catch (IOException e) {
	            }

	        }
	    }
	    
	    private static void writeZip(File file, String parentPath, ZipOutputStream zos) {
	        if(file.exists()){
	            if(file.isDirectory()){//处理文件夹
	                parentPath+=file.getName()+File.separator;
	                File [] files=file.listFiles();
	                for(File f:files){
	                    writeZip(f, parentPath, zos);
	                }
	            }else{
	                FileInputStream fis=null;
	                DataInputStream dis=null;
	                try {
	                    fis=new FileInputStream(file);
	                    dis=new DataInputStream(new BufferedInputStream(fis));
	                    ZipEntry ze = new ZipEntry(parentPath + file.getName());
	                    zos.putNextEntry(ze);
	                    byte [] content=new byte[1024];
	                    int len;
	                    while((len=fis.read(content))!=-1){
	                        zos.write(content,0,len);
	                        zos.flush();
	                    }
	                    	                
	                } catch (FileNotFoundException e) {
	                } catch (IOException e) {
	                }finally{
	                    try {
	                        if(dis!=null){
	                            dis.close();
	                        }
	                    }catch(IOException e){
	                    }
	                }
	            }
	        }
	    }

		@Override
		public ArrayList<String> getIssueIds(String releaseName,
				String project, String owner) {
			ArrayList<String> ids = new ArrayList<String>();
			
			int project_id = MetaDaoFactory.getProjectDao().getProject(owner,project).getId();
			ArrayList<Issue> issues = MetaDaoFactory.getIssueDao().getAllIssues(project_id);
			
			String start_time = "";
			String end_time = "";
			
			List<UnPublishedRelease> uprs = MetaDaoFactory.getUnPublishedReleaseDao().getAllUnPublishedReleases(project_id);
			for(int i = 0;i<uprs.size();i++){
				if(uprs.get(i).getName().equals(releaseName)){
					start_time = uprs.get(i).getDate();
					if(i!=uprs.size()-1)
						end_time = uprs.get(i+1).getDate();
					
					break;
				}
				
			}
			
			for(int i = 0;i<issues.size();i++){
				if(inPeriod(issues.get(i).getInjectedDate(),start_time,end_time))
					ids.add(issues.get(i).getIssueId() + "");
			}
			
			return ids;
		}

		@Override
		public ArrayList<String> getRelevantPersonsInOneIssue(String issueId) {
			// TODO Auto-generated method stub
			return null;
		}    
	
		private boolean inPeriod(String date,String start_time,String end_time){
			int result1 = Dates.compare_date(date, start_time);
			int result2 = Dates.compare_date(date, end_time);
			
			if(result1 == 1 && result2 == -1)
				return true;
			
			return false;
		}
		
}
