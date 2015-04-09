package dao;

import java.util.ArrayList;
import java.util.List;

import usefuldata.ForceEcharts;

public interface ForceChartDao {
	public boolean addForceChart(int project_id, int release_id,
			String relation,String main_relation);
	
	public boolean addForceChart(ForceEcharts fc);
	
	
	public ArrayList<String> getForceChart(int project_id,
			int release_id);
	
	public boolean addForceCharts(List<ForceEcharts> fe);
	
	public ForceEcharts getForceChartPj(int project_id,int release_id);
	
	public boolean updateForceChart(ForceEcharts fc);
}
