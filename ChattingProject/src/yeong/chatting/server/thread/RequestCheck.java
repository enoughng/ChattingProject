package yeong.chatting.server.thread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import javafx.application.Platform;
import yeong.chatting.model.Member;
import yeong.chatting.model.Message;
import yeong.chatting.model.RoomInfo;
import yeong.chatting.server.dao.ServerDAO;
import yeong.chatting.server.main.MainController;
import yeong.chatting.util.Log;
import yeong.chatting.util.ProtocolType;

/**
 * 클라이언트에서 요청된 메시지를 분석하여 다시 보내주는 클래스이다.
 * 
 * @author Yeong
 *
 */

public class RequestCheck {

	private MainController controller = MainController.getMainController();

	private ServerDAO sDao;

	private Message request;
	private Message response;

	ProtocolType responseProtocol = null;

	private boolean isBroadType = false;

	private boolean isWaitingRoom = false;

	public RequestCheck(Message msg) throws SQLException, IOException {
		request = msg;
		sDao = ServerDAO.getInstance();
	}

	public Message result() throws SQLException {
		switch (request.getProtocol()) {
		case REQUEST_LOGIN:
			// 현재 사용자 수
			response = login();
			isBroadType = false;
			break;
		case REQUEST_REGISTRY:
			response = registry();
			isBroadType = false;
			break;
		case REQUEST_WAITINGROOM_MEMBER:
			response = getMembers();
			isBroadType = true;
			break;
		case REQUEST_LOGOUT:
			response = logout();
			isBroadType = false;
			break;
		default:
		case REQUEST_CREATEROOM:
			response = createRoom();
			isBroadType = false;
		}

		return response;
	}

	public boolean sendType() {
		return isBroadType;
	}

	/** 
	 *  공통 기능들
	 *  
	 *  서버 프로그램에 Log남기기
	 */
	private void appendLog(String msg) {

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String time = sdf.format(Calendar.getInstance().getTime());

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				controller.getLog().appendText("[" + time + "] " + msg + "\n");
			}
		});
	}

	/**
	 * switch case 문에 따른 메소드들
	 * 
	 * 로그인 요청에 따른 Select조회 및 리턴
	 * 
	 * @throws SQLException
	 */
	private Message login() throws SQLException {
		Message response;
		ProtocolType responseProtocol;
		Member loginMember = sDao.checkLogin(request.getFrom()); // DB 조회한 멤버
		if (loginMember != null) { // 조회한 멤버가 있다면
			appendLog(loginMember.getId() + "(" + loginMember.getName() + ")님이 로그인 하셨습니다.");
			responseProtocol = ProtocolType.RESPONSE_LOGIN_SUCCESS;
			ServerThread.memberList.add(loginMember);
			InputThread.memberCount++;
			ServerThread.isLogout=false;
			loginMember.setWaitingRoom(true);
			response = new Message(responseProtocol, loginMember);
			appendLog("현재 인원 : " + ServerThread.memberList + "\nMember size : " + InputThread.memberCount);
		} else {
			responseProtocol = ProtocolType.RESPONSE_LOGIN_FAIL;
			response = new Message(responseProtocol, request.getFrom());
		}

		return response;
	}

	/**
	 * 로그아웃
	 */
	private Message logout() throws SQLException {

		ProtocolType responseProtocol = ProtocolType.RESPONSE_LOGOUT;
		ServerThread.isLogout =true;
		ServerThread.memberList.remove(--InputThread.memberCount);
		appendLog(request.getFrom().getId() + "님이 로그아웃 하셨습니다.");
		appendLog("현재 인원 : " + ServerThread.memberList + "\n 인원수 : " +InputThread.memberCount);
		ServerThread.isLogout=true;

		Message response = new Message(responseProtocol, request.getFrom());
		response.setMemberList(new Vector<Member>(ServerThread.memberList));
		return  response;
	}

	/**	
	 * 회원등록
	 */
	private Message registry() throws SQLException {
		Message response;

		sDao.insertMember(request.getFrom()); // 데이터베이스 insert문 삽입
		responseProtocol = ProtocolType.RESPONSE_REGISTRY_SUCCESS;
		response = new Message(responseProtocol, request.getFrom());

		return response;
	}

	/**
	 * 현재 들어와있는 멤버 수 및 만들어져있는 방정보
	 * @return
	 * @throws SQLException
	 */
	private Message getMembers() throws SQLException {
		isWaitingRoom = true;
		ProtocolType responseProtocol = ProtocolType.RESPONSE_WAITINGROOM_MEMBER;
		Vector<RoomInfo> list = sDao.getRooms();
		Message response = new Message(responseProtocol, request.getFrom(), new Vector<Member>(ServerThread.memberList));
		response.getFrom().setWaitingRoom(isWaitingRoom);
		response.setRoomList(list);
		return response;
	}

	/**
	 * 방 만들기
	 * 
	 * @return
	 * @throws SQLException
	 */
	private Message createRoom() throws SQLException { 
		isWaitingRoom = false;
		RoomInfo result = sDao.insertRoom(request.getrInfo());
		if(result != null) {
			response = new Message(ProtocolType.RESPONSE_CREATEROOM, request.getFrom());
			response.setrInfo(result);
		} else {
			response = new Message(ProtocolType.RESPONSE_CREATEROOM_FAIL, request.getFrom());
		}


		return response;
	}


	/**
	 * 현재 방 정보들
	 */
}
