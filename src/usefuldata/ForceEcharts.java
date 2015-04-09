package usefuldata;

public class ForceEcharts {
	private int project_id;
	private int release_id;
	private String relation;
	private String main_relation;
	
	public ForceEcharts(){
		super();
	}
	
	public ForceEcharts(int project_id, int release_id, String relation,
			String main_relation) {
		super();
		this.project_id = project_id;
		this.release_id = release_id;
		this.relation = relation;
		this.main_relation = main_relation;
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
	public String getRelation() {
		return relation;
	}
	public void setRelation(String relation) {
		this.relation = relation;
	}
	public String getMain_relation() {
		return main_relation;
	}
	public void setMain_relation(String main_relation) {
		this.main_relation = main_relation;
	}
	
	
}
