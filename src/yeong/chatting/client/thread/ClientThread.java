package yeong.chatting.client.thread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.Optional;
import java.util.Vector;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import yeong.chatting.client.base.action.GoAction;
import yeong.chatting.client.chattingroom.ChattingRoomController;
import yeong.chatting.client.invite.InviteController;
import yeong.chatting.client.profile.MyProfileController;
import yeong.chatting.client.profile.ProfileController;
import yeong.chatting.client.registry.RegistryController;
import yeong.chatting.client.search.SearchController;
import yeong.chatting.client.util.ClientInfo;
import yeong.chatting.client.util.Place;
import yeong.chatting.client.util.alert.AlertFactory;
import yeong.chatting.client.util.alert.MyAlert;
import yeong.chatting.client.waitingroom.WaitingRoomController;
import yeong.chatting.model.Member;
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
				oos.writeObject(new Message(ProtocolType.REQUEST_WAITINGROOM_MEMBER, new Member(ClientInfo.currentMember)));
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
				oos.writeObject(new Message(ProtocolType.REQUEST_WAITINGROOM_MEMBER, new Member(ClientInfo.currentMember)));
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
			Platform.runLater( () -> {
				Alert alert = new Alert(AlertType.ERROR, "서버가 닫혔습니다.");
				alert.setTitle("서버가 닫혔습니다");
				alert.setHeaderText("서버 에러");
				alert.show();
			});
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
					ClientInfo.currentRoom=null;
					ClientInfo.currentMember.setPlace(Place.WaitingRoom);
					GoAction.staticGo(primaryStage, getClass().getResource(CommonPathAddress.WaitingRoomLayout));
					updateWaitingRoomList(message);	
					updateFriendList(message.getFriendList());
				});
			} catch(Exception e) {
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
			Platform.runLater( () -> {
				FXMLLoader loader = new FXMLLoader(ClientInfo.getResource(CommonPathAddress.InviteLayout));
				Parent p=null;
				try { p = loader.load(); } catch (IOException e) { e.printStackTrace(); }
				Scene scene = new Scene(p);
				
				Stage s = new Stage();
				s.initOwner(primaryStage);
				s.initModality(Modality.APPLICATION_MODAL);
				s.setTitle("초대하기");
				s.setScene(scene);
				s.show();
				InviteController inviteCon = InviteController.getCon();	/** 초대기능 ListView*/
				ObservableList<Member> memberList = FXCollections.observableArrayList(message.getMemberList());
				inviteCon.setListView(memberList);
			});
			break;
		case RESPONSE_INVITE_REQUEST:
			Platform.runLater( () -> {
				if(message.getTo().equals(ClientInfo.currentMember)) {
					Alert alert = new Alert(AlertType.CONFIRMATION, message.getFrom() + "님으로부터 " + message.getrInfo()+"으로 초대합니다.");
					alert.setTitle(message.getFrom() + "님의 초대");
					ButtonType bt1 = new ButtonType("수락");
					ButtonType bt2 = new ButtonType("거절");
					alert.getButtonTypes().setAll(bt1,bt2);
					Optional<ButtonType> result = alert.showAndWait();
					Message request;
					if(result.get() == bt1) {
						request = new Message(ProtocolType.REQUEST_ENTERROOM,ClientInfo.currentMember,message.getrInfo());
					} else {
						request = new Message(ProtocolType.RESPONSE_INVITE_REJECT,ClientInfo.currentMember,message.getrInfo());
						request.setTo(message.getFrom());
					}
					try { oos.writeObject(request); } catch (IOException e) { e.printStackTrace(); }
				}
			});
			break;
		case RESPONSE_INVITE_REJECT:
			Platform.runLater( () -> {
				if(message.getTo().equals(ClientInfo.currentMember)) {
					Alert alert = new Alert(AlertType.INFORMATION, message.getFrom() + "님이 초대를 거절하셨습니다.");
					alert.setTitle(message.getFrom() + "님의 거절");
					alert.setHeaderText("거 절");
					alert.showAndWait();
				}
			});
			break;

		case RESPONSE_SEARCH_ID:
		case RESPONSE_SEARCH_PW:
			search(message);
			break;
		case RESPONSE_PROFILE:
			URL url; 
			boolean isMy = message.getTo().equals(ClientInfo.currentMember);
			if(isMy) {
				url = ClientInfo.getResource(CommonPathAddress.MyProfileLayout);
			} else {
				url = ClientInfo.getResource(CommonPathAddress.ProfileLayout);				
			}
			FXMLLoader loader = new FXMLLoader(url);

			Platform.runLater( () -> {
				Parent p=null;
				if(isMy) {

					try { p = loader.load(); } catch (IOException e) { e.printStackTrace(); }
					MyProfileController con = loader.getController();

					con.setProfile(message.getProfile());
					Scene scene = new Scene(p);
					Stage stage = new Stage();
					stage.setTitle("나의 프로필");
					stage.setScene(scene);
					stage.show();
				} else {
					try { p = loader.load(); } catch (IOException e) { e.printStackTrace(); }
					ProfileController con = loader.getController();
					if(message.getMsg()!=null) 
						con.setProfileFriend(message.getProfile(),message.getMsg());
					else 
						con.setProfile(message.getProfile());
					Scene scene = new Scene(p);
					Stage stage = new Stage();
					stage.setTitle(message.getTo()+"님의 프로필");
					stage.setScene(scene);
					stage.show();
				}
			});

			break;
		case RESPONSE_PROFILE_EDIT:
			if(message.getMsg().equals("true")) {
				ClientInfo.currentMember.setName(message.getProfile().getNickname());
				Platform.runLater( () -> {
					Alert alert = new Alert(AlertType.INFORMATION, "업데이트를 성공하였습니다.");
					alert.setHeaderText("알림");
					alert.setTitle("성공");
					alert.showAndWait();
				});
			} else {
				Platform.runLater( () -> {
					Alert alert = new Alert(AlertType.INFORMATION, "업데이트를 실패하였습니다.");
					alert.setHeaderText("알림");
					alert.setTitle("실패");
					alert.showAndWait();
				});
			}
			break;
		case RESPONSE_DELETE_ACCOUNT:
			if(message.getMsg().equals("true")) {
				Platform.runLater( () -> {
					Alert alert = new Alert(AlertType.INFORMATION, "지금까지 이용해 주셔서 감사합니다.");
					alert.setContentText("계정이 성공적으로 삭제되었습니다.");
					alert.show();
					ClientInfo.currentMember.setPlace(Place.LoginLayout);
					MyProfileController con = MyProfileController.getProfileController();
					con.closeMyProfileController();
					message.setProtocol(ProtocolType.REQUEST_LOGOUT);
					try { oos.writeObject(message); } catch (IOException e) { e.printStackTrace(); }
					primaryStage.close();
				});

			}
			break;
		case RESPONSE_ADD_FRIEND_REQUEST:
			Platform.runLater( () -> {
				addFriendRequest(message);				
			});
			break;
		case RESPONSE_ADD_FRIEND_RESPONSE:
			Platform.runLater( () -> {
				addFriendResponse(message);
			});
			break;
		case RESPONSE_ADD_FRIEND_REQUEST_FAIL:
			Platform.runLater( () -> {
				if(message.getMsg().equals("N")) {
					Alert alert = new Alert(AlertType.ERROR, "해당 유저는 접속해 있지 않습니다.");
					alert.setTitle("잘못된 요청");
					alert.setHeaderText("에러");
					alert.show();
				} else {
					Alert alert = new Alert(AlertType.ERROR, "해당 유저와는 이미 친구입니다.");
					alert.setTitle("잘못된 요청");
					alert.setHeaderText("에러");
					alert.show();
				}
			});
			break;
		case RESPONSE_REMOVE_FRIEND:
			Platform.runLater( () -> {
				if(message.getMsg().equals("true") && message.getFrom().equals(ClientInfo.currentMember)) {
					Alert alert = new Alert(AlertType.ERROR, message.getTo() + "님을 성공적으로 삭제하였습니다.");
					alert.setTitle(ClientInfo.currentMember.toString());
					alert.setHeaderText("삭제 성공");
					alert.show();
					updateFriendList(message.getFriendList());
				}else if(message.getMsg().equals("true")) {
					Alert alert = new Alert(AlertType.ERROR, message.getFrom() + "님에게 친구삭제 당했습니다!");
					alert.setTitle(ClientInfo.currentMember.toString());
					alert.setHeaderText("삭제 알림 ");
					alert.show();
					updateFriendList(message.getResponseFriendList());

				} else {
					Alert alert = new Alert(AlertType.ERROR, message.getTo() + "님의 삭제를 실패하였습니다");
					alert.setTitle("삭제요청");
					alert.setHeaderText("삭제 실패");
					alert.show();
				}
			});
			break;
		case UPDATE_FRIEND_LIST:
			Platform.runLater( () -> {
				updateFriendList(message.getResponseFriendList());				
			});
			break;

		default:
		}
	}


	private void addFriendRequest(Message message) {
		if((message.getFrom().equals(ClientInfo.currentMember))) { // 보내는 사람이라면
			Alert alert = new Alert(AlertType.CONFIRMATION, message.getTo() + "님에게 친구 신청을 하셨습니다.");
			alert.setTitle("친구신청");
			alert.setHeaderText("친구신청");
			ButtonType bt1 = new ButtonType("확인");
			alert.getButtonTypes().setAll(bt1);
			alert.show();
		} else { // 받는사람이라면
			Alert alert = new Alert(AlertType.INFORMATION, "수락하시겠습니까?");
			alert.setTitle("친구신청");
			alert.setHeaderText(message.getFrom() + "님에게 친구 신청이 왔습니다.");
			ButtonType bt1 = new ButtonType("수락");
			ButtonType bt2 = new ButtonType("거절");
			alert.getButtonTypes().setAll(bt1,bt2);
			Optional<ButtonType> result = alert.showAndWait();
			String msg = null;
			if(result.get() == bt1) {
				msg = "accept";
			} else {				
				msg = "deny";
			}
			Member previousFrom = message.getFrom();
			Member previousTo = message.getTo();
			ProtocolType requestProtocol = ProtocolType.REQUEST_ADD_FRIEND_RESPONSE;
			Message request = new Message(requestProtocol,previousTo);
			request.setTo(previousFrom);
			request.setMsg(msg);
			try { oos.writeObject(request); } catch (IOException e) { e.printStackTrace(); }
		}
	}

	private void addFriendResponse(Message message) {
		if((message.getFrom().equals(ClientInfo.currentMember))) { // 보내는 사람이라면
			Alert alert = null;
			if(message.getMsg().equals("accept")) {
				alert = new Alert(AlertType.INFORMATION, message.getTo() + "님의 친구요청을 수락하셨습니다.");
				alert.setTitle("친구수락");
				alert.setHeaderText(message.getTo()+"님과 친구가 되었습니다.");
				updateFriendList(message.getFriendList());
			} else {
				alert = new Alert(AlertType.INFORMATION, message.getTo() + "님의 친구요청을 거절하셨습니다.");
				alert.setTitle("친구거절");
				alert.setHeaderText(message.getTo()+"님과 친구가 되지 못했습니다.");
			}
			ButtonType bt1 = new ButtonType("확인");
			alert.getButtonTypes().setAll(bt1);
			alert.showAndWait();
		} else { // 받는 사람이라면
			Alert alert = null;
			if(message.getMsg().equals("accept")) {
				alert = new Alert(AlertType.INFORMATION, message.getFrom() + "님이 친구요청을 수락하셨습니다.");
				alert.setTitle("친구수락");
				alert.setHeaderText(message.getFrom()+"님과 친구가 되었습니다.");
				updateFriendList(message.getResponseFriendList());
			} else {
				alert = new Alert(AlertType.INFORMATION, message.getFrom() + "님이 친구요청을 거절하셨습니다.");
				alert.setTitle("친구거절");
				alert.setHeaderText(message.getFrom()+"님과 친구가 되지 못했습니다.");
			}
			ButtonType bt1 = new ButtonType("확인");
			alert.getButtonTypes().setAll(bt1);
			alert.showAndWait();
		}
	}




	private void search(Message msg) {
		String responseStr;
		if(msg.getProtocol() == ProtocolType.RESPONSE_SEARCH_ID) {
			responseStr = message.getSv().getId();

		} else {
			responseStr = message.getSv().getPassword();

		}
		Platform.runLater( () -> {
			if(responseStr==null) {
				Alert alert = new Alert(AlertType.INFORMATION, "찾는 아이디가 없습니다.");
				alert.setTitle("ID 찾기");
				alert.setHeaderText("없는 정보");
				alert.showAndWait();

			} else {
				Alert alert;
				if(message.getSv().getPassword()==null) {				
					alert = new Alert(AlertType.INFORMATION, "ID = "+responseStr);
					alert.setTitle("ID 찾기");
					alert.setHeaderText("일치하는 아이디");	

				} else {
					alert = new Alert(AlertType.INFORMATION, "PASSWORD = " + responseStr);
					alert.setTitle("PASSWORD 찾기");
					alert.setHeaderText("일치하는 비밀번호");	
				}
				alert.showAndWait();
				SearchController sc = SearchController.getCon();
				sc.getSearchControllerStage().close();
			}
		});
	}




	private void updateFriendList(Vector<Member> list) {
		WaitingRoomController con = WaitingRoomController.getController();
		ObservableList<Member> oList = FXCollections.observableArrayList(list);
		con.setFriendView(oList);
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

		if(msg.getFriendList() != null && msg.getFrom().equals(ClientInfo.currentMember)) {
			ObservableList<Member> friendList = FXCollections.observableArrayList(msg.getFriendList());
			con.setFriendView(friendList);
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
		ObservableList<Member> whisperList = FXCollections.observableArrayList();

		for(Member m :roomMemberList) {
			if(m.equals(ClientInfo.currentMember))
				continue;
			whisperList.add(m);
		}
		con.setWhisperView(whisperList);
	}

	private void updateInviteForm(Message msg) {
		InviteController inviteCon = InviteController.getCon();	/** 초대기능 ListView*/
		ObservableList<Member> memberList = FXCollections.observableArrayList(msg.getMemberList());
		if(inviteCon!=null) {
			inviteCon.setListView(memberList);
		}
	}



}
