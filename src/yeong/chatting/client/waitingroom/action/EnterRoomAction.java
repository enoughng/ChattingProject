package yeong.chatting.client.waitingroom.action;

import java.io.IOException;

import javafx.stage.Window;
import yeong.chatting.client.base.action.ActionInfo;
import yeong.chatting.client.base.action.CommonAction;
import yeong.chatting.client.util.ClientInfo;
import yeong.chatting.model.Member;
import yeong.chatting.model.Message;
import yeong.chatting.model.RoomInfo;
import yeong.chatting.util.Log;
import yeong.chatting.util.ProtocolType;

public class EnterRoomAction implements CommonAction {

	@Override
	public void action(ActionInfo info) {

		ProtocolType requestProtocol = ProtocolType.REQUEST_ENTERROOM;
		Member currentMember = new Member(ClientInfo.currentMember);
		
		RoomInfo selectedRoom = (RoomInfo)info.getUserDatas()[0];
		
		Message request = new Message(requestProtocol, currentMember);
		selectedRoom.setRoom_pwd("");
		request.setrInfo(selectedRoom);

		try {
			oos.writeObject(request);
		} catch (IOException e) {
			Log.e(getClass(), e);
		}
	}
}
