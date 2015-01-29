package metadao;

import entity.Commit;

public interface CommitDao {
	public boolean addCommit(Commit commit,int project_id);
}
