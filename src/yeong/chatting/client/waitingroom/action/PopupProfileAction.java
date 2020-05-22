package yeong.chatting.client.waitingroom.action;

import java.io.IOException;

import yeong.chatting.client.base.action.ActionInfo;
import yeong.chatting.client.base.action.CommonAction;
import yeong.chatting.client.util.ClientInfo;
import yeong.chatting.model.Member;
import yeong.chatting.model.Message;
import yeong.chatting.util.Log;
import yeong.chatting.util.ProtocolType;

public class PopupProfileAction implements CommonAction{
	@Override
	public void action(ActionInfo info) {
		String input = (String)info.getUserDatas()[0];
		Member to;
		Log.i(getClass(), input);
		if(input.equals("My")) {
			to = ClientInfo.currentMember;
		} else {
			to = (Member)info.getUserDatas()[1];
		}
		ProtocolType requestProtocol = ProtocolType.REQUEST_PROFILE;
		Message request = new Message(requestProtocol, ClientInfo.currentMember);
		request.setTo(to);
		
		try { oos.writeObject(request); } catch (IOException e) { e.printStackTrace(); }
		
	}
}
