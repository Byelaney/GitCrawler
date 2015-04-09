package metadao;

import java.util.ArrayList;

public interface GitURLDao {
	public void addURL(String url,String state);
	
	public void deleteCrawledURL(String state);
	
	public boolean findURL(String URL);
	
	public ArrayList<String> getURLNotCrawled(String state);
	
	public void changeState(String url);
}
