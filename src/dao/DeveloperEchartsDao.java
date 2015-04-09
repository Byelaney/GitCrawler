package dao;

import java.util.List;

import usefuldata.DeveloperEcharts;

public interface DeveloperEchartsDao {

	public boolean addDeveloperEcharts(int project_id,int release_id,int developer_id,String json);

	public boolean addEcharts(DeveloperEcharts dpe);
	
	public String getDeveloperEcharts(int project_id, int release_id,
			int developer_id);
	
	public DeveloperEcharts getDeveloperEchart(int project_id, int release_id,
			int developer_id);
		
	public boolean addEcharts(List<DeveloperEcharts> des);
	
	public boolean updateEcharts(DeveloperEcharts dpe);
}
