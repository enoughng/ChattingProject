package yeong.chatting.client.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import yeong.chatting.client.action.ActionInfo;
import yeong.chatting.client.action.CommonAction;
import yeong.chatting.client.action.GoAction;
import yeong.chatting.client.controller.BaseController;
import yeong.chatting.client.util.alert.AlertFactory;
import yeong.chatting.client.util.alert.MyAlert;
import yeong.chatting.client.util.alert.WarningAlert;
import yeong.chatting.model.Message;
import yeong.chatting.util.CommonPathAddress;
import yeong.chatting.util.Log;

/**
 * 수신해서 가공해주는 클래스
 */
public class ClientThread implements Runnable {

	private ObjectInputStream ois;
	private Message message;
	
	private Stage primaryStage;

	public ClientThread(ObjectInputStream ois, Stage primaryStage) {
		this.ois = ois;
		this.primaryStage = primaryStage;
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
	
	
	
	/**
	 * 들어온 데이터에 대해 분류해주는 메소드
	 */
	
	private void checkProtocol() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				switch(message.getProtocol()) {
				case RESPONSE_LOGIN_SUCCESS:  
					AlertFactory.createAlert(AlertType.CONFIRMATION, "로그인 성공"); 
					GoAction.go(primaryStage, getClass().getResource(CommonPathAddress.WaitingRoomLayout));
					break;
				case RESPONSE_LOGIN_FAIL:  
					AlertFactory.createAlert(AlertType.ERROR, "아이디 또는 패스워드를 확인해주세요"); 
					break;
				case RESPONSE_REGISTRY_SUCCESS: 
					AlertFactory.createAlert(AlertType.CONFIRMATION, "회원등록 성공"); 
					break;
				case RESPONSE_REGISTRY_FAIL: 
					AlertFactory.createAlert(AlertType.ERROR, "회원등록 실패"); 
					break;
				default:
				}
			}
		});
		
	}

	/**
	 * 로그인 성공시 대기실로 이동
	 */

	

}
