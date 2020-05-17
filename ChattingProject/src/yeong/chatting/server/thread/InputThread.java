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
 * 서버의 InputStream 클래스
 *
 */
public class InputThread implements Runnable{

	//

	//로그찍기위한 MainController
	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;

	private boolean isBroadcast = false;
	private Message response;


	public static int ThreadCount; // Thread 죽일때 사용하는 ThreadCount
	public static int memberCount; // 접속해있는 멤버수

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
				
				Log.i(getClass(), "현재 List 목록 : " + ServerThread.serverThreads);
				
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
	 * 전체 메시지 보내기
	 * @throws IOException 
	 */
	private void broadcastSend(Message msg) throws IOException {
		for(InputThread t :ServerThread.serverThreads) {
			t.send(msg);
		}
	}
	/**
	 *  Message를 보내는 기능
	 * @param msg
	 * @throws IOException 
	 */
	private void send(Message msg) throws IOException {
		oos.writeObject(msg);
	}
	
	
	@Override
	public String toString() {
		return "\n현재 소켓주소  : " + socket.getRemoteSocketAddress() + "\n멤버 리스트 : " + ServerThread.memberList.toString() + "\n실행되고있는 ThreadCount" + ThreadCount+"\n";
	}

}
