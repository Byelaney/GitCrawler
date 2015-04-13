package dao;

import java.util.List;

import usefuldata.Developer;
import usefuldata.DeveloperEcharts;
import usefuldata.EvolveEcharts;
import usefuldata.ForceEcharts;
import usefuldata.Project;
import usefuldata.ProjectContribution;
import usefuldata.Release;
import usefuldata.ReleaseContribution;
import usefuldata.ReleaseEcharts;
import usefuldata.Vitality;
import factory.DaoFactory;
import analysis.AnalysisModule;

public class DaoController {
	private AnalysisModule anm;
	
	public DaoController(AnalysisModule anm){
		this.anm = anm;
	}
	
	/**
	 * save all analysis data to database
	 */
	public void IntoDataBase(){
		List<Developer> dps = this.anm.getDevelopers();
		DaoFactory.getDeveloperDao().addDevelopers(dps);
		
		List<DeveloperEcharts> dp_charts = this.anm.getDeveloper_echarts();
		DaoFactory.getDeveloperEchartsDao().addEcharts(dp_charts);
				
		List<ForceEcharts> fe = this.anm.getForce_echarts();
		DaoFactory.getForceChartDao().addForceCharts(fe);
		
		Project p = this.anm.getProject();
		DaoFactory.getProjectDao().addProject(p);
		
		List<ProjectContribution> pct = this.anm.getProject_contribution();
		DaoFactory.getProjectContribution().addProjectContributions(pct);
				
		List<Vitality> vs = this.anm.getVitalities();
		DaoFactory.getVitalityDao().addVitalities(vs);
		
		List<ReleaseEcharts> res = this.anm.getRelease_echarts();
		DaoFactory.getReleaseEchartsDao().addReleaseEcharts(res);
		
		List<Release> releases = this.anm.getReleases();		
		DaoFactory.getReleaseDao().addReleases(releases);
		
		List<ReleaseContribution> rct = this.anm.getRelease_contributions();
		DaoFactory.getReleaseContribution().addReleaseContributions(rct);
	}
	
	public void EvolveIntoDB(List<EvolveEcharts> ev_charts){
		DaoFactory.getEvolveEchartsDao().addEcharts(ev_charts);
	}
	
	
}
