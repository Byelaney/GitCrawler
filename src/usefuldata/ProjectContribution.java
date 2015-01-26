package usefuldata;

public class ProjectContribution {
	private int project_id;
	private int developer_id;
	private int contributions;
	private String projectName;
	
	
	public ProjectContribution(){
		super();
	}

	public ProjectContribution(int project_id, int developer_id,
			int contributions,String projectName) {
		super();
		this.project_id = project_id;
		this.developer_id = developer_id;
		this.contributions = contributions;
		this.projectName = projectName;
	}

	public int getProject_id() {
		return project_id;
	}

	public void setProject_id(int project_id) {
		this.project_id = project_id;
	}

	public int getDeveloper_id() {
		return developer_id;
	}

	public void setDeveloper_id(int developer_id) {
		this.developer_id = developer_id;
	}

	public int getContributions() {
		return contributions;
	}

	public void setContributions(int contributions) {
		this.contributions = contributions;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	
	
	
}
