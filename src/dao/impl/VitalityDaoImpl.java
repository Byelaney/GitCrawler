package dao.impl;

import helper.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import usefuldata.Developer;
import usefuldata.Vitality;
import dao.VitalityDao;

public class VitalityDaoImpl implements VitalityDao{

	private static VitalityDaoImpl vitalityDaoImpl=new VitalityDaoImpl();
	private static DBHelper daoHelper=DaoHelperImpl.getBaseDaoInstance();
	
	public static VitalityDaoImpl getInstance(){
		return vitalityDaoImpl;
	}
	
	@Override
	public List<Vitality> getVitality(Developer developer) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		try{
			ps=con.prepareStatement("select * from gitcrawler.vitality where developer_id =?");
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
	public boolean updateVitality(Vitality vitality) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		try{
			ps=con.prepareStatement("UPDATE `gitcrawler`.`vitality` SET `id`=?, `date`=?,`vitality`=?,`developer_id`=? where `id`=? and `developer_id`=?");
			ps.setInt(1,vitality.getId());
			ps.setString(2,vitality.getDate());
			ps.setInt(3,vitality.getVitality());
			ps.setInt(4,vitality.getDeveloper_id());
			ps.setInt(5,vitality.getId());
			ps.setInt(6,vitality.getDeveloper_id());
			
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
	public boolean addVitality(Vitality vitality) {
		Vitality v = getVitalityById(vitality.getId());
		if(v != null){
			return updateVitality(vitality);
		}
		else{
			Connection con=daoHelper.getConnection();
			PreparedStatement ps=null;
			
			
			try{
				ps=con.prepareStatement("INSERT INTO `gitcrawler`.`vitality` (`id`,`date`,`vitality`,`developer_id`,`project_id`,`release_id`) VALUES (?,?,?,?,?,?)");
				ps.setInt(1,0);
				ps.setString(2,vitality.getDate());
				ps.setInt(3, vitality.getVitality());
				ps.setInt(4,vitality.getDeveloper_id());
				ps.setInt(5,vitality.getProject_id());
				ps.setInt(6,vitality.getRelease_id());
				
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
	public boolean deleteVitality(Vitality vitality) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		
		try{
			ps=con.prepareStatement("delete from gitcrawler.vitality where id=? and developer_id=?");
			ps.setInt(1,vitality.getId());
			ps.setInt(2, vitality.getDeveloper_id());
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
	public boolean deleteAllVitality(int id) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		
		try{
			ps=con.prepareStatement("delete from gitcrawler.vitality where developer_id=?");
			ps.setInt(1, id);
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
	public List<Vitality> getVitality(int developer_id) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		try{
			ps=con.prepareStatement("select * from gitcrawler.vitality where developer_id =?");
			ps.setInt(1, developer_id);
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
	public List<Vitality> getVitality(int project_id, int release_id,
			int developer_id) {
				
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		try{
			ps=con.prepareStatement("select * from gitcrawler.vitality where developer_id =? and project_id =? and release_id=?");
			ps.setInt(1, developer_id);
			ps.setInt(2, project_id);
			ps.setInt(3, release_id);
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
	public boolean addVitalities(List<Vitality> vs) {
		for(int i = 0;i<vs.size();i++)
			addVitality(vs.get(i));
		
		return true;
	}

	@Override
	public Vitality getVitalityById(int vitality_id) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		try{
			ps=con.prepareStatement("select * from gitcrawler.vitality where id=?");
			ps.setInt(1, vitality_id);
			rs=ps.executeQuery();
			
			Vitality v = null;
			
			if(rs.next()){
				v = new Vitality();
				v.setId(rs.getInt("id"));
				v.setDate(rs.getString("date"));
				v.setVitality(rs.getInt("vitality"));
						
			}	
			
			return v;
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
	public Vitality checkVitality(Vitality v) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		try{
			ps=con.prepareStatement("select * from gitcrawler.vitality where date=? and developer_id=? and project_id=? and release_id=?");
			ps.setString(1, v.getDate());
			ps.setInt(2, v.getDeveloper_id());
			ps.setInt(3, v.getProject_id());
			ps.setInt(4, v.getRelease_id());
			rs=ps.executeQuery();
			
			Vitality vs = null;
			
			if(rs.next()){
				vs = new Vitality();
				vs.setId(rs.getInt("id"));
				vs.setDate(rs.getString("date"));
				vs.setVitality(rs.getInt("vitality"));
				vs.setDeveloper_id(rs.getInt("developer_id"));
				vs.setProject_id(rs.getInt("project_id"));
				vs.setRelease_id(rs.getInt(rs.getInt("release_id")));
			}	
			
			return v;
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
