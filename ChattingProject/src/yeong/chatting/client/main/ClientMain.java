package yeong.chatting.client.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import yeong.chatting.util.CommonPathAddress;
import yeong.chatting.util.Log;

public class ClientMain extends Application{
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(CommonPathAddress.LoginLayout));
		Parent root = (Parent)loader.load();
		Scene scene = new Scene(root);
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	/**
	 * chatProject ����
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
