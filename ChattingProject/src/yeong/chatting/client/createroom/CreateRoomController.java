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
import yeong.chatting.client.action.GoAction;
import yeong.chatting.client.controller.BaseController;
import yeong.chatting.client.waitingroom.WaitingRoomController;
import yeong.chatting.util.CommonPathAddress;

public class CreateRoomController extends BaseController {
	@FXML TextField room_title;
	@FXML PasswordField room_password;
	@FXML CheckBox room_pwdchk;
	
	@FXML Button createBtn;
	@FXML Button cancelBtn;
	
	
	@FXML
	private void create() {
		ActionInfo createRoom = new ActionInfo("CreateRoom",createBtn,CommonPathAddress.ChattingRoomLayout);
		createRoom.setCons(room_title, room_password, room_pwdchk);
		action(createRoom);
		action(new ActionInfo("Go", createBtn, CommonPathAddress.ChattingRoomLayout));
	}
	
	@FXML
	private void cancel() {
		Stage stage = (Stage)cancelBtn.getScene().getWindow();
		stage.close();
		GoAction.WaitingRoomGo((Stage)cancelBtn.getScene().getWindow(), getClass().getResource(CommonPathAddress.WaitingRoomLayout));
	}
}
