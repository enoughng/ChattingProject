package yeong.chatting.client.util.alert;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

public class ConfirmAlert extends MyAlert {
	public ConfirmAlert(AlertType type, String msg) {
		Platform.runLater(()->{
			Alert alert = new Alert(type,msg);
			alert.getButtonTypes().setAll(new ButtonType("확인"));
			alert.setHeaderText("확인");
			alert.show();
		});
	}
}
