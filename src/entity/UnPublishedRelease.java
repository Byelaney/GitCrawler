package entity;

import org.mongodb.morphia.annotations.Entity;

import com.google.gson.annotations.SerializedName;


@Entity("UnPublishedRelease")
public class UnPublishedRelease {

	@SerializedName("name")
	private String name;
	
	@SerializedName("zipball_url")
	private String zipball_url;
	
	@SerializedName("tarball_url")
	private String tarball_url;
	
	
	private String commit_url;
	
	private String date;

	public UnPublishedRelease(String name, String zipball_url,
			String tarball_url, String commit_url,String date) {
		super();
		this.name = name;
		this.zipball_url = zipball_url;
		this.tarball_url = tarball_url;
		this.commit_url = commit_url;
		this.date = date;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getZipball_url() {
		return zipball_url;
	}
	public void setZipball_url(String zipball_url) {
		this.zipball_url = zipball_url;
	}
	public String getTarball_url() {
		return tarball_url;
	}
	public void setTarball_url(String tarball_url) {
		this.tarball_url = tarball_url;
	}
	public String getCommit_url() {
		return commit_url;
	}
	public void setCommit_url(String commit_url) {
		this.commit_url = commit_url;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	
	
}
