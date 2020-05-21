package yeong.chatting.client.chattingroom;

import java.io.IOException;
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
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import yeong.chatting.client.base.action.ActionInfo;
import yeong.chatting.client.base.action.FormAction;
import yeong.chatting.client.base.action.GoAction;
import yeong.chatting.client.base.controller.BaseController;
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

	public void setWhisper(Member m, Member to, String msg) {
		StringBuilder sb = new StringBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String time = sdf.format(Calendar.getInstance().getTime());

		
		if(m.equals(ClientInfo.currentMember)) {
			Log.i(getClass(), m);
			Log.i(getClass(), ClientInfo.currentMember);
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
		memberList.setCellFactory(new Callback<ListView<Member>, ListCell<Member>>() {
			@Override
			public ListCell<Member> call(ListView<Member> param) {
				ListCell<Member> cell = new ListCell<Member>() {
					@Override
					protected void updateItem(Member item, boolean empty) {
						if(item != null && !empty) {
							StringBuilder sb = new StringBuilder();
							sb.append(item.toString() + " (방장)");
							if(item.getId().equals(ClientInfo.currentRoom.getRoom_host())) {
								setText(sb.toString());
								return;
							}
							setText(item.toString());
						}
					}
				};
				return cell;
			}
		});
	}
	
	private void initEvent() {
		chat.setOnAction(e -> {
			sendBtn.fire();
		});
	}
	
	private void initWhisperList() {
		whisperList = FXCollections.observableArrayList();
		whisper.setItems(whisperList);

	}

}
