package yeong.chatting.server.main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import yeong.chatting.client.base.action.ActionInfo;
import yeong.chatting.server.cotroller.BaseController;
import yeong.chatting.server.dao.ServerDAO;

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
	@FXML Button save;
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
	
	@FXML
	private void saveLog() {
		FileChooser chooser = new FileChooser();
		chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text files", ".txt"));
		File file = chooser.showSaveDialog(save.getScene().getWindow());
		
		
		if( file != null) {
			saveFile(file);
		}
		
	}
	
	private void saveFile(File file) {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new FileWriter(file),true);
			writer.println(log.getText().replace("\n", "\r\n"));
			writer.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public TextArea getLog() {
		return log;
	}
	
	public static MainController getMainController() {
		return controller;
	}
	
}
