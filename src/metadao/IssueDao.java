package metadao;

import java.util.ArrayList;
import java.util.List;

public interface IssueDao {
	
	/**
	 * get useful issue
	 * @param project_id
	 * @return
	 */
	public ArrayList<usefuldata.Issue> getAllIssues(int project_id);
	
	public boolean addIssue(entity.Issue issue,int project_id);
	
	public boolean CheckaddIssue(entity.Issue issue,int project_id);
	
	public boolean addIssues(List<entity.Issue> issues,int project_id);
	
	public boolean CheckaddIssues(List<entity.Issue> issues,int project_id);
	
	public boolean updateIssue(entity.Issue issue,int project_id);
	
	/**
	 * get meta issue
	 * @param project_id
	 * @return
	 */
	public List<entity.Issue> getAllMetaIssues(int project_id);
	
	public int IssueNum(int project_id,String start_time,String end_time);
	
	public int IssueNum(int project_id,String start_time);
	
	public entity.Issue getIssue(int issue_id,int project_id);
	
}
