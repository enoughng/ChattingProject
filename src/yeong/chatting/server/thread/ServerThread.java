package yeong.chatting.server.thread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import yeong.chatting.model.Member;
import yeong.chatting.model.Message;
import yeong.chatting.model.RoomInfo;
import yeong.chatting.server.dao.ServerDAO;
import yeong.chatting.util.Log;
import yeong.chatting.util.ProtocolType;

public class ServerThread extends Thread{

	//InputThread�� �ĺ��ϱ����� �Ű��� ����
	private int inputThreadID = 10000;
	
	private Runnable initThread;
	private ServerSocket server;
	private Socket socket;
	public static Vector<InputThread> serverThreads = new Vector<>();
	public static Vector<Member> memberList = new Vector<>();

	public static Map<Integer, Vector<Member>> roomMemberList = new HashMap<>();
	public static boolean isLogout = true;


	private ServerDAO sDao;
	private static ExecutorService threadPool;

	public ServerThread() {
		init();
		sDao = ServerDAO.getInstance();
	}

	/**
	 *  ���� ������ ���� ��ư ( ��Ʈ���� )
	 */
	public void start() {
		try {
			server = new ServerSocket(9500);

		} catch (IOException e1) {
			try {if(!server.isClosed()) {
				close();
			} } catch(NullPointerException e ) {
				Alert alert = new Alert(AlertType.ERROR, "��Ʈ�� �̹� �����ֽ��ϴ�.");
				alert.setTitle("����");
				alert.setHeaderText("����  : ");
				alert.showAndWait();
				System.exit(0);
			}
		}
		threadPool = Executors.newCachedThreadPool();
		threadPool.execute(initThread);  // initThread( accept �޼ҵ� ������� ���� )
	}

	public void close() {
		try {

			sDao.deleteRooms();
			sDao.UpdateLogoutAll();
			
			if(threadPool != null && !threadPool.isShutdown())
				threadPool.shutdownNow();

			if(server != null && !server.isClosed()) {
				server.close();
				
			}
			
			memberList.clear();
			roomMemberList.clear();
			
			for(int i=0;i<serverThreads.size();i++) {
				serverThreads.get(i).send(new Message(ProtocolType.CLOSE, new Member("Server",null,null)));
				serverThreads.remove(serverThreads.get(i));
				Log.i(serverThreads.toString());
			}
//				for(InputThread t :serverThreads) {
//				}
			




		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	public static ExecutorService getThreadPool() {
		return threadPool;
	}

	/**
	 * Ŭ���̾�Ʈ�� ����ɶ����� serverThreads�� ��Ƴ��´�.
	 */
	private void init() {
		initThread = new Runnable() {
			@Override
			public void run() {
				while(!currentThread().isInterrupted()) {
					try {
						socket = server.accept();
						InputThread inputThread = new InputThread(socket, inputThreadID++);
						serverThreads.add(inputThread);
						ServerThread.getThreadPool().execute(inputThread);
					} 
					catch (Exception e) {
						if(!server.isClosed()) {
							close();
						}
						break;
					}
				}
			}
		}; // end inputThread
	}

}

