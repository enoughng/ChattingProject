package yeong.chatting.client.search.action;

import java.io.IOException;

import javafx.scene.control.TextField;
import yeong.chatting.client.base.action.ActionInfo;
import yeong.chatting.client.base.action.CommonAction;
import yeong.chatting.model.Message;
import yeong.chatting.model.SearchValue;
import yeong.chatting.util.Log;
import yeong.chatting.util.ProtocolType;

public class IDSearchAction implements CommonAction{
	@Override
	public void action(ActionInfo info) {
		ProtocolType requestProtocol = ProtocolType.REQUEST_SEARCH_ID;
		
		TextField name = (TextField)info.getCons()[0];
		TextField email = (TextField)info.getCons()[1];
		
		String strName = name.getText();
		String strEmail = email.getText();
		
		if((strName.trim().length()==0) || (strEmail.trim().length()==0)) return;
		
		SearchValue sv = new SearchValue(strEmail, strName);
		
		Message request = new Message(requestProtocol);
		request.setSv(sv);
		
		try { oos.writeObject(request); } catch (IOException e) { Log.e(getClass(), e); }
		
	}
}
