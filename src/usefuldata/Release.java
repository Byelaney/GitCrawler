package usefuldata;

import java.util.HashMap;
import java.util.List;

public class Release {
	private int id;
	private String name;
	private int codes;
	private String date;
	private int release_commits;
	
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
	
	
	
	
	
}
