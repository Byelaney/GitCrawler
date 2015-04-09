package metadao;

import entity.Crawlindex;

public interface CrawlindexDao {
	public Crawlindex getCrawlindex(int project_id);

	public boolean addCrawlindex(Crawlindex crawlindex);
	
	public boolean updateContributor(Crawlindex crawlindex);
}
