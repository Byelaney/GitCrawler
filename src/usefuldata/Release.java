package usefuldata;

import java.util.HashMap;
import java.util.List;

public class Release {
	private int id;
	private String name;
	private int codes;
	private int project_id;
	private String date;
	private int release_commits;
	private int document;
	private int test;
	private double commit_rate;
	private int issue_number;
	private int comprehensive;
	
	private List<Developer> developpers;
	
	//map with developer_id and contributions
	private HashMap<Integer,Integer> contributions;
	
	public Release(){
		super();
	}

	public Release(int id, String name, int codes,
			List<Developer> developpers,
			HashMap<Integer, Integer> contributions,
			String date,
			int release_commits) {
		super();
		this.id = id;
		this.name = name;
		this.codes = codes;
		this.developpers = developpers;
		this.contributions = contributions;
		this.date = date;
		this.release_commits = release_commits;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCodes() {
		return codes;
	}

	public void setCodes(int codes) {
		this.codes = codes;
	}



	public List<Developer> getDeveloppers() {
		return developpers;
	}

	public void setDeveloppers(List<Developer> developpers) {
		this.developpers = developpers;
	}

	public HashMap<Integer, Integer> getContributions() {
		return contributions;
	}

	public void setContributions(HashMap<Integer, Integer> contributions) {
		this.contributions = contributions;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getRelease_commits() {
		return release_commits;
	}

	public void setRelease_commits(int release_commits) {
		this.release_commits = release_commits;
	}

	public int getDocument() {
		return document;
	}

	public void setDocument(int document) {
		this.document = document;
	}

	public int getTest() {
		return test;
	}

	public void setTest(int test) {
		this.test = test;
	}

	public double getCommit_rate() {
		return commit_rate;
	}

	public void setCommit_rate(double commit_rate) {
		this.commit_rate = commit_rate;
	}

	public int getIssue_number() {
		return issue_number;
	}

	public void setIssue_number(int issue_number) {
		this.issue_number = issue_number;
	}

	public int getComprehensive() {
		return comprehensive;
	}

	public void setComprehensive(int comprehensive) {
		this.comprehensive = comprehensive;
	}
	
	public void CommitsPlus(){
		this.release_commits++;
	}
	
	public void issuePlus(){
		this.issue_number++;
	}

	public int getProject_id() {
		return project_id;
	}

	public void setProject_id(int project_id) {
		this.project_id = project_id;
	}
	
	
	
}
