package yeong.chatting.client.chattingroom;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import yeong.chatting.client.util.ClientInfo;
import yeong.chatting.client.util.Place;
import yeong.chatting.model.Member;
import yeong.chatting.util.CommonPathAddress;
import yeong.chatting.util.Log;

public class ChattingRoomController extends BaseController{
	
	@FXML private ListView<Member> memberList;
	@FXML private TextArea ChattingLog;
	@FXML private TextField chat;
	@FXML private Button sendBtn;
	@FXML private Button exitBtn;
	
	private ObservableList<Member> list;
	private static ChattingRoomController con;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		con = this;
		list = FXCollections.observableArrayList();
		memberList.setItems(list);
		
		chat.setOnAction(e -> {
			sendBtn.fire();
		});
	}
	
		
	
	@FXML
	private void send() {
		ActionInfo send = new ActionInfo("Send",sendBtn);
		send.setCons(chat);
		action(send);
	}
	
	@FXML
	private void exit() {
		ClientInfo.currentMember.setPlace(Place.WaitingRoom);
		action(new ActionInfo("RoomExit",exitBtn,CommonPathAddress.WaitingRoomLayout));
	}
	
	public void setListView(ObservableList<Member> list) {
		this.list.setAll(list);
	}
	
	public static ChattingRoomController getController() {
		return con;
	}
	
	public void setLog(Member m, String msg) {
		StringBuilder sb = new StringBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String time = sdf.format(Calendar.getInstance().getTime());
		sb.append("[");
		sb.append(time);
		sb.append("] ");
		sb.append(m.getName());
		sb.append(" > ");
		sb.append(msg);
		sb.append("\n");
		ChattingLog.appendText(sb.toString());
		chat.setText("");
	}
	
}
