package yeong.chatting.client.util.alert;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AlertFactory {
	public static MyAlert createAlert(AlertType type, String msg) {
		MyAlert alert = null;
		switch(type) {
			case WARNING: 
				alert = new WarningAlert(type, msg);
				break;
			case CONFIRMATION:
				alert = new ConfirmAlert(type, msg);
				break;
			case ERROR:
				alert = new ErrorAlert(type, msg);
				break;
		}
		return alert;
	}
}
