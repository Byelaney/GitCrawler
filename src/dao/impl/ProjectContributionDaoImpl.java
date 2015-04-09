package dao.impl;
import helper.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import usefuldata.Developer;
import usefuldata.ProjectContribution;
import dao.ProjectContributionDao;
import factory.DaoFactory;

public class ProjectContributionDaoImpl implements ProjectContributionDao{

	
	private static ProjectContributionDaoImpl projectContributionDaoImpl=new ProjectContributionDaoImpl();
	private static DBHelper daoHelper=DaoHelperImpl.getBaseDaoInstance();
	
	public static ProjectContributionDaoImpl getInstance(){
		return projectContributionDaoImpl;
	}
	
	
	@Override
	public boolean addProjectContribution(ProjectContribution projectContribution) {
		ProjectContribution pc = findProjectContribution(projectContribution.getProject_id(),projectContribution.getDeveloper_id());
		if(pc != null){
			return updateProjectContribution(projectContribution);
		}
		else{
			Connection con=daoHelper.getConnection();
			PreparedStatement ps=null;			
			try{
				ps=con.prepareStatement("INSERT INTO `gitcrawler`.`project_contribution` (`project_id`,`developer_id`,`contributions`) VALUES (?,?,?)");
				ps.setInt(1,projectContribution.getProject_id());
				ps.setInt(2, projectContribution.getDeveloper_id());
				ps.setInt(3, projectContribution.getContributions());
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
	public ProjectContribution findProjectContribution(int project_id,
			int developer_id) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		try{
			ps=con.prepareStatement("select * from gitcrawler.project_contribution where project_id =? and developer_id=?");
			ps.setInt(1, project_id);
			ps.setInt(2, developer_id);
			rs=ps.executeQuery();
			ProjectContribution pcb = null;
			if(rs.next()){
				pcb = new ProjectContribution();
				pcb.setProject_id(project_id);
				pcb.setDeveloper_id(developer_id);
				pcb.setContributions(rs.getInt("contributions"));
			}	
			
			return pcb;
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
	public boolean updateProjectContribution(ProjectContribution pct) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		try{
			ps=con.prepareStatement("UPDATE `gitcrawler`.`project_contribution` SET `project_id`=?, `developer_id`=?,`contributions`=? where `project_id`=? and `developer_id`=?");
			ps.setInt(1,pct.getProject_id());
			ps.setInt(2,pct.getDeveloper_id());
			ps.setInt(3,pct.getContributions());
			ps.setInt(4,pct.getProject_id());
			ps.setInt(5,pct.getDeveloper_id());
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
	public boolean deleteProjectContribution(ProjectContribution pct) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		
		try{
			ps=con.prepareStatement("delete from gitcrawler.project_contribution where project_id=? and developer_id=?");
			ps.setInt(1, pct.getProject_id());
			ps.setInt(2, pct.getDeveloper_id());
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
	public List<ProjectContribution> findProjectContribution(
			int developer_id,List<usefuldata.Project> projects) {
		
		List<ProjectContribution> results = new ArrayList<ProjectContribution>();	
		Developer dp = new Developer();
		dp.setId(developer_id);
				
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		try{
		
		for(int i = 0;i<projects.size();i++){
		
				ps=con.prepareStatement("select * from gitcrawler.project_contribution where project_id =? and developer_id=?");
				ps.setInt(1, projects.get(i).getId());
				ps.setInt(2, developer_id);
				rs=ps.executeQuery();
				
				while(rs.next()){
					ProjectContribution pcb = new ProjectContribution();
					pcb = new ProjectContribution();
					pcb.setProject_id(projects.get(i).getId());
					pcb.setDeveloper_id(developer_id);
					pcb.setContributions(rs.getInt("contributions"));
					pcb.setProjectName(projects.get(i).getName());
					results.add(pcb);
				}	
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			daoHelper.closeResult(rs);
			daoHelper.closePreparedStatement(ps);
			daoHelper.closeConnection(con);
		}
		
		
		return results;
	}

	@Override
	public int getProjectContributions(int developer_id, int project_id) {
		
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		try{
			ps=con.prepareStatement("select * from gitcrawler.project_contribution where project_id =? and developer_id=?");
			ps.setInt(1, project_id);
			ps.setInt(2, developer_id);
			
			rs=ps.executeQuery();
			if(rs.next()){
				return rs.getInt("contributions");
			}	
			
			return 0;
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			daoHelper.closeResult(rs);
			daoHelper.closePreparedStatement(ps);
			daoHelper.closeConnection(con);
		}
		
		return 0;
	}


	@Override
	public ArrayList<Integer> getAllProjectContributors(int project_id) {
		
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		ArrayList<Integer> developer_ids = new ArrayList<Integer>();
		
		try{
			ps=con.prepareStatement("select * from gitcrawler.project_contribution where project_id =?");
			ps.setInt(1, project_id);
			
			rs=ps.executeQuery();
			while(rs.next()){
				int id = rs.getInt("developer_id");
				developer_ids.add(id);
			}	
			
			return developer_ids;
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
	public boolean addProjectContributions(List<ProjectContribution> pct) {
		for(int i = 0;i<pct.size();i++)
			addProjectContribution(pct.get(i));
		
		return true;
	}


	@Override
	public List<ProjectContribution> findProjectContributionByProjectID(int project_id){
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		List<ProjectContribution> results = new ArrayList<ProjectContribution>();
		
		try{
			ps=con.prepareStatement("select * from gitcrawler.project_contribution where project_id=?");
			ps.setInt(1, project_id);
			rs=ps.executeQuery();
			ProjectContribution pcb = null;
			while(rs.next()){
				pcb = new ProjectContribution();
				pcb.setProject_id(project_id);
				pcb.setDeveloper_id(rs.getInt("developer_id"));
				pcb.setContributions(rs.getInt("contributions"));
				String projectName = DaoFactory.getProjectDao().getProjectById(rs.getInt("project_id")).getName();
				pcb.setProjectName(projectName);
				results.add(pcb);
			}
			System.out.println(project_id);
			System.out.println(results.size());
			return results;
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
	public List<ProjectContribution> findProjectContribution(int developer_id) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		List<ProjectContribution> results = new ArrayList<ProjectContribution>();
		
		try{
			ps=con.prepareStatement("select * from gitcrawler.project_contribution where developer_id=?");
			ps.setInt(1, developer_id);
			rs=ps.executeQuery();
			ProjectContribution pcb = null;
			while(rs.next()){
				pcb = new ProjectContribution();
				pcb.setProject_id(rs.getInt("project_id"));
				pcb.setDeveloper_id(developer_id);
				pcb.setContributions(rs.getInt("contributions"));
				String projectName = DaoFactory.getProjectDao().getProjectById(rs.getInt("project_id")).getName();
				pcb.setProjectName(projectName);
				results.add(pcb);
			}	
			
			return results;
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
