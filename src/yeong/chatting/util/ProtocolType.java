package yeong.chatting.util;

public enum ProtocolType {
	/** Client Request */
	REQUEST_LOGIN,
	REQUEST_REGISTRY,
	REQUEST_IDCHECK,
	REQUEST_WAITINGROOM_MEMBER,
	REQUEST_LOGOUT,
	REQUEST_CREATEROOM,
	REQUEST_ENTERROOM,
	REQUEST_EXITROOM,
	REQUEST_UPDATEWAITINGROOM,
	REQUEST_UPDATECHATTINGROOM,
	REQUEST_SEND,
	REQUEST_WHISPER,
	
	REQUEST_INVITE,
	REQUEST_INVITEUPDATE,
	
	REQUEST_SEARCH_ID,
	REQUEST_SEARCH_PW,
	
	REQUEST_PROFILE,
	REQUEST_PROFILE_EDIT,
	
	REQUEST_DELETE_ACCOUNT,
	
	REQUEST_ADD_FRIEND_REQUEST,
	REQUEST_ADD_FRIEND_RESPONSE,
	
	REQUEST_REMOVE_FRIEND,
	
	/** Server Response*/
	RESPONSE_LOGOUT,
	RESPONSE_IDCHECK,
	RESPONSE_REGISTRY_SUCCESS,
	RESPONSE_REGISTRY_FAIL,
	RESPONSE_LOGIN_SUCCESS,
	RESPONSE_LOGIN_FAIL,
	RESPONSE_WAITINGROOM_MEMBER,
	RESPONSE_CREATEROOM,
	RESPONSE_CREATEROOM_FAIL,
	RESPONSE_ENTERROOM_SUCCESS,
	RESPONSE_ENTERROOM_FAIL,
	RESPONSE_EXITROOM,
	RESPONSE_EXITROOM_HOST,
	RESPONSE_WHISPER,
	
	RESPONSE_UPDATEWAITINGROOM,
	RESPONSE_UPDATECHATTINGROOM,
	RESPONSE_FORCEDEXIT,
	RESPONSE_SEND,
	RESPONSE_ENTERROOM_PASSWORD,
	
	RESPONSE_INVITE_REQUEST,
	RESPONSE_INVITEUPDATE,
	RESPONSE_INVITE_REJECT,
	
	RESPONSE_SEARCH_ID,
	RESPONSE_SEARCH_PW,
	
	RESPONSE_PROFILE,
	RESPONSE_PROFILE_EDIT,
	
	RESPONSE_DELETE_ACCOUNT,
	
	RESPONSE_ADD_FRIEND_REQUEST,
	RESPONSE_ADD_FRIEND_REQUEST_FAIL,
	RESPONSE_ADD_FRIEND_RESPONSE,
	RESPONSE_ADD_FRIEND_RESPONSE_ERROR,
	
	RESPONSE_REMOVE_FRIEND,
	
	RESPONSE_ENTER_EXIT_MESSAGE,
	
	UPDATE_FRIEND_LIST,
	CLOSE
}
