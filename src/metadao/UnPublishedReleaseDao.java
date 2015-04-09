package metadao;

import java.util.List;

import entity.UnPublishedRelease;

public interface UnPublishedReleaseDao {
	public boolean addUnPublishedRelease(UnPublishedRelease unPublishedRelease,int projectId);
	
	public boolean CheckaddUnPublishedRelease(UnPublishedRelease unPublishedRelease,int projectId);
		
	public boolean addUnPublishedReleases(List<UnPublishedRelease> unPublishedRelease,int projectId);
	
	public boolean CheckaddUnPublishedReleases(List<UnPublishedRelease> unPublishedRelease,int projectId);
	
	public UnPublishedRelease getUnPublishedRelease(String releaseName,int project_id);
	
	public UnPublishedRelease getUnPublishedRelease(int release_id);
	
	public boolean UpdateRelease(UnPublishedRelease unPublishedRelease,int projectId);
	
	
	/**
	 * 
	 * @param project_id
	 * @return releases already sorted
	 */
	public List<UnPublishedRelease> getAllUnPublishedReleases(int project_id);
}
