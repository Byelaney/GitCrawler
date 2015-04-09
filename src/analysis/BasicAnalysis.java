package analysis;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import metadao.MetaDaoController;
import usefuldata.DeveloperEcharts;
import usefuldata.EvolveEcharts;
import usefuldata.ForceEcharts;
import usefuldata.ProjectContribution;
import usefuldata.Release;
import usefuldata.ReleaseContribution;
import usefuldata.ReleaseEcharts;
import usefuldata.Vitality;
import util.Dates;
import util.ZipFiles;
import crawler.DataSource;
import entity.Contributor;
import entity.UnPublishedRelease;
import entity.User;
import factory.MetaDaoFactory;

public class BasicAnalysis extends AnalysisModule{
	private DataSource datasource;
	private MetaDaoController metaController;
	
	private CodeLinesCount codeLinesCount;
	private DevelopDigram developDigram;
	private EvolveAnalysis evolveAnalysis;
	private PackageDependency packageDependency;
	private Relation relation;
	
	private DataHelper dataHelperImpl;
	
	private int project_id;
	private String project_name;
	
	
	public BasicAnalysis(MetaDaoController metaController){
		this.metaController = metaController;
		this.datasource = metaController.getDataSource();
		
		this.project_id = datasource.getProject().getId();
		this.project_name = datasource.getProject().getName();
		
		this.codeLinesCount = new CodeLinesCountImpl();
		this.developDigram = new DevelopDigramImpl();
		this.evolveAnalysis = new EvolveAnalysis(project_name);
		this.packageDependency = new PackageDependencyImpl();
		this.dataHelperImpl = new DataHelperImpl(project_name);
				
	}
	

	
	private List<usefuldata.Developer> developers;
	private List<usefuldata.DeveloperEcharts> developer_echarts;
	private List<usefuldata.EvolveEcharts> evolve_echarts;
	private List<usefuldata.ForceEcharts> force_echarts;
	private usefuldata.Project project;
	private List<usefuldata.ProjectContribution> project_contribution;
	private List<usefuldata.ReleaseContribution> release_contributions;
	private List<usefuldata.ReleaseEcharts> release_echarts;
	private List<usefuldata.Release> releases;
	private List<usefuldata.Vitality> vitalities;
	
	
	/**
	 * analysis meta data and get all useful data
	 */
	public void analyzeAll(){
		DeveloperAnalysis();
		DeveloperEchartAnalysis();
		VitalityAnalysis();	
		ForceEchartsAnalysis();
		ProjectContributionAnalysis();
		
		ProjectAnalysis();
		
		ReleaseEchartsAnalysis();
		
		ReleaseAnalysis();
		
		ReleaseContributionAnalysis();
	}
	
	
	public void invokeEvolveAnalysis(){		
		//dao should be done before this operation
		EvolveEchartsAnalysis();
	}
	
	/**
	 * get developer with meta data
	 */
	private void DeveloperAnalysis(){
		ArrayList<usefuldata.Developer> developers = new ArrayList<usefuldata.Developer>();
		List<Contributor> contributors = MetaDaoFactory.getContributorDao().getAllContributors(this.project_id);
		List<User> users = MetaDaoFactory.getUserDao().getAllUsers(contributors);
		
		for(int i = 0;i<users.size();i++){
			developers.add(users.get(i).DeveloperTransform());
		}
		
		this.developers = developers;
	}
	
	/**
	 * get developer_echarts with meta data
	 */
	private void DeveloperEchartAnalysis(){
		this.developer_echarts = new ArrayList<usefuldata.DeveloperEcharts>();
		List<UnPublishedRelease> uprs = MetaDaoFactory.getUnPublishedReleaseDao().getAllUnPublishedReleases(this.project_id);	
		List<Contributor> contributors = MetaDaoFactory.getContributorDao().getAllContributors(this.project_id);
		
		for(int i = 0;i<contributors.size();i++){
			for(int j = 0;j<uprs.size();j++){
				DeveloperEcharts dle = new DeveloperEcharts();
				dle.setProject_id(this.project_id);
				dle.setRelease_id(uprs.get(j).getId());
				dle.setDeveloper_id(contributors.get(i).getId());
				ArrayList<String> file_names = this.dataHelperImpl.getFiles(this.project_name, uprs.get(j).getName(), contributors.get(i).getLogin());
				String json_string = "";
				if(file_names!=null && file_names.size() != 0)
					json_string = this.developDigram.getDevelopDigramByVersion(file_names);
		
				if(!json_string.equals("")){
					dle.setJson_string(json_string);
					developer_echarts.add(dle);
				}
			}
		}
		
	}
	
	/**
	 * get evolve_echarts with meta data
	 */
	private void EvolveEchartsAnalysis(){
		this.evolve_echarts = new ArrayList<usefuldata.EvolveEcharts>();
		List<UnPublishedRelease> uprs = MetaDaoFactory.getUnPublishedReleaseDao().getAllUnPublishedReleases(this.project_id);
		String json = "";
		
		for(int i = 0;i<uprs.size();i++){
			json = evolveAnalysis.getEvolveJson(uprs.get(i).getName());
			if(!json.equals("")){
				EvolveEcharts e = new EvolveEcharts(this.project_id,uprs.get(i).getId(),json);
				if(e!=null)
					this.evolve_echarts.add(e);	
			}
				
		}
	}
	
	/**
	 * get force_echarts with meta data
	 */
	private void ForceEchartsAnalysis(){
		this.force_echarts = new ArrayList<usefuldata.ForceEcharts>();
		
		List<UnPublishedRelease> uprs = MetaDaoFactory.getUnPublishedReleaseDao().getAllUnPublishedReleases(this.project_id);
		String relation_json = "";
		String main_relation_json = "";
		
		for(int i = 0;i<uprs.size();i++){
			this.relation = new RelationImpl(this.project_name,uprs.get(i).getName());
			relation_json = this.relation.getRelations();
			main_relation_json = this.relation.getMainRelations();
			if(!relation_json.equals("") && !main_relation_json.equals("")){
				ForceEcharts fe = new ForceEcharts(this.project_id,uprs.get(i).getId(),relation_json,main_relation_json);
				if(fe!=null)
					this.force_echarts.add(fe);
			}
			
		}
		
	}
	
	
	/**
	 * get project with meta data
	 */
	private void ProjectAnalysis(){
		ArrayList<String> languages = new ArrayList<String>();
		languages.add(this.datasource.getProject().getLanguage());	
		
		System.out.println(languages.get(0) + " is the main language!");
		
		String destination = "Downloads/"+this.getProject_name() +".zip";
		ZipFiles zip = new ZipFiles(this.datasource.getLatest_location(),destination);
		zip.ZipZup();
		
		File file = new File(this.datasource.getLatest_location());
		zip.recursiveDelete(file);
		
		System.out.println(destination + " is the destination!");
		
		int main_count = codeLinesCount.getCodeLines(destination, languages);
		System.out.println(main_count + " is the code num!");
		
		this.project = this.datasource.getProject().ProjectTransform(main_count);
				
	}
	
	/**
	 * get project_contribution with meta data
	 */
	private void ProjectContributionAnalysis(){
		this.project_contribution = new ArrayList<ProjectContribution>();
		
		List<Contributor> contributors = MetaDaoFactory.getContributorDao().getAllContributors(this.project_id);
		for(int i = 0;i<contributors.size();i++){
			ProjectContribution pjc = new ProjectContribution();
			pjc.setProjectName(this.project_name);
			pjc.setProject_id(this.project_id);
			pjc.setDeveloper_id(contributors.get(i).getId());
			
			int ctb = MetaDaoFactory.getCommitDao().Contributions(this.project_id, contributors.get(i).getId());
			pjc.setContributions(ctb);	
			this.project_contribution.add(pjc);
		}
		
	}
	
	
	/**
	 * get release_contribution with meta data
	 */
	private void ReleaseContributionAnalysis(){		
		this.release_contributions = new ArrayList<usefuldata.ReleaseContribution>();
		
		List<UnPublishedRelease> uprs = MetaDaoFactory.getUnPublishedReleaseDao().getAllUnPublishedReleases(this.project_id);
		
		List<Contributor> contributors = MetaDaoFactory.getContributorDao().getAllContributors(this.project_id);
		for(int i = 0;i<contributors.size();i++){
		
		for(int j = 0;j<uprs.size();j++){
			ReleaseContribution rlct = new ReleaseContribution();
			rlct.setRelease_id(uprs.get(j).getId());
			rlct.setProject_id(this.project_id);
			rlct.setDeveloper_id(contributors.get(i).getId());
			rlct.setReleaseName(uprs.get(j).getName());
			
			int contributions = this.dataHelperImpl.getReleaseSize(contributors.get(i).getLogin(), this.project_name, uprs.get(j).getName());
			rlct.setContributions(contributions);
			
			this.release_contributions.add(rlct);
		}
		}
	}
	
	
	/**
	 * get release_echarts with meta data
	 */
	private void ReleaseEchartsAnalysis(){
		this.release_echarts = new ArrayList<usefuldata.ReleaseEcharts>();
		ArrayList<String> languages = new ArrayList<String>();
		languages.add(this.datasource.getProject().getLanguage());
				
		ArrayList<String> package_json = this.packageDependency.getPakageDependency(this.datasource.getRelease_location(), languages);		
				
		List<UnPublishedRelease> uprs = MetaDaoFactory.getUnPublishedReleaseDao().getAllUnPublishedReleases(this.project_id);		
		for(int i = 0;i<uprs.size();i++){
			if(package_json.size() > i){
			ReleaseEcharts re = new ReleaseEcharts(this.project_id,
				uprs.get(i).getId(),package_json.get(i));		
			release_echarts.add(re);
			}else{
			ReleaseEcharts re = new ReleaseEcharts(this.project_id,
						uprs.get(i).getId(),"");		
			release_echarts.add(re);	
			}
			
	}
		
	}
	
	
	/**
	 * get release with meta data
	 */
	private void ReleaseAnalysis(){		
		ArrayList<String> languages = new ArrayList<String>();
		languages.add(this.datasource.getProject().getLanguage());	
		
		this.releases = new ArrayList<usefuldata.Release>();
		ArrayList<String> uprs_location = this.datasource.getRelease_location();
		List<UnPublishedRelease> unpublish_releases = MetaDaoFactory.getUnPublishedReleaseDao().getAllUnPublishedReleases(this.project_id);
		for(int i = 0;i<unpublish_releases.size();i++){
			Release release = new Release();
			release.setId(unpublish_releases.get(i).getId());			
			release.setName(unpublish_releases.get(i).getName());	
			
			int codes = codeLinesCount.getCodeLines(uprs_location.get(i), languages);
			
			//int codes = this.dataHelperImpl.getCodes(this.project_name, unpublish_releases.get(i).getName());
			
			release.setCodes(codes);
			release.setDate(unpublish_releases.get(i).getDate());
			
			int commits = this.dataHelperImpl.getReleaseCommits(this.project_name, unpublish_releases.get(i).getName());
			release.setRelease_commits(commits);
			
			release.setDocument(this.dataHelperImpl.getDocument());
			release.setTest(this.dataHelperImpl.getTest());
			
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
			
			int issues = this.dataHelperImpl.getIssueNum(this.project_name, unpublish_releases.get(i).getName());
			release.setIssue_number(issues);
			
			release.setComprehensive(this.dataHelperImpl.getComprehensive());
			
			releases.add(release);
		}
	
	}
	
	/**
	 * get vitality with meta data
	 */
	private void VitalityAnalysis(){
		this.vitalities = new ArrayList<Vitality>();
		List<Contributor> developers = MetaDaoFactory.getContributorDao().getAllContributors(this.project_id);
		for(Contributor c:developers){
			List<Vitality> v1 = this.dataHelperImpl.getVitality(this.project_name, c.getLogin());
			if(v1!=null)
			this.vitalities.addAll(v1);			
		}
		
	}

	
	
	public CodeLinesCount getCodeLinesCount() {
		return codeLinesCount;
	}

	public void setCodeLinesCount(CodeLinesCount codeLinesCount) {
		this.codeLinesCount = codeLinesCount;
	}

	public DevelopDigram getDevelopDigram() {
		return developDigram;
	}

	public void setDevelopDigram(DevelopDigram developDigram) {
		this.developDigram = developDigram;
	}

	public EvolveAnalysis getEvolveAnalysis() {
		return evolveAnalysis;
	}

	public void setEvolveAnalysis(EvolveAnalysis evolveAnalysis) {
		this.evolveAnalysis = evolveAnalysis;
	}

	public PackageDependency getPackageDependency() {
		return packageDependency;
	}

	public void setPackageDependency(PackageDependency packageDependency) {
		this.packageDependency = packageDependency;
	}

	public Relation getRelation() {
		return relation;
	}

	public void setRelation(Relation relation) {
		this.relation = relation;
	}

	public DataHelper getDataHelperImpl() {
		return dataHelperImpl;
	}

	public void setDataHelperImpl(DataHelper dataHelperImpl) {
		this.dataHelperImpl = dataHelperImpl;
	}

	public int getProject_id() {
		return project_id;
	}

	public void setProject_id(int project_id) {
		this.project_id = project_id;
	}

	public String getProject_name() {
		return project_name;
	}

	public void setProject_name(String project_name) {
		this.project_name = project_name;
	}

	public List<usefuldata.Developer> getDevelopers() {
		return developers;
	}

	public void setDevelopers(List<usefuldata.Developer> developers) {
		this.developers = developers;
	}

	public List<usefuldata.DeveloperEcharts> getDeveloper_echarts() {
		return developer_echarts;
	}

	public void setDeveloper_echarts(
			List<usefuldata.DeveloperEcharts> developer_echarts) {
		this.developer_echarts = developer_echarts;
	}

	public List<usefuldata.EvolveEcharts> getEvolve_echarts() {
		return evolve_echarts;
	}

	public void setEvolve_echarts(List<usefuldata.EvolveEcharts> evolve_echarts) {
		this.evolve_echarts = evolve_echarts;
	}

	public List<usefuldata.ForceEcharts> getForce_echarts() {
		return force_echarts;
	}

	public void setForce_echarts(List<usefuldata.ForceEcharts> force_echarts) {
		this.force_echarts = force_echarts;
	}

	public usefuldata.Project getProject() {
		return project;
	}

	public void setProject(usefuldata.Project project) {
		this.project = project;
	}

	public List<usefuldata.ProjectContribution> getProject_contribution() {
		return project_contribution;
	}

	public void setProject_contribution(
			List<usefuldata.ProjectContribution> project_contribution) {
		this.project_contribution = project_contribution;
	}

	public List<usefuldata.ReleaseContribution> getRelease_contributions() {
		return release_contributions;
	}

	public void setRelease_contributions(
			List<usefuldata.ReleaseContribution> release_contributions) {
		this.release_contributions = release_contributions;
	}

	public List<usefuldata.ReleaseEcharts> getRelease_echarts() {
		return release_echarts;
	}

	public void setRelease_echarts(List<usefuldata.ReleaseEcharts> release_echarts) {
		this.release_echarts = release_echarts;
	}

	public List<usefuldata.Release> getReleases() {
		return releases;
	}

	public void setReleases(List<usefuldata.Release> releases) {
		this.releases = releases;
	}

	public List<usefuldata.Vitality> getVitalities() {
		return vitalities;
	}

	public void setVitalities(List<usefuldata.Vitality> vitalities) {
		this.vitalities = vitalities;
	}


	public MetaDaoController getMetaController() {
		return metaController;
	}

	public void setMetaController(MetaDaoController metaController) {
		this.metaController = metaController;
	}

}
