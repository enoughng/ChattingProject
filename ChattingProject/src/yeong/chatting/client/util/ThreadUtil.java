package yeong.chatting.client.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import yeong.chatting.util.Log;

/**
 * @FileName  : ThreadUtil.java
 * @Project     : ChattingProject
 * @Date         : 2020. 5. 9.
 * @�ۼ���      : Yeong
 * @�����̷� :
 * @���α׷� ���� : Ŭ���̾�Ʈ���� Input Output ��Ʈ���� �����ϱ� ���� Ŭ����
 */
public class ThreadUtil {
	
	private static ObjectInputStream ois;
	private static ObjectOutputStream oos;
	
	static {
		Thread socketThread = new Thread(new socketImpl());
		socketThread.setDaemon(true);
		socketThread.start();
	}
	
	public static ObjectInputStream getOis() {
		return ois;
	}
	
	public static ObjectOutputStream getOos() {
		return oos;
	}
	
	public static void close() {
		if(ois != null) try {ois.close();} catch (IOException e) { Log.e("ois close ����"); }
		if(oos != null) try {oos.close();} catch (IOException e) { Log.e("oos close ����"); }
	}
	
	private static class socketImpl implements Runnable{
		private static Socket socket;
		@Override
		public void run() {
			try {
				socket = new Socket("localhost", 9500);
				oos = new ObjectOutputStream(socket.getOutputStream());
				ois = new ObjectInputStream(socket.getInputStream());
			} catch (IOException e) {
				Log.i("ThreadUtil : ������ �����ֽ��ϴ�.");
				System.exit(0);
			}
		}
	}
	//	public Thread 
	
}
