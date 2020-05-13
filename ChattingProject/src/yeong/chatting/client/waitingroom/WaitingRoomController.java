package yeong.chatting.client.waitingroom;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import yeong.chatting.client.action.ActionInfo;
import yeong.chatting.client.action.CommonAction;
import yeong.chatting.client.controller.BaseController;
import yeong.chatting.client.util.ThreadUtil;
import yeong.chatting.model.Member;
import yeong.chatting.model.RoomInfo;
import yeong.chatting.util.CommonPathAddress;

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
	@FXML ListView<Member> memberList;

	@FXML Button createBtn;
	@FXML Button enterBtn;
	@FXML Button logoutBtn;
	
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
		action(new ActionInfo("Go",logoutBtn,CommonPathAddress.LoginLayout));
	}
}
