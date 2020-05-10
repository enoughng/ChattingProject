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
		if(toggle.getText().equals("���� ����")) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					toggle.setText("���� ����");
				}
			});
			st.start();
			Log.i(getClass(),"�������� �Ϸ�");
		} else {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					toggle.setText("���� ����");
				}
			});
			st.close();
			Log.i(getClass(),"�������� �Ϸ�");
		}
 	}
	
	@FXML
	private void exit() {
		Stage stage = (Stage)exit.getScene().getWindow();
		stage.close();
	}
	
	
}
