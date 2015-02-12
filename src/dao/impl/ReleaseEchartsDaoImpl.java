package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dao.DaoHelper;
import dao.ReleaseEchartsDao;

public class ReleaseEchartsDaoImpl implements ReleaseEchartsDao{

	private static ReleaseEchartsDaoImpl releaseEchartsDao = new ReleaseEchartsDaoImpl();
	private static DaoHelper daoHelper=DaoHelperImpl.getBaseDaoInstance();
	
	public static ReleaseEchartsDaoImpl getInstance(){
		return releaseEchartsDao;
	}
	
	
	@Override
	public String getReleaseEcharts(int project_id, int release_id) {	
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		try{
			ps=con.prepareStatement("select * from gitcrawler.release_echarts where project_id =? and release_id =?");
			ps.setInt(1, project_id);
			ps.setInt(2, release_id);
			
			rs=ps.executeQuery();
			String json_string = null;
			if(rs.next()){
				json_string = rs.getString("json_string");
			}	
			
			return json_string;
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
	public boolean addReleaseEcharts(int project_id, int release_id,
			String json_string) {
		
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		
		try{
			ps=con.prepareStatement("INSERT INTO `gitcrawler`.`release_echarts` (`id`,`project_id`,`release_id`,`json_string`) VALUES (?,?,?,?)");
			ps.setInt(1,0);
			ps.setInt(2,project_id);
			ps.setInt(3, release_id);
			ps.setString(4, json_string);
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
