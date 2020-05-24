package yeong.chatting.server.main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import yeong.chatting.util.CommonPathAddress;
import yeong.chatting.util.Log;


public class ServerMain extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(CommonPathAddress.ServerMainLayout));
		Parent root = (Parent)loader.load();
		Scene scene = new Scene(root);

		getTime(primaryStage);

		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	private void getTime(Stage stage) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Thread t = new Thread() {
			@Override
			public void run() {

				while(true) {

					String s = sdf.format(Calendar.getInstance().getTime());
					Platform.runLater( () -> {
						stage.setTitle("서버 프로그램 ( 현재 시간 : " + s +")");
					});
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		};
		t.setDaemon(true);
		t.start();

	}


	/**
	 * chatProject 실행
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
