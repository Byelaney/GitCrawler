package metadao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import entity.User;
import metadao.MetaDaoHelper;
import metadao.UserDao;

public class UserDaoImpl implements UserDao{

	private static UserDaoImpl userDaoImpl=new UserDaoImpl();
	private static MetaDaoHelper daoHelper=MetaDaoHelperImpl.getBaseDaoInstance();
	
	public static UserDaoImpl getInstance(){
		return userDaoImpl;
	}
	
	@Override
	public boolean addUser(User user) {
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

}
