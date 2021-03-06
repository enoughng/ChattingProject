package yeong.chatting.client.chattingroom;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import yeong.chatting.client.base.action.ActionInfo;
import yeong.chatting.client.base.controller.BaseController;
import yeong.chatting.client.util.ClientInfo;
import yeong.chatting.client.util.Place;
import yeong.chatting.model.Member;
import yeong.chatting.util.CommonPathAddress;
import yeong.chatting.util.Log;

public class ChattingRoomController extends BaseController{

	@FXML BorderPane chatMain;
	
	@FXML private ListView<Member> memberList;
	@FXML private TextArea ChattingLog;
	@FXML private TextField chat;
	@FXML private Button sendBtn;
	@FXML private Button exitBtn;
	@FXML private ComboBox<Member> whisper;
	@FXML private Button invite;
	
	private ObservableList<Member> list;
	private ObservableList<Member> whisperList;
	private static ChattingRoomController con;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		con = this;
		
		initMemberList();
		initEvent();
		initWhisperList();
		
	}

	@FXML
	private void send() {

		if(whisper.getSelectionModel().getSelectedIndex() == 0) {
			ActionInfo send = new ActionInfo("Send",sendBtn);
			send.setCons(chat);
			action(send);
		} else {
			ActionInfo secretSend = new ActionInfo("SecretSend", sendBtn);
			secretSend.setCons(whisper,chat,memberList);
			
			action(secretSend);
		}
	}


	@FXML
	private void exit() {
		ClientInfo.currentMember.setPlace(Place.WaitingRoom);
		action(new ActionInfo("RoomExit",exitBtn,CommonPathAddress.WaitingRoomLayout));
	}
	
	@FXML
	private void invite() {
		ClientInfo.currentMember.setPlace(Place.InviteForm);
		action(new ActionInfo("FormInvite", invite));
	}
	
	
	/** 내 / 외부 로직을 위한 메서드 */

	/**
	 * Thread에서 MemberList 업데이트
	 * @param list
	 */
	public void setListView(ObservableList<Member> list) {
		this.list.setAll(list);
	}

	/**
	 * Thread에서 귓속말 목록 업데이트
	 * @param list
	 */
	public void setWhisperView(ObservableList<Member> list) {
		whisperList.clear();
		whisperList.add(new Member("전체", null, "전체"));
		whisperList.addAll(list);
		whisper.getSelectionModel().select(0);
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
		sb.append("[전체] ");
		sb.append(m.getName());
		sb.append(": ");
		sb.append(msg);
		sb.append("\n");
		ChattingLog.appendText(sb.toString());
		if(m.equals(ClientInfo.currentMember))
			chat.setText("");
	}
	
	public void exit(Member m) {
		StringBuilder sb = new StringBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String time = sdf.format(Calendar.getInstance().getTime());
		
		sb.append("[");
		sb.append(time);
		sb.append("] ");
		sb.append(m.toString());
		sb.append(" 님이 퇴장 하셨습니다.");
		sb.append("\n");
		ChattingLog.appendText(sb.toString());
	}
	
	public void enter(Member m) {
		StringBuilder sb = new StringBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String time = sdf.format(Calendar.getInstance().getTime());
		
		sb.append("[");
		sb.append(time);
		sb.append("] ");
		sb.append(m.toString());
		sb.append(" 님이 입장 하셨습니다.");
		sb.append("\n");
		ChattingLog.appendText(sb.toString());
	}

	public void setWhisper(Member m, Member to, String msg) {
		StringBuilder sb = new StringBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String time = sdf.format(Calendar.getInstance().getTime());

		
		if(m.equals(ClientInfo.currentMember)) {
			sb.append("[");
			sb.append(time);
			sb.append("] ");
			sb.append("[발신] ");
			sb.append(m.getName());
			sb.append("-> ");
			sb.append(to.getName());
			sb.append(": ");
			sb.append(msg);
			sb.append("\n");
			ChattingLog.appendText(sb.toString());
			chat.setText("");
		} else {
			sb.append("[");
			sb.append(time);
			sb.append("] ");
			sb.append("[수신] ");
			sb.append(m.getName());
			sb.append(": ");
			sb.append(msg);
			sb.append("\n");
			ChattingLog.appendText(sb.toString());
		}
	}

	public Stage getWindow() {
		return (Stage)sendBtn.getScene().getWindow();
	}
	
	private void initMemberList() {
		list = FXCollections.observableArrayList();
		memberList.setItems(list);
		
		memberList.setOnMouseClicked(event -> {
			String eventTarget = event.getTarget().toString();
			if(eventTarget.substring(eventTarget.length()-6, eventTarget.length()).contains("'null'")) {
				 memberList.getSelectionModel().clearSelection();
				 return;
			}
			if(event.getClickCount() == 2) {	 
				 ActionInfo action = new ActionInfo("PopupProfile",memberList,CommonPathAddress.MyProfileLayout);
				 if(memberList.getSelectionModel().getSelectedItem().equals(ClientInfo.currentMember)) {
					action.setUserDatas("My", memberList.getSelectionModel().getSelectedItem());
				} else {
					action.setUserDatas("", memberList.getSelectionModel().getSelectedItem());					
				}
				action(action);
			}
		});
		memberList.setCellFactory(new Callback<ListView<Member>, ListCell<Member>>() {
			@Override
			public ListCell<Member> call(ListView<Member> param) {
				ListCell<Member> cell = new ListCell<Member>() {
					@Override
					protected void updateItem(Member item, boolean empty) {
						super.updateItem(item, empty);
						if(item != null && !empty) {
							StringBuilder sb = new StringBuilder();
							sb.append(item.toString() + " (방장)");
							if(item.getId().equals(ClientInfo.currentRoom.getRoom_host())) {
								setText(sb.toString());
							} else {
								setText(item.toString());
							} 
						} else {
							setText(null);
						}
					}
				};
				return cell;
			}
		});
	}
	
	protected void initEvent() {
		chat.setOnAction(e -> {
			sendBtn.fire();
		});
	}
	
	private void initWhisperList() {
		whisperList = FXCollections.observableArrayList();
		whisper.setItems(whisperList);

	}

}
