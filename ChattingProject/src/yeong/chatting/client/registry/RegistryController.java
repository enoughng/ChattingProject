package yeong.chatting.client.registry;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import yeong.chatting.client.action.ActionInfo;
import yeong.chatting.client.controller.BaseController;
import yeong.chatting.util.CommonPathAddress;
import yeong.chatting.util.Log;


/**
 * @FileName  : RegistryController.java
 * @Project     : ChattingProject
 * @Date         : 2020. 5. 10.
 * @�ۼ���      : Yeong
 * @�����̷� :
 * @���α׷� ���� : ȸ����� Form�� ���� ��Ʈ�ѷ��̴�.
 */
public class RegistryController extends BaseController{
	
	// fxml ��Ʈ�ѿ� ���� ������
	@FXML private TextField id;
	@FXML private TextField email;
	@FXML private TextField nickname;
	@FXML private PasswordField password;
	@FXML private PasswordField passwordCheck;
	
	@FXML private Label pwdLb;
	@FXML private Label pwdchkLb;
	
	@FXML private Button idchkBtn;
	@FXML private Button registryBtn;
	
	//�ʱ�ȭ
//	@Override
//	public void initialize(URL location, ResourceBundle resources) {
//		initStream();
//		initCommand();
//	}
	
	@FXML
	private void registry() {
		ActionInfo registryAction = new ActionInfo("Registry",registryBtn);
		registryAction.setCons(id, nickname, password, passwordCheck, email);
		action(registryAction);
		
		Stage stage = (Stage)registryBtn.getScene().getWindow();
		stage.close();
	}
	
	@FXML
	private void idCheck() {
		action(new ActionInfo("IDCheck",idchkBtn));
	}
	
}
