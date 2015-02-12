package metadao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import util.Dates;
import entity.UnPublishedRelease;
import metadao.MetaDaoHelper;
import metadao.UnPublishedReleaseDao;

public class UnPublishedReleaseDaoImpl implements UnPublishedReleaseDao{
	
	private static UnPublishedReleaseDaoImpl unPublishedReleaseDaoImpl=new UnPublishedReleaseDaoImpl();
	private static MetaDaoHelper daoHelper=MetaDaoHelperImpl.getBaseDaoInstance();
	
	public static UnPublishedReleaseDaoImpl getInstance(){
		return unPublishedReleaseDaoImpl;
	}
	
	
	@Override
	public boolean addUnPublishedRelease(UnPublishedRelease unPublishedRelease,int projectId) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		
		try{
			ps=con.prepareStatement("INSERT INTO `metadata`.`unpublish_releases` (`id`,`name`,`zipball_url`,`tarball_url`,`commit_url`,`project_id`,`date`) VALUES (?,?,?,?,?,?,?)");
			
			ps.setInt(1,0);
			ps.setString(2,unPublishedRelease.getName());
			ps.setString(3,unPublishedRelease.getZipball_url());
			ps.setString(4,unPublishedRelease.getTarball_url());
			ps.setString(5,unPublishedRelease.getCommit_url());
			ps.setInt(6,projectId);
			ps.setString(7,unPublishedRelease.getDate());
			
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
	public UnPublishedRelease getUnPublishedRelease(String releaseName,
			int project_id) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		try{
			ps=con.prepareStatement("select * from metadata.unpublish_releases where name =? and project_id =?");
			ps.setString(1, releaseName);
			ps.setInt(2, project_id);
			
			rs=ps.executeQuery();
			
			UnPublishedRelease upr = null;
			if(rs.next()){
				upr = new UnPublishedRelease();
				upr.setName(rs.getString("name"));
				upr.setZipball_url(rs.getString("zipball_url"));
				upr.setTarball_url(rs.getString("tarball_url"));
				upr.setCommit_url(rs.getString("commit_url"));
				upr.setDate(rs.getString("date"));
				
			}	
			
			return upr;
			
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
	public List<UnPublishedRelease> getAllUnPublishedReleases(int project_id) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		try{
			ps=con.prepareStatement("select * from metadata.unpublish_releases where project_id =?");
			ps.setInt(1, project_id);
			
			rs=ps.executeQuery();
			
			List<UnPublishedRelease> results = new ArrayList<UnPublishedRelease>();
			while(rs.next()){
				UnPublishedRelease upr = new UnPublishedRelease();
				upr.setName(rs.getString("name"));
				upr.setZipball_url(rs.getString("zipball_url"));
				upr.setTarball_url(rs.getString("tarball_url"));
				upr.setCommit_url(rs.getString("commit_url"));
				upr.setDate(rs.getString("date"));
				
				results.add(upr);
			}	
			
			return Dates.unPublishedReleaseSort(results);
			
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
