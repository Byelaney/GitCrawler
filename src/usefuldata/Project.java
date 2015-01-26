package usefuldata;

import java.util.HashMap;
import java.util.List;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Reference;

import com.google.gson.annotations.SerializedName;

@Entity("project")
public class Project {
	
	@SerializedName("id")
	@Indexed(unique=true, dropDups=true)
	@Id private int id;
	
	@SerializedName("name")
	private String name;
	
	@SerializedName("codes")
	private int codes;
	
	@SerializedName("files")
	private int files;
	
	@SerializedName("login")
	private String owner;
	
	@SerializedName("description")
	private String description;
	
	@Reference
	private List<Developer> developpers;
	
	@Reference
	private List<Release> releases;
	
	@Reference
	private HashMap<Integer,Integer> contributions;
	
	public Project(){
		super();
	}

	

	public Project(int id, String name, int codes, int files, String owner,
			String description,
			List<Developer> developpers, List<Release> releases,
			HashMap<Integer, Integer> contributions) {
		super();
		this.id = id;
		this.name = name;
		this.codes = codes;
		this.files = files;
		this.owner = owner;
		this.description = description;
		this.developpers = developpers;
		this.releases = releases;
		this.contributions = contributions;
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

	public List<Release> getReleases() {
		return releases;
	}

	public void setReleases(List<Release> releases) {
		this.releases = releases;
	}

	public HashMap<Integer, Integer> getContributions() {
		return contributions;
	}

	public void setContributions(HashMap<Integer, Integer> contributions) {
		this.contributions = contributions;
	}



	public String getOwner() {
		return owner;
	}



	public void setOwner(String owner) {
		this.owner = owner;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
}
