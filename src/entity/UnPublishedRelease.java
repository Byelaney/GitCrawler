package entity;

import org.mongodb.morphia.annotations.Entity;

import com.google.gson.annotations.SerializedName;


@Entity("UnPublishedRelease")
public class UnPublishedRelease {

	@SerializedName("tag_name")
	private String name;
	
	@SerializedName("tag_name")
	private String zipball_url;
	
	@SerializedName("tag_name")
	private String tarball_url;
	private String commit_url;
	

	public UnPublishedRelease(String name, String zipball_url,
			String tarball_url, String commit_url) {
		super();
		this.name = name;
		this.zipball_url = zipball_url;
		this.tarball_url = tarball_url;
		this.commit_url = commit_url;
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
	
	
	
}
