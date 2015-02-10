package metadao;

import entity.UnPublishedRelease;

public interface UnPublishedReleaseDao {
	public boolean addUnPublishedRelease(UnPublishedRelease unPublishedRelease,int projectId);
}
