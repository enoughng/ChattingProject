package yeong.chatting.client.createroom;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import yeong.chatting.client.action.ActionInfo;
import yeong.chatting.client.action.CommonAction;
import yeong.chatting.client.controller.BaseController;
import yeong.chatting.util.CommonPathAddress;

public class CreateRoomController extends BaseController {
	@FXML TextField room_title;
	@FXML PasswordField room_password;
	@FXML CheckBox room_pwdchk;
	
	@FXML Button createBtn;
	@FXML Button cancelBtn;
	
	
	@FXML
	private void create() {
		action(new ActionInfo("CreateRoom",createBtn,CommonPathAddress.ChattingRoomLayout));
		action(new ActionInfo("Go", createBtn, CommonPathAddress.ChattingRoomLayout));
	}
	
	@FXML
	private void cancel() {
		Stage stage = (Stage)cancelBtn.getScene().getWindow();
		stage.close();
		action(new ActionInfo("Form",cancelBtn,CommonPathAddress.WaitingRoomLayout));
	}
}
