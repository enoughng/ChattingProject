package yeong.chatting.server.thread;

import java.awt.CheckboxGroup;
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
	
	private InputThread t;
	
	public RequestCheck(InputThread t, Message msg) throws SQLException, IOException {
		this.t = t;
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
		case REQUEST_IDCHECK:
			response = checkID();
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
		case REQUEST_CREATEROOM:
			response = createRoom();
			isBroadType = false;
			break;
		case REQUEST_ENTERROOM:
			response = enterRoom();
			isBroadType = false;
			break;
		case REQUEST_EXITROOM:
			isBroadType = false;
			response = exitRoom();
			break;
		case REQUEST_SEND:
			isBroadType = false;
			response = send();
			break;
		default:
			response = null;
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
			t.setCurrentID(loginMember.getId());
			appendLog(loginMember.getId() + "(" + loginMember.getName() + ")���� �α��� �ϼ̽��ϴ�.");
			responseProtocol = ProtocolType.RESPONSE_LOGIN_SUCCESS;
			ServerThread.memberList.add(loginMember);
			ServerThread.isLogout=false;
			response = new Message(responseProtocol, loginMember);
			appendLog("���� �ο� : " + ServerThread.serverThreads + "\nMember size : " + ServerThread.serverThreads.size());
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
		t.setCurrentID("");
		ServerThread.memberList.remove(request.getFrom());
		appendLog(request.getFrom().getId() + "���� �α׾ƿ� �ϼ̽��ϴ�.");
		appendLog("���� �ο� : " + ServerThread.memberList + "\n �ο��� : " +ServerThread.memberList.size());

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
		ProtocolType responseProtocol = ProtocolType.RESPONSE_WAITINGROOM_MEMBER;

		//���� �� �ο��� ����
		Message response = new Message(responseProtocol, request.getFrom(), new Vector<Member>(ServerThread.memberList)); // ���� ���
//		response.getFrom(); // ���� 
		
		Vector<RoomInfo> list = sDao.getRooms(); // ���� ������ DB�� ���ؼ� ������
		response.setRoomList(new Vector<RoomInfo>(list)); // ���� �� ������ Message�� �����
		return response;
	}

	/**
	 * �� �����
	 * 
	 * 1. �Է¹��� �������� �α��� ������ ���ؼ� DB�� ���� �����Ѵ�.
	 * 2. ������ �� ������ response�� ����ش�.
	 * 3. ���� ��Ͽ��� ��û�� ����� �������ش�. ( memberList.remove(request.geFrom())
	 * 4. ���ο� ����Ʋ ���� ��û�� ����� �־��ش�.
	 * 5. 4������ ���� ���͸� ServerThread.roomMemberList(�ؽ���)�� ���ȣ�� Ű������ �־��ش�.
	 * 6. 4������ ���� ���͸� Response������ �����ش�.
	 * 
	 * 
	 * @return result(DB roomInfo), from(��û�� ���), roomMemberList( rInfo�� �ش��ϴ� Vector�迭 )
	 * @throws SQLException
	 */
	private Message createRoom() throws SQLException { 
		RoomInfo result = sDao.insertRoom(request.getrInfo(), request.getFrom());
		if(result != null) {	
			response = new Message(ProtocolType.RESPONSE_CREATEROOM, request.getFrom());
			response.setrInfo(result);
			//���Ǹ�Ͽ��� �ش� ����� ����
			ServerThread.memberList.remove(request.getFrom());
			
			// ���� ���������� ����������� �ش� �ο��� ���� ���͸� �������ְ�, �ش� ���Ϳ� ���� ���� ����� �ְ� �̸� �޼����� ��Ƽ� �����ش�.
			Vector<Member> newList = new Vector<>();
			newList.add(request.getFrom());
			ServerThread.roomMemberList.put(result.getRoom_num(), newList);
			response.setRoomMemberList(newList);
		} else {
			response = new Message(ProtocolType.RESPONSE_CREATEROOM_FAIL, request.getFrom());
		}
		return response;
	}

	/**
	 * �� ����
	 * 
	 * ��û�� : ������ �� ����, ��û�� ���
	 * 1. ������ �� ������ ���� DB�� �������� �����´�.
	 * 2. ��û�� ����� ���� �������Ʈ���� �����Ѵ�.
	 * 3. DB���� ��ȸ�� �� ��ȣ�� ������ roomMemberList���� Vector�迭�� �����´�.
	 * 4. 3������ Vector�� ���� ������ �־��ش�.
	 * 5. response�� Vector�� �־��ش�.
	 * 6. DB���� ��ȸ�� �� ������ �ִ´�.
	 */
	private Message enterRoom() throws SQLException {

		Member from = request.getFrom();
		RoomInfo requestRoomInfo = request.getrInfo();	
		/* ���õ� ���� ���� ��*/
		if(requestRoomInfo == null) {
			responseProtocol = ProtocolType.RESPONSE_ENTERROOM_FAIL;
			Message response = new Message(responseProtocol, from);
			return response;
		}
		/* ��û�� �������� �̿��Ͽ� DB���� �ִ��� ��ȸ */
		RoomInfo checkedRoomInfo = sDao.hasRoom(requestRoomInfo);
		ProtocolType responseProtocol;
		/* ������ success ������ fail�� ���� */
		if(checkedRoomInfo != null) {
			responseProtocol = ProtocolType.RESPONSE_ENTERROOM_SUCCESS;
		} else {
			responseProtocol = ProtocolType.RESPONSE_ENTERROOM_FAIL;
		}
		Message response = new Message(responseProtocol, from);
		ServerThread.memberList.remove(request.getFrom());
		//��û�� ����� ���� �� �����Ϳ� �־��ش�.
		Vector<Member> roomMemberList = ServerThread.roomMemberList.get(checkedRoomInfo.getRoom_num());
		roomMemberList.add(request.getFrom());
		// index���� �̿��Ͽ� ServerThread�� �ִ� Map�� ��ȸ�Ͽ� �ش� ���ȣ�� �����ϴ� Vector�� ��ȯ���ش�.
		response.setRoomMemberList(new Vector<Member>(roomMemberList));
		response.setrInfo(checkedRoomInfo); // RoomInfo
		return response;
	}

	/**
	 * ������ �۾� �������� ������ ���� ���ٸ�? 
	 * ���� �����ϴ� ������ �ۼ� �� �����ڵ� ���� ����
	 * ��û�ڿ� ������ �ٸ��ٸ�? ��Ͽ��� �������ϰ� ȭ����ȯ
	 * 1. �Էµ� �������� ���� DB�� �������� �����´�.
	 * 2. ��û�� ����� DB�� �������� ������ ���ٸ� ���� �����Ѵ�.
	 * 2-1 �׷��� �ʴٸ� Map�� �ִ� ����� ��û�� ����� �����Ѵ�.
	 * 3. ���ǿ� ���� ����� �־��ش�.
	 * 4. ��������� DB��ȸ���� �������� Message�� �־��ش�.
	 */
	private Message exitRoom() throws SQLException {
		Message response = null;
		ProtocolType responseProtocol;

		RoomInfo DBrInfo = sDao.hasRoom(request.getrInfo());

		
		if(DBrInfo.getRoom_host().equals(request.getFrom().getName())) { //�����»���� �����̸� ������
			isBroadType = false;
			responseProtocol = ProtocolType.RESPONSE_EXITROOM_HOST;
			sDao.deleteRoom(DBrInfo);
		} else { // �����»���� ������ �ƴϸ� �� ������
			isBroadType = false;
			responseProtocol = ProtocolType.RESPONSE_EXITROOM;
			ServerThread.roomMemberList.get(DBrInfo.getRoom_num()).remove(request.getFrom());
		}
		
		ServerThread.memberList.add(request.getFrom()); // ��û�Ȱ� �����ο����� �߰�
		response = new Message(responseProtocol, request.getFrom(), new Vector<Member>(ServerThread.memberList)); // �������
		response.setrInfo(new RoomInfo(DBrInfo)); 
		Vector<RoomInfo> list = sDao.getRooms(); // ���� ������ DB�� ���ؼ� ������
		response.setRoomList(list); // ������ 
//		response.setRoomMemberList(new Vector(list);
		return response;
	}
	
	/**
	 * ������ �ִ� �������� ���������, MSG���� �Է¹޾� �濡 �������ִ� ������� �����ִ� �޼ҵ��̴�.
	 */

	private Message send() throws SQLException {
		ProtocolType responseProtocol = ProtocolType.RESPONSE_SEND;
		RoomInfo dbRoomInfo = sDao.hasRoom(request.getrInfo());
		// �������ݰ� �������� �ٲ��ش�.
		request.setProtocol(responseProtocol);
		request.setrInfo(dbRoomInfo);
		return request;
	}
	
	private Message checkID() throws SQLException {
		ProtocolType responseProtocol = ProtocolType.RESPONSE_IDCHECK;
		String isHave = sDao.checkID(request.getMsg());

		Message response = new Message(responseProtocol,isHave);
		response.setFrom(new Member(request.getMsg(),null));
		return response;
	}
}
