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
 * 수신해서 가공해주는 클래스
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
	 * 들어온 데이터에 대해 분류해주는 메소드
	 */
	
	private void checkProtocol() {
		switch(message.getProtocol()) {
		case LOGIN_SUCCESS: LoginSuccess(); break;
		case LOGIN_FAIL: LoginFail(); break;
		}
	}

	/**
	 * 로그인 성공시 대기실로 이동
	 */
	private void LoginSuccess() {
		info.goAction(CommonPathAddress.ClientWaitingRoomLayout);
	}

	/**
	 * 로그인 실패시 띄우는 Alert
	 */
	private void LoginFail() {
		Alert warning = new Alert(AlertType.WARNING, "아이디 또는 비밀번호가 틀렸습니다.");
		warning.setHeaderText("로그인 실패");
		warning.show();
	}
	
	

}
