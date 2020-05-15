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
	
	
	public RequestCheck(Message msg) throws SQLException, IOException {
		request = msg;
	}
	
	public Message result() throws SQLException {
		sDao = ServerDAO.getInstance();

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
			InputThread.memberCount++;
			Log.i("memberCount : " + InputThread.memberCount);
			appendLog(loginMember.getId() + "(" + loginMember.getName() + ")���� �α��� �ϼ̽��ϴ�.");
			responseProtocol = ProtocolType.RESPONSE_LOGIN_SUCCESS;
			InputThread.memberList.add(loginMember);
			response = new Message(responseProtocol, loginMember);
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
		InputThread.memberList.remove(--InputThread.memberCount);
		appendLog(request.getFrom().getId() + "���� �α׾ƿ� �ϼ̽��ϴ�.");
		
		Message response = new Message(responseProtocol, request.getFrom());
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
	 * ���� �����ִ� ��� ��
	 * @return
	 * @throws SQLException
	 */
	private Message getMembers() throws SQLException {
		ProtocolType responseProtocol = ProtocolType.RESPONSE_WAITINGROOM_MEMBER;
		Message response = new Message(responseProtocol, request.getFrom());
		response.setMemberList(InputThread.memberList);
		Log.i(response.getMemberList());
		return response;
	}
	
}
