package yeong.chatting.client.createroom;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import yeong.chatting.client.action.ActionInfo;
import yeong.chatting.client.controller.BaseController;
import yeong.chatting.client.util.ClientInfo;
import yeong.chatting.client.util.Place;
import yeong.chatting.util.CommonPathAddress;

public class CreateRoomController extends BaseController {
	@FXML TextField room_title;
	@FXML PasswordField room_password;
	@FXML CheckBox room_pwdchk;
	
	@FXML Button createBtn;
	@FXML Button cancelBtn;
	
	
	@FXML
	private void create() {
		ClientInfo.currentMember.setPlace(Place.ChattingRoom);
		ActionInfo createRoom = new ActionInfo("CreateRoom",createBtn,CommonPathAddress.ChattingRoomLayout);
		createRoom.setCons(room_title, room_password, room_pwdchk);
		action(createRoom);

	}
	
	@FXML
	private void cancel() {
		ClientInfo.currentMember.setPlace(Place.WaitingRoom);
		action(new ActionInfo("Cancel", cancelBtn, CommonPathAddress.WaitingRoomLayout));
	}
}
