package metadao;

import entity.CommitFile;

public interface CommitFileDao {
	public boolean addCommitFile(CommitFile cmf);
	
	public CommitFile getCommitFile(String commit_sha);
	
}
