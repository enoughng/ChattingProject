package yeong.chatting.client.profile.action;

import java.io.IOException;

import yeong.chatting.client.base.action.ActionInfo;
import yeong.chatting.client.base.action.CommonAction;
import yeong.chatting.client.util.ClientInfo;
import yeong.chatting.model.Member;
import yeong.chatting.model.Message;
import yeong.chatting.util.Log;
import yeong.chatting.util.ProtocolType;

public class AddFriendAction implements CommonAction{
	@Override
	public void action(ActionInfo info) {
		
		String id = (String)info.getUserDatas()[0];
		
		ProtocolType requestProtocol = ProtocolType.REQUEST_ADD_FRIEND_REQUEST;
		Member from = new Member(ClientInfo.currentMember);
		Member to = new Member(id,"");
		
		Message request = new Message(requestProtocol,from);
		request.setTo(to);
		
		try { oos.writeObject(request); } catch (IOException e) { e.printStackTrace(); }
		info.closeAction();
	}
}
