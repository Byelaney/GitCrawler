package dao.impl;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import usefuldata.Developer;
import usefuldata.Project;
import usefuldata.Vitality;
import dao.DaoHelper;
import dao.DeveloperDao;

public class DeveloperDaoImpl implements DeveloperDao{

	private static DeveloperDaoImpl developerDao=new DeveloperDaoImpl();
	private static DaoHelper daoHelper=DaoHelperImpl.getBaseDaoInstance();
	
	public static DeveloperDaoImpl getInstance(){
		return developerDao;
	}
	
	
	@Override
	public Developer findDeveloper(String name) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		try{
			ps=con.prepareStatement("select * from developer where login =?");
			ps.setString(1,name);
			rs=ps.executeQuery();
			Developer developer = null;
			if(rs.next()){
				developer = new Developer();
				developer.setId(rs.getInt("id"));
				developer.setLogin(rs.getString("login"));
				developer.setEmail(rs.getString("email"));
				developer.setUrl(rs.getString("url"));
			}	
			
			return developer;
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
	public Developer findDeveloperWithVitality(Developer developer) {
		List<Vitality> a = findDeveloperForVitality(developer);
		if(a != null){
			developer.setVitalities(a);
			return developer;
		}
		
		return null;
	}
	
	@Override
	public List<Vitality> findDeveloperForVitality(Developer developer) {
				Connection con=daoHelper.getConnection();
				PreparedStatement ps=null;
				ResultSet rs=null;
				
				try{
					ps=con.prepareStatement("select * from vitality where developer_id =?");
					ps.setInt(1, developer.getId());
					rs=ps.executeQuery();
					
					ArrayList<Vitality> a1 = new ArrayList<Vitality>();
					
					while(rs.next()){
						Vitality v = new Vitality();
						v.setId(rs.getInt("id"));
						v.setDate(rs.getString("date"));
						v.setVitality(rs.getInt("vitality"));
						
						a1.add(v);			
					}	
					
					return a1;
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
	public List<usefuldata.Project> findDeveloperForProjects(Developer developer) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		ResultSet rs2=null;
		
		try{
			ps=con.prepareStatement("select * from project_contribution where developer_id =?");
			ps.setInt(1, developer.getId());
			rs=ps.executeQuery();
			
			ArrayList<Project> a1 = new ArrayList<Project>();
			
			while(rs.next()){
				int project_id = rs.getInt("project_id");
				ps = con.prepareStatement("select * from project where id = ?");
				ps.setInt(1, project_id);
				rs2 = ps.executeQuery();
				if(rs2.next()){
					Project p = new Project();
					p.setId(project_id);
					p.setName(rs2.getString("name"));
					p.setCodes(rs2.getInt("codes"));
					p.setFiles(rs2.getInt("files"));
					p.setOwner(rs2.getString("owner"));
					p.setDescription(rs2.getString("description"));
					a1.add(p);
				}		
			}	
			
			return a1;
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			daoHelper.closeResult(rs2);
			daoHelper.closeResult(rs);
			daoHelper.closePreparedStatement(ps);
			daoHelper.closeConnection(con);
		}
		
		return null;
	}
	
	

	@Override
	public boolean deleteDeveloper(String name) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		
		try{
			ps=con.prepareStatement("delete from developer where login=?");
			ps.setString(1, name);
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
	public boolean addDeveloper(Developer developer) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		
		try{
			ps=con.prepareStatement("INSERT INTO `gitcrawler`.`developer` (`id`,`login`,`email`,`url`) VALUES (?,?,?,?)");
			ps.setInt(1,developer.getId());
			ps.setString(2,developer.getLogin());
			ps.setString(3, developer.getEmail());
			ps.setString(4, developer.getUrl());
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
	public Developer findDeveloperForName(int id) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		try{
			ps=con.prepareStatement("select * from developer where id =?");
			ps.setInt(1,id);
			rs=ps.executeQuery();
			Developer developer = null;
			if(rs.next()){
				developer = new Developer();
				developer.setId(rs.getInt("id"));
				developer.setLogin(rs.getString("login"));
				developer.setEmail(rs.getString("email"));
				developer.setUrl(rs.getString("url"));
			}	
			
			return developer;
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
