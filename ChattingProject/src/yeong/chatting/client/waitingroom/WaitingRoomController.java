package yeong.chatting.client.waitingroom;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import yeong.chatting.client.action.ActionInfo;
import yeong.chatting.client.controller.BaseController;
import yeong.chatting.model.Member;
import yeong.chatting.model.MemberBeans;
import yeong.chatting.model.RoomInfo;
import yeong.chatting.util.CommonPathAddress;
import yeong.chatting.util.Log;

/**
 * @FileName  : WaitingRoomController.java
 * @Project     : ChattingProject
 * @Date         : 2020. 5. 10.
 * @작성자      : Yeong
 * @변경이력 :
 * @프로그램 설명 : 대기실에 대한 클래스
 */
public class WaitingRoomController extends BaseController {
	
	@FXML TableView<RoomInfo> roomList;
	@FXML ListView<MemberBeans> memberList;

	@FXML Button createBtn;
	@FXML Button enterBtn;
	@FXML Button logoutBtn;
	
	
		@Override
		public void initialize(URL location, ResourceBundle resources) {
			super.initialize(location, resources);
			setControls(roomList, memberList);
		}
	
	@FXML
	private void create() {
		action(new ActionInfo("Form",createBtn,CommonPathAddress.CreateRoomLayout));
		Stage stage = (Stage)createBtn.getScene().getWindow();
		stage.close();
	}
	
	@FXML
	private void enter() {
		action(new ActionInfo("EnterRoom",enterBtn,CommonPathAddress.ChattingRoomLayout));
	}
	
	@FXML
	private void logout() {
		action(new ActionInfo("Logout",logoutBtn,CommonPathAddress.LoginLayout));
		action(new ActionInfo("Go",logoutBtn,CommonPathAddress.LoginLayout));
	}
	
	//화면이 띄어졌을때
	@FXML
	public void getMembers() {
		ActionInfo action = new ActionInfo("RequestCurrentMember",memberList);
		action(action);
	}
	
	
	
	
}
