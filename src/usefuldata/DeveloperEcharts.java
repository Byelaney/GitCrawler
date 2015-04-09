package usefuldata;

public class DeveloperEcharts {
	private int developer_id;
	private int project_id;
	private int release_id;
	private String json_string;
	
	public DeveloperEcharts(){
		super();
	}
	
	public DeveloperEcharts(int developer_id, int project_id,
			int release_id, String json_string) {
		super();
		this.developer_id = developer_id;
		this.project_id = project_id;
		this.release_id = release_id;
		this.json_string = json_string;
	}

	public int getDeveloper_id() {
		return developer_id;
	}
	public void setDeveloper_id(int developer_id) {
		this.developer_id = developer_id;
	}
	public int getProject_id() {
		return project_id;
	}
	public void setProject_id(int project_id) {
		this.project_id = project_id;
	}
	public int getRelease_id() {
		return release_id;
	}
	public void setRelease_id(int release_id) {
		this.release_id = release_id;
	}
	public String getJson_string() {
		return json_string;
	}
	public void setJson_string(String json_string) {
		this.json_string = json_string;
	}
	
	public String toString(){
		return  " developer_id" + developer_id + " project_id" + project_id
				+ " release_id" + release_id + " json_string" + json_string;
	}
	
}
