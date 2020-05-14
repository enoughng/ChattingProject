package yeong.chatting.client.util.alert;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class WarningAlert extends MyAlert {
	public WarningAlert(AlertType type, String msg) {
		Alert alert = new Alert(type,msg);
		alert.setHeaderText("°æ°í");
		alert.showAndWait();
	}
}
