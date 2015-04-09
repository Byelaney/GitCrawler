package metadao.impl;

import helper.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entity.Contributor;
import entity.User;
import metadao.UserDao;

public class UserDaoImpl implements UserDao{

	private static UserDaoImpl userDaoImpl=new UserDaoImpl();
	private static DBHelper daoHelper=MetaDaoHelperImpl.getBaseDaoInstance();
	
	public static UserDaoImpl getInstance(){
		return userDaoImpl;
	}
	
	@Override
	public boolean addUser(User user) {
		User duplicate = getUser(user.getLogin());
		if(duplicate!=null)
			return updateUser(user);
		
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		
		try{
			ps=con.prepareStatement("INSERT INTO `metadata`.`user` (`id`,`name`,`login`,`email`,`company`,`location`,`blog`,`hireable`,`followers`,`following`,`publicrepos`,`publicgists`,`createdat`,`updatedat`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			
			ps.setInt(1,user.getId());
			ps.setString(2,user.getName());
			ps.setString(3, user.getLogin());
			ps.setString(4, user.getEmail());
			ps.setString(5,user.getCompany());
			ps.setString(6,user.getLocation());
			ps.setString(7, user.getBlog());
			ps.setInt(8, user.isHireable()?1:0);
			ps.setInt(9, user.getFollowers());
			ps.setInt(10, user.getFollowing());
			ps.setInt(11, user.getPublic_repos());
			ps.setInt(12, user.getPublic_gists());
			ps.setString(13, user.getCreated_at());
			ps.setString(14, user.getUpdatedAt());
			
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
	public boolean addUsers(List<User> users) {
		for(User u:users){
			addUser(u);
		}
		return true;
	}

	@Override
	public User getUser(String login) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		try{
			ps=con.prepareStatement("select * from metadata.user where login =?");
			ps.setString(1, login);
			
			rs=ps.executeQuery();
			
			User u = null;
			if(rs.next()){
				String name = rs.getString("name");
				String login_ = rs.getString("login");
				String email = rs.getString("email");
				String company = rs.getString("company");
				String location = rs.getString("location");
				String blog = rs.getString("blog");
				String createdat = rs.getString("createdat");
				String updatedat = rs.getString("updatedat");
				
				int id = rs.getInt("id");
				int hireable = rs.getInt("hireable");
				int followers = rs.getInt("followers");
				int following = rs.getInt("following");
				int publicrepos = rs.getInt("publicrepos");
				int publicgists = rs.getInt("publicgists");
				
				u = new User(login_,name);
				u.setBlog(blog);
				u.setEmail(email);
				u.setCompany(company);
				u.setLocation(location);
				u.setCreated_at(createdat);
				u.setUpdatedAt(updatedat);
				u.setId(id);
				u.setFollowers(followers);
				u.setFollowing(following);
				u.setPublic_gists(publicgists);
				u.setPublic_repos(publicrepos);
				u.setHireable((hireable==1));
			}	
			
			return u;
			
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
	public boolean updateUser(User user) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		try{
			ps=con.prepareStatement("UPDATE `metadata`.`user` SET `id`=?,`name`=?,`login`=?,`email`=?,`company`=?,`location`=?,`blog`=?,`hireable`=?,`followers`=?,`following`=?,`publicrepos`=?,`publicgists`=?,`createdat`=?,`updatedat`=? where `id`=?");
			
			ps.setInt(1,user.getId());
			ps.setString(2,user.getName());
			ps.setString(3, user.getLogin());
			ps.setString(4, user.getEmail());
			ps.setString(5,user.getCompany());
			ps.setString(6,user.getLocation());
			ps.setString(7, user.getBlog());
			ps.setInt(8, user.isHireable()?1:0);
			ps.setInt(9, user.getFollowers());
			ps.setInt(10, user.getFollowing());
			ps.setInt(11, user.getPublic_repos());
			ps.setInt(12, user.getPublic_gists());
			ps.setString(13, user.getCreated_at());
			ps.setString(14, user.getUpdatedAt());
			ps.setInt(15, user.getId());
			
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
	public boolean CheckaddUser(User user) {
		User u = getUser(user.getLogin());
		if(u == null)
			return addUser(user);
		else
			return updateUser(user);
		
	}

	@Override
	public boolean CheckaddUsers(List<User> users) {
		for(User u:users){
			CheckaddUser(u);
		}
		return true;
	}

	@Override
	public ArrayList<User> getAllUsers(List<Contributor> contributors) {
		ArrayList<User> users = new ArrayList<User>();
		for(Contributor c:contributors){
			User u = getUser(c.getLogin());
			if(u!=null)
				users.add(u);			
		}
		
		return users;
	}

}
