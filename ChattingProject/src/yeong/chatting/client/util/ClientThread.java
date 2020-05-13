package yeong.chatting.client.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import yeong.chatting.client.action.ActionInfo;
import yeong.chatting.model.Message;
import yeong.chatting.util.CommonPathAddress;
import yeong.chatting.util.Log;

/**
 * �����ؼ� �������ִ� Ŭ����
 */
public class ClientThread implements Runnable {

	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private ActionInfo info;
	private Message message;

	public ClientThread(ObjectInputStream ois, ObjectOutputStream oos, ActionInfo info) {
		this.ois = ois;
		this.oos = oos;
		this.info = info;
	}



	@Override
	public void run() {

		try {
			while(true) {
				message = (Message)ois.readObject();
				checkProtocol();
			}
		} catch (ClassNotFoundException e) {
			Log.e(getClass(), e);
		} catch (IOException e) {
			Log.e(getClass(), e);
		}

	}



	private void solveMessage(Message msg) {
		switch(msg.getProtocol()) {
		case LOGIN_SUCCESS:
			LoginSuccess();
			break;
		case LOGIN_FAIL:
			LoginFail();
			break;
		}
	}
	
	
	
	/**
	 * ���� �����Ϳ� ���� �з����ִ� �޼ҵ�
	 */
	
	private void checkProtocol() {
		switch(message.getProtocol()) {
		case LOGIN_SUCCESS: LoginSuccess(); break;
		case LOGIN_FAIL: LoginFail(); break;
		}
	}

	/**
	 * �α��� ������ ���Ƿ� �̵�
	 */
	private void LoginSuccess() {
		info.goAction(CommonPathAddress.ClientWaitingRoomLayout);
	}

	/**
	 * �α��� ���н� ���� Alert
	 */
	private void LoginFail() {
		Alert warning = new Alert(AlertType.WARNING, "���̵� �Ǵ� ��й�ȣ�� Ʋ�Ƚ��ϴ�.");
		warning.setHeaderText("�α��� ����");
		warning.show();
	}
	
	

}
