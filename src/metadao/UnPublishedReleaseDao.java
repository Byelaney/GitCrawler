package metadao;

import java.util.List;

import entity.UnPublishedRelease;

public interface UnPublishedReleaseDao {
	public boolean addUnPublishedRelease(UnPublishedRelease unPublishedRelease,int projectId);
	
	public UnPublishedRelease getUnPublishedRelease(String releaseName,int project_id);
	
	/**
	 * 
	 * @param project_id
	 * @return releases already sorted
	 */
	public List<UnPublishedRelease> getAllUnPublishedReleases(int project_id);
}
