package yeong.chatting.client.registry.action;

import java.io.IOException;

import javafx.scene.control.Control;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import yeong.chatting.client.action.ActionInfo;
import yeong.chatting.client.action.CommonAction;
import yeong.chatting.model.Member;
import yeong.chatting.model.Message;
import yeong.chatting.util.Log;
import yeong.chatting.util.ProtocolType;

public class RegistryAction implements CommonAction {

	
	@Override
	public void action(ActionInfo info) {
		Control[] cons = info.getCons();
		
		TextField id = (TextField) cons[0];
		TextField nickname = (TextField) cons[1];
		PasswordField pw = (PasswordField) cons[2];
		PasswordField pwChk = (PasswordField) cons[3];
		TextField email = (TextField) cons[4];
		
		String strID = id.getText();
		String strNick = nickname.getText();
		String strPW = pw.getText();
		String strPWChk = pwChk.getText();
		String strEmail = email.getText();
		
		Member member = new Member(strID, strPW, strNick, strEmail);
		
		Message msg = new Message(ProtocolType.REQUEST_REGISTRY, member);
		
		
		try {
			oos.writeObject(msg);
		} catch (IOException e) {
			Log.e(getClass(), "회원등록 실패", e);
		}
	}
	
	
	

}
