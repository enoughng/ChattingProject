package yeong.chatting.client.registry.action;

import java.io.IOException;
import java.util.Optional;

import javafx.scene.control.Control;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import yeong.chatting.client.base.action.ActionInfo;
import yeong.chatting.client.base.action.CommonAction;
import yeong.chatting.client.base.action.FormAction;
import yeong.chatting.client.util.alert.AlertFactory;
import yeong.chatting.client.util.alert.MyAlert;
import yeong.chatting.model.Member;
import yeong.chatting.model.Message;
import yeong.chatting.util.CommonPathAddress;
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
		TextField email1 = (TextField) cons[4];
		TextField email2 = (TextField) cons[5];

		String strID = validationCheck(id);
		String strNick = validationCheck(nickname);
		String strPW = validationCheck(pw);
		String strPWChk = validationCheck(pwChk);
		String strEmail1 = validationCheck(email1);
		String strEmail2 = validationCheck(email2);

		StringBuilder sb = new StringBuilder();
		sb.append(strEmail1);
		sb.append("@");
		sb.append(strEmail2);

		String strEmail = sb.toString();

		boolean validationCheck = (strID == null) || (strNick == null) || (strPW == null) || (strPWChk == null);
		if(validationCheck) {
			Alert alert = new Alert(AlertType.ERROR, "값을 비워두면 안됩니다.");
			ButtonType bt = new ButtonType("확인");			
			alert.getButtonTypes().setAll(bt);
			alert.showAndWait();
			
			return;
		}

		Member member = new Member(strID.toLowerCase(), strPW, strNick, strEmail);

		Message msg = new Message(ProtocolType.REQUEST_REGISTRY, member);

		try {
			oos.writeObject(msg);
		} catch (IOException e) {
			Log.e(getClass(), "회원등록 실패", e);
		}
		
		info.closeAction();
	}

	private String validationCheck(TextInputControl con) {
		String input = con.getText();
		if(input.trim().length() == 0) {
			return null;
		}
		return input;
	}
}
