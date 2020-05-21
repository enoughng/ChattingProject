package yeong.chatting.model;

import java.io.Serializable;

import yeong.chatting.util.Log;

public class RoomInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8272759855914282389L;
	
	private int room_num;
	private String room_title;
	private String room_pwd;
	private String room_host;
	private int room_members;
	private boolean chk;
	
	public RoomInfo(RoomInfo info) {
		this.room_num = info.room_num;
		this.room_title = info.room_title;
		this.room_pwd = info.room_pwd;
		this.room_host = info.room_host;
		this.room_members = info.room_members;
	}



	public RoomInfo(int room_num, String room_title, String room_pwd, String room_host, boolean chk) {
		this.room_num = room_num;
		this.room_title = room_title;
		this.room_pwd = room_pwd;
		this.room_host = room_host;
		this.chk = chk;
	}



	public int getRoom_members() {
		return room_members;
	}
	public void setRoom_members(int room_members) {
		this.room_members = room_members;
	}
	
	public boolean isChk() {
		return chk;
	}

	public void setChk(boolean chk) {
		this.chk = chk;
	}

	public RoomInfo(String room_title, String room_pwd, String room_host) {
		this.room_title = room_title;
		this.room_pwd = room_pwd;
		this.room_host = room_host;
	}
	public RoomInfo(int room_num, String room_title, String room_pwd, String room_host) {
		this.room_num = room_num;
		this.room_title = room_title;
		this.room_pwd = room_pwd;
		this.room_host = room_host;
	}
	
	public RoomInfo(int room_num, String room_title, String room_pwd, String room_host, int room_members) {
		this(room_num, room_title, room_pwd, room_host);
		this.room_members = room_members;
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
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof RoomInfo) {
			RoomInfo tmp =(RoomInfo)obj;
			if(room_host.equals(tmp.room_host) && room_num == tmp.room_num) {
				return true;
			}
		}
		return false;
	}
	@Override
	public String toString() {
		return room_num + "¹ø¹æ " + room_title + " ";
	}
	
	
	
}
