package dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dao.DaoHelper;

public class DaoHelperImpl implements DaoHelper{
	
	
	private static DaoHelperImpl baseDao=new DaoHelperImpl();
	
	private Connection connection = null;
	
	private String driver;
	private String url;
	private String user;
	private String password;
	
	private DaoHelperImpl(){
        
		
        driver = "com.mysql.jdbc.Driver";

        
        url = "jdbc:mysql://127.0.0.1:3306/GitCrawler";

        
        user = "root"; 

        password = "";
        
        try{
        	Class.forName(driver);
        	
        }catch(Exception e){
        	e.printStackTrace();
        }
        
		
	}
	
	public static DaoHelperImpl getBaseDaoInstance(){
		return baseDao;
	}
	
	
	public Connection getConnection() {
		try{
			connection = DriverManager.getConnection(url, user, password);
//			if(!connection.isClosed()) 
//	             System.out.println("Succeeded connecting to the Database!");
		}catch(SQLException e){
			e.printStackTrace();
		}
		return connection;
	}
	
	public void closeConnection(Connection connection) {
		
		if(connection!=null){
		try{
			connection.close();
//			if(connection.isClosed())
//				System.out.println("Succeeded closing the Connection!");
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
