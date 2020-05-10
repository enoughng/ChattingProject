package yeong.chatting.server.thread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import yeong.chatting.model.BaseModel;
import yeong.chatting.util.Log;

public class InputThread {
	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;

	private boolean isStop = false;

	public InputThread(Socket socket) {
		this.socket = socket;
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
						Log.i(getClass(),"객체 수신중");
						BaseModel model = (BaseModel)ois.readObject();
						Log.i("수신된 문자 : " + model.toString());
					}
				} catch(IOException e) {
					try {
						socket.close();
					} catch (IOException e1) {
						Log.e("연결이 끊긴 소켓과 연결 끊기 실패");
					}
					Log.e("클라이언트와 연결이 끊겼습니다.");	
				} catch (ClassNotFoundException e) {
					Log.e(getClass(), "해당 클래스를 찾을 수 없습니다. ", e);
				}
			}
		};
		ServerThread.getThreadPool().execute(run);
	}

}
