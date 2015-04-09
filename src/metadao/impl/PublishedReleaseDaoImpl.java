package metadao.impl;

import helper.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import util.Dates;
import entity.Release;
import metadao.PublishedReleaseDao;

public class PublishedReleaseDaoImpl implements PublishedReleaseDao{

	private static PublishedReleaseDaoImpl publishedReleaseDaoImpl=new PublishedReleaseDaoImpl();
	private static DBHelper daoHelper=MetaDaoHelperImpl.getBaseDaoInstance();
	
	public static PublishedReleaseDaoImpl getInstance(){
		return publishedReleaseDaoImpl;
	}
	
	@Override
	public boolean addPublishedRelease(Release publishedRelease, int projectId) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		
		try{
			ps=con.prepareStatement("INSERT INTO `metadata`.`releases` (`id`,`tagname`,`targetcommitish`,`name`,`assetsurl`,`body`,`draft`,`prerelease`,`createdat`,`publishedat`,`project_id`) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
			
			ps.setInt(1,publishedRelease.getId());
			ps.setString(2,publishedRelease.getTagName());
			ps.setString(3,publishedRelease.getTargetCommitish());
			ps.setString(4,publishedRelease.getName());
			ps.setString(5,publishedRelease.getAssetsUrl());
			ps.setString(6,publishedRelease.getBody());
			ps.setInt(7,publishedRelease.isDraft()?1:0);
			ps.setInt(8,publishedRelease.isPreRelease()?1:0);
			ps.setString(9,Dates.dateToString(publishedRelease.getCreatedAt()));
			ps.setString(10,Dates.dateToString(publishedRelease.getPublishedAt()));
			ps.setInt(11, projectId);
			
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
	public boolean addPublishedReleases(List<Release> publishedRelease,
			int projectId) {
		for(Release upsr:publishedRelease){
			addPublishedRelease(upsr,projectId);
		}

		return true;
	}

}
