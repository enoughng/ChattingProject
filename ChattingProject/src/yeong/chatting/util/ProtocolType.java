package yeong.chatting.util;

public enum ProtocolType {
	/** Client Request */
	REQUEST_LOGIN,
	
	/** Server Response*/
	LOGIN_SUCCESS,
	LOGIN_FAIL,
	LOGOUT,
	ENTER_ROOM,
	SEND_MESSAGE,
	SECRET_MESSAGE
}
