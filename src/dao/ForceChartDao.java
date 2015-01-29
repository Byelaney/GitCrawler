package dao;

import java.util.ArrayList;

public interface ForceChartDao {
	public boolean addForceChart(String projectName,String releaseName,String relation,String main_relation);
	
	public ArrayList<String> getForceChart(String projectName,String releaseName);
	
}
