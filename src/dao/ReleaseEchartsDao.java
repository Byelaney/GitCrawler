package dao;

import java.util.List;

import usefuldata.ReleaseEcharts;

public interface ReleaseEchartsDao {
	public String getReleaseEcharts(int project_id, int release_id);
	
	public ReleaseEcharts getReleaseEchartPO(int project_id, int release_id);
	
	public boolean addReleaseEcharts(ReleaseEcharts re);
	
	public boolean addReleaseEcharts(List<ReleaseEcharts> res);
	
	public boolean updateReleaseEcharts(ReleaseEcharts re);
}
