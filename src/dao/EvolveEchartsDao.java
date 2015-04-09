package dao;

import java.util.List;

import usefuldata.EvolveEcharts;

public interface EvolveEchartsDao {
	public boolean addEvolveEcharts(int project_id,int release_id,String json);
	
	public boolean addEcharts(EvolveEcharts eve);
	
	public boolean addEcharts(List<EvolveEcharts> ev_charts);
	
	public EvolveEcharts getEvolveEcharts(int project_id,int release_id);
	
	public boolean updateEcharts(EvolveEcharts eve);
}
