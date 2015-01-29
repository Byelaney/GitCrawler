package metadao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import entity.Contributor;
import metadao.ContributorDao;
import metadao.MetaDaoHelper;

public class ContributorDaoImpl implements ContributorDao{

	private static ContributorDaoImpl contributorDao=new ContributorDaoImpl();
	private static MetaDaoHelper daoHelper=MetaDaoHelperImpl.getBaseDaoInstance();
	
	public static ContributorDaoImpl getInstance(){
		return contributorDao;
	}
	
	
	@Override
	public boolean addContributor(Contributor contributor) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		
		try{
			ps=con.prepareStatement("INSERT INTO `metadata`.`contributor` (`id`,`login`,`avatar_url`,`gravatar_id`,`html_url`,`followers_url`,`following_url`,`gists_url`,`starred_url`,`subscriptions_url`,`repos_url`,`events_url`,`received_events_url`,`type`,`site_admin`,`contributions`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			
			ps.setInt(1,contributor.getId());
			ps.setString(2,contributor.getLogin());
			ps.setString(3, contributor.getAvatar_url());
			ps.setString(4, contributor.getGravatar_id());
			ps.setString(5,contributor.getHtml_url());
			ps.setString(6,contributor.getFollowers_url());
			ps.setString(7, contributor.getFollowing_url());
			ps.setString(8, contributor.getGists_url());
			ps.setString(9, contributor.getStarred_url());
			ps.setString(10, contributor.getSubscriptions_url());
			ps.setString(11, contributor.getRepos_url());
			ps.setString(12, contributor.getEvents_url());
			ps.setString(13, contributor.getReceived_events_url());
			ps.setString(14, contributor.getType());
			ps.setInt(15,contributor.isSite_admin()?1:0);
			ps.setInt(16,contributor.getContributions());
			
			
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
