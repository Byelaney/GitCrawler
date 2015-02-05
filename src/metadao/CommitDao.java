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
	public List<Commit> getCommits(String projectName);
	public boolean updateCommit(Commit commit);
}
