package metadao.impl;

import helper.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import util.Dates;
import entity.Commit;
import entity.User;
import metadao.CommitDao;

public class CommitDaoImpl implements CommitDao{

	private static CommitDaoImpl commitDao=new CommitDaoImpl();
	private static DBHelper daoHelper=MetaDaoHelperImpl.getBaseDaoInstance();
	
	public static CommitDaoImpl getInstance(){
		return commitDao;
	}
	
	@Override
	public boolean addCommit(Commit commit,int project_id) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		
		try{
			ps=con.prepareStatement("INSERT INTO `metadata`.`commits` (`sha`,`message`,`commitdate`,`additionscount`,`deletionscount`,`project_id`,`contributor_id`) VALUES (?,?,?,?,?,?,?)");
			
			ps.setString(1,commit.getSha());
			ps.setString(2,commit.getMessage());
			ps.setString(3, Dates.metaDateFormat(commit.getCommitDate().toString()));
			ps.setInt(4, commit.getAdditionsCount());
			ps.setInt(5, commit.getDeletionsCount());
			ps.setInt(6, project_id);
			ps.setInt(7, commit.getCommiter().getId());
			
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
	public List<Commit> getCommits(int projectId) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		ResultSet rs2=null;
		PreparedStatement ps2=null;
		try{
			ps=con.prepareStatement("select * from metadata.commits where project_id =?");
			ps.setInt(1, projectId);
			
			rs=ps.executeQuery();
			
			List<Commit> result = new ArrayList<Commit>();
			while(rs.next()){
				Commit commit = new Commit();
				commit.setSha(rs.getString("sha"));
				commit.setMessage(rs.getString("message"));
				commit.setCommitDate(rs.getString("commitdate"));;
				commit.setAdditionsCount(rs.getInt("additionscount"));
				commit.setDeletionsCount(rs.getInt("deletionscount"));
				
				User u = new User("");
				int id = rs.getInt("contributor_id");
				ps2=con.prepareStatement("select * from metadata.contributor where id =?");
				ps2.setInt(1, id);
				rs2=ps2.executeQuery();
				String login = "";
				if(rs2.next()){
					login = rs2.getString("login");
				}
				u.setId(id);
				u.setLogin(login);
				
				commit.setCommiter(u);
				
				result.add(commit);
			}	
			
			return result;
			
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
	public boolean updateCommit(Commit commit,int project_id) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		try{
			ps=con.prepareStatement("UPDATE `metadata`.`commits` SET `sha`=?,`message`=?,`commitdate`=?,`additionscount`=?,`deletionscount`=?,`project_id`=?,`contributor_id`=? where `sha`=? and `project_id`=? and `contributor_id`=?");
			
			ps.setString(1,commit.getSha());
			ps.setString(2,commit.getMessage());
			ps.setString(3, Dates.metaDateFormat(commit.getCommitDate().toString()));
			ps.setInt(4, commit.getAdditionsCount());
			ps.setInt(5, commit.getDeletionsCount());
			ps.setInt(6, project_id);
			ps.setInt(7, commit.getCommiter().getId());
			
			ps.setString(8,commit.getSha());
			ps.setInt(9, project_id);
			ps.setInt(10, commit.getCommiter().getId());
			
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
	public List<Commit> getCommits(int projectId, int contributor_id) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		try{
			ps=con.prepareStatement("select * from metadata.commits where project_id =? and contributor_id =?");
			ps.setInt(1, projectId);
			ps.setInt(2, contributor_id);
			
			rs=ps.executeQuery();
			
			List<Commit> result = new ArrayList<Commit>();
			while(rs.next()){
				Commit commit = new Commit();
				commit.setSha(rs.getString("sha"));
				commit.setMessage(rs.getString("message"));
				commit.setCommitDate(rs.getString("commitdate"));;
				commit.setAdditionsCount(rs.getInt("additionscount"));
				commit.setDeletionsCount(rs.getInt("deletionscount"));
				
				User u = new User("");
				u.setId(rs.getInt("contributor_id"));
				commit.setCommiter(u);
				
				result.add(commit);
			}	
			
			return result;
			
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
	public int Contributions(int projectId, int contributor_id) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		int contributions = 0;
		try{
			ps=con.prepareStatement("select * from metadata.commits where project_id =? and contributor_id =?");
			ps.setInt(1, projectId);
			ps.setInt(2, contributor_id);
			
			rs=ps.executeQuery();
			while(rs.next()){
				int add = rs.getInt("additionscount");
				int del = rs.getInt("deletionscount");
				
				contributions += add + del;
			}	
			
			return contributions;
			
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
	public int releaseCommits(int project_id, String start_time, String end_time) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		int commit_nums = 0;
		try{
			ps=con.prepareStatement("select * from metadata.commits where project_id =? and commitdate >=? and commitdate <=?");
			ps.setInt(1, project_id);
			ps.setString(2,start_time);
			ps.setString(3,end_time);
			
			rs=ps.executeQuery();
			while(rs.next()){
				commit_nums +=1;
			}	
			
			return commit_nums;
			
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
	public int releaseCommits(int project_id, String start_time) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		int commit_nums = 0;
		try{
			ps=con.prepareStatement("select * from metadata.commits where project_id =? and commitdate >=?");
			ps.setInt(1, project_id);
			ps.setString(2,start_time);
			
			rs=ps.executeQuery();
			while(rs.next()){
				commit_nums +=1;
			}	
			
			return commit_nums;
			
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
	public int CheckaddCommit(Commit commit, int project_id) {
		Commit c = getCommit(commit.getSha(),project_id);
		if(c == null){
			//not in the database yet
			addCommit(commit,project_id);
			return 1;
		}else
			return 0;
		
	}

	@Override
	public Commit getCommit(String sha, int projectId) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		ResultSet rs2=null;
		PreparedStatement ps2=null;
		try{
			ps=con.prepareStatement("select * from metadata.commits where sha = ? and project_id =?");
			ps.setInt(1, projectId);
			
			rs=ps.executeQuery();
			
			Commit result = null;
			if(rs.next()){
				result = new Commit();
				result.setSha(rs.getString("sha"));
				result.setMessage(rs.getString("message"));
				result.setCommitDate(rs.getString("commitdate"));;
				result.setAdditionsCount(rs.getInt("additionscount"));
				result.setDeletionsCount(rs.getInt("deletionscount"));
				
				User u = new User("");
				int id = rs.getInt("contributor_id");
				ps2=con.prepareStatement("select * from metadata.contributor where id =?");
				ps2.setInt(1, id);
				rs2=ps2.executeQuery();
				String login = "";
				if(rs2.next()){
					login = rs2.getString("login");
				}
				u.setId(id);
				u.setLogin(login);
				
				result.setCommiter(u);
				
			}	
			
			return result;
			
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



}
