package metadao.impl;

import helper.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entity.Issue;
import util.Dates;
import metadao.IssueDao;

public class IssueDaoImpl implements IssueDao{

	private static IssueDaoImpl issueDaoImpl=new IssueDaoImpl();
	private static DBHelper daoHelper=MetaDaoHelperImpl.getBaseDaoInstance();
	
	public static IssueDaoImpl getInstance(){
		return issueDaoImpl;
	}
	
	
	@Override
	public ArrayList<usefuldata.Issue> getAllIssues(int project_id) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		try{
			ps=con.prepareStatement("select * from metadata.issue where project_id =?");
			ps.setInt(1, project_id);
			
			rs=ps.executeQuery();
			
			ArrayList<usefuldata.Issue> result = new ArrayList<usefuldata.Issue>();
			while(rs.next()){
				usefuldata.Issue issue = new usefuldata.Issue();
				issue.setIssueId(rs.getInt("id"));
				issue.setInjectedDate(rs.getString("createdat"));
				issue.setState(rs.getString("state"));
				ArrayList<String> dates = new ArrayList<String>();
				dates.add(rs.getString("updatedat"));
				issue.setUpdateDate(dates);
				result.add(issue);
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
	public boolean addIssue(entity.Issue issue,int project_id) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		
		try{
			ps=con.prepareStatement("INSERT INTO `metadata`.`issue` (`id`,`number`,`comments`,`title`,`body`,`state`,`createdat`,`updatedat`,`closedat`,`project_id`) VALUES (?,?,?,?,?,?,?,?,?,?)");
			
			ps.setInt(1,issue.getId());
			ps.setInt(2,issue.getNumber());
			ps.setInt(3,issue.getCommentsCount());
			ps.setString(4,issue.getTitle());
			ps.setString(5,issue.getBody());
			ps.setString(6,issue.getState());
			ps.setString(7,Dates.dateToString(issue.getCreatedAt()) );
			ps.setString(8,Dates.dateToString(issue.getUpdatedAt()) );
			if(issue.getClosedAt()!=null){
				ps.setString(9,Dates.dateToString(issue.getClosedAt()) );
			}
			else{
				ps.setString(9,"");
			}
			ps.setInt(10, project_id);
			
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
	public boolean updateIssue(Issue issue,int project_id) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		try{
			ps=con.prepareStatement("UPDATE `metadata`.`issue` SET `id`=?,`number`=?,`comments`=?,`title`=?,`body`=?,`state`=?,`createdat`=?,`updatedat`=?,`closedat`=?,`project_id`=? where `id`=?");
			
			ps.setInt(1,issue.getId());
			ps.setInt(2,issue.getNumber());
			ps.setInt(3,issue.getCommentsCount());
			ps.setString(4,issue.getTitle());
			ps.setString(5,issue.getBody());
			ps.setString(6,issue.getState());
			ps.setString(7,Dates.dateToString(issue.getCreatedAt()) );
			ps.setString(8,Dates.dateToString(issue.getUpdatedAt()) );
			if(issue.getClosedAt()!=null){
				ps.setString(9,Dates.dateToString(issue.getClosedAt()) );
			}
			else{
				ps.setString(9,"");
			}
			ps.setInt(10, project_id);
			ps.setInt(11,issue.getId());
			
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
	public List<Issue> getAllMetaIssues(int project_id) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		try{
			ps=con.prepareStatement("select * from metadata.issue  where project_id =?");
			ps.setInt(1, project_id);
			
			rs=ps.executeQuery();
			
			ArrayList<entity.Issue> result = new ArrayList<entity.Issue>();
			
			while(rs.next()){
				entity.Issue issue = new entity.Issue();
				issue.setId(rs.getInt("id"));
				issue.setNumber(rs.getInt("number"));
				issue.setCommentsCount(rs.getInt("comments"));
				issue.setTitle(rs.getString("title"));
				issue.setBody(rs.getString("body"));
				issue.setState(rs.getString("state"));
				issue.setCreatedAt(Dates.stringToDate(rs.getString("createdat")));
				issue.setUpdatedAt(Dates.stringToDate(rs.getString("updatedat")));
				issue.setClosedAt(Dates.stringToDate(rs.getString("closedat")));
				result.add(issue);
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
	public boolean addIssues(List<Issue> issues, int project_id) {
		for(Issue i:issues){
			addIssue(i,project_id);
		}
		
		return true;
	}


	@Override
	public int IssueNum(int project_id, String start_time, String end_time) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		int num = 0;
		
		try{
			ps=con.prepareStatement("select * from metadata.issue  where project_id =? and createdat >=? and createdat <=?");
			ps.setInt(1, project_id);
			ps.setString(2, start_time);
			ps.setString(3, end_time);
			
			
			rs=ps.executeQuery();
			
			while(rs.next()){
				num += 1;
			}	
			
			return num;
			
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
	public int IssueNum(int project_id, String start_time) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		int num = 0;
		
		try{
			ps=con.prepareStatement("select * from metadata.issue  where project_id =? and createdat >=?");
			ps.setInt(1, project_id);
			ps.setString(2, start_time);
			
			rs=ps.executeQuery();
			
			while(rs.next()){
				num += 1;
			}	
			
			return num;
			
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
	public boolean CheckaddIssue(Issue issue, int project_id) {
		Issue is = getIssue(issue.getId(),project_id);
		if(is == null){
			return addIssue(issue,project_id);
		}else
			return updateIssue(issue,project_id);
		
	}


	@Override
	public boolean CheckaddIssues(List<Issue> issues, int project_id) {
		for(Issue i:issues){
			CheckaddIssue(i,project_id);
		}
		
		return true;
	}


	@Override
	public Issue getIssue(int issue_id, int project_id) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		try{
			ps=con.prepareStatement("select * from metadata.issue where id =? and project_id = ?");
			ps.setInt(1,issue_id);
			ps.setInt(2, project_id);
			
			rs=ps.executeQuery();
			Issue issue = null;
			
			if(rs.next()){
				issue = new Issue();
				issue.setId(rs.getInt("id"));
				issue.setNumber(rs.getInt("number"));
				issue.setCommentsCount(rs.getInt("comments"));
				issue.setTitle(rs.getString("title"));
				issue.setBody(rs.getString("body"));
				issue.setState(rs.getString("state"));
				issue.setCreatedAt(Dates.stringToDate(rs.getString("createdat")));
				issue.setUpdatedAt(Dates.stringToDate(rs.getString("updatedat")));
				issue.setClosedAt(Dates.stringToDate(rs.getString("closedat")));
				
			}	
			
			return issue;
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
