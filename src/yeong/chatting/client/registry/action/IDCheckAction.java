package yeong.chatting.client.registry.action;

import yeong.chatting.client.base.action.ActionInfo;
import yeong.chatting.client.base.action.CommonAction;
import yeong.chatting.model.Message;

import java.io.IOException;

import javafx.scene.control.TextField;
import yeong.chatting.util.Log;
import yeong.chatting.util.ProtocolType;

public class IDCheckAction implements CommonAction {
	@Override
	public void action(ActionInfo info) {
		
		TextField id = (TextField)info.getCons()[0];
		String strId = id.getText();
		ProtocolType requestProtocol = ProtocolType.REQUEST_IDCHECK;
		String msg = strId;
		
		Message request = new Message(requestProtocol, msg);
		
		try {
			oos.writeObject(request);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
