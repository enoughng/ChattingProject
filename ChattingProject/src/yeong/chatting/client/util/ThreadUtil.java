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
 * @작성자      : Yeong
 * @변경이력 :
 * @프로그램 설명 : 클라이언트에서 Input Output 스트림을 공유하기 위한 클래스
 */
public class ThreadUtil {
	
	private static ObjectInputStream ois;
	private static ObjectOutputStream oos;
	
	static {
		Log.i("ThreadUtil static 초기화자 실행");
		Thread socketThread = new Thread(new socketImpl());
		socketThread.setDaemon(false);
		socketThread.start();
		Log.i("ThreadUtil static 초기화자 완료");
	}
	
	public static ObjectInputStream getOis() {
		return ois;
	}
	
	public static ObjectOutputStream getOos() {
		return oos;
	}
	
	public void close() {
		if(ois != null) try {ois.close();} catch (IOException e) { Log.e("ois close 실패"); }
		if(oos != null) try {oos.close();} catch (IOException e) { Log.e("oos close 실패"); }
	}
	
	private static class socketImpl implements Runnable{
		private static Socket socket;
		@Override
		public void run() {
			try {
				socket = new Socket("localhost", 9500);
				oos = new ObjectOutputStream(socket.getOutputStream());
				ois = new ObjectInputStream(socket.getInputStream());
				Log.i("ThreadUtil : 3개 생성 성공");
			} catch (IOException e) {
				Log.e("ThreadUtil : 소켓생성 오류" + e.getMessage());
			}
		}
	}
	//	public Thread 
	
}
