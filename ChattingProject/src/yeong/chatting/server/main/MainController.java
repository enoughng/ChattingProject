package yeong.chatting.server.main;

import java.io.FileInputStream;

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
	
	@FXML TextArea log;
	@FXML Button toggle;
	@FXML Button exit;
	
	@FXML
	private void toggle() {
		ActionInfo start= new ActionInfo("StartButton", toggle);
		start.setCons(log, toggle, exit);
		action(start);
 	}
	
	@FXML
	private void exit() {
		Stage stage = (Stage)exit.getScene().getWindow();
		stage.close();
	}
	
	
}
