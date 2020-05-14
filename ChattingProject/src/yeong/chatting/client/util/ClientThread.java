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
 * �����ؼ� �������ִ� Ŭ����
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
	 * ���� �����Ϳ� ���� �з����ִ� �޼ҵ�
	 */
	
	private void checkProtocol() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				switch(message.getProtocol()) {
				case RESPONSE_LOGIN_SUCCESS:  
					AlertFactory.createAlert(AlertType.CONFIRMATION, "�α��� ����"); 
					GoAction.go(primaryStage, getClass().getResource(CommonPathAddress.WaitingRoomLayout));
					break;
				case RESPONSE_LOGIN_FAIL:  
					AlertFactory.createAlert(AlertType.ERROR, "���̵� �Ǵ� �н����带 Ȯ�����ּ���"); 
					break;
				case RESPONSE_REGISTRY_SUCCESS: 
					AlertFactory.createAlert(AlertType.CONFIRMATION, "ȸ����� ����"); 
					break;
				case RESPONSE_REGISTRY_FAIL: 
					AlertFactory.createAlert(AlertType.ERROR, "ȸ����� ����"); 
					break;
				default:
				}
			}
		});
		
	}

	/**
	 * �α��� ������ ���Ƿ� �̵�
	 */

	

}
