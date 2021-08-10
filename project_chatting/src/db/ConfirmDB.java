package db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ConfirmDB {

	private String sql1 = "CREATE TABLE chatLog(chat_log varchar2(100), d date default sysdate)";
	private String sql2 = " CREATE TABLE conLog(con_log varchar2(50), d date default sysdate)";
	private Connection con;
	private Statement pstmt = null;

	public ConfirmDB() throws ClassNotFoundException, SQLException {
		con = new ConnectDB().getConnection();
	}
	public int confirm() {

		try {
			pstmt = con.createStatement();
			pstmt.executeQuery(sql1);
			pstmt.executeQuery(sql2);
			return 1;
		} catch (SQLException e) {
			return 0;
		}
	
	}

}
