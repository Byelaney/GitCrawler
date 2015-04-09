package metadao.impl;

import helper.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.Dates;
import entity.Project;
import entity.User;
import metadao.ProjectDao;

public class ProjectDaoImpl implements ProjectDao{

	private static ProjectDaoImpl projectDao=new ProjectDaoImpl();
	private static DBHelper daoHelper=MetaDaoHelperImpl.getBaseDaoInstance();
	
	public static ProjectDaoImpl getInstance(){
		return projectDao;
	}
	
	
	@Override
	public boolean addProject(Project project) {
		Project duplicate = getProject(project.getName());
		if(duplicate!=null)
			return updateProject(project);
			
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		
		try{
			ps=con.prepareStatement("INSERT INTO `metadata`.`project` (`id`,`name`,`description`,`language`,`checkouturl`,`sourcecodeurl`,`createdat`,`lastpushedat`,`isfork`,`hasdownloads`,`hasissues`,`haswiki`,`watcherscount`,`forkscount`,`issuescount`,`owner`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			
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
			
			ps.setString(16, project.getUser().getName());
			
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


	@Override
	public Project getProject(String owner, String projectName) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		try{
			ps=con.prepareStatement("select * from metadata.project where name =? and owner =?");
			ps.setString(1, projectName);
			ps.setString(2, owner);
			
			rs=ps.executeQuery();
			
			Project project = null;
			if(rs.next()){
				project = new Project();
				project.setId(rs.getInt("id"));
				project.setName(rs.getString("name"));
				project.setDescription(rs.getString("description"));
				project.setLanguage(rs.getString("language"));
				project.setCheckoutURL(rs.getString("checkouturl"));
				project.setSourceCodeURL(rs.getString("sourcecodeurl"));
				project.setCreatedAt(rs.getString("createdat"));
				project.setLastPushedAt(rs.getString("lastpushedat"));
				project.setIsFork((rs.getInt("isfork") == 0)?false:true);
				project.setHasDownloads((rs.getInt("hasdownloads") == 0)?false:true);
				project.setHasIssues((rs.getInt("hasissues") == 0)?false:true);
				project.setHasWiki((rs.getInt("haswiki") == 0)?false:true);
				project.setWatchersCount(rs.getInt("watcherscount"));
				project.setForksCount(rs.getInt("forkscount"));
				project.setIssuesCount(rs.getInt("issuescount"));
				project.setUser(new User(rs.getString("owner")));
			}	
			
			return project;
			
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			daoHelper.closeResult(rs);
			daoHelper.closePreparedStatement(ps);
			daoHelper.closeConnection(con);
		}
		
		return null;
	}


	@Override
	public Project getProject(String projectName) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		try{
			ps=con.prepareStatement("select * from metadata.project where name =?");
			ps.setString(1, projectName);
			
			rs=ps.executeQuery();
			
			Project project = null;
			if(rs.next()){
				project = new Project();
				project.setId(rs.getInt("id"));
				project.setName(rs.getString("name"));
				project.setDescription(rs.getString("description"));
				project.setLanguage(rs.getString("language"));
				project.setCheckoutURL(rs.getString("checkouturl"));
				project.setSourceCodeURL(rs.getString("sourcecodeurl"));
				project.setCreatedAt(rs.getString("createdat"));
				project.setLastPushedAt(rs.getString("lastpushedat"));
				project.setIsFork((rs.getInt("isfork") == 0)?false:true);
				project.setHasDownloads((rs.getInt("hasdownloads") == 0)?false:true);
				project.setHasIssues((rs.getInt("hasissues") == 0)?false:true);
				project.setHasWiki((rs.getInt("haswiki") == 0)?false:true);
				project.setWatchersCount(rs.getInt("watcherscount"));
				project.setForksCount(rs.getInt("forkscount"));
				project.setIssuesCount(rs.getInt("issuescount"));
				project.setUser(new User(rs.getString("owner")));
			}	
			
			return project;
			
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			daoHelper.closeResult(rs);
			daoHelper.closePreparedStatement(ps);
			daoHelper.closeConnection(con);
		}
		
		return null;
	}


	@Override
	public boolean updateProject(Project project) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		try{
			ps=con.prepareStatement("UPDATE `metadata`.`project` SET `id`=?,`name`=?,`description`=?,`language`=?,`checkouturl`=?,`sourcecodeurl`=?,`createdat`=?,`lastpushedat`=?,`isfork`=?,`hasdownloads`=?,`hasissues`=?,`haswiki`=?,`watcherscount`=?,`forkscount`=?,`issuescount`=?,`owner`=? where `id`=?");
			
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
			
			ps.setString(16, project.getUser().getName());
			ps.setInt(17, project.getId());
			
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
