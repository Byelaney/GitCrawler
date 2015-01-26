package usefuldata;

public class ReleaseContribution {
	private String releaseName;
	private int contributions;
	
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
	
	
}
