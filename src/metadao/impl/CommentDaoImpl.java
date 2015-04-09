package metadao.impl;

import helper.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entity.Comment;
import metadao.CommentDao;

public class CommentDaoImpl implements CommentDao{

	private static CommentDaoImpl commentDao=new CommentDaoImpl();
	private static DBHelper daoHelper=MetaDaoHelperImpl.getBaseDaoInstance();
	
	public static CommentDaoImpl getInstance(){
		return commentDao;
	}
	
	
	@Override
	public boolean addComment(Comment comment, int project_id) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		
		try{
			ps=con.prepareStatement("INSERT INTO `metadata`.`comments` (`id`,`url`,`user`,`user_id`,`position`,`line`,`path`,`commit_id`,`created_at`,`updated_at`,`body`,`project_id`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");
			
			ps.setInt(1,comment.getId());
			ps.setString(2, comment.getUrl());
			ps.setString(3, comment.getUser());
			ps.setInt(4,comment.getUser_id());
			ps.setInt(5,comment.getPosition());
			ps.setInt(6,comment.getLine());
			ps.setString(7, comment.getPath());
			ps.setString(8, comment.getCommit_id());
			ps.setString(9, comment.getCreated_at());
			ps.setString(10, comment.getUpdated_at());
			ps.setString(11, comment.getBody());
			ps.setInt(12,project_id);
			
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
	public ArrayList<usefuldata.Comment> getUsefulComments(String projectName,int project_id) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		try{
			ps=con.prepareStatement("select * from metadata.comments where project_id =?");
			ps.setInt(1, project_id);
			
			rs=ps.executeQuery();
			
			ArrayList<usefuldata.Comment> result = new ArrayList<usefuldata.Comment>();
			while(rs.next()){
				usefuldata.Comment comment = new usefuldata.Comment();
				comment.setCommentId(rs.getInt("id"));
				comment.setCommentator(rs.getString("user"));
				comment.setContent(rs.getString("body"));
				comment.setCommitId(rs.getString("commit_id"));
				comment.setDate(rs.getString("created_at"));
				comment.setProjectName(projectName);
				
				result.add(comment);
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
	public boolean addComments(List<Comment> comments, int project_id) {
		for(Comment c:comments){
			addComment(c,project_id);
		}
		return true;
	}

	@Override
	public boolean CheckaddComments(List<Comment> comments, int project_id) {
		for(Comment c:comments){
			Comment as = getComment(c.getId(),project_id);
			if(as == null)
				addComment(c,project_id);
			else
				updateComment(c,project_id);
		}
		return true;
	}

	@Override
	public Comment getComment(int id, int project_id) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		try{
			ps=con.prepareStatement("select * from metadata.comments where id = ? and project_id =?");
			ps.setInt(1, id);
			ps.setInt(2, project_id);
			
			rs=ps.executeQuery();
			
			Comment result = null;
			if(rs.next()){
				result = new Comment();
				result.setId(rs.getInt("id"));
				result.setUrl(rs.getString("url"));
				result.setUser(rs.getString("user"));
				result.setUser_id(rs.getInt("user_id"));
				result.setPosition(rs.getInt("position"));
				result.setLine(rs.getInt("line"));
				result.setPath(rs.getString("path"));
				result.setCommit_id(rs.getString("commit_id"));
				result.setCreated_at(rs.getString("created_at"));
				result.setUpdated_at(rs.getString("updated_at"));
				result.setBody(rs.getString("body"));
				
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
	public boolean updateComment(Comment comment, int project_id) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		try{
			ps=con.prepareStatement("UPDATE `metadata`.`comment` SET `id`=?,`url`=?,`user`=?,`user_id`=?,`position`=?,`line`=?,`path`=?,`commit_id`=?,`created_at`=?,`updated_at`=?,`body`=?,`project_id`=? where `id` =? and `project_id`=? ");
			
			ps.setInt(1, comment.getId());
			ps.setString(2,comment.getURL());
			ps.setString(3, comment.getUser());
			ps.setInt(4, comment.getUser_id());
			ps.setInt(5, comment.getPosition());
			ps.setInt(6, comment.getLine());
			ps.setString(7, comment.getPath());
			
			ps.setString(8,comment.getCommit_id());
			ps.setString(9, comment.getCreated_at());
			ps.setString(10, comment.getUpdated_at());
			
			ps.setString(11, comment.getBody());
			ps.setInt(12, project_id);
			
			ps.setInt(13, comment.getId());
			ps.setInt(14, project_id);
			
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
