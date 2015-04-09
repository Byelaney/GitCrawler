package metadao;

import java.util.List;

import entity.CommitFile;

public interface CommitFileDao {
	public boolean addCommitFile(CommitFile cmf);
	
	public boolean addCommitFiles(List<CommitFile> cmfs);
	
	public CommitFile getCommitFile(String commit_sha);
	
}
