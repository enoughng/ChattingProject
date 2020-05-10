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
						Log.i(getClass(),"��ü ������");
						BaseModel model = (BaseModel)ois.readObject();
						Log.i("���ŵ� ���� : " + model.toString());
					}
				} catch(IOException e) {
					try {
						socket.close();
					} catch (IOException e1) {
						Log.e("������ ���� ���ϰ� ���� ���� ����");
					}
					Log.e("Ŭ���̾�Ʈ�� ������ ������ϴ�.");	
				} catch (ClassNotFoundException e) {
					Log.e(getClass(), "�ش� Ŭ������ ã�� �� �����ϴ�. ", e);
				}
			}
		};
		ServerThread.getThreadPool().execute(run);
	}

}
