package transform;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import usefuldata.Release;
import util.Dates;
import entity.UnPublishedRelease;
import factory.MetaDaoFactory;

public class MetaTransformImpl implements MetaTransform{

	@Override
	public List<UnPublishedRelease> getAllUnPublishedRelease(int project_id) {
		return MetaDaoFactory.getUnPublishedReleaseDao().getAllUnPublishedReleases(project_id);
	}

	@Override
	public List<Release> getCommitNumber(List<UnPublishedRelease> ubsrs,int project_id) {
		List<UnPublishedRelease> sorted_ubsr = Dates.unPublishedReleaseSort(ubsrs);
		
		List<Release> releases = new ArrayList<Release>();
		
		Map<String,String> release_date = new HashMap<String,String>();
		
		for(int i = 0;i<sorted_ubsr.size();i++){
			Release r = new Release();			
			r.setName(sorted_ubsr.get(i).getName());
			r.setDate(sorted_ubsr.get(i).getDate());
			r.setId(sorted_ubsr.get(i).getId());
			releases.add(r);
			release_date.put(r.getName(), r.getDate());
		}
		
		List<entity.Commit> commits = MetaDaoFactory.getCommitDao().getCommits(project_id);
		for(int i = 0;i<commits.size();i++){
			String date = Dates.dateToString(commits.get(i).getCommitDate());
			int idx = Dates.getDateIndex(date, release_date);
			if(idx >= releases.size())
				idx = releases.size() - 1;
			releases.get(idx).CommitsPlus();
		}
		
		return releases;
	}

	@Override
	public List<Release> getCommitRate(List<Release> rel) {
		for(int i = 0;i<rel.size()-1;i++){
			Release r = rel.get(i);
			String date1 = r.getDate();
			String date2 = rel.get(i+1).getDate();
			long diff_day = Dates.dayDiffer(date1, date2);
			double rate = diff_day/1.0;
			r.setCommit_rate(rate);
		}
		
		Release r = rel.get(rel.size() - 1);
		String date1 = r.getDate();
		String date2 = Dates.dateToString(new Date());
		long diff_day = Dates.dayDiffer(date1, date2);
		double rate = diff_day/7.0;
		r.setCommit_rate(rate);
		
		
		return rel;
	}

	@Override
	public List<Release> getIssueNum(List<Release> rel, int project_id) {
		Map<String,String> release_date = new HashMap<String,String>();
		for(int i = 0;i<rel.size();i++){		
			release_date.put(rel.get(i).getName(),rel.get(i).getDate());
		}
		List<entity.Issue> issues = MetaDaoFactory.getIssueDao().getAllMetaIssues(project_id);
		for(int i = 0;i<issues.size();i++){
			String date = Dates.dateToString(issues.get(i).getCreatedAt());
			int idx = Dates.getDateIndex(date, release_date);
			if(idx >= rel.size())
				idx = rel.size() - 1;
			
			rel.get(idx).issuePlus();
			
		}
				
		return rel;
	}
	
	
}
