package yeong.chatting.client.main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import yeong.chatting.client.action.CommonAction;
import yeong.chatting.client.controller.BaseController;
import yeong.chatting.client.util.CommandMap;
import yeong.chatting.client.util.ThreadUtil;
import yeong.chatting.util.CommonPathAddress;
import yeong.chatting.util.Log;

/**
 * @FileName : MainController.java
 * @Project : ChatProject
 * @Date : 2020. 5. 8.
 * @�ۼ��� : Yeong
 * @�����̷� :
 * @���α׷� ���� : ClientMain�� �α���, ȸ������ ����� �����ϴ� Ŭ����
 */
public class MainController extends BaseController {

	// �� ��Ʈ�� ����
	@FXML
	TextField idTf;
	@FXML
	PasswordField pwPf;
	@FXML
	Button loginBtn;
	@FXML
	Button exitBtn;
	@FXML
	Button registryBtn;
	
	/**
	 * ����ɶ� 1ȸ �ʱ�ȭ BaseController�� ��ӹ޾� �⺻���� Map, Stream�� �޾ƿ´�.
	 */	
//	@Override
//	public void initialize(URL location, ResourceBundle resources) {
//		initStream();
//		initCommand();
//		Log.i("map size : " + commandMap.size());
//	}
	
	
	@FXML
	public void login() {
		formAction("Login",loginBtn, CommonPathAddress.ClientWaitingRoomLayout);
	}
	
	@FXML 
	public void exit() {
		action("Exit");
	}
	
	@FXML
	public void registry() {
		formAction("Form",registryBtn, CommonPathAddress.RegistryLayout);
	}

}
