package yeong.chatting.client.waitingroom.action;

import java.io.IOException;

import javafx.stage.Window;
import yeong.chatting.client.base.action.ActionInfo;
import yeong.chatting.client.base.action.CommonAction;
import yeong.chatting.client.base.action.GoAction;
import yeong.chatting.client.util.ClientInfo;
import yeong.chatting.model.Member;
import yeong.chatting.model.Message;
import yeong.chatting.util.Log;
import yeong.chatting.util.ProtocolType;

public class LogoutAction implements CommonAction{
	
	@Override
	public void action(ActionInfo info) {
		Message message = new Message(ProtocolType.REQUEST_LOGOUT, new Member(ClientInfo.currentMember));
		try {
			oos.writeObject(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
