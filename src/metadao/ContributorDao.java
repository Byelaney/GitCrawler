package metadao;

import java.util.List;

import entity.Contributor;

public interface ContributorDao {
	
	public boolean addContributor(Contributor contributor,int project_id);
	
	public List<entity.Contributor> getAllContributors(int project_id);
}
