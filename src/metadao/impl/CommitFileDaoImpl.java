package metadao.impl;

import helper.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import entity.CommitFile;
import metadao.CommitFileDao;

public class CommitFileDaoImpl implements CommitFileDao{

	private static CommitFileDaoImpl commitFileDaoImpl=new CommitFileDaoImpl();
	private static DBHelper daoHelper=MetaDaoHelperImpl.getBaseDaoInstance();
	
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

	@Override
	public CommitFile getCommitFile(String commit_sha) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		try{
			ps=con.prepareStatement("select * from metadata.commit_files where commit_sha =?");
			ps.setString(1,commit_sha);
			
			rs=ps.executeQuery();
			
			CommitFile commitFile = null;
			if(rs.next()){
				commitFile = new CommitFile();
				commitFile.setSha(rs.getString("file_sha"));
				commitFile.setFilename(rs.getString("filename"));
				commitFile.setStatus(rs.getString("status"));
				commitFile.setAdditions(rs.getInt("additions"));
				commitFile.setDeletions(rs.getInt("deletions"));
				commitFile.setChanges(rs.getInt("changes"));
				commitFile.setContents_url(rs.getString("contents_url"));
				commitFile.setCommit_sha(rs.getString("commit_sha"));
			}	
			
			return commitFile;
			
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
	public boolean addCommitFiles(List<CommitFile> cmfs) {
		for(CommitFile cmf:cmfs){
			addCommitFile(cmf);
		}

		return true;
	}
	
}
