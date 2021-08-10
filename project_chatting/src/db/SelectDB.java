package db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class SelectDB {
	private Connection con;
	private PreparedStatement pstmt = null;
	private ArrayList<VO> arr_vo = new ArrayList<VO>();
	SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
	
	public ArrayList<VO> getArr_vo() {
		return arr_vo;
	}

	private ResultSet rs = null;
	
	public SelectDB() throws ClassNotFoundException, SQLException {
		con = new ConnectDB().getConnection();
	}
	
	private String sql1 = "SELECT * FROM chatLog ORDER BY d";
	private String sql2 = "SELECT * FROM conLog ORDER BY d";
	
	
	public ArrayList<VO> select_chat() throws SQLException {
		pstmt = con.prepareStatement(sql1);
		rs = pstmt.executeQuery();
		
		while(rs.next()) {
			String str = rs.getString("chat_log");
			Timestamp d =  rs.getTimestamp("d");
			VO vo = new VO(str,d);
			arr_vo.add(vo);
		}
		return arr_vo;
	
	}
	
	public ArrayList<VO> select_con() throws SQLException {
		pstmt = con.prepareStatement(sql2);
		rs = pstmt.executeQuery();
		
		while(rs.next()) {
			String str = rs.getString("con_log");
			Timestamp d =  rs.getTimestamp("d");
			VO vo = new VO(str,d);
			arr_vo.add(vo);
		}
		return arr_vo;
	
	}


}
