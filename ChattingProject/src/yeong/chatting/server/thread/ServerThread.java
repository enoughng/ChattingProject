package yeong.chatting.server.thread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import yeong.chatting.util.Log;

public class ServerThread extends Thread{

	private Runnable initThread;
	private ServerSocket server;
	private Socket socket;
	public static int COUNT = 0; // ThreadCount;


	private static ExecutorService threadPool;

	public static Vector<InputThread> serverThreads = new Vector<>();

	public ServerThread() {
		init();
	}

	/**
	 *  서버 쓰레드 시작 버튼 ( 포트열기 )
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
		threadPool.execute(initThread);  // initThread( accept 메소드 쓰레드로 실행 )
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
	 * 클라이언트가 연결될때마다 serverThreads에 담아놓는다.
	 */
	private void init() {
		initThread = new Runnable() {
			@Override
			public synchronized void run() {
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

