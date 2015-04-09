package transform;

import java.util.List;

import usefuldata.Release;
import entity.UnPublishedRelease;

public interface MetaTransform {
	
	//get all unsorted releases
	public List<entity.UnPublishedRelease> getAllUnPublishedRelease(int project_id);
	
	public List<Release> getCommitNumber(List<UnPublishedRelease> ubsrs,int project_id);
	
	//commit per day
	public List<Release> getCommitRate(List<Release> rel);
	
	public List<Release> getIssueNum(List<Release> rel,int project_id);
	
}
