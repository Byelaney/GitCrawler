package test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import usefuldata.DeveloperEcharts;
import usefuldata.EvolveEcharts;
import usefuldata.ForceEcharts;
import usefuldata.ProjectContribution;
import usefuldata.Release;
import usefuldata.ReleaseContribution;
import usefuldata.ReleaseEcharts;
import usefuldata.Vitality;
import util.Dates;
import analysis.AnalysisModule;
import analysis.CodeLinesCount;
import analysis.CodeLinesCountImpl;
import analysis.DataHelper;
import analysis.DataHelperImpl;
import analysis.DevelopDigram;
import analysis.DevelopDigramImpl;
import analysis.EvolveAnalysis;
import analysis.PackageDependency;
import analysis.PackageDependencyImpl;
import analysis.Relation;
import analysis.RelationImpl;
import crawler.CrawlModule;
import entity.Contributor;
import entity.UnPublishedRelease;
import factory.MetaDaoFactory;

public class ModuleTest {
	public static void main(String []args){
		ModuleTest mt = new ModuleTest();
		//System.out.println(111);
		mt.ProjectAnalysis();
	}
	
	public void test(){
		//CrawlToDB ctdb = new CrawlToDB("mct","nasa","aaa");
		//System.out.println(ctdb.getCrawlController().getProjectName());
		//List<UnPublishedRelease> unPublishedRelease = MetaDaoFactory.getUnPublishedReleaseDao().getAllUnPublishedReleases(4193864);
		//System.out.println(111);
		//ctdb.CrawlCommitsToDB(contributors);
		
		//ctdb.downLoadUnpublish_releases(unPublishedRelease);
	}
	
	public void EvolveEchartsAnalysis(){
		ArrayList<usefuldata.EvolveEcharts> evolve_echarts = new ArrayList<usefuldata.EvolveEcharts>();
		List<UnPublishedRelease> uprs = MetaDaoFactory.getUnPublishedReleaseDao().getAllUnPublishedReleases(15293);
		String json = "";
		EvolveAnalysis evolveAnalysis = new EvolveAnalysis("bcrypt-ruby");
		
		for(int i = 0;i<uprs.size();i++){
			json = evolveAnalysis.getEvolveJson(uprs.get(i).getName());
			if(!json.equals("")){
				EvolveEcharts e = new EvolveEcharts(15293,uprs.get(i).getId(),json);
				if(e!=null)
					evolve_echarts.add(e);	
			}
				
		}
	}
	
	
	public void AnalysisTest(){
		String projectName,owner,filepath;
    	projectName = "bcrypt-ruby";
    	owner = "codahale";
    	filepath = "Downloads/" + owner +"/";
		
    	
    	
    	//CrawlModule crawlModule = new CrawlModule(projectName,owner,filepath);
		
		//AnalysisModule analysisModule = new AnalysisModule(crawlModule);
		
		List<usefuldata.DeveloperEcharts> developer_echarts = new ArrayList<usefuldata.DeveloperEcharts>();
		
		List<UnPublishedRelease> uprs = MetaDaoFactory.getUnPublishedReleaseDao().getAllUnPublishedReleases(15293);	
		
		List<Contributor> contributors = MetaDaoFactory.getContributorDao().getAllContributors(15293);
		
		DataHelper dataHelperImpl = new DataHelperImpl("bcrypt-ruby");
		DevelopDigram developDigram = new DevelopDigramImpl();
		
		for(int i = 0;i<contributors.size();i++){
			for(int j = 0;j<uprs.size();j++){
				DeveloperEcharts dle = new DeveloperEcharts();
				dle.setProject_id(15293);
				dle.setRelease_id(uprs.get(j).getId());
				dle.setDeveloper_id(contributors.get(i).getId());
				ArrayList<String> file_names = dataHelperImpl.getFiles("bcrypt-ruby", uprs.get(j).getName(), contributors.get(i).getLogin());
				
//				System.out.println(file_names.size());
//				for(int z = 0;z<file_names.size();z++){
//					System.out.println(file_names.get(z) +" contributor:" + contributors.get(i).getLogin() + "   " + uprs.get(j).getName());
//				}
				
				//System.out.println(uprs.get(j).getName());
				//System.out.println(contributors.get(i).getLogin());
				
				String json_string = "";
				if(file_names!=null && file_names.size() != 0)
					json_string = developDigram.getDevelopDigramByVersion(file_names);
		
				if(!json_string.equals("")){
					dle.setJson_string(json_string);
					developer_echarts.add(dle);
				}
				
			}
		}
		
//		for(int i = 0;i<developer_echarts.size();i++){
//			System.out.println(developer_echarts.get(i).toString());
//			
//		}
			
		
	}
	
	public void ForceEchartsAnalysis(){
		ArrayList<usefuldata.ForceEcharts> force_echarts = new ArrayList<usefuldata.ForceEcharts>();
		
		List<UnPublishedRelease> uprs = MetaDaoFactory.getUnPublishedReleaseDao().getAllUnPublishedReleases(15293);	
		String relation_json = "";
		String main_relation_json = "";
		
		Relation relation;
		
		for(int i = 0;i<uprs.size();i++){
			relation = new RelationImpl("bcrypt-ruby",uprs.get(i).getName());
			relation_json = relation.getRelations();
			main_relation_json = relation.getMainRelations();
			if(!relation_json.equals("") && !main_relation_json.equals("")){
				ForceEcharts fe = new ForceEcharts(15293,uprs.get(i).getId(),relation_json,main_relation_json);
				if(fe!=null)
					force_echarts.add(fe);
			}
			
		}
		
		
		for(int i = 0;i<force_echarts.size();i++){
			System.out.println(force_echarts.get(i).getProject_id());
			System.out.println(force_echarts.get(i).getRelease_id());
			System.out.println(force_echarts.get(i).getRelation());
			System.out.println(force_echarts.get(i).getMain_relation());
			System.out.println("********");
		}
	}
	
	public void ProjectContributionAnalysis(){
		ArrayList<ProjectContribution> project_contribution = new ArrayList<ProjectContribution>();
		
		List<Contributor> contributors = MetaDaoFactory.getContributorDao().getAllContributors(15293);
		
		for(int i = 0;i<contributors.size();i++){
			ProjectContribution pjc = new ProjectContribution();
			pjc.setProjectName("bcrypt-ruby");
			pjc.setProject_id(15293);
			pjc.setDeveloper_id(contributors.get(i).getId());
			
			int ctb = MetaDaoFactory.getCommitDao().Contributions(15293, contributors.get(i).getId());
			pjc.setContributions(ctb);	
			project_contribution.add(pjc);
		}
		
		for(int i = 0;i<project_contribution.size();i++){
			System.out.println(project_contribution.get(i).getContributions());
		}
		
	}
	
	public void ReleaseContributionAnalysis(){		
		ArrayList<usefuldata.ReleaseContribution> release_contributions = new ArrayList<usefuldata.ReleaseContribution>();
		List<UnPublishedRelease> uprs = MetaDaoFactory.getUnPublishedReleaseDao().getAllUnPublishedReleases(15293);	
		
		List<Contributor> contributors = MetaDaoFactory.getContributorDao().getAllContributors(15293);
		
		DataHelper dataHelperImpl = new DataHelperImpl("bcrypt-ruby");
		
		for(int i = 0;i<contributors.size();i++){
		
		for(int j = 0;j<uprs.size();j++){
			ReleaseContribution rlct = new ReleaseContribution();
			rlct.setRelease_id(uprs.get(j).getId());
			rlct.setProject_id(15293);
			rlct.setDeveloper_id(contributors.get(i).getId());
			rlct.setReleaseName(uprs.get(j).getName());
			
			int contributions = dataHelperImpl.getReleaseSize(contributors.get(i).getLogin(), "bcrypt-ruby", uprs.get(j).getName());
			rlct.setContributions(contributions);
			
			release_contributions.add(rlct);
		}
		}
		
		for(int i = 0;i<release_contributions.size();i++){
			System.out.println(release_contributions.get(i).getContributions());
		}
	}
	
	public  void ReleaseEchartsAnalysis(){
		ArrayList<usefuldata.ReleaseEcharts> release_echarts = new ArrayList<usefuldata.ReleaseEcharts>();
		ArrayList<String> languages = new ArrayList<String>();
		languages.add("c");	
		
		PackageDependency packageDependency = new PackageDependencyImpl();
		
		ArrayList<String> location = new ArrayList<String>();
		location.add("Downloads/codahale_bcrypt-ruby/rel_2_0_3.zip");
		location.add("Downloads/codahale_bcrypt-ruby/rel_2_0_4.zip");
		location.add("Downloads/codahale_bcrypt-ruby/rel_2_0_5.zip");
		location.add("Downloads/codahale_bcrypt-ruby/rel_2_1_0.zip");
		location.add("Downloads/codahale_bcrypt-ruby/rel_2_1_1.zip");
		location.add("Downloads/codahale_bcrypt-ruby/rel_2_1_2.zip");
		location.add("Downloads/codahale_bcrypt-ruby/v3.0.0.zip");
		location.add("Downloads/codahale_bcrypt-ruby/v3.0.1.zip");
		location.add("Downloads/codahale_bcrypt-ruby/v3.1.1.zip");
		location.add("Downloads/codahale_bcrypt-ruby/v3.1.3.zip");
		location.add("Downloads/codahale_bcrypt-ruby/v3.1.6.zip");
		
		
		ArrayList<String> package_json = packageDependency.getPakageDependency(location, languages);		
		List<UnPublishedRelease> uprs = MetaDaoFactory.getUnPublishedReleaseDao().getAllUnPublishedReleases(15293);			
		
		for(int i = 0;i<uprs.size();i++){
			ReleaseEcharts re = new ReleaseEcharts(15293,
				uprs.get(i).getId(),package_json.get(i));		
			release_echarts.add(re);
			
			System.out.println(re.getJson_string());
		}
	}

	public  void ReleaseAnalysis(){		
		//ArrayList<usefuldata.Release> releases = new ArrayList<usefuldata.Release>();
		List<UnPublishedRelease> unpublish_releases = MetaDaoFactory.getUnPublishedReleaseDao().getAllUnPublishedReleases(15293);	
		
		CodeLinesCount codeLinesCount = new CodeLinesCountImpl();
		
		ArrayList<String> languages = new ArrayList<String>();
		languages.add("c");	
		
		ArrayList<String> uprs_location = new ArrayList<String>();
		uprs_location.add("Downloads/codahale_bcrypt-ruby/rel_2_0_3.zip");
		uprs_location.add("Downloads/codahale_bcrypt-ruby/rel_2_0_4.zip");
		uprs_location.add("Downloads/codahale_bcrypt-ruby/rel_2_0_5.zip");
		uprs_location.add("Downloads/codahale_bcrypt-ruby/rel_2_1_0.zip");
		uprs_location.add("Downloads/codahale_bcrypt-ruby/rel_2_1_1.zip");
		uprs_location.add("Downloads/codahale_bcrypt-ruby/rel_2_1_2.zip");
		uprs_location.add("Downloads/codahale_bcrypt-ruby/v3.0.0.zip");
		uprs_location.add("Downloads/codahale_bcrypt-ruby/v3.0.1.zip");
		uprs_location.add("Downloads/codahale_bcrypt-ruby/v3.1.1.zip");
		uprs_location.add("Downloads/codahale_bcrypt-ruby/v3.1.3.zip");
		uprs_location.add("Downloads/codahale_bcrypt-ruby/v3.1.6.zip");
		
		DataHelper dataHelperImpl = new DataHelperImpl("bcrypt-ruby");
		
		for(int i = 0;i<unpublish_releases.size();i++){
			Release release = new Release();
			release.setId(unpublish_releases.get(i).getId());			
			release.setName(unpublish_releases.get(i).getName());	
			
			int codes = codeLinesCount.getCodeLines(uprs_location.get(i), languages);
			release.setCodes(codes);
			release.setDate(unpublish_releases.get(i).getDate());
			
			int commits = dataHelperImpl.getReleaseCommits("bcrypt-ruby", unpublish_releases.get(i).getName());
			release.setRelease_commits(commits);
			
			release.setDocument(dataHelperImpl.getDocument());
			release.setTest(dataHelperImpl.getTest());
			
			if(i != unpublish_releases.size()-1){
				String date1 = unpublish_releases.get(i).getDate();
				String date2 = unpublish_releases.get(i+1).getDate();
				long diff_day = Dates.dayDiffer(date1, date2);
				double rate = diff_day/1.0;
				release.setCommit_rate(rate);
			}
			else{
				String date1 = unpublish_releases.get(i).getDate();
				String date2 = Dates.dateToString(new Date());
				long diff_day = Dates.dayDiffer(date1, date2);
				double rate = diff_day/7.0;
				release.setCommit_rate(rate);
			}
			
			int issues = dataHelperImpl.getIssueNum("bcrypt-ruby", unpublish_releases.get(i).getName());
			release.setIssue_number(issues);
			
			release.setComprehensive(dataHelperImpl.getComprehensive());
			
			System.out.println(release.getId());
			System.out.println(release.getName());
			System.out.println(release.getCodes());
			System.out.println(release.getDate());
			System.out.println(release.getCommit_rate());
			System.out.println(release.getIssue_number());
			
			System.out.println("***********");
			
			
		}
			
	}
	
	public void VitalityAnalysis(){
		ArrayList<Vitality> vitalities = new ArrayList<Vitality>();
		
		List<Contributor> developers = MetaDaoFactory.getContributorDao().getAllContributors(15293);
		
		DataHelper dataHelperImpl = new DataHelperImpl("bcrypt-ruby");
		
		for(Contributor c:developers){
			List<Vitality> v1 = dataHelperImpl.getVitality("bcrypt-ruby", c.getLogin());
			if(v1!=null){
				vitalities.addAll(v1);	
				
			}
			
		}
		
		
		for(int i = 0;i<vitalities.size();i++){
			System.out.println(vitalities.get(i).getVitality());
			System.out.println(vitalities.get(i).getDate());
			System.out.println("************");
			
		}
	}
	
//	public void ProjectAnalysis(){
//		ArrayList<String> languages = new ArrayList<String>();
//		languages.add("c");		
//		CodeLinesCount codeLinesCount = new CodeLinesCountImpl();
//		
//		int main_count = codeLinesCount.getCodeLines(crawlModule.getLatest_path(), languages);
//		this.project = crawlModule.getProject().ProjectTransform(main_count);
//				
//	}
	
	public void ProjectAnalysis(){
		ArrayList<String> languages = new ArrayList<String>();
		languages.add("c");		
		
		CodeLinesCount codeLinesCount = new CodeLinesCountImpl();
		
		int main_count = codeLinesCount.getCodeLines("Downloads/bcrypt-ruby.zip", languages);
		
		System.out.println(main_count);
		
	}
	
	
}
