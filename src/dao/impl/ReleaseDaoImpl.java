package dao.impl;

import helper.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import usefuldata.Developer;
import usefuldata.Release;
import util.Dates;
import dao.DeveloperDao;
import dao.ReleaseDao;
import factory.DaoFactory;

public class ReleaseDaoImpl implements ReleaseDao{

	private static ReleaseDaoImpl releaseDaoImpl=new ReleaseDaoImpl();
	private static DBHelper daoHelper=DaoHelperImpl.getBaseDaoInstance();
	private static DeveloperDao developerDao = DaoFactory.getDeveloperDao();
	
	public static ReleaseDaoImpl getInstance(){
		return releaseDaoImpl;
	}
	
	@Override
	public Release getRelease(int projectId, String releaseName) {
				Connection con=daoHelper.getConnection();
				PreparedStatement ps=null;
				ResultSet rs=null;
				
				try{
					ps=con.prepareStatement("select * from gitcrawler.releases where name =? and project_id=?");
					ps.setString(1,releaseName);
					ps.setInt(2, projectId);
					rs=ps.executeQuery();
					Release release = null;
					if(rs.next()){
						release = new Release();
						release.setId(rs.getInt("id"));
						release.setName(rs.getString("name"));	
						release.setCodes(rs.getInt("codes"));
						release.setProject_id(rs.getInt("project_id"));
						release.setDate(rs.getString("date"));
						release.setRelease_commits(rs.getInt("release_commits"));
						release.setDocument(rs.getInt("document"));
						release.setTest(rs.getInt("test"));
						release.setCommit_rate(rs.getDouble("commit_rate"));
						release.setIssue_number(rs.getInt("issue_number"));
						release.setComprehensive(rs.getInt("comprehensive"));
					}	
					
					return release;
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
	public List<Developer> getAllDeveloper(int projectId, String releaseName) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		PreparedStatement ps2=null;
		ResultSet rs=null;
		ResultSet rs2=null;
		try{
			ps=con.prepareStatement("select * from gitcrawler.releases where name =? and project_id=?");
			ps.setString(1,releaseName);
			ps.setInt(2, projectId);
			rs=ps.executeQuery();
			int release_id;
			List<Developer> dvps = null;
			
			if(rs.next()){
				release_id = rs.getInt("id");
				ps2 = con.prepareStatement("select * from gitcrawler.release_contribution where release_id =? and project_id=?");
				ps2.setInt(1, release_id);
				ps2.setInt(2, projectId);
				rs2 = ps2.executeQuery();
				dvps = new ArrayList<Developer>();
				
				while(rs2.next()){
					Developer d = developerDao.findDeveloperForName(rs2.getInt("developer_id"));
					dvps.add(d);
				}
				
			}	
			
			return dvps;
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			daoHelper.closeResult(rs2);
			daoHelper.closeResult(rs);
			daoHelper.closePreparedStatement(ps2);
			daoHelper.closePreparedStatement(ps);
			daoHelper.closeConnection(con);
			
		}
		
		return null;
	}
	
	
	@Override
	public HashMap<Integer, Integer> getContributions(int release_id) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		try{
			ps=con.prepareStatement("select * from gitcrawler.release_contribution where release_id =?");
			ps.setInt(1, release_id);
			rs=ps.executeQuery();
			HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();
			while(rs.next()){
				hm.put(rs.getInt("developer_id"),rs.getInt("contributions") );
			}	
			
			return hm;
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
	public void deleteRelease(String projectName, String releaseName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean updateReleaseContributon(int release_id,int developer_id,int project_id,int submits) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		try{
			ps=con.prepareStatement("UPDATE `gitcrawler`.`release_contribution` SET `release_id`=?, `developer_id`=?,`project_id`=?,`contributions`=? where `release_id`=? and `developer_id`=?");
			ps.setInt(1,release_id);
			ps.setInt(2,developer_id);
			ps.setInt(3,project_id);
			ps.setInt(4,submits);
			ps.setInt(5,release_id);
			ps.setInt(6,developer_id);
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
	public boolean updateReleaseInfo(Release release) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		try{
			ps=con.prepareStatement("UPDATE `gitcrawler`.`releases` SET `id`=?, `name`=?,`codes`=?,`project_id`=?,`date`=?,`release_commits`=?,`document`=?,`test`=?,`commit_rate`=?,`issue_number`=?,`comprehensive` where `id`=? and `project_id`=?");
			ps.setInt(1,release.getId());
			ps.setString(2, release.getName());
			ps.setInt(3,release.getCodes());
			ps.setInt(4, release.getProject_id());
			ps.setString(5, release.getDate());
			ps.setInt(6, release.getRelease_commits());
			ps.setInt(7, release.getDocument());
			ps.setInt(8, release.getTest());
			ps.setDouble(9, release.getCommit_rate());
			ps.setInt(10, release.getIssue_number());
			ps.setInt(11, release.getComprehensive());
			ps.setInt(12,release.getId());
			ps.setInt(13,release.getProject_id());
			
			
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
	public boolean addReleaseInfo(Release release, int project_id) {
		Release r = getRelease(project_id,release.getName());
		if(r != null){
			return updateReleaseInfo(release);
		}
		else{
			Connection con=daoHelper.getConnection();
			PreparedStatement ps=null;
			
			try{
				ps=con.prepareStatement("INSERT INTO `gitcrawler`.`releases` (`id`, `name`,`codes`,`project_id`,`date`,`release_commits`,`document`,`test`,`commit_rate`,`issue_number`,`comprehensive`) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
				
				ps.setInt(1,release.getId());
				ps.setString(2, release.getName());
				ps.setInt(3,release.getCodes());
				ps.setInt(4,release.getProject_id());
				ps.setString(5, release.getDate());
				ps.setInt(6, release.getRelease_commits());
				ps.setInt(7, release.getDocument());
				ps.setInt(8, release.getTest());
				ps.setDouble(9, release.getCommit_rate());
				ps.setInt(10, release.getIssue_number());
				ps.setInt(11, release.getComprehensive());
				
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
	public boolean addReleaseContribution(int release_id,int developer_id,int project_id,int contributions) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		
		try{
			ps=con.prepareStatement("INSERT INTO `gitcrawler`.`release_contribution` (`release_id`, `developer_id`,`project_id`,`contributions`) VALUES (?,?,?,?)");
			ps.setInt(1,release_id);
			ps.setInt(2,developer_id);
			ps.setInt(3,project_id);
			ps.setInt(4,contributions);
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
	public List<Release> getAllRelease(int projectId) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		try{
			
			ps=con.prepareStatement("select * from gitcrawler.releases where project_id=?");
			ps.setInt(1, projectId);
			rs=ps.executeQuery();
			
			List<Release> results = new ArrayList<Release>();
			
			while(rs.next()){
				Release release = null;
				release = new Release();
				release.setId(rs.getInt("id"));
				release.setName(rs.getString("name"));	
				release.setCodes(rs.getInt("codes"));
				release.setProject_id(rs.getInt("project_id"));
				release.setDate(rs.getString("date"));
				release.setRelease_commits(rs.getInt("release_commits"));
				release.setDocument(rs.getInt("document"));
				release.setTest(rs.getInt("test"));
				release.setCommit_rate(rs.getDouble("commit_rate"));
				release.setIssue_number(rs.getInt("issue_number"));
				release.setComprehensive(rs.getInt("comprehensive"));
				results.add(release);
			}	
			
			return Dates.releaseSort(results);
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			daoHelper.closeResult(rs);
			daoHelper.closePreparedStatement(ps);
			daoHelper.closeConnection(con);
		}
		
		return null;
	}

	
	/**
	 * this method also deals with the data
	 */
	public Map<String, Integer> getReleaseCommitNum(int projectId) {
		List<Release> tmp_releases = getAllRelease(projectId);
		Map<String, Integer> results = new HashMap<String, Integer>();
		
		Map<String, String> unSortedDates = new HashMap<String, String>();
		for(Release rel:tmp_releases){
			unSortedDates.put(rel.getName(), rel.getDate());
		}
		
		ArrayList<String> sorted_date = Dates.dateSort(unSortedDates);
		ArrayList<Integer> sorted_commit = new ArrayList<Integer>();
		
		for(int i = 0;i<sorted_date.size();i++){
			String tag_name = sorted_date.get(i);
			for(Release rel:tmp_releases){
				if(rel.getName().equals(tag_name)){
					sorted_commit.add(rel.getCodes());
					break;
				}
			}
		}
		
		for(int j =0;j<sorted_commit.size();j++){
			if(j == 0)
			results.put(sorted_date.get(j), sorted_commit.get(j));
			else
				results.put(sorted_date.get(j),Math.abs(sorted_commit.get(j) -sorted_commit.get(j-1)));
		}
		
		return results;
	}

	@Override
	public Release getRelease(int release_id) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		try{
			ps=con.prepareStatement("select * from gitcrawler.releases where id=?");
			ps.setInt(1, release_id);
			rs=ps.executeQuery();
			Release release = null;
			if(rs.next()){
				release = new Release();
				release.setId(rs.getInt("id"));
				release.setName(rs.getString("name"));	
				release.setCodes(rs.getInt("codes"));
				release.setProject_id(rs.getInt("project_id"));
				release.setDate(rs.getString("date"));
				release.setRelease_commits(rs.getInt("release_commits"));
				release.setDocument(rs.getInt("document"));
				release.setTest(rs.getInt("test"));
				release.setCommit_rate(rs.getDouble("commit_rate"));
				release.setIssue_number(rs.getInt("issue_number"));
				release.setComprehensive(rs.getInt("comprehensive"));
			}	
			
			return release;
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
	public boolean addRelease(Release r) {
		return addReleaseInfo(r,r.getProject_id());
	}

	@Override
	public boolean addReleases(List<Release> releases) {
		for(int i = 0;i<releases.size();i++)
			addRelease(releases.get(i));
		
		return true;
	}

//	@Override
//	public ArrayList<ReleaseContribution> getSortedContributions(int release_id) {
//		// TODO Auto-generated method stub
//		
//		return null;
//		
//	}

}
