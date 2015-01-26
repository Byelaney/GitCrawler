package dao.impl;

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
import dao.DaoHelper;
import dao.DeveloperDao;
import dao.ReleaseDao;
import factory.DaoFactory;

public class ReleaseDaoImpl implements ReleaseDao{

	private static ReleaseDaoImpl releaseDaoImpl=new ReleaseDaoImpl();
	private static DaoHelper daoHelper=DaoHelperImpl.getBaseDaoInstance();
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
					ps=con.prepareStatement("select * from releases where name =? and project_id=?");
					ps.setString(1,releaseName);
					ps.setInt(2, projectId);
					rs=ps.executeQuery();
					Release release = null;
					if(rs.next()){
						release = new Release();
						release.setId(rs.getInt("id"));
						release.setName(rs.getString("name"));	
						release.setCodes(rs.getInt("codes"));
						release.setFiles(rs.getInt("files"));
						release.setDate(rs.getString("date"));
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
			ps=con.prepareStatement("select * from releases where name =? and project_id=?");
			ps.setString(1,releaseName);
			ps.setInt(2, projectId);
			rs=ps.executeQuery();
			int release_id;
			List<Developer> dvps = null;
			
			if(rs.next()){
				release_id = rs.getInt("id");
				ps2 = con.prepareStatement("select * from release_contribution where release_id =? and project_id=?");
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
			ps=con.prepareStatement("select * from release_contribution where release_id =?");
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
	public boolean updateRelease(int release_id,int developer_id,int project_id,int submits) {
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
			ps=con.prepareStatement("UPDATE `gitcrawler`.`releases` SET `id`=?, `name`=?,`codes`=?,`files`=?,`date`=? where `id`=?");
			ps.setInt(1,release.getId());
			ps.setString(2, release.getName());
			ps.setInt(3,release.getCodes());
			ps.setInt(4,release.getFiles());
			ps.setString(5, release.getDate());
			ps.setInt(6,release.getId());
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
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		
		try{
			ps=con.prepareStatement("INSERT INTO `gitcrawler`.`releases` (`id`, `name`,`codes`,`files`,`project_id`,`date`) VALUES (?,?,?,?,?,?)");
			ps.setInt(1,0);
			ps.setString(2,release.getName());
			ps.setInt(3,release.getCodes());
			ps.setInt(4,release.getFiles());
			ps.setInt(5,project_id);
			ps.setString(6, release.getDate());
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
	public boolean addRelease(int release_id,int developer_id,int project_id,int contributions) {
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
	public List<Release> getAllRelease(String projectName) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		try{
			int projectId = DaoFactory.getProjectDao().getProject(projectName).getId();
			con=daoHelper.getConnection();
			
			ps=con.prepareStatement("select * from releases where project_id=?");
			ps.setInt(1, projectId);
			rs=ps.executeQuery();
			
			List<Release> results = new ArrayList<Release>();
			
			while(rs.next()){
				Release release = null;
				release = new Release();
				release.setId(rs.getInt("id"));
				release.setName(rs.getString("name"));	
				release.setCodes(rs.getInt("codes"));
				release.setFiles(rs.getInt("files"));
				release.setDate(rs.getString("date"));
				
				results.add(release);
			}	
			
			return results;
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
	public Map<String, Integer> getReleaseCommitNum(String projectName) {
		List<Release> tmp_releases = getAllRelease(projectName);
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
			ps=con.prepareStatement("select * from releases where id=?");
			ps.setInt(1, release_id);
			rs=ps.executeQuery();
			Release release = null;
			if(rs.next()){
				release = new Release();
				release.setId(rs.getInt("id"));
				release.setName(rs.getString("name"));	
				release.setCodes(rs.getInt("codes"));
				release.setFiles(rs.getInt("files"));
				release.setDate(rs.getString("date"));
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

}
