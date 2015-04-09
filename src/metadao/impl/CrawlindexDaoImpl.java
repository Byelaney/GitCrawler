package metadao.impl;

import helper.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import entity.Crawlindex;
import metadao.CrawlindexDao;

public class CrawlindexDaoImpl implements CrawlindexDao{

	private static CrawlindexDaoImpl CrawlindexDao=new CrawlindexDaoImpl();
	private static DBHelper daoHelper=MetaDaoHelperImpl.getBaseDaoInstance();
	
	public static CrawlindexDaoImpl getInstance(){
		return CrawlindexDao;
	}
	
	@Override
	public Crawlindex getCrawlindex(int project_id) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		try{
			ps=con.prepareStatement("select * from metadata.crawl_index where project_id =?");
			ps.setInt(1, project_id);
			
			rs=ps.executeQuery();
			
			Crawlindex result = null;
			
			if(rs.next()){
				result = new Crawlindex();
				result.setProject_id(rs.getInt("project_id"));
				result.setCommit_page(rs.getInt("commit_page"));
				result.setComment_page(rs.getInt("comment_page"));
				result.setIssue_page(rs.getInt("issue_page"));
				result.setRelease_page(rs.getInt("release_page"));
				result.setUpbrelease_page(rs.getInt("upbrelease_page"));
				result.setContributor_page(rs.getInt("contributor_page"));
				result.setMilestone_page(rs.getInt("milestone_page"));
				result.setPullrequest_page(rs.getInt("pullrequest_page"));
				result.setUser_page(rs.getInt("user_page"));
				result.setRelease_idx(rs.getInt("release_idx"));
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
	public boolean addCrawlindex(Crawlindex crawlindex) {
		Crawlindex duplicate = getCrawlindex(crawlindex.getProject_id());
		if(duplicate!=null)
			return updateContributor(duplicate);
				
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		
		try{
			ps=con.prepareStatement("INSERT INTO `metadata`.`crawl_index` (`project_id`,`commit_page`,`comment_page`,`issue_page`,`release_page`,`upbrelease_page`,`contributor_page`,`milestone_page`,`pullrequest_page`,`user_page`,`release_idx`) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
			
			ps.setInt(1, crawlindex.getProject_id());
			ps.setInt(2, crawlindex.getCommit_page());
			ps.setInt(3, crawlindex.getComment_page());
			ps.setInt(4, crawlindex.getIssue_page());
			ps.setInt(5, crawlindex.getRelease_page());
			ps.setInt(6, crawlindex.getUpbrelease_page());
			ps.setInt(7, crawlindex.getContributor_page());
			ps.setInt(8, crawlindex.getMilestone_page());
			ps.setInt(9, crawlindex.getPullrequest_page());
			ps.setInt(10, crawlindex.getUser_page());
			ps.setInt(11, crawlindex.getRelease_idx());
									
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
	public boolean updateContributor(Crawlindex crawlindex) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		try{
			ps=con.prepareStatement("UPDATE `metadata`.`crawl_index` SET `project_id`=?,`commit_page`=?,`comment_page`=?,`issue_page`=?,`release_page`=?,`upbrelease_page`=?,`contributor_page`=?,`milestone_page`=?,`pullrequest_page`=?,`user_page`=?,`release_idx`=? where `project_id`=?");
			
			ps.setInt(1, crawlindex.getProject_id());
			ps.setInt(2, crawlindex.getCommit_page());
			ps.setInt(3, crawlindex.getComment_page());
			ps.setInt(4, crawlindex.getIssue_page());
			ps.setInt(5, crawlindex.getRelease_page());
			ps.setInt(6, crawlindex.getUpbrelease_page());
			ps.setInt(7, crawlindex.getContributor_page());
			ps.setInt(8, crawlindex.getMilestone_page());
			ps.setInt(9, crawlindex.getPullrequest_page());
			ps.setInt(10, crawlindex.getUser_page());
			ps.setInt(11, crawlindex.getRelease_idx());
			
			ps.setInt(12, crawlindex.getProject_id());
			
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
