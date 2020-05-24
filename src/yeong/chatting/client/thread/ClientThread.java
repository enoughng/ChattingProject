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
		} catch(NullPointerException e) {
			Platform.runLater( () -> {
				Alert alert = new Alert(AlertType.ERROR, "������ �����־� �����մϴ�."); 
				alert.showAndWait();
				System.exit(0);				
			});
		}catch (ClassNotFoundException e) {
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
				oos.writeObject(new Message(ProtocolType.REQUEST_WAITINGROOM_MEMBER, new Member(ClientInfo.currentMember)));
			} catch (IOException e) {
				e.printStackTrace();
			} 
			break;
		case RESPONSE_LOGIN_FAIL:  
			if(message.getFrom() == null) {
				AlertFactory.createAlert(AlertType.ERROR, "���̵� �Ǵ� �н����带 Ȯ�����ּ���"); 				
			} else {
				AlertFactory.createAlert(AlertType.ERROR, "�̹� �α��ε� �����Դϴ�.");
			}
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
				Alert alert = new Alert(AlertType.ERROR, "������ �������ϴ�.");
				alert.setTitle("������ �������ϴ�");
				alert.setHeaderText("���� ����");
				alert.show();
			});
			System.exit(0);
			break;

		case RESPONSE_FORCEDEXIT:
			try {
				Platform.runLater( ()-> {
					Alert alert = new Alert(AlertType.CONFIRMATION, "������ ���� ���Ľ��׽��ϴ�!!!");
					alert.setTitle("������ ����������");
					ButtonType t1 = new ButtonType("Ȯ��");
					alert.getButtonTypes().setAll(t1);
					alert.setHeaderText("��������");
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
			break;
		case RESPONSE_ENTERROOM_PASSWORD:
			Platform.runLater( () -> {
				TextInputDialog tid = new TextInputDialog();

				tid.setTitle("��й�ȣ �Է�");
				tid.setHeaderText("��й�ȣ �Է� : ");
				tid.setContentText("��й�ȣ�� �Է��ϼ���");
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
						Alert alert = new Alert(AlertType.ERROR, "�߸��� ��й�ȣ �Դϴ�.");
						alert.setHeaderText("��й�ȣ ����");
						alert.setTitle("��й�ȣ ����");
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
				s.setTitle("�ʴ��ϱ�");
				s.setScene(scene);
				s.show();
				InviteController inviteCon = InviteController.getCon();	/** �ʴ��� ListView*/
				ObservableList<Member> memberList = FXCollections.observableArrayList(message.getMemberList());
				inviteCon.setListView(memberList);
			});
			break;
		case RESPONSE_INVITE_REQUEST:
			Platform.runLater( () -> {
				if(message.getTo().equals(ClientInfo.currentMember)) {
					Alert alert = new Alert(AlertType.CONFIRMATION, message.getFrom() + "�����κ��� " + message.getrInfo()+"���� �ʴ��մϴ�.");
					alert.setTitle(message.getFrom() + "���� �ʴ�");
					ButtonType bt1 = new ButtonType("����");
					ButtonType bt2 = new ButtonType("����");
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
					Alert alert = new Alert(AlertType.INFORMATION, message.getFrom() + "���� �ʴ븦 �����ϼ̽��ϴ�.");
					alert.setTitle(message.getFrom() + "���� ����");
					alert.setHeaderText("�� ��");
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
					stage.setTitle("���� ������");
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
					stage.setTitle(message.getTo()+"���� ������");
					stage.setScene(scene);
					stage.show();
				}
			});

			break;
		case RESPONSE_PROFILE_EDIT:
			if(message.getMsg().equals("true")) {
				ClientInfo.currentMember.setName(message.getProfile().getNickname());
				Platform.runLater( () -> {
					Alert alert = new Alert(AlertType.INFORMATION, "������Ʈ�� �����Ͽ����ϴ�.");
					alert.setHeaderText("�˸�");
					alert.setTitle("����");
					alert.showAndWait();
				});
			} else {
				Platform.runLater( () -> {
					Alert alert = new Alert(AlertType.INFORMATION, "������Ʈ�� �����Ͽ����ϴ�.");
					alert.setHeaderText("�˸�");
					alert.setTitle("����");
					alert.showAndWait();
				});
			}
			break;
		case RESPONSE_DELETE_ACCOUNT:
			if(message.getMsg().equals("true")) {
				Platform.runLater( () -> {
					Alert alert = new Alert(AlertType.INFORMATION, "���ݱ��� �̿��� �ּż� �����մϴ�.");
					alert.setContentText("������ ���������� �����Ǿ����ϴ�.");
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
					Alert alert = new Alert(AlertType.ERROR, "�ش� ������ ������ ���� �ʽ��ϴ�.");
					alert.setTitle("�߸��� ��û");
					alert.setHeaderText("����");
					alert.show();
				} else {
					Alert alert = new Alert(AlertType.ERROR, "�ش� �����ʹ� �̹� ģ���Դϴ�.");
					alert.setTitle("�߸��� ��û");
					alert.setHeaderText("����");
					alert.show();
				}
			});
			break;
		case RESPONSE_REMOVE_FRIEND:
			Platform.runLater( () -> {
				if(message.getMsg().equals("true") && message.getFrom().equals(ClientInfo.currentMember)) {
					Alert alert = new Alert(AlertType.ERROR, message.getTo() + "���� ���������� �����Ͽ����ϴ�.");
					alert.setTitle(ClientInfo.currentMember.toString());
					alert.setHeaderText("���� ����");
					alert.show();
					updateFriendList(message.getFriendList());
				}else if(message.getMsg().equals("true")) {
					Alert alert = new Alert(AlertType.ERROR, message.getFrom() + "�Կ��� ģ������ ���߽��ϴ�!");
					alert.setTitle(ClientInfo.currentMember.toString());
					alert.setHeaderText("���� �˸� ");
					alert.show();
					updateFriendList(message.getResponseFriendList());

				} else {
					Alert alert = new Alert(AlertType.ERROR, message.getTo() + "���� ������ �����Ͽ����ϴ�");
					alert.setTitle("������û");
					alert.setHeaderText("���� ����");
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
		if((message.getFrom().equals(ClientInfo.currentMember))) { // ������ ����̶��
			Alert alert = new Alert(AlertType.CONFIRMATION, message.getTo() + "�Կ��� ģ�� ��û�� �ϼ̽��ϴ�.");
			alert.setTitle("ģ����û");
			alert.setHeaderText("ģ����û");
			ButtonType bt1 = new ButtonType("Ȯ��");
			alert.getButtonTypes().setAll(bt1);
			alert.show();
		} else { // �޴»���̶��
			Alert alert = new Alert(AlertType.INFORMATION, "�����Ͻðڽ��ϱ�?");
			alert.setTitle("ģ����û");
			alert.setHeaderText(message.getFrom() + "�Կ��� ģ�� ��û�� �Խ��ϴ�.");
			ButtonType bt1 = new ButtonType("����");
			ButtonType bt2 = new ButtonType("����");
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
		if((message.getFrom().equals(ClientInfo.currentMember))) { // ������ ����̶��
			Alert alert = null;
			if(message.getMsg().equals("accept")) {
				alert = new Alert(AlertType.INFORMATION, message.getTo() + "���� ģ����û�� �����ϼ̽��ϴ�.");
				alert.setTitle("ģ������");
				alert.setHeaderText(message.getTo()+"�԰� ģ���� �Ǿ����ϴ�.");
				updateFriendList(message.getFriendList());
			} else {
				alert = new Alert(AlertType.INFORMATION, message.getTo() + "���� ģ����û�� �����ϼ̽��ϴ�.");
				alert.setTitle("ģ������");
				alert.setHeaderText(message.getTo()+"�԰� ģ���� ���� ���߽��ϴ�.");
			}
			ButtonType bt1 = new ButtonType("Ȯ��");
			alert.getButtonTypes().setAll(bt1);
			alert.showAndWait();
		} else { // �޴� ����̶��
			Alert alert = null;
			if(message.getMsg().equals("accept")) {
				alert = new Alert(AlertType.INFORMATION, message.getFrom() + "���� ģ����û�� �����ϼ̽��ϴ�.");
				alert.setTitle("ģ������");
				alert.setHeaderText(message.getFrom()+"�԰� ģ���� �Ǿ����ϴ�.");
				updateFriendList(message.getResponseFriendList());
			} else {
				alert = new Alert(AlertType.INFORMATION, message.getFrom() + "���� ģ����û�� �����ϼ̽��ϴ�.");
				alert.setTitle("ģ������");
				alert.setHeaderText(message.getFrom()+"�԰� ģ���� ���� ���߽��ϴ�.");
			}
			ButtonType bt1 = new ButtonType("Ȯ��");
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
				Alert alert = new Alert(AlertType.INFORMATION, "ã�� ���̵� �����ϴ�.");
				alert.setTitle("ID ã��");
				alert.setHeaderText("���� ����");
				alert.showAndWait();

			} else {
				Alert alert;
				if(message.getSv().getPassword()==null) {				
					alert = new Alert(AlertType.INFORMATION, "ID = "+responseStr);
					alert.setTitle("ID ã��");
					alert.setHeaderText("��ġ�ϴ� ���̵�");	

				} else {
					alert = new Alert(AlertType.INFORMATION, "PASSWORD = " + responseStr);
					alert.setTitle("PASSWORD ã��");
					alert.setHeaderText("��ġ�ϴ� ��й�ȣ");	
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
		/** �ӼӸ� ���� ����Ʈ*/
		ObservableList<Member> whisperList = FXCollections.observableArrayList();

		for(Member m :roomMemberList) {
			if(m.equals(ClientInfo.currentMember))
				continue;
			whisperList.add(m);
		}
		con.setWhisperView(whisperList);
	}

	private void updateInviteForm(Message msg) {
		InviteController inviteCon = InviteController.getCon();	/** �ʴ��� ListView*/
		ObservableList<Member> memberList = FXCollections.observableArrayList(msg.getMemberList());
		if(inviteCon!=null) {
			inviteCon.setListView(memberList);
		}
	}



}
