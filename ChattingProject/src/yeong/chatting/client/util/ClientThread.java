package yeong.chatting.client.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Vector;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Control;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import yeong.chatting.client.action.GoAction;
import yeong.chatting.client.chattingroom.ChattingRoomController;
import yeong.chatting.client.registry.RegistryController;
import yeong.chatting.client.util.alert.AlertFactory;
import yeong.chatting.client.util.alert.MyAlert;
import yeong.chatting.client.waitingroom.WaitingRoomController;
import yeong.chatting.model.Member;
import yeong.chatting.model.MemberBeans;
import yeong.chatting.model.Message;
import yeong.chatting.model.RoomInfo;
import yeong.chatting.util.CommonPathAddress;
import yeong.chatting.util.Log;
import yeong.chatting.util.ProtocolType;

/**
 * �����ؼ� �������ִ� Ŭ����
 */
public class ClientThread implements Runnable {

	private ObjectInputStream ois;
	private ObjectOutputStream oos;

	private Message message;

	private Stage primaryStage;

	private ChattingRoomController crCon;
	private RegistryController rCon;

	public ClientThread(ObjectInputStream ois,ObjectOutputStream oos, Stage primaryStage) {
		this.ois = ois;
		this.oos = oos;
		this.primaryStage = primaryStage;
	}



	@Override
	public void run() {
		try {
			while(true) {
				message = (Message)ois.readObject();
				checkProtocol(message);
			}	
		} catch (ClassNotFoundException e) {
			Log.e(getClass(), e);
		} catch (IOException e) {
			Log.e("�������� ������ ������ϴ�.");
			System.exit(0);
		}

	}

	/**
	 * ���� �����Ϳ� ���� �з����ִ� �޼ҵ�
	 */

	private void checkProtocol(Message message) {
		switch(message.getProtocol()) {
		case RESPONSE_LOGIN_SUCCESS:  
			ClientInfo.currentMember = message.getFrom();
			ClientInfo.currentMember.setPlace(Place.WaitingRoom);
			try {
				oos.writeObject(new Message(ProtocolType.REQUEST_WAITINGROOM_MEMBER, ClientInfo.currentMember));
			} catch (IOException e) {
				e.printStackTrace();
			} 
			break;
		case RESPONSE_LOGIN_FAIL:  
			AlertFactory.createAlert(AlertType.ERROR, "���̵� �Ǵ� �н����带 Ȯ�����ּ���"); 
			break;
		case RESPONSE_REGISTRY_SUCCESS: 
			AlertFactory.createAlert(AlertType.CONFIRMATION, "ȸ����� ����"); 
			break;
		case RESPONSE_REGISTRY_FAIL: 
			AlertFactory.createAlert(AlertType.ERROR, "ȸ����� ����"); 
			break;
		case RESPONSE_WAITINGROOM_MEMBER:
			try { setWaitingRoom(message); } catch (IOException e) { Log.e(getClass(),e);}
			break;
		case RESPONSE_LOGOUT:
			try {
				oos.writeObject(new Message(ProtocolType.REQUEST_WAITINGROOM_MEMBER, ClientInfo.currentMember));
				ClientInfo.currentMember = null;	
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case RESPONSE_CREATEROOM:
			ClientInfo.currentRoom = message.getrInfo();
			Platform.runLater( () -> {
				GoAction.staticGo(primaryStage, getClass().getResource(CommonPathAddress.ChattingRoomLayout));
				try { setChattingRoom(message); } catch (IOException e) { e.printStackTrace(); }
			});
			break;
		case RESPONSE_ENTERROOM_SUCCESS:
			ClientInfo.currentRoom = message.getrInfo();
			try { setChattingRoom(message); } catch (IOException e) { Log.e(getClass(), e);}
			break;
		case RESPONSE_EXITROOM:
			Log.i(getClass(), " MEMBER EXITROOM RESPONSE = " + ClientInfo.currentMember );
			Log.i(getClass(), " ROOM EXITROOM RESPONSE = " + ClientInfo.currentRoom);
			ClientInfo.currentRoom = null;
			try { setWaitingRoom(message); } catch (IOException e) { Log.e(getClass(),e);}
			break;
		case RESPONSE_EXITROOM_HOST:

			Log.i(ClientInfo.currentMember +" Ŭ�� "+ClientInfo.currentRoom);
			Log.i(ClientInfo.currentMember +" ���� "+message.getrInfo());
			if(ClientInfo.currentRoom.equals(message.getrInfo())) {
				ClientInfo.currentMember.setPlace(Place.WaitingRoom);
				ClientInfo.currentRoom = null;
				try { setWaitingRoom(message); } catch (IOException e) { e.printStackTrace(); }
			}
			break;
		case RESPONSE_UPDATEWAITINGROOM:
			if(ClientInfo.currentMember == null)  return;
			if(ClientInfo.currentMember.getPlace() == Place.WaitingRoom) {
				Platform.runLater( () -> {
					updateWaitingRoomList(message);				
				});
			}
			break;
		case RESPONSE_UPDATECHATTINGROOM:
			if(ClientInfo.currentRoom == null) {
				try { setWaitingRoom(message); } catch (IOException e) { e.printStackTrace(); }
				return;
			}
			if(ClientInfo.currentRoom.getRoom_num() == message.getrInfo().getRoom_num())  {
				Platform.runLater( () -> {
					updateChattingRoomList(message);				
				});
			}
			break;
		case CLOSE:
			Log.i("������ �ݾҽ��ϴ�.");
			System.exit(0);
			break;

		case RESPONSE_FORCEDEXIT:
			try {
				ClientInfo.currentMember.setPlace(Place.WaitingRoom);
				GoThread goWaitingRoom = new GoThread(primaryStage, getClass().getResource(CommonPathAddress.WaitingRoomLayout));
				Platform.runLater(goWaitingRoom);
				if(ClientInfo.currentMember.getPlace() == Place.WaitingRoom)
					Platform.runLater( () -> {
						updateWaitingRoomList(message);				
					});
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			break;
		case RESPONSE_SEND:
			crCon = ChattingRoomController.getController();
			crCon.setLog(message.getFrom(), message.getMsg());
			break;
		case RESPONSE_IDCHECK:
			String checkedID= message.getMsg();
			if(checkedID != null) {
				MyAlert af = AlertFactory.createAlert(AlertType.ERROR, "�ش� ���̵� " + message.getMsg() +"�� ����Ͻ� �� �����ϴ�.");
			} else {
				rCon = RegistryController.getCon();
				Platform.runLater( () -> {
					Alert alert = new Alert(AlertType.CONFIRMATION, message.getFrom().getId() +"(��)�� ����� �����մϴ�. ����Ͻðڽ��ϱ�?");
					ButtonType bt1 = new ButtonType("Ȯ��");
					ButtonType bt2 = new ButtonType("���");

					alert.getButtonTypes().setAll(bt1,bt2);
					Optional<ButtonType> clickBT = alert.showAndWait();
					if(clickBT.get() == bt1) {
						rCon.getIDCheckButton().setDisable(true);
						rCon.getIDField().setDisable(true);
						rCon.setisCheckedId(true);
						rCon.getEmailTextField().requestFocus(); 
					}
				});
			}
		default:
		}
	}

	private void setWaitingRoom(Message message) throws IOException {

		if(ClientInfo.currentMember == null) return;
		GoThread goWaitingRoom = new GoThread(primaryStage, getClass().getResource(CommonPathAddress.WaitingRoomLayout));
		/**
		 * �޽��� ��������̶� ��������� ���ٸ� ���Ƿ� �̵����Ѷ�
		 */
		if(ClientInfo.currentMember.equals(message.getFrom())) {
			Platform.runLater(goWaitingRoom);
		}
		/**
		 * ���ǿ� �ִٸ� ����Ʈ�� ������Ʈ ���Ѷ�
		 */
		if(ClientInfo.currentMember.getPlace() == Place.WaitingRoom)
			Platform.runLater( () -> {
				updateWaitingRoomList(message);				
			});

	}


	private void setChattingRoom(Message message) throws IOException {
		GoThread goChattingRoom = new GoThread(primaryStage, getClass().getResource(CommonPathAddress.ChattingRoomLayout));

		/**
		 * �޽��� ��������̶� ��������� ���ٸ� ������� ���� �������� ���ý�Ű�� �̵����Ѷ�
		 */
		if(ClientInfo.currentMember.getId().equals(message.getFrom().getId())) {
			ClientInfo.currentRoom = message.getrInfo();
			Platform.runLater(goChattingRoom);
		}

		/**
		 * ���� ���۵� �޽����� ���� �濡 ���� �ִٸ� List�� ������Ʈ ���Ѷ�
		 */
		if(ClientInfo.currentRoom.getRoom_num() == message.getrInfo().getRoom_num()) 
			Platform.runLater( () -> {
				updateChattingRoomList(message);				
			});


	}

	/**
	 * updateWaitingRoom
	 * @param msg
	 */
	private void updateWaitingRoomList(Message msg) {
		WaitingRoomController con = WaitingRoomController.getController();

		if(msg.getMemberList() != null) {
			ObservableList<Member> memberList = FXCollections.observableArrayList(msg.getMemberList());
			con.setListView(memberList);
		}
		if(msg.getRoomList() != null) {
			ObservableList<RoomInfo> roomList = FXCollections.observableArrayList(msg.getRoomList());
			con.setTableView(roomList);
		}
	}

	/** 
//	 * updateChattingRoom
	 */
	private void updateChattingRoomList(Message msg) {
		ChattingRoomController con = ChattingRoomController.getController();
		ObservableList<Member> roomMemberList = FXCollections.observableArrayList(msg.getRoomMemberList());
		con.setListView(roomMemberList);
	}

}
