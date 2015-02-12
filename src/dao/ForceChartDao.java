package dao;

import java.util.ArrayList;

public interface ForceChartDao {
	public boolean addForceChart(int project_id, int release_id,
			String relation,String main_relation);
	
	public ArrayList<String> getForceChart(int project_id,
			int release_id);
	
}
