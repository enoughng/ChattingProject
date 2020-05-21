package yeong.chatting.client.createroom.action;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.Window;
import yeong.chatting.client.base.action.ActionInfo;
import yeong.chatting.client.base.action.CommonAction;
import yeong.chatting.client.base.action.GoAction;
import yeong.chatting.client.util.ClientInfo;
import yeong.chatting.model.Message;
import yeong.chatting.model.RoomInfo;
import yeong.chatting.util.CommonPathAddress;
import yeong.chatting.util.Log;
import yeong.chatting.util.ProtocolType;

public class CreateRoomAction implements CommonAction {
	@Override
	public void action(ActionInfo info) {
		
		Message request;
		
		TextField id = (TextField)info.getCons()[0];
		PasswordField pw = (PasswordField)info.getCons()[1];
		CheckBox chk = (CheckBox)info.getCons()[2];
		if(id.getText().trim().length()==0) {
			Alert alert = new Alert(AlertType.ERROR, "제목을 입력해주세요");
			alert.setTitle("에러");
			alert.showAndWait();
			return;
		}
		String strId = id.getText();
		String strPw = pw.getText();
		boolean bChk = chk.isSelected();
		if(bChk && strPw.trim().length()==0) {
			Alert alert = new Alert(AlertType.WARNING, "비밀번호를 입력해주세요");
			alert.setTitle("경고");
			alert.showAndWait();
			return;
		}
		RoomInfo rInfo= new RoomInfo(strId,strPw,ClientInfo.currentMember.getId());
		rInfo.setChk(bChk);
		
		request = new Message(ProtocolType.REQUEST_CREATEROOM, ClientInfo.currentMember);
		request.setrInfo(rInfo);
		
		try {
			oos.writeObject(request);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
