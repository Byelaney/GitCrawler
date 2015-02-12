package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dao.DaoHelper;
import dao.ForceChartDao;

public class ForceChartDaoImpl implements ForceChartDao{

	private static ForceChartDaoImpl forceChartDao = new ForceChartDaoImpl();
	private static DaoHelper daoHelper=DaoHelperImpl.getBaseDaoInstance();
	
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

}
