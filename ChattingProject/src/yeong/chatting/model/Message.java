package yeong.chatting.model;

import java.io.Serializable;

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
	//�ʼ�����
	ProtocolType protocol;
	private final Member from;
	
	//��������
	private Member to;
	private String msg;
	private RoomInfo rInfo;
	
	public ProtocolType getProtocol() {
		return protocol;
	}
	public Member getFrom() {
		return from;
	}
	public Member getTo() {
		return to;
	}
	public String getMsg() {
		return msg;
	}
	public RoomInfo getrInfo() {
		return rInfo;
	}
	public static class Builder {
		
		ProtocolType protocol;
		private final Member from;
		
		private Member to;
		private String msg;
		private RoomInfo rInfo;
		
		public Builder(ProtocolType protocol, Member from) {
			this.protocol = protocol;
			this.from = from;
		}
		
		public Builder to(Member to) {
			this.to = to;
			return this;
		}
		
		public Builder msg(String msg) {
			this.msg = msg;
			return this;
		}
		
		public Builder rInfo(RoomInfo rInfo) {
			this.rInfo = rInfo;
			return this;
		}
		
		public Message build() {
			return new Message(this);
		}
	}
	
	
	public Message(Builder mBuilder) {
		protocol = mBuilder.protocol;
		from = mBuilder.from;
		to = mBuilder.to;
		msg = mBuilder.msg;
		rInfo = mBuilder.rInfo;
	}
	
}
