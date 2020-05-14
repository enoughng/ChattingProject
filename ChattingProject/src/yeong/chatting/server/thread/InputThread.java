package yeong.chatting.server.thread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;

import yeong.chatting.model.Member;
import yeong.chatting.model.Message;
import yeong.chatting.server.dao.ServerDAO;
import yeong.chatting.util.Log;
import yeong.chatting.util.ProtocolType;

/**
 * 
 * ������ InputStream Ŭ����
 *
 */
public class InputThread {
	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	
	private Member currentMember;
	
	private boolean isStop = false;

	private ServerDAO sDao;
	
	public InputThread(Socket socket) {
		this.socket = socket;
	    sDao = ServerDAO.getInstance();
		init();
	}

	private void init() {
		Runnable run = new Runnable() {
			@Override
			public void run() {
				try {
					ois = new ObjectInputStream(socket.getInputStream());
					oos = new ObjectOutputStream(socket.getOutputStream());
					while(!isStop) {	
						Log.i(getClass(),"��ü ������");
						Message message = (Message)ois.readObject();
						checkMessage(message);
					}
				} catch(IOException e) {
					try {
						ServerThread.serverThreads.remove(this);
						socket.close();
					} catch (IOException e1) {
						Log.e("������ ���� ���ϰ� ���� ���� ����");
					}
					Log.e("Ŭ���̾�Ʈ�� ������ ������ϴ�.");	
				} catch (ClassNotFoundException e) {
					Log.e(getClass(), "�ش� Ŭ������ ã�� �� �����ϴ�. ", e);
				} catch (SQLException e) {
					Log.e(getClass(), e);
				}
			}
		};
		ServerThread.getThreadPool().execute(run);
	}
	
	
	private void checkMessage(Message msg) throws SQLException, IOException {
		Message response = null;
		ProtocolType responseProtocol = null;
		switch(msg.getProtocol()) {
		case REQUEST_LOGIN: 
			boolean hasLogin = sDao.checkLogin(msg.getFrom());
			if(hasLogin) {
				responseProtocol=ProtocolType.RESPONSE_LOGIN_SUCCESS;
			} else {
				responseProtocol=ProtocolType.RESPONSE_LOGIN_FAIL;
			}
			break;
		case REQUEST_REGISTRY:
			sDao.insertMember(msg.getFrom());
			responseProtocol=ProtocolType.RESPONSE_REGISTRY_SUCCESS;
		case REQUEST_WAITINGROOM_MEMBER:
			responseProtocol=ProtocolType.RESPONSE_WAITINROOM_MEMBER;
			sendList();
			break;
		default:
		}
		currentMember = msg.getFrom();
		response = new Message.mBuilder(responseProtocol, msg.getFrom()).build();
		oos.writeObject(response);
	}
	
	
	private void sendList() {
		for(InputThread t : ServerThread.serverThreads) {
			
		}
	}
	
	
	private Member getMember() {
		return currentMember;
	}

}
