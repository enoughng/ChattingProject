package yeong.chatting.server.thread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import yeong.chatting.model.BaseModel;
import yeong.chatting.util.Log;

public class ServerThread extends Thread{

	private  Runnable initThread;
	private ServerSocket server;
	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;

	private static ExecutorService threadPool;

	public static ArrayList<InputThread> inputThreads= new ArrayList<>();

	public ServerThread() {
		init();
		threadPool = Executors.newCachedThreadPool();
	}

	public void start() {
		threadPool.execute(initThread);
	}

	public void close() {
		try {
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


	private void init() {
		try {
			server = new ServerSocket(9500);
			Log.i(getClass(),"서버 준비 완료");
		} catch (IOException e1) {
			if(!server.isClosed()) {
				close();
			}
		}
		initThread = new Runnable() {
			@Override
			public void run() {
				while(!currentThread().isInterrupted()) {
					try {
						socket = server.accept();
						inputThreads.add(new InputThread(socket));
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

