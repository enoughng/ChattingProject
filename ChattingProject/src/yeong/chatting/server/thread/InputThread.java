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
import yeong.chatting.server.dao.ServerDAO;
import yeong.chatting.server.main.MainController;
import yeong.chatting.util.Log;
import yeong.chatting.util.ProtocolType;

/**
 * 
 * ������ InputStream Ŭ����
 *
 */
public class InputThread implements Runnable{

	//

	//�α�������� MainController
	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;

	private boolean isBroadcast = false;
	private Message response;


	public static int ThreadCount; // Thread ���϶� ����ϴ� ThreadCount
	public static int memberCount; // �������ִ� �����

	public InputThread(Socket socket) {
		this.socket = socket;
		ThreadCount = ServerThread.COUNT++;
	}

	@Override
	public void run() {
		try {
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
			while(true) {	
				
				Message message = (Message)ois.readObject();
				Log.i(getClass(),message.getProtocol().toString());
				RequestCheck rc = new RequestCheck(message);
				response = rc.result();
				isBroadcast = rc.sendType();
				
				
				
				if(isBroadcast) {
					broadcastSend(response);
					
				} else {
					send(response);
				}
			}
			
		} catch(IOException e) {
			
			try {
				ServerThread.serverThreads.remove(this);
				ServerThread.COUNT--;
				if(ServerThread.isLogout)
					InputThread.memberCount--;
				
				Log.i(getClass(), "���� List ��� : " + ServerThread.serverThreads);
				
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
	 * ��ü �޽��� ������
	 * @throws IOException 
	 */
	private void broadcastSend(Message msg) throws IOException {
		for(InputThread t :ServerThread.serverThreads) {
			t.send(msg);
		}
	}
	/**
	 *  Message�� ������ ���
	 * @param msg
	 * @throws IOException 
	 */
	private void send(Message msg) throws IOException {
		oos.writeObject(msg);
	}
	
	
	@Override
	public String toString() {
		return "\n���� �����ּ�  : " + socket.getRemoteSocketAddress() + "\n��� ����Ʈ : " + ServerThread.memberList.toString() + "\n����ǰ��ִ� ThreadCount" + ThreadCount+"\n";
	}

}
