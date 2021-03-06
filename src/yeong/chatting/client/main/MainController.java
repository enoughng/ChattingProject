package yeong.chatting.client.main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.jws.soap.InitParam;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import yeong.chatting.client.base.action.ActionInfo;
import yeong.chatting.client.base.action.CommonAction;
import yeong.chatting.client.base.controller.BaseController;
import yeong.chatting.client.thread.ThreadUtil;
import yeong.chatting.client.util.CommandMap;
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

	@FXML
	GridPane main;
	
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
	Button search;
	
	
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
	
	@FXML
	public void searchUser() {
		action(new ActionInfo("Form",search,CommonPathAddress.SearchLayout));
	}
	
	@Override
	protected void initEvent() {
		event.nextTextField(idTf, pwPf);
		event.fireEvent(pwPf, loginBtn);
	}

}
