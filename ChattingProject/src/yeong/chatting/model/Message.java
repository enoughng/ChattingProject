package yeong.chatting.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;

import yeong.chatting.util.ProtocolType;

/**
 * @FileName  : Message.java
 * @Project     : ChatProject
 * @Date         : 2020. 5. 8.
 * @�ۼ���      : Yeong
 * @�����̷� :
 * @���α׷� ���� : ChatProject���� ����ϴ� ���������̴�. (Builder ���� ���)
 */
public class Message implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2172814615582842171L;
	ProtocolType protocol;
	private Member from;
	
	//��������
	private Member to;
	private String msg;
	private RoomInfo rInfo;
	private ArrayList<Member> memberList;
	
	public Message(ProtocolType protocol, Member from) {
		this.protocol = protocol;
		this.from = from;
	}
	
	public ProtocolType getProtocol() {
		return protocol;
	}
	public void setProtocol(ProtocolType protocol) {
		this.protocol = protocol;
	}
	public Member getFrom() {
		return from;
	}
	public void setFrom(Member from) {
		this.from = from;
	}
	public Member getTo() {
		return to;
	}
	public void setTo(Member to) {
		this.to = to;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public RoomInfo getrInfo() {
		return rInfo;
	}
	public void setrInfo(RoomInfo rInfo) {
		this.rInfo = rInfo;
	}
	public ArrayList<Member> getMemberList() {
		return memberList;
	}
	public void setMemberList(ArrayList<Member> memberList) {
		this.memberList = memberList;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "Message [from=" + from + "]";
	}
	
	
	
	
}
