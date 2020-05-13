package yeong.chatting.client.createroom.action;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;
import yeong.chatting.client.action.ActionInfo;
import yeong.chatting.client.action.CommonAction;
import yeong.chatting.util.Log;

public class CreateRoomAction implements CommonAction {
	@Override
	public void action(ActionInfo info) {
		Log.i(getClass(),"createRoomAction ½ÇÇà");
		Stage primaryStage = info.getPrimaryStage();
		primaryStage.close();
	}
}
