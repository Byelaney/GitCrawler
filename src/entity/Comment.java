package entity;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;

import com.google.gson.annotations.SerializedName;

@Entity("comment")
public class Comment extends GitHubEntity{

	@SerializedName("id")
	@Indexed(unique=true, dropDups=true)
	@Id private int id;
	
	@SerializedName("url")
	private String url;
	
	private String user;
	private int user_id;
	
	@SerializedName("position")
	private int position;
	
	@SerializedName("line")
	private int line;
	
	@SerializedName("path")
	private String path;
	
	@SerializedName("commit_id")
	private String commit_id;
	
	@SerializedName("created_at")
	private String created_at;
	
	@SerializedName("updated_at")
	private String updated_at;
	
	@SerializedName("body")
	private String body;

	public Comment(){
		super();
	}
	
	public Comment(int id, String url, String user, int user_id, int position,
			int line, String path, String commit_id, String created_at,
			String updated_at, String body) {
		super();
		this.id = id;
		this.url = url;
		this.user = user;
		this.user_id = user_id;
		this.position = position;
		this.line = line;
		this.path = path;
		this.commit_id = commit_id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.body = body;
	}

	

	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getUrl() {
		return url;
	}



	public void setUrl(String url) {
		this.url = url;
	}



	public String getUser() {
		return user;
	}



	public void setUser(String user) {
		this.user = user;
	}



	public int getUser_id() {
		return user_id;
	}



	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}



	public int getPosition() {
		return position;
	}



	public void setPosition(int position) {
		this.position = position;
	}



	public int getLine() {
		return line;
	}



	public void setLine(int line) {
		this.line = line;
	}



	public String getPath() {
		return path;
	}



	public void setPath(String path) {
		this.path = path;
	}



	public String getCommit_id() {
		return commit_id;
	}



	public void setCommit_id(String commit_id) {
		this.commit_id = commit_id;
	}



	public String getCreated_at() {
		return created_at;
	}



	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}



	public String getUpdated_at() {
		return updated_at;
	}



	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}



	public String getBody() {
		return body;
	}



	public void setBody(String body) {
		this.body = body;
	}



	@Override
	public String getURL() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
