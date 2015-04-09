package metadao;

import java.util.List;

import entity.Contributor;

public interface ContributorDao {
	
	public boolean CheckaddContributor(Contributor contributor,int project_id);
	
	public boolean addContributor(Contributor contributor,int project_id);
	
	public boolean addContributors(List<Contributor> contributor,int project_id);
	
	public boolean CheckaddContributors(List<Contributor> contributor,int project_id);
	
	public List<entity.Contributor> getAllContributors(int project_id);
	
	public Contributor getContributor(String login);
	
	public boolean updateContributor(Contributor contributor,int project_id);
}
