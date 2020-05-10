package yeong.chatting.client.action;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import yeong.chatting.util.Log;

public class GoAction implements CommonAction {

	@Override
	public void formAction(Window stage, String URL) {
		Log.i(getClass(),"formAction(stage, url) »£√‚µ ");
		go(stage, URL);
	}
	
	private void go(Window stage, String URL) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(URL));
			Parent regiForm = (Parent)loader.load();
			Scene scene = new Scene(regiForm);
			Stage primaryStage = (Stage)stage;
			
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
			Log.e(getClass(), e);
		}
	}

}
