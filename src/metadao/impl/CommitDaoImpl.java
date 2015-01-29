package metadao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
			ps.setString(3, commit.getCommitDate().toString());
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

}
