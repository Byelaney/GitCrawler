package dao.impl;

import helper.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import usefuldata.Release;
import usefuldata.ReleaseContribution;
import dao.ReleaseContributionDao;
import factory.DaoFactory;

public class ReleaseContributionDaoImpl implements ReleaseContributionDao{

	private static ReleaseContributionDaoImpl releaseContributionDao=new ReleaseContributionDaoImpl();
	private static DBHelper daoHelper=DaoHelperImpl.getBaseDaoInstance();
	
	public static ReleaseContributionDaoImpl getInstance(){
		return releaseContributionDao;
	}
	
	@Override
	public ArrayList<ReleaseContribution> getReleaseContribution(
			int project_id, int developer_id) {
		
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		try{
			ps=con.prepareStatement("select * from gitcrawler.release_contribution where project_id =? and developer_id=?");
			ps.setInt(1, project_id);
			ps.setInt(2, developer_id);
			rs=ps.executeQuery();
			ReleaseContribution rcb = null;
			ArrayList<ReleaseContribution> results = new ArrayList<ReleaseContribution>();
			
			while(rs.next()){
				rcb = new ReleaseContribution();
				int release_id = rs.getInt("release_id");
				String releaseName = DaoFactory.getReleaseDao().getRelease(release_id).getName();
				rcb.setReleaseName(releaseName);
				rcb.setContributions(rs.getInt("contributions"));
				rcb.setRelease_id(release_id);
				rcb.setDeveloper_id(rs.getInt("developer_id"));
				rcb.setProject_id(rs.getInt("project_id"));
				
				results.add(rcb);
			}	
			
			List<Release> reles = DaoFactory.getReleaseDao().getAllRelease(project_id);
			ArrayList<ReleaseContribution> res = new ArrayList<ReleaseContribution>();
			for(Release r:reles){
				res.add(new ReleaseContribution(r.getName(),0));
			}
			
			for(int i = 0;i<results.size();i++){
				for(int j =0;j<res.size();j++){
					if(res.get(j).getReleaseName().equals(results.get(i).getReleaseName())){
						res.get(j).setContributions(results.get(i).getContributions());
						break;
					}
					
				}
				
			}
			
			return res;
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
	public int getReleaseContributions(int developer_id,
			int project_id, int release_id) {		
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		try{
			ps=con.prepareStatement("select * from gitcrawler.release_contribution where release_id =? and developer_id=? and project_id = ?");
			ps.setInt(1, release_id);
			ps.setInt(2, developer_id);
			ps.setInt(3, project_id);
			
			rs=ps.executeQuery();
			if(rs.next()){
				return rs.getInt("contributions");
			}	
			
			return 0;
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			daoHelper.closeResult(rs);
			daoHelper.closePreparedStatement(ps);
			daoHelper.closeConnection(con);
		}
		
		return 0;
	}

	@Override
	public boolean addReleaseContribution(ReleaseContribution rc) {
		ReleaseContribution sc = getContributions(rc.getDeveloper_id(), rc.getProject_id(), rc.getRelease_id());
		if(sc == null){
			Connection con=daoHelper.getConnection();
			PreparedStatement ps=null;
			
			try{
				ps=con.prepareStatement("INSERT INTO `gitcrawler`.`release_contribution` (`release_id`,`developer_id`,`project_id`,`contributions`,`releaseName`) VALUES (?,?,?,?,?)");
				ps.setInt(1,rc.getRelease_id());
				ps.setInt(2, rc.getDeveloper_id());
				ps.setInt(3, rc.getProject_id());
				ps.setInt(4, rc.getContributions());
				ps.setString(5, rc.getReleaseName());
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
		
		else
			return updateReleaseContribution(rc);
		
	}

	@Override
	public boolean addReleaseContributions(List<ReleaseContribution> rct) {
		for(int i = 0;i<rct.size();i++)
			addReleaseContribution(rct.get(i));
		return true;
	}

	@Override
	public ReleaseContribution getContributions(int developer_id,
			int project_id, int release_id) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		try{
			ps=con.prepareStatement("select * from gitcrawler.release_contribution where release_id =? and developer_id=? and project_id = ?");
			ps.setInt(1, release_id);
			ps.setInt(2, developer_id);
			ps.setInt(3, project_id);
			
			rs=ps.executeQuery();
			
			ReleaseContribution rcs = null;
			if(rs.next()){
				rcs = new ReleaseContribution();
				rcs.setDeveloper_id(rs.getInt("developer_id"));
				rcs.setProject_id(rs.getInt("project_id"));
				rcs.setRelease_id(rs.getInt("release_id"));
				rcs.setReleaseName(rs.getString("releaseName"));
				rcs.setContributions(rs.getInt("contributions"));
				
			}	
			
			return rcs;
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
	public boolean updateReleaseContribution(ReleaseContribution rc) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		try{
			ps=con.prepareStatement("UPDATE `gitcrawler`.`release_contribution` SET `release_id`=?,`developer_id`=?,`project_id`=?,`contributions`=?,`releaseName`=? where `release_id` =? and `developer_id`=? and `project_id` = ?");
						
			ps.setInt(1,rc.getRelease_id());
			ps.setInt(2,rc.getDeveloper_id());
			ps.setInt(3,rc.getProject_id());
			ps.setInt(4,rc.getContributions());
			ps.setString(5, rc.getReleaseName());
			
			ps.setInt(6,rc.getRelease_id());
			ps.setInt(7,rc.getDeveloper_id());
			ps.setInt(8,rc.getProject_id());
			
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
