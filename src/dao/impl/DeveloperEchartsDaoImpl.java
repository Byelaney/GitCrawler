package dao.impl;

import helper.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import usefuldata.DeveloperEcharts;
import dao.DeveloperEchartsDao;

public class DeveloperEchartsDaoImpl implements DeveloperEchartsDao{

	private static DeveloperEchartsDaoImpl developerEchartsDao=new DeveloperEchartsDaoImpl();
	private static DBHelper daoHelper=DaoHelperImpl.getBaseDaoInstance();
	
	public static DeveloperEchartsDaoImpl getInstance(){
		return developerEchartsDao;
	}
	
	@Override
	public boolean addDeveloperEcharts(int project_id, int release_id,
			int developer_id, String json) {
		
		DeveloperEcharts dp = new DeveloperEcharts();
		dp.setDeveloper_id(developer_id);
		dp.setProject_id(project_id);
		dp.setRelease_id(release_id);
		dp.setJson_string(json);
		
		return addEcharts(dp);
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

	@Override
	public boolean addEcharts(DeveloperEcharts dpe) {	
		DeveloperEcharts dd = getDeveloperEchart(dpe.getProject_id(),dpe.getRelease_id(),dpe.getDeveloper_id());
		if(dd != null){
			return updateEcharts(dpe);
		}
		else{
			Connection con=daoHelper.getConnection();
			PreparedStatement ps=null;
			
			try{
				ps=con.prepareStatement("INSERT INTO `gitcrawler`.`developer_echarts` (`developer_id`,`project_id`,`release_id`,`json_string`) VALUES (?,?,?,?)");
				
				ps.setInt(1,dpe.getDeveloper_id());
				ps.setInt(2,dpe.getProject_id());
				ps.setInt(3,dpe.getRelease_id());
				ps.setString(4, dpe.getJson_string());
				
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
	public boolean addEcharts(List<DeveloperEcharts> des) {
		for(int i = 0;i<des.size();i++)
			addEcharts(des.get(i));
		
		return true;
	}

	@Override
	public boolean updateEcharts(DeveloperEcharts dpe) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		
		try{
			ps=con.prepareStatement("UPDATE `gitcrawler`.`developer_echarts` SET `developer_id`=?,`project_id`=?,`release_id`=?,`json_string`=? where `developer_id`=? and `project_id`=? and `release_id`=?");
			
			ps.setInt(1,dpe.getDeveloper_id());
			ps.setInt(2,dpe.getProject_id());
			ps.setInt(3,dpe.getRelease_id());
			ps.setString(4, dpe.getJson_string());
			
			ps.setInt(5,dpe.getDeveloper_id());
			ps.setInt(6,dpe.getProject_id());
			ps.setInt(7,dpe.getRelease_id());
			
			
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
	public DeveloperEcharts getDeveloperEchart(int project_id, int release_id,
			int developer_id) {
		
		String json = getDeveloperEcharts(project_id,release_id,developer_id);
		if(json != null){
			DeveloperEcharts de = new DeveloperEcharts();
			de.setDeveloper_id(developer_id);
			de.setProject_id(project_id);
			de.setRelease_id(release_id);
			de.setJson_string(json);
			return de;
		}
		
		return null;
	}
	
}
