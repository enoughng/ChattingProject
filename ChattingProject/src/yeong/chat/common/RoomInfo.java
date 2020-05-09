package yeong.chat.common;

public class RoomInfo {
	private int room_num;
	private String room_title;
	private String room_pwd;
	private String room_host;
	
	public RoomInfo(int room_num, String room_title, String room_pwd, String room_host) {
		this.room_num = room_num;
		this.room_title = room_title;
		this.room_pwd = room_pwd;
		this.room_host = room_host;
	}
	public int getRoom_num() {
		return room_num;
	}
	public void setRoom_num(int room_num) {
		this.room_num = room_num;
	}
	public String getRoom_title() {
		return room_title;
	}
	public void setRoom_title(String room_title) {
		this.room_title = room_title;
	}
	public String getRoom_pwd() {
		return room_pwd;
	}
	public void setRoom_pwd(String room_pwd) {
		this.room_pwd = room_pwd;
	}
	public String getRoom_host() {
		return room_host;
	}
	public void setRoom_host(String room_host) {
		this.room_host = room_host;
	}
	
}
