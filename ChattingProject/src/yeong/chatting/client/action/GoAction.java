package yeong.chatting.client.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Callback;
import yeong.chatting.util.CommandMap;
import yeong.chatting.util.Log;

public class GoAction implements CommonAction {
	@Override
	public void action(ActionInfo info) {
		Log.i(getClass(),"GoAction »£√‚µ ");
		go(info);
	}
	
	private void go(ActionInfo info) {
		try {
			FXMLLoader loader = new FXMLLoader(info.getURL());
			Parent regiForm = (Parent)loader.load();
			Scene scene = new Scene(regiForm);
			Stage primaryStage = (Stage)info.getPrimaryStage();
			
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
			Log.e(getClass(), e);
		}
	}

}
