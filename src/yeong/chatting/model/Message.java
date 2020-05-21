package yeong.chatting.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
	
	private Member to;
	private String msg;
	private RoomInfo rInfo;
	
	private Vector<Member> roomMemberList; // Ư�� �� ���Ӹ��
	private Vector<RoomInfo> roomList; // ���� �� ����
	private Vector<Member> memberList; // ���� ���
	
	public Message(Message msg) {
		this.protocol = msg.protocol;
		this.from = msg.from;
		this.to = msg.to;
		this.msg = msg.msg;
		this.rInfo = msg.rInfo;
		this.roomList = msg.roomList;
		this.roomMemberList = msg.roomMemberList;
		this.memberList = msg.memberList;
	}
	
	public Message(ProtocolType protocol, Member from, Member to, String msg) {
		this.protocol = protocol;
		this.from = from;
		this.to = to;
		this.msg = msg;
	}
	
	
	public Message(ProtocolType protocol, String msg) {
		this.protocol = protocol;
		this.msg = msg;
	}
	
	public Message(ProtocolType protocol, Member from) {
		this.protocol = protocol;
		this.from = from;
	}
	
	public Message(ProtocolType protocol, Member from, RoomInfo rInfo) {
		this(protocol, from);
		this.rInfo = rInfo;
	}

	public Message(ProtocolType protocol, Member from, Vector<Member> memberList) {
		this(protocol, from);
		this.memberList = memberList;
	}
	
	
	

	public Vector<Member> getRoomMemberList() {
		return roomMemberList;
	}
	public void setRoomMemberList(Vector<Member> roomMemberList) {
		this.roomMemberList = roomMemberList;
	}
	public Vector<RoomInfo> getRoomList() {
		return roomList;
	}
	public void setRoomList(Vector<RoomInfo> roomList) {
		this.roomList = roomList;
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
	public Vector<Member> getMemberList() {
		return memberList;
	}
	public void setMemberList(Vector<Member> memberList) {
		this.memberList = memberList;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "Message [protocol=" + protocol + ", from=" + from + ", to=" + to + ", msg=" + msg + ", rInfo=" + rInfo
				+ ", roomMemberList=" + roomMemberList + ", roomList=" + roomList + ", memberList=" + memberList + "]";
	}

	
	
	
	
	
	
}
