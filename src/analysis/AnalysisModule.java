package analysis;

import java.util.List;

import metadao.MetaDaoController;

/**
 * this module use meta data and some methods to
 * get useful data 
 * @author guanjun
 *
 */
public abstract class AnalysisModule {	
	private String project_name;
	
	private MetaDaoController metaController;
	
	public AnalysisModule(){
		super();
	}
	
	public AnalysisModule(MetaDaoController metaController){
		this.metaController = metaController;	
		
		this.project_name = metaController.getDataSource().getProjectName();
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
	 * after invoke this method you can use Getter method to get data
	 */
	public abstract void analyzeAll();


	public String getProject_name() {
		return project_name;
	}


	public MetaDaoController getMetaController() {
		return metaController;
	}


	public List<usefuldata.Developer> getDevelopers() {
		return developers;
	}


	public List<usefuldata.DeveloperEcharts> getDeveloper_echarts() {
		return developer_echarts;
	}


	public List<usefuldata.EvolveEcharts> getEvolve_echarts() {
		return evolve_echarts;
	}


	public List<usefuldata.ForceEcharts> getForce_echarts() {
		return force_echarts;
	}


	public usefuldata.Project getProject() {
		return project;
	}


	public List<usefuldata.ProjectContribution> getProject_contribution() {
		return project_contribution;
	}


	public List<usefuldata.ReleaseContribution> getRelease_contributions() {
		return release_contributions;
	}


	public List<usefuldata.ReleaseEcharts> getRelease_echarts() {
		return release_echarts;
	}


	public List<usefuldata.Release> getReleases() {
		return releases;
	}


	public List<usefuldata.Vitality> getVitalities() {
		return vitalities;
	}
	
	public abstract void invokeEvolveAnalysis();
	
}
