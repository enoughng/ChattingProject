package yeong.chatting.model;

import java.io.Serializable;

public class ChattingProfile implements Serializable{
	private String nickname;
	private String id;
	private String introduce;
	
	public ChattingProfile() {
		// TODO Auto-generated constructor stub
	}
	
	public ChattingProfile(String nickname, String id, String introduce) {
		this.nickname = nickname;
		this.id = id;
		this.introduce = introduce;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIntroduce() {
		return introduce;
	}
	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	@Override
	public String toString() {
		return "ChattingProfile [nickname=" + nickname + ", id=" + id + ", introduce=" + introduce + "]";
	}
	
	
	
}
