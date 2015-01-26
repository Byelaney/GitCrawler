package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public interface DaoHelper {
	
	
	public Connection getConnection();
	
	public void closeConnection(Connection con);
		
	public void closePreparedStatement(PreparedStatement stmt);
	
	public void closeResult(ResultSet result);
}
