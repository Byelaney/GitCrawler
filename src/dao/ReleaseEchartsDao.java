package dao;

public interface ReleaseEchartsDao {
	public String getReleaseEcharts(String projectName,String releaseName);
	
	public boolean addReleaseEcharts(int project_id,int release_id,String json_string);
}
