package usefuldata;

import java.util.ArrayList;

public class Issue {
	private int issueId;
	private String injectedDate;
	private ArrayList<String> updateDate;
    private String state;
    
	public int getIssueId() {
		return issueId;
	}
	public void setIssueId(int issueId) {
		this.issueId = issueId;
	}
	public String getInjectedDate() {
		return injectedDate;
	}
	public void setInjectedDate(String injectedDate) {
		this.injectedDate = injectedDate;
	}
	public ArrayList<String> getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(ArrayList<String> updateDate) {
		this.updateDate = updateDate;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
}
