package yeong.chatting.server.cotroller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
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
	
	@FXML TextArea log;
	@FXML Button toggle;
	@FXML Button exit;
	
	@FXML
	private void toggle() {
		if(toggle.getText().equals("서버 구동")) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					toggle.setText("서버 종료");
				}
			});
			st.start();
			Log.i(getClass(),"서버구동 완료");
		} else {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					toggle.setText("서버 구동");
				}
			});
			st.close();
			Log.i(getClass(),"서버종료 완료");
		}
 	}
	
	@FXML
	private void exit() {
		Stage stage = (Stage)exit.getScene().getWindow();
		stage.close();
	}
	
	
}
