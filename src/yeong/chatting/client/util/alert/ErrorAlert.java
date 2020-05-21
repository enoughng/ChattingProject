package yeong.chatting.client.util.alert;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ErrorAlert extends MyAlert  {
	public ErrorAlert(AlertType type, String msg) {
		Platform.runLater(()->{
			Alert alert = new Alert(type,msg);
			alert.setHeaderText("°æ°í");
			alert.showAndWait();
		});
	}
}
