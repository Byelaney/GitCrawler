package helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import util.XMLHelper;

public abstract class DBHelper {
	
	private static DBHelper baseDao;
	private Connection connection = null;
	
	private String driver;
	private String url;
	private String user;
	private String password;
	
	public DBHelper(){
        XMLHelper xmlhelper = new XMLHelper();
        ArrayList<String> params = xmlhelper.DBInputXML("DBConfigure.xml");       
        driver = params.get(0);
        url = params.get(1);
        user = params.get(2);
        password = params.get(3);        
        try{
        	Class.forName(driver);
        	
        }catch(Exception e){
        	e.printStackTrace();
        }		
	}
	
	public static DBHelper getBaseDaoInstance(){
		return baseDao;
	}
	
	
	public Connection getConnection() {
		try{
			connection = DriverManager.getConnection(url, user, password);
		}catch(SQLException e){
			e.printStackTrace();
		}
		return connection;
	}
	
	public void closeConnection(Connection connection) {
		
		if(connection!=null){
		try{
			connection.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		}	
	}
	
	public void closePreparedStatement(PreparedStatement stmt) {
		
		if(stmt!=null){
			try{
				stmt.close();
			}catch(SQLException e){
				e.printStackTrace();
				
			}
			
		}	
	}
	
	public void closeResult(ResultSet result) {
		
		if(result!=null){
			try{
				result.close();
				
			}catch(SQLException e){
				e.printStackTrace();
			}
			
		}
		
	}
}
