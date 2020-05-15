package yeong.chatting.client.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Vector;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Control;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import yeong.chatting.client.action.GoAction;
import yeong.chatting.client.util.alert.AlertFactory;
import yeong.chatting.model.Member;
import yeong.chatting.model.MemberBeans;
import yeong.chatting.model.Message;
import yeong.chatting.model.RoomInfo;
import yeong.chatting.util.CommonPathAddress;
import yeong.chatting.util.Log;
import yeong.chatting.util.ProtocolType;

/**
 * 수신해서 가공해주는 클래스
 */
public class ClientThread implements Runnable {

	private ObjectInputStream ois;
	private ObjectOutputStream oos;

	private Message message;

	private Stage primaryStage;

	private Control[] cons;

	public ClientThread(ObjectInputStream ois,ObjectOutputStream oos, Stage primaryStage) {
		this.ois = ois;
		this.oos = oos;
		this.primaryStage = primaryStage;
	}



	@Override
	public void run() {
		try {
			while(true) {
				try {
					Message message = (Message)ois.readObject();
					checkProtocol();
				} catch (NullPointerException e) {
				}
			}
		} catch (ClassNotFoundException e) {
			Log.e(getClass(), e);
		} catch (IOException e) {
			Log.e("서버와의 연결이 끊겼습니다.");
			System.exit(0);
		}

	}

	/**
	 * 들어온 데이터에 대해 분류해주는 메소드
	 */

	private void checkProtocol() {

		switch(message.getProtocol()) {
		case RESPONSE_LOGIN_SUCCESS:  
			ClientInfo.currentMember = message.getFrom();
			Platform.runLater(()->{
				GoAction.go(primaryStage, getClass().getResource(CommonPathAddress.WaitingRoomLayout));
			});
			try {
				oos.writeObject(new Message(ProtocolType.REQUEST_WAITINGROOM_MEMBER, ClientInfo.currentMember));
			} catch (IOException e) {
				e.printStackTrace();
			} 
			break;
		case RESPONSE_LOGIN_FAIL:  
			AlertFactory.createAlert(AlertType.ERROR, "아이디 또는 패스워드를 확인해주세요"); 
			break;
		case RESPONSE_REGISTRY_SUCCESS: 
			AlertFactory.createAlert(AlertType.CONFIRMATION, "회원등록 성공"); 
			break;
		case RESPONSE_REGISTRY_FAIL: 
			AlertFactory.createAlert(AlertType.ERROR, "회원등록 실패"); 
			break;
		case RESPONSE_WAITINGROOM_MEMBER:
			setListView();
			break;
		case RESPONSE_LOGOUT:
			

		default:
		}


	}

	private void setListView() {
		Log.i(message.getMemberList());
		ObservableList<Member> list = FXCollections.observableArrayList(message.getMemberList());

		//		TableView<RoomInfo> tableView = (TableView<RoomInfo>)cons[0];
		//				ListView<Member> listView = (ListView<Member>)cons[1];
		//				listView.getItems().setAll(list);
	}


}
