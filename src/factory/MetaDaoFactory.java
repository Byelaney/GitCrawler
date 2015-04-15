package factory;

import metadao.CommentDao;
import metadao.CommitDao;
import metadao.CommitFileDao;
import metadao.ContributorDao;
import metadao.CrawlindexDao;
import metadao.GitURLDao;
import metadao.IssueDao;
import metadao.ProjectDao;
import metadao.PublishedReleaseDao;
import metadao.UnPublishedReleaseDao;
import metadao.UserDao;
import metadao.impl.CommentDaoImpl;
import metadao.impl.CommitDaoImpl;
import metadao.impl.CommitFileDaoImpl;
import metadao.impl.ContributorDaoImpl;
import metadao.impl.CrawlindexDaoImpl;
import metadao.impl.GitURLDaoImpl;
import metadao.impl.IssueDaoImpl;
import metadao.impl.PublishedReleaseDaoImpl;
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
	
	public static IssueDao getIssueDao(){
		return IssueDaoImpl.getInstance();
	}
	
	public static CommentDao getCommentDao(){
		return CommentDaoImpl.getInstance();
	}
	
	public static PublishedReleaseDao getPublishedReleaseDao(){
		return PublishedReleaseDaoImpl.getInstance();
	}
	
	public static CrawlindexDao getCrawlindexDao(){
		return CrawlindexDaoImpl.getInstance();
	}
	
	public static GitURLDao getGitURLDao(){
		return GitURLDaoImpl.getInstance();
	}
}
