package dao;

public interface DeveloperEchartsDao {

	public boolean addDeveloperEcharts(int project_id,int release_id,int developer_id,String json);

	public String getDeveloperEcharts(String projectName,String releaseName,String developer);
}
