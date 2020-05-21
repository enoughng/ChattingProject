package yeong.chatting.client.createroom.action;

import java.io.IOException;

import yeong.chatting.client.base.action.ActionInfo;
import yeong.chatting.client.base.action.CommonAction;
import yeong.chatting.client.util.ClientInfo;
import yeong.chatting.model.Member;
import yeong.chatting.model.Message;
import yeong.chatting.util.Log;
import yeong.chatting.util.ProtocolType;

public class CancelAction implements CommonAction{
	@Override
	public void action(ActionInfo info) {
		
		ProtocolType requestProtocol = ProtocolType.REQUEST_WAITINGROOM_MEMBER;
		Member from = ClientInfo.currentMember;
		
		Message request = new Message(requestProtocol, from);
		
		try {
			oos.writeObject(request);
		} catch (IOException e) {
			Log.e(getClass(),e);
		}
		
		
	}
}
