package metadao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public interface MetaDaoHelper {
	
	public Connection getConnection();
	
	public void closeConnection(Connection con);
		
	public void closePreparedStatement(PreparedStatement stmt);
	
	public void closeResult(ResultSet result);
	
}
