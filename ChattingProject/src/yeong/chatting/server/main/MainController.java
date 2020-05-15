package yeong.chatting.server.main;

import java.io.FileInputStream;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import yeong.chatting.client.action.ActionInfo;
import yeong.chatting.server.cotroller.BaseController;
import yeong.chatting.util.Log;

/**
 * @FileName  : MainController.java
 * @Project     : ChattingProject
 * @Date         : 2020. 5. 10.
 * @�ۼ���      : Yeong
 * @�����̷� :
 * @���α׷� ���� : ������ �����ϴ� MAIN FXML�� ��ư�� �޼��带 ���ν����ش�.
 */
public class MainController extends BaseController {
	
	private static MainController controller;
	
	
	@FXML TextArea log;
	@FXML Button toggle;
	@FXML Button exit;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		controller = this;
	}
	
	@FXML
	private void toggle() {
		ActionInfo start= new ActionInfo("StartButton", toggle);
		start.setCons(log, toggle, exit);
		action(start);
 	}
	
	@FXML
	private void exit() {
		System.exit(0);
	}
	
	
	public TextArea getLog() {
		return log;
	}
	
	public static MainController getMainController() {
		return controller;
	}
	
}
