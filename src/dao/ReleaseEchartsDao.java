package dao;

public interface ReleaseEchartsDao {
	public String getReleaseEcharts(int project_id, int release_id);
	
	public boolean addReleaseEcharts(int project_id,int release_id,String json_string);
}
