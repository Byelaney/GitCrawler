package factory;

import dao.DeveloperDao;
import dao.ProjectContributionDao;
import dao.ProjectDao;
import dao.ReleaseContributionDao;
import dao.ReleaseDao;
import dao.VitalityDao;
import dao.impl.DeveloperDaoImpl;
import dao.impl.ProjectContributionDaoImpl;
import dao.impl.ProjectDaoImpl;
import dao.impl.ReleaseContributionDaoImpl;
import dao.impl.ReleaseDaoImpl;
import dao.impl.VitalityDaoImpl;

/**
 * used to get DaoImpl instances
 * @author guanjun
 *
 */
public class DaoFactory {
	
	public static DeveloperDao getDeveloperDao(){
		return DeveloperDaoImpl.getInstance();
	}
	
	public static VitalityDao getVitalityDao(){
		return VitalityDaoImpl.getInstance();
	}
	
	public static ReleaseDao getReleaseDao(){
		return ReleaseDaoImpl.getInstance();
	}
	
	public static ProjectDao getProjectDao(){
		return ProjectDaoImpl.getInstance();
	}
	
	public static ProjectContributionDao getProjectContribution(){
		return ProjectContributionDaoImpl.getInstance();
	}
	
	
	public static ReleaseContributionDao getReleaseContribution(){
		return ReleaseContributionDaoImpl.getInstance();
	}
	
}
