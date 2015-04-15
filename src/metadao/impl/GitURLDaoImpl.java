package metadao.impl;

import dao.impl.DaoHelperImpl;
import metadao.GitURLDao;
import helper.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class GitURLDaoImpl implements GitURLDao {
	private static GitURLDaoImpl gitURLDao = new GitURLDaoImpl();
	private static DBHelper daoHelper=DaoHelperImpl.getBaseDaoInstance();
	
	public static GitURLDaoImpl getInstance(){
		return gitURLDao;
	}

	@Override
	public void addURL(String url, String state) {
		// TODO Auto-generated method stub
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		
		try{
			ps=con.prepareStatement("INSERT INTO `metadata`.`giturl` (`url`,`state`) VALUES (?,?)");
			ps.setString(1,url);
			ps.setString(2,state);
			ps.execute();			
			
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			daoHelper.closePreparedStatement(ps);
			daoHelper.closeConnection(con);
		}
		
	}

	@Override
	public void deleteCrawledURL(String state) {
		// TODO Auto-generated method stub
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		
		try{
			ps=con.prepareStatement("delete from metadata.giturl where state=?");
			ps.setString(1,state);
			ps.execute();			
			
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			daoHelper.closePreparedStatement(ps);
			daoHelper.closeConnection(con);
		}
	}

	@Override
	public boolean findURL(String URL) {
		// TODO Auto-generated method stub
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		try{
			ps=con.prepareStatement("select * from metadata.giturl where url =?");
			ps.setString(1, URL);
			rs=ps.executeQuery();
						
			if(rs.next()){
				return true;
			}	
			
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			daoHelper.closeResult(rs);
			daoHelper.closePreparedStatement(ps);
			daoHelper.closeConnection(con);
		}
		
		return false;
	}

	@Override
	public ArrayList<String> getURLNotCrawled(String state) {
		// TODO Auto-generated method stub
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		try{
			ps=con.prepareStatement("select * from metadata.giturl where state =?");
			ps.setString(1, state);
			rs=ps.executeQuery();
			ArrayList<String> urls = new ArrayList<String>();
						
			while(rs.next()){
				urls.add(rs.getString("url"));
			}	
			
			return urls;
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
	public void changeState(String url,String state) {
		// TODO Auto-generated method stub
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		
		try{
			ps=con.prepareStatement("update metadata.giturl set state=? where url=?");
			ps.setString(1, state);
			ps.setString(2, url);
			ps.executeUpdate();
						
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			daoHelper.closePreparedStatement(ps);
			daoHelper.closeConnection(con);
		}
	}

	@Override
	public String getState(String url) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		try{
			ps=con.prepareStatement("select * from metadata.giturl where url =?");
			ps.setString(1, url);
			rs=ps.executeQuery();
			
			String state = null;
			if(rs.next()){
				state = rs.getString("state");
			}	
			
			return state;
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

