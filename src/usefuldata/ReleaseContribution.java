package usefuldata;

public class ReleaseContribution {
	private String releaseName;
	private int contributions;
	
	/**
	 * 
	 */
	private int release_id;
	private int developer_id;
	private int project_id;
	
	public ReleaseContribution(){
		super();
	}

	
	
	public ReleaseContribution(String releaseName, int contributions) {
		super();
		this.releaseName = releaseName;
		this.contributions = contributions;
	}


	public String getReleaseName() {
		return releaseName;
	}



	public void setReleaseName(String releaseName) {
		this.releaseName = releaseName;
	}



	public int getContributions() {
		return contributions;
	}

	public void setContributions(int contributions) {
		this.contributions = contributions;
	}



	public int getRelease_id() {
		return release_id;
	}



	public void setRelease_id(int release_id) {
		this.release_id = release_id;
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
	
	
}
