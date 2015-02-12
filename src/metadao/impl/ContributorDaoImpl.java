package metadao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
	public boolean addContributor(Contributor contributor,int project_id) {
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
			ps.setInt(17,project_id);
			
			
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
	public List<Contributor> getAllContributors(int project_id) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		try{
			ps=con.prepareStatement("select * from metadata.contributor where project_id =?");
			ps.setInt(1,project_id);
			
			rs=ps.executeQuery();
			
			List<Contributor> contributors = new ArrayList<Contributor>();
			
			while(rs.next()){
				Contributor ctb = new Contributor();
				ctb.setId(rs.getInt("id"));
				ctb.setLogin(rs.getString("login"));
				ctb.setAvatar_url(rs.getString("avatar_url"));
				ctb.setGravatar_id(rs.getString("gravatar_id"));
				ctb.setHtml_url(rs.getString("html_url"));
				ctb.setFollowers_url(rs.getString("followers_url"));
				ctb.setFollowing_url(rs.getString("following_url"));
				ctb.setGists_url(rs.getString("gists_url"));
				ctb.setStarred_url(rs.getString("starred_url"));
				ctb.setSubscriptions_url(rs.getString("subscriptions_url"));
				ctb.setRepos_url(rs.getString("repos_url"));
				ctb.setEvents_url(rs.getString("events_url"));
				ctb.setReceived_events_url(rs.getString("received_events_url"));
				ctb.setType(rs.getString("type"));
				int admin = rs.getInt("site_admin");
				boolean site_admin = (admin == 1)?true:false;
				ctb.setSite_admin(site_admin);
				ctb.setContributions(rs.getInt("contributions"));
				contributors.add(ctb);
			}	
			
			return contributors;
			
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
