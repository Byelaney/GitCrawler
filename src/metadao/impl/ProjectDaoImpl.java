package metadao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import util.Dates;
import entity.Project;
import metadao.MetaDaoHelper;
import metadao.ProjectDao;

public class ProjectDaoImpl implements ProjectDao{

	private static ProjectDaoImpl projectDao=new ProjectDaoImpl();
	private static MetaDaoHelper daoHelper=MetaDaoHelperImpl.getBaseDaoInstance();
	
	public static ProjectDaoImpl getInstance(){
		return projectDao;
	}
	
	
	@Override
	public boolean addProject(Project project) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		
		try{
			ps=con.prepareStatement("INSERT INTO `metadata`.`project` (`id`,`name`,`description`,`language`,`checkouturl`,`sourcecodeurl`,`createdat`,`lastpushedat`,`isfork`,`hasdownloads`,`hasissues`,`haswiki`,`watcherscount`,`forkscount`,`issuescount`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			
			ps.setInt(1, project.getId());
			ps.setString(2,project.getName());
			ps.setString(3, project.getDescription());
			ps.setString(4, project.getLanguage());
			ps.setString(5, project.getCheckoutURL());
			ps.setString(6, project.getSourceCodeURL());
			ps.setString(7, Dates.metaDateFormat(project.getCreatedAt().toString()));
			ps.setString(8, Dates.metaDateFormat(project.getLastPushedAt().toString()));
			
			
			ps.setInt(9, project.isFork()?1:0);
			ps.setInt(10, project.hasDownloads()?1:0);
			ps.setInt(11, project.hasIssues()?1:0);
			ps.setInt(12, project.hasWiki()?1:0);
			
			ps.setInt(13, project.getWatchersCount());
			ps.setInt(14, project.getForksCount());
			ps.setInt(15, project.getIssuesCount());
			
			
			ps.execute();			
			return true;
			
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			daoHelper.closePreparedStatement(ps);
			daoHelper.closeConnection(con);
		}
		
		return false;
	}
	
}
