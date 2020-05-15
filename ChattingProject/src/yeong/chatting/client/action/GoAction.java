package yeong.chatting.client.action;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import yeong.chatting.util.CommandMap;
import yeong.chatting.util.CommonPathAddress;
import yeong.chatting.util.Log;

public class GoAction implements CommonAction {
	@Override
	public void action(ActionInfo info) {
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
	
	public static void go(Stage stage, URL url) {
		FXMLLoader loader;
		try {
			loader = new FXMLLoader(url);
			Parent regiForm = (Parent)loader.load();
			Scene scene = new Scene(regiForm);
			stage.setScene(scene);
			stage.show();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
