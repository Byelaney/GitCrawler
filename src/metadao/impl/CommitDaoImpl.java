package metadao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import util.Dates;
import entity.Commit;
import metadao.CommitDao;
import metadao.MetaDaoHelper;

public class CommitDaoImpl implements CommitDao{

	private static CommitDaoImpl commitDao=new CommitDaoImpl();
	private static MetaDaoHelper daoHelper=MetaDaoHelperImpl.getBaseDaoInstance();
	
	public static CommitDaoImpl getInstance(){
		return commitDao;
	}
	
	@Override
	public boolean addCommit(Commit commit,int project_id) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		
		try{
			ps=con.prepareStatement("INSERT INTO `metadata`.`commits` (`sha`,`message`,`commitdate`,`additionscount`,`deletionscount`,`project_id`) VALUES (?,?,?,?,?,?)");
			
			ps.setString(1,commit.getSha());
			ps.setString(2,commit.getMessage());
			ps.setString(3, Dates.metaDateFormat(commit.getCommitDate().toString()));
			ps.setInt(4, commit.getAdditionsCount());
			ps.setInt(5, commit.getDeletionsCount());
			ps.setInt(6, project_id);
			
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
	public boolean updateCommit(Commit commit) {
		// TODO Auto-generated method stub
		return false;
	}

}
