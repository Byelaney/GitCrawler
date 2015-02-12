package factory;

import metadao.CommitDao;
import metadao.CommitFileDao;
import metadao.ContributorDao;
import metadao.ProjectDao;
import metadao.UnPublishedReleaseDao;
import metadao.UserDao;
import metadao.impl.CommitDaoImpl;
import metadao.impl.CommitFileDaoImpl;
import metadao.impl.ContributorDaoImpl;
import metadao.impl.UnPublishedReleaseDaoImpl;
import metadao.impl.UserDaoImpl;



public class MetaDaoFactory {
	
	public static ContributorDao getContributorDao(){
		return ContributorDaoImpl.getInstance();
	}
	
	public static CommitDao getCommitDao(){
		return CommitDaoImpl.getInstance();
	}
	
	public static ProjectDao getProjectDao(){
		return metadao.impl.ProjectDaoImpl.getInstance();
	}
	
	public static UnPublishedReleaseDao getUnPublishedReleaseDao(){
		return UnPublishedReleaseDaoImpl.getInstance();
	}
	
	public static CommitFileDao getCommitFileDao(){
		return CommitFileDaoImpl.getInstance();
	}
	
	public static UserDao getUserDao(){
		return UserDaoImpl.getInstance();
	}
}
