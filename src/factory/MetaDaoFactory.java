package factory;

import metadao.CommitDao;
import metadao.ContributorDao;
import metadao.impl.CommitDaoImpl;
import metadao.impl.ContributorDaoImpl;


public class MetaDaoFactory {
	public static ContributorDao getContributorDao(){
		return ContributorDaoImpl.getInstance();
	}
	
	public static CommitDao getCommitDao(){
		return CommitDaoImpl.getInstance();
	}
}
