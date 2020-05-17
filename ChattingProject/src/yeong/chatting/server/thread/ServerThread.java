package yeong.chatting.server.thread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import yeong.chatting.model.Member;
import yeong.chatting.model.RoomInfo;

public class ServerThread extends Thread{

	private Runnable initThread;
	private ServerSocket server;
	private Socket socket;
	public static int COUNT = 0; // ThreadCount;


	private static ExecutorService threadPool;

	public static Vector<InputThread> serverThreads = new Vector<>();
	public static Vector<Member> memberList = new Vector<>();
	
	public static Map<String, RoomInfo> list = new HashMap<>();
	public static Vector<Member> roomMemberList = new Vector<>();
	public static boolean isLogout = true;
	
	public ServerThread() {
		init();
	}

	/**
	 *  ���� ������ ���� ��ư ( ��Ʈ���� )
	 */
	public void start() {
		try {
			server = new ServerSocket(9500);
			
			} catch (IOException e1) {
			e1.printStackTrace();
			if(!server.isClosed()) {
				close();
			}
		}
		threadPool = Executors.newCachedThreadPool();
		threadPool.execute(initThread);  // initThread( accept �޼ҵ� ������� ���� )
	}

	public void close() {
		try {
			for(InputThread t :serverThreads) {
				serverThreads.remove(t);
			}
			
			if(server != null && !server.isClosed()) {
				server.close();
			}
			
			if(threadPool != null && !threadPool.isShutdown())
				threadPool.shutdownNow();
			
		} catch (IOException e) {
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
						InputThread inputThread = new InputThread(socket);
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

