package yeong.chatting.client.registry;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import yeong.chatting.client.base.action.ActionInfo;
import yeong.chatting.client.base.controller.BaseController;
import yeong.chatting.util.Log;


/**
 * @FileName  : RegistryController.java
 * @Project     : ChattingProject
 * @Date         : 2020. 5. 10.
 * @작성자      : Yeong
 * @변경이력 :
 * @프로그램 설명 : 회원등록 Form에 대한 컨트롤러이다.
 */
public class RegistryController extends BaseController{

	// fxml 컨트롤에 대한 변수값
	@FXML private TextField id;
	@FXML private TextField email1;
	@FXML private TextField email2;
	@FXML private TextField nickname;
	@FXML private PasswordField password;
	@FXML private PasswordField passwordCheck;

	@FXML private Label pwdLb;
	@FXML private Label pwdchkLb;

	@FXML private Button idchkBtn;
	@FXML private Button registryBtn;

	private static RegistryController rCon;

	private boolean isCheckedId = false;
	private boolean passCheck = false;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		rCon = this;
		initNotHangleEvent();
		passwordCheck.setOnKeyPressed(e->{
			initPasswordField();
		});
		
		
	}

	@FXML
	private void registry() {
		if(!isCheckedId) {
			Alert alert = new Alert(AlertType.WARNING, "ID Check를 먼저해주세요");
			alert.showAndWait();
			id.requestFocus();
			return;
		}

		if(!passCheck) {
			Alert alert = new Alert(AlertType.WARNING,"비밀번호 확인란을 다시 확인해주세요");
			alert.showAndWait();
			return;
		}
		ActionInfo registryAction = new ActionInfo("Registry",registryBtn);
		registryAction.setCons(id, nickname, password, passwordCheck, email1, email2);
		action(registryAction);

//		Stage stage = (Stage)registryBtn.getScene().getWindow();
//		stage.close();
	}

	@FXML
	private void idCheck() {
		if(id.getText().trim().length()==0) {
			Alert alert = new Alert(AlertType.WARNING,"ID는 공백이 될 수 없습니다."); 
			alert.showAndWait();
			return;
		}
		ActionInfo checkAction = new ActionInfo("IDCheck", idchkBtn);
		checkAction.setCons(id);
		action(checkAction);
		
	}

	public static RegistryController getCon() {
		return rCon;
	}

	public TextField getEmailTextField() {
		return email1;
	}

	public TextField getIDField() {
		return id;
	}

	public Button getIDCheckButton() {
		return idchkBtn;
	}

	public void setisCheckedId(boolean check) {
		isCheckedId = check;
	}

	
	private void initNotHangleEvent() {
		blockHangle(id);
		blockHangle(password);
		blockHangle(passwordCheck);	
		blockHangle(email1);
	}
	
	private void blockHangle(TextInputControl tif) {
		tif.textProperty().addListener((observable, oldValue, newValue) -> {
	        if (!newValue.matches("\\sa-zA-Z0-9*")) {
	            tif.setText(newValue.replaceAll("[^\\sa-zA-Z0-9]", ""));
	        }
	    });
	}
	

	private void initPasswordField() {
		Task<String> task = new Task<String>() {
			@Override
			protected String call() throws Exception {
				Log.i(password.getText());
				Log.i(passwordCheck.getText());
				if(password.getText().equals(passwordCheck.getText())) {
					updateMessage("비밀번호 일치");
					passCheck = true;
				} else {
					updateMessage("");
					passCheck = false;
				}
				return null;
			}
		};
		pwdchkLb.textProperty().bind(task.messageProperty());
		Thread t = new Thread(task);
		t.setDaemon(true);
		t.start();
	}
}
