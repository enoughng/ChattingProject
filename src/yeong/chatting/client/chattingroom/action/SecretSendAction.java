package yeong.chatting.client.chattingroom.action;

import java.io.IOException;

import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import yeong.chatting.client.base.action.ActionInfo;
import yeong.chatting.client.base.action.CommonAction;
import yeong.chatting.client.util.ClientInfo;
import yeong.chatting.model.Member;
import yeong.chatting.model.Message;
import yeong.chatting.model.RoomInfo;
import yeong.chatting.util.Log;
import yeong.chatting.util.ProtocolType;

public class SecretSendAction implements CommonAction {	
	
	@Override
	public void action(ActionInfo info) {
		ComboBox<Member> box = (ComboBox<Member>)info.getCons()[0];
		TextField chat = (TextField)info.getCons()[1];
		ListView<Member> listView = (ListView<Member>)info.getCons()[2];
		
		ProtocolType requestProtocol  = ProtocolType.REQUEST_WHISPER;
		Member to = box.getSelectionModel().getSelectedItem();

		String msg = chat.getText();
		if(msg.equals("")) return;
		RoomInfo rinfo = ClientInfo.currentRoom;
		
		Message request = new Message(requestProtocol, new Member(ClientInfo.currentMember), to, msg);
		request.setrInfo(rinfo);
		try {
			oos.writeObject(request);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
