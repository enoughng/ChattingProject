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
import yeong.chatting.client.action.ActionInfo;
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
 * @작성자 : Yeong
 * @변경이력 :
 * @프로그램 설명 : ClientMain의 로그인, 회원가입 기능을 구현하는 클래스
 */
public class MainController extends BaseController {

	// 각 컨트롤 정의
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

	
	@FXML
	public void login() {
		ActionInfo loginAction = new ActionInfo("Login",loginBtn);
		loginAction.setCons(idTf, pwPf);
		action(loginAction);
	}
	
	@FXML 
	public void exit() {
		action(new ActionInfo("Exit",exitBtn));
	}
	
	@FXML
	public void registry() {
		action(new ActionInfo("Form",registryBtn,CommonPathAddress.RegistryLayout));
	}

}
