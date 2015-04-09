package metadao;

import java.util.List;

import entity.Release;

public interface PublishedReleaseDao {
	public boolean addPublishedRelease(Release publishedRelease,int projectId);
	
	public boolean addPublishedReleases(List<Release> publishedRelease,int projectId);
		
}
