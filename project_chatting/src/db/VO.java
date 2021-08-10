package db;


import java.sql.Timestamp;

public class VO {
	private String str;
	private Timestamp time;
	
	public VO() {}
	public VO(String str ,Timestamp time) {
		this.str = str;
		this.time = time;
	}
	public String getStr() {
		return str;
	}
	public void setStr(String str) {
		this.str = str;
	}
	public Timestamp getTime() {
		return time;
	}
	public void setTime(Timestamp time) {
		this.time = time;
	}
	

}
