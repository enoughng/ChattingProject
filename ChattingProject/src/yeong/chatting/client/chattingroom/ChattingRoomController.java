package yeong.chatting.client.chattingroom;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import yeong.chatting.client.action.ActionInfo;
import yeong.chatting.client.controller.BaseController;
import yeong.chatting.model.Member;
import yeong.chatting.util.CommonPathAddress;

public class ChattingRoomController extends BaseController{
	
	@FXML private ListView<Member> memberList;
	@FXML private TextArea ChattingLog;
	@FXML private TextField chat;
	@FXML private Button sendBtn;
	@FXML private Button exitBtn;
	
	@FXML
	private void send() {
		action(new ActionInfo("Send",sendBtn));
	}
	
	@FXML
	private void exit() {
		action(new ActionInfo("Form",exitBtn,CommonPathAddress.WaitingRoomLayout));
		Stage stage = (Stage)exitBtn.getScene().getWindow();
		stage.close();
	}
	
	
}
