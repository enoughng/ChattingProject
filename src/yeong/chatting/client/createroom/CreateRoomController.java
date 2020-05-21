package yeong.chatting.client.createroom;

import java.net.URL;
import java.util.ResourceBundle;

import javax.jws.soap.InitParam;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import oracle.jdbc.proxy.annotation.SetDelegate;
import yeong.chatting.client.base.action.ActionInfo;
import yeong.chatting.client.base.controller.BaseController;
import yeong.chatting.client.util.ClientInfo;
import yeong.chatting.client.util.Place;
import yeong.chatting.util.CommonPathAddress;
import yeong.chatting.util.Log;

public class CreateRoomController extends BaseController {
	
	@FXML TextField room_title;
	@FXML PasswordField room_password;
	@FXML CheckBox room_pwdchk;
	
	@FXML Button createBtn;
	@FXML Button cancelBtn;
	
	Stage stage;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		
		initCheckButtonEvent();
	}
	
	@FXML
	private void create() {
		ClientInfo.currentMember.setPlace(Place.ChattingRoom);
		ActionInfo createRoom = new ActionInfo("CreateRoom",createBtn,CommonPathAddress.ChattingRoomLayout);
		createRoom.setCons(room_title, room_password, room_pwdchk);
		action(createRoom);

	}
	
	@FXML
	private void cancel() {
		Log.i("cancel()");
		ClientInfo.currentMember.setPlace(Place.WaitingRoom);
		action(new ActionInfo("Cancel", cancelBtn, CommonPathAddress.WaitingRoomLayout));
	}
	
	/*
	 * @Override public void setStage(Stage stage) { // TODO Auto-generated method
	 * stub super.setStage(stage); stage.setOnCloseRequest(event -> {
	 * stage.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST,
	 * this::closeWindowEvent); }); }
	 * 
	 * private void closeWindowEvent(WindowEvent event) { cancel();
	 * 
	 * }
	 */
	
	private void initCheckButtonEvent() {
		room_pwdchk.setSelected(false);
		room_password.setDisable(true);
		room_pwdchk.setOnAction(event -> {
			if(room_pwdchk.isSelected()) {
				room_password.setDisable(false);
			} else {
				room_password.setText("");
				room_password.setDisable(true);
			}
		});
	}
}
