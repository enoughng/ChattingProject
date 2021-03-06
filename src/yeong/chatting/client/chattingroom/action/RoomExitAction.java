package yeong.chatting.client.chattingroom.action;

import java.io.IOException;

import yeong.chatting.client.base.action.ActionInfo;
import yeong.chatting.client.base.action.CommonAction;
import yeong.chatting.client.util.ClientInfo;
import yeong.chatting.model.Member;
import yeong.chatting.model.Message;
import yeong.chatting.model.RoomInfo;
import yeong.chatting.util.Log;
import yeong.chatting.util.ProtocolType;

public class RoomExitAction implements CommonAction{

	@Override
	public void action(ActionInfo info) {
		
		ProtocolType requestProtocol = ProtocolType.REQUEST_EXITROOM;
		Member currentMember = new Member(ClientInfo.currentMember);
		RoomInfo rInfo = ClientInfo.currentRoom;
		Message request = new Message(requestProtocol,currentMember, rInfo);
		
		try {
			oos.writeObject(request);
		} catch (IOException e) {
			Log.e(getClass(), e);
		}
	}
}
