package dao.impl;

import helper.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import usefuldata.ForceEcharts;
import dao.ForceChartDao;

public class ForceChartDaoImpl implements ForceChartDao{

	private static ForceChartDaoImpl forceChartDao = new ForceChartDaoImpl();
	private static DBHelper daoHelper=DaoHelperImpl.getBaseDaoInstance();
	
	public static ForceChartDaoImpl getInstance(){
		return forceChartDao;
	}
	
	
	
	@Override
	public boolean addForceChart(int project_id, int release_id,
			String relation,String main_relation) {

		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		
		try{
			ps=con.prepareStatement("INSERT INTO `gitcrawler`.`force_chart` (`project_id`,`release_id`,`relation`,`main_relation`) VALUES (?,?,?,?)");
			ps.setInt(1,project_id);
			ps.setInt(2,release_id);
			ps.setString(3, relation);
			ps.setString(4, main_relation);
			
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
	public ArrayList<String> getForceChart(int project_id,
			int release_id) {

		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		try{
			ps=con.prepareStatement("select * from gitcrawler.force_chart where project_id=? and release_id=?");
			ps.setInt(1, project_id);
			ps.setInt(2, release_id);
			rs=ps.executeQuery();
			ArrayList<String> results = null;
			if(rs.next()){
				results = new ArrayList<String>();
				results.add(rs.getString("relation"));
				results.add(rs.getString("main_relation"));
				
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



	@Override
	public boolean addForceChart(ForceEcharts fc) {
		ForceEcharts fec = getForceChartPj(fc.getProject_id(),fc.getRelease_id());
		if(fec != null){
			return updateForceChart(fc);
		}
		else{
			Connection con=daoHelper.getConnection();
			PreparedStatement ps=null;
			
			try{
				ps=con.prepareStatement("INSERT INTO `gitcrawler`.`force_chart` (`project_id`,`release_id`,`relation`,`main_relation`) VALUES (?,?,?,?)");
				ps.setInt(1,fc.getProject_id());
				ps.setInt(2,fc.getRelease_id());
				ps.setString(3, fc.getRelation());
				ps.setString(4, fc.getMain_relation());
				
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
	public boolean addForceCharts(List<ForceEcharts> fe) {
		for(int i = 0;i<fe.size();i++)
			addForceChart(fe.get(i));
		
		return true;
	}



	@Override
	public ForceEcharts getForceChartPj(int project_id, int release_id) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		try{
			ps=con.prepareStatement("select * from gitcrawler.force_chart where project_id=? and release_id=?");
			ps.setInt(1, project_id);
			ps.setInt(2, release_id);
			rs=ps.executeQuery();
			
			ForceEcharts fe = null;
			if(rs.next()){
				fe = new ForceEcharts();
				fe.setProject_id(project_id);
				fe.setRelease_id(release_id);
				fe.setRelation(rs.getString("relation"));
				fe.setMain_relation(rs.getString("main_relation"));
								
			}	
			
			return fe;
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
	public boolean updateForceChart(ForceEcharts fc) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		
		try{
			ps=con.prepareStatement("UPDATE `gitcrawler`.`force_chart` SET `project_id`=?,`release_id`=?,`relation`=?,`main_relation`=? where `project_id`=? and `release_id`=?");
			
			ps.setInt(1,fc.getProject_id());
			ps.setInt(2,fc.getRelease_id());
			ps.setString(3, fc.getRelation());
			ps.setString(4, fc.getMain_relation());
			
			ps.setInt(5,fc.getProject_id());
			ps.setInt(6,fc.getRelease_id());			
			
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
