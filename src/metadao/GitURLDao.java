package metadao;

import java.util.ArrayList;
/**
 * giturl dao has 3 states
 * uncrawled,unupdated 
 * @author guanjun
 *
 */
public interface GitURLDao {
	public void addURL(String url,String state);
	
	public void deleteCrawledURL(String state);
	
	public boolean findURL(String URL);
	
	public ArrayList<String> getURLNotCrawled(String state);
	
	public void changeState(String url,String state);
	
	public String getState(String url);
}
