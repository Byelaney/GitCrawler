package dao.impl;

import helper.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import usefuldata.ReleaseEcharts;
import dao.ReleaseEchartsDao;

public class ReleaseEchartsDaoImpl implements ReleaseEchartsDao{

	private static ReleaseEchartsDaoImpl releaseEchartsDao = new ReleaseEchartsDaoImpl();
	private static DBHelper daoHelper=DaoHelperImpl.getBaseDaoInstance();
	
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
	public boolean addReleaseEcharts(ReleaseEcharts re) {
		ReleaseEcharts r = getReleaseEchartPO(re.getProject_id(),re.getRelease_id());
		if(r != null){
			return updateReleaseEcharts(re);
		}
		else{
			Connection con=daoHelper.getConnection();
			PreparedStatement ps=null;
			
			try{
				ps=con.prepareStatement("INSERT INTO `gitcrawler`.`release_echarts` (`project_id`,`release_id`,`json_string`) VALUES (?,?,?)");
				ps.setInt(1,re.getProject_id());
				ps.setInt(2, re.getRelease_id());
				ps.setString(3, re.getJson_string());
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
	public ReleaseEcharts getReleaseEchartPO(int project_id, int release_id) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		try{
			ps=con.prepareStatement("select * from gitcrawler.release_echarts where project_id =? and release_id =?");
			ps.setInt(1, project_id);
			ps.setInt(2, release_id);
			
			rs=ps.executeQuery();
			ReleaseEcharts re = null;
			if(rs.next()){
				re = new ReleaseEcharts(project_id,release_id,rs.getString("json_string"));
			}	
			
			return re;
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
	public boolean addReleaseEcharts(List<ReleaseEcharts> res) {
		for(int i = 0;i<res.size();i++)
			addReleaseEcharts(res.get(i));
		
		return true;
	}


	@Override
	public boolean updateReleaseEcharts(ReleaseEcharts re) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		try{
			ps=con.prepareStatement("UPDATE `gitcrawler`.`release_echarts` SET `project_id`=?,`release_id`=?,`json_string`=? where `project_id` =? and `release_id`=?");
						
			ps.setInt(1,re.getProject_id());
			ps.setInt(2,re.getRelease_id());
			ps.setString(3, re.getJson_string());
			
			ps.setInt(4,re.getProject_id());
			ps.setInt(5,re.getRelease_id());
			
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
