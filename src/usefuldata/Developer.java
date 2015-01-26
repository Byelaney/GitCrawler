package usefuldata;

import java.util.List;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Reference;

import com.google.gson.annotations.SerializedName;


@Entity("developers")
public class Developer {
	
	@SerializedName("id")
    @Indexed(unique=true, dropDups=true)
    @Id private int id;
	
	@SerializedName("login")
	private String login;
	
	@SerializedName("email")
	private String email;
	
	@SerializedName("html_url")
	private String url;
	
	@Reference
	private List<Vitality> vitalities;	
	
	public Developer(){
		super();
	}

	
	
	public Developer(int id, String login,String email,String url, List<Vitality> vitalities) {
		super();
		this.id = id;
		this.login = login;
		this.vitalities = vitalities;
		this.email = email;
		this.url = url;
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public List<Vitality> getVitalities() {
		return vitalities;
	}

	public void setVitalities(List<Vitality> vitalities) {
		this.vitalities = vitalities;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getUrl() {
		return url;
	}



	public void setUrl(String url) {
		this.url = url;
	}

	
	
}
