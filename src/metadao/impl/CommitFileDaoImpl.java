package metadao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import entity.CommitFile;
import metadao.CommitFileDao;
import metadao.MetaDaoHelper;

public class CommitFileDaoImpl implements CommitFileDao{

	private static CommitFileDaoImpl commitFileDaoImpl=new CommitFileDaoImpl();
	private static MetaDaoHelper daoHelper=MetaDaoHelperImpl.getBaseDaoInstance();
	
	public static CommitFileDaoImpl getInstance(){
		return commitFileDaoImpl;
	}
	
	@Override
	public boolean addCommitFile(CommitFile cmf) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		
		try{
			ps=con.prepareStatement("INSERT INTO `metadata`.`commit_files` (`file_sha`,`filename`,`status`,`additions`,`deletions`,`changes`,`contents_url`,`commit_sha`,`id`) VALUES (?,?,?,?,?,?,?,?,?)");
			
			ps.setString(1,cmf.getSha());
			ps.setString(2,cmf.getFilename());
			ps.setString(3, cmf.getStatus());
			ps.setInt(4, cmf.getAdditions());
			ps.setInt(5, cmf.getDeletions());
			ps.setInt(6, cmf.getChanges());
			ps.setString(7,cmf.getContents_url());
			ps.setString(8,cmf.getCommit_sha());
			ps.setInt(9, 0);
			
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
