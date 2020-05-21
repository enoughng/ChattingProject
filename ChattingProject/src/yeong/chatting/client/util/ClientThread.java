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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Control;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Modality;
import javafx.stage.Stage;
import yeong.chatting.client.base.action.GoAction;
import yeong.chatting.client.chattingroom.ChattingRoomController;
import yeong.chatting.client.invite.InviteController;
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
 * 수신해서 가공해주는 클래스
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
		} catch(NullPointerException e) {
			e.printStackTrace();
			Platform.runLater( () -> {
				Alert alert = new Alert(AlertType.ERROR, "서버가 닫혀있어 종료합니다."); 
				alert.showAndWait();
				System.exit(0);				
			});
		}catch (ClassNotFoundException e) {
			Log.e(getClass(), e);
		} catch (IOException e) {
			Log.e("서버와의 연결이 끊겼습니다.");
			System.exit(0);
		}

	}

	/**
	 * 들어온 데이터에 대해 분류해주는 메소드
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
			if(message.getFrom() == null) {
				AlertFactory.createAlert(AlertType.ERROR, "아이디 또는 패스워드를 확인해주세요"); 				
			} else {
				AlertFactory.createAlert(AlertType.ERROR, "이미 로그인된 계정입니다.");
			}
			break;
		case RESPONSE_REGISTRY_SUCCESS: 
			AlertFactory.createAlert(AlertType.CONFIRMATION, "회원등록 성공"); 
			break;
		case RESPONSE_REGISTRY_FAIL: 
			AlertFactory.createAlert(AlertType.ERROR, "회원등록 실패"); 
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
			ClientInfo.currentRoom = null;
			try { setWaitingRoom(message); } catch (IOException e) { Log.e(getClass(),e);}
			break;
		case RESPONSE_EXITROOM_HOST:
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
			if(ClientInfo.currentMember.getPlace() == Place.InviteForm) {
				Platform.runLater( () -> {
					updateInviteForm(message);
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
			Log.i("서버를 닫았습니다.");
			System.exit(0);
			break;

		case RESPONSE_FORCEDEXIT:
			try {
				Platform.runLater( ()-> {
					Alert alert = new Alert(AlertType.CONFIRMATION, "방장이 방을 폭파시켰습니다!!!");
					alert.setTitle("방장이 나가버렸음");
					ButtonType t1 = new ButtonType("확인");
					alert.getButtonTypes().setAll(t1);
					alert.setHeaderText("강제퇴장");
					alert.show();
				});
				ClientInfo.currentRoom=null;
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
				MyAlert af = AlertFactory.createAlert(AlertType.ERROR, "해당 아이디 " + message.getMsg() +"는 사용하실 수 없습니다.");
			} else {
				rCon = RegistryController.getCon();
				Platform.runLater( () -> {
					Alert alert = new Alert(AlertType.CONFIRMATION, message.getFrom().getId() +"(은)는 사용이 가능합니다. 사용하시겠습니까?");
					ButtonType bt1 = new ButtonType("확인");
					ButtonType bt2 = new ButtonType("취소");

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
			break;
		case RESPONSE_ENTERROOM_PASSWORD:
			Platform.runLater( () -> {
				TextInputDialog tid = new TextInputDialog();

				tid.setTitle("비밀번호 입력");
				tid.setHeaderText("비밀번호 입력 : ");
				tid.setContentText("비밀번호를 입력하세요");
				tid.initOwner(primaryStage);
				tid.initModality(Modality.APPLICATION_MODAL);
				Optional<String> result = tid.showAndWait();

				result.ifPresent(resultString ->{
					if(resultString.equals(message.getrInfo().getRoom_pwd())) {

						message.setProtocol(ProtocolType.REQUEST_ENTERROOM);
						String userPw = resultString;
						RoomInfo newInfo = message.getrInfo();
						newInfo.setRoom_pwd(userPw);
						message.setrInfo(newInfo);
						try { oos.writeObject(message); } catch (IOException e) { e.printStackTrace(); }
						return;
					} else {
						Alert alert = new Alert(AlertType.ERROR, "잘못된 비밀번호 입니다.");
						alert.setHeaderText("비밀번호 오류");
						alert.setTitle("비밀번호 오류");
						alert.showAndWait();
						return;
					}
				});
			});
			break;
		case RESPONSE_WHISPER:
			crCon = ChattingRoomController.getController();
			crCon.setWhisper(message.getFrom(), message.getTo(), message.getMsg());
			break;

		case RESPONSE_INVITEUPDATE:
			Log.i(getClass(),"inviteUpdate");
			Platform.runLater( () -> {
				FXMLLoader loader = new FXMLLoader(ClientInfo.getResource(CommonPathAddress.InviteLayout));
				Parent p=null;
				try { p = loader.load(); } catch (IOException e) { e.printStackTrace(); }
				Scene scene = new Scene(p);
				
				Stage s = new Stage();
				s.initOwner(primaryStage);
				s.initModality(Modality.APPLICATION_MODAL);
				s.setScene(scene);
				s.show();
				InviteController inviteCon = InviteController.getCon();	/** 초대기능 ListView*/
				Log.i(getClass(), "invite 실행");
				ObservableList<Member> memberList = FXCollections.observableArrayList(message.getMemberList());
				inviteCon.setListView(memberList);
			});


			break;
		default:
		}
	}

	private void setWaitingRoom(Message message) throws IOException {

		if(ClientInfo.currentMember == null) return;
		GoThread goWaitingRoom = new GoThread(primaryStage, getClass().getResource(CommonPathAddress.WaitingRoomLayout));
		/**
		 * 메시지 보낸사람이랑 받은사람이 같다면 대기실로 이동시켜라
		 */
		if(ClientInfo.currentMember.equals(message.getFrom())) {
			Platform.runLater(goWaitingRoom);
		}


		/**
		 * 대기실에 있다면 리스트를 업데이트 시켜라
		 */
		if(ClientInfo.currentMember.getPlace() == Place.WaitingRoom)
			Platform.runLater( () -> {
				updateWaitingRoomList(message);				
			});

	}


	private void setChattingRoom(Message message) throws IOException {
		GoThread goChattingRoom = new GoThread(primaryStage, getClass().getResource(CommonPathAddress.ChattingRoomLayout));

		/**
		 * 메시지 보낸사람이랑 받은사람이 같다면 현재방을 받은 방정보로 셋팅시키고 이동시켜라
		 */
		if(ClientInfo.currentMember.getId().equals(message.getFrom().getId())) {
			ClientInfo.currentRoom = message.getrInfo();
			Platform.runLater(goChattingRoom);
		}

		/**
		 * 현재 전송된 메시지와 같은 방에 들어와 있다면 List를 업데이트 시켜라
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
		/** 귓속말 전용 리스트*/
		ObservableList<String> whisperList = FXCollections.observableArrayList();

//		for(Member m :roomMemberList) {
//			String name = m.getName();
//			if(name.equals(ClientInfo.currentMember.getName()))
//				continue;
//			whisperList.add(name);
//		}
		con.setWhisperView(roomMemberList);
	}

	private void updateInviteForm(Message msg) {
		InviteController inviteCon = InviteController.getCon();	/** 초대기능 ListView*/
		ObservableList<Member> memberList = FXCollections.observableArrayList(msg.getMemberList());
		if(inviteCon!=null) {
			Log.i(getClass(), "invite 실행");
			inviteCon.setListView(memberList);
		}
	}

}
