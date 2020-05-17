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
 * Ŭ���̾�Ʈ���� ��û�� �޽����� �м��Ͽ� �ٽ� �����ִ� Ŭ�����̴�.
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
			// ���� ����� ��
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
	 *  ���� ��ɵ�
	 *  
	 *  ���� ���α׷��� Log�����
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
	 * switch case ���� ���� �޼ҵ��
	 * 
	 * �α��� ��û�� ���� Select��ȸ �� ����
	 * 
	 * @throws SQLException
	 */
	private Message login() throws SQLException {
		Message response;
		ProtocolType responseProtocol;
		Member loginMember = sDao.checkLogin(request.getFrom()); // DB ��ȸ�� ���
		if (loginMember != null) { // ��ȸ�� ����� �ִٸ�
			appendLog(loginMember.getId() + "(" + loginMember.getName() + ")���� �α��� �ϼ̽��ϴ�.");
			responseProtocol = ProtocolType.RESPONSE_LOGIN_SUCCESS;
			ServerThread.memberList.add(loginMember);
			InputThread.memberCount++;
			ServerThread.isLogout=false;
			loginMember.setWaitingRoom(true);
			response = new Message(responseProtocol, loginMember);
			appendLog("���� �ο� : " + ServerThread.memberList + "\nMember size : " + InputThread.memberCount);
		} else {
			responseProtocol = ProtocolType.RESPONSE_LOGIN_FAIL;
			response = new Message(responseProtocol, request.getFrom());
		}

		return response;
	}

	/**
	 * �α׾ƿ�
	 */
	private Message logout() throws SQLException {

		ProtocolType responseProtocol = ProtocolType.RESPONSE_LOGOUT;
		ServerThread.isLogout =true;
		ServerThread.memberList.remove(--InputThread.memberCount);
		appendLog(request.getFrom().getId() + "���� �α׾ƿ� �ϼ̽��ϴ�.");
		appendLog("���� �ο� : " + ServerThread.memberList + "\n �ο��� : " +InputThread.memberCount);
		ServerThread.isLogout=true;

		Message response = new Message(responseProtocol, request.getFrom());
		response.setMemberList(new Vector<Member>(ServerThread.memberList));
		return  response;
	}

	/**	
	 * ȸ�����
	 */
	private Message registry() throws SQLException {
		Message response;

		sDao.insertMember(request.getFrom()); // �����ͺ��̽� insert�� ����
		responseProtocol = ProtocolType.RESPONSE_REGISTRY_SUCCESS;
		response = new Message(responseProtocol, request.getFrom());

		return response;
	}

	/**
	 * ���� �����ִ� ��� �� �� ��������ִ� ������
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
	 * �� �����
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
	 * ���� �� ������
	 */
}
