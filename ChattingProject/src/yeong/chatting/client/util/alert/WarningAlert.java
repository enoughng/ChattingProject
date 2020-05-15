package yeong.chatting.client.util.alert;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class WarningAlert extends MyAlert {
	public WarningAlert(AlertType type, String msg) {
		Platform.runLater( () -> {
			Alert alert = new Alert(type,msg);
			alert.setHeaderText("���");
			alert.showAndWait();
		});
	} 
}
