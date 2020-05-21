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


	private ServerDAO sDao;

	private Message request;
	private Message response;

	ProtocolType responseProtocol = null;

	private boolean isBroadType = false;
	
	private InputThread t;
	
	private MainController controller = MainController.getMainController();

	
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
			break;
		case REQUEST_IDCHECK:
			response = checkID();
			break;
		case REQUEST_REGISTRY:
			response = registry();
			break;
		case REQUEST_WAITINGROOM_MEMBER:
			response = getMembers();
			isBroadType = true;
			break;
		case REQUEST_LOGOUT:
			response = logout();
			break;
		case REQUEST_CREATEROOM:
			response = createRoom();
			break;
		case REQUEST_ENTERROOM:
			response = enterRoom();
			break;
		case REQUEST_EXITROOM:
			response = exitRoom();
			break;
		case REQUEST_SEND:
			response = send();
			break;
		case REQUEST_WHISPER:
			response = whisper();
			break;
		case REQUEST_INVITE:
			response = invite();
			break;
		case REQUEST_INVITEUPDATE:
			response = inviteUpdate();
			break;
		case RESPONSE_INVITE_REJECT:
			response = reject();
		default:
			response = request;
			
		}

		return response;
	}

	public boolean sendType() {
		return isBroadType;
	}
	
	/**
	 * ����UI�� �α����
	 * @param msg
	 */
	private void appendLog(String msg) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
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
	 * @throws SQLExcep8tion
	 */
	private Message login() throws SQLException {
		Message response;
		ProtocolType responseProtocol;
		Member loginMember = sDao.checkLogin(request.getFrom()); // DB ��ȸ�� ���
		if (loginMember != null && loginMember.getLogin().equals("N")) { // ��ȸ�� ����� �����鼭 �� ����� �α����� ���°� �ƴ϶��
			
			sDao.updateLogin(loginMember, true);
			t.setCurrentID(loginMember.getId()); // InputThread�� �̸��� ���� �α����� ����� ID�� �����Ѵ�.
			responseProtocol = ProtocolType.RESPONSE_LOGIN_SUCCESS; // ���� �޽����� �������� Ÿ���� �����ش�.
			ServerThread.memberList.add(loginMember); // ���������忡 �ִ� �������Ʈ�� �����ش�.
			ServerThread.isLogout=false; // ���������� �α��λ��º����� �α��λ��·� ������ش�.
			response = new Message(responseProtocol, loginMember); // ����޽����� �����.
			
			appendLog(loginMember.getId() + "(" + loginMember.getName() + ")���� �α��� �ϼ̽��ϴ�."); // �α����� �α����� ����ش�.
		} else {
			responseProtocol = ProtocolType.RESPONSE_LOGIN_FAIL;
			response = new Message(responseProtocol, loginMember);
		}

		return response;
	}

	/**
	 * �α׾ƿ�
	 */
	private Message logout() throws SQLException {

		ProtocolType responseProtocol = ProtocolType.RESPONSE_LOGOUT;
		sDao.updateLogin(request.getFrom(), false);
		ServerThread.isLogout =true;
		t.setCurrentID("");
		ServerThread.memberList.remove(request.getFrom());
		Message response = new Message(responseProtocol, request.getFrom());
		response.setMemberList(new Vector<Member>(ServerThread.memberList));
		
		appendLog(request.getFrom().getId() + "(" + request.getFrom().getName() +")"+ " ���� �α׾ƿ� �ϼ̽��ϴ�.");
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
		
		appendLog(request.getFrom().getId() + "(" + request.getFrom().getName() + ") ���� ȸ������� �ϼ̽��ϴ�.");
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
			appendLog(request.getFrom().getId() + "(" + request.getFrom().getName() + "���� ("+result.getRoom_num()+"��) "+result.getRoom_title()+" ���� ����̽��ϴ�.");
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
		/* ��й�ȣ�� �ʿ��ϸ� ��й�ȣ�� �ʿ��ϴٰ� ����~ */
		if(checkedRoomInfo != null && checkedRoomInfo.isChk() && !(requestRoomInfo.getRoom_pwd().equals(checkedRoomInfo.getRoom_pwd()))) {
			responseProtocol = ProtocolType.RESPONSE_ENTERROOM_PASSWORD;
			Message response = new Message(responseProtocol, from, checkedRoomInfo);
			return response;
		} 
		ProtocolType responseProtocol = ProtocolType.RESPONSE_ENTERROOM_SUCCESS;
		Message response = new Message(responseProtocol, from);
		ServerThread.memberList.remove(request.getFrom());
		//��û�� ����� ���� �� �����Ϳ� �־��ش�.
		Vector<Member> roomMemberList = ServerThread.roomMemberList.get(checkedRoomInfo.getRoom_num());
		roomMemberList.add(request.getFrom());
		// index���� �̿��Ͽ� ServerThread�� �ִ� Map�� ��ȸ�Ͽ� �ش� ���ȣ�� �����ϴ� Vector�� ��ȯ���ش�.
		response.setRoomMemberList(new Vector<Member>(roomMemberList));
		response.setrInfo(checkedRoomInfo); // RoomInfo
		appendLog(request.getFrom().getId() + "(" + request.getFrom().getName() + ") ���� ["+checkedRoomInfo.getRoom_num()+"��] "+checkedRoomInfo.getRoom_title()+"�� �����ϼ̽��ϴ�.");
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
		
		if(DBrInfo.getRoom_host().equals(request.getFrom().getId())) { //�����»���� �����̸� ������
			isBroadType = false;
			responseProtocol = ProtocolType.RESPONSE_EXITROOM_HOST;
			sDao.deleteRoom(DBrInfo);
			appendLog(request.getFrom().getId() + "(" + request.getFrom().getName() + "���� ("+DBrInfo.getRoom_num()+"��) "+DBrInfo.getRoom_title()+" ���� �ı��ϼ̽��ϴ�.");

		} else { // �����»���� ������ �ƴϸ� �� ������
			isBroadType = false;
			responseProtocol = ProtocolType.RESPONSE_EXITROOM;
			ServerThread.roomMemberList.get(DBrInfo.getRoom_num()).remove(request.getFrom());
			appendLog(request.getFrom().getId() + "(" + request.getFrom().getName() + "���� ("+DBrInfo.getRoom_num()+"��) "+DBrInfo.getRoom_title()+" �濡�� �����̽��ϴ�.");

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
//		appendLog(request.getFrom().getId() + "(" + request.getFrom().getName() + "���� ("+request.getrInfo().getRoom_num()+"��) "+request.getrInfo()+" �濡�� ä���ϼ̽��ϴ�.");
		return request;
		
	}
	
	private Message whisper() throws SQLException {
		ProtocolType responseProtocol = ProtocolType.RESPONSE_WHISPER;
		Message response = new Message(request);
		response.setProtocol(responseProtocol);
		
		return response;
	}
	
	
	private Message checkID() throws SQLException {
		ProtocolType responseProtocol = ProtocolType.RESPONSE_IDCHECK;
		String isHave = sDao.checkID(request.getMsg());

		Message response = new Message(responseProtocol,isHave);
		response.setFrom(new Member(request.getMsg(),null));
		return response;
	}
	
	private Message invite() {
		ProtocolType responseProtocol = ProtocolType.RESPONSE_INVITE_REQUEST;
		Message response = new Message(request);
		response.setProtocol(responseProtocol);
		return response;
	}
	
	private Message inviteUpdate() {
		Message response = new Message(request);
		response.setProtocol(ProtocolType.RESPONSE_INVITEUPDATE);
		response.setMemberList(new Vector<Member>(ServerThread.memberList));
		return response;
	}
	
	private Message reject() {
		Message response = request;
		return response;
	}
}
