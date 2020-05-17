package yeong.chatting.client.chattingroom;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import yeong.chatting.client.action.ActionInfo;
import yeong.chatting.client.action.GoAction;
import yeong.chatting.client.controller.BaseController;
import yeong.chatting.model.Member;
import yeong.chatting.util.CommonPathAddress;

public class ChattingRoomController extends BaseController{
	
	@FXML private ListView<Member> memberList;
	@FXML private TextArea ChattingLog;
	@FXML private TextField chat;
	@FXML private Button sendBtn;
	@FXML private Button exitBtn;
	
	private ObservableList<Member> list;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		list = FXCollections.observableArrayList();
		memberList.setItems(list);
	}
	
	
	
	@FXML
	private void send() {
		action(new ActionInfo("Send",sendBtn));
	}
	
	@FXML
	private void exit() {
		
		GoAction.WaitingRoomGo((Stage)exitBtn.getScene().getWindow(), getClass().getResource(CommonPathAddress.WaitingRoomLayout));
	}
	
	
//	public void setListView(ObservableList<Member> list) {
//		this.list.setAll(list);
//	}
	
}
