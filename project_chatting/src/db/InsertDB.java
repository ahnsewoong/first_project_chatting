package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.StringTokenizer;

import db.ConnectDB;

public class InsertDB {

	private Connection con;
	private PreparedStatement pstmt = null;
	private StringTokenizer st;

	public InsertDB() throws ClassNotFoundException, SQLException {
		con = new ConnectDB().getConnection();
	}

	private String sql1 = "INSERT INTO chatLog(chat_log) VALUES(?)";
	private String sql2 = "INSERT INTO conLog(con_log) VALUES(?)";

	public void chat_insert(String chat_log) {
		try {
			pstmt = con.prepareStatement(sql1);
			st = new StringTokenizer(chat_log, "/");
			String str1 = st.nextToken();
			String str2 = st.nextToken();

			st = new StringTokenizer(str2, "@");
			String str3 = st.nextToken();
			String str4 = st.nextToken();
			if (str1.equals("Chatting")) {
				pstmt.setString(1, "[일반채팅]" + str3 + "  " + str4);
			} else {
				pstmt.setString(1, "[비밀채팅]" + str3 + "  " + str4);
			}

			pstmt.executeUpdate();
		} catch (SQLException e) {
		}
	}

	public void log_insert(String con_log) {
		try {
			pstmt = con.prepareStatement(sql2);
			pstmt.setString(1, con_log);
			pstmt.executeUpdate();
		} catch (SQLException e) {
		}

	}

}
