package dao.impl;

import helper.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import usefuldata.EvolveEcharts;
import dao.EvolveEchartsDao;

public class EvolveEchartsDaoImpl implements EvolveEchartsDao{

	private static EvolveEchartsDaoImpl evolveEchartsDaoImpl=new EvolveEchartsDaoImpl();
	private static DBHelper daoHelper=DaoHelperImpl.getBaseDaoInstance();
	
	public static EvolveEchartsDaoImpl getInstance(){
		return evolveEchartsDaoImpl;
	}
	
	@Override
	public boolean addEvolveEcharts(int project_id, int release_id, String json) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		
		try{
			ps=con.prepareStatement("INSERT INTO `gitcrawler`.`evolve_echarts` (`id`,`project_id`,`release_id`,`json_string`) VALUES (?,?,?,?)");
			
			ps.setInt(1,0);
			ps.setInt(2,project_id);
			ps.setInt(3,release_id);
			ps.setString(4, json);
			
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
	public boolean addEcharts(EvolveEcharts eve) {
		EvolveEcharts evs = getEvolveEcharts(eve.getProject_id(),eve.getRelease_id());
		if(evs !=null){
			return updateEcharts(eve); 
		}
		else{
			Connection con=daoHelper.getConnection();
			PreparedStatement ps=null;
			
			try{
				ps=con.prepareStatement("INSERT INTO `gitcrawler`.`evolve_echarts` (`id`,`project_id`,`release_id`,`json_string`) VALUES (?,?,?,?)");
				
				ps.setInt(1,0);
				ps.setInt(2,eve.getProject_id());
				ps.setInt(3,eve.getRelease_id());
				ps.setString(4, eve.getJson_string());
				
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
	public boolean addEcharts(List<EvolveEcharts> ev_charts) {
		for(int i = 0;i<ev_charts.size();i++)
			addEcharts(ev_charts.get(i));
		
		return true;
	}

	@Override
	public EvolveEcharts getEvolveEcharts(int project_id, int release_id) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		try{
			ps=con.prepareStatement("select * from gitcrawler.evolve_echarts where project_id =? and release_id =?");
			
			ps.setInt(1, project_id);
			ps.setInt(2,release_id);
						
			rs=ps.executeQuery();
			EvolveEcharts eve = null;
			if(rs.next()){
				eve = new EvolveEcharts();
				eve.setRelease_id(release_id);
				eve.setProject_id(project_id);
				eve.setJson_string(rs.getString("json_string"));
			}	
			
			return eve;
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
	public boolean updateEcharts(EvolveEcharts eve) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		
		try{
			ps=con.prepareStatement("UPDATE `gitcrawler`.`evolve_echarts` SET `project_id`=?,`release_id`=?,`json_string`=? where `project_id`=? and `release_id`=?");
			
			ps.setInt(1,eve.getProject_id());
			ps.setInt(2,eve.getRelease_id());
			ps.setString(3, eve.getJson_string());
			
			ps.setInt(4,eve.getProject_id());
			ps.setInt(5,eve.getRelease_id());
			
			
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
