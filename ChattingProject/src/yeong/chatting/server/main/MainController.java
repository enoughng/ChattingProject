package yeong.chatting.server.main;

import java.io.FileInputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import yeong.chatting.client.action.ActionInfo;
import yeong.chatting.server.cotroller.BaseController;
import yeong.chatting.server.dao.CommonDao;
import yeong.chatting.server.dao.ServerDAO;
import yeong.chatting.util.Log;

/**
 * @FileName  : MainController.java
 * @Project     : ChattingProject
 * @Date         : 2020. 5. 10.
 * @작성자      : Yeong
 * @변경이력 :
 * @프로그램 설명 : 서버를 구동하는 MAIN FXML에 버튼과 메서드를 맵핑시켜준다.
 */
public class MainController extends BaseController {
	
	private static MainController controller;
	
	
	@FXML TextArea log;
	@FXML Button toggle;
	@FXML Button exit;
	// 커넥션을 닫기위한 DAO
	private ServerDAO dao;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		controller = this;
		dao = ServerDAO.getInstance();
		try {
			dao.deleteRooms();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@FXML
	private void toggle() {
		ActionInfo start= new ActionInfo("StartButton", toggle);
		start.setCons(log, toggle, exit);
		action(start);
 	}
	
	@FXML
	private void exit() {
		if(toggle.getText().equals("서버 중지"))
			dao.closeConnection();
		System.exit(0);
	}
	
	
	public TextArea getLog() {
		return log;
	}
	
	public static MainController getMainController() {
		return controller;
	}
	
}
