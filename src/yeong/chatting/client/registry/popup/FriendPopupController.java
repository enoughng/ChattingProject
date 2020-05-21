package yeong.chatting.client.registry.popup;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import yeong.chatting.client.base.action.ActionInfo;
import yeong.chatting.client.base.controller.BaseController;

public class FriendPopupController extends BaseController{
	
	@FXML Button add;
	@FXML Button delete;
	@FXML Button show;
	
	@FXML
	private void addAction() {
		ActionInfo info = new ActionInfo("FriendPopupAdd",add);
		action(info);
	}
	@FXML
	private void deleteAction() {
		ActionInfo info = new ActionInfo("FriendPopupDelete",add);
		action(info);		
	}
	@FXML
	private void showAction() {
		ActionInfo info = new ActionInfo("FriendPopupShow",add);
		action(info);
	}
	
	
}
