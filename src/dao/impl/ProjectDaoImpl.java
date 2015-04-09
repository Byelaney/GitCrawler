package dao.impl;

import helper.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import usefuldata.Developer;
import usefuldata.Project;
import usefuldata.Release;
import util.Dates;
import dao.ProjectDao;
import factory.DaoFactory;

public class ProjectDaoImpl implements ProjectDao{

	private static ProjectDaoImpl projectDaoImpl=new ProjectDaoImpl();
	private static DBHelper daoHelper=DaoHelperImpl.getBaseDaoInstance();
	
	
	public static ProjectDaoImpl getInstance(){
		return projectDaoImpl;
	}
	
	@Override
	public Project getProject(String owner, String projectName) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		try{
			ps=con.prepareStatement("select * from gitcrawler.project where name = ? and owner =?");
			ps.setString(1,projectName);
			ps.setString(2, owner);
			rs=ps.executeQuery();
			Project project = null;
			if(rs.next()){
				project = new Project();
				project.setId(rs.getInt("id"));
				project.setName(rs.getString("name"));
				project.setCodes(rs.getInt("codes"));
				project.setOwner(rs.getString("owner"));
				project.setDescription(rs.getString("description"));
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
	public Project getProjectWithId(int project_id, String projectName) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		try{
			ps=con.prepareStatement("select * from gitcrawler.project where id = ? and name =?");
			ps.setInt(1, project_id);
			ps.setString(2,projectName);
			rs=ps.executeQuery();
			Project project = null;
			if(rs.next()){
				project = new Project();
				project.setId(rs.getInt("id"));
				project.setName(rs.getString("name"));
				project.setCodes(rs.getInt("codes"));
				project.setOwner(rs.getString("owner"));
				project.setDescription(rs.getString("description"));
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
	public void deleteProject(String owner, String projectName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean updateProject(Project project) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		
		try{
			ps=con.prepareStatement("UPDATE `gitcrawler`.`project` SET `id`=?,`name`=?,`codes`=?,`owner`=?,`description`=? where `id`=?");
			
			ps.setInt(1,project.getId());
			ps.setString(2,project.getName());
			ps.setInt(3, project.getCodes());
			ps.setString(4, project.getOwner());
			ps.setString(5, project.getDescription());
			
			ps.setInt(6,project.getId());			
			
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
	public boolean addProject(Project project) {
		Project pj = getProjectById(project.getId());
		if(pj != null){
			return updateProject(project);
		}
		else{
			Connection con=daoHelper.getConnection();
			PreparedStatement ps=null;
			
			try{
				ps=con.prepareStatement("INSERT INTO `gitcrawler`.`project` (`id`,`name`,`codes`,`owner`,`description`) VALUES (?,?,?,?,?)");
				ps.setInt(1,project.getId());
				ps.setString(2,project.getName());
				ps.setInt(3, project.getCodes());
				ps.setString(4, project.getOwner());
				ps.setString(5, project.getDescription());
				
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

	@Override
	public List<Developer> getAllDevelopers(int project_id) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		try{
			ps=con.prepareStatement("select * from gitcrawler.project_contribution where project_id=?");
			ps.setInt(1, project_id);
			rs=ps.executeQuery();
			
			List<Developer> developers = new ArrayList<Developer>();
			
			while(rs.next()){
				int developer_id = rs.getInt("developer_id");
				Developer dp = DaoFactory.getDeveloperDao().findDeveloperForName(developer_id);
				if(dp!=null)
					developers.add(dp);
			}	
			
			return developers;
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
	public List<Release> getAllReleases(String projectName) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		try{
			int projectId = getProject(projectName).getId();
			con=daoHelper.getConnection();
			
			ps=con.prepareStatement("select * from gitcrawler.releases where project_id=?");
			ps.setInt(1, projectId);
			rs=ps.executeQuery();
			
			List<Release> results = new ArrayList<Release>();
			
			while(rs.next()){
				Release release = null;
				release = new Release();
				release.setId(rs.getInt("id"));
				release.setName(rs.getString("name"));	
				release.setCodes(rs.getInt("codes"));
				release.setDate(rs.getString("date"));
				release.setRelease_commits(rs.getInt("release_commits"));
				
				results.add(release);
			}	
			
			return Dates.releaseSort(results);
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
			ps=con.prepareStatement("select * from gitcrawler.project where name =?");
			ps.setString(1,projectName);
			rs=ps.executeQuery();
			Project project = null;
			if(rs.next()){
				project = new Project();
				project.setId(rs.getInt("id"));
				project.setName(rs.getString("name"));
				project.setCodes(rs.getInt("codes"));
				project.setOwner(rs.getString("owner"));
				project.setDescription(rs.getString("description"));
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

		public Project getProjectById(int project_id) {
			Connection con=daoHelper.getConnection();
			PreparedStatement ps=null;
			ResultSet rs=null;
			
			try{
				ps=con.prepareStatement("select * from gitcrawler.project where id = ?");
				ps.setInt(1, project_id);
				rs=ps.executeQuery();
				Project project = null;
				if(rs.next()){
					project = new Project();
					project.setId(rs.getInt("id"));
					project.setName(rs.getString("name"));
					project.setCodes(rs.getInt("codes"));
					project.setOwner(rs.getString("owner"));
					project.setDescription(rs.getString("description"));
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
			public ArrayList<Project> getAllProjects() {
				Connection con=daoHelper.getConnection();
				PreparedStatement ps=null;
				ResultSet rs=null;
				ArrayList<Project> projects = new ArrayList<Project>();
				
				try{
					ps=con.prepareStatement("select * from gitcrawler.project");
					rs=ps.executeQuery();
					while(rs.next()){
						Project project = new Project();
						project.setId(rs.getInt("id"));
						project.setName(rs.getString("name"));
						project.setCodes(rs.getInt("codes"));
						project.setOwner(rs.getString("owner"));
						project.setDescription(rs.getString("description"));
						projects.add(project);
					}	
					
					return projects;
				}catch(SQLException e){
					e.printStackTrace();
				}finally{
					daoHelper.closeResult(rs);
					daoHelper.closePreparedStatement(ps);
					daoHelper.closeConnection(con);
				}
				
				return null;
			}
		}
	

	
