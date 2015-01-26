package usefuldata;

import java.util.HashMap;
import java.util.List;

public class Release {
	private int id;
	private String name;
	private int codes;
	private int files;
	private String date;
	
	private List<Developer> developpers;
	
	//map with developer_id and contributions
	private HashMap<Integer,Integer> contributions;
	
	public Release(){
		super();
	}

	public Release(int id, String name, int codes, int files,
			List<Developer> developpers,
			HashMap<Integer, Integer> contributions,
			String date) {
		super();
		this.id = id;
		this.name = name;
		this.codes = codes;
		this.files = files;
		this.developpers = developpers;
		this.contributions = contributions;
		this.date = date;
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

	public int getFiles() {
		return files;
	}

	public void setFiles(int files) {
		this.files = files;
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
	
	
	
}
