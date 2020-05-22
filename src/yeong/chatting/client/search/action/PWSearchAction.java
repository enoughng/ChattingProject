package yeong.chatting.client.search.action;

import java.io.IOException;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import yeong.chatting.client.base.action.ActionInfo;
import yeong.chatting.client.base.action.CommonAction;
import yeong.chatting.model.Message;
import yeong.chatting.model.SearchValue;
import yeong.chatting.util.Log;
import yeong.chatting.util.ProtocolType;

public class PWSearchAction implements CommonAction{
	@Override
	public void action(ActionInfo info) {

		ProtocolType requestProtocol = ProtocolType.REQUEST_SEARCH_PW;

		TextField name = (TextField)info.getCons()[0];
		TextField email = (TextField)info.getCons()[1];
		TextField id = (TextField)info.getCons()[2];

		String strName = name.getText();
		String strEmail = email.getText();
		String strId = id.getText();

		if((strName.trim().length()==0) || (strEmail.trim().length()==0) || (strId.trim().length()==0)) {
			Alert alert = new Alert(AlertType.ERROR, "값을 입력해주세요");
			alert.setTitle("");
			ButtonType bt1 = new ButtonType("확인");
			alert.getButtonTypes().setAll(bt1);
			alert.setHeaderText("에러");
			alert.showAndWait();
			return;
		}

		SearchValue sv = new SearchValue(strEmail, strName, strId);

		Message request = new Message(requestProtocol);
		request.setSv(sv);

		try { oos.writeObject(request); } catch (IOException e) { Log.e(getClass(), e); }
	}
}
