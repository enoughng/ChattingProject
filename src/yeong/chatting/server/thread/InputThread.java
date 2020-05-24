package yeong.chatting.server.thread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.sound.midi.ControllerEventListener;

import javafx.application.Platform;
import yeong.chatting.model.Member;
import yeong.chatting.model.Message;
import yeong.chatting.model.RoomInfo;
import yeong.chatting.server.dao.ServerDAO;
import yeong.chatting.server.main.MainController;
import yeong.chatting.util.Log;
import yeong.chatting.util.ProtocolType;

/**
 * 
 * ������ InputStream Ŭ����
 *
 */
public class InputThread implements Runnable {

	// InputThread�� �����ϱ� ���� �ĺ���
	private int inputThreadID;

	// ���� ������ ������ �̸��� �����ϱ����� ����
	private String currentMemberID="";
	
	
	// �α�������� MainController
	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;

	private boolean isBroadcast = false;
	private Message request;
	private Message response;

	private ServerDAO sDao;

	public static int ThreadCount; // Thread ���϶� ����ϴ� ThreadCount

	public InputThread(Socket socket, int inputThreadID) {
		this.socket = socket;
		this.inputThreadID = inputThreadID;
		sDao = ServerDAO.getInstance();

	}

	@Override
	public void run() {
		try {
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
			while (true) {

				request = (Message) ois.readObject();
				RequestCheck rc = new RequestCheck(this, request);
				response = rc.result();
				isBroadcast = rc.sendType();
				
				if (isBroadcast) {
					broadcastSend(response);
				} else {
					send(response);
				}

				switch (response.getProtocol()) {
				case RESPONSE_CREATEROOM:
					updateWaitingRoom(request);
					break;
				case RESPONSE_ENTERROOM_SUCCESS:
					updateChattingRoom(request);
					updateWaitingRoom(request);
					break;
				case RESPONSE_EXITROOM:
					updateChattingRoom(request);
					updateWaitingRoom(request);
					break;
				case RESPONSE_EXITROOM_HOST:
					response.setProtocol(ProtocolType.RESPONSE_WAITINGROOM_MEMBER);
					exitBrokenRoom(response);
					updateWaitingRoom(request);
					break;
				case RESPONSE_SEND:
					chattingRoomSend(response);
					break;
				case RESPONSE_WHISPER:
					chattingRoomWhisper(response);
					break;
				case RESPONSE_INVITE_REQUEST:
					waitingRoomInvite(response);
				break;
				case RESPONSE_INVITE_REJECT:
					rejectSend(response);
					break;
				case RESPONSE_PROFILE_EDIT:
					if(response.getrInfo()!=null) {
						updateChattingRoom(response);
						updateWaitingRoom(response);
					} else if(response.getMemberList() != null){
						updateWaitingRoom(response);
					}
					break;
				case RESPONSE_ADD_FRIEND_REQUEST:
					chattingRoomWhisper(response);
					break;
				case RESPONSE_ADD_FRIEND_RESPONSE:
				case RESPONSE_REMOVE_FRIEND:
					chattingRoomWhisper(response);
					break;
				case RESPONSE_LOGIN_SUCCESS:
				case RESPONSE_LOGOUT:
					updateFriendListMember(response);
					break;
				case RESPONSE_DELETE_ACCOUNT:
					updateFriendListMember(response);
					break;
				default:
				}

			}

		} catch (IOException e) {

			try {
				ServerThread.serverThreads.remove(this);
				if (request != null)
					ServerThread.memberList.remove(request.getFrom());

				socket.close();
				Log.e("Ŭ���̾�Ʈ�� ������ ������ϴ�.");
			} catch (IOException e1) {
				Log.e("������ ���� ���ϰ� ���� ���� ����");
			}
		} catch (ClassNotFoundException e) {
			Log.e(getClass(), "�ش� Ŭ������ ã�� �� �����ϴ�. ", e);
		} catch (SQLException e) {
			Log.e(getClass(), e);
		}
	}
	

	/**
	 * Message�� ������ ���
	 * 
	 * @param msg
	 * @throws IOException
	 */
	public void send(Message response) throws IOException {
		oos.writeObject(response);
	}

	public int getInputThreadID() {
		return inputThreadID;
	}

	public String getCurrentID() {
		return currentMemberID;
	}

	public void setCurrentID(String ID) {
		currentMemberID = ID;
	}
	
	
	
	/**
	 * ���� �������ִ� ����� ģ������� ������Ʈ ��Ű�� �����̴�.
	 * @throws SQLException 
	 * @throws IOException 
	 */
	private void updateFriendListMember(Message msg) throws SQLException, IOException {
		Vector<Member> friendList = msg.getFriendList();
		for(Member m :friendList) {
			if(m.getIsLogin().equals("Y")) {
				for(InputThread t :ServerThread.serverThreads) {
					if(t.currentMemberID.equals(m.getId())) {
						Vector<Member> friendResponseList = sDao.friendList(m);
						msg.setResponseFriendList(friendResponseList);
						msg.setProtocol(ProtocolType.UPDATE_FRIEND_LIST);
						t.send(msg);
					}
				}
			}
		}
	}
	
	

	/**
	 * �ڱ��ڽ��� ������ �������� ��� ������� ������
	 * @throws SQLException 
	 */
	private void exitBrokenRoom(Message response) throws IOException, SQLException {
		Vector<Member> roomMemberList = ServerThread.roomMemberList.get(response.getrInfo().getRoom_num());
		for (Member m : roomMemberList) {
			if (m.getId().equals(getCurrentID()))
				continue;
			for (InputThread t : ServerThread.serverThreads) {
				if (m.getId().equals(t.getCurrentID())) {
					response.setProtocol(ProtocolType.RESPONSE_FORCEDEXIT);
					ServerThread.memberList.add(m);
					response.setMemberList(new Vector<Member>(ServerThread.memberList));
					response.setFriendList(sDao.friendList(m));
					t.send(response);
				}
			}
		}
	}

	/**
	 * ���� ä�ù濡 �ִ� ��� ������� �޽��� ����
	 */
	private void chattingRoomSend(Message response) throws IOException {
		Vector<Member> roomMemberList = ServerThread.roomMemberList.get(response.getrInfo().getRoom_num());
		for (Member m : roomMemberList) {
			if (m.getId().equals(getCurrentID()))
				continue;

			for (InputThread t : ServerThread.serverThreads) {
				if (m.getId().equals(t.getCurrentID())) {
					t.send(response);
				}
			}
		}
	}

	/**
	 * �ӼӸ� ��, To���Ը� ������ �޼���
	 */
	private void chattingRoomWhisper(Message response) throws IOException {
		for(InputThread t:ServerThread.serverThreads) {
			if(response.getTo().getId().equals(t.currentMemberID))
				t.send(response);
		}
	}


	private void waitingRoomInvite(Message response) throws IOException {
		for(InputThread t:ServerThread.serverThreads) {
			if(response.getTo().getId().equals(t.currentMemberID))
				t.send(response);
		}
	}
	

	/**
	 * ��ü �޽��� ������
	 * 
	 * @throws IOException
	 */
	private void broadcastSend(Message response) throws IOException {
		for (InputThread t : ServerThread.serverThreads) {
			t.send(response);
		}
	}
	
	private void rejectSend(Message response) throws IOException {
		if(response.getProtocol() == ProtocolType.RESPONSE_INVITE_REJECT) {
			chattingRoomWhisper(response);
			return;
		}
	}

	/**
	 * ����, ��������� ��ƾ��Ѵ�.
	 * 
	 * @throws IOException
	 * @throws SQLException
	 */
	private void updateWaitingRoom(Message request) throws IOException, SQLException {

		Message response = null;
		ProtocolType responseProtocol = ProtocolType.RESPONSE_UPDATEWAITINGROOM;

		// 1. ����� ������ DB���� �����´�.
		Vector<RoomInfo> rooms = sDao.getRooms();

		// 2. ����� ��������� ServerThread���� �����´�.
		Vector<Member> memberList = ServerThread.memberList;
		
		// 2.5 ����� ģ������� DB���� ����´�.
		Vector<Member> friendList = sDao.friendList(request.getFrom());

		// 3. �޼����� �Ǿ ������.
		response = new Message(responseProtocol, new Member("Broadcast Server", null));
		response.setRoomList(new Vector<RoomInfo>(rooms));
		response.setMemberList(new Vector<Member>(memberList));
		response.setFriendList(new Vector<Member>(friendList));
		
		broadcastSend(response);
	}
	

	/**
	 * roomMemberList(Vector)�� ������ �ȴ�.
	 * 
	 * @throws SQLException
	 */
	private void updateChattingRoom(Message request) throws IOException, SQLException {
		Message response = null;
		ProtocolType responseProtocol = ProtocolType.RESPONSE_UPDATECHATTINGROOM;

		// 1. ��û�� request�� rInfo�� �޾ƿ´�.
		RoomInfo requestRInfo = request.getrInfo();
		RoomInfo dbRoomInfo = sDao.hasRoom(requestRInfo);
		// 2. ��û�� request�� rInfo�� index��ȣ�� Map���ִ� Vector�� �����´�.
		Vector<Member> roomMemberList = ServerThread.roomMemberList.get(requestRInfo.getRoom_num());
		// 3. roomMemberList�� Message�� ����ش�.
		response = new Message(responseProtocol, new Member("Broadcast Server", null));
		response.setRoomMemberList(new Vector<Member>(roomMemberList));
		response.setrInfo(dbRoomInfo);

		// 4. ��ε�ĳ��Ʈ�� �����Ѵ�.
		broadcastSend(response);
	}

	@Override
	public String toString() {
		return currentMemberID;
	}

}
