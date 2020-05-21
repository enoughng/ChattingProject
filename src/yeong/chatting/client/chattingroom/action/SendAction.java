package yeong.chatting.client.chattingroom.action;

import java.io.IOException;

import javafx.scene.control.TextField;
import yeong.chatting.client.base.action.ActionInfo;
import yeong.chatting.client.base.action.CommonAction;
import yeong.chatting.client.util.ClientInfo;
import yeong.chatting.model.Message;
import yeong.chatting.util.Log;
import yeong.chatting.util.ProtocolType;

public class SendAction implements CommonAction{	
	@Override
	public void action(ActionInfo info) {
		
		ProtocolType requestProtocol = ProtocolType.REQUEST_SEND;
		
		TextField chat = (TextField)info.getCons()[0];
		String msg = chat.getText();
		if(msg.equals("")) return;
		Message request = new Message(requestProtocol, ClientInfo.currentMember, ClientInfo.currentRoom);
		request.setMsg(msg);
		try {
			oos.writeObject(request);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
}
