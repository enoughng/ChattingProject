package yeong.chatting.client.chattingroom.action;

import java.io.IOException;

import yeong.chatting.client.base.action.ActionInfo;
import yeong.chatting.client.base.action.CommonAction;
import yeong.chatting.client.util.ClientInfo;
import yeong.chatting.model.Message;
import yeong.chatting.util.ProtocolType;

public class FormInviteAction implements CommonAction{
	@Override
	public void action(ActionInfo info) {
		Message request = new Message(ProtocolType.REQUEST_INVITEUPDATE, ClientInfo.currentMember, ClientInfo.currentRoom);
		try {
			oos.writeObject(request);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
