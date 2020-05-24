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
 * 서버의 InputStream 클래스
 *
 */
public class InputThread implements Runnable {

	// InputThread를 구별하기 위한 식별자
	private int inputThreadID;

	// 현재 접속한 유저의 이름을 저장하기위한 변수
	private String currentMemberID="";
	
	
	// 로그찍기위한 MainController
	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;

	private boolean isBroadcast = false;
	private Message request;
	private Message response;

	private ServerDAO sDao;

	public static int ThreadCount; // Thread 죽일때 사용하는 ThreadCount

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
				Log.e("클라이언트와 연결이 끊겼습니다.");
			} catch (IOException e1) {
				Log.e("연결이 끊긴 소켓과 연결 끊기 실패");
			}
		} catch (ClassNotFoundException e) {
			Log.e(getClass(), "해당 클래스를 찾을 수 없습니다. ", e);
		} catch (SQLException e) {
			Log.e(getClass(), e);
		}
	}
	

	/**
	 * Message를 보내는 기능
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
	 * 현재 접속해있는 멤버의 친구목록을 업데이트 시키는 구문이다.
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
	 * 자기자신을 제외한 같은방의 모든 멤버에게 보내라
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
	 * 같은 채팅방에 있는 모든 멤버에게 메시지 전달
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
	 * 귓속말 즉, To에게만 보내는 메세지
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
	 * 전체 메시지 보내기
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
	 * 방목록, 멤버정보를 담아야한다.
	 * 
	 * @throws IOException
	 * @throws SQLException
	 */
	private void updateWaitingRoom(Message request) throws IOException, SQLException {

		Message response = null;
		ProtocolType responseProtocol = ProtocolType.RESPONSE_UPDATEWAITINGROOM;

		// 1. 담아줄 방목록을 DB에서 가져온다.
		Vector<RoomInfo> rooms = sDao.getRooms();

		// 2. 담아줄 멤버정보를 ServerThread에서 가져온다.
		Vector<Member> memberList = ServerThread.memberList;
		
		// 2.5 담아줄 친구목록을 DB에서 갖고온다.
		Vector<Member> friendList = sDao.friendList(request.getFrom());

		// 3. 메세지에 실어서 보낸다.
		response = new Message(responseProtocol, new Member("Broadcast Server", null));
		response.setRoomList(new Vector<RoomInfo>(rooms));
		response.setMemberList(new Vector<Member>(memberList));
		response.setFriendList(new Vector<Member>(friendList));
		
		broadcastSend(response);
	}
	

	/**
	 * roomMemberList(Vector)를 담으면 된다.
	 * 
	 * @throws SQLException
	 */
	private void updateChattingRoom(Message request) throws IOException, SQLException {
		Message response = null;
		ProtocolType responseProtocol = ProtocolType.RESPONSE_UPDATECHATTINGROOM;

		// 1. 요청된 request의 rInfo를 받아온다.
		RoomInfo requestRInfo = request.getrInfo();
		RoomInfo dbRoomInfo = sDao.hasRoom(requestRInfo);
		// 2. 요청된 request의 rInfo의 index번호로 Map에있는 Vector를 가져온다.
		Vector<Member> roomMemberList = ServerThread.roomMemberList.get(requestRInfo.getRoom_num());
		// 3. roomMemberList를 Message에 담아준다.
		response = new Message(responseProtocol, new Member("Broadcast Server", null));
		response.setRoomMemberList(new Vector<Member>(roomMemberList));
		response.setrInfo(dbRoomInfo);

		// 4. 브로드캐스트로 전송한다.
		broadcastSend(response);
	}

	@Override
	public String toString() {
		return currentMemberID;
	}

}
