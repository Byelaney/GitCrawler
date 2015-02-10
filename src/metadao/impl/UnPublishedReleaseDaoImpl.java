package metadao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

}
