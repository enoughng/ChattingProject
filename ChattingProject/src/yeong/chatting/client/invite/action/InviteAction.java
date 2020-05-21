package yeong.chatting.client.invite.action;

import java.io.IOException;

import yeong.chatting.client.base.action.ActionInfo;
import yeong.chatting.client.base.action.CommonAction;
import yeong.chatting.client.util.ClientInfo;
import yeong.chatting.model.Member;
import yeong.chatting.model.Message;
import yeong.chatting.util.ProtocolType;

/**
 * 초대하기 ( currentMember, currentRoomInfo, to )
 * @author Yeong
 */
public class InviteAction implements CommonAction{
	@Override
	public void action(ActionInfo info) {
		
		ProtocolType requestProtocol = ProtocolType.REQUEST_INVITE;
		Member selectedMember = (Member)info.getUserDatas()[0];
		String selectedID= selectedMember.getId();
		
		Message request = new Message(requestProtocol, ClientInfo.currentMember);
		request.setTo(selectedID);
		request.setrInfo(ClientInfo.currentRoom);
		
		try { oos.writeObject(request); } catch (IOException e) {  e.printStackTrace(); }
	}
}
