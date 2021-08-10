package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB { 
	
	private Connection con; 
	
	public Connection getConnection() {
		return con;
	}

	public ConnectDB()
			throws ClassNotFoundException, SQLException {
		Class.forName("oracle.jdbc.driver.OracleDriver");
	    con=DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe","hr","1234");
	
	}
}
