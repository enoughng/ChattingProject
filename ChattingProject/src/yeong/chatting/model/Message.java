package yeong.chatting.model;

import java.io.Serializable;

import yeong.chatting.util.ProtocolType;

/**
 * @FileName  : Message.java
 * @Project     : ChatProject
 * @Date         : 2020. 5. 8.
 * @작성자      : Yeong
 * @변경이력 :
 * @프로그램 설명 : ChatProject에서 사용하는 프로토콜이다. (Builder 패턴 사용)
 */
public class Message implements Serializable{
	//필수인자
	ProtocolType protocol;
	private final Member from;
	
	//선택인자
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
