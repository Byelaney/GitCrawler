package metadao;

import java.util.List;

import entity.Commit;

public interface CommitDao {
	/**
	 *  after crawling call this method
	 *  inside the method use Dates to format the date
	 * @param commit
	 * @param project_id
	 * @return
	 */
	public boolean addCommit(Commit commit,int project_id);
	
	public int CheckaddCommit(Commit commit,int project_id);
	
	
	public List<Commit> getCommits(int projectId);
	
	public List<Commit> getCommits(int projectId,int contributor_id);
	
	public Commit getCommit(String sha,int projectId);
	
	
	public boolean updateCommit(Commit commit,int project_id);
	
	public int Contributions(int projectId,int contributor_id);
	
	public int releaseCommits(int project_id,String start_time,String end_time);
	
	public int releaseCommits(int project_id,String start_time);
	
}
