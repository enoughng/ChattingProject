package yeong.chat.client.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * @FileName  : MainController.java
 * @Project     : ChatProject
 * @Date         : 2020. 5. 8.
 * @작성자      : Yeong
 * @변경이력 : 
 * @프로그램 설명 : ClientMain에 대한 기능들을 부여하는 클래스
 */
public class MainController implements CommomController {
	
	//각 컨트롤 정의
	@FXML TextField idTf;
	@FXML PasswordField pwPf;
	@FXML Button loginBtn;
	@FXML Button exitBtn;
	
	
	/**
	 * 실행될때 1회 초기화
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	
	
	
}
