package usefuldata;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Vitality {
	
    private int id;
	
	private String date;
	
	private int vitality;
	
	private int developer_id;
	
	private int project_id;
	
	private int release_id;


	public Vitality(){
		super();
	}
	
	
	public Vitality(int id, String date, int vitality,
			int developer_id,int project_id,int release_id) {
		super();
		this.id = id;
		this.date = date;
		this.vitality = vitality;
		this.developer_id = developer_id;
		this.project_id = project_id;
		this.release_id = release_id;
		
	}

	public int getVitality() {
		return vitality;
	}
	public void setVitality(int vitality) {
		this.vitality = vitality;
	}
	
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String dateTrim(Date date){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");    
		String result=sdf.format(date);  
		return result;
	}
		

	public String toString(){
		return "date:" + date + ",number:" + vitality;
	}


	public int getRelease_id() {
		return release_id;
	}


	public void setRelease_id(int release_id) {
		this.release_id = release_id;
	}


	public int getDeveloper_id() {
		return developer_id;
	}


	public void setDeveloper_id(int developer_id) {
		this.developer_id = developer_id;
	}


	public int getProject_id() {
		return project_id;
	}


	public void setProject_id(int project_id) {
		this.project_id = project_id;
	}
	
	
	
}
