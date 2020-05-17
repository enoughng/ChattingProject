package yeong.chatting.client.createroom.action;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;
import yeong.chatting.client.action.ActionInfo;
import yeong.chatting.client.action.CommonAction;
import yeong.chatting.client.util.ClientInfo;
import yeong.chatting.model.Message;
import yeong.chatting.model.RoomInfo;
import yeong.chatting.util.Log;
import yeong.chatting.util.ProtocolType;

public class CreateRoomAction implements CommonAction {
	@Override
	public void action(ActionInfo info) {
		
		
		Message request;
		
		TextField id = (TextField)info.getCons()[0];
		PasswordField pw = (PasswordField)info.getCons()[1];
		
		String strId = id.getText();
		String strPw = pw.getText();
		
		RoomInfo rInfo= new RoomInfo(strId,strPw,ClientInfo.currentMember.getName());
		
		
		
		request = new Message(ProtocolType.REQUEST_CREATEROOM, ClientInfo.currentMember);
		request.setrInfo(rInfo);
		
		try {
			oos.writeObject(request);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Stage primaryStage = info.getPrimaryStage();
		primaryStage.close();
	}
}
