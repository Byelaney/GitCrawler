package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dao.DaoHelper;
import dao.DeveloperEchartsDao;

public class DeveloperEchartsDaoImpl implements DeveloperEchartsDao{

	private static DeveloperEchartsDaoImpl developerEchartsDao=new DeveloperEchartsDaoImpl();
	private static DaoHelper daoHelper=DaoHelperImpl.getBaseDaoInstance();
	
	public static DeveloperEchartsDaoImpl getInstance(){
		return developerEchartsDao;
	}
	
	@Override
	public boolean addDeveloperEcharts(int project_id, int release_id,
			int developer_id, String json) {
		
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		
		try{
			ps=con.prepareStatement("INSERT INTO `gitcrawler`.`developer_echarts` (`id`,`developer_id`,`project_id`,`release_id`,`json_string`) VALUES (?,?,?,?,?)");
			
			ps.setInt(1,0);
			ps.setInt(2,developer_id);
			ps.setInt(3,project_id);
			ps.setInt(4,release_id);
			ps.setString(5, json);
			
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
	public String getDeveloperEcharts(int project_id, int release_id,
			int developer_id) {

		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		try{
			ps=con.prepareStatement("select * from gitcrawler.developer_echarts where developer_id=? and project_id=? and release_id=?");
			ps.setInt(1, developer_id);
			ps.setInt(2, project_id);
			ps.setInt(3, release_id);
			
			rs=ps.executeQuery();
			String json = null;
			if(rs.next()){
				json = rs.getString("json_string");
			}	
			
			return json;
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
