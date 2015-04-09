package usefuldata;

public class EvolveEcharts {
	private int project_id;
	private int release_id;
	private String json_string;
	
	public EvolveEcharts(){
		super();
	}
	
	public EvolveEcharts(int project_id, int release_id,
			String json_string) {
		this.project_id = project_id;
		this.release_id = release_id;
		this.json_string = json_string;
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
	
	
}
